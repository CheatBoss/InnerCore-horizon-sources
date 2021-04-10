package org.spongycastle.pqc.math.linearalgebra;

import java.util.*;
import java.math.*;

public class GF2nPolynomialElement extends GF2nElement
{
    private static final int[] bitMask;
    private GF2Polynomial polynomial;
    
    static {
        bitMask = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE, 0 };
    }
    
    public GF2nPolynomialElement(final GF2nPolynomialElement gf2nPolynomialElement) {
        this.mField = gf2nPolynomialElement.mField;
        this.mDegree = gf2nPolynomialElement.mDegree;
        this.polynomial = new GF2Polynomial(gf2nPolynomialElement.polynomial);
    }
    
    public GF2nPolynomialElement(final GF2nPolynomialField mField, final Random random) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree);
        this.randomize(random);
    }
    
    public GF2nPolynomialElement(final GF2nPolynomialField mField, final GF2Polynomial gf2Polynomial) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        (this.polynomial = new GF2Polynomial(gf2Polynomial)).expandN(this.mDegree);
    }
    
    public GF2nPolynomialElement(final GF2nPolynomialField mField, final byte[] array) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        (this.polynomial = new GF2Polynomial(this.mDegree, array)).expandN(this.mDegree);
    }
    
    public GF2nPolynomialElement(final GF2nPolynomialField mField, final int[] array) {
        this.mField = mField;
        this.mDegree = this.mField.getDegree();
        (this.polynomial = new GF2Polynomial(this.mDegree, array)).expandN(mField.mDegree);
    }
    
    public static GF2nPolynomialElement ONE(final GF2nPolynomialField gf2nPolynomialField) {
        return new GF2nPolynomialElement(gf2nPolynomialField, new GF2Polynomial(gf2nPolynomialField.getDegree(), new int[] { 1 }));
    }
    
    public static GF2nPolynomialElement ZERO(final GF2nPolynomialField gf2nPolynomialField) {
        return new GF2nPolynomialElement(gf2nPolynomialField, new GF2Polynomial(gf2nPolynomialField.getDegree()));
    }
    
    private GF2Polynomial getGF2Polynomial() {
        return new GF2Polynomial(this.polynomial);
    }
    
    private GF2nPolynomialElement halfTrace() throws RuntimeException {
        if ((this.mDegree & 0x1) != 0x0) {
            final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
            for (int i = 1; i <= this.mDegree - 1 >> 1; ++i) {
                gf2nPolynomialElement.squareThis();
                gf2nPolynomialElement.squareThis();
                gf2nPolynomialElement.addToThis(this);
            }
            return gf2nPolynomialElement;
        }
        throw new RuntimeException();
    }
    
    private void randomize(final Random random) {
        this.polynomial.expandN(this.mDegree);
        this.polynomial.randomize(random);
    }
    
    private void reducePentanomialBitwise(final int[] array) {
        final int mDegree = this.mDegree;
        final int n = array[2];
        final int mDegree2 = this.mDegree;
        final int n2 = array[1];
        final int mDegree3 = this.mDegree;
        final int n3 = array[0];
        for (int i = this.polynomial.getLength() - 1; i >= this.mDegree; --i) {
            if (this.polynomial.testBit(i)) {
                this.polynomial.xorBit(i);
                this.polynomial.xorBit(i - (mDegree - n));
                this.polynomial.xorBit(i - (mDegree2 - n2));
                this.polynomial.xorBit(i - (mDegree3 - n3));
                this.polynomial.xorBit(i - this.mDegree);
            }
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }
    
    private void reduceThis() {
        if (this.polynomial.getLength() > this.mDegree) {
            if (((GF2nPolynomialField)this.mField).isTrinomial()) {
                try {
                    final int tc = ((GF2nPolynomialField)this.mField).getTc();
                    if (this.mDegree - tc > 32 && this.polynomial.getLength() <= this.mDegree << 1) {
                        this.polynomial.reduceTrinomial(this.mDegree, tc);
                        return;
                    }
                    this.reduceTrinomialBitwise(tc);
                    return;
                }
                catch (RuntimeException ex) {
                    throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a trinomial");
                }
            }
            if (((GF2nPolynomialField)this.mField).isPentanomial()) {
                try {
                    final int[] pc = ((GF2nPolynomialField)this.mField).getPc();
                    if (this.mDegree - pc[2] > 32 && this.polynomial.getLength() <= this.mDegree << 1) {
                        this.polynomial.reducePentanomial(this.mDegree, pc);
                        return;
                    }
                    this.reducePentanomialBitwise(pc);
                    return;
                }
                catch (RuntimeException ex2) {
                    throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a pentanomial");
                }
            }
            (this.polynomial = this.polynomial.remainder(this.mField.getFieldPolynomial())).expandN(this.mDegree);
            return;
        }
        if (this.polynomial.getLength() < this.mDegree) {
            this.polynomial.expandN(this.mDegree);
        }
    }
    
    private void reduceTrinomialBitwise(final int n) {
        final int mDegree = this.mDegree;
        int length = this.polynomial.getLength();
        while (true) {
            final int n2 = length - 1;
            if (n2 < this.mDegree) {
                break;
            }
            length = n2;
            if (!this.polynomial.testBit(n2)) {
                continue;
            }
            this.polynomial.xorBit(n2);
            this.polynomial.xorBit(n2 - (mDegree - n));
            this.polynomial.xorBit(n2 - this.mDegree);
            length = n2;
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }
    
    @Override
    public GFElement add(final GFElement gfElement) throws RuntimeException {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.addToThis(gfElement);
        return gf2nPolynomialElement;
    }
    
    @Override
    public void addToThis(final GFElement gfElement) throws RuntimeException {
        if (!(gfElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        final GF2nField mField = this.mField;
        final GF2nPolynomialElement gf2nPolynomialElement = (GF2nPolynomialElement)gfElement;
        if (mField.equals(gf2nPolynomialElement.mField)) {
            this.polynomial.addToThis(gf2nPolynomialElement.polynomial);
            return;
        }
        throw new RuntimeException();
    }
    
    @Override
    void assignOne() {
        this.polynomial.assignOne();
    }
    
    @Override
    void assignZero() {
        this.polynomial.assignZero();
    }
    
    @Override
    public Object clone() {
        return new GF2nPolynomialElement(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GF2nPolynomialElement)) {
            return false;
        }
        final GF2nPolynomialElement gf2nPolynomialElement = (GF2nPolynomialElement)o;
        return (this.mField == gf2nPolynomialElement.mField || this.mField.getFieldPolynomial().equals(gf2nPolynomialElement.mField.getFieldPolynomial())) && this.polynomial.equals(gf2nPolynomialElement.polynomial);
    }
    
    @Override
    public int hashCode() {
        return this.mField.hashCode() + this.polynomial.hashCode();
    }
    
    @Override
    public GF2nElement increase() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.increaseThis();
        return gf2nPolynomialElement;
    }
    
    @Override
    public void increaseThis() {
        this.polynomial.increaseThis();
    }
    
    @Override
    public GFElement invert() throws ArithmeticException {
        return this.invertMAIA();
    }
    
    public GF2nPolynomialElement invertEEA() throws ArithmeticException {
        if (!this.isZero()) {
            GF2Polynomial gf2Polynomial = new GF2Polynomial(this.mDegree + 32, "ONE");
            gf2Polynomial.reduceN();
            GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this.mDegree + 32);
            gf2Polynomial2.reduceN();
            GF2Polynomial gf2Polynomial3 = this.getGF2Polynomial();
            GF2Polynomial fieldPolynomial = this.mField.getFieldPolynomial();
            gf2Polynomial3.reduceN();
            while (!gf2Polynomial3.isOne()) {
                gf2Polynomial3.reduceN();
                fieldPolynomial.reduceN();
                final int n = gf2Polynomial3.getLength() - fieldPolynomial.getLength();
                GF2Polynomial gf2Polynomial4 = gf2Polynomial;
                GF2Polynomial gf2Polynomial5 = gf2Polynomial2;
                GF2Polynomial gf2Polynomial6 = gf2Polynomial3;
                GF2Polynomial gf2Polynomial7 = fieldPolynomial;
                int n2;
                if ((n2 = n) < 0) {
                    n2 = -n;
                    gf2Polynomial.reduceN();
                    gf2Polynomial7 = gf2Polynomial3;
                    gf2Polynomial6 = fieldPolynomial;
                    gf2Polynomial5 = gf2Polynomial;
                    gf2Polynomial4 = gf2Polynomial2;
                }
                gf2Polynomial6.shiftLeftAddThis(gf2Polynomial7, n2);
                gf2Polynomial4.shiftLeftAddThis(gf2Polynomial5, n2);
                gf2Polynomial = gf2Polynomial4;
                gf2Polynomial2 = gf2Polynomial5;
                gf2Polynomial3 = gf2Polynomial6;
                fieldPolynomial = gf2Polynomial7;
            }
            gf2Polynomial.reduceN();
            return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gf2Polynomial);
        }
        throw new ArithmeticException();
    }
    
    public GF2nPolynomialElement invertMAIA() throws ArithmeticException {
        if (!this.isZero()) {
            GF2Polynomial gf2Polynomial = new GF2Polynomial(this.mDegree, "ONE");
            GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this.mDegree);
            GF2Polynomial gf2Polynomial3 = this.getGF2Polynomial();
            GF2Polynomial fieldPolynomial = this.mField.getFieldPolynomial();
            while (true) {
                if (!gf2Polynomial3.testBit(0)) {
                    gf2Polynomial3.shiftRightThis();
                    if (gf2Polynomial.testBit(0)) {
                        gf2Polynomial.addToThis(this.mField.getFieldPolynomial());
                    }
                    gf2Polynomial.shiftRightThis();
                }
                else {
                    if (gf2Polynomial3.isOne()) {
                        break;
                    }
                    gf2Polynomial3.reduceN();
                    fieldPolynomial.reduceN();
                    GF2Polynomial gf2Polynomial4 = gf2Polynomial;
                    GF2Polynomial gf2Polynomial5 = gf2Polynomial2;
                    GF2Polynomial gf2Polynomial6 = gf2Polynomial3;
                    GF2Polynomial gf2Polynomial7 = fieldPolynomial;
                    if (gf2Polynomial3.getLength() < fieldPolynomial.getLength()) {
                        gf2Polynomial7 = gf2Polynomial3;
                        gf2Polynomial6 = fieldPolynomial;
                        gf2Polynomial5 = gf2Polynomial;
                        gf2Polynomial4 = gf2Polynomial2;
                    }
                    gf2Polynomial6.addToThis(gf2Polynomial7);
                    gf2Polynomial4.addToThis(gf2Polynomial5);
                    gf2Polynomial = gf2Polynomial4;
                    gf2Polynomial2 = gf2Polynomial5;
                    gf2Polynomial3 = gf2Polynomial6;
                    fieldPolynomial = gf2Polynomial7;
                }
            }
            return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gf2Polynomial);
        }
        throw new ArithmeticException();
    }
    
    public GF2nPolynomialElement invertSquare() throws ArithmeticException {
        if (!this.isZero()) {
            final int n = this.mField.getDegree() - 1;
            final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
            gf2nPolynomialElement.polynomial.expandN((this.mDegree << 1) + 32);
            gf2nPolynomialElement.polynomial.reduceN();
            int i = IntegerFunctions.floorLog(n) - 1;
            int n2 = 1;
            while (i >= 0) {
                final GF2nPolynomialElement gf2nPolynomialElement2 = new GF2nPolynomialElement(gf2nPolynomialElement);
                for (int j = 1; j <= n2; ++j) {
                    gf2nPolynomialElement2.squareThisPreCalc();
                }
                gf2nPolynomialElement.multiplyThisBy(gf2nPolynomialElement2);
                final int n3 = n2 <<= 1;
                if ((GF2nPolynomialElement.bitMask[i] & n) != 0x0) {
                    gf2nPolynomialElement.squareThisPreCalc();
                    gf2nPolynomialElement.multiplyThisBy(this);
                    n2 = n3 + 1;
                }
                --i;
            }
            gf2nPolynomialElement.squareThisPreCalc();
            return gf2nPolynomialElement;
        }
        throw new ArithmeticException();
    }
    
    @Override
    public boolean isOne() {
        return this.polynomial.isOne();
    }
    
    @Override
    public boolean isZero() {
        return this.polynomial.isZero();
    }
    
    @Override
    public GFElement multiply(final GFElement gfElement) throws RuntimeException {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.multiplyThisBy(gfElement);
        return gf2nPolynomialElement;
    }
    
    @Override
    public void multiplyThisBy(final GFElement gfElement) throws RuntimeException {
        if (!(gfElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        final GF2nField mField = this.mField;
        final GF2nPolynomialElement gf2nPolynomialElement = (GF2nPolynomialElement)gfElement;
        if (!mField.equals(gf2nPolynomialElement.mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gfElement)) {
            this.squareThis();
            return;
        }
        this.polynomial = this.polynomial.multiply(gf2nPolynomialElement.polynomial);
        this.reduceThis();
    }
    
    public GF2nPolynomialElement power(final int n) {
        if (n == 1) {
            return new GF2nPolynomialElement(this);
        }
        final GF2nPolynomialElement one = ONE((GF2nPolynomialField)this.mField);
        if (n == 0) {
            return one;
        }
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.polynomial.expandN((gf2nPolynomialElement.mDegree << 1) + 32);
        gf2nPolynomialElement.polynomial.reduceN();
        for (int i = 0; i < this.mDegree; ++i) {
            if ((1 << i & n) != 0x0) {
                one.multiplyThisBy(gf2nPolynomialElement);
            }
            gf2nPolynomialElement.square();
        }
        return one;
    }
    
    @Override
    public GF2nElement solveQuadraticEquation() throws RuntimeException {
        if (this.isZero()) {
            return ZERO((GF2nPolynomialField)this.mField);
        }
        if ((this.mDegree & 0x1) == 0x1) {
            return this.halfTrace();
        }
        GF2nPolynomialElement gf2nPolynomialElement;
        GF2nPolynomialElement zero;
        do {
            final GF2nPolynomialElement gf2nPolynomialElement2 = new GF2nPolynomialElement((GF2nPolynomialField)this.mField, new Random());
            zero = ZERO((GF2nPolynomialField)this.mField);
            gf2nPolynomialElement = (GF2nPolynomialElement)gf2nPolynomialElement2.clone();
            for (int i = 1; i < this.mDegree; ++i) {
                zero.squareThis();
                gf2nPolynomialElement.squareThis();
                zero.addToThis(gf2nPolynomialElement.multiply(this));
                gf2nPolynomialElement.addToThis(gf2nPolynomialElement2);
            }
        } while (gf2nPolynomialElement.isZero());
        if (this.equals(zero.square().add(zero))) {
            return zero;
        }
        throw new RuntimeException();
    }
    
    @Override
    public GF2nElement square() {
        return this.squarePreCalc();
    }
    
    public GF2nPolynomialElement squareBitwise() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.squareThisBitwise();
        gf2nPolynomialElement.reduceThis();
        return gf2nPolynomialElement;
    }
    
    public GF2nPolynomialElement squareMatrix() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.squareThisMatrix();
        gf2nPolynomialElement.reduceThis();
        return gf2nPolynomialElement;
    }
    
    public GF2nPolynomialElement squarePreCalc() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.squareThisPreCalc();
        gf2nPolynomialElement.reduceThis();
        return gf2nPolynomialElement;
    }
    
    @Override
    public GF2nElement squareRoot() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        gf2nPolynomialElement.squareRootThis();
        return gf2nPolynomialElement;
    }
    
    @Override
    public void squareRootThis() {
        this.polynomial.expandN((this.mDegree << 1) + 32);
        this.polynomial.reduceN();
        for (int i = 0; i < this.mField.getDegree() - 1; ++i) {
            this.squareThis();
        }
    }
    
    @Override
    public void squareThis() {
        this.squareThisPreCalc();
    }
    
    public void squareThisBitwise() {
        this.polynomial.squareThisBitwise();
        this.reduceThis();
    }
    
    public void squareThisMatrix() {
        final GF2Polynomial polynomial = new GF2Polynomial(this.mDegree);
        for (int i = 0; i < this.mDegree; ++i) {
            if (this.polynomial.vectorMult(((GF2nPolynomialField)this.mField).squaringMatrix[this.mDegree - i - 1])) {
                polynomial.setBit(i);
            }
        }
        this.polynomial = polynomial;
    }
    
    public void squareThisPreCalc() {
        this.polynomial.squareThisPreCalc();
        this.reduceThis();
    }
    
    @Override
    boolean testBit(final int n) {
        return this.polynomial.testBit(n);
    }
    
    @Override
    public boolean testRightmostBit() {
        return this.polynomial.testBit(0);
    }
    
    @Override
    public byte[] toByteArray() {
        return this.polynomial.toByteArray();
    }
    
    @Override
    public BigInteger toFlexiBigInt() {
        return this.polynomial.toFlexiBigInt();
    }
    
    @Override
    public String toString() {
        return this.polynomial.toString(16);
    }
    
    @Override
    public String toString(final int n) {
        return this.polynomial.toString(n);
    }
    
    @Override
    public int trace() {
        final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this);
        for (int i = 1; i < this.mDegree; ++i) {
            gf2nPolynomialElement.squareThis();
            gf2nPolynomialElement.addToThis(this);
        }
        if (gf2nPolynomialElement.isOne()) {
            return 1;
        }
        return 0;
    }
}
