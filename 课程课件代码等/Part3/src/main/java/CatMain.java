public class CatMain {
    public static void main(String[] args) {
        /*
        Cat c1 = new Cat();
        c1.setName("花花");
        c1.setAge(1);
        c1.setHeight(33);
        //System.out.println(c1.getName()+","+c1.getAge());
        test(c1);//test(new Cat{Name="yzk",Age=18})*/

        //Cat c = new Cat().setAge(18).setName("huahua").setHeight(18);
        //test(c);
        test(new Cat().setAge(18).setName("huahua").setHeight(18));
        /*
        Cat c = new Cat();
        Cat c1 = c.setAge(18);
        Cat c2 = c1.setName("aaa");
        Cat c3 = c2.setHeight(33);
        test(c3);*/
    }

    static void test(Cat c)
    {

    }
}
