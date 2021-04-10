package com.appsflyer.internal;

import java.io.*;

public class an extends FilterInputStream
{
    private byte[] \u0131;
    private int \u0196;
    private ak \u01c3;
    private final int \u0269;
    private int \u0279;
    private byte[] \u0399;
    private byte[] \u03b9;
    private int[] \u0406;
    private int \u0456;
    private int \u04c0;
    
    public an(final InputStream inputStream, final int[] array, final byte[] array2, final int n, final boolean b, final int \u0279) throws IOException {
        super(inputStream);
        this.\u0196 = Integer.MAX_VALUE;
        this.\u0269 = Math.min(Math.max(n, 3), 16);
        this.\u0131 = new byte[8];
        final byte[] \u03b9 = new byte[8];
        this.\u0399 = \u03b9;
        this.\u03b9 = new byte[8];
        this.\u0406 = new int[2];
        this.\u0456 = 8;
        this.\u04c0 = 8;
        this.\u0279 = \u0279;
        if (\u0279 == 2) {
            System.arraycopy(array2, 0, \u03b9, 0, 8);
        }
        this.\u01c3 = new ak(array, this.\u0269, true, b);
    }
    
    private void \u0131() {
        if (this.\u0279 == 2) {
            final byte[] \u0131 = this.\u0131;
            System.arraycopy(\u0131, 0, this.\u03b9, 0, \u0131.length);
        }
        final byte[] \u01312 = this.\u0131;
        aj.\u0131((\u01312[0] << 24 & 0xFF000000) + (\u01312[1] << 16 & 0xFF0000) + (\u01312[2] << 8 & 0xFF00) + (\u01312[3] & 0xFF), (0xFF000000 & \u01312[4] << 24) + (0xFF0000 & \u01312[5] << 16) + (0xFF00 & \u01312[6] << 8) + (\u01312[7] & 0xFF), false, this.\u0269, this.\u01c3.\u01c3, this.\u01c3.\u0269, this.\u0406);
        final int[] \u0456 = this.\u0406;
        final int n = \u0456[0];
        final int n2 = \u0456[1];
        final byte[] \u01313 = this.\u0131;
        \u01313[0] = (byte)(n >> 24);
        \u01313[1] = (byte)(n >> 16);
        \u01313[2] = (byte)(n >> 8);
        \u01313[3] = (byte)n;
        \u01313[4] = (byte)(n2 >> 24);
        \u01313[5] = (byte)(n2 >> 16);
        \u01313[6] = (byte)(n2 >> 8);
        \u01313[7] = (byte)n2;
        if (this.\u0279 == 2) {
            for (int i = 0; i < 8; ++i) {
                final byte[] \u01314 = this.\u0131;
                \u01314[i] ^= this.\u0399[i];
            }
            final byte[] \u03b9 = this.\u03b9;
            System.arraycopy(\u03b9, 0, this.\u0399, 0, \u03b9.length);
        }
    }
    
    private int \u0399() throws IOException {
        if (this.\u0196 == Integer.MAX_VALUE) {
            this.\u0196 = super.in.read();
        }
        final int \u0456 = this.\u0456;
        final int n = 8;
        if (\u0456 == 8) {
            final byte[] \u0131 = this.\u0131;
            final int \u0269 = this.\u0196;
            \u0131[0] = (byte)\u0269;
            if (\u0269 < 0) {
                throw new IllegalStateException("unexpected block size");
            }
            int n2 = 1;
            int n3;
            do {
                final int read = super.in.read(this.\u0131, n2, 8 - n2);
                n3 = n2;
                if (read <= 0) {
                    break;
                }
                n3 = n2 + read;
            } while ((n2 = n3) < 8);
            if (n3 < 8) {
                throw new IllegalStateException("unexpected block size");
            }
            this.\u0131();
            final int read2 = super.in.read();
            this.\u0196 = read2;
            this.\u0456 = 0;
            int \u04cf = n;
            if (read2 < 0) {
                \u04cf = 8 - (this.\u0131[7] & 0xFF);
            }
            this.\u04c0 = \u04cf;
        }
        return this.\u04c0;
    }
    
    @Override
    public int available() throws IOException {
        this.\u0399();
        return this.\u04c0 - this.\u0456;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        this.\u0399();
        final int \u0456 = this.\u0456;
        if (\u0456 >= this.\u04c0) {
            return -1;
        }
        final byte[] \u0131 = this.\u0131;
        this.\u0456 = \u0456 + 1;
        return \u0131[\u0456] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        final int n3 = n + n2;
        int i = n;
        while (i < n3) {
            this.\u0399();
            final int \u0456 = this.\u0456;
            if (\u0456 >= this.\u04c0) {
                if (i == n) {
                    return -1;
                }
                return n2 - (n3 - i);
            }
            else {
                final byte[] \u0131 = this.\u0131;
                this.\u0456 = \u0456 + 1;
                array[i] = \u0131[\u0456];
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
