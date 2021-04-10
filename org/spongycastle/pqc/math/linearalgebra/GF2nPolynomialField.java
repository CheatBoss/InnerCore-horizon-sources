package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;
import java.util.*;

public class GF2nPolynomialField extends GF2nField
{
    private boolean isPentanomial;
    private boolean isTrinomial;
    private int[] pc;
    GF2Polynomial[] squaringMatrix;
    private int tc;
    
    public GF2nPolynomialField(final int mDegree, final SecureRandom secureRandom) {
        super(secureRandom);
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (mDegree >= 3) {
            this.mDegree = mDegree;
            this.computeFieldPolynomial();
            this.computeSquaringMatrix();
            this.fields = new Vector();
            this.matrices = new Vector();
            return;
        }
        throw new IllegalArgumentException("k must be at least 3");
    }
    
    public GF2nPolynomialField(int i, final SecureRandom secureRandom, final GF2Polynomial fieldPolynomial) throws RuntimeException {
        super(secureRandom);
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (i < 3) {
            throw new IllegalArgumentException("degree must be at least 3");
        }
        if (fieldPolynomial.getLength() != i + 1) {
            throw new RuntimeException();
        }
        if (fieldPolynomial.isIrreducible()) {
            this.mDegree = i;
            this.fieldPolynomial = fieldPolynomial;
            this.computeSquaringMatrix();
            i = 1;
            int n = 2;
            while (i < this.fieldPolynomial.getLength() - 1) {
                int n2 = n;
                if (this.fieldPolynomial.testBit(i)) {
                    final int n3 = n + 1;
                    if (n3 == 3) {
                        this.tc = i;
                    }
                    if ((n2 = n3) <= 5) {
                        this.pc[n3 - 3] = i;
                        n2 = n3;
                    }
                }
                ++i;
                n = n2;
            }
            if (n == 3) {
                this.isTrinomial = true;
            }
            if (n == 5) {
                this.isPentanomial = true;
            }
            this.fields = new Vector();
            this.matrices = new Vector();
            return;
        }
        throw new RuntimeException();
    }
    
    public GF2nPolynomialField(final int mDegree, final SecureRandom secureRandom, final boolean b) {
        super(secureRandom);
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (mDegree >= 3) {
            this.mDegree = mDegree;
            if (b) {
                this.computeFieldPolynomial();
            }
            else {
                this.computeFieldPolynomial2();
            }
            this.computeSquaringMatrix();
            this.fields = new Vector();
            this.matrices = new Vector();
            return;
        }
        throw new IllegalArgumentException("k must be at least 3");
    }
    
