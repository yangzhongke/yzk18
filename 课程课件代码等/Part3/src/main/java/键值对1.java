import java.util.LinkedHashMap;

public class 键值对1 {
    public static void main(String[] args) {
        LinkedHashMap map1 = new LinkedHashMap();
        map1.put("hello","你好");//键-值，key-value
        map1.put("dog","狗");
        map1.put("cat","猫");
        System.out.println(map1.get("dog"));
        System.out.println(map1.get("bird"));
        map1.put("dog","犬");
        map1.put("hi","你好");
        System.out.println(map1.get("dog"));
        String s = "bat";
        Object obj = map1.get(s);
        if(obj==null)
        {
            System.out.println("查无此词");
        }
        else
        {
            System.out.println("结果是："+obj);
        }
    }
}
