package org.spongycastle.jce.spec;

import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;
import java.security.spec.*;
import org.spongycastle.math.field.*;

public class ECNamedCurveSpec extends ECParameterSpec
{
    private String name;
    
    public ECNamedCurveSpec(final String name, final EllipticCurve ellipticCurve, final ECPoint ecPoint, final BigInteger bigInteger) {
        super(ellipticCurve, ecPoint, bigInteger, 1);
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final EllipticCurve ellipticCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        super(ellipticCurve, ecPoint, bigInteger, bigInteger2.intValue());
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final org.spongycastle.math.ec.ECPoint ecPoint, final BigInteger bigInteger) {
        super(convertCurve(ecCurve, null), convertPoint(ecPoint), bigInteger, 1);
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final org.spongycastle.math.ec.ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        super(convertCurve(ecCurve, null), convertPoint(ecPoint), bigInteger, bigInteger2.intValue());
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final org.spongycastle.math.ec.ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2, final byte[] array) {
        super(convertCurve(ecCurve, array), convertPoint(ecPoint), bigInteger, bigInteger2.intValue());
        this.name = name;
    }
    
    private static EllipticCurve convertCurve(final ECCurve ecCurve, final byte[] array) {
        return new EllipticCurve(convertField(ecCurve.getField()), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), array);
    }
    
    private static ECField convertField(final FiniteField finiteField) {
        if (ECAlgorithms.isFpField(finiteField)) {
            return new ECFieldFp(finiteField.getCharacteristic());
        }
        final Polynomial minimalPolynomial = ((PolynomialExtensionField)finiteField).getMinimalPolynomial();
        final int[] exponentsPresent = minimalPolynomial.getExponentsPresent();
        return new ECFieldF2m(minimalPolynomial.getDegree(), Arrays.reverse(Arrays.copyOfRange(exponentsPresent, 1, exponentsPresent.length - 1)));
    }
    
    private static ECPoint convertPoint(org.spongycastle.math.ec.ECPoint normalize) {
        normalize = normalize.normalize();
        return new ECPoint(normalize.getAffineXCoord().toBigInteger(), normalize.getAffineYCoord().toBigInteger());
    }
    
    public String getName() {
        return this.name;
    }
}
