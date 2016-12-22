package netty.time.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import messagepack.MsgpackDecoder;
import messagepack.MsgpackEncoder;
import messagepack.server.EchoServerHandler;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十一月 18, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NettyTimeServer
{
    public void bind(int port)
    {

            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());

            ChannelFuture fu = bootstrap.bind(new InetSocketAddress(port)).sync();
            fu.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>
    {
        @Override
        protected void initChannel(SocketChannel ch)
            throws Exception
        {
//            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//            ByteBuf delimit = Unpooled.copiedBuffer("_$".getBytes());
//            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimit));
//            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
//            ch.pipeline().addLast(new StringDecoder());
//            ch.pipeline().addLast(new TimeServerHandler3());
            ch.pipeline().addLast(new MsgpackDecoder());
            ch.pipeline().addLast(new MsgpackEncoder());
            ch.pipeline().addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args)
    {
        new NettyTimeServer().bind(8082);
    }
}
