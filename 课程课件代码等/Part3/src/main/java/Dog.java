public class Dog {
    private String name="无名氏";
    private int age=18;
    public void setName(String name)
    {
        this.name = name;
    }

    public  void setAge(int value)
    {
        this.age = value;
    }

    public void sayHello()
    {
        System.out.println(this.age+","+this.name);
        String name = "多多";
        System.out.println(name);
        System.out.println(this.age+","+this.name);
    }
}
