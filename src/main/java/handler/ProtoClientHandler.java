package handler;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import protobuf.SubscribeReqProto;
import protobuf.SubscribeRespProto;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 22, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProtoClientHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        SubscribeRespProto.SubscribeResp req = (SubscribeRespProto.SubscribeResp)msg;
        int id = req.getSubReqID();
        System.out.println("client receive id:"+id+"code:"+req.getRespCode()+"desc:"+req.getDesc());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        for(int i = 0; i< 10; i++)
        {
            SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
            builder.setSubReqID(i);
            builder.setUserName("Linlinfei");
            builder.setProductName("mingtian");
            List<String> add = new ArrayList<String>();
            add.add("ab");
            add.add("bc");
            add.add("ed");
            builder.addAllAddress(add);
            SubscribeReqProto.SubscribeReq req = builder.build();
            ctx.write(req);
        }
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
