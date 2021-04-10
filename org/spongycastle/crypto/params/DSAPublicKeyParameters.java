package org.spongycastle.crypto.params;

import java.math.*;

public class DSAPublicKeyParameters extends DSAKeyParameters
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private BigInteger y;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public DSAPublicKeyParameters(final BigInteger bigInteger, final DSAParameters dsaParameters) {
        super(false, dsaParameters);
        this.y = this.validate(bigInteger, dsaParameters);
    }
    
    private BigInteger validate(final BigInteger bigInteger, final DSAParameters dsaParameters) {
        if (dsaParameters == null) {
            return bigInteger;
        }
        if (DSAPublicKeyParameters.TWO.compareTo(bigInteger) <= 0 && dsaParameters.getP().subtract(DSAPublicKeyParameters.TWO).compareTo(bigInteger) >= 0 && DSAPublicKeyParameters.ONE.equals(bigInteger.modPow(dsaParameters.getQ(), dsaParameters.getP()))) {
            return bigInteger;
        }
        throw new IllegalArgumentException("y value does not appear to be in correct group");
    }
    
    public BigInteger getY() {
        return this.y;
    }
}
