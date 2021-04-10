package com.google.gson;

import java.util.function.*;
import com.google.gson.stream.*;
import java.util.*;
import com.google.gson.internal.*;
import java.io.*;

public final class JsonStreamParser implements Iterator<JsonElement>
{
    private final Object lock;
    private final JsonReader parser;
    
    public JsonStreamParser(final Reader reader) {
        (this.parser = new JsonReader(reader)).setLenient(true);
        this.lock = new Object();
    }
    
    public JsonStreamParser(final String s) {
        this(new StringReader(s));
    }
    
    @Override
    public void forEachRemaining(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public boolean hasNext() {
        final Object lock = this.lock;
        // monitorenter(lock)
        while (true) {
            try {
                try {
                    // monitorexit(lock)
                    return this.parser.peek() != JsonToken.END_DOCUMENT;
                }
                catch (IOException ex) {
                    throw new JsonIOException(ex);
                }
                catch (MalformedJsonException ex2) {
                    throw new JsonSyntaxException(ex2);
                }
                // monitorexit(lock)
                throw;
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    @Override
    public JsonElement next() throws JsonParseException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return Streams.parse(this.parser);
        }
        catch (JsonParseException ex) {
            if (ex.getCause() instanceof EOFException) {
                ex = new NoSuchElementException();
            }
            throw ex;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            throw new RuntimeException("Failed parsing JSON source to Json", outOfMemoryError);
        }
        catch (StackOverflowError stackOverflowError) {
            throw new RuntimeException("Failed parsing JSON source to Json", stackOverflowError);
        }
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
