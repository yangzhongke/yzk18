public class String测试2 {
    public static void main(String[] args) {
        /*
        String s= "杨中科是最丑的人，我讨厌杨中科！";
        String s1 = s.replace("杨中科","科科");
        System.out.println(s);
        System.out.println(s1);*/
        /*
        String s = "zack,yang,tom,tim,lucy";
        String[] strs = s.split(",");
        for(String s1 : strs)
        {
            System.out.println(s1);
        }*/
        //yes 8000 cook 6000
        /*
        String s = "how are you I am fine too";
        String[] strs = s.split(" ");
        for(String s1 : strs)
        {
            System.out.println(s1);
        }*/
        /*
        String s= "http://www.youzack.com";
        if(s.startsWith("https://")||s.startsWith("http://"))
        {
            System.out.println("这是一个网址");
        }
        else
        {
            System.out.println("不是一个网址");
        }*/
        /*
        String s = "a.txt";
        System.out.println(s.endsWith(".txt"));
        System.out.println(s.endsWith(".exe"));
        System.out.println(s.endsWith("a"));*/
        //判断是否是邮箱：包含@，并且不以@开头以及结尾，必须以.com结尾
        //abc@qq.com  @aaa@a.com  afadfadsfa.com  adfa@qq.cn
       // String email="abc@qq.com";
        String email="@aaa@a.com";
        if(email.contains("@")&&!email.startsWith("@")
                &&!email.endsWith("@")&&email.endsWith(".com"))
        {
            System.out.println("是邮箱");
        }
        else
        {
            System.out.println("不是邮箱");
        }
    }
}
