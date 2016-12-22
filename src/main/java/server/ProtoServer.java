package server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import handler.ProtoServerHandler;
import protobuf.SubscribeReqProto;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProtoServer
{
    private int port;

    public void bind(int port)
        throws InterruptedException
    {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try
        {
            serverBootstrap.group(parent, child)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new ProtoServerHandler());
                    }
                });
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }
    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new ProtoServer().bind(8888);
    }
}
