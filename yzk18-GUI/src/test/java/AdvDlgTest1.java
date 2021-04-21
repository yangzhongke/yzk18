import com.github.lgooddatepicker.components.TimePicker;
import com.yzk18.GUI.GUI;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdvDlgTest1 {
    public static void main(String[] args) {
        //System.out.println(GUI.dateBox("x", LocalDate.of(2008,8,8)));
        //System.out.println(GUI.datetimeBox("x", LocalDateTime.of(2008,8,8,8,8,8)));;
        //System.out.println(GUI.datetimeBox("x"));
        System.out.println(GUI.timeBox("下班时间"));
    }
}
