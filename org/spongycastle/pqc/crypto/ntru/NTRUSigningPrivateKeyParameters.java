package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.params.*;
import java.io.*;
import java.util.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;

public class NTRUSigningPrivateKeyParameters extends AsymmetricKeyParameter
{
    private List<Basis> bases;
    private NTRUSigningPublicKeyParameters publicKey;
    
    public NTRUSigningPrivateKeyParameters(final InputStream inputStream, final NTRUSigningKeyGenerationParameters ntruSigningKeyGenerationParameters) throws IOException {
        super(true);
        this.bases = new ArrayList<Basis>();
        for (int i = 0; i <= ntruSigningKeyGenerationParameters.B; ++i) {
            this.add(new Basis(inputStream, ntruSigningKeyGenerationParameters, i != 0));
        }
        this.publicKey = new NTRUSigningPublicKeyParameters(inputStream, ntruSigningKeyGenerationParameters.getSigningParameters());
    }
    
    public NTRUSigningPrivateKeyParameters(final List<Basis> list, final NTRUSigningPublicKeyParameters publicKey) {
        super(true);
        this.bases = new ArrayList<Basis>(list);
        this.publicKey = publicKey;
    }
    
    public NTRUSigningPrivateKeyParameters(final byte[] array, final NTRUSigningKeyGenerationParameters ntruSigningKeyGenerationParameters) throws IOException {
        this(new ByteArrayInputStream(array), ntruSigningKeyGenerationParameters);
    }
    
    private void add(final Basis basis) {
        this.bases.add(basis);
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
        final NTRUSigningPrivateKeyParameters ntruSigningPrivateKeyParameters = (NTRUSigningPrivateKeyParameters)o;
        if (this.bases == null != (ntruSigningPrivateKeyParameters.bases == null)) {
            return false;
        }
        final List<Basis> bases = this.bases;
        if (bases == null) {
            return true;
        }
        if (bases.size() != ntruSigningPrivateKeyParameters.bases.size()) {
            return false;
        }
        for (int i = 0; i < this.bases.size(); ++i) {
            final Basis basis = this.bases.get(i);
            final Basis basis2 = ntruSigningPrivateKeyParameters.bases.get(i);
            if (!basis.f.equals(basis2.f)) {
                return false;
            }
            if (!basis.fPrime.equals(basis2.fPrime)) {
                return false;
            }
            if (i != 0 && !basis.h.equals(basis2.h)) {
                return false;
            }
            if (!basis.params.equals(basis2.params)) {
                return false;
            }
        }
        return true;
    }
    
    public Basis getBasis(final int n) {
        return this.bases.get(n);
    }
    
