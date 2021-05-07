import java.time.LocalDate;
import java.time.LocalTime;

public class LocalTimeTest {
    public static void main(String[] args) {

        LocalTime t1 = LocalTime.now();
        System.out.println(t1);
        LocalTime t2 = LocalTime.of(8,5,21);
        System.out.println(t2);
        LocalTime t3 = LocalTime.parse("02:05:09");
        System.out.println(t3);
        System.out.println(t1.getHour());
        System.out.println(t1.getMinute());
        System.out.println(t1.getSecond());
    }
}
