package 批量生成学生卡;

import com.yzk18.GUI.GUI;
import com.yzk18.commons.IOHelpers;
import com.yzk18.commons.ImageHelpers;
import com.yzk18.docs.ExcelHelpers;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args)
    {
        File fileDir = new File(GUI.dirOpenBox("请选择文件夹"));
        List<File> dirs = Arrays.stream(fileDir.listFiles()).filter(f->f.isDirectory())
                .collect(Collectors.toList());
        for(File dir:dirs)
        {
            String className = dir.getName().replace(".xlsx","");
            File fileExcel = Arrays.stream(dir.listFiles()).filter(f->f.isFile()&&f.getName().endsWith(".xlsx"))
                    .findFirst().get();
            Workbook workbook = ExcelHelpers.openFile(fileExcel);
            Sheet sheet = workbook.getSheetAt(0);
            for(int i=1;i<=sheet.getLastRowNum();i++)
            {
                Row row = sheet.getRow(i);
                String name = ExcelHelpers.getCellStringValue(row.getCell(0));
                String number = ExcelHelpers.getCellStringValue(row.getCell(1));
                String gender = ExcelHelpers.getCellStringValue(row.getCell(2));
                File picFile = detectPhotoFile(dir,name);
                System.out.println(className+","+name+","+number+","+gender+","+picFile);

                BufferedImage buffImg = new BufferedImage(400,300,BufferedImage.TYPE_INT_RGB);
                Graphics2D g = buffImg.createGraphics();
                g.drawString("你好",100,100);

                byte[] bytes = ImageHelpers.toByteArray(buffImg,"png");
                IOHelpers.writeAllBytes("d:/1.png",bytes);
            }
        }
    }

    static File detectPhotoFile(File dir,String baseName)
    {
        File filePng = new File(dir,baseName+".png");
        if(filePng.exists())
        {
            return filePng;
        }
        File filejpg = new File(dir,baseName+".jpg");
        if(filejpg.exists())
        {
            return filejpg;
        }
        return null;
    }

    public BufferedImage scaleImage(BufferedImage img, int width, int height,
                                    Color background) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth*height < imgHeight*width) {
            width = imgWidth*height/imgHeight;
        } else {
            height = imgHeight*width/imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(background);
            g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }
}

