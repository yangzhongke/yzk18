import com.yzk18.commons.IOHelpers;

import java.io.File;

public class Test2 {
    public static void main(String[] args) {
        IOHelpers.writeAllText("d:/1.txt","hello");
        IOHelpers.writeAllText(new File("d:/1.txt"),"你好");
        IOHelpers.appendAllText("d:/1.txt","我好");
        IOHelpers.appendAllLines(new File("d:/1.txt"),new String[]{"11","22","33"});
    }
}
