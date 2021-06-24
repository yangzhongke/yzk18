import com.yzk18.commons.IOHelpers;

public class 统计视频播放量 {
    public static void main(String[] args) {
        String[] lines = IOHelpers.readAllLines("d:/视频播放量.txt");
        int sum=0;//总次数
        for(String line : lines)
        {
            /*
            if(line.contains("次观看"))
            {
                System.out.println(line);
                int indexofCi = line.indexOf("次观看");//"次观看"的位置
                String s= line.substring(0,indexofCi);
                int count = Integer.valueOf(s);
                System.out.println(count);
                sum=sum+count;//sum+=count;累加播放量
            }*/
            int indexofCi = line.indexOf("次观看");//"次观看"的位置
            if(indexofCi>=0)
            {
                String s= line.substring(0,indexofCi);
                int count = Integer.valueOf(s);
                sum=sum+count;
            }
        }
        System.out.println(sum);
    }
}
