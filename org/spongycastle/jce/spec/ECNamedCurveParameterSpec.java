package org.spongycastle.jce.spec;

import org.spongycastle.math.ec.*;
import java.math.*;

public class ECNamedCurveParameterSpec extends ECParameterSpec
{
    private String name;
    
    public ECNamedCurveParameterSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger) {
        super(ecCurve, ecPoint, bigInteger);
        this.name = name;
    }
    
    public ECNamedCurveParameterSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        super(ecCurve, ecPoint, bigInteger, bigInteger2);
        this.name = name;
    }
    
    public ECNamedCurveParameterSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2, final byte[] array) {
        super(ecCurve, ecPoint, bigInteger, bigInteger2, array);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
