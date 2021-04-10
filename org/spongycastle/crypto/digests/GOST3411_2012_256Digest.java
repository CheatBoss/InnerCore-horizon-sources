package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public final class GOST3411_2012_256Digest extends GOST3411_2012Digest
{
    private static final byte[] IV;
    
    static {
        IV = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    }
    
    public GOST3411_2012_256Digest() {
        super(GOST3411_2012_256Digest.IV);
    }
    
    public GOST3411_2012_256Digest(final GOST3411_2012_256Digest gost3411_2012_256Digest) {
        super(GOST3411_2012_256Digest.IV);
        this.reset(gost3411_2012_256Digest);
    }
    
    @Override
    public Memoable copy() {
        return new GOST3411_2012_256Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] array2 = new byte[64];
        super.doFinal(array2, 0);
        System.arraycopy(array2, 32, array, n, 32);
        return 32;
    }
    
    @Override
    public String getAlgorithmName() {
        return "GOST3411-2012-256";
    }
    
    @Override
    public int getDigestSize() {
        return 32;
    }
}
