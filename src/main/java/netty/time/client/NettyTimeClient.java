package netty.time.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import messagepack.MsgpackDecoder;
import messagepack.MsgpackEncoder;
import messagepack.client.EchoClientHandler;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十一月 18, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NettyTimeClient
{
    public void connect(int port, String host)
        throws InterruptedException
    {
        NioEventLoopGroup connectGroup = new NioEventLoopGroup();
        try
        {
            Bootstrap strap = new Bootstrap();
            strap.group(connectGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new TimeClientChannel());

            ChannelFuture future = strap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            connectGroup.shutdownGracefully();
        }
    }

    private class TimeClientChannel extends ChannelInitializer<SocketChannel>
    {

        @Override
        protected void initChannel(SocketChannel ch)
            throws Exception
        {
//            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//            ByteBuf delimit = Unpooled.copiedBuffer("_$".getBytes());
//            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimit));
//            ch.pipeline().addLast(new StringDecoder());
//            ch.pipeline().addLast(new TimeClientHandler2());
            ch.pipeline().addLast(new MsgpackDecoder());
            ch.pipeline().addLast(new MsgpackEncoder());
            ch.pipeline().addLast(new EchoClientHandler(2));
        }
    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new NettyTimeClient().connect(8082, "127.0.0.1");
    }
}
