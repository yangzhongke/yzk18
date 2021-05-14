import com.yzk18.GUI.GUI;

public class 标准体重计算器 {
    public static void main(String[] args) {
        String 性别 = GUI.buttonsBox("选择您的性别","男","女");
        double 身高 = GUI.doubleBox("您的身高");
        double 体重 = GUI.doubleBox("您的体重");
        double 标准体重;
        if(性别.equals("男"))
        {
            //double 标准体重 = (身高-80)*0.7;
            标准体重 = (身高-80)*0.7;
            System.out.println(标准体重);
            //double 和标准体重的差=(体重-标准体重)/标准体重;//DRY
        }
        else
        {
            //double 标准体重 = (身高-70)*0.6;
            标准体重 = (身高-70)*0.6;
            System.out.println(标准体重);
            //double 和标准体重的差=(体重-标准体重)/标准体重;
        }
        double 和标准体重的差=(体重-标准体重)/标准体重;
        if(和标准体重的差>-0.1&&和标准体重的差<=0.1)
        {
            GUI.msgBox("您好标准呀");
        }
        else if(和标准体重的差>=-0.2&&和标准体重的差<=-0.1)
        {
            GUI.msgBox("偏瘦");
        }
        else if(和标准体重的差>0.1&&和标准体重的差<=0.2)
        {
            GUI.msgBox("偏胖");
        }
        else if(和标准体重的差>0.2)
        {
            GUI.msgBox("太胖了，胖的不行了，快减肥");
        }
        else
        {
            GUI.msgBox("太瘦了，瘦的不行了，快多吃点吧");
        }
    }
}
