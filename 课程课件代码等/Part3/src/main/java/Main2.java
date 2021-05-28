public class Main2 {
    public static void main(String[] args) {
        /*
        int i=10;
        int j=i;//10  把i“当前”的值赋值给j
        System.out.println(j);
        i=20;
        System.out.println(j);*/
        Person yzk = new Person();
        yzk.setAge(18);//yzk.setAge=18;
        yzk.setName("杨中科");
        yzk.sayHello();

        Person niuren = yzk;//把yzk“当前指向的对象”赋值给niuren
        //让niuren变量指向yzk“当前指向的对象”
        //Person niuren = new Person();
        niuren.setAge(80);
        niuren.setName("狂徒张三");
        niuren.sayHello();
        yzk.sayHello();
    }
}
