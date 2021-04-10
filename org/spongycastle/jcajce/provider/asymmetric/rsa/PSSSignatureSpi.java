package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.crypto.signers.*;
import org.spongycastle.jcajce.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.crypto.params.*;
import java.security.interfaces.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.crypto.engines.*;

public class PSSSignatureSpi extends SignatureSpi
{
    private Digest contentDigest;
    private AlgorithmParameters engineParams;
    private final JcaJceHelper helper;
    private boolean isRaw;
    private Digest mgfDigest;
    private PSSParameterSpec originalSpec;
    private PSSParameterSpec paramSpec;
    private PSSSigner pss;
    private int saltLength;
    private AsymmetricBlockCipher signer;
    private byte trailer;
    
    protected PSSSignatureSpi(final AsymmetricBlockCipher asymmetricBlockCipher, final PSSParameterSpec pssParameterSpec) {
        this(asymmetricBlockCipher, pssParameterSpec, false);
    }
    
    protected PSSSignatureSpi(final AsymmetricBlockCipher signer, final PSSParameterSpec pssParameterSpec, final boolean isRaw) {
        this.helper = new BCJcaJceHelper();
        this.signer = signer;
        this.originalSpec = pssParameterSpec;
        if (pssParameterSpec == null) {
            this.paramSpec = PSSParameterSpec.DEFAULT;
        }
        else {
            this.paramSpec = pssParameterSpec;
        }
        this.mgfDigest = DigestFactory.getDigest(this.paramSpec.getDigestAlgorithm());
        this.saltLength = this.paramSpec.getSaltLength();
        this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
        this.isRaw = isRaw;
        this.setupContentDigest();
    }
    
    private byte getTrailer(final int n) {
        if (n == 1) {
            return -68;
        }
        throw new IllegalArgumentException("unknown trailer field");
    }
    
