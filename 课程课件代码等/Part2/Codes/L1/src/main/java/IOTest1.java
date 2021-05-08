import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;
import com.yzk18.commons.DesktopHelpers;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.ImageHelpers;
import com.yzk18.commons.QRCodeHelpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class IOTest1 {
    public static void main(String[] args) {
        //IO: Input Output
       //String s = IOHelpers.readAllText("e:/temp/nihao.txt");
        //System.out.println(s);
        //File file =new File("E:\\temp\\maven备份");
        //file.delete();
       // IOHelpers.deleteDir(file);
        //IOHelpers.deleteDir("E:\\temp\\maven备份 - 副本");
        //System.out.println(IOHelpers.detectTextEncoding("e:/temp/nihao.txt"));
        //System.out.println(IOHelpers.getExtension("e:/temp/nihao.txt"));
       // System.out.println(IOHelpers.getFileNameWithoutExtension("e:/temp/nihao.txt"));
        //File file = new File("E:\\temp\\jdk-8u291-docs-all\\docs");
        //System.out.println(Arrays.toString(file.list()));
        //String[] files =   IOHelpers.getFilesRecursively("E:/temp/jdk-8u291-docs-all/docs","html");
        //String[] files =   IOHelpers.getFilesRecursively("E:/","mp4");
        //System.out.println(Arrays.toString(files));
        //byte[] bytes = IOHelpers.readAllBytes("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part2课件\\1-文件、日期等常用类.pptx");
        //System.out.println(Arrays.toString(bytes));
       // String[] lines = IOHelpers.readAllLines("e:/temp/nihao.txt");
        //System.out.println(Arrays.toString(lines));
        //byte[] bytes={3,5,8,92,33,33,33};
        //IOHelpers.writeAllBytes("e:/temp/1.bin",bytes);
        /*
        byte[] bytes = IOHelpers.readAllBytes("E:\\主同步盘\\我的坚果云\\UoZ\\SE101-玩着学编程\\Part2课件\\1-文件、日期等常用类.pptx");
        IOHelpers.writeAllBytes("e:/temp/1.pptx",bytes);*/
        /*
        String[] lines = {"你好","我好"};
        IOHelpers.writeAllLines("e:/temp/a/b/222.txt",lines);
        IOHelpers.writeAllText("e:/temp/a/b/666.txt","aaa\nadsfasdfa\n你好吗");*/
        //DesktopHelpers.editFile("e:/temp/nihao.txt");
        //String s = DesktopHelpers.getClipboardText();
        //System.out.println(s);
        //DesktopHelpers.setClipboardText("杨中科最帅");
        //DesktopHelpers.openUrl("https://www.youzack.com");
        //bug: 中文有问题
      //  BufferedImage img = QRCodeHelpers.generateQRCodeImage("hello youzack",200,200);
        //ImageHelpers.writeToFile(img,"png","e:/temp/2.png");
        Result result = QRCodeHelpers.parseImage("e:/temp/2.png");
        System.out.println(result.getText());
    }
}
