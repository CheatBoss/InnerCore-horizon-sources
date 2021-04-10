package com.google.android.gms.internal.measurement;

import android.util.*;

final class zzsp extends zzsl<Long>
{
    zzsp(final zzsv zzsv, final String s, final Long n) {
        super(zzsv, s, n, null);
    }
    
    private final Long zzfk(final String s) {
        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException ex) {
            final String zzbrc = this.zzbrc;
            final StringBuilder sb = new StringBuilder(String.valueOf(zzbrc).length() + 25 + String.valueOf(s).length());
            sb.append("Invalid long value for ");
            sb.append(zzbrc);
            sb.append(": ");
            sb.append(s);
            Log.e("PhenotypeFlag", sb.toString());
            return null;
        }
    }
}
