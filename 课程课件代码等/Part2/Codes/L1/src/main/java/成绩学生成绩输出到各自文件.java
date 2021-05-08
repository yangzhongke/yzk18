import com.yzk18.commons.IOHelpers;
import sun.security.rsa.RSAUtil;

public class 成绩学生成绩输出到各自文件 {
    public static void main(String[] args) {
        /*
        String name = "你好";
        System.out.println(name);
        System.out.println("name");*/
        //int a="5";
        //String s = 5;
        String[] lines =  IOHelpers.readAllLines("E:/temp/xxx/成绩.txt");
        for(String line : lines)
        {
            //"3",3 不一样
            String[] strs = line.split(",");
            /*
            String name = strs[0];
            String 语文 = strs[0];
            String 数学 = strs[0];
            String 外语 = strs[0];
            double 平均风 =语文+数学+外语/3;*/
            //System.out.println(line);

            String name = strs[0];
           // System.out.println(name);
            double 语文 = Double.parseDouble(strs[1]);
            double 数学 = Double.parseDouble(strs[2]);
            double 外语 = Double.parseDouble(strs[3]);
            double 平均分 =(语文+数学+外语)/3;
            String msg = name+"你好，语文成绩为"+语文+"，数学为"+数学+"，外语为"+外语+"，平均分为"+平均分;
            String filename = "E:/temp/xxx/"+name+".txt";
            System.out.println(filename);
            IOHelpers.writeAllText(filename,msg);
        }
    }
}
