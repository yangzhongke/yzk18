import static com.yzk18.commons.CommonHelpers.*;
import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class Test1 {
    public static void main(String[] args) {
        /*
        System.out.println(CommonHelpers.toString(new String[]{"a","3"}));
        System.out.println(CommonHelpers.toString(Arrays.asList(3,5,8)));
        System.out.println(CommonHelpers.toString(null));
        System.out.println(CommonHelpers.toString(3));
        Person p1 = new Person();
        p1.setAge(3);
        p1.name="tom";
        p1.Id=333;
        System.out.println(CommonHelpers.toString(p1));
        System.out.println(CommonHelpers.toString(Arrays.asList(p1)));
        println("输入姓名");
        String name = readLine();
        println("输入年龄");
        int age = readInt();
        println("输入职业");
        String pos= readLine();
        println("你好"+name+",年龄"+age+",职业"+pos);*/
        /*
        String s = IOHelpers.readAllText("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\封装库代码\\yzk18\\yzk18-commons\\src\\test\\java\\Test1.java");
        System.out.println(s);
        String s1 = IOHelpers.readAllText("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\案例策划.txt");
        System.out.println(s1);
        */
        /*
        IOHelpers.writeAllText("d:/temp/a/c/x/1.txt","hello");
        IOHelpers.writeAllLines("d:/temp/a/c/x/1.txt",new String[]{"adfadsf","你好"});
        String[] s5= IOHelpers.readAllLines("d:/temp/a/c/x/1.txt");
        CommonHelpers.println(s5);
        CommonHelpers.println(IOHelpers.readAllText("d:/temp/a/c/x/1.txt"));*/
        //IOHelpers.deleteDir("d:/temp");
        //println(IOHelpers.getFiles("d:/temp",true));
        //println(IOHelpers.getDirs("D:\\greeninstall",true));
        //println(IOHelpers.getDirs("D:\\greeninstall"));
        //println(IOHelpers.getFiles("D:\\greeninstall",true));
        //println(IOHelpers.getFiles("D:\\greeninstall"));
        //println(IOHelpers.getFiles("D:\\greeninstall",true,"exe"));
        /*
        println(IOHelpers.exists("w:/"));
        println(IOHelpers.exists("d:/"));
        String f1 = "e:/temp/小程序修复的bug.txt";
        println(IOHelpers.exists(f1));
        println(IOHelpers.getParentDir(f1));
        println(IOHelpers.getFileNameWithoutExtension(f1));
        println(IOHelpers.getExtension(f1));
        println(IOHelpers.getFileName(f1));
        println(IOHelpers.getTempDirectory());
        println(IOHelpers.getUserDirectory());*/
        /*
        println("1. "+DesktopHelpers.getClipboardText());
        DesktopHelpers.setClipboardText("hello,kia ora");
        println("2. "+DesktopHelpers.getClipboardText());
        //DesktopHelpers.openFile("E:\\temp\\超级玛丽启动画面.png");
        //DesktopHelpers.editFile("E:\\temp\\小程序修复的bug.txt");
        DesktopHelpers.openUrl("https://www.youzack.com");*/
        println(CommonHelpers.toInt(3.14));
        println(CommonHelpers.toInt("5"));
        println(CommonHelpers.toLocalDate(new Date()));
        println(CommonHelpers.toLocalDate("2008-08-08"));
        println(CommonHelpers.toLocalDateTime("2008-08-08 08:08:08"));
        int[] nums = CommonHelpers.parseJson("[1,3,5]",int[].class);
        println(nums);
    }
}

