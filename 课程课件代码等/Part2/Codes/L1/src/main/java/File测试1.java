import java.io.File;

public class File测试1 {
    public static void main(String[] args) {
        File f1 = new File("d:/temp/1.txt");
        //System.out.println(f1);
        File f2 = new File("E:/主同步盘/我的坚果云/UoZ/SE101-玩着学编程/Part2课件/9-GUI案例.pptx");
        //System.out.println(f2);
        File f3 = new File("d:/temp");
        //System.out.println(f3);
        File f4 = new File("d:/temp/2.txt");
        //System.out.println(f4);
/*
        f1.delete();
        f3.delete();*/
        /*
        System.out.println(f1.exists());
        System.out.println(f2.exists());
        System.out.println(f3.exists());
        System.out.println(f4.exists());*/
/*
        System.out.println(f1.getName());
        System.out.println(f2.getName());
        System.out.println(f3.getName());
        System.out.println(f4.getName());*/
        /*
        System.out.println(f1.isFile());
        System.out.println(f1.isDirectory());*/
        String[] files = f3.list();
        for (String file : files)
        {
            System.out.println(file);
        }
    }
}
