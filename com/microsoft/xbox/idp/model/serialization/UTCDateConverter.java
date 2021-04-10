package com.microsoft.xbox.idp.model.serialization;

import android.text.*;
import java.util.*;
import android.util.*;
import java.io.*;
import java.lang.reflect.*;
import com.google.gson.*;
import java.text.*;

public class UTCDateConverter
{
    private static final int NO_MS_STRING_LENGTH = 19;
    private static final String TAG;
    private static SimpleDateFormat defaultFormatMs;
    private static SimpleDateFormat defaultFormatNoMs;
    private static SimpleDateFormat shortDateAlternateFormat;
    private static SimpleDateFormat shortDateFormat;
    
    static {
        TAG = UTCDateConverter.class.getSimpleName();
        UTCDateConverter.defaultFormatNoMs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        UTCDateConverter.defaultFormatMs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        UTCDateConverter.shortDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
        UTCDateConverter.shortDateAlternateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    }
    
    public static Date convert(String s) {
    Label_0122_Outer:
        while (true) {
            while (true) {
            Label_0207:
                while (true) {
                    Label_0202: {
                        synchronized (UTCDateConverter.class) {
                            if (!TextUtils.isEmpty((CharSequence)s)) {
                                Serializable s2 = s;
                                if (((String)s).endsWith("Z")) {
                                    s2 = ((String)s).replace("Z", "");
                                }
                                if (((String)s2).endsWith("+00:00")) {
                                    s = ((String)s2).replace("+00:00", "");
                                    break Label_0202;
                                }
                                if (((String)s2).endsWith("+01:00")) {
                                    s = ((String)s2).replace("+01:00", "");
                                    s2 = TimeZone.getTimeZone("GMT+01:00");
                                    if (((String)s).length() != 19) {
                                        break Label_0207;
                                    }
                                    final int n = 1;
                                    Serializable timeZone = s2;
                                    if (s2 == null) {
                                        timeZone = TimeZone.getTimeZone("GMT");
                                    }
                                    Label_0167: {
                                        if (n == 0) {
                                            break Label_0167;
                                        }
                                        try {
                                            UTCDateConverter.defaultFormatNoMs.setTimeZone((TimeZone)timeZone);
                                            s2 = UTCDateConverter.defaultFormatNoMs;
                                            return ((DateFormat)s2).parse((String)s);
                                        }
                                        catch (ParseException ex) {
                                            Log.e(UTCDateConverter.TAG, ex.toString());
                                            return null;
                                            UTCDateConverter.defaultFormatMs.setTimeZone((TimeZone)timeZone);
                                            s2 = UTCDateConverter.defaultFormatMs;
                                        }
                                    }
                                }
                                else {
                                    s = s2;
                                    if (((String)s2).contains(".")) {
                                        s = ((String)s2).replaceAll("([.][0-9]{3})[0-9]*$", "$1");
                                    }
                                    break Label_0202;
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
    
    public static class UTCDateConverterJSONDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date>
    {
        public Date deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            return UTCDateConverter.convert(jsonElement.getAsJsonPrimitive().getAsString());
        }
        
        @Override
        public JsonElement serialize(final Date date, final Type type, final JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(UTCDateConverter.defaultFormatNoMs.format(date));
        }
    }
    
    public static class UTCDateConverterShortDateAlternateFormatJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(JsonElement parse, Type asString, JsonDeserializationContext timeZone) {
            asString = (Type)((JsonElement)parse).getAsJsonPrimitive().getAsString();
            timeZone = (JsonDeserializationContext)TimeZone.getTimeZone("GMT");
            UTCDateConverter.shortDateFormat.setTimeZone((TimeZone)timeZone);
            try {
                parse = UTCDateConverter.shortDateFormat.parse((String)asString);
            }
            catch (ParseException ex) {
                final String access$200 = UTCDateConverter.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to parse short date ");
                sb.append((String)asString);
                Log.d(access$200, sb.toString());
                parse = null;
            }
            if (parse != null && ((Date)parse).getYear() + 1900 < 2000) {
                UTCDateConverter.shortDateAlternateFormat.setTimeZone((TimeZone)timeZone);
                try {
                    return UTCDateConverter.shortDateAlternateFormat.parse((String)asString);
                }
                catch (ParseException ex2) {
                    final String access$201 = UTCDateConverter.TAG;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("failed to parse alternate short date ");
                    sb2.append((String)asString);
                    Log.d(access$201, sb2.toString());
                }
            }
            return (Date)parse;
        }
    }
    
    public static class UTCDateConverterShortDateFormatJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(JsonElement asString, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            asString = (JsonElement)asString.getAsJsonPrimitive().getAsString();
            UTCDateConverter.shortDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return UTCDateConverter.shortDateFormat.parse((String)asString);
            }
            catch (ParseException ex) {
                final String access$200 = UTCDateConverter.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to parse date ");
                sb.append((String)asString);
                Log.d(access$200, sb.toString());
                return null;
            }
        }
    }
    
    public static class UTCRoundtripDateConverterJSONDeserializer implements JsonDeserializer<Date>
    {
        public Date deserialize(JsonElement o, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
            final Object o2 = o = ((JsonElement)o).getAsJsonPrimitive().getAsString();
            if (((String)o2).endsWith("Z")) {
                o = ((String)o2).replace("Z", "");
            }
            UTCDateConverter.defaultFormatNoMs.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return UTCDateConverter.defaultFormatNoMs.parse((String)o);
            }
            catch (ParseException ex) {
                final String access$200 = UTCDateConverter.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to parse date ");
                sb.append((String)o);
                Log.d(access$200, sb.toString());
                return null;
            }
        }
    }
}
