package netty.time.client;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十一月 18, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeClientHandler extends ChannelOutboundHandlerAdapter
{
    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
        ChannelPromise promise)
        throws Exception
    {
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
        throws Exception
    {
        super.write(ctx, msg, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx)
        throws Exception
    {
        super.read(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx)
        throws Exception
    {
        super.flush(ctx);
    }
}
