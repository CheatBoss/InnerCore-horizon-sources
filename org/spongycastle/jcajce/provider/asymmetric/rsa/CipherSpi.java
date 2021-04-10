package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.io.*;
import org.spongycastle.jcajce.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.crypto.engines.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.interfaces.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import java.security.*;
import org.spongycastle.crypto.encodings.*;

public class CipherSpi extends BaseCipherSpi
{
    private ByteArrayOutputStream bOut;
    private AsymmetricBlockCipher cipher;
    private AlgorithmParameters engineParams;
    private final JcaJceHelper helper;
    private AlgorithmParameterSpec paramSpec;
    private boolean privateKeyOnly;
    private boolean publicKeyOnly;
    
    public CipherSpi(final OAEPParameterSpec oaepParameterSpec) {
        this.helper = new BCJcaJceHelper();
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        try {
            this.initFromSpec(oaepParameterSpec);
        }
        catch (NoSuchPaddingException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    public CipherSpi(final AsymmetricBlockCipher cipher) {
        this.helper = new BCJcaJceHelper();
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        this.cipher = cipher;
    }
    
    public CipherSpi(final boolean publicKeyOnly, final boolean privateKeyOnly, final AsymmetricBlockCipher cipher) {
        this.helper = new BCJcaJceHelper();
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        this.publicKeyOnly = publicKeyOnly;
        this.privateKeyOnly = privateKeyOnly;
        this.cipher = cipher;
    }
    
    private byte[] getOutput() throws BadPaddingException {
        try {
            try {
                final byte[] byteArray = this.bOut.toByteArray();
                final byte[] processBlock = this.cipher.processBlock(byteArray, 0, byteArray.length);
                this.bOut.reset();
                return processBlock;
            }
            finally {}
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new BadBlockException("unable to decrypt block", ex);
        }
        catch (InvalidCipherTextException ex2) {
            throw new BadBlockException("unable to decrypt block", ex2);
        }
        this.bOut.reset();
    }
    
    private void initFromSpec(final OAEPParameterSpec paramSpec) throws NoSuchPaddingException {
        final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)paramSpec.getMGFParameters();
        final Digest digest = DigestFactory.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
        if (digest != null) {
            this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, ((PSource.PSpecified)paramSpec.getPSource()).getValue());
            this.paramSpec = paramSpec;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("no match on OAEP constructor for digest algorithm: ");
        sb.append(mgf1ParameterSpec.getDigestAlgorithm());
        throw new NoSuchPaddingException(sb.toString());
    }
    
    @Override
    protected int engineDoFinal(byte[] output, int i, final int n, final byte[] array, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        if (output != null) {
            this.bOut.write(output, i, n);
        }
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        for (output = this.getOutput(), i = 0; i != output.length; ++i) {
            array[n2 + i] = output[i];
        }
        return output.length;
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        if (array != null) {
            this.bOut.write(array, n, n2);
        }
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return this.getOutput();
    }
    
    @Override
    protected int engineGetBlockSize() {
        try {
            return this.cipher.getInputBlockSize();
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        BigInteger bigInteger;
        if (key instanceof RSAPrivateKey) {
            bigInteger = ((RSAPrivateKey)key).getModulus();
        }
        else {
            if (!(key instanceof RSAPublicKey)) {
                throw new IllegalArgumentException("not an RSA key!");
            }
            bigInteger = ((RSAPublicKey)key).getModulus();
        }
        return bigInteger.bitLength();
    }
    
    @Override
    protected int engineGetOutputSize(int outputBlockSize) {
        try {
            outputBlockSize = this.cipher.getOutputBlockSize();
            return outputBlockSize;
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.paramSpec != null) {
            try {
                (this.engineParams = this.helper.createAlgorithmParameters("OAEP")).init(this.paramSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        Label_0056: {
            if (engineParams != null) {
                try {
                    parameterSpec = engineParams.getParameterSpec(OAEPParameterSpec.class);
                    break Label_0056;
                }
                catch (InvalidParameterSpecException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("cannot recognise parameters: ");
                    sb.append(ex.toString());
                    throw new InvalidAlgorithmParameterException(sb.toString(), ex);
                }
            }
            parameterSpec = null;
        }
        this.engineParams = engineParams;
        this.engineInit(n, key, parameterSpec, secureRandom);
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Eeeek! ");
            sb.append(ex.toString());
            throw new InvalidKeyException(sb.toString(), ex);
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec paramSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (paramSpec != null && !(paramSpec instanceof OAEPParameterSpec)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown parameter type: ");
            sb.append(paramSpec.getClass().getName());
            throw new InvalidAlgorithmParameterException(sb.toString());
        }
        RSAKeyParameters rsaKeyParameters;
        if (key instanceof RSAPublicKey) {
            if (this.privateKeyOnly && n == 1) {
                throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
            }
            rsaKeyParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)key);
        }
        else {
            if (!(key instanceof RSAPrivateKey)) {
                throw new InvalidKeyException("unknown key type passed to RSA");
            }
            if (this.publicKeyOnly && n == 1) {
                throw new InvalidKeyException("mode 2 requires RSAPublicKey");
            }
            rsaKeyParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)key);
        }
        if (paramSpec != null) {
            final OAEPParameterSpec oaepParameterSpec = (OAEPParameterSpec)paramSpec;
            this.paramSpec = paramSpec;
            if (!oaepParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !oaepParameterSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())) {
                throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
            }
            if (!(oaepParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("unkown MGF parameters");
            }
            final Digest digest = DigestFactory.getDigest(oaepParameterSpec.getDigestAlgorithm());
            if (digest == null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("no match on digest algorithm: ");
                sb2.append(oaepParameterSpec.getDigestAlgorithm());
                throw new InvalidAlgorithmParameterException(sb2.toString());
            }
            final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)oaepParameterSpec.getMGFParameters();
            final Digest digest2 = DigestFactory.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
            if (digest2 == null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("no match on MGF digest algorithm: ");
                sb3.append(mgf1ParameterSpec.getDigestAlgorithm());
                throw new InvalidAlgorithmParameterException(sb3.toString());
            }
            this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, digest2, ((PSource.PSpecified)oaepParameterSpec.getPSource()).getValue());
        }
        RSAKeyParameters rsaKeyParameters2 = rsaKeyParameters;
        if (!(this.cipher instanceof RSABlindedEngine)) {
            ParametersWithRandom parametersWithRandom;
            if (secureRandom != null) {
                parametersWithRandom = new ParametersWithRandom(rsaKeyParameters, secureRandom);
            }
            else {
                parametersWithRandom = new ParametersWithRandom(rsaKeyParameters, new SecureRandom());
            }
            rsaKeyParameters2 = (RSAKeyParameters)parametersWithRandom;
        }
        this.bOut.reset();
        Label_0505: {
            if (n != 1) {
                if (n != 2) {
                    if (n == 3) {
                        break Label_0505;
                    }
                    if (n != 4) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("unknown opmode ");
                        sb4.append(n);
                        sb4.append(" passed to RSA");
                        throw new InvalidParameterException(sb4.toString());
                    }
                }
                this.cipher.init(false, rsaKeyParameters2);
                return;
            }
        }
        this.cipher.init(true, rsaKeyParameters2);
    }
    
    @Override
    protected void engineSetMode(final String s) throws NoSuchAlgorithmException {
        final String upperCase = Strings.toUpperCase(s);
        if (upperCase.equals("NONE")) {
            return;
        }
        if (upperCase.equals("ECB")) {
            return;
        }
        if (upperCase.equals("1")) {
            this.privateKeyOnly = true;
            this.publicKeyOnly = false;
            return;
        }
        if (upperCase.equals("2")) {
            this.privateKeyOnly = false;
            this.publicKeyOnly = true;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("can't support mode ");
        sb.append(s);
        throw new NoSuchAlgorithmException(sb.toString());
    }
    
    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(s);
        if (upperCase.equals("NOPADDING")) {
            this.cipher = new RSABlindedEngine();
            return;
        }
        if (upperCase.equals("PKCS1PADDING")) {
            this.cipher = new PKCS1Encoding(new RSABlindedEngine());
            return;
        }
        if (upperCase.equals("ISO9796-1PADDING")) {
            this.cipher = new ISO9796d1Encoding(new RSABlindedEngine());
            return;
        }
        if (upperCase.equals("OAEPWITHMD5ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPPADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (upperCase.equals("OAEPWITHSHA1ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-1ANDMGF1PADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (upperCase.equals("OAEPWITHSHA224ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA256ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA384ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA512ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-512ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA3-224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA3-224", "MGF1", new MGF1ParameterSpec("SHA3-224"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA3-256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA3-256", "MGF1", new MGF1ParameterSpec("SHA3-256"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA3-384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA3-384", "MGF1", new MGF1ParameterSpec("SHA3-384"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA3-512ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA3-512", "MGF1", new MGF1ParameterSpec("SHA3-512"), PSource.PSpecified.DEFAULT));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" unavailable with RSA.");
        throw new NoSuchPaddingException(sb.toString());
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        this.bOut.write(array, n, n2);
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return 0;
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        this.bOut.write(array, n, n2);
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return null;
    }
    
    public static class ISO9796d1Padding extends CipherSpi
    {
        public ISO9796d1Padding() {
            super(new ISO9796d1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class NoPadding extends CipherSpi
    {
        public NoPadding() {
            super(new RSABlindedEngine());
        }
    }
    
    public static class OAEPPadding extends CipherSpi
    {
        public OAEPPadding() {
            super(OAEPParameterSpec.DEFAULT);
        }
    }
    
    public static class PKCS1v1_5Padding extends CipherSpi
    {
        public PKCS1v1_5Padding() {
            super(new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class PKCS1v1_5Padding_PrivateOnly extends CipherSpi
    {
        public PKCS1v1_5Padding_PrivateOnly() {
            super(false, true, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class PKCS1v1_5Padding_PublicOnly extends CipherSpi
    {
        public PKCS1v1_5Padding_PublicOnly() {
            super(true, false, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
}
