package com.amazon.device.iap.internal.b;

import java.util.*;
import com.amazon.device.iap.internal.*;

public final class g implements b
{
    private static final Map<Class, Class> a;
    
    static {
        (a = new HashMap<Class, Class>()).put(c.class, com.amazon.device.iap.internal.b.c.class);
        g.a.put(a.class, f.class);
    }
    
    @Override
    public <T> Class<T> a(final Class<T> clazz) {
        return g.a.get(clazz);
    }
}
