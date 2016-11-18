package netty.time.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                .handler(new TimeClientHandler());

            strap.connect(host, port).sync();
        }
        finally
        {
            connectGroup.shutdownGracefully();
        }
    }

    private class TimeClientChannel extends ChannelInitializer
    {
        @Override
        protected void initChannel(Channel ch)
            throws Exception
        {
            ch.pipeline().addLast(new TimeClientHandler());
        }
    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new NettyTimeClient().connect(8081, "127.0.0.1");
    }
}