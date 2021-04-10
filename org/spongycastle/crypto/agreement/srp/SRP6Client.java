package org.spongycastle.crypto.agreement.srp;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SRP6Client
{
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger Key;
    protected BigInteger M1;
    protected BigInteger M2;
    protected BigInteger N;
    protected BigInteger S;
    protected BigInteger a;
    protected Digest digest;
    protected BigInteger g;
    protected SecureRandom random;
    protected BigInteger u;
    protected BigInteger x;
    
    private BigInteger calculateS() {
        return this.B.subtract(this.g.modPow(this.x, this.N).multiply(SRP6Util.calculateK(this.digest, this.N, this.g)).mod(this.N)).mod(this.N).modPow(this.u.multiply(this.x).add(this.a), this.N);
    }
    
    public BigInteger calculateClientEvidenceMessage() throws CryptoException {
        final BigInteger a = this.A;
        if (a != null) {
            final BigInteger b = this.B;
            if (b != null) {
                final BigInteger s = this.S;
                if (s != null) {
                    return this.M1 = SRP6Util.calculateM1(this.digest, this.N, a, b, s);
                }
            }
        }
        throw new CryptoException("Impossible to compute M1: some data are missing from the previous operations (A,B,S)");
    }
    
    public BigInteger calculateSecret(BigInteger bigInteger) throws CryptoException {
        bigInteger = SRP6Util.validatePublicValue(this.N, bigInteger);
        this.B = bigInteger;
        this.u = SRP6Util.calculateU(this.digest, this.N, this.A, bigInteger);
        bigInteger = this.calculateS();
        return this.S = bigInteger;
    }
    
    public BigInteger calculateSessionKey() throws CryptoException {
        final BigInteger s = this.S;
        if (s != null && this.M1 != null && this.M2 != null) {
            return this.Key = SRP6Util.calculateKey(this.digest, this.N, s);
        }
        throw new CryptoException("Impossible to compute Key: some data are missing from the previous operations (S,M1,M2)");
    }
    
    public BigInteger generateClientCredentials(final byte[] array, final byte[] array2, final byte[] array3) {
        this.x = SRP6Util.calculateX(this.digest, this.N, array, array2, array3);
        final BigInteger selectPrivateValue = this.selectPrivateValue();
        this.a = selectPrivateValue;
        return this.A = this.g.modPow(selectPrivateValue, this.N);
    }
    
    public void init(final BigInteger n, final BigInteger g, final Digest digest, final SecureRandom random) {
        this.N = n;
        this.g = g;
        this.digest = digest;
        this.random = random;
    }
    
    public void init(final SRP6GroupParameters srp6GroupParameters, final Digest digest, final SecureRandom secureRandom) {
        this.init(srp6GroupParameters.getN(), srp6GroupParameters.getG(), digest, secureRandom);
    }
    
    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
    }
    
    public boolean verifyServerEvidenceMessage(final BigInteger m2) throws CryptoException {
        final BigInteger a = this.A;
        if (a != null) {
            final BigInteger m3 = this.M1;
            if (m3 != null) {
                final BigInteger s = this.S;
                if (s != null) {
                    if (SRP6Util.calculateM2(this.digest, this.N, a, m3, s).equals(m2)) {
                        this.M2 = m2;
                        return true;
                    }
                    return false;
                }
            }
        }
        throw new CryptoException("Impossible to compute and verify M2: some data are missing from the previous operations (A,M1,S)");
    }
}
