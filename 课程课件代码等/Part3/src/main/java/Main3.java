public class Main3 {
    public static void main(String[] args) {
        Person p1 = new Person();
        p1.setAge(18);
        p1.setName("杨中科");
        Person p2 = p1;
        p1 = new Person();
        p1.setName("张三");
        p1.setAge(80);

        p2.sayHello();
    }
}
