package com.google.android.gms.internal.measurement;

import android.util.*;

final class zzsq extends zzsl<Integer>
{
    zzsq(final zzsv zzsv, final String s, final Integer n) {
        super(zzsv, s, n, null);
    }
    
    private final Integer zzfl(final String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            final String zzbrc = this.zzbrc;
            final StringBuilder sb = new StringBuilder(String.valueOf(zzbrc).length() + 28 + String.valueOf(s).length());
            sb.append("Invalid integer value for ");
            sb.append(zzbrc);
            sb.append(": ");
            sb.append(s);
            Log.e("PhenotypeFlag", sb.toString());
            return null;
        }
    }
}
