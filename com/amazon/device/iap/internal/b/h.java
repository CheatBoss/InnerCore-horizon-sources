package com.amazon.device.iap.internal.b;

import java.util.*;

public class h
{
    static final /* synthetic */ boolean b;
    public final Map<String, Object> a;
    
    static {
        b = true;
    }
    
    public h() {
        this.a = new HashMap<String, Object>();
    }
    
    public Object a() {
        return this.a.get("RESPONSE");
    }
    
    public Object a(final String s) {
        return this.a.get(s);
    }
    
    public void a(final Object o) {
        if (!h.b && o == null) {
            throw new AssertionError();
        }
        this.a.put("RESPONSE", o);
    }
    
    public void a(final String s, final Object o) {
        this.a.put(s, o);
    }
    
    public void b() {
        this.a.remove("RESPONSE");
    }
}
