import java.time.LocalDate;

public class LocalDateTest1 {
    public static void main(String[] args) {
        LocalDate d1 = LocalDate.now();
        System.out.println(d1);
        LocalDate d2 = LocalDate.of(2008,8,30);
        System.out.println(d2);
        LocalDate d3 = LocalDate.parse("2021-09-18");
        System.out.println(d3);

        System.out.println(d3.getYear());
        System.out.println(d3.getMonth());
        System.out.println(d3.getMonthValue());
        System.out.println(d3.getDayOfMonth());
        System.out.println(d3.getDayOfWeek());
        System.out.println(d3.getDayOfYear());
    }
}
