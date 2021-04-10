package org.spongycastle.pqc.math.linearalgebra;

public class GF2nPolynomial
{
    private GF2nElement[] coeff;
    private int size;
    
    private GF2nPolynomial(final int size) {
        this.size = size;
        this.coeff = new GF2nElement[size];
    }
    
    public GF2nPolynomial(int i, final GF2nElement gf2nElement) {
        this.size = i;
        this.coeff = new GF2nElement[i];
        for (i = 0; i < this.size; ++i) {
            this.coeff[i] = (GF2nElement)gf2nElement.clone();
        }
    }
    
    public GF2nPolynomial(final GF2Polynomial gf2Polynomial, final GF2nField gf2nField) {
        final int size = gf2nField.getDegree() + 1;
        this.size = size;
        this.coeff = new GF2nElement[size];
        final boolean b = gf2nField instanceof GF2nONBField;
        final int n = 0;
        int i = 0;
        if (b) {
            while (i < this.size) {
                if (gf2Polynomial.testBit(i)) {
                    this.coeff[i] = GF2nONBElement.ONE((GF2nONBField)gf2nField);
                }
                else {
                    this.coeff[i] = GF2nONBElement.ZERO((GF2nONBField)gf2nField);
                }
                ++i;
            }
        }
        else {
            if (!(gf2nField instanceof GF2nPolynomialField)) {
                throw new IllegalArgumentException("PolynomialGF2n(Bitstring, GF2nField): B1 must be an instance of GF2nONBField or GF2nPolynomialField!");
            }
            for (int j = n; j < this.size; ++j) {
                if (gf2Polynomial.testBit(j)) {
                    this.coeff[j] = GF2nPolynomialElement.ONE((GF2nPolynomialField)gf2nField);
                }
                else {
                    this.coeff[j] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)gf2nField);
                }
            }
        }
    }
    
    public GF2nPolynomial(final GF2nPolynomial gf2nPolynomial) {
        final int size = gf2nPolynomial.size;
        this.coeff = new GF2nElement[size];
        this.size = size;
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i] = (GF2nElement)gf2nPolynomial.coeff[i].clone();
        }
    }
    
    public final GF2nPolynomial add(final GF2nPolynomial gf2nPolynomial) throws RuntimeException {
        final int size = this.size();
        final int size2 = gf2nPolynomial.size();
        final int n = 0;
        int n2 = 0;
        GF2nPolynomial gf2nPolynomial3;
        if (size >= size2) {
            final GF2nPolynomial gf2nPolynomial2 = new GF2nPolynomial(this.size());
            int n3;
            while (true) {
                n3 = n2;
                if (n2 >= gf2nPolynomial.size()) {
                    break;
                }
                gf2nPolynomial2.coeff[n2] = (GF2nElement)this.coeff[n2].add(gf2nPolynomial.coeff[n2]);
                ++n2;
            }
            while (true) {
                gf2nPolynomial3 = gf2nPolynomial2;
                if (n3 >= this.size()) {
                    break;
                }
                gf2nPolynomial2.coeff[n3] = this.coeff[n3];
                ++n3;
            }
        }
        else {
            final GF2nPolynomial gf2nPolynomial4 = new GF2nPolynomial(gf2nPolynomial.size());
            int n4 = n;
            int n5;
            while (true) {
                n5 = n4;
                if (n4 >= this.size()) {
                    break;
                }
                gf2nPolynomial4.coeff[n4] = (GF2nElement)this.coeff[n4].add(gf2nPolynomial.coeff[n4]);
                ++n4;
            }
            while (true) {
                gf2nPolynomial3 = gf2nPolynomial4;
                if (n5 >= gf2nPolynomial.size()) {
                    break;
                }
                gf2nPolynomial4.coeff[n5] = gf2nPolynomial.coeff[n5];
                ++n5;
            }
        }
        return gf2nPolynomial3;
    }
    
    public final void assignZeroToElements() {
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i].assignZero();
        }
    }
    
    public final GF2nElement at(final int n) {
        return this.coeff[n];
    }
    
    public final GF2nPolynomial[] divide(GF2nPolynomial gf2nPolynomial) throws RuntimeException, ArithmeticException {
        final GF2nPolynomial[] array = new GF2nPolynomial[2];
        GF2nPolynomial add = new GF2nPolynomial(this);
        add.shrink();
        final int degree = gf2nPolynomial.getDegree();
        final GF2nElement gf2nElement = (GF2nElement)gf2nPolynomial.coeff[degree].invert();
        if (add.getDegree() < degree) {
            (array[0] = new GF2nPolynomial(this)).assignZeroToElements();
            array[0].shrink();
            array[1] = new GF2nPolynomial(this);
            gf2nPolynomial = array[1];
        }
        else {
            (array[0] = new GF2nPolynomial(this)).assignZeroToElements();
            while (true) {
                final int n = add.getDegree() - degree;
                if (n < 0) {
                    break;
                }
                final GF2nElement gf2nElement2 = (GF2nElement)add.coeff[add.getDegree()].multiply(gf2nElement);
                final GF2nPolynomial scalarMultiply = gf2nPolynomial.scalarMultiply(gf2nElement2);
                scalarMultiply.shiftThisLeft(n);
                add = add.add(scalarMultiply);
                add.shrink();
                array[0].coeff[n] = (GF2nElement)gf2nElement2.clone();
            }
            array[1] = add;
            gf2nPolynomial = array[0];
        }
        gf2nPolynomial.shrink();
        return array;
    }
    
    public final void enlarge(final int size) {
        final int size2 = this.size;
        if (size <= size2) {
            return;
        }
        final GF2nElement[] coeff = new GF2nElement[size];
        System.arraycopy(this.coeff, 0, coeff, 0, size2);
        final GF2nField field = this.coeff[0].getField();
        final GF2nElement[] coeff2 = this.coeff;
        if (coeff2[0] instanceof GF2nPolynomialElement) {
            for (int i = this.size; i < size; ++i) {
                coeff[i] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)field);
            }
        }
        else if (coeff2[0] instanceof GF2nONBElement) {
            for (int j = this.size; j < size; ++j) {
                coeff[j] = GF2nONBElement.ZERO((GF2nONBField)field);
            }
        }
        this.size = size;
        this.coeff = coeff;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof GF2nPolynomial)) {
            return false;
        }
        final GF2nPolynomial gf2nPolynomial = (GF2nPolynomial)o;
        if (this.getDegree() != gf2nPolynomial.getDegree()) {
            return false;
        }
        for (int i = 0; i < this.size; ++i) {
            if (!this.coeff[i].equals(gf2nPolynomial.coeff[i])) {
                return false;
            }
        }
        return true;
    }
    
    public final GF2nPolynomial gcd(GF2nPolynomial gf2nPolynomial) throws RuntimeException, ArithmeticException {
        GF2nPolynomial gf2nPolynomial2 = new GF2nPolynomial(this);
        gf2nPolynomial = new GF2nPolynomial(gf2nPolynomial);
        gf2nPolynomial2.shrink();
        gf2nPolynomial.shrink();
        while (!gf2nPolynomial.isZero()) {
            final GF2nPolynomial remainder = gf2nPolynomial2.remainder(gf2nPolynomial);
            gf2nPolynomial2 = gf2nPolynomial;
            gf2nPolynomial = remainder;
        }
        return gf2nPolynomial2.scalarMultiply((GF2nElement)gf2nPolynomial2.coeff[gf2nPolynomial2.getDegree()].invert());
    }
    
    public final int getDegree() {
        int size = this.size;
        int n;
        do {
            n = size - 1;
            if (n < 0) {
                return -1;
            }
            size = n;
        } while (this.coeff[n].isZero());
        return n;
    }
    
    @Override
    public int hashCode() {
        return this.getDegree() + this.coeff.hashCode();
    }
    
    public final boolean isZero() {
        for (int i = 0; i < this.size; ++i) {
            final GF2nElement[] coeff = this.coeff;
            if (coeff[i] != null && !coeff[i].isZero()) {
                return false;
            }
        }
        return true;
    }
    
    public final GF2nPolynomial multiply(final GF2nPolynomial gf2nPolynomial) throws RuntimeException {
        final int size = this.size();
        if (size == gf2nPolynomial.size()) {
            final GF2nPolynomial gf2nPolynomial2 = new GF2nPolynomial((size << 1) - 1);
            for (int i = 0; i < this.size(); ++i) {
                for (int j = 0; j < gf2nPolynomial.size(); ++j) {
                    final GF2nElement[] coeff = gf2nPolynomial2.coeff;
                    final int n = i + j;
                    if (coeff[n] == null) {
                        coeff[n] = (GF2nElement)this.coeff[i].multiply(gf2nPolynomial.coeff[j]);
                    }
                    else {
                        coeff[n] = (GF2nElement)coeff[n].add(this.coeff[i].multiply(gf2nPolynomial.coeff[j]));
                    }
                }
            }
            return gf2nPolynomial2;
        }
        throw new IllegalArgumentException("PolynomialGF2n.multiply: this and b must have the same size!");
    }
    
    public final GF2nPolynomial multiplyAndReduce(final GF2nPolynomial gf2nPolynomial, final GF2nPolynomial gf2nPolynomial2) throws RuntimeException, ArithmeticException {
        return this.multiply(gf2nPolynomial).reduce(gf2nPolynomial2);
    }
    
    public final GF2nPolynomial quotient(final GF2nPolynomial gf2nPolynomial) throws RuntimeException, ArithmeticException {
        return this.divide(gf2nPolynomial)[0];
    }
    
    public final GF2nPolynomial reduce(final GF2nPolynomial gf2nPolynomial) throws RuntimeException, ArithmeticException {
        return this.remainder(gf2nPolynomial);
    }
    
    public final GF2nPolynomial remainder(final GF2nPolynomial gf2nPolynomial) throws RuntimeException, ArithmeticException {
        return this.divide(gf2nPolynomial)[1];
    }
    
    public final GF2nPolynomial scalarMultiply(final GF2nElement gf2nElement) throws RuntimeException {
        final GF2nPolynomial gf2nPolynomial = new GF2nPolynomial(this.size());
        for (int i = 0; i < this.size(); ++i) {
            gf2nPolynomial.coeff[i] = (GF2nElement)this.coeff[i].multiply(gf2nElement);
        }
        return gf2nPolynomial;
    }
    
    public final void set(final int n, final GF2nElement gf2nElement) {
        if (!(gf2nElement instanceof GF2nPolynomialElement) && !(gf2nElement instanceof GF2nONBElement)) {
            throw new IllegalArgumentException("PolynomialGF2n.set f must be an instance of either GF2nPolynomialElement or GF2nONBElement!");
        }
        this.coeff[n] = (GF2nElement)gf2nElement.clone();
    }
    
    public final GF2nPolynomial shiftLeft(final int n) {
        if (n <= 0) {
            return new GF2nPolynomial(this);
        }
        final int size = this.size;
        final GF2nElement[] coeff = this.coeff;
        int i = 0;
        final GF2nPolynomial gf2nPolynomial = new GF2nPolynomial(size + n, coeff[0]);
        gf2nPolynomial.assignZeroToElements();
        while (i < this.size) {
            gf2nPolynomial.coeff[i + n] = this.coeff[i];
            ++i;
        }
        return gf2nPolynomial;
    }
    
    public final void shiftThisLeft(int n) {
        if (n > 0) {
            int size = this.size;
            final GF2nField field = this.coeff[0].getField();
            this.enlarge(this.size + n);
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                final GF2nElement[] coeff = this.coeff;
                coeff[size + n] = coeff[size];
            }
            final GF2nElement[] coeff2 = this.coeff;
            if (coeff2[0] instanceof GF2nPolynomialElement) {
                while (true) {
                    --n;
                    if (n < 0) {
                        break;
                    }
                    this.coeff[n] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)field);
                }
            }
            else if (coeff2[0] instanceof GF2nONBElement) {
                while (true) {
                    --n;
                    if (n < 0) {
                        break;
                    }
                    this.coeff[n] = GF2nONBElement.ZERO((GF2nONBField)field);
                }
            }
        }
    }
    
    public final void shrink() {
        int size = this.size;
        do {
            --size;
        } while (this.coeff[size].isZero() && size > 0);
        final int size2 = size + 1;
        if (size2 < this.size) {
            final GF2nElement[] coeff = new GF2nElement[size2];
            System.arraycopy(this.coeff, 0, coeff, 0, size2);
            this.coeff = coeff;
            this.size = size2;
        }
    }
    
    public final int size() {
        return this.size;
    }
}
