package org.spongycastle.crypto.agreement.srp;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SRP6Server
{
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger Key;
    protected BigInteger M1;
    protected BigInteger M2;
    protected BigInteger N;
    protected BigInteger S;
    protected BigInteger b;
    protected Digest digest;
    protected BigInteger g;
    protected SecureRandom random;
    protected BigInteger u;
    protected BigInteger v;
    
    private BigInteger calculateS() {
        return this.v.modPow(this.u, this.N).multiply(this.A).mod(this.N).modPow(this.b, this.N);
    }
    
    public BigInteger calculateSecret(BigInteger bigInteger) throws CryptoException {
        bigInteger = SRP6Util.validatePublicValue(this.N, bigInteger);
        this.A = bigInteger;
        this.u = SRP6Util.calculateU(this.digest, this.N, bigInteger, this.B);
        bigInteger = this.calculateS();
        return this.S = bigInteger;
    }
    
    public BigInteger calculateServerEvidenceMessage() throws CryptoException {
        final BigInteger a = this.A;
        if (a != null) {
            final BigInteger m1 = this.M1;
            if (m1 != null) {
                final BigInteger s = this.S;
                if (s != null) {
                    return this.M2 = SRP6Util.calculateM2(this.digest, this.N, a, m1, s);
                }
            }
        }
        throw new CryptoException("Impossible to compute M2: some data are missing from the previous operations (A,M1,S)");
    }
    
    public BigInteger calculateSessionKey() throws CryptoException {
        final BigInteger s = this.S;
        if (s != null && this.M1 != null && this.M2 != null) {
            return this.Key = SRP6Util.calculateKey(this.digest, this.N, s);
        }
        throw new CryptoException("Impossible to compute Key: some data are missing from the previous operations (S,M1,M2)");
    }
    
    public BigInteger generateServerCredentials() {
        final BigInteger calculateK = SRP6Util.calculateK(this.digest, this.N, this.g);
        this.b = this.selectPrivateValue();
        return this.B = calculateK.multiply(this.v).mod(this.N).add(this.g.modPow(this.b, this.N)).mod(this.N);
    }
    
    public void init(final BigInteger n, final BigInteger g, final BigInteger v, final Digest digest, final SecureRandom random) {
        this.N = n;
        this.g = g;
        this.v = v;
        this.random = random;
        this.digest = digest;
    }
    
    public void init(final SRP6GroupParameters srp6GroupParameters, final BigInteger bigInteger, final Digest digest, final SecureRandom secureRandom) {
        this.init(srp6GroupParameters.getN(), srp6GroupParameters.getG(), bigInteger, digest, secureRandom);
    }
    
    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
    }
    
    public boolean verifyClientEvidenceMessage(final BigInteger m1) throws CryptoException {
        final BigInteger a = this.A;
        if (a != null) {
            final BigInteger b = this.B;
            if (b != null) {
                final BigInteger s = this.S;
                if (s != null) {
                    if (SRP6Util.calculateM1(this.digest, this.N, a, b, s).equals(m1)) {
                        this.M1 = m1;
                        return true;
                    }
                    return false;
                }
            }
        }
        throw new CryptoException("Impossible to compute and verify M1: some data are missing from the previous operations (A,B,S)");
    }
}
