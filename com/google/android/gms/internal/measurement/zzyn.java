package com.google.android.gms.internal.measurement;

final class zzyn extends IllegalArgumentException
{
    zzyn(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder(54);
        sb.append("Unpaired surrogate at index ");
        sb.append(n);
        sb.append(" of ");
        sb.append(n2);
        super(sb.toString());
    }
}
