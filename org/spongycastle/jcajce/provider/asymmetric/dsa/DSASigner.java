package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import java.math.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.crypto.digests.*;

public class DSASigner extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest digest;
    private SecureRandom random;
    private DSA signer;
    
    protected DSASigner(final Digest digest, final DSA signer) {
        this.digest = digest;
        this.signer = signer;
    }
    
    private BigInteger[] derDecode(final byte[] array) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(array);
        if (asn1Sequence.size() != 2) {
            throw new IOException("malformed signature");
        }
        if (Arrays.areEqual(array, asn1Sequence.getEncoded("DER"))) {
            return new BigInteger[] { ((ASN1Integer)asn1Sequence.getObjectAt(0)).getValue(), ((ASN1Integer)asn1Sequence.getObjectAt(1)).getValue() };
        }
        throw new IOException("malformed signature");
    }
    
    private byte[] derEncode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
        return new DERSequence(new ASN1Integer[] { new ASN1Integer(bigInteger), new ASN1Integer(bigInteger2) }).getEncoded("DER");
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        CipherParameters generatePrivateKeyParameter;
        final AsymmetricKeyParameter asymmetricKeyParameter = (AsymmetricKeyParameter)(generatePrivateKeyParameter = DSAUtil.generatePrivateKeyParameter(privateKey));
        if (this.random != null) {
            generatePrivateKeyParameter = new ParametersWithRandom(asymmetricKeyParameter, this.random);
        }
        this.digest.reset();
        this.signer.init(true, generatePrivateKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        final AsymmetricKeyParameter generatePublicKeyParameter = DSAUtil.generatePublicKeyParameter(publicKey);
        this.digest.reset();
        this.signer.init(false, generatePublicKeyParameter);
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
            return this.derEncode(generateSignature[0], generateSignature[1]);
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
            final BigInteger[] derDecode = this.derDecode(array);
            return this.signer.verifySignature(array2, derDecode[0], derDecode[1]);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
    
    public static class detDSA extends DSASigner
    {
        public detDSA() {
            super(DigestFactory.createSHA1(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA1())));
        }
    }
    
    public static class detDSA224 extends DSASigner
    {
        public detDSA224() {
            super(DigestFactory.createSHA224(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA224())));
        }
    }
    
    public static class detDSA256 extends DSASigner
    {
        public detDSA256() {
            super(DigestFactory.createSHA256(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA256())));
        }
    }
    
    public static class detDSA384 extends DSASigner
    {
        public detDSA384() {
            super(DigestFactory.createSHA384(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA384())));
        }
    }
    
    public static class detDSA512 extends DSASigner
    {
        public detDSA512() {
            super(DigestFactory.createSHA512(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA512())));
        }
    }
    
    public static class detDSASha3_224 extends DSASigner
    {
        public detDSASha3_224() {
            super(DigestFactory.createSHA3_224(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_224())));
        }
    }
    
    public static class detDSASha3_256 extends DSASigner
    {
        public detDSASha3_256() {
            super(DigestFactory.createSHA3_256(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_256())));
        }
    }
    
    public static class detDSASha3_384 extends DSASigner
    {
        public detDSASha3_384() {
            super(DigestFactory.createSHA3_384(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_384())));
        }
    }
    
    public static class detDSASha3_512 extends DSASigner
    {
        public detDSASha3_512() {
            super(DigestFactory.createSHA3_512(), new org.spongycastle.crypto.signers.DSASigner(new HMacDSAKCalculator(DigestFactory.createSHA3_512())));
        }
    }
    
    public static class dsa224 extends DSASigner
    {
        public dsa224() {
            super(DigestFactory.createSHA224(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsa256 extends DSASigner
    {
        public dsa256() {
            super(DigestFactory.createSHA256(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsa384 extends DSASigner
    {
        public dsa384() {
            super(DigestFactory.createSHA384(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsa512 extends DSASigner
    {
        public dsa512() {
            super(DigestFactory.createSHA512(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsaSha3_224 extends DSASigner
    {
        public dsaSha3_224() {
            super(DigestFactory.createSHA3_224(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsaSha3_256 extends DSASigner
    {
        public dsaSha3_256() {
            super(DigestFactory.createSHA3_256(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsaSha3_384 extends DSASigner
    {
        public dsaSha3_384() {
            super(DigestFactory.createSHA3_384(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class dsaSha3_512 extends DSASigner
    {
        public dsaSha3_512() {
            super(DigestFactory.createSHA3_512(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class noneDSA extends DSASigner
    {
        public noneDSA() {
            super(new NullDigest(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
    
    public static class stdDSA extends DSASigner
    {
        public stdDSA() {
            super(DigestFactory.createSHA1(), new org.spongycastle.crypto.signers.DSASigner());
        }
    }
}
