package org.spongycastle.crypto.agreement.srp;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SRP6VerifierGenerator
{
    protected BigInteger N;
    protected Digest digest;
    protected BigInteger g;
    
    public BigInteger generateVerifier(final byte[] array, final byte[] array2, final byte[] array3) {
        return this.g.modPow(SRP6Util.calculateX(this.digest, this.N, array, array2, array3), this.N);
    }
    
    public void init(final BigInteger n, final BigInteger g, final Digest digest) {
        this.N = n;
        this.g = g;
        this.digest = digest;
    }
    
    public void init(final SRP6GroupParameters srp6GroupParameters, final Digest digest) {
        this.N = srp6GroupParameters.getN();
        this.g = srp6GroupParameters.getG();
        this.digest = digest;
    }
}
