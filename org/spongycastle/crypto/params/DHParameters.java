package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class DHParameters implements CipherParameters
{
    private static final int DEFAULT_MINIMUM_LENGTH = 160;
    private BigInteger g;
    private BigInteger j;
    private int l;
    private int m;
    private BigInteger p;
    private BigInteger q;
    private DHValidationParameters validation;
    
    public DHParameters(final BigInteger bigInteger, final BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null, 0);
    }
    
    public DHParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }
    
    public DHParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final int n) {
        this(bigInteger, bigInteger2, bigInteger3, getDefaultMParam(n), n, null, null);
    }
    
    public DHParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final int n, final int n2) {
        this(bigInteger, bigInteger2, bigInteger3, n, n2, null, null);
    }
    
    public DHParameters(final BigInteger p7, final BigInteger g, final BigInteger q, final int m, final int l, final BigInteger j, final DHValidationParameters validation) {
        if (l != 0) {
            if (l > p7.bitLength()) {
                throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
            }
            if (l < m) {
                throw new IllegalArgumentException("when l value specified, it may not be less than m value");
            }
        }
        this.g = g;
        this.p = p7;
        this.q = q;
        this.m = m;
        this.l = l;
        this.j = j;
        this.validation = validation;
    }
    
    public DHParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4, final DHValidationParameters dhValidationParameters) {
        this(bigInteger, bigInteger2, bigInteger3, 160, 0, bigInteger4, dhValidationParameters);
    }
    
    private static int getDefaultMParam(final int n) {
        if (n == 0) {
            return 160;
        }
        if (n < 160) {
            return n;
        }
        return 160;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DHParameters dhParameters = (DHParameters)o;
        if (this.getQ() != null) {
            if (!this.getQ().equals(dhParameters.getQ())) {
                return false;
            }
        }
        else if (dhParameters.getQ() != null) {
            return false;
        }
        boolean b3 = b2;
        if (dhParameters.getP().equals(this.p)) {
            b3 = b2;
            if (dhParameters.getG().equals(this.g)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getJ() {
        return this.j;
    }
    
    public int getL() {
        return this.l;
    }
    
    public int getM() {
        return this.m;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    public BigInteger getQ() {
        return this.q;
    }
    
    public DHValidationParameters getValidationParameters() {
        return this.validation;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.getP().hashCode();
        final int hashCode2 = this.getG().hashCode();
        int hashCode3;
        if (this.getQ() != null) {
            hashCode3 = this.getQ().hashCode();
        }
        else {
            hashCode3 = 0;
        }
        return hashCode ^ hashCode2 ^ hashCode3;
    }
}
