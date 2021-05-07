public class 字符串登录案例 {
    public static void main(String[] args) {
        String username = " Xdmin ";
        String password = "123456";
        String s1 = username.trim();
        if(s1.equalsIgnoreCase("admin")&&password.equals("123456"))
        //if(username.trim().equalsIgnoreCase("admin")&&password.equals("123456"))
        {
            System.out.println("ok");
        }
        else
        {
            System.out.println("error");
        }
    }
}
