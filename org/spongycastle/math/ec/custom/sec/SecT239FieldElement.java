package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;
import java.math.*;
import org.spongycastle.util.*;

public class SecT239FieldElement extends ECFieldElement
{
    protected long[] x;
    
    public SecT239FieldElement() {
        this.x = Nat256.create64();
    }
    
    public SecT239FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= 239) {
            this.x = SecT239Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecT239FieldElement");
    }
    
    protected SecT239FieldElement(final long[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat256.create64();
        SecT239Field.add(this.x, ((SecT239FieldElement)ecFieldElement).x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement addOne() {
        final long[] create64 = Nat256.create64();
        SecT239Field.addOne(this.x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        return this.multiply(ecFieldElement.invert());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecT239FieldElement && Nat256.eq64(this.x, ((SecT239FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecT239Field";
    }
    
    @Override
    public int getFieldSize() {
        return 239;
    }
    
    public int getK1() {
        return 158;
    }
    
    public int getK2() {
        return 0;
    }
    
    public int getK3() {
        return 0;
    }
    
    public int getM() {
        return 239;
    }
    
    public int getRepresentation() {
        return 2;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.x, 0, 4) ^ 0x16CAFFE;
    }
    
    @Override
    public ECFieldElement invert() {
        final long[] create64 = Nat256.create64();
        SecT239Field.invert(this.x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public boolean isOne() {
        return Nat256.isOne64(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat256.isZero64(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat256.create64();
        SecT239Field.multiply(this.x, ((SecT239FieldElement)ecFieldElement).x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiplyPlusProduct(ecFieldElement, ecFieldElement2, ecFieldElement3);
    }
    
    @Override
    public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        final long[] x = this.x;
        final long[] x2 = ((SecT239FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT239FieldElement)ecFieldElement2).x;
        final long[] x4 = ((SecT239FieldElement)ecFieldElement3).x;
        final long[] ext64 = Nat256.createExt64();
        SecT239Field.multiplyAddToExt(x, x2, ext64);
        SecT239Field.multiplyAddToExt(x3, x4, ext64);
        final long[] create64 = Nat256.create64();
        SecT239Field.reduce(ext64, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement negate() {
        return this;
    }
    
    @Override
    public ECFieldElement sqrt() {
        final long[] create64 = Nat256.create64();
        SecT239Field.sqrt(this.x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement square() {
        final long[] create64 = Nat256.create64();
        SecT239Field.square(this.x, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.squarePlusProduct(ecFieldElement, ecFieldElement2);
    }
    
    @Override
    public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        final long[] x = this.x;
        final long[] x2 = ((SecT239FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT239FieldElement)ecFieldElement2).x;
        final long[] ext64 = Nat256.createExt64();
        SecT239Field.squareAddToExt(x, ext64);
        SecT239Field.multiplyAddToExt(x2, x3, ext64);
        final long[] create64 = Nat256.create64();
        SecT239Field.reduce(ext64, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squarePow(final int n) {
        if (n < 1) {
            return this;
        }
        final long[] create64 = Nat256.create64();
        SecT239Field.squareN(this.x, n, create64);
        return new SecT239FieldElement(create64);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        return this.add(ecFieldElement);
    }
    
    @Override
    public boolean testBitZero() {
        final long[] x = this.x;
        boolean b = false;
        if ((x[0] & 0x1L) != 0x0L) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat256.toBigInteger64(this.x);
    }
}
