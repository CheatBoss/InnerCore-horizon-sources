package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzzj
{
    public static final int[] zzcax;
    private static final int zzcfn = 11;
    private static final int zzcfo = 12;
    private static final int zzcfp = 16;
    private static final int zzcfq = 26;
    public static final long[] zzcfr;
    private static final float[] zzcfs;
    private static final double[] zzcft;
    private static final boolean[] zzcfu;
    public static final String[] zzcfv;
    private static final byte[][] zzcfw;
    public static final byte[] zzcfx;
    
    static {
        zzcax = new int[0];
        zzcfr = new long[0];
        zzcfs = new float[0];
        zzcft = new double[0];
        zzcfu = new boolean[0];
        zzcfv = new String[0];
        zzcfw = new byte[0][];
        zzcfx = new byte[0];
    }
    
    public static final int zzb(final zzyx zzyx, final int n) throws IOException {
        final int position = zzyx.getPosition();
        zzyx.zzao(n);
        int n2 = 1;
        while (zzyx.zzug() == n) {
            zzyx.zzao(n);
            ++n2;
        }
        zzyx.zzt(position, n);
        return n2;
    }
}
