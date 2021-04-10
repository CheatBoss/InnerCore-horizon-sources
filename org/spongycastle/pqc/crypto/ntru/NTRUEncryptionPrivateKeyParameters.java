package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.pqc.math.ntru.polynomial.*;
import java.io.*;

public class NTRUEncryptionPrivateKeyParameters extends NTRUEncryptionKeyParameters
{
    public IntegerPolynomial fp;
    public IntegerPolynomial h;
    public Polynomial t;
    
    public NTRUEncryptionPrivateKeyParameters(final InputStream inputStream, final NTRUEncryptionParameters ntruEncryptionParameters) throws IOException {
        super(true, ntruEncryptionParameters);
        if (ntruEncryptionParameters.polyType == 1) {
            final int n = ntruEncryptionParameters.N;
            final int df1 = ntruEncryptionParameters.df1;
            final int df2 = ntruEncryptionParameters.df2;
            final int df3 = ntruEncryptionParameters.df3;
            int df4;
            if (ntruEncryptionParameters.fastFp) {
                df4 = ntruEncryptionParameters.df3;
            }
            else {
                df4 = ntruEncryptionParameters.df3 - 1;
            }
            this.h = IntegerPolynomial.fromBinary(inputStream, ntruEncryptionParameters.N, ntruEncryptionParameters.q);
            this.t = ProductFormPolynomial.fromBinary(inputStream, n, df1, df2, df3, df4);
        }
        else {
            this.h = IntegerPolynomial.fromBinary(inputStream, ntruEncryptionParameters.N, ntruEncryptionParameters.q);
            final IntegerPolynomial fromBinary3Tight = IntegerPolynomial.fromBinary3Tight(inputStream, ntruEncryptionParameters.N);
            TernaryPolynomial t;
            if (ntruEncryptionParameters.sparse) {
                t = new SparseTernaryPolynomial(fromBinary3Tight);
            }
            else {
                t = new DenseTernaryPolynomial(fromBinary3Tight);
            }
            this.t = t;
        }
        this.init();
    }
    
    public NTRUEncryptionPrivateKeyParameters(final IntegerPolynomial h, final Polynomial t, final IntegerPolynomial fp, final NTRUEncryptionParameters ntruEncryptionParameters) {
        super(true, ntruEncryptionParameters);
        this.h = h;
        this.t = t;
        this.fp = fp;
    }
    
    public NTRUEncryptionPrivateKeyParameters(final byte[] array, final NTRUEncryptionParameters ntruEncryptionParameters) throws IOException {
        this(new ByteArrayInputStream(array), ntruEncryptionParameters);
    }
    
    private void init() {
        if (this.params.fastFp) {
            final IntegerPolynomial fp = new IntegerPolynomial(this.params.N);
            this.fp = fp;
            fp.coeffs[0] = 1;
            return;
        }
        this.fp = this.t.toIntegerPolynomial().invertF3();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof NTRUEncryptionPrivateKeyParameters)) {
            return false;
        }
        final NTRUEncryptionPrivateKeyParameters ntruEncryptionPrivateKeyParameters = (NTRUEncryptionPrivateKeyParameters)o;
        if (this.params == null) {
            if (ntruEncryptionPrivateKeyParameters.params != null) {
                return false;
            }
        }
        else if (!this.params.equals(ntruEncryptionPrivateKeyParameters.params)) {
            return false;
        }
        final Polynomial t = this.t;
        if (t == null) {
            if (ntruEncryptionPrivateKeyParameters.t != null) {
                return false;
            }
        }
        else if (!t.equals(ntruEncryptionPrivateKeyParameters.t)) {
            return false;
        }
        return this.h.equals(ntruEncryptionPrivateKeyParameters.h);
    }
    
    public byte[] getEncoded() {
        final byte[] binary = this.h.toBinary(this.params.q);
        final Polynomial t = this.t;
        byte[] array;
        if (t instanceof ProductFormPolynomial) {
            array = ((ProductFormPolynomial)t).toBinary();
        }
        else {
            array = t.toIntegerPolynomial().toBinary3Tight();
        }
        final byte[] array2 = new byte[binary.length + array.length];
        System.arraycopy(binary, 0, array2, 0, binary.length);
        System.arraycopy(array, 0, array2, binary.length, array.length);
        return array2;
    }
    
    @Override
    public int hashCode() {
        final NTRUEncryptionParameters params = this.params;
        int hashCode = 0;
        int hashCode2;
        if (params == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.params.hashCode();
        }
        final Polynomial t = this.t;
        int hashCode3;
        if (t == null) {
            hashCode3 = 0;
        }
        else {
            hashCode3 = t.hashCode();
        }
        final IntegerPolynomial h = this.h;
        if (h != null) {
            hashCode = h.hashCode();
        }
        return ((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode;
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        outputStream.write(this.getEncoded());
    }
}
