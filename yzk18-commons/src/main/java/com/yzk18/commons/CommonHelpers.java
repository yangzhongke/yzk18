package com.yzk18.commons;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.beanutils.ConvertUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;


public class CommonHelpers {
    static
    {
        final String[] datePatterns = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd HH:mm", "yyyy-MM-dd",
                "HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS" ,"HH:mm"};
        JavaTimeConverters.registerAll(datePatterns);
    }

    /**
     * <div lang="zh-cn">
     *  创建可以对File、LocalDate、LocalTime、LocalDate等类型进行Json处理的Gson对象。
     * </div>
     * @return
     */
    public static Gson createGson()
    {
        GsonBuilder gsonBuilder = Converters.registerAll(new GsonBuilder());
        gsonBuilder.registerTypeAdapter(File.class, new FileJsonConverter());
        Gson gson = gsonBuilder.create();
        return gson;
    }

    /**
     * <div lang="zh-cn">把Json字符串解析为clz类型对象（兼容File、LocalDate、LocalTime、LocalDate等类型）</div>
     * @param jsonString
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String jsonString,Class<T> clz)
    {
        return (T)createGson().fromJson(jsonString,clz);
    }

    /**
     * <div lang="zh-cn">把对象转换为Json字符串（兼容File、LocalDate、LocalTime、LocalDate等类型）</div>
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj)
    {
        return createGson().toJson(obj);
    }

    /**
     * <div lang="zh-cn">把对象obj转换为可读性强的字符串。</div>
     * @param obj
     * @return
     */
    public static String toString(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        else if(obj instanceof Double)
        {
            return toString((Double) obj);
        }
        else if(obj instanceof Float)
        {
            return toString((Float) obj);
        }
        //avoid double quotes of String by Gson
        else if(obj instanceof  CharSequence)
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
        else if(obj instanceof File)
        {
            File file = (File)obj;
            return file.toString();
        }
        else
        {
            return toJsonString(obj);
        }
    }

    /**
     * <div lang="zh-cn">把Double类型转换为字符串，保留两位小数</div>
     * @param d
     * @return
     */
    public static String toString(Double d)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

    /**
     * <div lang="zh-cn">把Float类型转换为字符串，保留两位小数</div>
     * @param
     * @return
     */
    public static String toString(Float f)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }

    /**
     * <div lang="zh-cn">生成一个把字符串s重复count遍的字符串。</div>
     * @param s
     * @param count
     * @return
     */
    public static String repeat(String s,int count)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<count;i++)
        {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * <div lang="zh-cn">用可读性强的格式，打印对象obj到标准输出。</div>
     * @param obj
     */
    public static void println(Object obj)
    {
        System.out.println(toString(obj));
    }

    /**
     * <div lang="zh-cn">从标准输入读入一行字符串。</div>
     * @return
     */
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

    /**
     * <div lang="zh-cn">从标准输入读入一行字符串，并且尝试转换为Integer。</div>
     * @return <div lang="zh-cn">转换为的结果，如果输入的不是合法的整数，则返回null。</div>
     */
    public static Integer readInt()
    {
        String s = readLine();
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }

    /**
     * <div lang="zh-cn">判断数组strs是否是长度为0的数组或者为null。</div>
     * @param array
     * @return
     */
    public static <T> boolean isEmpty(T[] array)
    {
        if(array==null)
        {
            return true;
        }
        if(array.length<=0)
        {
            return true;
        }
        return false;
    }

    /**
     * <div lang="zh-cn">关闭c，如果关闭失败，则抛出RuntimeException。</div>
     * @param c
     */
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

    /**
     * <div lang="zh-cn">安静地关闭c，如果关闭失败，则不抛出异常。</div>
     * @param c
     */
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

    /**
     * <div lang="zh-cn">尝试把value转换为targetClass类型地对象。</div>
     * @param value
     * @param targetClass
     * @param <T>
     * @return
     */
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
        return (T) ConvertUtils.convert(value,targetClass);
    }

    /**
     * <div lang="zh-cn">把obj对象转换为Double类型。</div>
     * @param obj
     * @return
     */
    public static Double toDouble(Object obj)
    {
        return (Double)ConvertUtils.convert(obj,Double.class);
    }

    /**
     * <div lang="zh-cn">把obj对象转换为Integer类型。</div>
     * @param obj
     * @return
     */
    public static Integer toInt(Object obj)
    {
        return (Integer)ConvertUtils.convert(obj,Integer.class);
    }

    /**
     * <div lang="zh-cn">把obj对象转换为Boolean类型。</div>
     * @param obj
     * @return
     */
    public static Boolean toBoolean(Object obj)
    {
        return (Boolean)ConvertUtils.convert(obj,Boolean.class);
    }

    /**
     * <div lang="zh-cn">把date对象转换为LocalDate类型。</div>
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * <div lang="zh-cn">把date对象转换为LocalDateTime类型。</div>
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * <div lang="zh-cn">把obj对象转换为LocalDate类型。</div>
     * @param obj
     * @return
     */
    public static LocalDate toLocalDate(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        else if(obj instanceof CharSequence)
        {
            String s = obj.toString();
            if(s.trim().length()<=0)
            {
                return null;
            }
            else
            {
                return (LocalDate)ConvertUtils.convert(s,LocalDate.class);
            }
        }
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

    /**
     * <div lang="zh-cn">把obj对象转换为LocalTime类型。</div>
     * @param obj
     * @return
     */
    public static LocalTime toLocalTime(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        else if(obj instanceof CharSequence)
        {
            String s = obj.toString();
            if(s.trim().length()<=0)
            {
                return null;
            }
            else
            {
                return (LocalTime)ConvertUtils.convert(s,LocalTime.class);
            }
        }
        Object value=ConvertUtils.convert(obj,LocalTime.class);
        if(value instanceof  LocalTime)
        {
            return (LocalTime)value;
        }
        else if(value instanceof Date)
        {
            Date dtValue = (Date)value;
            return dtValue.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
        else
        {
            throw new IllegalArgumentException("unsupported type:"+value.getClass());
        }
    }

    /**
     * <div lang="zh-cn">把obj对象转换为LocalDateTime类型。</div>
     * @param obj
     * @return
     */
    public static LocalDateTime toLocalDateTime(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        else if(obj instanceof CharSequence)
        {
            String s = obj.toString();
            if(s.trim().length()<=0)
            {
                return null;
            }
            else
            {
                return (LocalDateTime)ConvertUtils.convert(s,LocalDateTime.class);
            }
        }
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
