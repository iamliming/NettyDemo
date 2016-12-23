package server;

import handler.HttpFileServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 23, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HttpFileServer
{
    public static final String URL = "/src/main/java/";
    public void bind(int port)
        throws InterruptedException
    {
        NioEventLoopGroup parent = new NioEventLoopGroup();
        NioEventLoopGroup child = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        try
        {
            server.group(parent, child).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    protected void initChannel(SocketChannel socketChannel)
                    {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpRequestDecoder());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(new HttpResponseEncoder());
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new HttpFileServerHandler(URL));
                    }
                });
            ChannelFuture future = server.bind(port).sync();
            future.channel().closeFuture().sync();
//                .bind(port)
        }
        finally
        {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }
    }

    public static void main(String[] args)
        throws InterruptedException
    {
        new HttpFileServer().bind(8080);
    }
}
