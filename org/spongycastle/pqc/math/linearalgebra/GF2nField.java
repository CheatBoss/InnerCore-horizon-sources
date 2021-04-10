package org.spongycastle.pqc.math.linearalgebra;

import java.util.*;
import java.security.*;

public abstract class GF2nField
{
    protected GF2Polynomial fieldPolynomial;
    protected Vector fields;
    protected int mDegree;
    protected Vector matrices;
    protected final SecureRandom random;
    
    protected GF2nField(final SecureRandom random) {
        this.random = random;
    }
    
    protected abstract void computeCOBMatrix(final GF2nField p0);
    
    protected abstract void computeFieldPolynomial();
    
    public final GF2nElement convert(GF2nElement gf2nElement, final GF2nField gf2nField) throws RuntimeException {
        if (gf2nField == this || this.fieldPolynomial.equals(gf2nField.fieldPolynomial)) {
            return (GF2nElement)gf2nElement.clone();
        }
        if (this.mDegree != gf2nField.mDegree) {
            throw new RuntimeException("GF2nField.convert: B1 has a different degree and thus cannot be coverted to!");
        }
        int n;
        if ((n = this.fields.indexOf(gf2nField)) == -1) {
            this.computeCOBMatrix(gf2nField);
            n = this.fields.indexOf(gf2nField);
        }
        final GF2Polynomial[] array = this.matrices.elementAt(n);
        gf2nElement = (GF2nElement)gf2nElement.clone();
        if (gf2nElement instanceof GF2nONBElement) {
            ((GF2nONBElement)gf2nElement).reverseOrder();
        }
        final GF2Polynomial gf2Polynomial = new GF2Polynomial(this.mDegree, gf2nElement.toFlexiBigInt());
        gf2Polynomial.expandN(this.mDegree);
        final GF2Polynomial gf2Polynomial2 = new GF2Polynomial(this.mDegree);
        for (int i = 0; i < this.mDegree; ++i) {
            if (gf2Polynomial.vectorMult(array[i])) {
                gf2Polynomial2.setBit(this.mDegree - 1 - i);
            }
        }
        if (gf2nField instanceof GF2nPolynomialField) {
            return new GF2nPolynomialElement((GF2nPolynomialField)gf2nField, gf2Polynomial2);
        }
        if (gf2nField instanceof GF2nONBField) {
            final GF2nONBElement gf2nONBElement = new GF2nONBElement((GF2nONBField)gf2nField, gf2Polynomial2.toFlexiBigInt());
            gf2nONBElement.reverseOrder();
            return gf2nONBElement;
        }
        throw new RuntimeException("GF2nField.convert: B1 must be an instance of GF2nPolynomialField or GF2nONBField!");
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GF2nField)) {
            return false;
        }
        final GF2nField gf2nField = (GF2nField)o;
        return gf2nField.mDegree == this.mDegree && this.fieldPolynomial.equals(gf2nField.fieldPolynomial) && (!(this instanceof GF2nPolynomialField) || gf2nField instanceof GF2nPolynomialField) && (!(this instanceof GF2nONBField) || gf2nField instanceof GF2nONBField);
    }
    
    public final int getDegree() {
        return this.mDegree;
    }
    
    public final GF2Polynomial getFieldPolynomial() {
        if (this.fieldPolynomial == null) {
            this.computeFieldPolynomial();
        }
        return new GF2Polynomial(this.fieldPolynomial);
    }
    
    protected abstract GF2nElement getRandomRoot(final GF2Polynomial p0);
    
    @Override
    public int hashCode() {
        return this.mDegree + this.fieldPolynomial.hashCode();
    }
    
    protected final GF2Polynomial[] invertMatrix(final GF2Polynomial[] array) {
        final GF2Polynomial[] array2 = new GF2Polynomial[array.length];
        final GF2Polynomial[] array3 = new GF2Polynomial[array.length];
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= this.mDegree) {
                break;
            }
            try {
                array2[n2] = new GF2Polynomial(array[n2]);
                (array3[n2] = new GF2Polynomial(this.mDegree)).setBit(this.mDegree - 1 - n2);
            }
            catch (RuntimeException ex) {
                ex.printStackTrace();
            }
            ++n2;
        }
        int j;
        while (i < (j = this.mDegree - 1)) {
            int n3 = i;
            while (true) {
                final int mDegree = this.mDegree;
                if (n3 >= mDegree || array2[n3].testBit(mDegree - 1 - i)) {
                    break;
                }
                ++n3;
            }
            if (n3 >= this.mDegree) {
                throw new RuntimeException("GF2nField.invertMatrix: Matrix cannot be inverted!");
            }
            if (i != n3) {
                final GF2Polynomial gf2Polynomial = array2[i];
                array2[i] = array2[n3];
                array2[n3] = gf2Polynomial;
                final GF2Polynomial gf2Polynomial2 = array3[i];
                array3[i] = array3[n3];
                array3[n3] = gf2Polynomial2;
            }
            int n5;
            final int n4 = n5 = i + 1;
            while (true) {
                final int mDegree2 = this.mDegree;
                if (n5 >= mDegree2) {
                    break;
                }
                if (array2[n5].testBit(mDegree2 - 1 - i)) {
                    array2[n5].addToThis(array2[i]);
                    array3[n5].addToThis(array3[i]);
                }
                ++n5;
            }
            i = n4;
        }
        while (j > 0) {
            int k;
            int n6;
            for (n6 = (k = j - 1); k >= 0; --k) {
                if (array2[k].testBit(this.mDegree - 1 - j)) {
                    array2[k].addToThis(array2[j]);
                    array3[k].addToThis(array3[j]);
                }
            }
            j = n6;
        }
        return array3;
    }
}
