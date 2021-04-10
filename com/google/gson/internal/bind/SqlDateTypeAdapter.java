package com.google.gson.internal.bind;

import java.sql.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.text.*;
import com.google.gson.stream.*;
import java.io.*;

public final class SqlDateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Date.class) {
                    return (TypeAdapter<T>)new SqlDateTypeAdapter();
                }
                return null;
            }
        };
    }
    
    public SqlDateTypeAdapter() {
        this.format = new SimpleDateFormat("MMM d, yyyy");
    }
    
    @Override
    public void write(final JsonWriter jsonWriter, final Date date) throws IOException {
        // monitorenter(this)
        Label_0020: {
            if (date == null) {
                final String format = null;
                break Label_0020;
            }
            try {
                final String format = this.format.format(date);
                jsonWriter.value(format);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
}
