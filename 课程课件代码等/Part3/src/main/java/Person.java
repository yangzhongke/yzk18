public class Person {
    private int age;
    private String name;
    public int height;
    private  void AA()
    {

    }
    public void setAge(int value)
    {
        if(value<0)
        {
            System.out.println("年龄不能小于0");
            return;
        }
        this.age=value;//给成员变量赋值
    }
    public void setName(String v)
    {
        this.name = v;//设置当前对象的name变量的值为v
    }

    public void sayHello()
    {
        System.out.println("你好，我是"+this.name+"，我的年龄是："+this.age);//从成员变量取值
    }

    public static void hi()
    {
        System.out.println("你们好");
        //this.age=5; static方法中不能直接访问非static成员
    }
}
