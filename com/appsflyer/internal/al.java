package com.appsflyer.internal;

import java.io.*;

public class al extends FilterInputStream
{
    private static final short \u0131;
    private int \u0196;
    private int \u01c3;
    private int \u0268;
    private byte[] \u0269;
    private int \u026a;
    private int \u0279;
    private byte[] \u0399;
    private byte[] \u03b9;
    private int \u0406;
    private int \u0456;
    private int \u04c0;
    private int \u04cf;
    
    static {
        \u0131 = (short)((Math.sqrt(5.0) - 1.0) * Math.pow(2.0, 15.0));
    }
    
    public al(final InputStream inputStream, final int[] array, int \u04cf, final byte[] array2, int \u04cf2, final int \u0456) throws IOException {
        super(inputStream);
        this.\u0456 = Integer.MAX_VALUE;
        this.\u0399 = new byte[8];
        this.\u03b9 = new byte[8];
        this.\u0269 = new byte[8];
        this.\u01c3 = 8;
        this.\u0279 = 8;
        this.\u0196 = Math.min(Math.max(\u04cf2, 5), 16);
        this.\u0406 = \u0456;
        if (\u0456 == 3) {
            System.arraycopy(array2, 0, this.\u03b9, 0, 8);
        }
        final long n = ((long)array[0] & 0xFFFFFFFFL) << 32 | ((long)array[1] & 0xFFFFFFFFL);
        if (\u04cf == 0) {
            this.\u04c0 = (int)n;
            final long n2 = n >> 3;
            final long n3 = al.\u0131;
            this.\u026a = (int)(n3 * n2 >> 32);
            this.\u0268 = (int)(n >> 32);
            \u04cf = (int)(n2 + n3);
        }
        else {
            \u04cf2 = (int)n;
            this.\u04c0 = \u04cf2;
            this.\u026a = \u04cf2 * \u04cf;
            this.\u0268 = (\u04cf2 ^ \u04cf);
            \u04cf = (int)(n >> 32);
        }
        this.\u04cf = \u04cf;
    }
    
    private int \u0399() throws IOException {
        if (this.\u0456 == Integer.MAX_VALUE) {
            this.\u0456 = super.in.read();
        }
        final int \u01c3 = this.\u01c3;
        final int n = 8;
        if (\u01c3 == 8) {
            final byte[] \u03b9 = this.\u0399;
            final int \u0456 = this.\u0456;
            \u03b9[0] = (byte)\u0456;
            if (\u0456 < 0) {
                throw new IllegalStateException("unexpected block size");
            }
            int n2 = 1;
            int n3;
            do {
                final int read = super.in.read(this.\u0399, n2, 8 - n2);
                n3 = n2;
                if (read <= 0) {
                    break;
                }
                n3 = n2 + read;
            } while ((n2 = n3) < 8);
            if (n3 < 8) {
                throw new IllegalStateException("unexpected block size");
            }
            this.\u03b9();
            final int read2 = super.in.read();
            this.\u0456 = read2;
            this.\u01c3 = 0;
            int \u0279 = n;
            if (read2 < 0) {
                \u0279 = 8 - (this.\u0399[7] & 0xFF);
            }
            this.\u0279 = \u0279;
        }
        return this.\u0279;
    }
    
    private void \u03b9() {
        if (this.\u0406 == 3) {
            final byte[] \u03b9 = this.\u0399;
            System.arraycopy(\u03b9, 0, this.\u0269, 0, \u03b9.length);
        }
        final byte[] \u03b92 = this.\u0399;
        int n = (\u03b92[0] << 24 & 0xFF000000) + (\u03b92[1] << 16 & 0xFF0000) + (\u03b92[2] << 8 & 0xFF00) + (\u03b92[3] & 0xFF);
        int n2 = (0xFF000000 & \u03b92[4] << 24) + (0xFF0000 & \u03b92[5] << 16) + (0xFF00 & \u03b92[6] << 8) + (\u03b92[7] & 0xFF);
        int n3 = 0;
        while (true) {
            final int \u0269 = this.\u0196;
            if (n3 >= \u0269) {
                break;
            }
            final int n4 = (\u0269 - n3) * al.\u0131;
            n2 -= (n4 + n ^ (n << 4) + this.\u0268 ^ (n >>> 5) + this.\u04cf);
            n -= ((n2 << 4) + this.\u04c0 ^ n4 + n2 ^ (n2 >>> 5) + this.\u026a);
            ++n3;
        }
        final byte[] \u03b93 = this.\u0399;
        \u03b93[0] = (byte)(n >> 24);
        \u03b93[1] = (byte)(n >> 16);
        \u03b93[2] = (byte)(n >> 8);
        \u03b93[3] = (byte)n;
        \u03b93[4] = (byte)(n2 >> 24);
        \u03b93[5] = (byte)(n2 >> 16);
        \u03b93[6] = (byte)(n2 >> 8);
        \u03b93[7] = (byte)n2;
        if (this.\u0406 == 3) {
            for (int i = 0; i < 8; ++i) {
                final byte[] \u03b94 = this.\u0399;
                \u03b94[i] ^= this.\u03b9[i];
            }
            final byte[] \u02692 = this.\u0269;
            System.arraycopy(\u02692, 0, this.\u03b9, 0, \u02692.length);
        }
    }
    
    @Override
    public int available() throws IOException {
        this.\u0399();
        return this.\u0279 - this.\u01c3;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        this.\u0399();
        final int \u01c3 = this.\u01c3;
        if (\u01c3 >= this.\u0279) {
            return -1;
        }
        final byte[] \u03b9 = this.\u0399;
        this.\u01c3 = \u01c3 + 1;
        return \u03b9[\u01c3] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        final int n3 = n + n2;
        int i = n;
        while (i < n3) {
            this.\u0399();
            final int \u01c3 = this.\u01c3;
            if (\u01c3 >= this.\u0279) {
                if (i == n) {
                    return -1;
                }
                return n2 - (n3 - i);
            }
            else {
                final byte[] \u03b9 = this.\u0399;
                this.\u01c3 = \u01c3 + 1;
                array[i] = \u03b9[\u01c3];
                ++i;
            }
        }
        return n2;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        long n2;
        for (n2 = 0L; n2 < n && this.read() != -1; ++n2) {}
        return n2;
    }
}
