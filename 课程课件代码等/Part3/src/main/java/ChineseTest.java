public class ChineseTest {
    public static void main(String[] args) {
        /*
        Chinese c1 = new Chinese();
        c1.setAge(50);
        c1.setName("杨中科");
        c1.setHukou("帝都");
        c1.sayHello();*/
        Person p1 = new Chinese();//父类类型的变量可以指向子类类型的对象
        //能调用什么方法，取决于变量的类型
        p1.setAge(50);
        p1.setName("hello");
        //p1.setHukou("xxx");
        p1.sayHello();

        Chinese p2 = (Chinese)p1;//显式转换。(Chinese)程序员确认：p1指向的一定是Chinese对象
        p2.setHukou("魔都");
        p2.setAge(18);
        p1.sayHello();
        System.out.println(p2.getHukou());

        Object obj = p1;
        System.out.println(obj.getClass());

    }
}
