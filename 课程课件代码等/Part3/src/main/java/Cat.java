public class Cat {
    private int age;
    private String name;
    private int height;
    public Cat setAge(int age)
    {
        this.age = age;
        return this;
    }
    public int getAge()
    {
        return this.age;
    }

    public Cat setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public Cat setHeight(int height)
    {
        this.height = height;
        return this;
    }
    public int getHeight()
    {
        return this.height;
    }
}
