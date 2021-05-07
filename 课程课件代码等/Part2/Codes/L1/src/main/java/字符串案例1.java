public class 字符串案例1 {
    public static void main(String[] args) {
        String s = "c:/a/b/c.txt";
        /*
        int lastIndexOfSlash = s.lastIndexOf("/");
        String filename = s.substring(lastIndexOfSlash+1);
        System.out.println(filename);*/
        String[] strs = s.split("/");
        String filename =  strs[strs.length-1];//最后一个
        System.out.println(filename);
    }
}
