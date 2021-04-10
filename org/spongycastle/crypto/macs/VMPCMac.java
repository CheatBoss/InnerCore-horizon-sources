package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class VMPCMac implements Mac
{
    private byte[] P;
    private byte[] T;
    private byte g;
    private byte n;
    private byte s;
    private byte[] workingIV;
    private byte[] workingKey;
    private byte x1;
    private byte x2;
    private byte x3;
    private byte x4;
    
    public VMPCMac() {
        this.n = 0;
        this.P = null;
        this.s = 0;
    }
    
    private void initKey(byte[] p2, final byte[] array) {
        this.s = 0;
        this.P = new byte[256];
        for (int i = 0; i < 256; ++i) {
            this.P[i] = (byte)i;
        }
        for (int j = 0; j < 768; ++j) {
            final byte[] p3 = this.P;
            final byte s = this.s;
            final int n = j & 0xFF;
            final byte s2 = p3[s + p3[n] + p2[j % p2.length] & 0xFF];
            this.s = s2;
            final byte b = p3[n];
            final int n2 = s2 & 0xFF;
            p3[n] = p3[n2];
            p3[n2] = b;
        }
        for (int k = 0; k < 768; ++k) {
            p2 = this.P;
            final byte s3 = this.s;
            final int n3 = k & 0xFF;
            final byte s4 = p2[s3 + p2[n3] + array[k % array.length] & 0xFF];
            this.s = s4;
            final byte b2 = p2[n3];
            final int n4 = s4 & 0xFF;
            p2[n3] = p2[n4];
            p2[n4] = b2;
        }
        this.n = 0;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        for (int i = 1; i < 25; ++i) {
            final byte[] p2 = this.P;
            final byte s = this.s;
            final byte n2 = this.n;
            final int n3 = n2 & 0xFF;
            final byte s2 = p2[s + p2[n3] & 0xFF];
            this.s = s2;
            final byte x4 = this.x4;
            final byte x5 = this.x3;
            final byte x6 = p2[x4 + x5 + i & 0xFF];
            this.x4 = x6;
            final byte x7 = this.x2;
            final byte x8 = p2[x5 + x7 + i & 0xFF];
            this.x3 = x8;
            final byte x9 = this.x1;
            final byte x10 = p2[x7 + x9 + i & 0xFF];
            this.x2 = x10;
            final byte x11 = p2[x9 + s2 + i & 0xFF];
            this.x1 = x11;
            final byte[] t = this.T;
            final byte g = this.g;
            final int n4 = g & 0x1F;
            t[n4] ^= x11;
            final int n5 = g + 1 & 0x1F;
            t[n5] ^= x10;
            final int n6 = g + 2 & 0x1F;
            t[n6] ^= x8;
            final int n7 = g + 3 & 0x1F;
            t[n7] ^= x6;
            this.g = (byte)(g + 4 & 0x1F);
            final byte b = p2[n3];
            final int n8 = s2 & 0xFF;
            p2[n3] = p2[n8];
            p2[n8] = b;
            this.n = (byte)(n2 + 1 & 0xFF);
        }
        for (int j = 0; j < 768; ++j) {
            final byte[] p3 = this.P;
            final byte s3 = this.s;
            final int n9 = j & 0xFF;
            final byte s4 = p3[s3 + p3[n9] + this.T[j & 0x1F] & 0xFF];
            this.s = s4;
            final byte b2 = p3[n9];
            final int n10 = s4 & 0xFF;
            p3[n9] = p3[n10];
            p3[n10] = b2;
        }
        final byte[] array2 = new byte[20];
        for (int k = 0; k < 20; ++k) {
            final byte[] p4 = this.P;
            final byte s5 = this.s;
            final int n11 = k & 0xFF;
            final byte s6 = p4[s5 + p4[n11] & 0xFF];
            this.s = s6;
            final int n12 = s6 & 0xFF;
            array2[k] = p4[p4[p4[n12] & 0xFF] + 1 & 0xFF];
            final byte b3 = p4[n11];
            p4[n11] = p4[n12];
            p4[n12] = b3;
        }
        System.arraycopy(array2, 0, array, n, 20);
        this.reset();
        return 20;
    }
    
    @Override
    public String getAlgorithmName() {
        return "VMPC-MAC";
    }
    
    @Override
    public int getMacSize() {
        return 20;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include an IV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        final KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include a key");
        }
        final byte[] iv = parametersWithIV.getIV();
        this.workingIV = iv;
        if (iv != null && iv.length >= 1 && iv.length <= 768) {
            this.workingKey = keyParameter.getKey();
            this.reset();
            return;
        }
        throw new IllegalArgumentException("VMPC-MAC requires 1 to 768 bytes of IV");
    }
    
    @Override
    public void reset() {
        this.initKey(this.workingKey, this.workingIV);
        this.n = 0;
        this.x4 = 0;
        this.x3 = 0;
        this.x2 = 0;
        this.x1 = 0;
        this.g = 0;
        this.T = new byte[32];
        for (int i = 0; i < 32; ++i) {
            this.T[i] = 0;
        }
    }
    
    @Override
    public void update(final byte b) throws IllegalStateException {
        final byte[] p = this.P;
        final byte s = this.s;
        final byte n = this.n;
        final int n2 = n & 0xFF;
        final byte s2 = p[s + p[n2] & 0xFF];
        this.s = s2;
        final int n3 = s2 & 0xFF;
        final byte b2 = (byte)(b ^ p[p[p[n3] & 0xFF] + 1 & 0xFF]);
        final byte x4 = this.x4;
        final byte x5 = this.x3;
        final byte x6 = p[x4 + x5 & 0xFF];
        this.x4 = x6;
        final byte x7 = this.x2;
        final byte x8 = p[x5 + x7 & 0xFF];
        this.x3 = x8;
        final byte x9 = this.x1;
        final byte x10 = p[x7 + x9 & 0xFF];
        this.x2 = x10;
        final byte x11 = p[x9 + s2 + b2 & 0xFF];
        this.x1 = x11;
        final byte[] t = this.T;
        final byte g = this.g;
        final int n4 = g & 0x1F;
        t[n4] ^= x11;
        final int n5 = g + 1 & 0x1F;
        t[n5] ^= x10;
        final int n6 = g + 2 & 0x1F;
        t[n6] ^= x8;
        final int n7 = g + 3 & 0x1F;
        t[n7] ^= x6;
        this.g = (byte)(g + 4 & 0x1F);
        final byte b3 = p[n2];
        p[n2] = p[n3];
        p[n3] = b3;
        this.n = (byte)(n + 1 & 0xFF);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalStateException {
        if (n + n2 <= array.length) {
            for (int i = 0; i < n2; ++i) {
                this.update(array[n + i]);
            }
            return;
        }
        throw new DataLengthException("input buffer too short");
    }
}
