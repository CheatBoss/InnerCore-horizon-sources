package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import org.spongycastle.math.raw.*;
import java.math.*;
import org.spongycastle.util.*;

public class SecT233FieldElement extends ECFieldElement
{
    protected long[] x;
    
    public SecT233FieldElement() {
        this.x = Nat256.create64();
    }
    
    public SecT233FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.bitLength() <= 233) {
            this.x = SecT233Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecT233FieldElement");
    }
    
    protected SecT233FieldElement(final long[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final long[] create64 = Nat256.create64();
        SecT233Field.add(this.x, ((SecT233FieldElement)ecFieldElement).x, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement addOne() {
        final long[] create64 = Nat256.create64();
        SecT233Field.addOne(this.x, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        return this.multiply(ecFieldElement.invert());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecT233FieldElement && Nat256.eq64(this.x, ((SecT233FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecT233Field";
    }
    
    @Override
    public int getFieldSize() {
        return 233;
    }
    
    public int getK1() {
        return 74;
    }
    
    public int getK2() {
        return 0;
    }
    
    public int getK3() {
        return 0;
    }
    
    public int getM() {
        return 233;
    }
    
    public int getRepresentation() {
        return 2;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.x, 0, 4) ^ 0x238DDA;
    }
    
    @Override
    public ECFieldElement invert() {
        final long[] create64 = Nat256.create64();
        SecT233Field.invert(this.x, create64);
        return new SecT233FieldElement(create64);
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
        SecT233Field.multiply(this.x, ((SecT233FieldElement)ecFieldElement).x, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement multiplyMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        return this.multiplyPlusProduct(ecFieldElement, ecFieldElement2, ecFieldElement3);
    }
    
    @Override
    public ECFieldElement multiplyPlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement ecFieldElement3) {
        final long[] x = this.x;
        final long[] x2 = ((SecT233FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT233FieldElement)ecFieldElement2).x;
        final long[] x4 = ((SecT233FieldElement)ecFieldElement3).x;
        final long[] ext64 = Nat256.createExt64();
        SecT233Field.multiplyAddToExt(x, x2, ext64);
        SecT233Field.multiplyAddToExt(x3, x4, ext64);
        final long[] create64 = Nat256.create64();
        SecT233Field.reduce(ext64, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement negate() {
        return this;
    }
    
    @Override
    public ECFieldElement sqrt() {
        final long[] create64 = Nat256.create64();
        SecT233Field.sqrt(this.x, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement square() {
        final long[] create64 = Nat256.create64();
        SecT233Field.square(this.x, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squareMinusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        return this.squarePlusProduct(ecFieldElement, ecFieldElement2);
    }
    
    @Override
    public ECFieldElement squarePlusProduct(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2) {
        final long[] x = this.x;
        final long[] x2 = ((SecT233FieldElement)ecFieldElement).x;
        final long[] x3 = ((SecT233FieldElement)ecFieldElement2).x;
        final long[] ext64 = Nat256.createExt64();
        SecT233Field.squareAddToExt(x, ext64);
        SecT233Field.multiplyAddToExt(x2, x3, ext64);
        final long[] create64 = Nat256.create64();
        SecT233Field.reduce(ext64, create64);
        return new SecT233FieldElement(create64);
    }
    
    @Override
    public ECFieldElement squarePow(final int n) {
        if (n < 1) {
            return this;
        }
        final long[] create64 = Nat256.create64();
        SecT233Field.squareN(this.x, n, create64);
        return new SecT233FieldElement(create64);
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
