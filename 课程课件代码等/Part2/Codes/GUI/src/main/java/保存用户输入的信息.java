import com.yzk18.GUI.GUI;
import com.yzk18.commons.IOHelpers;

public class 保存用户输入的信息 {
    public static void main(String[] args) {
        String dir = GUI.dirSaveBox("选择保存的文件夹");
       /*
        String[] strs = GUI.multiInputBox("请输入学生信息","姓名","手机号","家庭地址");
        String name  = strs[0];
        String phone  = strs[1];
        String addr = strs[2];
        String content = "姓名："+name+"，电话："+phone+"，地址："+addr;
        IOHelpers.writeAllText(dir+"/"+name+".txt",content);`
        while(GUI.yesNoBox("要继续吗？"))
        {
            strs = GUI.multiInputBox("请输入学生信息","姓名","手机号","家庭地址");
            name  = strs[0];
            phone  = strs[1];
            addr = strs[2];
            content = "姓名："+name+"，电话："+phone+"，地址："+addr;
            IOHelpers.writeAllText(dir+"/"+name+".txt",content);
        }*/
        do {
            String[] strs = GUI.multiInputBox("请输入学生信息","姓名","手机号","家庭地址");
            String name  = strs[0];
            String phone  = strs[1];
            String addr = strs[2];
            String content = "姓名："+name+"，电话："+phone+"，地址："+addr;
            IOHelpers.writeAllText(dir+"/"+name+".txt",content);
        }while(GUI.yesNoBox("要继续吗？"));
    }
}
