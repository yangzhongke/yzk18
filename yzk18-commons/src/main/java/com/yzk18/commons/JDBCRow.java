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

    public Object getObject(String name)
    {
        //case insensitive
        return data.get(name.toLowerCase());
    }

    public String getString(String name)
    {
        return CommonHelpers.convert(getObject(name),String.class);
    }


    public Integer getInt(String name)
    {
        return CommonHelpers.convert(getObject(name),Integer.class);
    }

    public Float getFloat(String name)
    {
        return CommonHelpers.convert(getObject(name),Float.class);
    }

    public Double getDouble(String name)
    {
        return CommonHelpers.convert(getObject(name),Double.class);
    }

    public Long getLong(String name)
    {
        return CommonHelpers.convert(getObject(name),Long.class);
    }

    public Short getShort(String name)
    {
        return CommonHelpers.convert(getObject(name),Short.class);
    }

    public Byte getByte(String name)
    {
        return CommonHelpers.convert(getObject(name),Byte.class);
    }

    public UUID getUUID(String name)
    {
        return CommonHelpers.convert(getObject(name),UUID.class);
    }

    public byte[] getBytes(String name)
    {
        return CommonHelpers.convert(getObject(name),byte[].class);
    }

    public Boolean getBoolean(String name)
    {
        return CommonHelpers.convert(getObject(name),Boolean.class);
    }

    public LocalDate getLocalDate(String name)
    {
        return CommonHelpers.convert(getObject(name),LocalDate.class);
    }

    public LocalDateTime getLocalDateTime(String name)
    {
        return CommonHelpers.convert(getObject(name),LocalDateTime.class);
    }

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
