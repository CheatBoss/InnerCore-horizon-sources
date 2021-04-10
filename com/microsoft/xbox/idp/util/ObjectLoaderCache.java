package com.microsoft.xbox.idp.util;

import com.microsoft.xbox.idp.toolkit.*;
import java.util.*;

public class ObjectLoaderCache implements Cache
{
    private final HashMap<Object, Result<?>> map;
    
    public ObjectLoaderCache() {
        this.map = new HashMap<Object, Result<?>>();
    }
    
    @Override
    public void clear() {
        this.map.clear();
    }
    
    @Override
    public <T> Result<T> get(final Object o) {
        return (Result<T>)this.map.get(o);
    }
    
    @Override
    public <T> Result<T> put(final Object o, final Result<T> result) {
        return (Result<T>)this.map.put(o, result);
    }
    
    @Override
    public <T> Result<T> remove(final Object o) {
        return (Result<T>)this.map.remove(o);
    }
}
