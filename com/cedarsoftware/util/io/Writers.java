package com.cedarsoftware.util.io;

import java.util.concurrent.atomic.*;
import java.math.*;
import java.text.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class Writers
{
    static final String DATE_FORMAT = "DATE_FORMAT";
    
    private Writers() {
    }
    
    protected static void writeJsonUtf8String(final String s, final Writer writer) throws IOException {
        JsonWriter.writeJsonUtf8String(s, writer);
    }
    
    public static class AtomicBooleanWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            if (b) {
                final AtomicBoolean atomicBoolean = (AtomicBoolean)o;
                writer.write("\"value\":");
                writer.write(atomicBoolean.toString());
                return;
            }
            this.writePrimitiveForm(o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            writer.write(((AtomicBoolean)o).toString());
        }
    }
    
    public static class AtomicIntegerWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            if (b) {
                final AtomicInteger atomicInteger = (AtomicInteger)o;
                writer.write("\"value\":");
                writer.write(atomicInteger.toString());
                return;
            }
            this.writePrimitiveForm(o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            writer.write(((AtomicInteger)o).toString());
        }
    }
    
    public static class AtomicLongWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            if (b) {
                final AtomicLong atomicLong = (AtomicLong)o;
                writer.write("\"value\":");
                writer.write(atomicLong.toString());
                return;
            }
            this.writePrimitiveForm(o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            writer.write(((AtomicLong)o).toString());
        }
    }
    
    public static class BigDecimalWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            if (b) {
                final BigDecimal bigDecimal = (BigDecimal)o;
                writer.write("\"value\":\"");
                writer.write(bigDecimal.toPlainString());
                writer.write(34);
                return;
            }
            this.writePrimitiveForm(o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            final BigDecimal bigDecimal = (BigDecimal)o;
            writer.write(34);
            writer.write(bigDecimal.toPlainString());
            writer.write(34);
        }
    }
    
    public static class BigIntegerWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            if (b) {
                final BigInteger bigInteger = (BigInteger)o;
                writer.write("\"value\":\"");
                writer.write(bigInteger.toString(10));
                writer.write(34);
                return;
            }
            this.writePrimitiveForm(o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            final BigInteger bigInteger = (BigInteger)o;
            writer.write(34);
            writer.write(bigInteger.toString(10));
            writer.write(34);
        }
    }
    
    public static class CalendarWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return false;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final Calendar calendar = (Calendar)o;
            MetaUtils.dateFormat.get().setTimeZone(calendar.getTimeZone());
            writer.write("\"time\":\"");
            writer.write(MetaUtils.dateFormat.get().format(calendar.getTime()));
            writer.write("\",\"zone\":\"");
            writer.write(calendar.getTimeZone().getID());
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
        }
    }
    
    public static class ClassWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final String name = ((Class)o).getName();
            writer.write("\"value\":");
            Writers.writeJsonUtf8String(name, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            Writers.writeJsonUtf8String(((Class)o).getName(), writer);
        }
    }
    
    public static class DateWriter implements JsonClassWriter, JsonClassWriterEx
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            throw new JsonIoException("Should never be called.");
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer, final Map map) throws IOException {
            final Date date = (Date)o;
            Serializable value;
            final String s = (String)(value = map.get("DATE_FORMAT"));
            if (s instanceof String) {
                value = new SimpleDateFormat(s, Locale.ENGLISH);
                map.put("DATE_FORMAT", value);
            }
            if (b) {
                writer.write("\"value\":");
            }
            if (value instanceof Format) {
                writer.write("\"");
                writer.write(((Format)value).format(date));
                writer.write("\"");
                return;
            }
            writer.write(Long.toString(((Date)o).getTime()));
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            throw new JsonIoException("Should never be called.");
        }
        
        public void writePrimitiveForm(final Object o, final Writer writer, final Map map) throws IOException {
            if (map.containsKey("DATE_FORMAT")) {
                this.write(o, false, writer, map);
                return;
            }
            writer.write(Long.toString(((Date)o).getTime()));
        }
    }
    
    public static class JsonStringWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            writer.write("\"value\":");
            Writers.writeJsonUtf8String((String)o, writer);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            Writers.writeJsonUtf8String((String)o, writer);
        }
    }
    
    public static class LocaleWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return false;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final Locale locale = (Locale)o;
            writer.write("\"language\":\"");
            writer.write(locale.getLanguage());
            writer.write("\",\"country\":\"");
            writer.write(locale.getCountry());
            writer.write("\",\"variant\":\"");
            writer.write(locale.getVariant());
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
        }
    }
    
    public static class StringBufferWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final StringBuffer sb = (StringBuffer)o;
            writer.write("\"value\":\"");
            writer.write(sb.toString());
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            final StringBuffer sb = (StringBuffer)o;
            writer.write(34);
            writer.write(sb.toString());
            writer.write(34);
        }
    }
    
    public static class StringBuilderWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return true;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final StringBuilder sb = (StringBuilder)o;
            writer.write("\"value\":\"");
            writer.write(sb.toString());
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
            final StringBuilder sb = (StringBuilder)o;
            writer.write(34);
            writer.write(sb.toString());
            writer.write(34);
        }
    }
    
    public static class TimeZoneWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return false;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final TimeZone timeZone = (TimeZone)o;
            writer.write("\"zone\":\"");
            writer.write(timeZone.getID());
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
        }
    }
    
    public static class TimestampWriter implements JsonClassWriter
    {
        @Override
        public boolean hasPrimitiveForm() {
            return false;
        }
        
        @Override
        public void write(final Object o, final boolean b, final Writer writer) throws IOException {
            final Timestamp timestamp = (Timestamp)o;
            writer.write("\"time\":\"");
            writer.write(Long.toString(timestamp.getTime() / 1000L * 1000L));
            writer.write("\",\"nanos\":\"");
            writer.write(Integer.toString(timestamp.getNanos()));
            writer.write(34);
        }
        
        @Override
        public void writePrimitiveForm(final Object o, final Writer writer) throws IOException {
        }
    }
}
