package org.spongycastle.crypto.engines;

public class VMPCKSA3Engine extends VMPCEngine
{
    @Override
    public String getAlgorithmName() {
        return "VMPC-KSA3";
    }
    
    @Override
    protected void initKey(final byte[] array, byte[] p2) {
        this.s = 0;
        this.P = new byte[256];
        for (int i = 0; i < 256; ++i) {
            this.P[i] = (byte)i;
        }
        for (int j = 0; j < 768; ++j) {
            final byte[] p3 = this.P;
            final byte s = this.s;
            final byte[] p4 = this.P;
            final int n = j & 0xFF;
            this.s = p3[s + p4[n] + array[j % array.length] & 0xFF];
            final byte b = this.P[n];
            this.P[n] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = b;
        }
        for (int k = 0; k < 768; ++k) {
            final byte[] p5 = this.P;
            final byte s2 = this.s;
            final byte[] p6 = this.P;
            final int n2 = k & 0xFF;
            this.s = p5[s2 + p6[n2] + p2[k % p2.length] & 0xFF];
            final byte b2 = this.P[n2];
            this.P[n2] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = b2;
        }
        for (int l = 0; l < 768; ++l) {
            p2 = this.P;
            final byte s3 = this.s;
            final byte[] p7 = this.P;
            final int n3 = l & 0xFF;
            this.s = p2[s3 + p7[n3] + array[l % array.length] & 0xFF];
            final byte b3 = this.P[n3];
            this.P[n3] = this.P[this.s & 0xFF];
            this.P[this.s & 0xFF] = b3;
        }
        this.n = 0;
    }
}
