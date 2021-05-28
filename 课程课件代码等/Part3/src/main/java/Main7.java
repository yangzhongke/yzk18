import com.yzk18.GUI.GUI;

import java.time.LocalDate;

public class Main7 {
    public static void main(String[] args) {
        LocalDate d1 = GUI.dateBox("选择日期");
        if(d1==null)
        {
            System.out.println("没有选择日期");
        }
        else
        {
            System.out.println(d1.getMonthValue());
        }
    }
}
