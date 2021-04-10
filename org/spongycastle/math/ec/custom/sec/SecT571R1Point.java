package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;

public class SecT571R1Point extends AbstractF2m
{
    public SecT571R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        this(ecCurve, ecFieldElement, ecFieldElement2, false);
    }
    
    public SecT571R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean withCompression) {
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
    
    SecT571R1Point(final ECCurve ecCurve, final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean withCompression) {
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
        final SecT571FieldElement secT571FieldElement = (SecT571FieldElement)this.x;
        final SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)ecPoint.getRawXCoord();
        if (secT571FieldElement.isZero()) {
            if (secT571FieldElement2.isZero()) {
                return curve.getInfinity();
            }
            return ecPoint.add(this);
        }
        else {
            final SecT571FieldElement secT571FieldElement3 = (SecT571FieldElement)this.y;
            final SecT571FieldElement secT571FieldElement4 = (SecT571FieldElement)this.zs[0];
            final SecT571FieldElement secT571FieldElement5 = (SecT571FieldElement)ecPoint.getRawYCoord();
            final SecT571FieldElement secT571FieldElement6 = (SecT571FieldElement)ecPoint.getZCoord(0);
            final long[] create64 = Nat576.create64();
            final long[] create65 = Nat576.create64();
            final long[] create66 = Nat576.create64();
            final long[] create67 = Nat576.create64();
            long[] precompMultiplicand;
            if (secT571FieldElement4.isOne()) {
                precompMultiplicand = null;
            }
            else {
                precompMultiplicand = SecT571Field.precompMultiplicand(secT571FieldElement4.x);
            }
            long[] x;
            long[] x2;
            if (precompMultiplicand == null) {
                x = secT571FieldElement2.x;
                x2 = secT571FieldElement5.x;
            }
            else {
                SecT571Field.multiplyPrecomp(secT571FieldElement2.x, precompMultiplicand, create65);
                SecT571Field.multiplyPrecomp(secT571FieldElement5.x, precompMultiplicand, create67);
                x = create65;
                x2 = create67;
            }
            long[] precompMultiplicand2;
            if (secT571FieldElement6.isOne()) {
                precompMultiplicand2 = null;
            }
            else {
                precompMultiplicand2 = SecT571Field.precompMultiplicand(secT571FieldElement6.x);
            }
            long[] x3;
            long[] x4;
            if (precompMultiplicand2 == null) {
                x3 = secT571FieldElement.x;
                x4 = secT571FieldElement3.x;
            }
            else {
                SecT571Field.multiplyPrecomp(secT571FieldElement.x, precompMultiplicand2, create64);
                SecT571Field.multiplyPrecomp(secT571FieldElement3.x, precompMultiplicand2, create66);
                x3 = create64;
                x4 = create66;
            }
            SecT571Field.add(x4, x2, create66);
            SecT571Field.add(x3, x, create67);
            if (!Nat576.isZero64(create67)) {
                SecT571FieldElement secT571FieldElement8;
                SecT571FieldElement secT571FieldElement10;
                SecT571FieldElement secT571FieldElement11;
                if (secT571FieldElement2.isZero()) {
                    final ECPoint normalize = this.normalize();
                    final SecT571FieldElement secT571FieldElement7 = (SecT571FieldElement)normalize.getXCoord();
                    final ECFieldElement yCoord = normalize.getYCoord();
                    final ECFieldElement divide = yCoord.add(secT571FieldElement5).divide(secT571FieldElement7);
                    secT571FieldElement8 = (SecT571FieldElement)divide.square().add(divide).add(secT571FieldElement7).addOne();
                    if (secT571FieldElement8.isZero()) {
                        return new SecT571R1Point(curve, secT571FieldElement8, SecT571R1Curve.SecT571R1_B_SQRT, this.withCompression);
                    }
                    final SecT571FieldElement secT571FieldElement9 = (SecT571FieldElement)divide.multiply(secT571FieldElement7.add(secT571FieldElement8)).add(secT571FieldElement8).add(yCoord).divide(secT571FieldElement8).add(secT571FieldElement8);
                    secT571FieldElement10 = (SecT571FieldElement)curve.fromBigInteger(ECConstants.ONE);
                    secT571FieldElement11 = secT571FieldElement9;
                }
                else {
                    SecT571Field.square(create67, create67);
                    final long[] precompMultiplicand3 = SecT571Field.precompMultiplicand(create66);
                    SecT571Field.multiplyPrecomp(x3, precompMultiplicand3, create64);
                    SecT571Field.multiplyPrecomp(x, precompMultiplicand3, create65);
                    final SecT571FieldElement secT571FieldElement12 = new SecT571FieldElement(create64);
                    SecT571Field.multiply(create64, create65, secT571FieldElement12.x);
                    if (secT571FieldElement12.isZero()) {
                        return new SecT571R1Point(curve, secT571FieldElement12, SecT571R1Curve.SecT571R1_B_SQRT, this.withCompression);
                    }
                    final SecT571FieldElement secT571FieldElement13 = new SecT571FieldElement(create66);
                    SecT571Field.multiplyPrecomp(create67, precompMultiplicand3, secT571FieldElement13.x);
                    if (precompMultiplicand2 != null) {
                        SecT571Field.multiplyPrecomp(secT571FieldElement13.x, precompMultiplicand2, secT571FieldElement13.x);
                    }
                    final long[] ext64 = Nat576.createExt64();
                    SecT571Field.add(create65, create67, create67);
                    SecT571Field.squareAddToExt(create67, ext64);
                    SecT571Field.add(secT571FieldElement3.x, secT571FieldElement4.x, create67);
                    SecT571Field.multiplyAddToExt(create67, secT571FieldElement13.x, ext64);
                    secT571FieldElement11 = new SecT571FieldElement(create67);
                    SecT571Field.reduce(ext64, secT571FieldElement11.x);
                    if (precompMultiplicand != null) {
                        SecT571Field.multiplyPrecomp(secT571FieldElement13.x, precompMultiplicand, secT571FieldElement13.x);
                    }
                    final SecT571FieldElement secT571FieldElement14 = secT571FieldElement12;
                    secT571FieldElement10 = secT571FieldElement13;
                    secT571FieldElement8 = secT571FieldElement14;
                }
                return new SecT571R1Point(curve, secT571FieldElement8, secT571FieldElement11, new ECFieldElement[] { secT571FieldElement10 }, this.withCompression);
            }
            if (Nat576.isZero64(create66)) {
                return this.twice();
            }
            return curve.getInfinity();
        }
    }
    
    @Override
    protected ECPoint detach() {
        return new SecT571R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
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
        return new SecT571R1Point(this.curve, x, y.add(ecFieldElement), new ECFieldElement[] { ecFieldElement }, this.withCompression);
    }
    
    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        final ECCurve curve = this.getCurve();
        final SecT571FieldElement secT571FieldElement = (SecT571FieldElement)this.x;
        if (secT571FieldElement.isZero()) {
            return curve.getInfinity();
        }
        final SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)this.y;
        final SecT571FieldElement secT571FieldElement3 = (SecT571FieldElement)this.zs[0];
        final long[] create64 = Nat576.create64();
        final long[] create65 = Nat576.create64();
        long[] precompMultiplicand;
        if (secT571FieldElement3.isOne()) {
            precompMultiplicand = null;
        }
        else {
            precompMultiplicand = SecT571Field.precompMultiplicand(secT571FieldElement3.x);
        }
        long[] x;
        long[] x2;
        if (precompMultiplicand == null) {
            x = secT571FieldElement2.x;
            x2 = secT571FieldElement3.x;
        }
        else {
            SecT571Field.multiplyPrecomp(secT571FieldElement2.x, precompMultiplicand, create64);
            SecT571Field.square(secT571FieldElement3.x, create65);
            x = create64;
            x2 = create65;
        }
        final long[] create66 = Nat576.create64();
        SecT571Field.square(secT571FieldElement2.x, create66);
        SecT571Field.addBothTo(x, x2, create66);
        if (Nat576.isZero64(create66)) {
            return new SecT571R1Point(curve, new SecT571FieldElement(create66), SecT571R1Curve.SecT571R1_B_SQRT, this.withCompression);
        }
        final long[] ext64 = Nat576.createExt64();
        SecT571Field.multiplyAddToExt(create66, x, ext64);
        final SecT571FieldElement secT571FieldElement4 = new SecT571FieldElement(create64);
        SecT571Field.square(create66, secT571FieldElement4.x);
        final SecT571FieldElement secT571FieldElement5 = new SecT571FieldElement(create66);
        if (precompMultiplicand != null) {
            SecT571Field.multiply(secT571FieldElement5.x, x2, secT571FieldElement5.x);
        }
        long[] x3;
        if (precompMultiplicand == null) {
            x3 = secT571FieldElement.x;
        }
        else {
            SecT571Field.multiplyPrecomp(secT571FieldElement.x, precompMultiplicand, create65);
            x3 = create65;
        }
        SecT571Field.squareAddToExt(x3, ext64);
        SecT571Field.reduce(ext64, create65);
        SecT571Field.addBothTo(secT571FieldElement4.x, secT571FieldElement5.x, create65);
        return new SecT571R1Point(curve, secT571FieldElement4, new SecT571FieldElement(create65), new ECFieldElement[] { secT571FieldElement5 }, this.withCompression);
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
        final SecT571FieldElement secT571FieldElement = (SecT571FieldElement)this.x;
        if (secT571FieldElement.isZero()) {
            return ecPoint;
        }
        final SecT571FieldElement secT571FieldElement2 = (SecT571FieldElement)ecPoint.getRawXCoord();
        final SecT571FieldElement secT571FieldElement3 = (SecT571FieldElement)ecPoint.getZCoord(0);
        if (secT571FieldElement2.isZero() || !secT571FieldElement3.isOne()) {
            return this.twice().add(ecPoint);
        }
        final SecT571FieldElement secT571FieldElement4 = (SecT571FieldElement)this.y;
        final SecT571FieldElement secT571FieldElement5 = (SecT571FieldElement)this.zs[0];
        final SecT571FieldElement secT571FieldElement6 = (SecT571FieldElement)ecPoint.getRawYCoord();
        final long[] create64 = Nat576.create64();
        final long[] create65 = Nat576.create64();
        final long[] create66 = Nat576.create64();
        final long[] create67 = Nat576.create64();
        SecT571Field.square(secT571FieldElement.x, create64);
        SecT571Field.square(secT571FieldElement4.x, create65);
        SecT571Field.square(secT571FieldElement5.x, create66);
        SecT571Field.multiply(secT571FieldElement4.x, secT571FieldElement5.x, create67);
        SecT571Field.addBothTo(create66, create65, create67);
        final long[] precompMultiplicand = SecT571Field.precompMultiplicand(create66);
        SecT571Field.multiplyPrecomp(secT571FieldElement6.x, precompMultiplicand, create66);
        SecT571Field.add(create66, create65, create66);
        final long[] ext64 = Nat576.createExt64();
        SecT571Field.multiplyAddToExt(create66, create67, ext64);
        SecT571Field.multiplyPrecompAddToExt(create64, precompMultiplicand, ext64);
        SecT571Field.reduce(ext64, create66);
        SecT571Field.multiplyPrecomp(secT571FieldElement2.x, precompMultiplicand, create64);
        SecT571Field.add(create64, create67, create65);
        SecT571Field.square(create65, create65);
        if (Nat576.isZero64(create65)) {
            if (Nat576.isZero64(create66)) {
                return ecPoint.twice();
            }
            return curve.getInfinity();
        }
        else {
            if (Nat576.isZero64(create66)) {
                return new SecT571R1Point(curve, new SecT571FieldElement(create66), SecT571R1Curve.SecT571R1_B_SQRT, this.withCompression);
            }
            final SecT571FieldElement secT571FieldElement7 = new SecT571FieldElement();
            SecT571Field.square(create66, secT571FieldElement7.x);
            SecT571Field.multiply(secT571FieldElement7.x, create64, secT571FieldElement7.x);
            final SecT571FieldElement secT571FieldElement8 = new SecT571FieldElement(create64);
            SecT571Field.multiply(create66, create65, secT571FieldElement8.x);
            SecT571Field.multiplyPrecomp(secT571FieldElement8.x, precompMultiplicand, secT571FieldElement8.x);
            final SecT571FieldElement secT571FieldElement9 = new SecT571FieldElement(create65);
            SecT571Field.add(create66, create65, secT571FieldElement9.x);
            SecT571Field.square(secT571FieldElement9.x, secT571FieldElement9.x);
            Nat.zero64(18, ext64);
            SecT571Field.multiplyAddToExt(secT571FieldElement9.x, create67, ext64);
            SecT571Field.addOne(secT571FieldElement6.x, create67);
            SecT571Field.multiplyAddToExt(create67, secT571FieldElement8.x, ext64);
            SecT571Field.reduce(ext64, secT571FieldElement9.x);
            return new SecT571R1Point(curve, secT571FieldElement7, secT571FieldElement9, new ECFieldElement[] { secT571FieldElement8 }, this.withCompression);
        }
    }
}
