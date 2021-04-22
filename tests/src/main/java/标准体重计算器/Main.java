package 标准体重计算器;

import com.yzk18.GUI.GUI;
import com.yzk18.commons.CommonHelpers;

import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        /*
        世卫计算方法:
        男性：标准体重=(身高cm－80)×70﹪
        女性：标准体重=(身高cm－70)×60﹪
        标准体重正负10﹪为正常体重
        标准体重正负10﹪~ 20﹪为偏胖或偏瘦
        标准体重正负20﹪以上为严重肥胖或严重瘦弱
         */
        String gender = GUI.buttonsBox("您的性别是？","男性","女性");
        Double height = GUI.doubleBox("请输入身高（厘米）");
        Double weight = GUI.doubleBox("请输入体重");
        double 标准体重;
        if(gender.equals("男性"))
        {
            标准体重 = (height-80)*0.7;
        }
        else
        {
            标准体重 = (height-70)*0.6;
        }
        double delta = (weight-标准体重)/标准体重;
        System.out.println(delta);
        String desc;
        if(delta<-0.2)
        {
            desc="严重瘦弱";
        }
        else if(delta>=-0.2&&delta<-0.1)
        {
            desc = "偏瘦";
        }
        else if(delta>=-0.1&&delta<=0.1)
        {
            desc="标准体重";
        }
        else if(delta>0.1&&delta<0.2)
        {
            desc="偏胖";
        }
        else
        {
            desc="严重肥胖";
        }
        GUI.msgBox(desc+"\r\n标准体重："+ CommonHelpers.toString(标准体重));
    }
}
