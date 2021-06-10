import java.util.LinkedHashMap;
import java.util.LinkedList;

public class 泛型1 {
    public static void main(String[] args) {
        /*
        LinkedList<String> list1 = new LinkedList<String>();
        list1.add("zzz");
        //list1.add(3);
        String s = list1.get(0);
        System.out.println(s);*/
        /*
        LinkedHashMap<String,String> map1 =
                new LinkedHashMap<String,String>();
        map1.put("dog","狗");
        //map1.put("dog",3);
        String s = map1.get("dog");
        System.out.println(s);*/
        //LinkedList<int> list1 =new LinkedList<int>();
        /*
        LinkedList<Integer> list1 =new LinkedList<Integer>();
        list1.add(3);
        list1.add(5);
       // list1.add("");
        int i = list1.get(1);
        */
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        map.put("张三",80);
        map.put("李四",90);
        map.put("杨中科",100);
        int i = map.get("张三风");
        System.out.println(i);
        /*
        Integer i = map.get("张三风");
        if(i==null)
        {
            System.out.println("没有成绩");
        }
        else
        {
            System.out.println(i);
        }*/
    }
}
