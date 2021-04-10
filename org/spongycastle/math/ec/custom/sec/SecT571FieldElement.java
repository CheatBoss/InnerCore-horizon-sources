package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;
import java.math.*;
import org.spongycastle.util.*;

public class SecT571FieldElement extends ECFieldElement
{
    protected long[] x;
    
    public SecT571FieldElement() {
        this.x = Nat576.create64();
    }
    
    public SecT571FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= 571) {
            this.x = SecT571Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecT571FieldElement");
    }
    
    protected SecT571FieldElement(final long[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat576.create64();
        SecT571Field.add(this.x, ((SecT571FieldElement)ecFieldElement).x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement addOne() {
        final long[] create64 = Nat576.create64();
        SecT571Field.addOne(this.x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        return this.multiply(ecFieldElement.invert());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecT571FieldElement && Nat576.eq64(this.x, ((SecT571FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecT571Field";
    }
    
    @Override
    public int getFieldSize() {
        return 571;
    }
    
    public int getK1() {
        return 2;
    }
    
    public int getK2() {
        return 5;
    }
    
    public int getK3() {
        return 10;
    }
    
    public int getM() {
        return 571;
    }
    
    public int getRepresentation() {
        return 3;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.x, 0, 9) ^ 0x5724CC;
    }
    
    @Override
    public ECFieldElement invert() {
        final long[] create64 = Nat576.create64();
        SecT571Field.invert(this.x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public boolean isOne() {
        return Nat576.isOne64(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat576.isZero64(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat576.create64();
        SecT571Field.multiply(this.x, ((SecT571FieldElement)ecFieldElement).x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiplyPlusProduct(ecFieldElement, ecFieldElement2, ecFieldElement3);
    }
    
    @Override
    public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        final long[] x = this.x;
        final long[] x2 = ((SecT571FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT571FieldElement)ecFieldElement2).x;
        final long[] x4 = ((SecT571FieldElement)ecFieldElement3).x;
        final long[] ext64 = Nat576.createExt64();
        SecT571Field.multiplyAddToExt(x, x2, ext64);
        SecT571Field.multiplyAddToExt(x3, x4, ext64);
        final long[] create64 = Nat576.create64();
        SecT571Field.reduce(ext64, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement negate() {
        return this;
    }
    
    @Override
    public ECFieldElement sqrt() {
        final long[] create64 = Nat576.create64();
        SecT571Field.sqrt(this.x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement square() {
        final long[] create64 = Nat576.create64();
        SecT571Field.square(this.x, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.squarePlusProduct(ecFieldElement, ecFieldElement2);
    }
    
    @Override
    public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        final long[] x = this.x;
        final long[] x2 = ((SecT571FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT571FieldElement)ecFieldElement2).x;
        final long[] ext64 = Nat576.createExt64();
        SecT571Field.squareAddToExt(x, ext64);
        SecT571Field.multiplyAddToExt(x2, x3, ext64);
        final long[] create64 = Nat576.create64();
        SecT571Field.reduce(ext64, create64);
        return new SecT571FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squarePow(final int n) {
        if (n < 1) {
            return this;
        }
        final long[] create64 = Nat576.create64();
        SecT571Field.squareN(this.x, n, create64);
        return new SecT571FieldElement(create64);
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
        return Nat576.toBigInteger64(this.x);
    }
}
