package com.google.gson;

import com.google.gson.stream.*;
import java.io.*;
import com.google.gson.internal.bind.*;

public abstract class TypeAdapter<T>
{
    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>() {
            @Override
            public void write(final JsonWriter jsonWriter, final T t) throws IOException {
                if (t == null) {
                    jsonWriter.nullValue();
                    return;
                }
                TypeAdapter.this.write(jsonWriter, t);
            }
        };
    }
    
    public final JsonElement toJsonTree(final T t) {
        try {
            final JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
            this.write(jsonTreeWriter, t);
            return jsonTreeWriter.get();
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
    }
    
    public abstract void write(final JsonWriter p0, final T p1) throws IOException;
}
