package org.spongycastle.util;

import java.math.*;
import java.security.*;
import java.util.*;

public class Properties
{
    private static final ThreadLocal threadProperties;
    
    static {
        threadProperties = new ThreadLocal();
    }
    
    private Properties() {
    }
    
    public static BigInteger asBigInteger(String fetchProperty) {
        fetchProperty = fetchProperty(fetchProperty);
        if (fetchProperty != null) {
            return new BigInteger(fetchProperty);
        }
        return null;
    }
    
    public static Set<String> asKeySet(String fetchProperty) {
        final HashSet<String> set = new HashSet<String>();
        fetchProperty = fetchProperty(fetchProperty);
        if (fetchProperty != null) {
            final StringTokenizer stringTokenizer = new StringTokenizer(fetchProperty, ",");
            while (stringTokenizer.hasMoreElements()) {
                set.add(Strings.toLowerCase(stringTokenizer.nextToken()).trim());
            }
        }
        return (Set<String>)Collections.unmodifiableSet((Set<?>)set);
    }
    
    private static String fetchProperty(final String s) {
        return AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            @Override
            public Object run() {
                final Map<K, Object> map = Properties.threadProperties.get();
                if (map != null) {
                    return map.get(s);
                }
                return System.getProperty(s);
            }
        });
    }
    
    public static boolean isOverrideSet(String fetchProperty) {
        try {
            fetchProperty = fetchProperty(fetchProperty);
            return fetchProperty != null && "true".equals(Strings.toLowerCase(fetchProperty));
        }
        catch (AccessControlException ex) {
            return false;
        }
    }
    
    public static boolean removeThreadOverride(final String s) {
        final boolean overrideSet = isOverrideSet(s);
        final Map map = Properties.threadProperties.get();
        if (map == null) {
            return false;
        }
        map.remove(s);
        if (map.isEmpty()) {
            Properties.threadProperties.remove();
            return overrideSet;
        }
        Properties.threadProperties.set(map);
        return overrideSet;
    }
    
    public static boolean setThreadOverride(final String s, final boolean b) {
        final boolean overrideSet = isOverrideSet(s);
        Map<String, String> map;
        if ((map = Properties.threadProperties.get()) == null) {
            map = new HashMap<String, String>();
        }
        String s2;
        if (b) {
            s2 = "true";
        }
        else {
            s2 = "false";
        }
        map.put(s, s2);
        Properties.threadProperties.set(map);
        return overrideSet;
    }
}
