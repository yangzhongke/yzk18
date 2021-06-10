import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class 异常1 {
    public static void main(String[] args) {
        /*
        String s = "123 ";
        int i = Integer.parseInt(s);
        System.out.println(i);
        NumberFormatException ex= new NumberFormatException();
        NullPointerException ex1 = new NullPointerException();*/
        //AA();
        /*
        try
        {
            FileInputStream fis = new FileInputStream("d:/1.txt");
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("文件不存在");
        }*/
        //FileInputStream fis = new FileInputStream("d:/2.txt");
        /*
        try
        {
            BB();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("cuocuocuo");
        }*/
        try {
            BB();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public  static void BB()
            throws FileNotFoundException
    {
        FileInputStream fis = new FileInputStream("d:/2.txt");
    }
    public static void AA()
    {

        try
        {
            Integer.parseInt("abc");
        }
        catch (NumberFormatException ex)
        {
            System.out.println("解析数字出错");
        }
        //Integer.parseInt("abc");
        System.out.println("AA结束");
    }
}
