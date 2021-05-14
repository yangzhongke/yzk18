import com.yzk18.commons.IOHelpers;

import java.io.File;
import java.util.Arrays;

public class 案例7mp4拷到新文件夹下 {
    public static void main(String[] args) {
        File file = new File("E:/test");
        File[] dirs = file.listFiles();
        for(File dir : dirs)//先遍历文件夹
        {
            for(File fileMp4 : dir.listFiles())//再遍历文件夹下的文件
            {
                //System.out.println(fileMp4);
                String filePath = fileMp4.getPath();//把File转换为字符串，方便用split等方法
                //System.out.println(filePath);
                filePath = filePath.replace("\\","/");//把\替换为/，
                //// 这样无论是Windows还是其他操作系统，这样路径分隔符都同意为/
                String[] strs = filePath.split("/");
                //System.out.println(Arrays.toString(strs));
                String folderName = strs[strs.length-2];//倒数第1个是文件夹的名字
                String fileName = strs[strs.length-1];//倒数第0个是文件的名字
                String outputFileName = "e:/temp/output/"+folderName+"-"+fileName;//目标文件名
                //System.out.println(outputFileName);
                byte[] bytes = IOHelpers.readAllBytes(fileMp4);//文件内容
                IOHelpers.writeAllBytes(outputFileName,bytes);//写入新文件
            }
        }
    }

}
