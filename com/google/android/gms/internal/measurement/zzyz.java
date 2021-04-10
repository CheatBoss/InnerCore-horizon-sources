package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzyz extends IOException
{
    zzyz(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder(108);
        sb.append("CodedOutputStream was writing to a flat byte array and ran out of space (pos ");
        sb.append(n);
        sb.append(" limit ");
        sb.append(n2);
        sb.append(").");
        super(sb.toString());
    }
}
