package netty.time.server;

import java.io.UnsupportedEncodingException;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 定长解决拆包、粘包问题
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeServerHandler3 extends ChannelHandlerAdapter
{
    private int counter;

    public void channelRead(ChannelHandlerContext context, Object msg)
        throws UnsupportedEncodingException
    {
        /*ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[(byteBuf.readableBytes())];
        byteBuf.readBytes(bytes);
        String body = new String(bytes, "UTF-8").substring(0, bytes.length - System.lineSeparator().length());*/
        String body = (String)msg;
        System.out.println("recieve times:" + (++counter) + "{}"+body);
        /*String currentTime = ((body.equalsIgnoreCase("abcd") ?
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) :
            "BAD ORDER")) + System.lineSeparator();
        ByteBuf rsp = Unpooled.copiedBuffer(currentTime.getBytes());
        context.writeAndFlush(rsp);*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.out.println("server exception catch");
        ctx.close();
    }
}
