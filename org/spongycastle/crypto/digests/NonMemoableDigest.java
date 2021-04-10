package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;

public class NonMemoableDigest implements ExtendedDigest
{
    private ExtendedDigest baseDigest;
    
    public NonMemoableDigest(final ExtendedDigest baseDigest) {
        if (baseDigest != null) {
            this.baseDigest = baseDigest;
            return;
        }
        throw new IllegalArgumentException("baseDigest must not be null");
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        return this.baseDigest.doFinal(array, n);
    }
    
    @Override
    public String getAlgorithmName() {
        return this.baseDigest.getAlgorithmName();
    }
    
    @Override
    public int getByteLength() {
        return this.baseDigest.getByteLength();
    }
    
    @Override
    public int getDigestSize() {
        return this.baseDigest.getDigestSize();
    }
    
    @Override
    public void reset() {
        this.baseDigest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.baseDigest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.baseDigest.update(array, n, n2);
    }
}
