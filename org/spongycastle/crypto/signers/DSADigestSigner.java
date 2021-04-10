package org.spongycastle.crypto.signers;

import java.math.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DSADigestSigner implements Signer
{
    private final Digest digest;
    private final DSA dsaSigner;
    private boolean forSigning;
    
    public DSADigestSigner(final DSA dsaSigner, final Digest digest) {
        this.digest = digest;
        this.dsaSigner = dsaSigner;
    }
    
    private BigInteger[] derDecode(final byte[] array) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(array);
        return new BigInteger[] { ((ASN1Integer)asn1Sequence.getObjectAt(0)).getValue(), ((ASN1Integer)asn1Sequence.getObjectAt(1)).getValue() };
    }
    
    private byte[] derEncode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(bigInteger));
        asn1EncodableVector.add(new ASN1Integer(bigInteger2));
        return new DERSequence(asn1EncodableVector).getEncoded("DER");
    }
    
    @Override
    public byte[] generateSignature() {
        if (this.forSigning) {
            final byte[] array = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(array, 0);
            final BigInteger[] generateSignature = this.dsaSigner.generateSignature(array);
            try {
                return this.derEncode(generateSignature[0], generateSignature[1]);
            }
            catch (IOException ex) {
                throw new IllegalStateException("unable to encode signature");
            }
        }
        throw new IllegalStateException("DSADigestSigner not initialised for signature generation.");
    }
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            asymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        if (forSigning && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Signing Requires Private Key.");
        }
        if (!forSigning && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Verification Requires Public Key.");
        }
        this.reset();
        this.dsaSigner.init(forSigning, cipherParameters);
    }
    
    @Override
    public void reset() {
        this.digest.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public boolean verifySignature(final byte[] array) {
        if (!this.forSigning) {
            final byte[] array2 = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(array2, 0);
            try {
                final BigInteger[] derDecode = this.derDecode(array);
                return this.dsaSigner.verifySignature(array2, derDecode[0], derDecode[1]);
            }
            catch (IOException ex) {
                return false;
            }
        }
        throw new IllegalStateException("DSADigestSigner not initialised for verification");
    }
}
