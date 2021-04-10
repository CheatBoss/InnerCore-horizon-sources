package com.amazon.device.iap.internal.a;

import android.util.*;

public class a implements com.amazon.device.iap.internal.a
{
    private static String a(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("In App Purchasing SDK - Sandbox Mode: ");
        sb.append(s);
        return sb.toString();
    }
    
    @Override
    public void a(final String s, final String s2) {
        Log.d(s, a(s2));
    }
    
    @Override
    public boolean a() {
        return true;
    }
    
    @Override
    public void b(final String s, final String s2) {
        Log.e(s, a(s2));
    }
    
    @Override
    public boolean b() {
        return true;
    }
}
