package com.yzk18.commons;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

//from:https://www.geek-share.com/detail/2627409564.html
public class JavaTimeConverters {



    public static void registerAll(String[] datePatterns) {
        final DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(datePatterns);
        dateConverter.setTimeZone(TimeZone.getTimeZone("GMT"));
        ConvertUtils.register(dateConverter, Date.class);
        ConvertUtils.register(new LocalDateTimeConverter(datePatterns), LocalDateTime.class);
        ConvertUtils.register(new LocalDateConverter(datePatterns), LocalDate.class);
        ConvertUtils.register(new OffsetDateTimeConverter(datePatterns), OffsetDateTime.class);
    }

    private static class LocalDateConverter extends AbstractConverter {
        private  DateTimeFormatter dtf;
        public LocalDateConverter(String[] datePatterns)
        {
            String[] strs = Arrays.stream(datePatterns).map(p->"["+p+"]").toArray(String[]::new);
            String pattern = String.join("",strs);
            this.dtf = DateTimeFormatter.ofPattern(pattern);
        }

        static final int MAX_LOCAL_DATE_LENGTH = 10;

        @Override
        protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
            if(value instanceof  CharSequence)
            {
                String valueAsString = value.toString();
                if (valueAsString.length() > MAX_LOCAL_DATE_LENGTH) {
                    valueAsString = valueAsString.substring(0, MAX_LOCAL_DATE_LENGTH);
                }
                return type.cast(LocalDate.parse(valueAsString,dtf));
            }
            else if(value instanceof  java.sql.Date)//java.sql.Date should locate before java.util.Date
            {
                java.sql.Date sqlDate = (java.sql.Date)value;
                return type.cast(sqlDate.toLocalDate());
            }
            else if(value instanceof  Date)
            {
                Date date = (Date) value;
                LocalDate localDT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return type.cast(localDT);
            }
            else
            {
                throw conversionException(type, value);
            }
        }

        @Override
        protected Class<?> getDefaultType() {
            return LocalDate.class;
        }
    }

    private static class LocalDateTimeConverter extends AbstractConverter {

        private  DateTimeFormatter dtf;
        public LocalDateTimeConverter(String[] datePatterns)
        {
            String[] strs = Arrays.stream(datePatterns).map(p->"["+p+"]").toArray(String[]::new);
            String pattern = String.join("",strs);
            this.dtf = DateTimeFormatter.ofPattern(pattern);
        }

        @Override
        protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
            if(value instanceof  CharSequence)
            {
                String paramValueString = value.toString();
                try {
                    return type.cast(LocalDateTime.parse(paramValueString,dtf));
                } catch (DateTimeParseException e) {
                    //We drop the timezone info from the String:
                    return type.cast(ZonedDateTime.parse(paramValueString).toLocalDateTime());
                }
            }
            else if(value instanceof  java.sql.Date)//java.sql.Date should locate before java.util.Date
            {
                java.sql.Date sqlDate = (java.sql.Date)value;
                Timestamp timestamp = new Timestamp(sqlDate.getTime());
                return type.cast(timestamp.toLocalDateTime());
            }
            else if(value instanceof  Date)
            {
                Date date = (Date) value;
                LocalDateTime localDT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                return type.cast(localDT);
            }
            else
            {
                throw conversionException(type, value);
            }

        }

        @Override
        protected Class<?> getDefaultType() {
            return LocalDateTime.class;
        }
    }

    private static class OffsetDateTimeConverter extends AbstractConverter {

        private  DateTimeFormatter dtf;
        public OffsetDateTimeConverter(String[] datePatterns)
        {
            String[] strs = Arrays.stream(datePatterns).map(p->"["+p+"]").toArray(String[]::new);
            String pattern = String.join("",strs);
            this.dtf = DateTimeFormatter.ofPattern(pattern);
        }

        @Override
        protected <T> T convertToType(Class<T> type, Object value) throws Throwable {
            if (!(value instanceof String)) {
                throw conversionException(type, value);
            }
            return type.cast(OffsetDateTime.parse((CharSequence) value,dtf));
        }

        @Override
        protected Class<?> getDefaultType() {
            return OffsetDateTime.class;
        }
    }
}
