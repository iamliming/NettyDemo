package netty.time.server;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeServerHandler1 extends ChannelHandlerAdapter
{
    private int counter;

    public void channelRead(ChannelHandlerContext context, Object msg)
        throws UnsupportedEncodingException
    {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[(byteBuf.readableBytes())];
        byteBuf.readBytes(bytes);
        String body = new String(bytes, "UTF-8").substring(0, bytes.length - System.lineSeparator().length());
        System.out.println("recieve times:" + (++counter));
        String currentTIme = ((body.equalsIgnoreCase("abcd") ?
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) :
            "BAD ORDER")) + System.lineSeparator();
        ByteBuf rsp = Unpooled.copiedBuffer(currentTIme.getBytes());
        context.writeAndFlush(rsp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.out.println("server exception catch");
        ctx.close();
    }
}
