package com.yzk18.GUI;

import com.yzk18.commons.IOHelpers;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

class LocalConfigUtils {

    private static String configFile = IOHelpers.getTempDirectory()
            +"/yzk18-GUI-config.properties";

    public static void loadLastCurrentDir(JFileChooser fileChooser,String id)
    {
        String value = readValue(id+"LastCurrentDir");
        if(value!=null)
        {
            fileChooser.setCurrentDirectory(new File(value));
        }
    }

    public static void saveLastCurrentDir(JFileChooser fileChooser,String id)
    {
        File dir = fileChooser.getCurrentDirectory();
        writeValue(id+"LastCurrentDir",dir.toString());
    }

    private static String readValue(String key)
    {
        return readValue(configFile,key);
    }

    private static String readValue(String filePath, String key)
    {
        Properties pps = new Properties();
        try(InputStream inStream = new FileInputStream(filePath))
        {
            pps.load(inStream);
            String value = pps.getProperty(key);
            return value;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private static void writeValue (String key, String value)
    {
        writeValue(configFile,key,value);
    }

    private static void writeValue (String filePath, String key, String value)
    {
        IOHelpers.mkParentDirs(filePath);
        Properties pps = new Properties();
        if(new File(filePath).exists())
        {
            try(InputStream inStream = new FileInputStream(filePath))
            {
                pps.load(inStream);
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        }

        try(OutputStream out = new FileOutputStream(filePath))
        {
            pps.setProperty(key, value);
            pps.store(out,null);
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
