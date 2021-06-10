import com.sun.org.apache.xpath.internal.operations.Bool;

public class IntTest {
    public static void main(String[] args) {

        //int i1 = 0;
        /*
        Integer i1 = 3;
        System.out.println(i1);
        Integer i2 = null;
        System.out.println(i2);
        int i3 = i1;
        System.out.println(i3);
        int i4= i2;
        System.out.println(i4);

        long l1 = 5;
        //long l2 = null;
        Long l2 = null;

        Boolean b1 = null;*/
        MyInteger i1 = new MyInteger();
        i1.setValue(3);
        System.out.println(i1.getValue());
    }
}
