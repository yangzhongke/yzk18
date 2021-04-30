public class IfTest1
{
    public static void main(String[] args)
    {
        /*
        int age = 8;
        if (age > 18)
        {
            System.out.println("成年人");
            System.out.println("afdasdf");
            System.out.println("adfadfasf");
        }*/
        int age = 8;
        /*
        if(age>18)
        {
            System.out.println("成年人");
        }
        else
        {
            System.out.println("未成年人");
        }*/
        if(age<0)
        {
            System.out.println("年龄咋是负数？");
        }
        else if(age<=1)
        {
            System.out.println("婴儿");
        }
        else if(age<=5)
        {
            System.out.println("幼儿");
        }
        else if(age<=10)
        {
            System.out.println("少儿");
        }
        else if(age<=18)
        {
            System.out.println("儿童");
        }
        else
        {
            System.out.println("成年人");
        }
        System.out.println("over");
    }
}
