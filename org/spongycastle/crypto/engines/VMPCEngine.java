package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class VMPCEngine implements StreamCipher
{
    protected byte[] P;
    protected byte n;
    protected byte s;
    protected byte[] workingIV;
    protected byte[] workingKey;
    
    public VMPCEngine() {
        this.n = 0;
        this.P = null;
        this.s = 0;
    }
    
    @Override
    public String getAlgorithmName() {
        return "VMPC";
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("VMPC init parameters must include an IV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("VMPC init parameters must include a key");
        }
        final KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        final byte[] iv = parametersWithIV.getIV();
        this.workingIV = iv;
        if (iv != null && iv.length >= 1 && iv.length <= 768) {
            this.initKey(this.workingKey = keyParameter.getKey(), this.workingIV);
            return;
        }
        throw new IllegalArgumentException("VMPC requires 1 to 768 bytes of IV");
    }
    
    protected void initKey(byte[] p2, final byte[] array) {
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
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (n + n2 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + n2 <= array2.length) {
            for (int i = 0; i < n2; ++i) {
                final byte[] p5 = this.P;
                final byte s = this.s;
                final byte n4 = this.n;
                final int n5 = n4 & 0xFF;
                final byte s2 = p5[s + p5[n5] & 0xFF];
                this.s = s2;
                final int n6 = s2 & 0xFF;
                final byte b = p5[p5[p5[n6] & 0xFF] + 1 & 0xFF];
                final byte b2 = p5[n5];
                p5[n5] = p5[n6];
                p5[n6] = b2;
                this.n = (byte)(n4 + 1 & 0xFF);
                array2[i + n3] = (byte)(array[i + n] ^ b);
            }
            return n2;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        this.initKey(this.workingKey, this.workingIV);
    }
    
    @Override
    public byte returnByte(final byte b) {
        final byte[] p = this.P;
        final byte s = this.s;
        final byte n = this.n;
        final int n2 = n & 0xFF;
        final byte s2 = p[s + p[n2] & 0xFF];
        this.s = s2;
        final int n3 = s2 & 0xFF;
        final byte b2 = p[p[p[n3] & 0xFF] + 1 & 0xFF];
        final byte b3 = p[n2];
        p[n2] = p[n3];
        p[n3] = b3;
        this.n = (byte)(n + 1 & 0xFF);
        return (byte)(b ^ b2);
    }
}
