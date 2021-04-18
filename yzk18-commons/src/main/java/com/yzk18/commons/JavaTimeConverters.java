package com.yzk18.commons;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
            if (!(value instanceof String)) {
                throw conversionException(type, value);
            }
            String valueAsString = (String) value;
            if (valueAsString.length() > MAX_LOCAL_DATE_LENGTH) {
                valueAsString = valueAsString.substring(0, MAX_LOCAL_DATE_LENGTH);
            }
            return type.cast(LocalDate.parse(valueAsString,dtf));
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
            if (!(value instanceof String)) {
                throw conversionException(type, value);
            }
            String paramValueString = (String) value;
            try {
                return type.cast(LocalDateTime.parse(paramValueString,dtf));
            } catch (DateTimeParseException e) {
                //We drop the timezone info from the String:
                return type.cast(ZonedDateTime.parse(paramValueString).toLocalDateTime());
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
