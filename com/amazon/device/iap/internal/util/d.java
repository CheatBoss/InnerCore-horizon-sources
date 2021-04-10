package com.amazon.device.iap.internal.util;

import java.util.*;

public class d
{
    public static void a(final Object o, final String s) {
        if (o != null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be null");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static void a(final String s, final String s2) {
        if (!a(s)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s2);
        sb.append(" must not be null or empty");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static void a(final Collection<?> collection, final String s) {
        if (!collection.isEmpty()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be empty");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static boolean a(final String s) {
        return s == null || s.trim().length() == 0;
    }
}
