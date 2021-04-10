package com.google.gson.internal.bind;

import java.sql.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.text.*;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;

public final class TimeTypeAdapter extends TypeAdapter<Time>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Time.class) {
                    return (TypeAdapter<T>)new TimeTypeAdapter();
                }
                return null;
            }
        };
    }
    
    public TimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Time time) throws IOException {
        // monitorenter(this)
        Label_0020: {
            if (time == null) {
                final String format = null;
                break Label_0020;
            }
            try {
                final String format = this.format.format(time);
                jsonWriter.value(format);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
