package org.spongycastle.crypto.prng;

import org.spongycastle.util.*;

public class VMPCRandomGenerator implements RandomGenerator
{
    private byte[] P;
    private byte n;
    private byte s;
    
    public VMPCRandomGenerator() {
        this.n = 0;
        this.P = new byte[] { -69, 44, 98, 127, -75, -86, -44, 13, -127, -2, -78, -126, -53, -96, -95, 8, 24, 113, 86, -24, 73, 2, 16, -60, -34, 53, -91, -20, -128, 18, -72, 105, -38, 47, 117, -52, -94, 9, 54, 3, 97, 45, -3, -32, -35, 5, 67, -112, -83, -56, -31, -81, 87, -101, 76, -40, 81, -82, 80, -123, 60, 10, -28, -13, -100, 38, 35, 83, -55, -125, -105, 70, -79, -103, 100, 49, 119, -43, 29, -42, 120, -67, 94, -80, -118, 34, 56, -8, 104, 43, 42, -59, -45, -9, -68, 111, -33, 4, -27, -107, 62, 37, -122, -90, 11, -113, -15, 36, 14, -41, 64, -77, -49, 126, 6, 21, -102, 77, 28, -93, -37, 50, -110, 88, 17, 39, -12, 89, -48, 78, 106, 23, 91, -84, -1, 7, -64, 101, 121, -4, -57, -51, 118, 66, 93, -25, 58, 52, 122, 48, 40, 15, 115, 1, -7, -47, -46, 25, -23, -111, -71, 90, -19, 65, 109, -76, -61, -98, -65, 99, -6, 31, 51, 96, 71, -119, -16, -106, 26, 95, -109, 61, 55, 75, -39, -88, -63, 27, -10, 57, -117, -73, 12, 32, -50, -120, 110, -74, 116, -114, -115, 22, 41, -14, -121, -11, -21, 112, -29, -5, 85, -97, -58, 68, 74, 69, 125, -30, 107, 92, 108, 102, -87, -116, -18, -124, 19, -89, 30, -99, -36, 103, 72, -70, 46, -26, -92, -85, 124, -108, 0, 33, -17, -22, -66, -54, 114, 79, 82, -104, 63, -62, 20, 123, 59, 84 };
        this.s = -66;
    }
    
    @Override
    public void addSeedMaterial(final long n) {
        this.addSeedMaterial(Pack.longToBigEndian(n));
    }
    
    @Override
    public void addSeedMaterial(final byte[] array) {
        for (int i = 0; i < array.length; ++i) {
            final byte[] p = this.P;
            final byte s = this.s;
            final byte n = this.n;
            final int n2 = n & 0xFF;
            final byte s2 = p[s + p[n2] + array[i] & 0xFF];
            this.s = s2;
            final byte b = p[n2];
            final int n3 = s2 & 0xFF;
            p[n2] = p[n3];
            p[n3] = b;
            this.n = (byte)(n + 1 & 0xFF);
        }
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        this.nextBytes(array, 0, array.length);
    }
    
    @Override
    public void nextBytes(final byte[] array, final int n, final int n2) {
        final byte[] p3 = this.P;
        // monitorenter(p3)
        int n3 = n;
    Label_0178_Outer:
        while (true) {
            Label_0174: {
                if (n3 == n2 + n) {
                    break Label_0174;
                }
                while (true) {
                    try {
                        final byte s = this.P[this.s + this.P[this.n & 0xFF] & 0xFF];
                        this.s = s;
                        final byte[] p4 = this.P;
                        final byte[] p5 = this.P;
                        final byte[] p6 = this.P;
                        final int n4 = s & 0xFF;
                        array[n3] = p4[p5[p6[n4] & 0xFF] + 1 & 0xFF];
                        final byte b = this.P[this.n & 0xFF];
                        this.P[this.n & 0xFF] = this.P[n4];
                        this.P[n4] = b;
                        this.n = (byte)(this.n + 1 & 0xFF);
                        ++n3;
                        continue Label_0178_Outer;
                        // monitorexit(p3)
                        throw array;
                    }
                    // monitorexit(p3)
                    finally {
                        continue;
                    }
                    break;
                }
            }
        }
    }
}
