package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.pqc.math.ntru.polynomial.*;
import java.io.*;

public class NTRUEncryptionPublicKeyParameters extends NTRUEncryptionKeyParameters
{
    public IntegerPolynomial h;
    
    public NTRUEncryptionPublicKeyParameters(final InputStream inputStream, final NTRUEncryptionParameters ntruEncryptionParameters) throws IOException {
        super(false, ntruEncryptionParameters);
        this.h = IntegerPolynomial.fromBinary(inputStream, ntruEncryptionParameters.N, ntruEncryptionParameters.q);
    }
    
    public NTRUEncryptionPublicKeyParameters(final IntegerPolynomial h, final NTRUEncryptionParameters ntruEncryptionParameters) {
        super(false, ntruEncryptionParameters);
        this.h = h;
    }
    
    public NTRUEncryptionPublicKeyParameters(final byte[] array, final NTRUEncryptionParameters ntruEncryptionParameters) {
        super(false, ntruEncryptionParameters);
        this.h = IntegerPolynomial.fromBinary(array, ntruEncryptionParameters.N, ntruEncryptionParameters.q);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof NTRUEncryptionPublicKeyParameters)) {
            return false;
        }
        final NTRUEncryptionPublicKeyParameters ntruEncryptionPublicKeyParameters = (NTRUEncryptionPublicKeyParameters)o;
        final IntegerPolynomial h = this.h;
        if (h == null) {
            if (ntruEncryptionPublicKeyParameters.h != null) {
                return false;
            }
        }
        else if (!h.equals(ntruEncryptionPublicKeyParameters.h)) {
            return false;
        }
        if (this.params == null) {
            if (ntruEncryptionPublicKeyParameters.params != null) {
                return false;
            }
        }
        else if (!this.params.equals(ntruEncryptionPublicKeyParameters.params)) {
            return false;
        }
        return true;
    }
    
    public byte[] getEncoded() {
        return this.h.toBinary(this.params.q);
    }
    
    @Override
    public int hashCode() {
        final IntegerPolynomial h = this.h;
        int hashCode = 0;
        int hashCode2;
        if (h == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = h.hashCode();
        }
        if (this.params != null) {
            hashCode = this.params.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        outputStream.write(this.getEncoded());
    }
}
