public class BreakTest1 {
    public static void main(String[] args) {
        /*
        for(int i=1;i<=5;i++)
        {
            if(i==3)
            {
                continue;
               // break;
            }
            System.out.println(i);
        }*/
        /*
        for(int i=0;i<=10;i++)
        {
            if(i%2==0)//9%4=1 偶数
            {
                break;
                //continue;
            }
            System.out.println(i);
        }*/
        int i=0;
        while(i<10)
        {
            if(i==5)
            {
                //i++;
                continue;
                //break;
            }
            System.out.println(i);
            i++;
        }
        System.out.println("over");
    }
}
