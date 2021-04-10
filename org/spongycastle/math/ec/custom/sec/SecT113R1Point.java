package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;

public class SecT113R1Point extends AbstractF2m
{
    public SecT113R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecT113R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
        super(ecCurve, ecFieldElement, ecFieldElement2);
        int n = false ? 1 : 0;
        final boolean b = ecFieldElement == null;
        if (ecFieldElement2 == null) {
            n = (true ? 1 : 0);
        }
        if ((b ? 1 : 0) == n) {
            this.withCompression = withCompression;
            return;
        }
        throw new IllegalArgumentException("Exactly one of the field elements is null");
    }
    
    SecT113R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
        super(ecCurve, ecFieldElement, ecFieldElement2, array);
        this.withCompression = withCompression;
    }
    
    @Override
    public ECPoint add(final ECPoint ecPoint) {
        if (this.isInfinity()) {
            return ecPoint;
        }
        if (ecPoint.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        ECFieldElement ecFieldElement = this.x;
        final ECFieldElement rawXCoord = ecPoint.getRawXCoord();
        if (ecFieldElement.isZero()) {
            if (rawXCoord.isZero()) {
                return curve.getInfinity();
            }
            return ecPoint.add(this);
        }
        else {
            final ECFieldElement y = this.y;
            final ECFieldElement ecFieldElement2 = this.zs[0];
            final ECFieldElement rawYCoord = ecPoint.getRawYCoord();
            final ECFieldElement zCoord = ecPoint.getZCoord(0);
            final boolean one = ecFieldElement2.isOne();
            ECFieldElement multiply;
            ECFieldElement multiply2;
            if (!one) {
                multiply = rawXCoord.multiply(ecFieldElement2);
                multiply2 = rawYCoord.multiply(ecFieldElement2);
            }
            else {
                multiply = rawXCoord;
                multiply2 = rawYCoord;
            }
            final boolean one2 = zCoord.isOne();
            ECFieldElement multiply3;
            if (!one2) {
                ecFieldElement = ecFieldElement.multiply(zCoord);
                multiply3 = y.multiply(zCoord);
            }
            else {
                multiply3 = y;
            }
            final ECFieldElement add = multiply3.add(multiply2);
            final ECFieldElement add2 = ecFieldElement.add(multiply);
            if (!add2.isZero()) {
                ECFieldElement ecFieldElement3;
                ECFieldElement fromBigInteger;
                ECFieldElement ecFieldElement4;
                if (rawXCoord.isZero()) {
                    final ECPoint normalize = this.normalize();
                    final ECFieldElement xCoord = normalize.getXCoord();
                    final ECFieldElement yCoord = normalize.getYCoord();
                    final ECFieldElement divide = yCoord.add(rawYCoord).divide(xCoord);
                    ecFieldElement3 = divide.square().add(divide).add(xCoord).add(curve.getA());
                    if (ecFieldElement3.isZero()) {
                        return new SecT113R1Point(curve, ecFieldElement3, curve.getB().sqrt(), this.withCompression);
                    }
                    final ECFieldElement add3 = divide.multiply(xCoord.add(ecFieldElement3)).add(ecFieldElement3).add(yCoord).divide(ecFieldElement3).add(ecFieldElement3);
                    fromBigInteger = curve.fromBigInteger(ECConstants.ONE);
                    ecFieldElement4 = add3;
                }
                else {
                    final ECFieldElement square = add2.square();
                    final ECFieldElement multiply4 = add.multiply(ecFieldElement);
                    final ECFieldElement multiply5 = add.multiply(multiply);
                    ecFieldElement3 = multiply4.multiply(multiply5);
                    if (ecFieldElement3.isZero()) {
                        return new SecT113R1Point(curve, ecFieldElement3, curve.getB().sqrt(), this.withCompression);
                    }
                    ECFieldElement ecFieldElement6;
                    final ECFieldElement ecFieldElement5 = ecFieldElement6 = add.multiply(square);
                    if (!one2) {
                        ecFieldElement6 = ecFieldElement5.multiply(zCoord);
                    }
                    final ECFieldElement squarePlusProduct = multiply5.add(square).squarePlusProduct(ecFieldElement6, y.add(ecFieldElement2));
                    if (!one) {
                        ecFieldElement6 = ecFieldElement6.multiply(ecFieldElement2);
                    }
                    ecFieldElement4 = squarePlusProduct;
                    fromBigInteger = ecFieldElement6;
                }
                return new SecT113R1Point(curve, ecFieldElement3, ecFieldElement4, new ECFieldElement[] { fromBigInteger }, this.withCompression);
            }
            if (add.isZero()) {
                return this.twice();
            }
            return curve.getInfinity();
        }
    }
    
    @Override
    protected ECPoint detach() {
        return new SecT113R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    @Override
    protected boolean getCompressionYTilde() {
        final ECFieldElement rawXCoord = this.getRawXCoord();
        final boolean zero = rawXCoord.isZero();
        boolean b = false;
        if (zero) {
            return false;
        }
        if (this.getRawYCoord().testBitZero() != rawXCoord.testBitZero()) {
            b = true;
        }
        return b;
    }
    
    @Override
    public ECFieldElement getYCoord() {
        final ECFieldElement x = this.x;
        final ECFieldElement y = this.y;
        if (this.isInfinity()) {
            return y;
        }
        if (x.isZero()) {
            return y;
        }
        final ECFieldElement multiply = y.add(x).multiply(x);
        final ECFieldElement ecFieldElement = this.zs[0];
        ECFieldElement divide = multiply;
        if (!ecFieldElement.isOne()) {
            divide = multiply.divide(ecFieldElement);
        }
        return divide;
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        final ECFieldElement x = this.x;
        if (x.isZero()) {
            return this;
        }
        final ECFieldElement y = this.y;
        final ECFieldElement ecFieldElement = this.zs[0];
        return new SecT113R1Point(this.curve, x, y.add(ecFieldElement), new ECFieldElement[] { ecFieldElement }, this.withCompression);
    }
    
    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        final ECFieldElement x = this.x;
        if (x.isZero()) {
            return curve.getInfinity();
        }
        final ECFieldElement y = this.y;
        final ECFieldElement ecFieldElement = this.zs[0];
        final boolean one = ecFieldElement.isOne();
        ECFieldElement multiply;
        if (one) {
            multiply = y;
        }
        else {
            multiply = y.multiply(ecFieldElement);
        }
        ECFieldElement square;
        if (one) {
            square = ecFieldElement;
        }
        else {
            square = ecFieldElement.square();
        }
        ECFieldElement ecFieldElement2 = curve.getA();
        if (!one) {
            ecFieldElement2 = ecFieldElement2.multiply(square);
        }
        final ECFieldElement add = y.square().add(multiply).add(ecFieldElement2);
        if (add.isZero()) {
            return new SecT113R1Point(curve, add, curve.getB().sqrt(), this.withCompression);
        }
        final ECFieldElement square2 = add.square();
        ECFieldElement multiply2;
        if (one) {
            multiply2 = add;
        }
        else {
            multiply2 = add.multiply(square);
        }
        ECFieldElement multiply3;
        if (one) {
            multiply3 = x;
        }
        else {
            multiply3 = x.multiply(ecFieldElement);
        }
        return new SecT113R1Point(curve, square2, multiply3.squarePlusProduct(add, multiply).add(square2).add(multiply2), new ECFieldElement[] { multiply2 }, this.withCompression);
    }
    
    @Override
    public ECPoint twicePlus(final ECPoint ecPoint) {
        if (this.isInfinity()) {
            return ecPoint;
        }
        if (ecPoint.isInfinity()) {
            return this.twice();
        }
        final ECCurve curve = this.getCurve();
        final ECFieldElement x = this.x;
        if (x.isZero()) {
            return ecPoint;
        }
        final ECFieldElement rawXCoord = ecPoint.getRawXCoord();
        final ECFieldElement zCoord = ecPoint.getZCoord(0);
        if (rawXCoord.isZero() || !zCoord.isOne()) {
            return this.twice().add(ecPoint);
        }
        final ECFieldElement y = this.y;
        final ECFieldElement ecFieldElement = this.zs[0];
        final ECFieldElement rawYCoord = ecPoint.getRawYCoord();
        final ECFieldElement square = x.square();
        final ECFieldElement square2 = y.square();
        final ECFieldElement square3 = ecFieldElement.square();
        final ECFieldElement add = curve.getA().multiply(square3).add(square2).add(y.multiply(ecFieldElement));
        final ECFieldElement addOne = rawYCoord.addOne();
        final ECFieldElement multiplyPlusProduct = curve.getA().add(addOne).multiply(square3).add(square2).multiplyPlusProduct(add, square, square3);
        final ECFieldElement multiply = rawXCoord.multiply(square3);
        final ECFieldElement square4 = multiply.add(add).square();
        if (square4.isZero()) {
            if (multiplyPlusProduct.isZero()) {
                return ecPoint.twice();
            }
            return curve.getInfinity();
        }
        else {
            if (multiplyPlusProduct.isZero()) {
                return new SecT113R1Point(curve, multiplyPlusProduct, curve.getB().sqrt(), this.withCompression);
            }
            final ECFieldElement multiply2 = multiplyPlusProduct.square().multiply(multiply);
            final ECFieldElement multiply3 = multiplyPlusProduct.multiply(square4).multiply(square3);
            return new SecT113R1Point(curve, multiply2, multiplyPlusProduct.add(square4).square().multiplyPlusProduct(add, addOne, multiply3), new ECFieldElement[] { multiply3 }, this.withCompression);
        }
    }
}
