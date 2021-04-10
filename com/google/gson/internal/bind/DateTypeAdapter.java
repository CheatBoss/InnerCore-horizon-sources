package com.google.gson.internal.bind;

import java.text.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.*;
import com.google.gson.stream.*;
import java.io.*;

public final class DateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Date.class) {
                    return (TypeAdapter<T>)new DateTypeAdapter();
                }
                return null;
            }
        };
    }
    
    public DateTypeAdapter() {
        this.enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
        this.localFormat = DateFormat.getDateTimeInstance(2, 2);
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Date date) throws IOException {
        // monitorenter(this)
        Label_0018: {
            if (date == null) {
                Label_0034: {
                    try {
                        jsonWriter.nullValue();
                        // monitorexit(this)
                        return;
                    }
                    finally {
                        break Label_0034;
                    }
                    break Label_0018;
                }
            }
            // monitorexit(this)
        }
        jsonWriter.value(this.enUsFormat.format(date));
    }
    // monitorexit(this)
}
