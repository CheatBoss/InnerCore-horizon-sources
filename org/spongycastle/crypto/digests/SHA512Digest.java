package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class SHA512Digest extends LongDigest
{
    private static final int DIGEST_LENGTH = 64;
    
    public SHA512Digest() {
    }
    
    public SHA512Digest(final SHA512Digest sha512Digest) {
        super(sha512Digest);
    }
    
    public SHA512Digest(final byte[] array) {
        this.restoreState(array);
    }
    
    @Override
    public Memoable copy() {
        return new SHA512Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        Pack.longToBigEndian(this.H1, array, n);
        Pack.longToBigEndian(this.H2, array, n + 8);
        Pack.longToBigEndian(this.H3, array, n + 16);
        Pack.longToBigEndian(this.H4, array, n + 24);
        Pack.longToBigEndian(this.H5, array, n + 32);
        Pack.longToBigEndian(this.H6, array, n + 40);
        Pack.longToBigEndian(this.H7, array, n + 48);
        Pack.longToBigEndian(this.H8, array, n + 56);
        this.reset();
        return 64;
    }
    
    @Override
    public String getAlgorithmName() {
        return "SHA-512";
    }
    
    @Override
    public int getDigestSize() {
        return 64;
    }
    
    @Override
    public byte[] getEncodedState() {
        final byte[] array = new byte[this.getEncodedStateSize()];
        super.populateState(array);
        return array;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.H1 = 7640891576956012808L;
        this.H2 = -4942790177534073029L;
        this.H3 = 4354685564936845355L;
        this.H4 = -6534734903238641935L;
        this.H5 = 5840696475078001361L;
        this.H6 = -7276294671716946913L;
        this.H7 = 2270897969802886507L;
        this.H8 = 6620516959819538809L;
    }
    
    @Override
    public void reset(final Memoable memoable) {
        this.copyIn((LongDigest)memoable);
    }
}
