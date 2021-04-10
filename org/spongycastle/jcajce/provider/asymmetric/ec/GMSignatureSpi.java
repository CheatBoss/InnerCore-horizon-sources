package org.spongycastle.jcajce.provider.asymmetric.ec;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import java.math.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;

public class GMSignatureSpi extends DSABase
{
    GMSignatureSpi(final Digest digest, final DSA dsa, final DSAEncoder dsaEncoder) {
        super(digest, dsa, dsaEncoder);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        final AsymmetricKeyParameter generatePrivateKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        this.digest.reset();
        if (this.appRandom != null) {
            this.signer.init(true, new ParametersWithRandom(generatePrivateKeyParameter, this.appRandom));
            return;
        }
        this.signer.init(true, generatePrivateKeyParameter);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        final AsymmetricKeyParameter generatePublicKeyParameter = ECUtils.generatePublicKeyParameter(publicKey);
        this.digest.reset();
        this.signer.init(false, generatePublicKeyParameter);
    }
    
    private static class StdDSAEncoder implements DSAEncoder
    {
        @Override
        public BigInteger[] decode(final byte[] array) throws IOException {
            final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(array);
            if (asn1Sequence.size() != 2) {
                throw new IOException("malformed signature");
            }
            if (Arrays.areEqual(array, asn1Sequence.getEncoded("DER"))) {
                return new BigInteger[] { ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue(), ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getValue() };
            }
            throw new IOException("malformed signature");
        }
        
        @Override
        public byte[] encode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(new ASN1Integer(bigInteger));
            asn1EncodableVector.add(new ASN1Integer(bigInteger2));
            return new DERSequence(asn1EncodableVector).getEncoded("DER");
        }
    }
    
    public static class sm3WithSM2 extends GMSignatureSpi
    {
        public sm3WithSM2() {
            super(new SM3Digest(), new SM2Signer(), new StdDSAEncoder());
        }
    }
}
