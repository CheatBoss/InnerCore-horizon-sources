package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SecP521R1Point extends AbstractFp
{
    public SecP521R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecP521R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    SecP521R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        if (this == ecPoint) {
            return this.twice();
        }
        final ECCurve curve = this.getCurve();
        final SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement)this.x;
        final SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement)this.y;
        final SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement)ecPoint.getXCoord();
        final SecP521R1FieldElement secP521R1FieldElement4 = (SecP521R1FieldElement)ecPoint.getYCoord();
        final SecP521R1FieldElement secP521R1FieldElement5 = (SecP521R1FieldElement)this.zs[0];
        final SecP521R1FieldElement secP521R1FieldElement6 = (SecP521R1FieldElement)ecPoint.getZCoord(0);
        final int[] create = Nat.create(17);
        final int[] create2 = Nat.create(17);
        final int[] create3 = Nat.create(17);
        final int[] create4 = Nat.create(17);
        final boolean one = secP521R1FieldElement5.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = secP521R1FieldElement3.x;
            x2 = secP521R1FieldElement4.x;
        }
        else {
            SecP521R1Field.square(secP521R1FieldElement5.x, create3);
            SecP521R1Field.multiply(create3, secP521R1FieldElement3.x, create2);
            SecP521R1Field.multiply(create3, secP521R1FieldElement5.x, create3);
            SecP521R1Field.multiply(create3, secP521R1FieldElement4.x, create3);
            x = create2;
            x2 = create3;
        }
        final boolean one2 = secP521R1FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = secP521R1FieldElement.x;
            x4 = secP521R1FieldElement2.x;
        }
        else {
            SecP521R1Field.square(secP521R1FieldElement6.x, create4);
            SecP521R1Field.multiply(create4, secP521R1FieldElement.x, create);
            SecP521R1Field.multiply(create4, secP521R1FieldElement6.x, create4);
            SecP521R1Field.multiply(create4, secP521R1FieldElement2.x, create4);
            x3 = create;
            x4 = create4;
        }
        final int[] create5 = Nat.create(17);
        SecP521R1Field.subtract(x3, x, create5);
        SecP521R1Field.subtract(x4, x2, create2);
        if (!Nat.isZero(17, create5)) {
            SecP521R1Field.square(create5, create3);
            final int[] create6 = Nat.create(17);
            SecP521R1Field.multiply(create3, create5, create6);
            SecP521R1Field.multiply(create3, x3, create3);
            SecP521R1Field.multiply(x4, create6, create);
            final SecP521R1FieldElement secP521R1FieldElement7 = new SecP521R1FieldElement(create4);
            SecP521R1Field.square(create2, secP521R1FieldElement7.x);
            SecP521R1Field.add(secP521R1FieldElement7.x, create6, secP521R1FieldElement7.x);
            SecP521R1Field.subtract(secP521R1FieldElement7.x, create3, secP521R1FieldElement7.x);
            SecP521R1Field.subtract(secP521R1FieldElement7.x, create3, secP521R1FieldElement7.x);
            final SecP521R1FieldElement secP521R1FieldElement8 = new SecP521R1FieldElement(create6);
            SecP521R1Field.subtract(create3, secP521R1FieldElement7.x, secP521R1FieldElement8.x);
            SecP521R1Field.multiply(secP521R1FieldElement8.x, create2, create2);
            SecP521R1Field.subtract(create2, create, secP521R1FieldElement8.x);
            final SecP521R1FieldElement secP521R1FieldElement9 = new SecP521R1FieldElement(create5);
            if (!one) {
                SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement5.x, secP521R1FieldElement9.x);
            }
            if (!one2) {
                SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement6.x, secP521R1FieldElement9.x);
            }
            return new SecP521R1Point(curve, secP521R1FieldElement7, secP521R1FieldElement8, new ECFieldElement[] { secP521R1FieldElement9 }, this.withCompression);
        }
        if (Nat.isZero(17, create2)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    @Override
    protected ECPoint detach() {
        return new SecP521R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    protected ECFieldElement doubleProductFromSquares(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3, final ECFieldElement ecFieldElement4) {
        return ecFieldElement.add(ecFieldElement2).square().subtract(ecFieldElement3).subtract(ecFieldElement4);
    }
    
    protected ECFieldElement eight(final ECFieldElement ecFieldElement) {
        return this.four(this.two(ecFieldElement));
    }
    
    protected ECFieldElement four(final ECFieldElement ecFieldElement) {
        return this.two(this.two(ecFieldElement));
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }
    
    protected ECFieldElement three(final ECFieldElement ecFieldElement) {
        return this.two(ecFieldElement).add(ecFieldElement);
    }
    
    @Override
    public ECPoint threeTimes() {
        if (this.isInfinity()) {
            return this;
        }
        if (this.y.isZero()) {
            return this;
        }
        return this.twice().add(this);
    }
    
    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        final SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement)this.y;
        if (secP521R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement)this.x;
        final SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement)this.zs[0];
        final int[] create = Nat.create(17);
        final int[] create2 = Nat.create(17);
        final int[] create3 = Nat.create(17);
        SecP521R1Field.square(secP521R1FieldElement.x, create3);
        final int[] create4 = Nat.create(17);
        SecP521R1Field.square(create3, create4);
        final boolean one = secP521R1FieldElement3.isOne();
        int[] x = secP521R1FieldElement3.x;
        if (!one) {
            SecP521R1Field.square(secP521R1FieldElement3.x, create2);
            x = create2;
        }
        SecP521R1Field.subtract(secP521R1FieldElement2.x, x, create);
        SecP521R1Field.add(secP521R1FieldElement2.x, x, create2);
        SecP521R1Field.multiply(create2, create, create2);
        Nat.addBothTo(17, create2, create2, create2);
        SecP521R1Field.reduce23(create2);
        SecP521R1Field.multiply(create3, secP521R1FieldElement2.x, create3);
        Nat.shiftUpBits(17, create3, 2, 0);
        SecP521R1Field.reduce23(create3);
        Nat.shiftUpBits(17, create4, 3, 0, create);
        SecP521R1Field.reduce23(create);
        final SecP521R1FieldElement secP521R1FieldElement4 = new SecP521R1FieldElement(create4);
        SecP521R1Field.square(create2, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, create3, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, create3, secP521R1FieldElement4.x);
        final SecP521R1FieldElement secP521R1FieldElement5 = new SecP521R1FieldElement(create3);
        SecP521R1Field.subtract(create3, secP521R1FieldElement4.x, secP521R1FieldElement5.x);
        SecP521R1Field.multiply(secP521R1FieldElement5.x, create2, secP521R1FieldElement5.x);
        SecP521R1Field.subtract(secP521R1FieldElement5.x, create, secP521R1FieldElement5.x);
        final SecP521R1FieldElement secP521R1FieldElement6 = new SecP521R1FieldElement(create2);
        SecP521R1Field.twice(secP521R1FieldElement.x, secP521R1FieldElement6.x);
        if (!one) {
            SecP521R1Field.multiply(secP521R1FieldElement6.x, secP521R1FieldElement3.x, secP521R1FieldElement6.x);
        }
        return new SecP521R1Point(curve, secP521R1FieldElement4, secP521R1FieldElement5, new ECFieldElement[] { secP521R1FieldElement6 }, this.withCompression);
    }
    
    @Override
    public ECPoint twicePlus(final ECPoint ecPoint) {
        if (this == ecPoint) {
            return this.threeTimes();
        }
        if (this.isInfinity()) {
            return ecPoint;
        }
        if (ecPoint.isInfinity()) {
            return this.twice();
        }
        if (this.y.isZero()) {
            return ecPoint;
        }
        return this.twice().add(ecPoint);
    }
    
    protected ECFieldElement two(final ECFieldElement ecFieldElement) {
        return ecFieldElement.add(ecFieldElement);
    }
}
