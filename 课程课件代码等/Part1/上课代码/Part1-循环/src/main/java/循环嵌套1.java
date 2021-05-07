public class 循环嵌套1 {
    public static void main(String[] args) {
        for(int i=0;i<3;i++)//i=0,i=1
        {
            System.out.println("内层之前");
            for(int j=1;j<=3;j++)
            {
                System.out.println(i+","+j);//0,1;0,2;0,3; 1,1;1,2
            }
            System.out.println("内层之后");
        }
    }
}
