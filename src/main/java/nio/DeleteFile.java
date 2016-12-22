package nio;

import java.io.File;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 十一月 22, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DeleteFile
{
    public void deleteFile(File file)
    {
        //不是文件夹
        if (!file.isDirectory())
        {
            file.delete();
        }
        //是文件夹
        else
        {
            String[] s = file.list();
            String path = file.getPath();
            for (String ss : s)
            {
                deleteFile(new File(path + "\\" + ss));
            }
            file.delete();
        }
    }

    public static void main(String[] args)
    {
        DeleteFile deleteFile = new DeleteFile();
        File file = new File("E:\\svn\\sns\\trunk\\snsserver\\out\\artifacts\\expServer_web");
        deleteFile.deleteFile(file);
    }
}
