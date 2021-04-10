package com.cedarsoftware.util.io;

import java.math.*;
import java.util.concurrent.atomic.*;
import java.text.*;
import java.util.regex.*;
import java.util.*;
import java.sql.*;

public class Readers
{
    private static final String DAYS = "(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thur|thu|friday|fri|saturday|sat|sunday|sun)";
    private static final String MOS = "(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sept|Sep|October|Oct|November|Nov|December|Dec)";
    private static final Pattern datePattern1;
    private static final Pattern datePattern2;
    private static final Pattern datePattern3;
    private static final Pattern datePattern4;
    private static final Pattern datePattern5;
    private static final Pattern datePattern6;
    private static final Pattern dayPattern;
    private static final Map<String, String> months;
    private static final Pattern timePattern1;
    private static final Pattern timePattern2;
    private static final Pattern timePattern3;
    
    static {
        datePattern1 = Pattern.compile("(\\d{4})[./-](\\d{1,2})[./-](\\d{1,2})");
        datePattern2 = Pattern.compile("(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})");
        datePattern3 = Pattern.compile("(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sept|Sep|October|Oct|November|Nov|December|Dec)[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*(\\d{4})", 2);
        datePattern4 = Pattern.compile("(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sept|Sep|October|Oct|November|Nov|December|Dec)[ ]*[,]?[ ]*(\\d{4})", 2);
        datePattern5 = Pattern.compile("(\\d{4})[ ]*[,]?[ ]*(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sept|Sep|October|Oct|November|Nov|December|Dec)[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)", 2);
        datePattern6 = Pattern.compile("(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thur|thu|friday|fri|saturday|sat|sunday|sun)[ ]+(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sept|Sep|October|Oct|November|Nov|December|Dec)[ ]+(\\d{1,2})[ ]+(\\d{2}:\\d{2}:\\d{2})[ ]+[A-Z]{1,4}\\s+(\\d{4})", 2);
        timePattern1 = Pattern.compile("(\\d{2})[.:](\\d{2})[.:](\\d{2})[.](\\d{1,10})([+-]\\d{2}[:]?\\d{2}|Z)?");
        timePattern2 = Pattern.compile("(\\d{2})[.:](\\d{2})[.:](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");
        timePattern3 = Pattern.compile("(\\d{2})[.:](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");
        dayPattern = Pattern.compile("(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thur|thu|friday|fri|saturday|sat|sunday|sun)", 2);
        (months = new LinkedHashMap<String, String>()).put("jan", "1");
        Readers.months.put("january", "1");
        Readers.months.put("feb", "2");
        Readers.months.put("february", "2");
        Readers.months.put("mar", "3");
        Readers.months.put("march", "3");
        Readers.months.put("apr", "4");
        Readers.months.put("april", "4");
        Readers.months.put("may", "5");
        Readers.months.put("jun", "6");
        Readers.months.put("june", "6");
        Readers.months.put("jul", "7");
        Readers.months.put("july", "7");
        Readers.months.put("aug", "8");
        Readers.months.put("august", "8");
        Readers.months.put("sep", "9");
        Readers.months.put("sept", "9");
        Readers.months.put("september", "9");
        Readers.months.put("oct", "10");
        Readers.months.put("october", "10");
        Readers.months.put("nov", "11");
        Readers.months.put("november", "11");
        Readers.months.put("dec", "12");
        Readers.months.put("december", "12");
    }
    
    private Readers() {
    }
    
