package com.amazon.device.iap.internal.a;

import java.util.*;
import com.amazon.device.iap.internal.*;

public final class d implements b
{
    private static final Map<Class, Class> a;
    
    static {
        (a = new HashMap<Class, Class>()).put(c.class, com.amazon.device.iap.internal.a.c.class);
        d.a.put(a.class, com.amazon.device.iap.internal.a.a.class);
    }
    
    @Override
    public <T> Class<T> a(final Class<T> clazz) {
        return d.a.get(clazz);
    }
}
