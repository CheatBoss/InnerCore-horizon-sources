package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;
import java.math.*;

public abstract class DSABase extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    protected Digest digest;
    protected DSAEncoder encoder;
    protected DSA signer;
    
    protected DSABase(final Digest digest, final DSA signer, final DSAEncoder encoder) {
        this.digest = digest;
        this.signer = signer;
        this.encoder = encoder;
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        try {
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            return this.encoder.encode(generateSignature[0], generateSignature[1]);
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.digest.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.digest.update(array, n, n2);
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        try {
            final BigInteger[] decode = this.encoder.decode(array);
            return this.signer.verifySignature(array2, decode[0], decode[1]);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
}
