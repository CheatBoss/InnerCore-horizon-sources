package com.google.android.gms.internal.measurement;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;

final class zzww
{
    static String zza(final zzwt zzwt, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(s);
        zza(zzwt, sb, 0);
        return sb.toString();
    }
    
    private static void zza(final zzwt zzwt, final StringBuilder sb, final int n) {
        final HashMap<Object, Method> hashMap = new HashMap<Object, Method>();
        final HashMap<String, Method> hashMap2 = new HashMap<String, Method>();
        final TreeSet<String> set = new TreeSet<String>();
        final Method[] declaredMethods = zzwt.getClass().getDeclaredMethods();
        for (int length = declaredMethods.length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            hashMap2.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                hashMap.put(method.getName(), method);
                if (method.getName().startsWith("get")) {
                    set.add(method.getName());
                }
            }
        }
        for (final String s : set) {
            final String replaceFirst = s.replaceFirst("get", "");
            if (replaceFirst.endsWith("List") && !replaceFirst.endsWith("OrBuilderList") && !replaceFirst.equals("List")) {
                final String value = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                final String value2 = String.valueOf(replaceFirst.substring(1, replaceFirst.length() - 4));
                String concat;
                if (value2.length() != 0) {
                    concat = value.concat(value2);
                }
                else {
                    concat = new String(value);
                }
                final Method method2 = hashMap.get(s);
                if (method2 != null && method2.getReturnType().equals(List.class)) {
                    zzb(sb, n, zzga(concat), zzvm.zza(method2, zzwt, new Object[0]));
                    continue;
                }
            }
            if (replaceFirst.endsWith("Map") && !replaceFirst.equals("Map")) {
                final String value3 = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                final String value4 = String.valueOf(replaceFirst.substring(1, replaceFirst.length() - 3));
                String concat2;
                if (value4.length() != 0) {
                    concat2 = value3.concat(value4);
                }
                else {
                    concat2 = new String(value3);
                }
                final Method method3 = hashMap.get(s);
                if (method3 != null && method3.getReturnType().equals(Map.class) && !method3.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(method3.getModifiers())) {
                    zzb(sb, n, zzga(concat2), zzvm.zza(method3, zzwt, new Object[0]));
                    continue;
                }
            }
            final String value5 = String.valueOf(replaceFirst);
            String concat3;
            if (value5.length() != 0) {
                concat3 = "set".concat(value5);
            }
            else {
                concat3 = new String("set");
            }
            if (hashMap2.get(concat3) != null) {
                if (replaceFirst.endsWith("Bytes")) {
                    final String value6 = String.valueOf(replaceFirst.substring(0, replaceFirst.length() - 5));
                    String concat4;
                    if (value6.length() != 0) {
                        concat4 = "get".concat(value6);
                    }
                    else {
                        concat4 = new String("get");
                    }
                    if (hashMap.containsKey(concat4)) {
                        continue;
                    }
                }
                final String value7 = String.valueOf(replaceFirst.substring(0, 1).toLowerCase());
                final String value8 = String.valueOf(replaceFirst.substring(1));
                String concat5;
                if (value8.length() != 0) {
                    concat5 = value7.concat(value8);
                }
                else {
                    concat5 = new String(value7);
                }
                final String value9 = String.valueOf(replaceFirst);
                String concat6;
                if (value9.length() != 0) {
                    concat6 = "get".concat(value9);
                }
                else {
                    concat6 = new String("get");
                }
                final Method method4 = hashMap.get(concat6);
                final String value10 = String.valueOf(replaceFirst);
                String concat7;
                if (value10.length() != 0) {
                    concat7 = "has".concat(value10);
                }
                else {
                    concat7 = new String("has");
                }
                final Method method5 = hashMap.get(concat7);
                if (method4 == null) {
                    continue;
                }
                final Object zza = zzvm.zza(method4, zzwt, new Object[0]);
                boolean booleanValue;
                if (method5 == null) {
                    boolean equals = false;
                    Label_0971: {
                        Label_0968: {
                            Label_0962: {
                                if (zza instanceof Boolean) {
                                    if (zza) {
                                        break Label_0968;
                                    }
                                }
                                else if (zza instanceof Integer) {
                                    if ((int)zza != 0) {
                                        break Label_0968;
                                    }
                                }
                                else if (zza instanceof Float) {
                                    if ((float)zza != 0.0f) {
                                        break Label_0968;
                                    }
                                }
                                else {
                                    if (!(zza instanceof Double)) {
                                        Serializable zzbtz;
                                        if (zza instanceof String) {
                                            zzbtz = "";
                                        }
                                        else if (zza instanceof zzud) {
                                            zzbtz = zzud.zzbtz;
                                        }
                                        else {
                                            if ((zza instanceof zzwt) ? (zza == ((zzwt)zza).zzwf()) : (zza instanceof Enum && ((Enum)zza).ordinal() == 0)) {
                                                break Label_0962;
                                            }
                                            break Label_0968;
                                        }
                                        equals = zza.equals(zzbtz);
                                        break Label_0971;
                                    }
                                    if ((double)zza != 0.0) {
                                        break Label_0968;
                                    }
                                }
                            }
                            equals = true;
                            break Label_0971;
                        }
                        equals = false;
                    }
                    booleanValue = !equals;
                }
                else {
                    booleanValue = (boolean)zzvm.zza(method5, zzwt, new Object[0]);
                }
                if (!booleanValue) {
                    continue;
                }
                zzb(sb, n, zzga(concat5), zza);
            }
        }
        if (zzwt instanceof zzvm.zzc) {
            final Iterator<Map.Entry<Object, Object>> iterator2 = ((zzvm.zzc)zzwt).zzbys.iterator();
            if (iterator2.hasNext()) {
                ((Map.Entry)iterator2.next()).getKey();
                throw new NoSuchMethodError();
            }
        }
        final zzvm zzvm = (zzvm)zzwt;
        if (zzvm.zzbym != null) {
            zzvm.zzbym.zzb(sb, n);
        }
    }
    
    static final void zzb(final StringBuilder sb, final int n, final String s, final Object o) {
        if (o instanceof List) {
            final Iterator<Object> iterator = (Iterator<Object>)((List)o).iterator();
            while (iterator.hasNext()) {
                zzb(sb, n, s, iterator.next());
            }
            return;
        }
        if (o instanceof Map) {
            final Iterator<Map.Entry<?, ?>> iterator2 = ((Map)o).entrySet().iterator();
            while (iterator2.hasNext()) {
                zzb(sb, n, s, iterator2.next());
            }
            return;
        }
        sb.append('\n');
        final int n2 = 0;
        final int n3 = 0;
        for (int i = 0; i < n; ++i) {
            sb.append(' ');
        }
        sb.append(s);
        if (o instanceof String) {
            sb.append(": \"");
            sb.append(zzxx.zzd(zzud.zzfv((String)o)));
            sb.append('\"');
            return;
        }
        if (o instanceof zzud) {
            sb.append(": \"");
            sb.append(zzxx.zzd((zzud)o));
            sb.append('\"');
            return;
        }
        if (o instanceof zzvm) {
            sb.append(" {");
            zza((zzwt)o, sb, n + 2);
            sb.append("\n");
            for (int j = n3; j < n; ++j) {
                sb.append(' ');
            }
            sb.append("}");
            return;
        }
        if (o instanceof Map.Entry) {
            sb.append(" {");
            final Map.Entry entry = (Map.Entry)o;
            final int n4 = n + 2;
            zzb(sb, n4, "key", entry.getKey());
            zzb(sb, n4, "value", entry.getValue());
            sb.append("\n");
            for (int k = n2; k < n; ++k) {
                sb.append(' ');
            }
            sb.append("}");
            return;
        }
        sb.append(": ");
        sb.append(o.toString());
    }
    
    private static final String zzga(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(char1));
        }
        return sb.toString();
    }
}
