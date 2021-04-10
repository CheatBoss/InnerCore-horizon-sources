package com.amazon.device.iap.internal.c;

import java.util.*;
import java.util.concurrent.*;
import com.amazon.device.iap.internal.util.*;

public class b
{
    private static final b b;
    private final Set<String> a;
    
    static {
        b = new b();
    }
    
    public b() {
        this.a = new ConcurrentSkipListSet<String>();
    }
    
    public static b a() {
        return com.amazon.device.iap.internal.c.b.b;
    }
    
    public boolean a(final String s) {
        return !d.a(s) && this.a.remove(s);
    }
    
    public void b(final String s) {
        if (!d.a(s)) {
            this.a.add(s);
        }
    }
}
