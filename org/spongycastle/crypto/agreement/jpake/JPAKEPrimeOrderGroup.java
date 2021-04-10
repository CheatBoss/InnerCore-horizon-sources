package org.spongycastle.crypto.agreement.jpake;

import java.math.*;

public class JPAKEPrimeOrderGroup
{
    private final BigInteger g;
    private final BigInteger p;
    private final BigInteger q;
    
    public JPAKEPrimeOrderGroup(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, false);
    }
    
    JPAKEPrimeOrderGroup(final BigInteger p4, final BigInteger q, final BigInteger g, final boolean b) {
        JPAKEUtil.validateNotNull(p4, "p");
        JPAKEUtil.validateNotNull(q, "q");
        JPAKEUtil.validateNotNull(g, "g");
        if (!b) {
            if (!p4.subtract(JPAKEUtil.ONE).mod(q).equals(JPAKEUtil.ZERO)) {
                throw new IllegalArgumentException("p-1 must be evenly divisible by q");
            }
            if (g.compareTo(BigInteger.valueOf(2L)) == -1 || g.compareTo(p4.subtract(JPAKEUtil.ONE)) == 1) {
                throw new IllegalArgumentException("g must be in [2, p-1]");
            }
            if (!g.modPow(q, p4).equals(JPAKEUtil.ONE)) {
                throw new IllegalArgumentException("g^q mod p must equal 1");
            }
            if (!p4.isProbablePrime(20)) {
                throw new IllegalArgumentException("p must be prime");
            }
            if (!q.isProbablePrime(20)) {
                throw new IllegalArgumentException("q must be prime");
            }
        }
        this.p = p4;
        this.q = q;
        this.g = g;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    public BigInteger getQ() {
        return this.q;
    }
}
