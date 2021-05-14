import com.yzk18.GUI.GUI;
import com.yzk18.commons.IOHelpers;

import java.io.File;
import java.util.Arrays;

public class 文件夹拷贝 {
    public static void main(String[] args) {
        String 源文件夹 = GUI.dirOpenBox("选择源文件夹");
        String destDir = GUI.dirSaveBox("选择目标文件夹");
        //递归
        File srcFile = new File(源文件夹);
        File[] files = srcFile.listFiles();
        /*
        for(int i=0;i<files.length;i++)
        //for(File file : srcFile.listFiles())
        {
            File file = files[i];
            if(file.isDirectory())
            {
                continue;
            }
            GUI.showProgressDialog("正在拷贝"+file.getName(),i,files.length);
            String destFileName = destDir+"/"+file.getName();
            byte[] bytes = IOHelpers.readAllBytes(file);
            IOHelpers.writeAllBytes(destFileName,bytes);
        }*/
        int i=0;
        for(File file : srcFile.listFiles())
        {
            if(file.isDirectory())
            {
                continue;
            }
            GUI.showProgressDialog("正在拷贝"+file.getName(),i,files.length);
            String destFileName = destDir+"/"+file.getName();
            byte[] bytes = IOHelpers.readAllBytes(file);
            IOHelpers.writeAllBytes(destFileName,bytes);
            i++;
        }
        GUI.closeProgressDialog();
        GUI.msgBox("搞定");
       // String[] files = IOHelpers.getFilesRecursively(srcDir,);
        //System.out.println(Arrays.toString(files));
    }
}
