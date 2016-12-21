package netty.time.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 定长解决粘包问题
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeClientHandler3 extends ChannelHandlerAdapter
{
    private int counter;
    private byte[] req;

    public TimeClientHandler3()
        throws UnsupportedEncodingException
    {
        req = ("abcd"+ System.lineSeparator()).getBytes("UTF-8");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        ByteBuf byteBuf = null;
        for(int i = 0; i < 100; i++)
        {
            byteBuf = Unpooled.buffer(req.length);
            byteBuf.writeBytes(req);
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        /*ByteBuf req = (ByteBuf)msg;
        byte[] message = new byte[req.readableBytes()];
        req.readBytes(message);
        String body = new String(message, "UTF-8");*/
        String body = (String)msg;
        System.out.println(body);
        System.out.println("client:"+(++counter));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        super.exceptionCaught(ctx, cause);
    }
}
