package messagepack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>
{
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
        ByteBuf byteBuf, List<Object> objects)
        throws Exception
    {
        //final byte[] arr;
        //final int len = byteBuf.readableBytes();
        //arr = new byte[len];
        ////byteBuf.readBytes(arr);
        //byteBuf.getBytes(byteBuf.readableBytes(), arr, 0, len);
        //MessagePack msgPack = new MessagePack();
        //objects.add(msgPack.read(arr));

        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readableBytes(), array, 0, length);
        MessagePack msgPack = new MessagePack();
        objects.add(msgPack.read(array));
    }
}
