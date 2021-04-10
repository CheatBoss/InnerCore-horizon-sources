package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;
import java.lang.reflect.*;
import java.util.*;

public class GF2nONBField extends GF2nField
{
    private static final int MAXLONG = 64;
    private int mBit;
    private int mLength;
    int[][] mMult;
    private int mType;
    
    public GF2nONBField(int i, final SecureRandom secureRandom) throws RuntimeException {
        super(secureRandom);
        if (i < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = i;
        this.mLength = this.mDegree / 64;
        i = (this.mDegree & 0x3F);
        if ((this.mBit = i) == 0) {
            this.mBit = 64;
        }
        else {
            ++this.mLength;
        }
        this.computeType();
        if (this.mType < 3) {
            this.mMult = (int[][])Array.newInstance(Integer.TYPE, this.mDegree, 2);
            int[][] mMult;
            for (i = 0; i < this.mDegree; ++i) {
                mMult = this.mMult;
                mMult[i][0] = -1;
                mMult[i][1] = -1;
            }
            this.computeMultMatrix();
            this.computeFieldPolynomial();
            this.fields = new Vector();
            this.matrices = new Vector();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("\nThe type of this field is ");
        sb.append(this.mType);
        throw new RuntimeException(sb.toString());
    }
    
    private void computeMultMatrix() {
        final int mType = this.mType;
        if ((mType & 0x7) != 0x0) {
            final int n = mType * this.mDegree + 1;
            final int[] array = new int[n];
            final int mType2 = this.mType;
            int elementOfOrder;
            if (mType2 == 1) {
                elementOfOrder = 1;
            }
            else if (mType2 == 2) {
                elementOfOrder = n - 1;
            }
            else {
                elementOfOrder = this.elementOfOrder(mType2, n);
            }
            int n2 = 0;
            int n3 = 1;
            int mType3;
            while (true) {
                mType3 = this.mType;
                if (n2 >= mType3) {
                    break;
                }
                int n4 = n3;
                for (int i = 0; i < this.mDegree; ++i) {
                    array[n4] = i;
                    final int n5 = (n4 << 1) % n;
                    if ((n4 = n5) < 0) {
                        n4 = n5 + n;
                    }
                }
                final int n6 = n3 * elementOfOrder % n;
                if ((n3 = n6) < 0) {
                    n3 = n6 + n;
                }
                ++n2;
            }
            if (mType3 == 1) {
                int n7;
                for (int j = 1; j < n - 1; j = n7) {
                    final int[][] mMult = this.mMult;
                    n7 = j + 1;
                    if (mMult[array[n7]][0] == -1) {
                        mMult[array[n7]][0] = array[n - j];
                    }
                    else {
                        mMult[array[n7]][1] = array[n - j];
                    }
                }
                for (int n8 = this.mDegree >> 1, k = 1; k <= n8; ++k) {
                    final int[][] mMult2 = this.mMult;
                    final int n9 = k - 1;
                    if (mMult2[n9][0] == -1) {
                        mMult2[n9][0] = n8 + k - 1;
                    }
                    else {
                        mMult2[n9][1] = n8 + k - 1;
                    }
                    final int[][] mMult3 = this.mMult;
                    final int n10 = n8 + k - 1;
                    if (mMult3[n10][0] == -1) {
                        mMult3[n10][0] = n9;
                    }
                    else {
                        mMult3[n10][1] = n9;
                    }
                }
            }
            else {
                if (mType3 != 2) {
                    throw new RuntimeException("only type 1 or type 2 implemented");
                }
                int n11;
                for (int l = 1; l < n - 1; l = n11) {
                    final int[][] mMult4 = this.mMult;
                    n11 = l + 1;
                    if (mMult4[array[n11]][0] == -1) {
                        mMult4[array[n11]][0] = array[n - l];
                    }
                    else {
                        mMult4[array[n11]][1] = array[n - l];
                    }
                }
            }
            return;
        }
        throw new RuntimeException("bisher nur fuer Gausssche Normalbasen implementiert");
    }
    
    private void computeType() throws RuntimeException {
        if ((this.mDegree & 0x7) != 0x0) {
            this.mType = 1;
            int i = 0;
            while (i != 1) {
                final int n = this.mType * this.mDegree + 1;
                if (IntegerFunctions.isPrime(n)) {
                    i = IntegerFunctions.gcd(this.mType * this.mDegree / IntegerFunctions.order(2, n), this.mDegree);
                }
                ++this.mType;
            }
            if (--this.mType == 1) {
                final int n2 = (this.mDegree << 1) + 1;
                if (IntegerFunctions.isPrime(n2) && IntegerFunctions.gcd((this.mDegree << 1) / IntegerFunctions.order(2, n2), this.mDegree) == 1) {
                    ++this.mType;
                }
            }
            return;
        }
        throw new RuntimeException("The extension degree is divisible by 8!");
    }
    
    private int elementOfOrder(int i, int n) {
        final Random random = new Random();
        int n3;
        int n4;
        int n5;
        for (int n2 = 0; (n3 = n2) == 0; n2 = n5 + n4) {
            final int nextInt = random.nextInt();
            n4 = n - 1;
            n5 = nextInt % n4;
            if ((n2 = n5) < 0) {}
        }
        int order;
        while (true) {
            order = IntegerFunctions.order(n3, n);
            int n6 = n3;
            if (order % i == 0) {
                if (order != 0) {
                    break;
                }
                n6 = n3;
            }
            while ((n3 = n6) == 0) {
                final int nextInt2 = random.nextInt();
                final int n7 = n - 1;
                final int n8 = nextInt2 % n7;
                if ((n6 = n8) < 0) {
                    n6 = n8 + n7;
                }
            }
        }
        final int n9 = i / order;
        i = 2;
        n = n3;
        while (i <= n9) {
            n *= n3;
            ++i;
        }
        return n;
    }
    
    @Override
    protected void computeCOBMatrix(final GF2nField gf2nField) {
        if (this.mDegree == gf2nField.mDegree) {
            final GF2Polynomial[] array = new GF2Polynomial[this.mDegree];
            for (int i = 0; i < this.mDegree; ++i) {
                array[i] = new GF2Polynomial(this.mDegree);
            }
            GF2nElement randomRoot;
            do {
                randomRoot = gf2nField.getRandomRoot(this.fieldPolynomial);
            } while (randomRoot.isZero());
            final GF2nPolynomialElement[] array2 = new GF2nPolynomialElement[this.mDegree];
            array2[0] = (GF2nPolynomialElement)randomRoot.clone();
            for (int j = 1; j < this.mDegree; ++j) {
                array2[j] = (GF2nPolynomialElement)array2[j - 1].square();
            }
            for (int k = 0; k < this.mDegree; ++k) {
                for (int l = 0; l < this.mDegree; ++l) {
                    if (array2[k].testBit(l)) {
                        array[this.mDegree - l - 1].setBit(this.mDegree - k - 1);
                    }
                }
            }
            this.fields.addElement(gf2nField);
            this.matrices.addElement(array);
            gf2nField.fields.addElement(this);
            gf2nField.matrices.addElement(this.invertMatrix(array));
            return;
        }
        throw new IllegalArgumentException("GF2nField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
    }
    
    @Override
    protected void computeFieldPolynomial() {
        final int mType = this.mType;
        int i = 1;
        if (mType == 1) {
            this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1, "ALL");
            return;
        }
        if (mType == 2) {
            GF2Polynomial gf2Polynomial = new GF2Polynomial(this.mDegree + 1, "ONE");
            GF2Polynomial fieldPolynomial = new GF2Polynomial(this.mDegree + 1, "X");
            fieldPolynomial.addToThis(gf2Polynomial);
            while (i < this.mDegree) {
                final GF2Polynomial shiftLeft = fieldPolynomial.shiftLeft();
                shiftLeft.addToThis(gf2Polynomial);
                ++i;
                gf2Polynomial = fieldPolynomial;
                fieldPolynomial = shiftLeft;
            }
            this.fieldPolynomial = fieldPolynomial;
        }
    }
    
    int getONBBit() {
        return this.mBit;
    }
    
    int getONBLength() {
        return this.mLength;
    }
    
    @Override
    protected GF2nElement getRandomRoot(final GF2Polynomial gf2Polynomial) {
        GF2nPolynomial quotient = new GF2nPolynomial(gf2Polynomial, this);
        while (quotient.getDegree() > 1) {
            int degree;
            int degree2;
            GF2nPolynomial gcd;
            do {
                final GF2nONBElement gf2nONBElement = new GF2nONBElement(this, this.random);
                final GF2nPolynomial gf2nPolynomial = new GF2nPolynomial(2, GF2nONBElement.ZERO(this));
                gf2nPolynomial.set(1, gf2nONBElement);
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
    
    int[][] invMatrix(final int[][] array) {
        final int mDegree = this.mDegree;
        final int mDegree2 = this.mDegree;
        final Class<Integer> type = Integer.TYPE;
        final int n = 0;
        final int[][] array2 = (int[][])Array.newInstance(type, mDegree, mDegree2);
        final int[][] array3 = (int[][])Array.newInstance(Integer.TYPE, this.mDegree, this.mDegree);
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= this.mDegree) {
                break;
            }
            array3[n2][n2] = 1;
            ++n2;
        }
        while (i < this.mDegree) {
            for (int j = i; j < this.mDegree; ++j) {
                array[this.mDegree - 1 - i][j] = array[i][i];
            }
            ++i;
        }
        return null;
    }
}
