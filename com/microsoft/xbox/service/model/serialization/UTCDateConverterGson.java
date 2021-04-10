package com.microsoft.xbox.service.model.serialization;

import com.microsoft.xbox.toolkit.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import com.google.gson.*;
import java.text.*;

public class UTCDateConverterGson
{
    private static final int NO_MS_STRING_LENGTH = 19;
    private static SimpleDateFormat defaultFormatMs;
    private static SimpleDateFormat defaultFormatNoMs;
    private static SimpleDateFormat shortDateAlternateFormat;
    private static SimpleDateFormat shortDateFormat;
    
    static {
        UTCDateConverterGson.defaultFormatNoMs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        UTCDateConverterGson.defaultFormatMs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        UTCDateConverterGson.shortDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        UTCDateConverterGson.shortDateAlternateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    }
    
    public static Date convert(String s) {
    Label_0122_Outer:
        while (true) {
            while (true) {
            Label_0193:
                while (true) {
                    Label_0188: {
                        synchronized (UTCDateConverterGson.class) {
                            if (!JavaUtil.isNullOrEmpty((String)s)) {
                                Serializable s2 = s;
                                if (((String)s).endsWith("Z")) {
                                    s2 = ((String)s).replace("Z", "");
                                }
                                if (((String)s2).endsWith("+00:00")) {
                                    s = ((String)s2).replace("+00:00", "");
                                    break Label_0188;
                                }
                                if (((String)s2).endsWith("+01:00")) {
                                    s = ((String)s2).replace("+01:00", "");
                                    s2 = TimeZone.getTimeZone("GMT+01:00");
                                    if (((String)s).length() == 19) {
                                        final int n = 1;
                                        Serializable timeZone = s2;
                                        if (s2 == null) {
                                            timeZone = TimeZone.getTimeZone("GMT");
                                        }
                                        while (true) {
                                            Label_0167: {
                                                if (n == 0) {
                                                    break Label_0167;
                                                }
                                                try {
                                                    UTCDateConverterGson.defaultFormatNoMs.setTimeZone((TimeZone)timeZone);
                                                    s2 = UTCDateConverterGson.defaultFormatNoMs;
                                                    return ((DateFormat)s2).parse((String)s);
                                                }
                                                catch (ParseException ex) {
                                                    return null;
                                                }
                                            }
                                            UTCDateConverterGson.defaultFormatMs.setTimeZone((TimeZone)timeZone);
                                            s2 = UTCDateConverterGson.defaultFormatMs;
                                            continue;
                                        }
                                    }
                                    break Label_0193;
                                }
                                else {
                                    s = s2;
                                    if (((String)s2).contains(".")) {
                                        s = ((String)s2).replaceAll("([.][0-9]{3})[0-9]*$", "$1");
                                    }
                                    break Label_0188;
                                }
                            }
                            return null;
                        }
                    }
                    Serializable s2 = null;
                    continue Label_0122_Outer;
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    public static class UTCDateConverterJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            return UTCDateConverterGson.convert(jsonElement.getAsJsonPrimitive().getAsString());
        }
    }
    
    public static class UTCDateConverterShortDateAlternateFormatJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(JsonElement parse, Type asString, JsonDeserializationContext timeZone) {
            asString = (Type)((JsonElement)parse).getAsJsonPrimitive().getAsString();
            timeZone = (JsonDeserializationContext)TimeZone.getTimeZone("GMT");
            UTCDateConverterGson.shortDateFormat.setTimeZone((TimeZone)timeZone);
            try {
                parse = UTCDateConverterGson.shortDateFormat.parse((String)asString);
            }
            catch (ParseException ex) {
                parse = null;
            }
            if (parse != null && ((Date)parse).getYear() + 1900 < 2000) {
                UTCDateConverterGson.shortDateAlternateFormat.setTimeZone((TimeZone)timeZone);
                try {
                    return UTCDateConverterGson.shortDateAlternateFormat.parse((String)asString);
                }
                catch (ParseException ex2) {
                    return (Date)parse;
                }
            }
            return (Date)parse;
        }
    }
    
    public static class UTCDateConverterShortDateFormatJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            final String asString = jsonElement.getAsJsonPrimitive().getAsString();
            UTCDateConverterGson.shortDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return UTCDateConverterGson.shortDateFormat.parse(asString);
            }
            catch (ParseException ex) {
                return null;
            }
        }
    }
    
    public static class UTCRoundtripDateConverterJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            String s2;
            final String s = s2 = jsonElement.getAsJsonPrimitive().getAsString();
            if (s.endsWith("Z")) {
                s2 = s.replace("Z", "");
            }
            UTCDateConverterGson.defaultFormatNoMs.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return UTCDateConverterGson.defaultFormatNoMs.parse(s2);
            }
            catch (ParseException ex) {
                return null;
            }
        }
    }
}
