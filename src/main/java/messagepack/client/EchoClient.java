package messagepack.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import messagepack.MsgpackDecoder;
import messagepack.MsgpackEncoder;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EchoClient
{
    public void connect(int port)
        throws InterruptedException
    {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch)
                        throws Exception
                    {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        pipeline.addLast(new MsgpackDecoder());
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new MsgpackEncoder());
                        pipeline.addLast(new EchoClientHandler(1000));
                    }
                });
            ChannelFuture future = bootstrap.connect("127.0.0.1", port).sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new EchoClient().connect(8888);
    }
}