    private void setupContentDigest() {
        Digest mgfDigest;
        if (this.isRaw) {
            mgfDigest = new NullPssDigest(this.mgfDigest);
        }
        else {
            mgfDigest = this.mgfDigest;
        }
        this.contentDigest = mgfDigest;
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.paramSpec != null) {
            try {
                (this.engineParams = this.helper.createAlgorithmParameters("PSS")).init(this.paramSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateKey) {
            (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey));
            return;
        }
        throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom secureRandom) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateKey) {
            (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(true, new ParametersWithRandom(RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey), secureRandom));
            return;
        }
        throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof RSAPublicKey) {
            (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(false, RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey));
            return;
        }
        throw new InvalidKeyException("Supplied key is not a RSAPublicKey instance");
    }
    
    @Override
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Only PSSParameterSpec supported");
        }
        final PSSParameterSpec paramSpec = (PSSParameterSpec)algorithmParameterSpec;
        final PSSParameterSpec originalSpec = this.originalSpec;
        if (originalSpec != null && !DigestFactory.isSameDigest(originalSpec.getDigestAlgorithm(), paramSpec.getDigestAlgorithm())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("parameter must be using ");
            sb.append(this.originalSpec.getDigestAlgorithm());
            throw new InvalidAlgorithmParameterException(sb.toString());
        }
        if (!paramSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !paramSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())) {
            throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
        }
        if (!(paramSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
            throw new InvalidAlgorithmParameterException("unknown MGF parameters");
        }
        final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)paramSpec.getMGFParameters();
        if (!DigestFactory.isSameDigest(mgf1ParameterSpec.getDigestAlgorithm(), paramSpec.getDigestAlgorithm())) {
            throw new InvalidAlgorithmParameterException("digest algorithm for MGF should be the same as for PSS parameters.");
        }
        final Digest digest = DigestFactory.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
        if (digest != null) {
            this.engineParams = null;
            this.paramSpec = paramSpec;
            this.mgfDigest = digest;
            this.saltLength = paramSpec.getSaltLength();
            this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
            this.setupContentDigest();
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("no match on MGF digest algorithm: ");
        sb2.append(mgf1ParameterSpec.getDigestAlgorithm());
        throw new InvalidAlgorithmParameterException(sb2.toString());
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        try {
            return this.pss.generateSignature();
        }
        catch (CryptoException ex) {
            throw new SignatureException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.pss.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.pss.update(array, n, n2);
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        return this.pss.verifySignature(array);
    }
    
    private class NullPssDigest implements Digest
    {
        private ByteArrayOutputStream bOut;
        private Digest baseDigest;
        private boolean oddTime;
        
        public NullPssDigest(final Digest baseDigest) {
            this.bOut = new ByteArrayOutputStream();
            this.oddTime = true;
            this.baseDigest = baseDigest;
        }
        
        @Override
        public int doFinal(final byte[] array, final int n) {
            final byte[] byteArray = this.bOut.toByteArray();
            if (this.oddTime) {
                System.arraycopy(byteArray, 0, array, n, byteArray.length);
            }
            else {
                this.baseDigest.update(byteArray, 0, byteArray.length);
                this.baseDigest.doFinal(array, n);
            }
            this.reset();
            this.oddTime ^= true;
            return byteArray.length;
        }
        
        @Override
        public String getAlgorithmName() {
            return "NULL";
        }
        
        public int getByteLength() {
            return 0;
        }
        
        @Override
        public int getDigestSize() {
            return this.baseDigest.getDigestSize();
        }
        
        @Override
        public void reset() {
            this.bOut.reset();
            this.baseDigest.reset();
        }
        
        @Override
        public void update(final byte b) {
            this.bOut.write(b);
        }
        
        @Override
        public void update(final byte[] array, final int n, final int n2) {
            this.bOut.write(array, n, n2);
        }
    }
    
    public static class PSSwithRSA extends PSSSignatureSpi
    {
        public PSSwithRSA() {
            super(new RSABlindedEngine(), null);
        }
    }
    
    public static class SHA1withRSA extends PSSSignatureSpi
    {
        public SHA1withRSA() {
            super(new RSABlindedEngine(), PSSParameterSpec.DEFAULT);
        }
    }
    
    public static class SHA224withRSA extends PSSSignatureSpi
    {
        public SHA224withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), 28, 1));
        }
    }
    
    public static class SHA256withRSA extends PSSSignatureSpi
    {
        public SHA256withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
        }
    }
    
    public static class SHA384withRSA extends PSSSignatureSpi
    {
        public SHA384withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1));
        }
    }
    
    public static class SHA3_224withRSA extends PSSSignatureSpi
    {
        public SHA3_224withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA3-224", "MGF1", new MGF1ParameterSpec("SHA3-224"), 28, 1));
        }
    }
    
    public static class SHA3_256withRSA extends PSSSignatureSpi
    {
        public SHA3_256withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA3-256", "MGF1", new MGF1ParameterSpec("SHA3-256"), 32, 1));
        }
    }
    
    public static class SHA3_384withRSA extends PSSSignatureSpi
    {
        public SHA3_384withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA3-384", "MGF1", new MGF1ParameterSpec("SHA3-384"), 48, 1));
        }
    }
    
    public static class SHA3_512withRSA extends PSSSignatureSpi
    {
        public SHA3_512withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA3-512", "MGF1", new MGF1ParameterSpec("SHA3-512"), 64, 1));
        }
    }
    
    public static class SHA512_224withRSA extends PSSSignatureSpi
    {
        public SHA512_224withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-512(224)", "MGF1", new MGF1ParameterSpec("SHA-512(224)"), 28, 1));
        }
    }
    
    public static class SHA512_256withRSA extends PSSSignatureSpi
    {
        public SHA512_256withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-512(256)", "MGF1", new MGF1ParameterSpec("SHA-512(256)"), 32, 1));
        }
    }
    
    public static class SHA512withRSA extends PSSSignatureSpi
    {
        public SHA512withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1));
        }
    }
    
    public static class nonePSS extends PSSSignatureSpi
    {
        public nonePSS() {
            super(new RSABlindedEngine(), null, true);
        }
    }
}
