package messagepack;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MsgpackEncoder extends MessageToByteEncoder<Person>
{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Person o, ByteBuf byteBuf)
        throws Exception
    {
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(o);
        byteBuf.writeBytes(raw);
        System.out.println("encoder");
    }
}
