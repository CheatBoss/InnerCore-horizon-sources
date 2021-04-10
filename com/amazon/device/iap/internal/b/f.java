package com.amazon.device.iap.internal.b;

import com.amazon.device.iap.internal.*;
import com.amazon.android.framework.util.*;

public class f implements a
{
    private static KiwiLogger a;
    
    static {
        f.a = new KiwiLogger("In App Purchasing SDK - Production Mode");
    }
    
    private static String c(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append(s2);
        return sb.toString();
    }
    
    @Override
    public void a(final String s, final String s2) {
        f.a.trace(c(s, s2));
    }
    
    @Override
    public boolean a() {
        return KiwiLogger.TRACE_ON;
    }
    
    @Override
    public void b(final String s, final String s2) {
        f.a.error(c(s, s2));
    }
    
    @Override
    public boolean b() {
        return KiwiLogger.ERROR_ON;
    }
}
