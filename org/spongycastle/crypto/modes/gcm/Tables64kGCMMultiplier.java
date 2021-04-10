package org.spongycastle.crypto.modes.gcm;

import java.lang.reflect.*;
import org.spongycastle.util.*;

public class Tables64kGCMMultiplier implements GCMMultiplier
{
    private byte[] H;
    private int[][][] M;
    
    @Override
    public void init(final byte[] array) {
        if (this.M == null) {
            this.M = (int[][][])Array.newInstance(Integer.TYPE, 16, 256, 4);
        }
        else if (Arrays.areEqual(this.H, array)) {
            return;
        }
        this.H = Arrays.clone(array);
        final int[][][] m = this.M;
        final int n = 0;
        GCMUtil.asInts(array, m[0][128]);
        int n2 = 64;
        int n3;
        while (true) {
            n3 = n;
            if (n2 < 1) {
                break;
            }
            final int[][][] i = this.M;
            GCMUtil.multiplyP(i[0][n2 + n2], i[0][n2]);
            n2 >>= 1;
        }
        while (true) {
            for (int j = 2; j < 256; j += j) {
                for (int k = 1; k < j; ++k) {
                    final int[][][] l = this.M;
                    GCMUtil.xor(l[n3][j], l[n3][k], l[n3][j + k]);
                }
            }
            final int n4 = n3 + 1;
            if (n4 == 16) {
                break;
            }
            int n5 = 128;
            while (true) {
                n3 = n4;
                if (n5 <= 0) {
                    break;
                }
                final int[][][] m2 = this.M;
                GCMUtil.multiplyP8(m2[n4 - 1][n5], m2[n4][n5]);
                n5 >>= 1;
            }
        }
    }
    
    @Override
    public void multiplyH(final byte[] array) {
        final int[] array2 = new int[4];
        for (int i = 15; i >= 0; --i) {
            final int[] array3 = this.M[i][array[i] & 0xFF];
            array2[0] ^= array3[0];
            array2[1] ^= array3[1];
            array2[2] ^= array3[2];
            array2[3] ^= array3[3];
        }
        Pack.intToBigEndian(array2, array, 0);
    }
}
