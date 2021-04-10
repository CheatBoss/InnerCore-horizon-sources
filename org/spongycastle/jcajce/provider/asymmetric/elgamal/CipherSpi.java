package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.crypto.engines.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import java.math.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import java.security.*;
import org.spongycastle.crypto.encodings.*;

public class CipherSpi extends BaseCipherSpi
{
    private BufferedAsymmetricBlockCipher cipher;
    private AlgorithmParameters engineParams;
    private AlgorithmParameterSpec paramSpec;
    
    public CipherSpi(final AsymmetricBlockCipher asymmetricBlockCipher) {
        this.cipher = new BufferedAsymmetricBlockCipher(asymmetricBlockCipher);
    }
    
    private byte[] getOutput() throws BadPaddingException {
        try {
            return this.cipher.doFinal();
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new BadBlockException("unable to decrypt block", ex);
        }
        catch (InvalidCipherTextException ex2) {
            throw new BadPaddingException("unable to decrypt block") {
                @Override
                public Throwable getCause() {
                    synchronized (this) {
                        return ex2;
                    }
                }
            };
        }
    }
    
    private void initFromSpec(final OAEPParameterSpec paramSpec) throws NoSuchPaddingException {
        final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)paramSpec.getMGFParameters();
        final Digest digest = DigestFactory.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
        if (digest != null) {
            this.cipher = new BufferedAsymmetricBlockCipher(new OAEPEncoding(new ElGamalEngine(), digest, ((PSource.PSpecified)paramSpec.getPSource()).getValue()));
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
        this.cipher.processBytes(output, i, n);
        for (output = this.getOutput(), i = 0; i != output.length; ++i) {
            array[n2 + i] = output[i];
        }
        return output.length;
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        this.cipher.processBytes(array, n, n2);
        return this.getOutput();
    }
    
    @Override
    protected int engineGetBlockSize() {
        return this.cipher.getInputBlockSize();
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        BigInteger bigInteger;
        if (key instanceof ElGamalKey) {
            bigInteger = ((ElGamalKey)key).getParameters().getP();
        }
        else {
            if (!(key instanceof DHKey)) {
                throw new IllegalArgumentException("not an ElGamal key!");
            }
            bigInteger = ((DHKey)key).getParams().getP();
        }
        return bigInteger.bitLength();
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return this.cipher.getOutputBlockSize();
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.paramSpec != null) {
            try {
                (this.engineParams = this.createParametersInstance("OAEP")).init(this.paramSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters algorithmParameters, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("can't handle parameters in ElGamal");
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException {
        if (algorithmParameterSpec == null) {
            AsymmetricKeyParameter asymmetricKeyParameter;
            if (key instanceof ElGamalPublicKey) {
                asymmetricKeyParameter = ElGamalUtil.generatePublicKeyParameter((PublicKey)key);
            }
            else {
                if (!(key instanceof ElGamalPrivateKey)) {
                    throw new InvalidKeyException("unknown key type passed to ElGamal");
                }
                asymmetricKeyParameter = ElGamalUtil.generatePrivateKeyParameter((PrivateKey)key);
            }
            CipherParameters cipherParameters = asymmetricKeyParameter;
            if (secureRandom != null) {
                cipherParameters = new ParametersWithRandom(asymmetricKeyParameter, secureRandom);
            }
            boolean b = true;
            while (true) {
                Label_0137: {
                    if (n == 1) {
                        break Label_0137;
                    }
                    if (n != 2) {
                        if (n == 3) {
                            break Label_0137;
                        }
                        if (n != 4) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unknown opmode ");
                            sb.append(n);
                            sb.append(" passed to ElGamal");
                            throw new InvalidParameterException(sb.toString());
                        }
                    }
                    final BufferedAsymmetricBlockCipher bufferedAsymmetricBlockCipher = this.cipher;
                    b = false;
                    bufferedAsymmetricBlockCipher.init(b, cipherParameters);
                    return;
                }
                final BufferedAsymmetricBlockCipher bufferedAsymmetricBlockCipher = this.cipher;
                continue;
            }
        }
        throw new IllegalArgumentException("unknown parameter type.");
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
        final StringBuilder sb = new StringBuilder();
        sb.append("can't support mode ");
        sb.append(s);
        throw new NoSuchAlgorithmException(sb.toString());
    }
    
    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(s);
        if (upperCase.equals("NOPADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new ElGamalEngine());
            return;
        }
        if (upperCase.equals("PKCS1PADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new PKCS1Encoding(new ElGamalEngine()));
            return;
        }
        if (upperCase.equals("ISO9796-1PADDING")) {
            this.cipher = new BufferedAsymmetricBlockCipher(new ISO9796d1Encoding(new ElGamalEngine()));
            return;
        }
        if (upperCase.equals("OAEPPADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (upperCase.equals("OAEPWITHMD5ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA1ANDMGF1PADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
            return;
        }
        if (upperCase.equals("OAEPWITHSHA224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
            return;
        }
        if (upperCase.equals("OAEPWITHSHA512ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" unavailable with ElGamal.");
        throw new NoSuchPaddingException(sb.toString());
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        this.cipher.processBytes(array, n, n2);
        return 0;
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        this.cipher.processBytes(array, n, n2);
        return null;
    }
    
    public static class NoPadding extends CipherSpi
    {
        public NoPadding() {
            super(new ElGamalEngine());
        }
    }
    
    public static class PKCS1v1_5Padding extends CipherSpi
    {
        public PKCS1v1_5Padding() {
            super(new PKCS1Encoding(new ElGamalEngine()));
        }
    }
}
