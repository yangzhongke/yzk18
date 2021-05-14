import com.yzk18.GUI.GUI;
import com.yzk18.commons.IOHelpers;

import java.time.LocalDate;
import java.util.Arrays;

public class GUITest1 {
    public static void main(String[] args) {
        /*
        String s = GUI.buttonsBox("你好","张三","李四","杨中科","xxx","yyy");
        System.out.println(s);*/
        /*
        String s = GUI.choiceBox("你好",new String[]{"张三","李四","杨中科","xxx","yyy"});
        System.out.println(s);*/
        //LocalDate d1 =  GUI.dateBox("请选择你的生日");//这些对话框是“模态”对话框，也就是窗口关闭之后，才继续往下执行
        /*
        LocalDate d1 =  GUI.dateBox("请选择你的生日",LocalDate.of(2008,8,8));
        System.out.println(d1);*/
        /*
        String s = GUI.dirOpenBox("请选择打开文件夹");
        System.out.println(s);*/
        /*
        String s = GUI.dirSaveBox("请选择保存文件夹");
        System.out.println(s);*/
/*
        double d = GUI.doubleBox("请输入体重",8.8);
        System.out.println(d);*/
        //GUI.errorBox("出错啦");
        /*
        String s = GUI.fileOpenBox("请选择一个ppt","ppt","pptx");
        System.out.println(s);*/
        //System.out.println(GUI.getScreenWidth()+"-"+ GUI.getScreenHeight());
        /*
        String s = GUI.imgBox("图片美吗？","E:\\temp\\xcc\\a.jpg","太美了","丑死了");
        System.out.println(s);*/
        /*
        String s = GUI.inputBox("您的姓名是？","无名氏");
        System.out.println(s);*/
        /*
        String[] strs = GUI.loginBox("请登录");//登录  登陆    账号 帐号
        System.out.println(Arrays.toString(strs));*/
       // GUI.msgBox("完成");
        /*
       String[] strs =  GUI.multiInputBox("请输入详细信息","姓名","籍贯","年龄","生日");
        System.out.println(Arrays.toString(strs));*/
        /*
        boolean b = GUI.okCancelBox("你是个人吗？");
        System.out.println(b);*/
        /*
        GUI.showIndeterminateProgressDialog("正在导入");//非模态对话框，不用等对话框关闭，代码就继续往后运行
        for(int i=0;i<1;i++)
        {
            byte[] bytes = IOHelpers.readAllBytes("E:\\temp\\output\\Part1-1-学编程前要搞明白的问题.mp4");
            IOHelpers.writeAllBytes("E:\\temp\\output\\666.mp4",bytes);
        }
        GUI.closeIndeterminateProgressDialog();
        GUI.msgBox("导入完成");*/

        for(int i=0;i<6000;i++)
        {
            GUI.showProgressDialog("正在导入第"+i+"次",6000,i);
            byte[] bytes = IOHelpers.readAllBytes("E:\\temp\\xcc\\DsoFramer.zip");
            IOHelpers.writeAllBytes("E:\\temp\\output\\666.zip",bytes);
        }
        GUI.closeProgressDialog();
        GUI.msgBox("导入成功");
        //Java：大部分情况，main函数执行结束了程序就退出了
    }
}
