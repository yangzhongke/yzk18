import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String s = "abc";
        //s.split();
        //Arrays.toString(new int[]{3});
        //Double.parseDouble("133");
        /*
        String s = "abc";
        s.split()*/
        Person p1 = new Person();
        //Person p1:声明Person类型的变量p1
        //new Person()创建Person类的一个对象；把Person类实例化
        //Person p1 = new Person(); 声明Person类型的变量p1;创建Person类的一个对象；把变量p1指向这个对象
        p1.setAge(18);//调用p1变量“当前所指向对象”的setAge方法
        p1.setName("杨中科");
        p1.sayHello();

        Person p2 = new Person();
        p2.setAge(30);
        p2.setName("张三");
        p2.sayHello();

        //p2.hi();
        Person.hi();
        //Person.sayHello();

        p1.sayHello();
        p2.sayHello();
    }
}
