package netty.blockio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十月 28, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeServer
{
    public static void main(String[] args)
    {
        int port = 8081;
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            Socket socket = null;
            while(true)
            {
                socket = server.accept();
                System.out.println("start");
                new Thread(new TimeServerHandler(socket)).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class TimeServerHandler implements Runnable
{
    private Socket socket;

    public TimeServerHandler(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String body = null;
            while(true)
            {
                body = reader.readLine();
                if (body == null)
                {
                    break;
                }
                writer.println(body + System.currentTimeMillis());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(writer != null)
            {
                writer.close();
            }
            if(socket != null)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
