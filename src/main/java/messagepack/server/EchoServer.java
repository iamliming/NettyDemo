package messagepack.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class EchoServer
{
    private int port;

    public void bind(int port)
        throws InterruptedException
    {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parent, child)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch)
                    throws Exception
                {
                    ch.pipeline().addLast("msg decoder", new MsgpackDecoder());
                    ch.pipeline().addLast("msg encoder", new MsgpackEncoder());
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(port)).sync();
        future.channel().closeFuture().sync();

    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new EchoServer().bind(8888);
    }
}
