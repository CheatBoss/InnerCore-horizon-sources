package org.spongycastle.math.field;

import java.math.*;
import org.spongycastle.util.*;

class GenericPolynomialExtensionField implements PolynomialExtensionField
{
    protected final Polynomial minimalPolynomial;
    protected final FiniteField subfield;
    
    GenericPolynomialExtensionField(final FiniteField subfield, final Polynomial minimalPolynomial) {
        this.subfield = subfield;
        this.minimalPolynomial = minimalPolynomial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenericPolynomialExtensionField)) {
            return false;
        }
        final GenericPolynomialExtensionField genericPolynomialExtensionField = (GenericPolynomialExtensionField)o;
        return this.subfield.equals(genericPolynomialExtensionField.subfield) && this.minimalPolynomial.equals(genericPolynomialExtensionField.minimalPolynomial);
    }
    
    @Override
    public BigInteger getCharacteristic() {
        return this.subfield.getCharacteristic();
    }
    
    @Override
    public int getDegree() {
        return this.minimalPolynomial.getDegree();
    }
    
    @Override
    public int getDimension() {
        return this.subfield.getDimension() * this.minimalPolynomial.getDegree();
    }
    
    @Override
    public Polynomial getMinimalPolynomial() {
        return this.minimalPolynomial;
    }
    
    @Override
    public FiniteField getSubfield() {
        return this.subfield;
    }
    
    @Override
    public int hashCode() {
        return this.subfield.hashCode() ^ Integers.rotateLeft(this.minimalPolynomial.hashCode(), 16);
    }
}
