package com.yzk18.commons;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * <div lang="zh-cn">代表SQL查询结果集中的一行记录。</div>
 */
public class JDBCRow {
    private LinkedHashMap<String,Object> data=new LinkedHashMap<>();

    void setObject(String name,Object value)
    {
        // name is case-insensitive
        String lowerCaseName = name.toLowerCase();
        if(value instanceof Clob)
        {
            Clob clobValue = (Clob)value;
            try
            {
                String strValue = IOUtils.toString(clobValue.getCharacterStream());
                data.put(lowerCaseName,strValue);
            } catch (IOException|SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        if(value instanceof Blob)
        {
            Blob blobValue = (Blob)value;
            try
            {
                byte[] bytes = IOUtils.toByteArray(blobValue.getBinaryStream());
                data.put(lowerCaseName,bytes);
            } catch (IOException|SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        else
        {
            data.put(lowerCaseName,value);
        }
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Object getObject(String name)
    {
        //case insensitive
        return data.get(name.toLowerCase());
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为String类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public String getString(String name)
    {
        return CommonHelpers.convert(getObject(name),String.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Integer类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Integer getInt(String name)
    {
        return CommonHelpers.convert(getObject(name),Integer.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Float类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Float getFloat(String name)
    {
        return CommonHelpers.convert(getObject(name),Float.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Double类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Double getDouble(String name)
    {
        return CommonHelpers.convert(getObject(name),Double.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Long类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Long getLong(String name)
    {
        return CommonHelpers.convert(getObject(name),Long.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Short类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Short getShort(String name)
    {
        return CommonHelpers.convert(getObject(name),Short.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Byte类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Byte getByte(String name)
    {
        return CommonHelpers.convert(getObject(name),Byte.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为UUID类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public UUID getUUID(String name)
    {
        return CommonHelpers.convert(getObject(name),UUID.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为byte[]类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public byte[] getBytes(String name)
    {
        return CommonHelpers.convert(getObject(name),byte[].class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为Boolean类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public Boolean getBoolean(String name)
    {
        return CommonHelpers.convert(getObject(name),Boolean.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为LocalDate类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public LocalDate getLocalDate(String name)
    {
        return CommonHelpers.convert(getObject(name),LocalDate.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为LocalDateTime类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public LocalDateTime getLocalDateTime(String name)
    {
        return CommonHelpers.convert(getObject(name),LocalDateTime.class);
    }

    /**
     * <div lang="zh-cn">获取名字为name的列的值，并且尝试转换为LocalTime类型。</div>
     * @param name <div lang="zh-cn">列名，大小写不敏感。</div>
     * @return
     */
    public LocalTime getLocalTime(String name)
    {
        return CommonHelpers.convert(getObject(name),LocalTime.class);
    }

    @Override
    public String toString() {
        String[] strs = this.data.entrySet().stream().map(e->e.getKey()+":"+e.getValue())
                .toArray(String[]::new);
        return String.join(",",strs);
    }
}
