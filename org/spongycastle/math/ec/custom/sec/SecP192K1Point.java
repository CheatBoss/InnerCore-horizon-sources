package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SecP192K1Point extends AbstractFp
{
    public SecP192K1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecP192K1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    SecP192K1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        final SecP192K1FieldElement secP192K1FieldElement = (SecP192K1FieldElement)this.x;
        final SecP192K1FieldElement secP192K1FieldElement2 = (SecP192K1FieldElement)this.y;
        final SecP192K1FieldElement secP192K1FieldElement3 = (SecP192K1FieldElement)ecPoint.getXCoord();
        final SecP192K1FieldElement secP192K1FieldElement4 = (SecP192K1FieldElement)ecPoint.getYCoord();
        final SecP192K1FieldElement secP192K1FieldElement5 = (SecP192K1FieldElement)this.zs[0];
        final SecP192K1FieldElement secP192K1FieldElement6 = (SecP192K1FieldElement)ecPoint.getZCoord(0);
        final int[] ext = Nat192.createExt();
        final int[] create = Nat192.create();
        final int[] create2 = Nat192.create();
        final int[] create3 = Nat192.create();
        final boolean one = secP192K1FieldElement5.isOne();
        int[] x;
        int[] x2;
        if (one) {
            x = secP192K1FieldElement3.x;
            x2 = secP192K1FieldElement4.x;
        }
        else {
            SecP192K1Field.square(secP192K1FieldElement5.x, create2);
            SecP192K1Field.multiply(create2, secP192K1FieldElement3.x, create);
            SecP192K1Field.multiply(create2, secP192K1FieldElement5.x, create2);
            SecP192K1Field.multiply(create2, secP192K1FieldElement4.x, create2);
            x = create;
            x2 = create2;
        }
        final boolean one2 = secP192K1FieldElement6.isOne();
        int[] x3;
        int[] x4;
        if (one2) {
            x3 = secP192K1FieldElement.x;
            x4 = secP192K1FieldElement2.x;
        }
        else {
            SecP192K1Field.square(secP192K1FieldElement6.x, create3);
            SecP192K1Field.multiply(create3, secP192K1FieldElement.x, ext);
            SecP192K1Field.multiply(create3, secP192K1FieldElement6.x, create3);
            SecP192K1Field.multiply(create3, secP192K1FieldElement2.x, create3);
            x3 = ext;
            x4 = create3;
        }
        final int[] create4 = Nat192.create();
        SecP192K1Field.subtract(x3, x, create4);
        SecP192K1Field.subtract(x4, x2, create);
        if (!Nat192.isZero(create4)) {
            SecP192K1Field.square(create4, create2);
            final int[] create5 = Nat192.create();
            SecP192K1Field.multiply(create2, create4, create5);
            SecP192K1Field.multiply(create2, x3, create2);
            SecP192K1Field.negate(create5, create5);
            Nat192.mul(x4, create5, ext);
            SecP192K1Field.reduce32(Nat192.addBothTo(create2, create2, create5), create5);
            final SecP192K1FieldElement secP192K1FieldElement7 = new SecP192K1FieldElement(create3);
            SecP192K1Field.square(create, secP192K1FieldElement7.x);
            SecP192K1Field.subtract(secP192K1FieldElement7.x, create5, secP192K1FieldElement7.x);
            final SecP192K1FieldElement secP192K1FieldElement8 = new SecP192K1FieldElement(create5);
            SecP192K1Field.subtract(create2, secP192K1FieldElement7.x, secP192K1FieldElement8.x);
            SecP192K1Field.multiplyAddToExt(secP192K1FieldElement8.x, create, ext);
            SecP192K1Field.reduce(ext, secP192K1FieldElement8.x);
            final SecP192K1FieldElement secP192K1FieldElement9 = new SecP192K1FieldElement(create4);
            if (!one) {
                SecP192K1Field.multiply(secP192K1FieldElement9.x, secP192K1FieldElement5.x, secP192K1FieldElement9.x);
            }
            if (!one2) {
                SecP192K1Field.multiply(secP192K1FieldElement9.x, secP192K1FieldElement6.x, secP192K1FieldElement9.x);
            }
            return new SecP192K1Point(curve, secP192K1FieldElement7, secP192K1FieldElement8, new ECFieldElement[] { secP192K1FieldElement9 }, this.withCompression);
        }
        if (Nat192.isZero(create)) {
            return this.twice();
        }
        return curve.getInfinity();
    }
    
    @Override
    protected ECPoint detach() {
        return new SecP192K1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }
    
    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP192K1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
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
        final SecP192K1FieldElement secP192K1FieldElement = (SecP192K1FieldElement)this.y;
        if (secP192K1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SecP192K1FieldElement secP192K1FieldElement2 = (SecP192K1FieldElement)this.x;
        final SecP192K1FieldElement secP192K1FieldElement3 = (SecP192K1FieldElement)this.zs[0];
        final int[] create = Nat192.create();
        SecP192K1Field.square(secP192K1FieldElement.x, create);
        final int[] create2 = Nat192.create();
        SecP192K1Field.square(create, create2);
        final int[] create3 = Nat192.create();
        SecP192K1Field.square(secP192K1FieldElement2.x, create3);
        SecP192K1Field.reduce32(Nat192.addBothTo(create3, create3, create3), create3);
        SecP192K1Field.multiply(create, secP192K1FieldElement2.x, create);
        SecP192K1Field.reduce32(Nat.shiftUpBits(6, create, 2, 0), create);
        final int[] create4 = Nat192.create();
        SecP192K1Field.reduce32(Nat.shiftUpBits(6, create2, 3, 0, create4), create4);
        final SecP192K1FieldElement secP192K1FieldElement4 = new SecP192K1FieldElement(create2);
        SecP192K1Field.square(create3, secP192K1FieldElement4.x);
        SecP192K1Field.subtract(secP192K1FieldElement4.x, create, secP192K1FieldElement4.x);
        SecP192K1Field.subtract(secP192K1FieldElement4.x, create, secP192K1FieldElement4.x);
        final SecP192K1FieldElement secP192K1FieldElement5 = new SecP192K1FieldElement(create);
        SecP192K1Field.subtract(create, secP192K1FieldElement4.x, secP192K1FieldElement5.x);
        SecP192K1Field.multiply(secP192K1FieldElement5.x, create3, secP192K1FieldElement5.x);
        SecP192K1Field.subtract(secP192K1FieldElement5.x, create4, secP192K1FieldElement5.x);
        final SecP192K1FieldElement secP192K1FieldElement6 = new SecP192K1FieldElement(create3);
        SecP192K1Field.twice(secP192K1FieldElement.x, secP192K1FieldElement6.x);
        if (!secP192K1FieldElement3.isOne()) {
            SecP192K1Field.multiply(secP192K1FieldElement6.x, secP192K1FieldElement3.x, secP192K1FieldElement6.x);
        }
        return new SecP192K1Point(curve, secP192K1FieldElement4, secP192K1FieldElement5, new ECFieldElement[] { secP192K1FieldElement6 }, this.withCompression);
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
