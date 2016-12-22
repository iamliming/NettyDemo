package messagepack.client;

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
        for(int i = 0 ; i < num ; i++)
        {
            Person person = new Person((int)(Math.random()*100),"abc",(float)(Math.random()*100000));
            ctx.write(person);
            ctx.flush();
            System.out.println(i);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        System.out.println("Client read msg:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.out.println("server exception catch");
        ctx.close();
    }
}
