package org.spongycastle.jcajce.provider.asymmetric.ec;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import java.math.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;

public class SignatureSpi extends DSABase
{
    SignatureSpi(final Digest digest, final DSA dsa, final DSAEncoder dsaEncoder) {
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
    
    private static class PlainDSAEncoder implements DSAEncoder
    {
        private byte[] makeUnsigned(final BigInteger bigInteger) {
            final byte[] byteArray = bigInteger.toByteArray();
            if (byteArray[0] == 0) {
                final int n = byteArray.length - 1;
                final byte[] array = new byte[n];
                System.arraycopy(byteArray, 1, array, 0, n);
                return array;
            }
            return byteArray;
        }
        
        @Override
        public BigInteger[] decode(final byte[] array) throws IOException {
            final int n = array.length / 2;
            final byte[] array2 = new byte[n];
            final int n2 = array.length / 2;
            final byte[] array3 = new byte[n2];
            System.arraycopy(array, 0, array2, 0, n);
            System.arraycopy(array, n, array3, 0, n2);
            return new BigInteger[] { new BigInteger(1, array2), new BigInteger(1, array3) };
        }
        
        @Override
        public byte[] encode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
            final byte[] unsigned = this.makeUnsigned(bigInteger);
            final byte[] unsigned2 = this.makeUnsigned(bigInteger2);
            int n;
            if (unsigned.length > unsigned2.length) {
                n = unsigned.length;
            }
            else {
                n = unsigned2.length;
            }
            final byte[] array = new byte[n * 2];
            System.arraycopy(unsigned, 0, array, array.length / 2 - unsigned.length, unsigned.length);
            System.arraycopy(unsigned2, 0, array, array.length - unsigned2.length, unsigned2.length);
            return array;
        }
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
    
    public static class ecCVCDSA extends SignatureSpi
    {
        public ecCVCDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
    
    public static class ecCVCDSA224 extends SignatureSpi
    {
        public ecCVCDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
    
    public static class ecCVCDSA256 extends SignatureSpi
    {
        public ecCVCDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
    
    public static class ecCVCDSA384 extends SignatureSpi
    {
        public ecCVCDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
    
    public static class ecCVCDSA512 extends SignatureSpi
    {
        public ecCVCDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
    
    public static class ecDSA extends SignatureSpi
    {
        public ecDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA224 extends SignatureSpi
    {
        public ecDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA256 extends SignatureSpi
    {
        public ecDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA384 extends SignatureSpi
    {
        public ecDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA512 extends SignatureSpi
    {
        public ecDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSARipeMD160 extends SignatureSpi
    {
        public ecDSARipeMD160() {
            super(new RIPEMD160Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSASha3_224 extends SignatureSpi
    {
        public ecDSASha3_224() {
            super(DigestFactory.createSHA3_224(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSASha3_256 extends SignatureSpi
    {
        public ecDSASha3_256() {
            super(DigestFactory.createSHA3_256(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSASha3_384 extends SignatureSpi
    {
        public ecDSASha3_384() {
            super(DigestFactory.createSHA3_384(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSASha3_512 extends SignatureSpi
    {
        public ecDSASha3_512() {
            super(DigestFactory.createSHA3_512(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSAnone extends SignatureSpi
    {
        public ecDSAnone() {
            super(new NullDigest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSA extends SignatureSpi
    {
        public ecDetDSA() {
            super(DigestFactory.createSHA1(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA1())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSA224 extends SignatureSpi
    {
        public ecDetDSA224() {
            super(DigestFactory.createSHA224(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA224())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSA256 extends SignatureSpi
    {
        public ecDetDSA256() {
            super(DigestFactory.createSHA256(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA256())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSA384 extends SignatureSpi
    {
        public ecDetDSA384() {
            super(DigestFactory.createSHA384(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA384())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSA512 extends SignatureSpi
    {
        public ecDetDSA512() {
            super(DigestFactory.createSHA512(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA512())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSASha3_224 extends SignatureSpi
    {
        public ecDetDSASha3_224() {
            super(DigestFactory.createSHA3_224(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_224())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSASha3_256 extends SignatureSpi
    {
        public ecDetDSASha3_256() {
            super(DigestFactory.createSHA3_256(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_256())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSASha3_384 extends SignatureSpi
    {
        public ecDetDSASha3_384() {
            super(DigestFactory.createSHA3_384(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_384())), new StdDSAEncoder());
        }
    }
    
    public static class ecDetDSASha3_512 extends SignatureSpi
    {
        public ecDetDSASha3_512() {
            super(DigestFactory.createSHA3_512(), new ECDSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_512())), new StdDSAEncoder());
        }
    }
    
    public static class ecNR extends SignatureSpi
    {
        public ecNR() {
            super(DigestFactory.createSHA1(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR224 extends SignatureSpi
    {
        public ecNR224() {
            super(DigestFactory.createSHA224(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR256 extends SignatureSpi
    {
        public ecNR256() {
            super(DigestFactory.createSHA256(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR384 extends SignatureSpi
    {
        public ecNR384() {
            super(DigestFactory.createSHA384(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR512 extends SignatureSpi
    {
        public ecNR512() {
            super(DigestFactory.createSHA512(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecPlainDSARP160 extends SignatureSpi
    {
        public ecPlainDSARP160() {
            super(new RIPEMD160Digest(), new ECDSASigner(), new PlainDSAEncoder());
        }
    }
}
