package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SecP224R1Point extends AbstractFp
{
    public SecP224R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecP224R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    SecP224R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        final SecP224R1FieldElement secP224R1FieldElement = (SecP224R1FieldElement)this.x;
        final SecP224R1FieldElement secP224R1FieldElement2 = (SecP224R1FieldElement)this.y;
        final SecP224R1FieldElement secP224R1FieldElement3 = (SecP224R1FieldElement)ecPoint.getXCoord();
        final SecP224R1FieldElement secP224R1FieldElement4 = (SecP224R1FieldElement)ecPoint.getYCoord();
        final SecP224R1FieldElement secP224R1FieldElement5 = (SecP224R1FieldElement)this.zs[0];
        final SecP224R1FieldElement secP224R1FieldElement6 = (SecP224R1FieldElement)ecPoint.getZCoord(0);
        final int[] ext = Nat224.createExt();
        final int[] create = Nat224.create();
        final int[] create2 = Nat224.create();
        final int[] create3 = Nat224.create();
        final boolean one = secP224R1FieldElement5.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = secP224R1FieldElement3.x;
            x2 = secP224R1FieldElement4.x;
        }
        else {
            SecP224R1Field.square(secP224R1FieldElement5.x, create2);
            SecP224R1Field.multiply(create2, secP224R1FieldElement3.x, create);
            SecP224R1Field.multiply(create2, secP224R1FieldElement5.x, create2);
            SecP224R1Field.multiply(create2, secP224R1FieldElement4.x, create2);
            x = create;
            x2 = create2;
        }
        final boolean one2 = secP224R1FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = secP224R1FieldElement.x;
            x4 = secP224R1FieldElement2.x;
        }
        else {
            SecP224R1Field.square(secP224R1FieldElement6.x, create3);
            SecP224R1Field.multiply(create3, secP224R1FieldElement.x, ext);
            SecP224R1Field.multiply(create3, secP224R1FieldElement6.x, create3);
            SecP224R1Field.multiply(create3, secP224R1FieldElement2.x, create3);
            x3 = ext;
            x4 = create3;
        }
        final int[] create4 = Nat224.create();
        SecP224R1Field.subtract(x3, x, create4);
        SecP224R1Field.subtract(x4, x2, create);
        if (!Nat224.isZero(create4)) {
            SecP224R1Field.square(create4, create2);
            final int[] create5 = Nat224.create();
            SecP224R1Field.multiply(create2, create4, create5);
            SecP224R1Field.multiply(create2, x3, create2);
            SecP224R1Field.negate(create5, create5);
            Nat224.mul(x4, create5, ext);
            SecP224R1Field.reduce32(Nat224.addBothTo(create2, create2, create5), create5);
            final SecP224R1FieldElement secP224R1FieldElement7 = new SecP224R1FieldElement(create3);
            SecP224R1Field.square(create, secP224R1FieldElement7.x);
            SecP224R1Field.subtract(secP224R1FieldElement7.x, create5, secP224R1FieldElement7.x);
            final SecP224R1FieldElement secP224R1FieldElement8 = new SecP224R1FieldElement(create5);
            SecP224R1Field.subtract(create2, secP224R1FieldElement7.x, secP224R1FieldElement8.x);
            SecP224R1Field.multiplyAddToExt(secP224R1FieldElement8.x, create, ext);
            SecP224R1Field.reduce(ext, secP224R1FieldElement8.x);
            final SecP224R1FieldElement secP224R1FieldElement9 = new SecP224R1FieldElement(create4);
            if (!one) {
                SecP224R1Field.multiply(secP224R1FieldElement9.x, secP224R1FieldElement5.x, secP224R1FieldElement9.x);
            }
            if (!one2) {
                SecP224R1Field.multiply(secP224R1FieldElement9.x, secP224R1FieldElement6.x, secP224R1FieldElement9.x);
            }
            return new SecP224R1Point(curve, secP224R1FieldElement7, secP224R1FieldElement8, new ECFieldElement[] { secP224R1FieldElement9 }, this.withCompression);
        }
        if (Nat224.isZero(create)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    @Override
    protected ECPoint detach() {
        return new SecP224R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP224R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
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
        final SecP224R1FieldElement secP224R1FieldElement = (SecP224R1FieldElement)this.y;
        if (secP224R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SecP224R1FieldElement secP224R1FieldElement2 = (SecP224R1FieldElement)this.x;
        final SecP224R1FieldElement secP224R1FieldElement3 = (SecP224R1FieldElement)this.zs[0];
        final int[] create = Nat224.create();
        final int[] create2 = Nat224.create();
        final int[] create3 = Nat224.create();
        SecP224R1Field.square(secP224R1FieldElement.x, create3);
        final int[] create4 = Nat224.create();
        SecP224R1Field.square(create3, create4);
        final boolean one = secP224R1FieldElement3.isOne();
        int[] x = secP224R1FieldElement3.x;
        if (!one) {
            SecP224R1Field.square(secP224R1FieldElement3.x, create2);
            x = create2;
        }
        SecP224R1Field.subtract(secP224R1FieldElement2.x, x, create);
        SecP224R1Field.add(secP224R1FieldElement2.x, x, create2);
        SecP224R1Field.multiply(create2, create, create2);
        SecP224R1Field.reduce32(Nat224.addBothTo(create2, create2, create2), create2);
        SecP224R1Field.multiply(create3, secP224R1FieldElement2.x, create3);
        SecP224R1Field.reduce32(Nat.shiftUpBits(7, create3, 2, 0), create3);
        SecP224R1Field.reduce32(Nat.shiftUpBits(7, create4, 3, 0, create), create);
        final SecP224R1FieldElement secP224R1FieldElement4 = new SecP224R1FieldElement(create4);
        SecP224R1Field.square(create2, secP224R1FieldElement4.x);
        SecP224R1Field.subtract(secP224R1FieldElement4.x, create3, secP224R1FieldElement4.x);
        SecP224R1Field.subtract(secP224R1FieldElement4.x, create3, secP224R1FieldElement4.x);
        final SecP224R1FieldElement secP224R1FieldElement5 = new SecP224R1FieldElement(create3);
        SecP224R1Field.subtract(create3, secP224R1FieldElement4.x, secP224R1FieldElement5.x);
        SecP224R1Field.multiply(secP224R1FieldElement5.x, create2, secP224R1FieldElement5.x);
        SecP224R1Field.subtract(secP224R1FieldElement5.x, create, secP224R1FieldElement5.x);
        final SecP224R1FieldElement secP224R1FieldElement6 = new SecP224R1FieldElement(create2);
        SecP224R1Field.twice(secP224R1FieldElement.x, secP224R1FieldElement6.x);
        if (!one) {
            SecP224R1Field.multiply(secP224R1FieldElement6.x, secP224R1FieldElement3.x, secP224R1FieldElement6.x);
        }
        return new SecP224R1Point(curve, secP224R1FieldElement4, secP224R1FieldElement5, new ECFieldElement[] { secP224R1FieldElement6 }, this.withCompression);
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
}
