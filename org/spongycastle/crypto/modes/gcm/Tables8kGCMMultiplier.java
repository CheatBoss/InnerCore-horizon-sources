package org.spongycastle.crypto.modes.gcm;

import java.lang.reflect.*;
import org.spongycastle.util.*;

public class Tables8kGCMMultiplier implements GCMMultiplier
{
    private byte[] H;
    private int[][][] M;
    
    @Override
    public void init(final byte[] array) {
        if (this.M == null) {
            this.M = (int[][][])Array.newInstance(Integer.TYPE, 32, 16, 4);
        }
        else if (Arrays.areEqual(this.H, array)) {
            return;
        }
        this.H = Arrays.clone(array);
        GCMUtil.asInts(array, this.M[1][8]);
        int n = 4;
        for (int i = 4; i >= 1; i >>= 1) {
            final int[][][] m = this.M;
            GCMUtil.multiplyP(m[1][i + i], m[1][i]);
        }
        final int[][][] j = this.M;
        final int[] array2 = j[1][1];
        final int n2 = 0;
        GCMUtil.multiplyP(array2, j[0][8]);
        int n3;
        while (true) {
            n3 = n2;
            if (n < 1) {
                break;
            }
            final int[][][] k = this.M;
            GCMUtil.multiplyP(k[0][n + n], k[0][n]);
            n >>= 1;
        }
        while (true) {
            for (int l = 2; l < 16; l += l) {
                for (int n4 = 1; n4 < l; ++n4) {
                    final int[][][] m2 = this.M;
                    GCMUtil.xor(m2[n3][l], m2[n3][n4], m2[n3][l + n4]);
                }
            }
            final int n5 = n3 + 1;
            if (n5 == 32) {
                break;
            }
            if ((n3 = n5) <= 1) {
                continue;
            }
            int n6 = 8;
            while (true) {
                n3 = n5;
                if (n6 <= 0) {
                    break;
                }
                final int[][][] m3 = this.M;
                GCMUtil.multiplyP8(m3[n5 - 2][n6], m3[n5][n6]);
                n6 >>= 1;
            }
        }
    }
    
    @Override
    public void multiplyH(final byte[] array) {
        final int[] array2 = new int[4];
        for (int i = 15; i >= 0; --i) {
            final int[][][] m = this.M;
            final int n = i + i;
            final int[] array3 = m[n][array[i] & 0xF];
            array2[0] ^= array3[0];
            array2[1] ^= array3[1];
            array2[2] ^= array3[2];
            array2[3] ^= array3[3];
            final int[] array4 = m[n + 1][(array[i] & 0xF0) >>> 4];
            array2[0] ^= array4[0];
            array2[1] ^= array4[1];
            array2[2] ^= array4[2];
            array2[3] ^= array4[3];
        }
        Pack.intToBigEndian(array2, array, 0);
    }
}