    private void computeSquaringMatrix() {
        final GF2Polynomial[] array = new GF2Polynomial[this.mDegree - 1];
        this.squaringMatrix = new GF2Polynomial[this.mDegree];
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            final GF2Polynomial[] squaringMatrix = this.squaringMatrix;
            i = n;
            if (n2 >= squaringMatrix.length) {
                break;
            }
            squaringMatrix[n2] = new GF2Polynomial(this.mDegree, "ZERO");
            ++n2;
        }
        while (i < this.mDegree - 1) {
            array[i] = new GF2Polynomial(1, "ONE").shiftLeft(this.mDegree + i).remainder(this.fieldPolynomial);
            ++i;
        }
        for (int j = 1; j <= Math.abs(this.mDegree >> 1); ++j) {
            for (int k = 1; k <= this.mDegree; ++k) {
                if (array[this.mDegree - (j << 1)].testBit(this.mDegree - k)) {
                    this.squaringMatrix[k - 1].setBit(this.mDegree - j);
                }
            }
        }
        for (int l = Math.abs(this.mDegree >> 1) + 1; l <= this.mDegree; ++l) {
            this.squaringMatrix[(l << 1) - this.mDegree - 1].setBit(this.mDegree - l);
        }
    }
    
    private boolean testPentanomials() {
        (this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1)).setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        int bit;
        boolean b;
        int bit2;
        int n;
        int bit3;
        int n2;
        boolean irreducible;
        int[] pc;
        for (bit = 1, b = false; bit <= this.mDegree - 3 && !b; bit = n) {
            this.fieldPolynomial.setBit(bit);
            for (n = (bit2 = bit + 1); bit2 <= this.mDegree - 2 && !b; bit2 = n2) {
                this.fieldPolynomial.setBit(bit2);
                for (n2 = (bit3 = bit2 + 1); bit3 <= this.mDegree - 1 && !b; ++bit3) {
                    this.fieldPolynomial.setBit(bit3);
                    if ((this.mDegree & 0x1) != 0x0 | (bit & 0x1) != 0x0 | (bit2 & 0x1) != 0x0 | (bit3 & 0x1) != 0x0) {
                        irreducible = this.fieldPolynomial.isIrreducible();
                        if (b = irreducible) {
                            this.isPentanomial = true;
                            pc = this.pc;
                            pc[0] = bit;
                            pc[1] = bit2;
                            pc[2] = bit3;
                            return irreducible;
                        }
                    }
                    this.fieldPolynomial.resetBit(bit3);
                }
                this.fieldPolynomial.resetBit(bit2);
            }
            this.fieldPolynomial.resetBit(bit);
        }
        return b;
    }
    
    private boolean testRandom() {
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        do {
            this.fieldPolynomial.randomize();
            this.fieldPolynomial.setBit(this.mDegree);
            this.fieldPolynomial.setBit(0);
        } while (!this.fieldPolynomial.isIrreducible());
        return true;
    }
    
    private boolean testTrinomials() {
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        final GF2Polynomial fieldPolynomial = this.fieldPolynomial;
        boolean irreducible = false;
        fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        for (int n = 1; n < this.mDegree && !irreducible; irreducible = this.fieldPolynomial.isIrreducible(), ++n) {
            this.fieldPolynomial.setBit(n);
            final boolean irreducible2 = this.fieldPolynomial.isIrreducible();
            if (irreducible2) {
                this.isTrinomial = true;
                this.tc = n;
                return irreducible2;
            }
            this.fieldPolynomial.resetBit(n);
        }
        return irreducible;
    }
    
    @Override
    protected void computeCOBMatrix(final GF2nField gf2nField) {
        if (this.mDegree != gf2nField.mDegree) {
            throw new IllegalArgumentException("GF2nPolynomialField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        final boolean b = gf2nField instanceof GF2nONBField;
        if (b) {
            gf2nField.computeCOBMatrix(this);
            return;
        }
        final GF2Polynomial[] array = new GF2Polynomial[this.mDegree];
        for (int i = 0; i < this.mDegree; ++i) {
            array[i] = new GF2Polynomial(this.mDegree);
        }
        GF2nElement randomRoot;
        do {
            randomRoot = gf2nField.getRandomRoot(this.fieldPolynomial);
        } while (randomRoot.isZero());
        GF2nElement[] array2;
        if (randomRoot instanceof GF2nONBElement) {
            array2 = new GF2nONBElement[this.mDegree];
            array2[this.mDegree - 1] = GF2nONBElement.ONE((GF2nONBField)gf2nField);
        }
        else {
            array2 = new GF2nPolynomialElement[this.mDegree];
            array2[this.mDegree - 1] = GF2nPolynomialElement.ONE((GF2nPolynomialField)gf2nField);
        }
        array2[this.mDegree - 2] = randomRoot;
        for (int j = this.mDegree - 3; j >= 0; --j) {
            array2[j] = (GF2nElement)array2[j + 1].multiply(randomRoot);
        }
        if (b) {
            for (int k = 0; k < this.mDegree; ++k) {
                for (int l = 0; l < this.mDegree; ++l) {
                    if (array2[k].testBit(this.mDegree - l - 1)) {
                        array[this.mDegree - l - 1].setBit(this.mDegree - k - 1);
                    }
                }
            }
        }
        else {
            for (int n = 0; n < this.mDegree; ++n) {
                for (int n2 = 0; n2 < this.mDegree; ++n2) {
                    if (array2[n].testBit(n2)) {
                        array[this.mDegree - n2 - 1].setBit(this.mDegree - n - 1);
                    }
                }
            }
        }
        this.fields.addElement(gf2nField);
        this.matrices.addElement(array);
        gf2nField.fields.addElement(this);
        gf2nField.matrices.addElement(this.invertMatrix(array));
    }
    
    @Override
    protected void computeFieldPolynomial() {
        if (this.testTrinomials()) {
            return;
        }
        if (this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }
    
    protected void computeFieldPolynomial2() {
        if (this.testTrinomials()) {
            return;
        }
        if (this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }
    
    public int[] getPc() throws RuntimeException {
        if (this.isPentanomial) {
            final int[] array = new int[3];
            System.arraycopy(this.pc, 0, array, 0, 3);
            return array;
        }
        throw new RuntimeException();
    }
    
    @Override
    protected GF2nElement getRandomRoot(final GF2Polynomial gf2Polynomial) {
        GF2nPolynomial quotient = new GF2nPolynomial(gf2Polynomial, this);
        while (quotient.getDegree() > 1) {
            int degree;
            int degree2;
            GF2nPolynomial gcd;
            do {
                final GF2nPolynomialElement gf2nPolynomialElement = new GF2nPolynomialElement(this, this.random);
                final GF2nPolynomial gf2nPolynomial = new GF2nPolynomial(2, GF2nPolynomialElement.ZERO(this));
                gf2nPolynomial.set(1, gf2nPolynomialElement);
                GF2nPolynomial add = new GF2nPolynomial(gf2nPolynomial);
                for (int i = 1; i <= this.mDegree - 1; ++i) {
                    add = add.multiplyAndReduce(add, quotient).add(gf2nPolynomial);
                }
                gcd = add.gcd(quotient);
                degree = gcd.getDegree();
                degree2 = quotient.getDegree();
            } while (degree == 0 || degree == degree2);
            if (degree << 1 > degree2) {
                quotient = quotient.quotient(gcd);
            }
            else {
                quotient = new GF2nPolynomial(gcd);
            }
        }
        return quotient.at(0);
    }
    
    public GF2Polynomial getSquaringVector(final int n) {
        return new GF2Polynomial(this.squaringMatrix[n]);
    }
    
    public int getTc() throws RuntimeException {
        if (this.isTrinomial) {
            return this.tc;
        }
        throw new RuntimeException();
    }
    
    public boolean isPentanomial() {
        return this.isPentanomial;
    }
    
    public boolean isTrinomial() {
        return this.isTrinomial;
    }
}
