package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class PKCS12ParametersGenerator extends PBEParametersGenerator
{
    public static final int IV_MATERIAL = 2;
    public static final int KEY_MATERIAL = 1;
    public static final int MAC_MATERIAL = 3;
    private Digest digest;
    private int u;
    private int v;
    
    public PKCS12ParametersGenerator(final Digest digest) {
        this.digest = digest;
        if (digest instanceof ExtendedDigest) {
            this.u = digest.getDigestSize();
            this.v = ((ExtendedDigest)digest).getByteLength();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Digest ");
        sb.append(digest.getAlgorithmName());
        sb.append(" unsupported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void adjust(final byte[] array, final int n, final byte[] array2) {
        final int n2 = (array2[array2.length - 1] & 0xFF) + (array[array2.length + n - 1] & 0xFF) + 1;
        array[array2.length + n - 1] = (byte)n2;
        int n3 = n2 >>> 8;
        for (int i = array2.length - 2; i >= 0; --i) {
            final byte b = array2[i];
            final int n4 = n + i;
            final int n5 = n3 + ((b & 0xFF) + (array[n4] & 0xFF));
            array[n4] = (byte)n5;
            n3 = n5 >>> 8;
        }
    }
    
    private byte[] generateDerivedKey(int i, final int n) {
        final int v = this.v;
        final byte[] array = new byte[v];
        final byte[] array2 = new byte[n];
        for (int j = 0; j != v; ++j) {
            array[j] = (byte)i;
        }
        byte[] array4;
        if (this.salt != null && this.salt.length != 0) {
            i = this.v;
            final int length = this.salt.length;
            final int v2 = this.v;
            final int n2 = i * ((length + v2 - 1) / v2);
            final byte[] array3 = new byte[n2];
            i = 0;
            while (true) {
                array4 = array3;
                if (i == n2) {
                    break;
                }
                array3[i] = this.salt[i % this.salt.length];
                ++i;
            }
        }
        else {
            array4 = new byte[0];
        }
        byte[] array6;
        if (this.password != null && this.password.length != 0) {
            i = this.v;
            final int length2 = this.password.length;
            final int v3 = this.v;
            final int n3 = i * ((length2 + v3 - 1) / v3);
            final byte[] array5 = new byte[n3];
            i = 0;
            while (true) {
                array6 = array5;
                if (i == n3) {
                    break;
                }
                array5[i] = this.password[i % this.password.length];
                ++i;
            }
        }
        else {
            array6 = new byte[0];
        }
        final int n4 = array4.length + array6.length;
        final byte[] array7 = new byte[n4];
        System.arraycopy(array4, 0, array7, 0, array4.length);
        System.arraycopy(array6, 0, array7, array4.length, array6.length);
        final int v4 = this.v;
        final byte[] array8 = new byte[v4];
        final int u = this.u;
        final int n5 = (n + u - 1) / u;
        final byte[] array9 = new byte[u];
        int k;
        int l;
        int n6;
        int v5;
        int n7;
        for (i = 1; i <= n5; ++i) {
            this.digest.update(array, 0, v);
            this.digest.update(array7, 0, n4);
            this.digest.doFinal(array9, 0);
            for (k = 1; k < this.iterationCount; ++k) {
                this.digest.update(array9, 0, u);
                this.digest.doFinal(array9, 0);
            }
            for (l = 0; l != v4; ++l) {
                array8[l] = array9[l % u];
            }
            n6 = 0;
            while (true) {
                v5 = this.v;
                if (n6 == n4 / v5) {
                    break;
                }
                this.adjust(array7, v5 * n6, array8);
                ++n6;
            }
            if (i == n5) {
                n7 = (i - 1) * this.u;
                System.arraycopy(array9, 0, array2, n7, n - n7);
            }
            else {
                System.arraycopy(array9, 0, array2, (i - 1) * this.u, u);
            }
        }
        return array2;
    }
    
    @Override
    public CipherParameters generateDerivedMacParameters(int n) {
        n /= 8;
        return new KeyParameter(this.generateDerivedKey(3, n), 0, n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n) {
        n /= 8;
        return new KeyParameter(this.generateDerivedKey(1, n), 0, n);
    }
    
    @Override
    public CipherParameters generateDerivedParameters(int n, int n2) {
        n /= 8;
        n2 /= 8;
        return new ParametersWithIV(new KeyParameter(this.generateDerivedKey(1, n), 0, n), this.generateDerivedKey(2, n2), 0, n2);
    }
}
