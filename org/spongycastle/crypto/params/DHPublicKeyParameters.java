package org.spongycastle.crypto.params;

import java.math.*;

public class DHPublicKeyParameters extends DHKeyParameters
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private BigInteger y;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public DHPublicKeyParameters(final BigInteger bigInteger, final DHParameters dhParameters) {
        super(false, dhParameters);
        this.y = this.validate(bigInteger, dhParameters);
    }
    
    private BigInteger validate(final BigInteger bigInteger, final DHParameters dhParameters) {
        if (bigInteger == null) {
            throw new NullPointerException("y value cannot be null");
        }
        if (bigInteger.compareTo(DHPublicKeyParameters.TWO) < 0 || bigInteger.compareTo(dhParameters.getP().subtract(DHPublicKeyParameters.TWO)) > 0) {
            throw new IllegalArgumentException("invalid DH public key");
        }
        if (dhParameters.getQ() == null) {
            return bigInteger;
        }
        if (DHPublicKeyParameters.ONE.equals(bigInteger.modPow(dhParameters.getQ(), dhParameters.getP()))) {
            return bigInteger;
        }
        throw new IllegalArgumentException("Y value does not appear to be in correct group");
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHPublicKeyParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        boolean b3 = b2;
        if (((DHPublicKeyParameters)o).getY().equals(this.y)) {
            b3 = b2;
            if (super.equals(o)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.y.hashCode() ^ super.hashCode();
    }
}
