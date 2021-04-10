package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.math.raw.*;

public class SecT131FieldElement extends ECFieldElement
{
    protected long[] x;
    
    public SecT131FieldElement() {
        this.x = Nat192.create64();
    }
    
    public SecT131FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= 131) {
            this.x = SecT131Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecT131FieldElement");
    }
    
    protected SecT131FieldElement(final long[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat192.create64();
        SecT131Field.add(this.x, ((SecT131FieldElement)ecFieldElement).x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public ECFieldElement addOne() {
        final long[] create64 = Nat192.create64();
        SecT131Field.addOne(this.x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        return this.multiply(ecFieldElement.invert());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecT131FieldElement && Nat192.eq64(this.x, ((SecT131FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecT131Field";
    }
    
    @Override
    public int getFieldSize() {
        return 131;
    }
    
    public int getK1() {
        return 2;
    }
    
    public int getK2() {
        return 3;
    }
    
    public int getK3() {
        return 8;
    }
    
    public int getM() {
        return 131;
    }
    
    public int getRepresentation() {
        return 3;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.x, 0, 3) ^ 0x202F8;
    }
    
    @Override
    public ECFieldElement invert() {
        final long[] create64 = Nat192.create64();
        SecT131Field.invert(this.x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public boolean isOne() {
        return Nat192.isOne64(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat192.isZero64(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat192.create64();
        SecT131Field.multiply(this.x, ((SecT131FieldElement)ecFieldElement).x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiplyPlusProduct(ecFieldElement, ecFieldElement2, ecFieldElement3);
    }
    
    @Override
    public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        final long[] x = this.x;
        final long[] x2 = ((SecT131FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT131FieldElement)ecFieldElement2).x;
        final long[] x4 = ((SecT131FieldElement)ecFieldElement3).x;
        final long[] create64 = Nat.create64(5);
        SecT131Field.multiplyAddToExt(x, x2, create64);
        SecT131Field.multiplyAddToExt(x3, x4, create64);
        final long[] create65 = Nat192.create64();
        SecT131Field.reduce(create64, create65);
        return new SecT131FieldElement(create65);
    }
    
    @Override
    public ECFieldElement negate() {
        return this;
    }
    
    @Override
    public ECFieldElement sqrt() {
        final long[] create64 = Nat192.create64();
        SecT131Field.sqrt(this.x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public ECFieldElement square() {
        final long[] create64 = Nat192.create64();
        SecT131Field.square(this.x, create64);
        return new SecT131FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.squarePlusProduct(ecFieldElement, ecFieldElement2);
    }
    
    @Override
    public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        final long[] x = this.x;
        final long[] x2 = ((SecT131FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT131FieldElement)ecFieldElement2).x;
        final long[] create64 = Nat.create64(5);
        SecT131Field.squareAddToExt(x, create64);
        SecT131Field.multiplyAddToExt(x2, x3, create64);
        final long[] create65 = Nat192.create64();
        SecT131Field.reduce(create64, create65);
        return new SecT131FieldElement(create65);
    }
    
    @Override
    public ECFieldElement squarePow(final int n) {
        if (n < 1) {
            return this;
        }
        final long[] create64 = Nat192.create64();
        SecT131Field.squareN(this.x, n, create64);
        return new SecT131FieldElement(create64);
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
        return Nat192.toBigInteger64(this.x);
    }
}
