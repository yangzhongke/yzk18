import com.google.zxing.Result;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.ImageHelpers;
import com.yzk18.commons.QRCodeHelpers;

import java.awt.image.BufferedImage;

public class TestQRCode1 {
    public static void main(String[] args) {
        BufferedImage buffImg = QRCodeHelpers.generateQRCodeImage("hello world 这是中文 \r\n java",300,300);
        byte[] pngBytes = ImageHelpers.toByteArray(buffImg,"png");
        IOHelpers.writeAllBytes("d:/temp/qr.png",pngBytes);
        Result result = QRCodeHelpers.parseImage("d:/temp/qr.png");
        if(result==null)
        {
            System.out.println("fail");
        }
        else
        {
            System.out.println(result.getText());
        }
    }
}
