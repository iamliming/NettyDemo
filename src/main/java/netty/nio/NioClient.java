package netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十月 31, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NioClient
{
    private Selector selector;
    public NioClient init(int port) throws IOException
    {
        //Socket Channel
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(port));
        //Selector 多路分发器
        selector = Selector.open();
        //regester
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen()
        throws IOException
    {
        System.out.println("客户端启动");
        while(true)
        {
            //阻塞获取到连接
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys)
            {
                //连接请求事件
                if (key.isConnectable())
                {
                    System.out.println("客户端已经===连接===到服务段");
                    SocketChannel channel = (SocketChannel)key.channel();
                    //如果正在连接，则完成连接
                    if(channel.isConnectionPending())
                    {
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    //发送消息
                    channel.write(ByteBuffer.wrap("hello, this message from client!".getBytes()));
//                    selectionKeys.remove(key);
                    channel.register(selector, SelectionKey.OP_READ);
                }
                else if(key.isReadable())
                {
                    System.out.println("===读取===服务端消息事件");
                    //获取客户端传输消息通道
                    SocketChannel socket = (SocketChannel)key.channel();
                    //设置读取ByteBuffer
                    ByteBuffer buffer = ByteBuffer.allocate(40);
                    long read = -1;
                    read = socket.read(buffer);
                    byte[] bytes = buffer.array();
                    String message = new String(bytes);
//                    selectionKeys.remove(key);
                    System.out.println("client receive message:" + message);
                }
            }
        }
    }

    public static void main(String[] args)
        throws IOException
    {
        new NioClient().init(8088).listen();
    }
}
