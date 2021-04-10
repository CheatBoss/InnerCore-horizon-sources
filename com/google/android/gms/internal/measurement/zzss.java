package com.google.android.gms.internal.measurement;

import android.util.*;

final class zzss extends zzsl<Double>
{
    zzss(final zzsv zzsv, final String s, final Double n) {
        super(zzsv, s, n, null);
    }
    
    private final Double zzfm(final String s) {
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            final String zzbrc = this.zzbrc;
            final StringBuilder sb = new StringBuilder(String.valueOf(zzbrc).length() + 27 + String.valueOf(s).length());
            sb.append("Invalid double value for ");
            sb.append(zzbrc);
            sb.append(": ");
            sb.append(s);
            Log.e("PhenotypeFlag", sb.toString());
            return null;
        }
    }
}
