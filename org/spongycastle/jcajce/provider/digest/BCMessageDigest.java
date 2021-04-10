package org.spongycastle.jcajce.provider.digest;

import java.security.*;
import org.spongycastle.crypto.*;

public class BCMessageDigest extends MessageDigest
{
    protected Digest digest;
    
    protected BCMessageDigest(final Digest digest) {
        super(digest.getAlgorithmName());
        this.digest = digest;
    }
    
    public byte[] engineDigest() {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        return array;
    }
    
    public void engineReset() {
        this.digest.reset();
    }
    
    public void engineUpdate(final byte b) {
        this.digest.update(b);
    }
    
    public void engineUpdate(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
}