    public byte[] getEncoded() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < this.bases.size(); ++i) {
            this.bases.get(i).encode(byteArrayOutputStream, i != 0);
        }
        byteArrayOutputStream.write(this.publicKey.getEncoded());
        return byteArrayOutputStream.toByteArray();
    }
    
    public NTRUSigningPublicKeyParameters getPublicKey() {
        return this.publicKey;
    }
    
    @Override
    public int hashCode() {
        final List<Basis> bases = this.bases;
        if (bases == null) {
            return 31;
        }
        int n = bases.hashCode() + 31;
        final Iterator<Basis> iterator = this.bases.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().hashCode();
        }
        return n;
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        outputStream.write(this.getEncoded());
    }
    
    public static class Basis
    {
        public Polynomial f;
        public Polynomial fPrime;
        public IntegerPolynomial h;
        NTRUSigningKeyGenerationParameters params;
        
        Basis(final InputStream inputStream, final NTRUSigningKeyGenerationParameters params, final boolean b) throws IOException {
            final int n = params.N;
            final int q = params.q;
            final int d1 = params.d1;
            final int d2 = params.d2;
            final int d3 = params.d3;
            final boolean sparse = params.sparse;
            this.params = params;
            Polynomial fromBinary;
            if (params.polyType == 1) {
                fromBinary = ProductFormPolynomial.fromBinary(inputStream, n, d1, d2, d3 + 1, d3);
            }
            else {
                final IntegerPolynomial fromBinary3Tight = IntegerPolynomial.fromBinary3Tight(inputStream, n);
                if (sparse) {
                    fromBinary = new SparseTernaryPolynomial(fromBinary3Tight);
                }
                else {
                    fromBinary = new DenseTernaryPolynomial(fromBinary3Tight);
                }
            }
            this.f = fromBinary;
            Polynomial fPrime;
            if (params.basisType == 0) {
                final IntegerPolynomial fromBinary2 = IntegerPolynomial.fromBinary(inputStream, n, q);
                int n2 = 0;
                while (true) {
                    fPrime = fromBinary2;
                    if (n2 >= fromBinary2.coeffs.length) {
                        break;
                    }
                    final int[] coeffs = fromBinary2.coeffs;
                    coeffs[n2] -= q / 2;
                    ++n2;
                }
            }
            else if (params.polyType == 1) {
                fPrime = ProductFormPolynomial.fromBinary(inputStream, n, d1, d2, d3 + 1, d3);
            }
            else {
                fPrime = IntegerPolynomial.fromBinary3Tight(inputStream, n);
            }
            this.fPrime = fPrime;
            if (b) {
                this.h = IntegerPolynomial.fromBinary(inputStream, n, q);
            }
        }
        
        protected Basis(final Polynomial f, final Polynomial fPrime, final IntegerPolynomial h, final NTRUSigningKeyGenerationParameters params) {
            this.f = f;
            this.fPrime = fPrime;
            this.h = h;
            this.params = params;
        }
        
        private byte[] getEncoded(final Polynomial polynomial) {
            if (polynomial instanceof ProductFormPolynomial) {
                return ((ProductFormPolynomial)polynomial).toBinary();
            }
            return polynomial.toIntegerPolynomial().toBinary3Tight();
        }
        
        void encode(final OutputStream outputStream, final boolean b) throws IOException {
            final int q = this.params.q;
            outputStream.write(this.getEncoded(this.f));
            byte[] array;
            if (this.params.basisType == 0) {
                final IntegerPolynomial integerPolynomial = this.fPrime.toIntegerPolynomial();
                for (int i = 0; i < integerPolynomial.coeffs.length; ++i) {
                    final int[] coeffs = integerPolynomial.coeffs;
                    coeffs[i] += q / 2;
                }
                array = integerPolynomial.toBinary(q);
            }
            else {
                array = this.getEncoded(this.fPrime);
            }
            outputStream.write(array);
            if (b) {
                outputStream.write(this.h.toBinary(q));
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (!(o instanceof Basis)) {
                return false;
            }
            final Basis basis = (Basis)o;
            final Polynomial f = this.f;
            if (f == null) {
                if (basis.f != null) {
                    return false;
                }
            }
            else if (!f.equals(basis.f)) {
                return false;
            }
            final Polynomial fPrime = this.fPrime;
            if (fPrime == null) {
                if (basis.fPrime != null) {
                    return false;
                }
            }
            else if (!fPrime.equals(basis.fPrime)) {
                return false;
            }
            final IntegerPolynomial h = this.h;
            if (h == null) {
                if (basis.h != null) {
                    return false;
                }
            }
            else if (!h.equals(basis.h)) {
                return false;
            }
            final NTRUSigningKeyGenerationParameters params = this.params;
            if (params == null) {
                if (basis.params != null) {
                    return false;
                }
            }
            else if (!params.equals(basis.params)) {
                return false;
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            final Polynomial f = this.f;
            int hashCode = 0;
            int hashCode2;
            if (f == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = f.hashCode();
            }
            final Polynomial fPrime = this.fPrime;
            int hashCode3;
            if (fPrime == null) {
                hashCode3 = 0;
            }
            else {
                hashCode3 = fPrime.hashCode();
            }
            final IntegerPolynomial h = this.h;
            int hashCode4;
            if (h == null) {
                hashCode4 = 0;
            }
            else {
                hashCode4 = h.hashCode();
            }
            final NTRUSigningKeyGenerationParameters params = this.params;
            if (params != null) {
                hashCode = params.hashCode();
            }
            return (((hashCode2 + 31) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
        }
    }
}
