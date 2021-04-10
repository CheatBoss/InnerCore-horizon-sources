package org.spongycastle.pqc.math.ntru.polynomial;

import java.math.*;

public class Constants
{
    static final BigDecimal BIGDEC_ONE;
    static final BigInteger BIGINT_ONE;
    static final BigInteger BIGINT_ZERO;
    
    static {
        BIGINT_ZERO = BigInteger.valueOf(0L);
        BIGINT_ONE = BigInteger.valueOf(1L);
        BIGDEC_ONE = BigDecimal.valueOf(1L);
    }
}
