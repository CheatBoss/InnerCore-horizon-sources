package org.spongycastle.pqc.math.ntru.polynomial;

import java.io.*;
import java.security.*;
import org.spongycastle.util.*;

public class ProductFormPolynomial implements Polynomial
{
    private SparseTernaryPolynomial f1;
    private SparseTernaryPolynomial f2;
    private SparseTernaryPolynomial f3;
    
    public ProductFormPolynomial(final SparseTernaryPolynomial f1, final SparseTernaryPolynomial f2, final SparseTernaryPolynomial f3) {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }
    
    public static ProductFormPolynomial fromBinary(final InputStream inputStream, final int n, final int n2, final int n3, final int n4, final int n5) throws IOException {
        return new ProductFormPolynomial(SparseTernaryPolynomial.fromBinary(inputStream, n, n2, n2), SparseTernaryPolynomial.fromBinary(inputStream, n, n3, n3), SparseTernaryPolynomial.fromBinary(inputStream, n, n4, n5));
    }
    
    public static ProductFormPolynomial fromBinary(final byte[] array, final int n, final int n2, final int n3, final int n4, final int n5) throws IOException {
        return fromBinary(new ByteArrayInputStream(array), n, n2, n3, n4, n5);
    }
    
    public static ProductFormPolynomial generateRandom(final int n, final int n2, final int n3, final int n4, final int n5, final SecureRandom secureRandom) {
        return new ProductFormPolynomial(SparseTernaryPolynomial.generateRandom(n, n2, n2, secureRandom), SparseTernaryPolynomial.generateRandom(n, n3, n3, secureRandom), SparseTernaryPolynomial.generateRandom(n, n4, n5, secureRandom));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final ProductFormPolynomial productFormPolynomial = (ProductFormPolynomial)o;
        final SparseTernaryPolynomial f1 = this.f1;
        if (f1 == null) {
            if (productFormPolynomial.f1 != null) {
                return false;
            }
        }
        else if (!f1.equals(productFormPolynomial.f1)) {
            return false;
        }
        final SparseTernaryPolynomial f2 = this.f2;
        if (f2 == null) {
            if (productFormPolynomial.f2 != null) {
                return false;
            }
        }
        else if (!f2.equals(productFormPolynomial.f2)) {
            return false;
        }
        final SparseTernaryPolynomial f3 = this.f3;
        if (f3 == null) {
            if (productFormPolynomial.f3 != null) {
                return false;
            }
        }
        else if (!f3.equals(productFormPolynomial.f3)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final SparseTernaryPolynomial f1 = this.f1;
        int hashCode = 0;
        int hashCode2;
        if (f1 == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = f1.hashCode();
        }
        final SparseTernaryPolynomial f2 = this.f2;
        int hashCode3;
        if (f2 == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = f2.hashCode();
        }
        final SparseTernaryPolynomial f3 = this.f3;
        if (f3 != null) {
            hashCode = f3.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode;
    }
    
    @Override
    public BigIntPolynomial mult(final BigIntPolynomial bigIntPolynomial) {
        final BigIntPolynomial mult = this.f2.mult(this.f1.mult(bigIntPolynomial));
        mult.add(this.f3.mult(bigIntPolynomial));
        return mult;
    }
    
    @Override
    public IntegerPolynomial mult(final IntegerPolynomial integerPolynomial) {
        final IntegerPolynomial mult = this.f2.mult(this.f1.mult(integerPolynomial));
        mult.add(this.f3.mult(integerPolynomial));
        return mult;
    }
    
    @Override
    public IntegerPolynomial mult(IntegerPolynomial mult, final int n) {
        mult = this.mult(mult);
        mult.mod(n);
        return mult;
    }
    
    public byte[] toBinary() {
        final byte[] binary = this.f1.toBinary();
        final byte[] binary2 = this.f2.toBinary();
        final byte[] binary3 = this.f3.toBinary();
        final byte[] copy = Arrays.copyOf(binary, binary.length + binary2.length + binary3.length);
        System.arraycopy(binary2, 0, copy, binary.length, binary2.length);
        System.arraycopy(binary3, 0, copy, binary.length + binary2.length, binary3.length);
        return copy;
    }
    
    @Override
    public IntegerPolynomial toIntegerPolynomial() {
        final IntegerPolynomial mult = this.f1.mult(this.f2.toIntegerPolynomial());
        mult.add(this.f3.toIntegerPolynomial());
        return mult;
    }
}
