package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP384R1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP384R1Curve.q;
    }
    
    public SecP384R1FieldElement() {
        this.x = Nat.create(12);
    }
    
    public SecP384R1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP384R1FieldElement.Q) < 0) {
            this.x = SecP384R1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP384R1FieldElement");
    }
    
    protected SecP384R1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat.create(12);
        SecP384R1Field.add(this.x, ((SecP384R1FieldElement)ecFieldElement).x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat.create(12);
        SecP384R1Field.addOne(this.x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat.create(12);
        Mod.invert(SecP384R1Field.P, ((SecP384R1FieldElement)ecFieldElement).x, create);
        SecP384R1Field.multiply(create, this.x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP384R1FieldElement && Nat.eq(12, this.x, ((SecP384R1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP384R1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP384R1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP384R1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 12);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat.create(12);
        Mod.invert(SecP384R1Field.P, this.x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat.isOne(12, this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat.isZero(12, this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat.create(12);
        SecP384R1Field.multiply(this.x, ((SecP384R1FieldElement)ecFieldElement).x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat.create(12);
        SecP384R1Field.negate(this.x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat.isZero(12, x)) {
            return this;
        }
        if (Nat.isOne(12, x)) {
            return this;
        }
        final int[] create = Nat.create(12);
        final int[] create2 = Nat.create(12);
        final int[] create3 = Nat.create(12);
        final int[] create4 = Nat.create(12);
        SecP384R1Field.square(x, create);
        SecP384R1Field.multiply(create, x, create);
        SecP384R1Field.squareN(create, 2, create2);
        SecP384R1Field.multiply(create2, create, create2);
        SecP384R1Field.square(create2, create2);
        SecP384R1Field.multiply(create2, x, create2);
        SecP384R1Field.squareN(create2, 5, create3);
        SecP384R1Field.multiply(create3, create2, create3);
        SecP384R1Field.squareN(create3, 5, create4);
        SecP384R1Field.multiply(create4, create2, create4);
        SecP384R1Field.squareN(create4, 15, create2);
        SecP384R1Field.multiply(create2, create4, create2);
        SecP384R1Field.squareN(create2, 2, create3);
        SecP384R1Field.multiply(create, create3, create);
        SecP384R1Field.squareN(create3, 28, create3);
        SecP384R1Field.multiply(create2, create3, create2);
        SecP384R1Field.squareN(create2, 60, create3);
        SecP384R1Field.multiply(create3, create2, create3);
        SecP384R1Field.squareN(create3, 120, create2);
        SecP384R1Field.multiply(create2, create3, create2);
        SecP384R1Field.squareN(create2, 15, create2);
        SecP384R1Field.multiply(create2, create4, create2);
        SecP384R1Field.squareN(create2, 33, create2);
        SecP384R1Field.multiply(create2, create, create2);
        SecP384R1Field.squareN(create2, 64, create2);
        SecP384R1Field.multiply(create2, x, create2);
        SecP384R1Field.squareN(create2, 30, create);
        SecP384R1Field.square(create, create2);
        if (Nat.eq(12, x, create2)) {
            return new SecP384R1FieldElement(create);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat.create(12);
        SecP384R1Field.square(this.x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat.create(12);
        SecP384R1Field.subtract(this.x, ((SecP384R1FieldElement)ecFieldElement).x, create);
        return new SecP384R1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat.toBigInteger(12, this.x);
    }
}
