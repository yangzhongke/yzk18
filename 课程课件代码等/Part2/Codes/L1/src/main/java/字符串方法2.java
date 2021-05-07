import java.util.Locale;

public class 字符串方法2 {
    public static void main(String[] args) {
        /*
        String s1 = "ABC";
        String s2 = "Abc";
        System.out.println(s1.equals(s2));
        System.out.println(s1.equalsIgnoreCase(s2));//case-sensitive*/
        /*
        String s = "我是杨中科，我是一个帅气的杨科科，你好呀，你好帅！";
        System.out.println(s.indexOf("杨中科"));
        System.out.println(s.indexOf("zack"));
        System.out.println(s.lastIndexOf("杨中科"));
        System.out.println(s.indexOf("你好"));
        System.out.println(s.lastIndexOf("你好"));*/
        /*
        String s = "我是杨中科，我是一个帅气的杨科科，你好呀，你好帅！";
        String s1 = s.substring(3);
        System.out.println(s1);
        String s2 = s.substring(3,5);
        System.out.println(s2);*/
        /*
        String s = "   你   好    ";
        System.out.println(s);
        String s1 = s.trim();
        System.out.println(s1);
        String s2 =  s.replace(" ","");
        System.out.println(s2);*/
        String s = "Zack Yang";
        String s1 = s.toLowerCase();
        System.out.println(s1);
        String s2 = s.toUpperCase();
        System.out.println(s2);
    }
}
