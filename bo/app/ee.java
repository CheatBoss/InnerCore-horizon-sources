package bo.app;

import java.math.*;

public final class ee
{
    private static final BigDecimal a;
    
    static {
        a = new BigDecimal("100");
    }
    
    public static BigDecimal a(final BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }
}
