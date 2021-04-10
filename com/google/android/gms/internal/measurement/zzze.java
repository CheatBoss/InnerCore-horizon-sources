package com.google.android.gms.internal.measurement;

import java.nio.charset.*;
import java.util.*;

public final class zzze
{
    private static final Charset ISO_8859_1;
    protected static final Charset UTF_8;
    public static final Object zzcfl;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        zzcfl = new Object();
    }
    
    public static boolean equals(final long[] array, final long[] array2) {
        if (array != null && array.length != 0) {
            return Arrays.equals(array, array2);
        }
        return array2 == null || array2.length == 0;
    }
    
    public static boolean equals(final Object[] array, final Object[] array2) {
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int length2;
        if (array2 == null) {
            length2 = 0;
        }
        else {
            length2 = array2.length;
        }
        int n = 0;
        int n2 = 0;
        while (true) {
            int n3 = n;
            if (n2 < length) {
                n3 = n;
                if (array[n2] == null) {
                    ++n2;
                    continue;
                }
            }
            while (n3 < length2 && array2[n3] == null) {
                ++n3;
            }
            final boolean b = n2 >= length;
            final boolean b2 = n3 >= length2;
            if (b && b2) {
                return true;
            }
            if (b != b2) {
                return false;
            }
            if (!array[n2].equals(array2[n3])) {
                return false;
            }
            ++n2;
            n = n3 + 1;
        }
    }
    
    public static int hashCode(final long[] array) {
        if (array != null && array.length != 0) {
            return Arrays.hashCode(array);
        }
        return 0;
    }
    
    public static int hashCode(final Object[] array) {
        int i = 0;
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int n = 0;
        while (i < length) {
            final Object o = array[i];
            int n2 = n;
            if (o != null) {
                n2 = n * 31 + o.hashCode();
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public static void zza(final zzza zzza, final zzza zzza2) {
        if (zzza.zzcfc != null) {
            zzza2.zzcfc = (zzzc)zzza.zzcfc.clone();
        }
    }
}
