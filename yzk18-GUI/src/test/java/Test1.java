import com.yzk18.GUI.GUI;
import com.yzk18.commons.CommonHelpers;

import java.time.LocalDate;
import java.util.*;

public class Test1 {
    public static void main(String[] args)  {
        //GUI.msgBox("hello");
       // GUI.msgBox("hello","提示");
        //int age = GUI.intBox("xx");
        /*
        ArrayList<String> items = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            items.add("北京"+i);
            items.add("山东"+i);
            items.add("深圳"+i);
        }
        //GUI.choiceBox("请选择你的城市",items);
        String[] values = GUI.multiInputBox("xxx",items,null);
        GUI.msgBox(values);
        CommonHelpers.println(values);*/
        /*
        GUI.multiInputBox("xw","身高","体重");
        String[] values1 = GUI.multiInputBox("xx",new String[]{"姓名","年龄"});
        GUI.msgBox(values1);
        String[] values2 = GUI.multiInputBox("xx",new String[]{"姓名","年龄"},new Object[]{"yzk"});
        GUI.msgBox(values2);
        String[] values3 = GUI.multiInputBox("xx",new String[]{"姓名","年龄"},new Object[]{"yzk",3});
        GUI.msgBox(values3);*/

        /*
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("姓名","杨中科");
        data.put("年龄",3);
        data.put("生日",LocalDate.now());
        data.put("备注",null);
        Map<String,String> results = GUI.multiInputBox("输入个人信息",data);
        GUI.msgBox(results);*/
        Person p1 = new Person().setHeight(180).setName("xx").setBirthDay(LocalDate.now());
        p1 = GUI.objectInputBox("个人信息",p1);
        GUI.msgBox(p1);

        /*
        String s = GUI.buttonsBox("选择","a","b","fad","xxxxxxx","cccccccccc");
        GUI.msgBox(s);*/
        /*
        String s1 = GUI.imgBox("你喜欢这个图片","E:\\temp\\IMG_20191009_173706.jpg","b","fad","xxxxxxx","cccccccccc");
        GUI.msgBox(s1);*/
        /*
        GUI.errorBox("错误");
        boolean yn = GUI.yesNoBox("是吗？");
        GUI.msgBox(yn);
        boolean oc = GUI.okCancelBox("确认还是取消？");
        GUI.errorBox(oc);
        String name = GUI.inputBox("请输入你的姓名");
        GUI.choiceBox(name+"请选择你的城市",new String[]{"北京","上海","广州"});*/
        //int age = GUI.intBox("请输入年龄");
        //GUI.inputBox("你的年龄"+age,66);
        /*
        String[] values = GUI.multiInputBox("xxx",new String[]{"aa","ccc"},null);
        if(values!=null)
        {
            String s = String.join(",",values);
            GUI.msgBox(s);
        }*/
        /*
        String pwd = GUI.passwordBox("input your password");
        GUI.msgBox(pwd);*/
        /*
        String txt ="fadfasdfad\nfadfa\nfadfad";
        for(int i=0;i<10;i++)
        {
            txt+=txt;
        }

        String s = GUI.textBox("hello",txt,"ok","yes");
        GUI.msgBox(s);*/
        /*
        String[] values = GUI.loginBox("请登录");
        GUI.msgBox(String.join(",",values));*/
        /*
        String f1 = GUI.fileOpenBox("xxx");
        GUI.msgBox(f1);*/
        /*
        String f2= GUI.fileOpenBox("xxx","mp3","mp4");
        GUI.msgBox(f2);*/
        /*
        String f3 = GUI.fileSaveBox("save","mp3");
        GUI.msgBox(Arrays.asList(f3));*/
    /*
        String[] files = GUI.filesOpenBox("x","mp3");
        GUI.msgBox(files);*/
        /*
        GUI.msgBox(GUI.dirOpenBox("xx"));
        GUI.msgBox(GUI.dirSaveBox(null));*/
    }
}
