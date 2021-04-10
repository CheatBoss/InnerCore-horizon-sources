package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;
import java.io.*;

public class NTRUSigningPublicKeyParameters extends AsymmetricKeyParameter
{
    public IntegerPolynomial h;
    private NTRUSigningParameters params;
    
    public NTRUSigningPublicKeyParameters(final InputStream inputStream, final NTRUSigningParameters params) throws IOException {
        super(false);
        this.h = IntegerPolynomial.fromBinary(inputStream, params.N, params.q);
        this.params = params;
    }
    
    public NTRUSigningPublicKeyParameters(final IntegerPolynomial h, final NTRUSigningParameters params) {
        super(false);
        this.h = h;
        this.params = params;
    }
    
    public NTRUSigningPublicKeyParameters(final byte[] array, final NTRUSigningParameters params) {
        super(false);
        this.h = IntegerPolynomial.fromBinary(array, params.N, params.q);
        this.params = params;
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
        final NTRUSigningPublicKeyParameters ntruSigningPublicKeyParameters = (NTRUSigningPublicKeyParameters)o;
        final IntegerPolynomial h = this.h;
        if (h == null) {
            if (ntruSigningPublicKeyParameters.h != null) {
                return false;
            }
        }
        else if (!h.equals(ntruSigningPublicKeyParameters.h)) {
            return false;
        }
        final NTRUSigningParameters params = this.params;
        if (params == null) {
            if (ntruSigningPublicKeyParameters.params != null) {
                return false;
            }
        }
        else if (!params.equals(ntruSigningPublicKeyParameters.params)) {
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
        final NTRUSigningParameters params = this.params;
        if (params != null) {
            hashCode = params.hashCode();
        }
        return (hashCode2 + 31) * 31 + hashCode;
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        outputStream.write(this.getEncoded());
    }
}
