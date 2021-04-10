package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.*;

public class SecP128R1FieldElement extends ECFieldElement
{
    public static final BigInteger Q;
    protected int[] x;
    
    static {
        Q = SecP128R1Curve.q;
    }
    
    public SecP128R1FieldElement() {
        this.x = Nat128.create();
    }
    
    public SecP128R1FieldElement(final BigInteger bigInteger) {
        if (bigInteger != null && bigInteger.signum() >= 0 && bigInteger.compareTo(SecP128R1FieldElement.Q) < 0) {
            this.x = SecP128R1Field.fromBigInteger(bigInteger);
            return;
        }
        throw new IllegalArgumentException("x value invalid for SecP128R1FieldElement");
    }
    
    protected SecP128R1FieldElement(final int[] x) {
        this.x = x;
    }
    
    @Override
    public ECFieldElement add(final ECFieldElement ecFieldElement) {
        final int[] create = Nat128.create();
        SecP128R1Field.add(this.x, ((SecP128R1FieldElement)ecFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement addOne() {
        final int[] create = Nat128.create();
        SecP128R1Field.addOne(this.x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement divide(final ECFieldElement ecFieldElement) {
        final int[] create = Nat128.create();
        Mod.invert(SecP128R1Field.P, ((SecP128R1FieldElement)ecFieldElement).x, create);
        SecP128R1Field.multiply(create, this.x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof SecP128R1FieldElement && Nat128.eq(this.x, ((SecP128R1FieldElement)o).x));
    }
    
    @Override
    public String getFieldName() {
        return "SecP128R1Field";
    }
    
    @Override
    public int getFieldSize() {
        return SecP128R1FieldElement.Q.bitLength();
    }
    
    @Override
    public int hashCode() {
        return SecP128R1FieldElement.Q.hashCode() ^ Arrays.hashCode(this.x, 0, 4);
    }
    
    @Override
    public ECFieldElement invert() {
        final int[] create = Nat128.create();
        Mod.invert(SecP128R1Field.P, this.x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public boolean isOne() {
        return Nat128.isOne(this.x);
    }
    
    @Override
    public boolean isZero() {
        return Nat128.isZero(this.x);
    }
    
    @Override
    public ECFieldElement multiply(final ECFieldElement ecFieldElement) {
        final int[] create = Nat128.create();
        SecP128R1Field.multiply(this.x, ((SecP128R1FieldElement)ecFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement negate() {
        final int[] create = Nat128.create();
        SecP128R1Field.negate(this.x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement sqrt() {
        final int[] x = this.x;
        if (Nat128.isZero(x)) {
            return this;
        }
        if (Nat128.isOne(x)) {
            return this;
        }
        final int[] create = Nat128.create();
        SecP128R1Field.square(x, create);
        SecP128R1Field.multiply(create, x, create);
        final int[] create2 = Nat128.create();
        SecP128R1Field.squareN(create, 2, create2);
        SecP128R1Field.multiply(create2, create, create2);
        final int[] create3 = Nat128.create();
        SecP128R1Field.squareN(create2, 4, create3);
        SecP128R1Field.multiply(create3, create2, create3);
        SecP128R1Field.squareN(create3, 2, create2);
        SecP128R1Field.multiply(create2, create, create2);
        SecP128R1Field.squareN(create2, 10, create);
        SecP128R1Field.multiply(create, create2, create);
        SecP128R1Field.squareN(create, 10, create3);
        SecP128R1Field.multiply(create3, create2, create3);
        SecP128R1Field.square(create3, create2);
        SecP128R1Field.multiply(create2, x, create2);
        SecP128R1Field.squareN(create2, 95, create2);
        SecP128R1Field.square(create2, create3);
        if (Nat128.eq(x, create3)) {
            return new SecP128R1FieldElement(create2);
        }
        return null;
    }
    
    @Override
    public ECFieldElement square() {
        final int[] create = Nat128.create();
        SecP128R1Field.square(this.x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public ECFieldElement subtract(final ECFieldElement ecFieldElement) {
        final int[] create = Nat128.create();
        SecP128R1Field.subtract(this.x, ((SecP128R1FieldElement)ecFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }
    
    @Override
    public boolean testBitZero() {
        final int[] x = this.x;
        boolean b = false;
        if (Nat128.getBit(x, 0) == 1) {
            b = true;
        }
        return b;
    }
    
    @Override
    public BigInteger toBigInteger() {
        return Nat128.toBigInteger(this.x);
    }
}
