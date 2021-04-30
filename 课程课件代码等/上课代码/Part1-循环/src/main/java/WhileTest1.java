public class WhileTest1 {
    public static void main(String[] args) {
        /*
        int i=0;
        while(i<3)
        {
            System.out.println(i);
            i++;
        }*/
        /*
        int i=10;
        while(i>=0)
        {
            System.out.println(i);
            i--;
        }*/
        int i=0;
        int sum=0;
        while(i<=10)
        {
            sum=sum+i;
            System.out.println("sum:"+sum+",i="+i);
            i++;
        }
        System.out.println("和="+sum);
        System.out.println("over");
    }
}
