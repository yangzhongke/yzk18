import com.google.zxing.Result;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.QRCodeHelpers;

import java.util.Arrays;

public class 检测二维码 {
    public static void main(String[] args) {
       String[] files =  IOHelpers.getFilesRecursively("E:\\temp\\xcc","png","jpg","gif");
        //System.out.println(Arrays.toString(files));
        boolean qrcodeFound =false;//是否找到了二维码
        for(String file:files)
        {
            //经过试验猜测发现：如果图片中没有二维码，返回值就是null
            Result result = QRCodeHelpers.parseImage(file);
            //System.out.println(result);
            if(result!=null)
            {
                //System.out.println("含有二维码");
                qrcodeFound = true;
                break;
            }
        }
        if(qrcodeFound)//if(qrcodeFound==true)
        {
            System.out.println("含有二维码");
        }
        else
        {
            System.out.println("不含有二维码");
        }
    }
}
