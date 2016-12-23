package handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十二月 23, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{

    private String url;

    public HttpFileServerHandler(String url)
    {
        this.url = url;
    }

    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request)
        throws Exception
    {
        if (!request.getDecoderResult().isSuccess())
        {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (request.getMethod() != HttpMethod.GET)
        {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }
        String path = sanitizerUri(request.getUri());
        if (path == null)
        {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists())
        {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        if (file.isDirectory())
        {
            if (request.getUri().endsWith("/"))
            {
                sendListing(ctx, file);
            }
            else
            {
                sendRedirect(ctx, request.getUri() + '/');
            }
            return;
        }
        if (file.isFile())
        {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            long fileLen = f.length();
            HttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, OK);
            setContentLength(rsp, fileLen);
            setContentTypeHeader(rsp, file);
            if (isKeepAlive(request))
            {
                rsp.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(rsp);
            ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(f, 0, fileLen, 8192), ctx.newProgressivePromise());
            sendFileFuture.addListener(new ChannelProgressiveFutureListener()
            {
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
                    throws Exception
                {
                    if (total < 0)
                    {
                        System.err.println("Transfer progress:" + progress);
                    }
                    else
                    {
                        System.err.println("Transfer progress:" + progress + "/" + total);
                    }
                }

                public void operationComplete(ChannelProgressiveFuture future)
                    throws Exception
                {
                    System.out.println("complete");
                }
            });
            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!isKeepAlive(request))
            {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void setContentTypeHeader(HttpResponse response, File file)
    {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }

    private void sendRedirect(ChannelHandlerContext ctx, String s)
    {
        FullHttpResponse rsp = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND);
        rsp.headers().set(LOCATION, s);
        ctx.writeAndFlush(rsp).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendListing(ChannelHandlerContext ctx, File dir)
    {
        FullHttpResponse rsp = new DefaultFullHttpResponse(HTTP_1_1, OK);
        rsp.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder builder = new StringBuilder();
        String dirPath = dir.getPath();
        builder.append("<!DOCTYPE html>\r\n");
        builder.append("<html><head><title>");
        builder.append(dirPath);
        builder.append(" 目录：");
        builder.append("</title></head><body>\r\n");
        builder.append("<h3>");
        builder.append(dirPath).append(" 目录：");
        builder.append("</h3>\r\n");
        builder.append("<ul>");
        builder.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        for (File f : dir.listFiles())
        {
            if (f.isHidden())
            {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches())
            {
                continue;
            }
            builder.append("<li> 链接:<a href=\"");
            builder.append(name);
            builder.append("\">");
            builder.append(name);
            builder.append("</a></li>\r\n");
        }
        builder.append("</ul></body></html>\r\n");
        ByteBuf buf = Unpooled.copiedBuffer(builder, CharsetUtil.UTF_8);
        rsp.content().writeBytes(buf);
        buf.release();
        ctx.writeAndFlush(rsp).addListener(ChannelFutureListener.CLOSE);
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern
        .compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private static void sendError(ChannelHandlerContext ctx,
        HttpResponseStatus status)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
            status, Unpooled.copiedBuffer("Failure: " + status.toString()
            + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private String sanitizerUri(String uri)
    {
        try
        {
            uri = URLDecoder.decode(uri, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            try
            {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            }
            catch (UnsupportedEncodingException e1)
            {
                throw new Error();
            }
        }
        if (!uri.startsWith(url))
        {
            return null;
        }
        if (!uri.startsWith("/"))
        {
            return null;
        }
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + '.')
            || uri.contains('.' + File.separator) || uri.startsWith(".")
            || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches())
        {
            return null;
        }
        return System.getProperty("user.dir") + File.separator + uri;
    }
}
