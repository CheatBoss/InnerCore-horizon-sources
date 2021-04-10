package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class HC128Engine implements StreamCipher
{
    private byte[] buf;
    private int cnt;
    private int idx;
    private boolean initialised;
    private byte[] iv;
    private byte[] key;
    private int[] p;
    private int[] q;
    
    public HC128Engine() {
        this.p = new int[512];
        this.q = new int[512];
        this.cnt = 0;
        this.buf = new byte[4];
        this.idx = 0;
    }
    
    private static int dim(final int n, final int n2) {
        return mod512(n - n2);
    }
    
    private static int f1(final int n) {
        return rotateRight(n, 18) ^ rotateRight(n, 7) ^ n >>> 3;
    }
    
    private static int f2(final int n) {
        return rotateRight(n, 19) ^ rotateRight(n, 17) ^ n >>> 10;
    }
    
    private int g1(final int n, final int n2, final int n3) {
        return (rotateRight(n, 10) ^ rotateRight(n3, 23)) + rotateRight(n2, 8);
    }
    
    private int g2(final int n, final int n2, final int n3) {
        return (rotateLeft(n, 10) ^ rotateLeft(n3, 23)) + rotateLeft(n2, 8);
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
    
    private int h1(final int n) {
        final int[] q = this.q;
        return q[n & 0xFF] + q[(n >> 16 & 0xFF) + 256];
    }
    
    private int h2(final int n) {
        final int[] p = this.p;
        return p[n & 0xFF] + p[(n >> 16 & 0xFF) + 256];
    }
    
    private void init() {
        final int length = this.key.length;
        final int n = 16;
        if (length == 16) {
            this.idx = 0;
            this.cnt = 0;
            final int[] array = new int[1280];
            for (int i = 0; i < 16; ++i) {
                final int n2 = i >> 2;
                array[n2] |= (this.key[i] & 0xFF) << (i & 0x3) * 8;
            }
            System.arraycopy(array, 0, array, 4, 4);
            int n3 = 0;
            while (true) {
                final byte[] iv = this.iv;
                if (n3 >= iv.length || n3 >= 16) {
                    break;
                }
                final int n4 = (n3 >> 2) + 8;
                array[n4] |= (iv[n3] & 0xFF) << (n3 & 0x3) * 8;
                ++n3;
            }
            System.arraycopy(array, 8, array, 12, 4);
            for (int j = n; j < 1280; ++j) {
                array[j] = f2(array[j - 2]) + array[j - 7] + f1(array[j - 15]) + array[j - 16] + j;
            }
            System.arraycopy(array, 256, this.p, 0, 512);
            System.arraycopy(array, 768, this.q, 0, 512);
            for (int k = 0; k < 512; ++k) {
                this.p[k] = this.step();
            }
            for (int l = 0; l < 512; ++l) {
                this.q[l] = this.step();
            }
            this.cnt = 0;
            return;
        }
        throw new IllegalArgumentException("The key must be 128 bits long");
    }
    
    private static int mod1024(final int n) {
        return n & 0x3FF;
    }
    
    private static int mod512(final int n) {
        return n & 0x1FF;
    }
    
    private static int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    private static int rotateRight(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private int step() {
        final int mod512 = mod512(this.cnt);
        int n;
        int n2;
        if (this.cnt < 512) {
            final int[] p = this.p;
            p[mod512] += this.g1(p[dim(mod512, 3)], this.p[dim(mod512, 10)], this.p[dim(mod512, 511)]);
            n = this.h1(this.p[dim(mod512, 12)]);
            n2 = this.p[mod512];
        }
        else {
            final int[] q = this.q;
            q[mod512] += this.g2(q[dim(mod512, 3)], this.q[dim(mod512, 10)], this.q[dim(mod512, 511)]);
            n = this.h2(this.q[dim(mod512, 12)]);
            n2 = this.q[mod512];
        }
        this.cnt = mod1024(this.cnt + 1);
        return n2 ^ n;
    }
    
    @Override
    public String getAlgorithmName() {
        return "HC-128";
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
        sb.append("Invalid parameter passed to HC128 init - ");
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
