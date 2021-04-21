package com.yzk18.commons;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.ConvertUtils;
import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;


public class CommonHelpers {
    static
    {
        final String[] datePatterns = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd",
                "HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS" };
        JavaTimeConverters.registerAll(datePatterns);
    }

    public static Gson createGson()
    {
        Gson gson = Converters.registerAll(new GsonBuilder()).create();
        return gson;
    }

    public static <T> T parseJson(String jsonString,Class<T> clz)
    {
        return (T)createGson().fromJson(jsonString,clz);
    }

    public static String toJsonString(Object obj)
    {
        return createGson().toJson(obj);
    }

    public static String toString(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        //avoid double quotes of String by Gson
        if(obj instanceof  CharSequence)
        {
            return ((CharSequence)obj).toString();
        }
        else if(obj instanceof  LocalDateTime)
        {
            LocalDateTime dt = (LocalDateTime)obj;
            return dt.toLocalDate().toString()+" "+dt.toLocalTime().toString();
        }
        else if(obj instanceof LocalTime)
        {
            LocalTime time = (LocalTime)obj;
            return time.toString();
        }
        else if(obj instanceof LocalDate)
        {
            LocalDate date = (LocalDate)obj;
            return date.toString();
        }
        else
        {
            return toJsonString(obj);
        }
    }

    public static void println(Object obj)
    {
        System.out.println(toString(obj));
    }

    public static String readLine()
    {
        //https://blog.csdn.net/zbuger/article/details/50933385
        //Scanner cannot be closed,or System.in cannot be reused.
        Scanner scanner = new Scanner(System.in);
        while(!scanner.hasNextLine())
        {
        }
        return scanner.nextLine();
    }

    public static int readInt()
    {
        String s = readLine();
        return Integer.parseInt(s);
    }

    public static boolean isEmpty(String[] strs)
    {
        if(strs==null)
        {
            return true;
        }
        if(strs.length<=0)
        {
            return true;
        }
        return false;
    }

    public static void close(Closeable c)
    {
        if(c!=null)
        {
            try {
                c.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeQuietly(Closeable c)
    {
        if(c!=null)
        {
            try {
                c.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public static <T> T convert(Object value,Class<T> targetClass)
    {
        if(value==null)
        {
            return null;
        }
        if(value!=null&&value.getClass()==targetClass)
        {
            return (T)value;
        }
        return (T)ConvertUtils.convert(value,targetClass);
    }

    public static Double toDouble(Object obj)
    {
        return (Double)ConvertUtils.convert(obj,Double.class);
    }

    public static Integer toInt(Object obj)
    {
        return (Integer)ConvertUtils.convert(obj,Integer.class);
    }

    public static LocalDate toLocalDate(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Object obj)
    {
        Object value=ConvertUtils.convert(obj,LocalDate.class);
        if(value instanceof  LocalDate)
        {
            return (LocalDate)value;
        }
        else if(value instanceof Date)
        {
            Date dtValue = (Date)value;
            return toLocalDate(dtValue);
        }
        else
        {
            throw new IllegalArgumentException("unsupported type:"+value.getClass());
        }
    }

    public static LocalDateTime toLocalDateTime(Object obj)
    {
        Object value=ConvertUtils.convert(obj,LocalDateTime.class);
        if(value instanceof  LocalDateTime)
        {
            return (LocalDateTime)value;
        }
        else if(value instanceof Date)
        {
            Date dtValue = (Date)value;
            return toLocalDateTime(dtValue);
        }
        else
        {
            throw new IllegalArgumentException("unsupported type:"+value.getClass());
        }
    }
}
