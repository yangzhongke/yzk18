public class Main4 {
    public static void main(String[] args) {
        //String s="";
        /*
        Person p1 = new Person();//只有new出来才是一个新的对象
        p1.setAge(18);
        p1.setName("杨中科");
        p1.sayHello();
        test(p1);
        p1.sayHello();*/
        int i=18;
        testInt(i);
        System.out.println(i);
    }

    static void testInt(int i)
    {
        i=10;
    }

    static void test(Person p)
    {
        p.setAge(80);
    }
}
