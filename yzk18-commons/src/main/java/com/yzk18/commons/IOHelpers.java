package com.yzk18.commons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.intl.chardet.nsDetector;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * <div lang="zh-cn">用于处理文件、文件夹等的工具类。</div>
 */
public class IOHelpers {

    /**
     * <div lang="zh-cn">检测文件filename的编码</div>
     * @param fileName
     * @return <div lang="zh-cn">检测到的编码的名称</div>
     */
    public static String detectTextEncoding(String fileName)
    {
        return detectTextEncoding(new File(fileName));
    }

    /**
     * <div lang="zh-cn">检测文件file的编码</div>
     * @param file
     * @return <div lang="zh-cn">检测到的编码的名称</div>
     */
    public static String detectTextEncoding(File file)
    {
        try(FileInputStream fis =  new FileInputStream(file))
        {
            return detectTextEncoding(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">检测inStream内容的文本编码</div>
     * @param inStream
     * @return <div lang="zh-cn">检测到的编码的名称</div>
     */
    public static String detectTextEncoding(InputStream inStream)
    {
        String[] charSetFounded = new String[1];
        nsDetector detector = new nsDetector();
        detector.Init(charset->charSetFounded[0] = charset);
        try {
            byte[] bytes=new byte[128];
            while (inStream.read(bytes)>0)
            {
                detector.DoIt(bytes, bytes.length, false);
                if(charSetFounded[0]!=null)
                {
                    break;
                }
            }
            detector.DataEnd();
            detector.Reset();
            return  charSetFounded[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容，会自动根据自动检测出来的编码进行读取。</div>
     * @param file
     * @return
     */
    public static String readAllText(File file)
    {
        return readAllText(file.toString());
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容，会自动根据自动检测出来的编码进行读取。</div>
     * @param file
     * @return
     */
    public static String readAllText(String file)
    {
        String charsetName = detectTextEncoding(file);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        return readAllText(file,charsetName);
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容，用charsetName这个编码读取。</div>
     * @param file
     * @param charsetName
     * @return
     */
    public static String readAllText(File file,String charsetName)
    {
        return  readAllText(file.toString(),charsetName);
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容，用charsetName这个编码读取。</div>
     * @param file
     * @param charsetName
     * @return
     */
    public static String readAllText(String file,String charsetName)
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return IOUtils.toString(fis,charsetName);
        }catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn"></div>
     * @param inputStream
     * @return
     */
    public static  String readAllText(InputStream inputStream)
    {
        InputStream newInStream;
        if(inputStream.markSupported())
        {
            newInStream = inputStream;
        }
        else
        {
            newInStream = toByteArrayInputStream(inputStream);
        }
        newInStream.mark(Integer.MAX_VALUE);
        String charsetName = detectTextEncoding(newInStream);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        try
        {
            newInStream.reset();
            String text = IOUtils.toString(newInStream,charsetName);
            if(newInStream!=inputStream)
            {
                newInStream.close();
            }
            return text;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取inputStream中的所有文本内容为一个String数组，会自动根据自动检测出来的编码进行读取。</div>
     * @param inputStream
     * @return <div lang="zh-cn">数组一个元素对应文本中的一行。</div>
     */
    public static  String[] readAllLines(InputStream inputStream)
    {
        InputStream newInStream;
        if(inputStream.markSupported())
        {
            newInStream = inputStream;
        }
        else
        {
            newInStream = toByteArrayInputStream(inputStream);
        }
        newInStream.mark(Integer.MAX_VALUE);
        String charsetName = detectTextEncoding(newInStream);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        try
        {
            newInStream.reset();
            String[] lines = IOUtils.readLines(newInStream, charsetName).toArray(new String[0]);
            if(newInStream!=inputStream)
            {
                newInStream.close();
            }
            return lines;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容为一个String数组，会自动根据自动检测出来的编码进行读取。</div>
     * @param file
     * @return <div lang="zh-cn">数组一个元素对应文本中的一行。</div>
     */
    public static String[] readAllLines(File file)
    {
        return readAllLines(file.toString());
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有文本内容为一个String数组，会自动根据自动检测出来的编码进行读取。</div>
     * @param file
     * @return <div lang="zh-cn">数组一个元素对应文本中的一行。</div>
     */
    public static String[] readAllLines(String file)
    {
        String charsetName = detectTextEncoding(file);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        return readAllLines(file,charsetName);
    }

    /**
     * <div lang="zh-cn">使用charsetName编码，读取文件file中的所有文本内容为一个String数组。</div>
     * @param file
     * @param charsetName
     * @return <div lang="zh-cn">数组一个元素对应文本中的一行。</div>
     */
    public static String[] readAllLines(File file,String charsetName)
    {
        return readAllLines(file.toString(),charsetName);
    }

    /**
     * <div lang="zh-cn">使用charsetName编码，读取文件file中的所有文本内容为一个String数组。</div>
     * @param file
     * @param charsetName
     * @return <div lang="zh-cn">数组一个元素对应文本中的一行。</div>
     */
    public static String[] readAllLines(String file,String charsetName)
    {
        try(FileInputStream fis = new FileInputStream((file)))
        {
            List<String> lines= IOUtils.readLines(fis,charsetName);
            return lines.toArray(new String[lines.size()]);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有内容。</div>
     * @param file
     * @return <div lang="zh-cn">文件内容的字节数据。</div>
     */

    public static byte[] readAllBytes(File file)
    {
        return readAllBytes(file.toString());
    }

    /**
     * <div lang="zh-cn">读取文件file中的所有内容。</div>
     * @param file
     * @return <div lang="zh-cn">文件内容的字节数据。</div>
     */
    public static byte[] readAllBytes(String file)
    {
        try(FileInputStream fis = new FileInputStream((file)))
        {
            return IOUtils.toByteArray(fis);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">读取inputStream中的所有内容。</div>
     * @param inputStream
     * @return <div lang="zh-cn">流内容的字节数据。</div>
     */
    public static byte[] readAllBytes(InputStream inputStream)
    {
        try
        {
            return IOUtils.toByteArray(inputStream);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">创建文件或者文件夹file的父目录，如果已经存在则忽略。如果父目录或者父目录的任意一级祖先目录不存在，则递归创建所有相关祖先目录。</div>
     * @param file
     */
    public static void mkParentDirs(String file)
    {
        mkParentDirs(new File(file));
    }

    /**
     * <div lang="zh-cn">创建文件或者文件夹file的父目录，如果已经存在则忽略。如果父目录或者父目录的任意一级祖先目录不存在，则递归创建所有相关祖先目录。</div>
     * @param file
     */
    public static void mkParentDirs(File file)
    {
        try
        {
            FileUtils.forceMkdirParent(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把字符串text的内容按照charsetName编码（覆盖式）写入文件file。</div>
     * @param file
     * @param text
     * @param charsetName
     */
    public static void writeAllText(File file,String text,String charsetName)
    {
        writeAllText(file.toString(),text,charsetName);
    }

    /**
     * <div lang="zh-cn">把字符串text的内容按照charsetName编码（覆盖式）写入文件fileName。</div>
     * @param fileName
     * @param text
     * @param charsetName
     */
    public static void writeAllText(String fileName,String text,String charsetName)
    {
        mkParentDirs(fileName);
        try(FileOutputStream fos = new FileOutputStream(fileName))
        {
            IOUtils.write(text.toCharArray(),fos,charsetName);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把字符串text的内容按照UTF-8编码（覆盖式）写入文件fileName。</div>
     * @param file
     * @param text
     */
    public static void writeAllText(File file,String text)
    {
        writeAllText(file.toString(),text);
    }

    /**
     * <div lang="zh-cn">把字符串text的内容按照UTF-8编码（覆盖式）写入文件fileName。</div>
     * @param fileName
     * @param text
     */
    public static void writeAllText(String fileName,String text)
    {
        writeAllText(fileName, text,"UTF-8");
    }

    /**
     * <div lang="zh-cn">把字符串数组lines的内容按照charsetName的编码（覆盖式）写入文件file。</div>
     * @param file
     * @param lines <div lang="zh-cn">这个参数中的每一个元素对应文件中的一行文本。</div>
     * @param charsetName
     */

    public static void writeAllLines(File file,String[] lines,String charsetName)
    {
        writeAllLines(file.toString(),lines,charsetName);
    }

    /**
     * <div lang="zh-cn">把字符串数组lines的内容按照charsetName的编码（覆盖式）写入文件fileName。</div>
     * @param fileName
     * @param lines  <div lang="zh-cn">这个参数中的每一个元素对应文件中的一行文本。</div>
     * @param charsetName
     */
    public static void writeAllLines(String fileName,String[] lines,String charsetName)
    {
        mkParentDirs(fileName);
        try(FileOutputStream fos = new FileOutputStream(fileName))
        {
            IOUtils.writeLines(Arrays.asList(lines), IOUtils.LINE_SEPARATOR_WINDOWS,
                    fos, charsetName);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">把字符串数组lines的内容按照UTF-8编码（覆盖式）写入文件file。</div>
     * @param file
     * @param lines  <div lang="zh-cn">这个参数中的每一个元素对应文件中的一行文本。</div>
     */
    public static void writeAllLines(File file,String[] lines)
    {
        writeAllLines(file.toString(),lines);
    }

    /**
     * <div lang="zh-cn">把字符串数组lines的内容按照UTF-8编码（覆盖式）写入文件fileName。</div>
     * @param fileName
     * @param lines  <div lang="zh-cn">这个参数中的每一个元素对应文件中的一行文本。</div>
     */
    public static void writeAllLines(String fileName,String[] lines)
    {
        writeAllLines(fileName,lines,"UTF-8");
    }

    /**
     * <div lang="zh-cn">把字节数组bytes写入文件file。</div>
     * @param file
     * @param bytes
     */
    public static void writeAllBytes(File file, byte[] bytes)
    {
        writeAllBytes(file.toString(),bytes);
    }

    /**
     * <div lang="zh-cn">把字节数组bytes写入文件fileName。</div>
     * @param fileName
     * @param bytes
     */
    public static void writeAllBytes(String fileName, byte[] bytes)
    {
        mkParentDirs(fileName);
        try(FileOutputStream fos = new FileOutputStream(fileName))
        {
            IOUtils.write(bytes,fos);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * <div lang="zh-cn">把inStream的内容转换为一个ByteArrayInputStream对象</div>
     * @param inStream
     * @return
     */
    public static InputStream toByteArrayInputStream(InputStream inStream)
    {
        byte[] bytes = readAllBytes(inStream);
        ByteArrayInputStream baInStream = new ByteArrayInputStream(bytes);
        return baInStream;
    }


    /**
     * <div lang="zh-cn">删除目录dirName，如果目录不为空，则先把目录清空再删除目录。</div>
     * @param dirName
     */
    public static void deleteDir(String dirName) {
        deleteDir(new File(dirName));
    }

    /**
     * <div lang="zh-cn">删除目录dir，如果目录不为空，则先把目录清空再删除目录。</div>
     * @param dir
     */
    public static void deleteDir(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">递归得到目录dir下以及后代目录下所有后缀名为extensions的文件。</div>
     * @param dir
     * @param extensions <div lang="zh-cn">比如jpg、png、pdf等，注意不能带*等通配符，也不能带“.”。</div>
     * @return
     */
    public static String[] getFilesRecursively(String dir,String... extensions)
    {
        return getFilesRecursively(new File(dir),extensions);
    }

    /**
     *  <div lang="zh-cn">递归得到目录dir下以及后代目录下所有后缀名为extensions的文件。</div>
     * @param dir
     * @param extensions <div lang="zh-cn">比如jpg、png、pdf等，注意不能带*等通配符，也不能带“.”。</div>
     * @return
     */
    public static String[] getFilesRecursively(File dir,String... extensions)
    {
        String[] files = FileUtils.listFiles(dir,extensions,true)
                .stream().map(f->f.toString()).toArray(String[]::new);
        return files;
    }

    /**
     * <div lang="zh-cn">得到文件路径file中不包含扩展名的文件名。比如"c:/a/b/www.txt"得到"www"。</div>
     * @param file
     * @return
     */
    public static String getFileNameWithoutExtension(String file)
    {
        return FilenameUtils.getBaseName(file);
    }

    /**
     * <div lang="zh-cn">得到文件路径file中不包含扩展名的文件名。比如"c:/a/b/www.txt"得到"www"。</div>
     * @param file
     * @return
     */
    public static String getFileNameWithoutExtension(File file)
    {
        return FilenameUtils.getBaseName(file.toString());
    }

    /**
     * <div lang="zh-cn">得到文件路径file中只有文件名的部分。比如"c:/a/b/www.txt"得到"www.txt"。</div>
     * @param file
     * @return
     */
    public static String getFileName(String file)
    {
        return FilenameUtils.getName(file);
    }

    /**
     * <div lang="zh-cn">得到文件路径file中只有文件名的部分。比如"c:/a/b/www.txt"得到"www.txt"。</div>
     * @param file
     * @return
     */
    public static String getFileName(File file)
    {
        return file.getName();
    }

    /**
     * <div lang="zh-cn">得到文件路径file中的文件扩展名。比如"c:/a/b/www.txt"得到"txt"。</div>
     * @param file
     * @return <div lang="zh-cn">文件的扩展名，不包含“.”，比如txt、png</div>
     */
    public static String getExtension(String file)
    {
        return FilenameUtils.getExtension(file);
    }

    /**
     * <div lang="zh-cn">得到文件路径file中的文件扩展名。比如"c:/a/b/www.txt"得到"txt"。</div>
     * @param file
     * @return <div lang="zh-cn">文件的扩展名，不包含“.”，比如txt、png</div>
     */
    public static String getExtension(File file)
    {
        return getExtension(file.toString());
    }

    /**
     * <div lang="zh-cn">得到系统临时目录的路径。</div>
     * @return
     */
    public static String getTempDirectory() {
        return FileUtils.getTempDirectoryPath();
    }

    /**
     * <div lang="zh-cn">得到系统当前用户的用户目录的路径。</div>
     * @return
     */
    public static String getUserDirectory() {
        return FileUtils.getUserDirectoryPath();
    }
}
