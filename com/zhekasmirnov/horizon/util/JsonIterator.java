package com.zhekasmirnov.horizon.util;

import org.json.*;
import java.util.*;
import android.support.annotation.*;

public class JsonIterator<T> implements Iterable<T>
{
    private final JSONArray array;
    
    public JsonIterator(final JSONArray array) {
        if (array != null) {
            this.array = array;
        }
        else {
            this.array = new JSONArray();
        }
    }
    
    public JsonIterator(final JSONObject object) {
        this((object != null) ? object.names() : null);
    }
    
    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < JsonIterator.this.array.length();
            }
            
            @Override
            public T next() {
                try {
                    return (T)JsonIterator.this.array.opt(this.index++);
                }
                catch (ClassCastException e) {
                    return null;
                }
            }
        };
    }
    
    public static JsonIterator<Object> iterate(final JSONArray array) {
        return new JsonIterator<Object>(array);
    }
    
    public static JsonIterator<String> iterate(final JSONObject obj) {
        return new JsonIterator<String>(obj);
    }
}
