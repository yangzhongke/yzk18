public class 字符串方法1 {
    public static void main(String[] args) {
        String s = "hello";
        //int[] nums = {3,5,8};
        //System.out.println(nums.length);
        System.out.println(s.length());
        char c1 = s.charAt(1);
        System.out.println(c1);
        System.out.println(s.contains("el"));
        System.out.println(s.contains("le"));
        System.out.println(s.contains("llo"));
        System.out.println(s.contains("oll"));
    }
}