    public static BigDecimal bigDecimalFrom(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal)o;
        }
        if (o instanceof String) {
            o = o;
            if ("".equals(((String)o).trim())) {
                return null;
            }
            try {
                return new BigDecimal(MetaUtils.removeLeadingAndTrailingQuotes((String)o));
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder("Could not parse '");
                sb.append((String)o);
                sb.append("' as BigDecimal.");
                throw new JsonIoException(sb.toString(), ex);
            }
        }
        if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger)o);
        }
        if (o instanceof Boolean) {
            if (o) {
                return BigDecimal.ONE;
            }
            return BigDecimal.ZERO;
        }
        else {
            if (!(o instanceof Long) && !(o instanceof Integer) && !(o instanceof Double) && !(o instanceof Short) && !(o instanceof Byte) && !(o instanceof Float)) {
                final StringBuilder sb2 = new StringBuilder("Could not convert value: ");
                sb2.append(o.toString());
                sb2.append(" to BigInteger.");
                throw new JsonIoException(sb2.toString());
            }
            return new BigDecimal(o.toString());
        }
    }
    
    public static BigInteger bigIntegerFrom(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof BigInteger) {
            return (BigInteger)o;
        }
        if (o instanceof String) {
            final String s = (String)o;
            if ("".equals(s.trim())) {
                return null;
            }
            try {
                return new BigInteger(MetaUtils.removeLeadingAndTrailingQuotes(s));
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder("Could not parse '");
                sb.append(o);
                sb.append("' as BigInteger.");
                throw new JsonIoException(sb.toString(), ex);
            }
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal)o).toBigInteger();
        }
        if (o instanceof Boolean) {
            if (o) {
                return BigInteger.ONE;
            }
            return BigInteger.ZERO;
        }
        else {
            if (o instanceof Double || o instanceof Float) {
                return new BigDecimal(((Number)o).doubleValue()).toBigInteger();
            }
            if (!(o instanceof Long) && !(o instanceof Integer) && !(o instanceof Short) && !(o instanceof Byte)) {
                final StringBuilder sb2 = new StringBuilder("Could not convert value: ");
                sb2.append(o.toString());
                sb2.append(" to BigInteger.");
                throw new JsonIoException(sb2.toString());
            }
            return new BigInteger(o.toString());
        }
    }
    
    static Class classForName(final String s, final ClassLoader classLoader) {
        return MetaUtils.classForName(s, classLoader);
    }
    
    private static Object getValueFromJsonObject(final Object o, final Object o2, final String s) {
        if (!(o instanceof JsonObject)) {
            return o2;
        }
        final JsonObject jsonObject = (JsonObject)o;
        if (jsonObject.containsKey("value")) {
            return jsonObject.get("value");
        }
        final StringBuilder sb = new StringBuilder(String.valueOf(s));
        sb.append(" defined as JSON {} object, missing 'value' field");
        throw new JsonIoException(sb.toString());
    }
    
    static Object newInstance(final Class clazz, final JsonObject jsonObject) {
        return JsonReader.newInstance(clazz, jsonObject);
    }
    
    public static class AtomicBooleanReader implements JsonClassReaderEx
    {
        @Override
        public Object read(Object access$11, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            access$11 = getValueFromJsonObject(access$11, access$11, "AtomicBoolean");
            if (access$11 instanceof String) {
                final String s = (String)access$11;
                if ("".equals(s.trim())) {
                    return null;
                }
                return new AtomicBoolean("true".equalsIgnoreCase(s));
            }
            else {
                if (access$11 instanceof Boolean) {
                    return new AtomicBoolean((boolean)access$11);
                }
                if (access$11 instanceof Number && !(access$11 instanceof Double) && !(access$11 instanceof Float)) {
                    return new AtomicBoolean(((Number)access$11).longValue() != 0L);
                }
                final StringBuilder sb = new StringBuilder("Unknown value in JSON assigned to AtomicBoolean, value type = ");
                sb.append(access$11.getClass().getName());
                throw new JsonIoException(sb.toString());
            }
        }
    }
    
    public static class AtomicIntegerReader implements JsonClassReaderEx
    {
        @Override
        public Object read(Object access$11, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            access$11 = getValueFromJsonObject(access$11, access$11, "AtomicInteger");
            if (access$11 instanceof String) {
                final String s = (String)access$11;
                if ("".equals(s.trim())) {
                    return null;
                }
                return new AtomicInteger(Integer.parseInt(MetaUtils.removeLeadingAndTrailingQuotes(s)));
            }
            else {
                if (access$11 instanceof Number && !(access$11 instanceof Double) && !(access$11 instanceof Float)) {
                    return new AtomicInteger(((Number)access$11).intValue());
                }
                final StringBuilder sb = new StringBuilder("Unknown value in JSON assigned to AtomicInteger, value type = ");
                sb.append(access$11.getClass().getName());
                throw new JsonIoException(sb.toString());
            }
        }
    }
    
    public static class AtomicLongReader implements JsonClassReaderEx
    {
        @Override
        public Object read(Object access$11, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            access$11 = getValueFromJsonObject(access$11, access$11, "AtomicLong");
            if (access$11 instanceof String) {
                final String s = (String)access$11;
                if ("".equals(s.trim())) {
                    return null;
                }
                return new AtomicLong(Long.parseLong(MetaUtils.removeLeadingAndTrailingQuotes(s)));
            }
            else {
                if (access$11 instanceof Number && !(access$11 instanceof Double) && !(access$11 instanceof Float)) {
                    return new AtomicLong(((Number)access$11).longValue());
                }
                final StringBuilder sb = new StringBuilder("Unknown value in JSON assigned to AtomicLong, value type = ");
                sb.append(access$11.getClass().getName());
                throw new JsonIoException(sb.toString());
            }
        }
    }
    
    public static class BigDecimalReader implements JsonClassReaderEx
    {
        @Override
        public Object read(Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            JsonObject<Object, Object> jsonObject = null;
            Object value = o;
            if (o instanceof JsonObject) {
                jsonObject = (JsonObject<Object, Object>)o;
                if (!jsonObject.containsKey("value")) {
                    throw new JsonIoException("BigDecimal missing 'value' field");
                }
                value = jsonObject.get("value");
            }
            o = value;
            if (value instanceof JsonObject) {
                final JsonObject<Object, Object> jsonObject2 = (JsonObject<Object, Object>)value;
                if ("java.math.BigInteger".equals(jsonObject2.type)) {
                    o = new BigIntegerReader().read(value, deque, map);
                }
                else {
                    if (!"java.math.BigDecimal".equals(jsonObject2.type)) {
                        return Readers.bigDecimalFrom(jsonObject2.get("value"));
                    }
                    o = this.read(value, deque, map);
                }
            }
            final BigDecimal bigDecimal = Readers.bigDecimalFrom(o);
            if (jsonObject != null) {
                jsonObject.target = bigDecimal;
            }
            return bigDecimal;
        }
    }
    
    public static class BigIntegerReader implements JsonClassReaderEx
    {
        @Override
        public Object read(Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            JsonObject<Object, Object> jsonObject = null;
            Object value = o;
            if (o instanceof JsonObject) {
                jsonObject = (JsonObject<Object, Object>)o;
                if (!jsonObject.containsKey("value")) {
                    throw new JsonIoException("BigInteger missing 'value' field");
                }
                value = jsonObject.get("value");
            }
            o = value;
            if (value instanceof JsonObject) {
                final JsonObject<Object, Object> jsonObject2 = (JsonObject<Object, Object>)value;
                if ("java.math.BigDecimal".equals(jsonObject2.type)) {
                    o = new BigDecimalReader().read(value, deque, map);
                }
                else {
                    if (!"java.math.BigInteger".equals(jsonObject2.type)) {
                        return Readers.bigIntegerFrom(jsonObject2.get("value"));
                    }
                    o = this.read(value, deque, map);
                }
            }
            final BigInteger bigInteger = Readers.bigIntegerFrom(o);
            if (jsonObject != null) {
                jsonObject.target = bigInteger;
            }
            return bigInteger;
        }
    }
    
    public static class CalendarReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, Deque<JsonObject<String, Object>> s, final Map<String, Object> map) {
            s = null;
            while (true) {
                try {
                    final JsonObject jsonObject = (JsonObject)o;
                    s = s;
                    final String s2 = jsonObject.get("time");
                    if (s2 == null) {
                        s = s2;
                        throw new JsonIoException("Calendar missing 'time' field");
                    }
                    s = s2;
                    final Date parse = MetaUtils.dateFormat.get().parse(s2);
                    s = s2;
                    Class<?> clazz;
                    if (jsonObject.getTarget() != null) {
                        s = s2;
                        clazz = jsonObject.getTarget().getClass();
                    }
                    else {
                        s = s2;
                        clazz = (Class<?>)Readers.classForName(jsonObject.type, map.get("CLASSLOADER"));
                    }
                    s = s2;
                    final Calendar target = (Calendar)Readers.newInstance(clazz, jsonObject);
                    s = s2;
                    target.setTime(parse);
                    s = s2;
                    jsonObject.setTarget(target);
                    s = s2;
                    final String s3 = jsonObject.get("zone");
                    if (s3 != null) {
                        s = s2;
                        target.setTimeZone(TimeZone.getTimeZone(s3));
                    }
                    return target;
                    final StringBuilder sb = new StringBuilder("Failed to parse calendar, time: ");
                    sb.append(s);
                    throw new JsonIoException(sb.toString());
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public static class ClassReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            if (o instanceof String) {
                return Readers.classForName((String)o, map.get("CLASSLOADER"));
            }
            final JsonObject jsonObject = (JsonObject)o;
            if (jsonObject.containsKey("value")) {
                return jsonObject.target = Readers.classForName((String)jsonObject.get("value"), map.get("CLASSLOADER"));
            }
            throw new JsonIoException("Class missing 'value' field");
        }
    }
    
    public static class DateReader implements JsonClassReaderEx
    {
        private Date parseDate(String s) {
            final String trim = s.trim();
            if (trim.isEmpty()) {
                return null;
            }
            final Matcher matcher = Readers.datePattern1.matcher(trim);
            String s2 = null;
            String s3 = null;
            String s4;
            String s5;
            if (matcher.find()) {
                s = matcher.group(1);
                s2 = matcher.group(2);
                s4 = matcher.group(3);
                s5 = matcher.replaceFirst("");
            }
            else {
                final Matcher matcher2 = Readers.datePattern2.matcher(trim);
                if (matcher2.find()) {
                    s2 = matcher2.group(1);
                    s4 = matcher2.group(2);
                    s = matcher2.group(3);
                    s5 = matcher2.replaceFirst("");
                }
                else {
                    final Matcher matcher3 = Readers.datePattern3.matcher(trim);
                    if (matcher3.find()) {
                        s3 = matcher3.group(1);
                        s4 = matcher3.group(2);
                        s = matcher3.group(4);
                        s5 = matcher3.replaceFirst("");
                    }
                    else {
                        final Matcher matcher4 = Readers.datePattern4.matcher(trim);
                        if (matcher4.find()) {
                            s4 = matcher4.group(1);
                            s3 = matcher4.group(3);
                            s = matcher4.group(4);
                            s5 = matcher4.replaceFirst("");
                        }
                        else {
                            final Matcher matcher5 = Readers.datePattern5.matcher(trim);
                            if (matcher5.find()) {
                                s = matcher5.group(1);
                                s3 = matcher5.group(2);
                                s4 = matcher5.group(3);
                                s5 = matcher5.replaceFirst("");
                            }
                            else {
                                final Matcher matcher6 = Readers.datePattern6.matcher(trim);
                                if (!matcher6.find()) {
                                    final StringBuilder sb = new StringBuilder("Unable to parse: ");
                                    sb.append(trim);
                                    throw new JsonIoException(sb.toString());
                                }
                                s = matcher6.group(5);
                                s3 = matcher6.group(2);
                                s4 = matcher6.group(3);
                                s5 = matcher6.group(4);
                            }
                        }
                    }
                }
            }
            String s6 = s2;
            if (s3 != null) {
                s6 = Readers.months.get(s3.trim().toLowerCase());
            }
            String s7 = null;
            String s8 = null;
            final String s9 = "00";
            final String s10 = "0";
            String s11 = null;
            String s12 = s5.trim();
            final Matcher matcher7 = Readers.timePattern1.matcher(s12);
            Matcher matcher8;
            String s13;
            String s14;
            if (matcher7.find()) {
                final String group = matcher7.group(1);
                final String group2 = matcher7.group(2);
                final String group3 = matcher7.group(3);
                final String group4 = matcher7.group(4);
                matcher8 = matcher7;
                s7 = group;
                s8 = group2;
                s13 = group3;
                s14 = group4;
                if (matcher7.groupCount() > 4) {
                    s11 = matcher7.group(5);
                    s14 = group4;
                    s13 = group3;
                    s8 = group2;
                    s7 = group;
                    matcher8 = matcher7;
                }
            }
            else {
                final Matcher matcher9 = Readers.timePattern2.matcher(s12);
                if (matcher9.find()) {
                    final String group5 = matcher9.group(1);
                    final String group6 = matcher9.group(2);
                    final String group7 = matcher9.group(3);
                    matcher8 = matcher9;
                    s7 = group5;
                    s8 = group6;
                    s13 = group7;
                    s14 = s10;
                    if (matcher9.groupCount() > 3) {
                        s11 = matcher9.group(4);
                        matcher8 = matcher9;
                        s7 = group5;
                        s8 = group6;
                        s13 = group7;
                        s14 = s10;
                    }
                }
                else {
                    final Matcher matcher10 = Readers.timePattern3.matcher(s12);
                    if (matcher10.find()) {
                        final String group8 = matcher10.group(1);
                        final String group9 = matcher10.group(2);
                        matcher8 = matcher10;
                        s7 = group8;
                        s8 = group9;
                        s13 = s9;
                        s14 = s10;
                        if (matcher10.groupCount() > 2) {
                            s11 = matcher10.group(3);
                            matcher8 = matcher10;
                            s7 = group8;
                            s8 = group9;
                            s13 = s9;
                            s14 = s10;
                        }
                    }
                    else {
                        matcher8 = null;
                        s13 = s9;
                        s14 = s10;
                    }
                }
            }
            if (matcher8 != null) {
                s12 = matcher8.replaceFirst("");
            }
            String trim2;
            if ((trim2 = s12) != null) {
                trim2 = s12;
                if (s12.length() > 0) {
                    final Matcher matcher11 = Readers.dayPattern.matcher(s12);
                    trim2 = s12;
                    if (matcher11.find()) {
                        trim2 = matcher11.replaceFirst("").trim();
                    }
                }
            }
            final String s15;
            if ((s15 = trim2) != null && trim2.length() > 0) {
                final String trim3 = trim2.trim();
                if (!trim3.equals(",") && !trim3.equals("T")) {
                    final StringBuilder sb2 = new StringBuilder("Issue parsing data/time, other characters present: ");
                    sb2.append(trim3);
                    throw new JsonIoException(sb2.toString());
                }
            }
            final Calendar instance = Calendar.getInstance();
            instance.clear();
            if (s11 != null) {
                if ("z".equalsIgnoreCase(s11)) {
                    instance.setTimeZone(TimeZone.getTimeZone("GMT"));
                }
                else {
                    final StringBuilder sb3 = new StringBuilder("GMT");
                    sb3.append(s11);
                    instance.setTimeZone(TimeZone.getTimeZone(sb3.toString()));
                }
            }
            final int int1 = Integer.parseInt(s);
            final int n = Integer.parseInt(s6) - 1;
            final int int2 = Integer.parseInt(s4);
            if (n < 0 || n > 11) {
                final StringBuilder sb4 = new StringBuilder("Month must be between 1 and 12 inclusive, date: ");
                sb4.append(trim);
                throw new JsonIoException(sb4.toString());
            }
            if (int2 >= 1 && int2 <= 31) {
                if (matcher8 == null) {
                    instance.set(int1, n, int2);
                }
                else {
                    final int int3 = Integer.parseInt(s7);
                    final int int4 = Integer.parseInt(s8);
                    final int int5 = Integer.parseInt(s13);
                    final int int6 = Integer.parseInt(s14);
                    if (int3 > 23) {
                        final StringBuilder sb5 = new StringBuilder("Hour must be between 0 and 23 inclusive, time: ");
                        sb5.append(trim);
                        throw new JsonIoException(sb5.toString());
                    }
                    if (int4 > 59) {
                        final StringBuilder sb6 = new StringBuilder("Minute must be between 0 and 59 inclusive, time: ");
                        sb6.append(trim);
                        throw new JsonIoException(sb6.toString());
                    }
                    if (int5 > 59) {
                        final StringBuilder sb7 = new StringBuilder("Second must be between 0 and 59 inclusive, time: ");
                        sb7.append(trim);
                        throw new JsonIoException(sb7.toString());
                    }
                    instance.set(int1, n, int2, int3, int4, int5);
                    instance.set(14, int6);
                }
                return instance.getTime();
            }
            final StringBuilder sb8 = new StringBuilder("Day must be between 1 and 31 inclusive, date: ");
            sb8.append(trim);
            throw new JsonIoException(sb8.toString());
        }
        
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            if (o instanceof Long) {
                return new Date((long)o);
            }
            if (o instanceof String) {
                return this.parseDate((String)o);
            }
            if (!(o instanceof JsonObject)) {
                final StringBuilder sb = new StringBuilder("Unable to parse date, encountered unknown object: ");
                sb.append(o);
                throw new JsonIoException(sb.toString());
            }
            final Object value = ((JsonObject)o).get("value");
            if (value instanceof Long) {
                return new Date((long)value);
            }
            if (value instanceof String) {
                return this.parseDate((String)value);
            }
            final StringBuilder sb2 = new StringBuilder("Unable to parse date: ");
            sb2.append(o);
            throw new JsonIoException(sb2.toString());
        }
    }
    
    public static class LocaleReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            final JsonObject jsonObject = (JsonObject)o;
            final Object value = jsonObject.get("language");
            if (value == null) {
                throw new JsonIoException("java.util.Locale must specify 'language' field");
            }
            final Object value2 = jsonObject.get("country");
            final Object value3 = jsonObject.get("variant");
            if (value2 == null) {
                return jsonObject.target = new Locale((String)value);
            }
            if (value3 == null) {
                return jsonObject.target = new Locale((String)value, (String)value2);
            }
            return jsonObject.target = new Locale((String)value, (String)value2, (String)value3);
        }
    }
    
    public static class SqlDateReader extends DateReader
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            return new java.sql.Date(((Date)super.read(o, deque, map)).getTime());
        }
    }
    
    public static class StringBufferReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            if (o instanceof String) {
                return new StringBuffer((String)o);
            }
            final JsonObject jsonObject = (JsonObject)o;
            if (jsonObject.containsKey("value")) {
                return jsonObject.target = new StringBuffer((String)jsonObject.get("value"));
            }
            throw new JsonIoException("StringBuffer missing 'value' field");
        }
    }
    
    public static class StringBuilderReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            if (o instanceof String) {
                return new StringBuilder((String)o);
            }
            final JsonObject jsonObject = (JsonObject)o;
            if (jsonObject.containsKey("value")) {
                return jsonObject.target = new StringBuilder((String)jsonObject.get("value"));
            }
            throw new JsonIoException("StringBuilder missing 'value' field");
        }
    }
    
    public static class StringReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            if (o instanceof String) {
                return o;
            }
            if (MetaUtils.isPrimitive(o.getClass())) {
                return o.toString();
            }
            final JsonObject jsonObject = (JsonObject)o;
            if (jsonObject.containsKey("value")) {
                return jsonObject.target = jsonObject.get("value");
            }
            throw new JsonIoException("String missing 'value' field");
        }
    }
    
    public static class TimeZoneReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            final JsonObject jsonObject = (JsonObject)o;
            final Object value = jsonObject.get("zone");
            if (value == null) {
                throw new JsonIoException("java.util.TimeZone must specify 'zone' field");
            }
            return jsonObject.target = TimeZone.getTimeZone((String)value);
        }
    }
    
    public static class TimestampReader implements JsonClassReaderEx
    {
        @Override
        public Object read(final Object o, final Deque<JsonObject<String, Object>> deque, final Map<String, Object> map) {
            final JsonObject jsonObject = (JsonObject)o;
            final Object value = jsonObject.get("time");
            if (value == null) {
                throw new JsonIoException("java.sql.Timestamp must specify 'time' field");
            }
            final Object value2 = jsonObject.get("nanos");
            if (value2 == null) {
                return jsonObject.target = new Timestamp(Long.valueOf((String)value));
            }
            final Timestamp target = new Timestamp(Long.valueOf((String)value));
            target.setNanos(Integer.valueOf((String)value2));
            return jsonObject.target = target;
        }
    }
}
