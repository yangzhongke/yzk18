package com.yzk18.commons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.mozilla.intl.chardet.nsDetector;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class IOHelpers {

    public static String detectTextEncoding(String fileName)
    {
        return detectTextEncoding(new File(fileName));
    }

    public static String detectTextEncoding(File file)
    {
        try(FileInputStream fis =  new FileInputStream(file))
        {
            return detectTextEncoding(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
    public static String readAllText(File file)
    {
        return readAllText(file.toString());
    }

    public static String readAllText(String file)
    {
        String charsetName = detectTextEncoding(file);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        return readAllText(file,charsetName);
    }

    public static String readAllText(File file,String charsetName)
    {
        return  readAllText(file.toString(),charsetName);
    }

    public static String readAllText(String file,String charsetName)
    {
        try(FileInputStream fis = new FileInputStream(file))
        {
            return IOUtils.toString(fis,charsetName);
        }catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

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

    public static String[] readAllLines(File file)
    {
        return readAllLines(file.toString());
    }

    public static String[] readAllLines(String file)
    {
        String charsetName = detectTextEncoding(file);
        if(charsetName==null)
        {
            charsetName = "UTF-8";
        }
        return readAllLines(file,charsetName);
    }

    public static String[] readAllLines(File file,String charsetName)
    {
        return readAllLines(file.toString(),charsetName);
    }

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

    public static byte[] readAllBytes(File file)
    {
        return readAllBytes(file.toString());
    }

    public static byte[] readAllBytes(String file)
    {
        try(FileInputStream fis = new FileInputStream((file)))
        {
            return IOUtils.toByteArray(fis);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readAllBytes(InputStream inputStream)
    {
        try
        {
            return IOUtils.toByteArray(inputStream);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mkDirs(String dir)
    {
        try
        {
            FileUtils.forceMkdir(new File(dir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mkParentDirs(String file)
    {
        try
        {
            FileUtils.forceMkdirParent(new File(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAllText(File file,String text,String charsetName)
    {
        writeAllText(file.toString(),text,charsetName);
    }

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

    public static void writeAllText(File file,String text)
    {
        writeAllText(file.toString(),text);
    }

    public static void writeAllText(String fileName,String text)
    {
        writeAllText(fileName, text,"UTF-8");
    }

    public static void writeAllLines(File file,String[] lines,String charsetName)
    {
        writeAllLines(file.toString(),lines,charsetName);
    }

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

    public static void writeAllLines(File file,String[] lines)
    {
        writeAllLines(file.toString(),lines);
    }

    public static void writeAllLines(String fileName,String[] lines)
    {
        writeAllLines(fileName,lines,"UTF-8");
    }

    public static void writeAllBytes(File file, byte[] bytes)
    {
        writeAllBytes(file.toString(),bytes);
    }

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

    //getFiles、getdirs、deleteDir等不讲，上课时候直接用File等类

    public static InputStream toByteArrayInputStream(InputStream inStream)
    {
        byte[] bytes = readAllBytes(inStream);
        ByteArrayInputStream baInStream = new ByteArrayInputStream(bytes);
        return baInStream;
    }

    public static void deleteDir(String dirName) {
        try {
            FileUtils.deleteDirectory(new File(dirName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFile(String fileName)
    {
        File file =new File(fileName);
        if(file.isDirectory())
        {
            throw new RuntimeException(fileName+" is a directory");
        }
        file.delete();
    }

    public static String[] getFiles(String dir,boolean recursive,String... extensions)
    {
        String[] files = FileUtils.listFiles(new File(dir),extensions,recursive)
                .stream().map(f->f.toString()).toArray(String[]::new);
        return files;
    }

    public static String[] getFiles(String dir,boolean recursive)
    {
        return getFiles(dir, recursive,null);
    }

    public static String[] getFiles(String dir)
    {
        return getFiles(dir,false);
    }

    public static String[] getDirs(String dir)
    {
        return Arrays.stream(new File(dir).listFiles((FileFilter)DirectoryFileFilter.INSTANCE))
            .map(f->f.toString()).toArray(String[]::new);
    }

    public static boolean exists(String file)
    {
        return new File(file).exists();
    }

    public static String getParentDir(String file)
    {
        return new File(file).getParent();
    }

    public static String getFileNameWithoutExtension(String file)
    {
        return FilenameUtils.getBaseName(file);
    }

    public static String getFileName(String file)
    {
        return FilenameUtils.getName(file);
    }

    public static String getExtension(String file)
    {
        return FilenameUtils.getExtension(file);
    }

    public static String getTempDirectory() {
        return FileUtils.getTempDirectoryPath();
    }

    public static String getUserDirectory() {
        return FileUtils.getUserDirectoryPath();
    }
}
