import java.util.LinkedHashMap;

public class 键值对2 {
    public static void main(String[] args) {
        LinkedHashMap map = new LinkedHashMap();
        map.put("张三",80);
        map.put("李四",90);
        map.put("杨中科",100);

        System.out.println(map.get("李四"));
    }
}
