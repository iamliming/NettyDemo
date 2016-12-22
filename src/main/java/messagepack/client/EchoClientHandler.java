package messagepack.client;

import java.util.ArrayList;
import java.util.List;

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
public class EchoClientHandler extends ChannelHandlerAdapter
{
    private int num;

    public EchoClientHandler(int num)
    {
        this.num = num;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        List<Person> persons = new ArrayList<Person>();
        for(int i = 0 ; i < num ; i++)
        {
            Person person = new Person((int)(Math.random()*100),"abc",(float)(Math.random()*100000));
            persons.add(person);
        }
        Person person = new Person((int)(Math.random()*100),"abc",(float)(Math.random()*100000));
        ctx.writeAndFlush(person);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        System.out.println("Client read msg:"+msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.out.println("server exception catch");
        ctx.close();
    }
}
