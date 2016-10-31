package netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十月 28, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NioServer
{
    //通道管理器
    private Selector selector;

    public NioServer init(int port)
        throws IOException
    {
        //获取ServerSocket通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        //获取通道管理器
        selector = Selector.open();
        //将通道注册到通道管理器，事件类型为SelectionKey.OP_ACCEPT事件
        channel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listen()
        throws IOException
    {
        System.out.println("服务器启动中...");
        //轮询处理器
        while (true)
        {
            //当有注册的事件到达时，方法返回，否则阻塞
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys)
            {
                //连接请求事件
                if (key.isAcceptable())
                {
                    System.out.println("客户端 ==连接==请求事件");
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    //获取连接通道
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    //向客户端发请求
                    channel.write(ByteBuffer.wrap("send message from server".getBytes()));
                    selectionKeys.remove(key);
                    channel.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable())
                {
                    System.out.println("读取客户端==read==消息事件");
                    //获取客户端传输消息通道
                    SocketChannel socket = (SocketChannel)key.channel();
                    //设置读取ByteBuffer
                    ByteBuffer buffer = ByteBuffer.allocate(40);
                    long read = -1;
                    read = socket.read(buffer);
                    byte[] bytes = buffer.array();
                    String message = new String(bytes);
                    selectionKeys.remove(key);
                    System.out.println("server receive message:" + message);
                }
            }

        }
    }

    public static void main(String[] args)
        throws IOException
    {
        new NioServer().init(8088).listen();

    }
}
