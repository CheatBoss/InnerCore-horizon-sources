package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class HC256Engine implements StreamCipher
{
    private byte[] buf;
    private int cnt;
    private int idx;
    private boolean initialised;
    private byte[] iv;
    private byte[] key;
    private int[] p;
    private int[] q;
    
    public HC256Engine() {
        this.p = new int[1024];
        this.q = new int[1024];
        this.cnt = 0;
        this.buf = new byte[4];
        this.idx = 0;
    }
    
    private byte getByte() {
        if (this.idx == 0) {
            final int step = this.step();
            final byte[] buf = this.buf;
            buf[0] = (byte)(step & 0xFF);
            final int n = step >> 8;
            buf[1] = (byte)(n & 0xFF);
            final int n2 = n >> 8;
            buf[2] = (byte)(n2 & 0xFF);
            buf[3] = (byte)(n2 >> 8 & 0xFF);
        }
        final byte[] buf2 = this.buf;
        final int idx = this.idx;
        final byte b = buf2[idx];
        this.idx = (0x3 & idx + 1);
        return b;
    }
    
    private void init() {
        final byte[] key = this.key;
        final int length = key.length;
        final int n = 16;
        if (length != 32 && key.length != 16) {
            throw new IllegalArgumentException("The key must be 128/256 bits long");
        }
        if (this.iv.length >= 16) {
            final byte[] key2 = this.key;
            if (key2.length != 32) {
                final byte[] key3 = new byte[32];
                System.arraycopy(key2, 0, key3, 0, key2.length);
                final byte[] key4 = this.key;
                System.arraycopy(key4, 0, key3, 16, key4.length);
                this.key = key3;
            }
            final byte[] iv = this.iv;
            if (iv.length < 32) {
                final byte[] iv2 = new byte[32];
                System.arraycopy(iv, 0, iv2, 0, iv.length);
                final byte[] iv3 = this.iv;
                System.arraycopy(iv3, 0, iv2, iv3.length, 32 - iv3.length);
                this.iv = iv2;
            }
            this.idx = 0;
            this.cnt = 0;
            final int[] array = new int[2560];
            for (int i = 0; i < 32; ++i) {
                final int n2 = i >> 2;
                array[n2] |= (this.key[i] & 0xFF) << (i & 0x3) * 8;
            }
            int n3 = 0;
            int j;
            while (true) {
                j = n;
                if (n3 >= 32) {
                    break;
                }
                final int n4 = (n3 >> 2) + 8;
                array[n4] |= (this.iv[n3] & 0xFF) << (n3 & 0x3) * 8;
                ++n3;
            }
            while (j < 2560) {
                final int n5 = array[j - 2];
                final int n6 = array[j - 15];
                array[j] = (rotateRight(n5, 19) ^ rotateRight(n5, 17) ^ n5 >>> 10) + array[j - 7] + (rotateRight(n6, 18) ^ rotateRight(n6, 7) ^ n6 >>> 3) + array[j - 16] + j;
                ++j;
            }
            System.arraycopy(array, 512, this.p, 0, 1024);
            System.arraycopy(array, 1536, this.q, 0, 1024);
            for (int k = 0; k < 4096; ++k) {
                this.step();
            }
            this.cnt = 0;
            return;
        }
        throw new IllegalArgumentException("The IV must be at least 128 bits long");
    }
    
    private static int rotateRight(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private int step() {
        final int cnt = this.cnt;
        final int n = cnt & 0x3FF;
        int n7;
        int n8;
        if (cnt < 1024) {
            final int[] p = this.p;
            final int n2 = p[n - 3 & 0x3FF];
            final int n3 = p[n - 1023 & 0x3FF];
            final int n4 = p[n];
            final int n5 = p[n - 10 & 0x3FF];
            final int rotateRight = rotateRight(n2, 10);
            final int rotateRight2 = rotateRight(n3, 23);
            final int[] q = this.q;
            p[n] = n4 + (n5 + (rotateRight2 ^ rotateRight) + q[(n2 ^ n3) & 0x3FF]);
            final int[] p2 = this.p;
            final int n6 = p2[n - 12 & 0x3FF];
            n7 = q[n6 & 0xFF] + q[(n6 >> 8 & 0xFF) + 256] + q[(n6 >> 16 & 0xFF) + 512] + q[(n6 >> 24 & 0xFF) + 768];
            n8 = p2[n];
        }
        else {
            final int[] q2 = this.q;
            final int n9 = q2[n - 3 & 0x3FF];
            final int n10 = q2[n - 1023 & 0x3FF];
            final int n11 = q2[n];
            final int n12 = q2[n - 10 & 0x3FF];
            final int rotateRight3 = rotateRight(n9, 10);
            final int rotateRight4 = rotateRight(n10, 23);
            final int[] p3 = this.p;
            q2[n] = n11 + (n12 + (rotateRight4 ^ rotateRight3) + p3[(n9 ^ n10) & 0x3FF]);
            final int[] q3 = this.q;
            final int n13 = q3[n - 12 & 0x3FF];
            n7 = p3[n13 & 0xFF] + p3[(n13 >> 8 & 0xFF) + 256] + p3[(n13 >> 16 & 0xFF) + 512] + p3[(n13 >> 24 & 0xFF) + 768];
            n8 = q3[n];
        }
        this.cnt = (this.cnt + 1 & 0x7FF);
        return n8 ^ n7;
    }
    
    @Override
    public String getAlgorithmName() {
        return "HC-256";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        CipherParameters parameters;
        if (cipherParameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.iv = parametersWithIV.getIV();
            parameters = parametersWithIV.getParameters();
        }
        else {
            this.iv = new byte[0];
            parameters = cipherParameters;
        }
        if (parameters instanceof KeyParameter) {
            this.key = ((KeyParameter)parameters).getKey();
            this.init();
            this.initialised = true;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid parameter passed to HC256 init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        if (!this.initialised) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 <= array2.length) {
            for (int i = 0; i < n2; ++i) {
                array2[n3 + i] = (byte)(array[n + i] ^ this.getByte());
            }
            return n2;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        this.init();
    }
    
    @Override
    public byte returnByte(final byte b) {
        return (byte)(b ^ this.getByte());
    }
}
