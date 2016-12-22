package messagepack.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messagepack.Person;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 21, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EchoServerHandler extends ChannelHandlerAdapter
{
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        Person persons = (Person)msg;
        System.out.println(persons.toString() + (++count));
        ctx.writeAndFlush("server read msg:");
        /*for(Person person : persons)
        {
            System.out.println(person);
            ctx.writeAndFlush("server read msg:" + person.getAge());
        }*/
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        cause.printStackTrace();
        System.out.println("server exception catch");
        ctx.close();
    }
}
