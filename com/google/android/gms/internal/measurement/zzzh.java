package com.google.android.gms.internal.measurement;

import java.lang.reflect.*;

public final class zzzh
{
    private static void zza(String s, Object o, final StringBuffer sb, final StringBuffer sb2) throws IllegalAccessException, InvocationTargetException {
        if (o != null) {
            if (o instanceof zzzg) {
                final int length = sb.length();
                if (s != null) {
                    sb2.append(sb);
                    sb2.append(zzgc(s));
                    sb2.append(" <\n");
                    sb.append("  ");
                }
                final Class<?> class1 = o.getClass();
                final Field[] fields = class1.getFields();
                for (int length2 = fields.length, i = 0; i < length2; ++i) {
                    final Field field = fields[i];
                    final int modifiers = field.getModifiers();
                    final String name = field.getName();
                    if (!"cachedSize".equals(name) && (modifiers & 0x1) == 0x1 && (modifiers & 0x8) != 0x8 && !name.startsWith("_") && !name.endsWith("_")) {
                        final Class<?> type = field.getType();
                        final Object value = field.get(o);
                        if (type.isArray() && type.getComponentType() != Byte.TYPE) {
                            int length3;
                            if (value == null) {
                                length3 = 0;
                            }
                            else {
                                length3 = Array.getLength(value);
                            }
                            for (int j = 0; j < length3; ++j) {
                                zza(name, Array.get(value, j), sb, sb2);
                            }
                        }
                        else {
                            zza(name, value, sb, sb2);
                        }
                    }
                }
                final Method[] methods = class1.getMethods();
                for (int length4 = methods.length, k = 0; k < length4; ++k) {
                    final String name2 = methods[k].getName();
                    if (name2.startsWith("set")) {
                        final String substring = name2.substring(3);
                        try {
                            final String value2 = String.valueOf(substring);
                            String concat;
                            if (value2.length() != 0) {
                                concat = "has".concat(value2);
                            }
                            else {
                                concat = new String("has");
                            }
                            if (class1.getMethod(concat, (Class<?>[])new Class[0]).invoke(o, new Object[0])) {
                                final String value3 = String.valueOf(substring);
                                String concat2;
                                if (value3.length() != 0) {
                                    concat2 = "get".concat(value3);
                                }
                                else {
                                    concat2 = new String("get");
                                }
                                zza(substring, class1.getMethod(concat2, (Class<?>[])new Class[0]).invoke(o, new Object[0]), sb, sb2);
                            }
                        }
                        catch (NoSuchMethodException ex) {}
                    }
                }
                if (s != null) {
                    sb.setLength(length);
                    sb2.append(sb);
                    sb2.append(">\n");
                }
                return;
            }
            s = zzgc(s);
            sb2.append(sb);
            sb2.append(s);
            sb2.append(": ");
            Label_0833: {
                if (o instanceof String) {
                    final String s2 = s = (String)o;
                    if (!s2.startsWith("http")) {
                        s = s2;
                        if (s2.length() > 200) {
                            s = String.valueOf(s2.substring(0, 200)).concat("[...]");
                        }
                    }
                    final int length5 = s.length();
                    o = new StringBuilder(length5);
                    for (int l = 0; l < length5; ++l) {
                        final char char1 = s.charAt(l);
                        if (char1 >= ' ' && char1 <= '~' && char1 != '\"' && char1 != '\'') {
                            ((StringBuilder)o).append(char1);
                        }
                        else {
                            ((StringBuilder)o).append(String.format("\\u%04x", (int)char1));
                        }
                    }
                    s = ((StringBuilder)o).toString();
                    sb2.append("\"");
                    sb2.append(s);
                    s = "\"";
                }
                else {
                    if (!(o instanceof byte[])) {
                        sb2.append(o);
                        break Label_0833;
                    }
                    final byte[] array = (byte[])o;
                    if (array != null) {
                        sb2.append('\"');
                        for (int n = 0; n < array.length; ++n) {
                            final int n2 = array[n] & 0xFF;
                            if (n2 != 92 && n2 != 34) {
                                if (n2 < 32 || n2 >= 127) {
                                    sb2.append(String.format("\\%03o", n2));
                                    continue;
                                }
                            }
                            else {
                                sb2.append('\\');
                            }
                            sb2.append((char)n2);
                        }
                        sb2.append('\"');
                        break Label_0833;
                    }
                    s = "\"\"";
                }
                sb2.append(s);
            }
            sb2.append("\n");
        }
    }
    
    public static <T extends zzzg> String zzc(final T t) {
        if (t == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        try {
            zza(null, t, new StringBuffer(), sb);
            return sb.toString();
        }
        catch (InvocationTargetException ex) {
            final String value = String.valueOf(ex.getMessage());
            if (value.length() != 0) {
                return "Error printing proto: ".concat(value);
            }
            return new String("Error printing proto: ");
        }
        catch (IllegalAccessException ex2) {
            final String value2 = String.valueOf(ex2.getMessage());
            if (value2.length() != 0) {
                return "Error printing proto: ".concat(value2);
            }
            return new String("Error printing proto: ");
        }
    }
    
    private static String zzgc(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            char lowerCase = '\0';
            Label_0034: {
                if (i != 0) {
                    lowerCase = char1;
                    if (!Character.isUpperCase(char1)) {
                        break Label_0034;
                    }
                    sb.append('_');
                }
                lowerCase = Character.toLowerCase(char1);
            }
            sb.append(lowerCase);
        }
        return sb.toString();
    }
}
