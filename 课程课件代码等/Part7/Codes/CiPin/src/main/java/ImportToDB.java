import com.yzk18.commons.IOHelpers;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ImportToDB {
    public static void main(String[] args) {
        /*
        String filename = "E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part7课件\\Codes\\CiPin\\DB.db";
        String[] lines = IOHelpers.readAllLines(filename);
        for(int i=1;i< lines.length-1;i++)
        {

        }*/
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();
        map.put("a",50);
        map.put("b",3);
        map.put("c",15);
        var items = map.entrySet().stream().sorted((o1, o2) -> o2.getValue()-o1.getValue()).toList();
        for(var item : items)
        {
            System.out.println(item.getKey()+":"+item.getValue());
        }
    }
}
