package com.microsoft.aad.adal;

import java.util.*;
import java.lang.reflect.*;
import java.text.*;
import com.google.gson.*;

public final class DateTimeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date>
{
    private static final String TAG = "DateTimeAdapter";
    private final DateFormat mEnUs24HourFormat;
    private final DateFormat mEnUsFormat;
    private final DateFormat mISO8601Format;
    private final DateFormat mLocal24HourFormat;
    private final DateFormat mLocalFormat;
    
    public DateTimeAdapter() {
        this.mEnUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.mLocalFormat = DateFormat.getDateTimeInstance(2, 2);
        this.mISO8601Format = buildIso8601Format();
        this.mEnUs24HourFormat = buildEnUs24HourDateFormat();
        this.mLocal24HourFormat = buildLocal24HourDateFormat();
    }
    
    private static DateFormat buildEnUs24HourDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
    }
    
    private static DateFormat buildIso8601Format() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }
    
    private static DateFormat buildLocal24HourDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault());
    }
    
    public Date deserialize(JsonElement asString, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        synchronized (this) {
            asString = (JsonElement)asString.getAsString();
            try {
                return this.mISO8601Format.parse((String)asString);
            }
            catch (ParseException ex2) {
                Logger.v("DateTimeAdapter:deserialize", "Cannot parse with ISO8601, try again with local format.");
                try {
                    return this.mLocalFormat.parse((String)asString);
                }
                catch (ParseException ex3) {
                    Logger.v("DateTimeAdapter:deserialize", "Cannot parse with local format, try again with local 24 hour format.");
                    try {
                        return this.mLocal24HourFormat.parse((String)asString);
                    }
                    catch (ParseException ex4) {
                        Logger.v("DateTimeAdapter:deserialize", "Cannot parse with local 24 hour format, try again with en us format.");
                        try {
                            return this.mEnUsFormat.parse((String)asString);
                        }
                        catch (ParseException ex5) {
                            Logger.v("DateTimeAdapter:deserialize", "Cannot parse with en us format, try again with en us 24 hour format.");
                            try {
                                return this.mEnUs24HourFormat.parse((String)asString);
                            }
                            catch (ParseException ex) {
                                Logger.e("DateTimeAdapter:deserialize", "Could not parse date. ", ex.getMessage(), ADALError.DATE_PARSING_FAILURE, ex);
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Could not parse date: ");
                                sb.append((String)asString);
                                throw new JsonParseException(sb.toString());
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public JsonElement serialize(final Date date, final Type type, final JsonSerializationContext jsonSerializationContext) {
        synchronized (this) {
            return new JsonPrimitive(this.mISO8601Format.format(date));
        }
    }
}
