import com.yzk18.GUI.GUI;
import com.yzk18.commons.CommonHelpers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class TypedDlgTest1 {
    public static void main(String[] args) {
        /*
        String[] values = GUI.multiInputBox("fadfad",new String[]{"姓名","性别","年龄","生日"},null,
                new Class[]{String.class,Boolean.class,int.class, LocalDate.class});
        GUI.msgBox(values);*/
        /*
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name",null);
        params.put("age",0);
        params.put("bd",3.5);
        params.put("bDay",LocalDate.now());
        params.put("gender",true);
        params.put("avatar",new File("d:/1.txt"));
        Map<String,String> map = GUI.multiInputBox("个人信息",params);
        GUI.msgBox(map);*/
        Dog d =new Dog();
        d.setAge(33);
        d = GUI.objectInputBox("个人信息",d);
        GUI.msgBox(d);
    }


}
