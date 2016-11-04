package blockio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十月 28, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class TimeClient
{
    public static void main(String[] args)
    {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        try
        {
            socket = new Socket("127.0.0.1",8081);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            System.out.println("send");
            writer.println("hello");
            System.out.println("finish");
            String rsp = reader.readLine();
            System.out.println(rsp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
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
                try
                {
                    writer.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
