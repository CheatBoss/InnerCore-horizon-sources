package org.spongycastle.jce.provider;

import org.spongycastle.crypto.paddings.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import javax.crypto.*;
import org.spongycastle.crypto.modes.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.engines.*;

public class BrokenJCEBlockCipher implements BrokenPBE
{
    private Class[] availableSpecs;
    private BufferedBlockCipher cipher;
    private AlgorithmParameters engineParams;
    private int ivLength;
    private ParametersWithIV ivParam;
    private int pbeHash;
    private int pbeIvSize;
    private int pbeKeySize;
    private int pbeType;
    
    protected BrokenJCEBlockCipher(final BlockCipher blockCipher) {
        this.availableSpecs = new Class[] { IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.ivLength = 0;
        this.engineParams = null;
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
    }
    
    protected BrokenJCEBlockCipher(final BlockCipher blockCipher, final int pbeType, final int pbeHash, final int pbeKeySize, final int pbeIvSize) {
        this.availableSpecs = new Class[] { IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.ivLength = 0;
        this.engineParams = null;
        this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        this.pbeType = pbeType;
        this.pbeHash = pbeHash;
        this.pbeKeySize = pbeKeySize;
        this.pbeIvSize = pbeIvSize;
    }
    
    protected int engineDoFinal(final byte[] array, int processBytes, int doFinal, final byte[] array2, final int n) throws IllegalBlockSizeException, BadPaddingException {
        if (doFinal != 0) {
            processBytes = this.cipher.processBytes(array, processBytes, doFinal, array2, n);
        }
        else {
            processBytes = 0;
        }
        try {
            doFinal = this.cipher.doFinal(array2, n + processBytes);
            return processBytes + doFinal;
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
        catch (DataLengthException ex2) {
            throw new IllegalBlockSizeException(ex2.getMessage());
        }
    }
    
    protected byte[] engineDoFinal(byte[] array, int processBytes, int doFinal) throws IllegalBlockSizeException, BadPaddingException {
        final byte[] array2 = new byte[this.engineGetOutputSize(doFinal)];
        if (doFinal != 0) {
            processBytes = this.cipher.processBytes(array, processBytes, doFinal, array2, 0);
        }
        else {
            processBytes = 0;
        }
        try {
            doFinal = this.cipher.doFinal(array2, processBytes);
            processBytes += doFinal;
            array = new byte[processBytes];
            System.arraycopy(array2, 0, array, 0, processBytes);
            return array;
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
        catch (DataLengthException ex2) {
            throw new IllegalBlockSizeException(ex2.getMessage());
        }
    }
    
    protected int engineGetBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    protected byte[] engineGetIV() {
        final ParametersWithIV ivParam = this.ivParam;
        if (ivParam != null) {
            return ivParam.getIV();
        }
        return null;
    }
    
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length;
    }
    
    protected int engineGetOutputSize(final int n) {
        return this.cipher.getOutputSize(n);
    }
    
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.ivParam != null) {
            String s2;
            final String s = s2 = this.cipher.getUnderlyingCipher().getAlgorithmName();
            if (s.indexOf(47) >= 0) {
                s2 = s.substring(0, s.indexOf(47));
            }
            try {
                (this.engineParams = AlgorithmParameters.getInstance(s2, "SC")).init(this.ivParam.getIV());
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        final AlgorithmParameterSpec algorithmParameterSpec = null;
        if (engineParams != null) {
            int n2 = 0;
            while (true) {
                final Class[] availableSpecs = this.availableSpecs;
                parameterSpec = algorithmParameterSpec;
                if (n2 != availableSpecs.length) {
                    try {
                        parameterSpec = engineParams.getParameterSpec((Class<AlgorithmParameterSpec>)availableSpecs[n2]);
                    }
                    catch (Exception ex) {
                        ++n2;
                        continue;
                    }
                    break;
                }
                break;
            }
            if (parameterSpec == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't handle parameter ");
                sb.append(engineParams.toString());
                throw new InvalidAlgorithmParameterException(sb.toString());
            }
        }
        this.engineParams = engineParams;
        this.engineInit(n, key, parameterSpec, secureRandom);
    }
    
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        CipherParameters pbeParameters = null;
        Label_0303: {
            Label_0214: {
                if (!(key instanceof BCPBEKey)) {
                    if (algorithmParameterSpec == null) {
                        pbeParameters = new KeyParameter(key.getEncoded());
                    }
                    else if (algorithmParameterSpec instanceof IvParameterSpec) {
                        if (this.ivLength != 0) {
                            pbeParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
                            this.ivParam = (ParametersWithIV)pbeParameters;
                        }
                        else {
                            pbeParameters = new KeyParameter(key.getEncoded());
                        }
                    }
                    else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                        final RC2ParameterSpec rc2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
                        final RC2Parameters rc2Parameters = (RC2Parameters)(pbeParameters = new RC2Parameters(key.getEncoded(), rc2ParameterSpec.getEffectiveKeyBits()));
                        if (rc2ParameterSpec.getIV() != null) {
                            pbeParameters = rc2Parameters;
                            if (this.ivLength != 0) {
                                pbeParameters = new ParametersWithIV(rc2Parameters, rc2ParameterSpec.getIV());
                                break Label_0214;
                            }
                        }
                    }
                    else {
                        if (!(algorithmParameterSpec instanceof RC5ParameterSpec)) {
                            throw new InvalidAlgorithmParameterException("unknown parameter type.");
                        }
                        final RC5ParameterSpec rc5ParameterSpec = (RC5ParameterSpec)algorithmParameterSpec;
                        final RC5Parameters rc5Parameters = new RC5Parameters(key.getEncoded(), rc5ParameterSpec.getRounds());
                        if (rc5ParameterSpec.getWordSize() != 32) {
                            throw new IllegalArgumentException("can only accept RC5 word size 32 (at the moment...)");
                        }
                        pbeParameters = rc5Parameters;
                        if (rc5ParameterSpec.getIV() != null) {
                            pbeParameters = rc5Parameters;
                            if (this.ivLength != 0) {
                                pbeParameters = new ParametersWithIV(rc5Parameters, rc5ParameterSpec.getIV());
                                break Label_0214;
                            }
                        }
                    }
                    break Label_0303;
                }
                final CipherParameters cipherParameters = pbeParameters = Util.makePBEParameters((BCPBEKey)key, algorithmParameterSpec, this.pbeType, this.pbeHash, this.cipher.getUnderlyingCipher().getAlgorithmName(), this.pbeKeySize, this.pbeIvSize);
                if (this.pbeIvSize == 0) {
                    break Label_0303;
                }
                pbeParameters = cipherParameters;
            }
            this.ivParam = (ParametersWithIV)pbeParameters;
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)pbeParameters;
        if (this.ivLength != 0) {
            parametersWithIV = (ParametersWithIV)pbeParameters;
            if (!(pbeParameters instanceof ParametersWithIV)) {
                SecureRandom secureRandom2;
                if ((secureRandom2 = secureRandom) == null) {
                    secureRandom2 = new SecureRandom();
                }
                if (n != 1 && n != 3) {
                    throw new InvalidAlgorithmParameterException("no IV set when one expected");
                }
                final byte[] array = new byte[this.ivLength];
                secureRandom2.nextBytes(array);
                parametersWithIV = new ParametersWithIV(pbeParameters, array);
                this.ivParam = parametersWithIV;
            }
        }
        Label_0433: {
            if (n != 1) {
                if (n != 2) {
                    if (n == 3) {
                        break Label_0433;
                    }
                    if (n != 4) {
                        System.out.println("eeek!");
                        return;
                    }
                }
                this.cipher.init(false, parametersWithIV);
                return;
            }
        }
        this.cipher.init(true, parametersWithIV);
    }
    
    protected void engineSetMode(final String s) {
        final String upperCase = Strings.toUpperCase(s);
        if (upperCase.equals("ECB")) {
            this.ivLength = 0;
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
            return;
        }
        if (upperCase.equals("CBC")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(this.cipher.getUnderlyingCipher()));
            return;
        }
        if (upperCase.startsWith("OFB")) {
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (upperCase.length() != 3) {
                this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), Integer.parseInt(upperCase.substring(3))));
                return;
            }
            this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.cipher.getUnderlyingCipher(), this.cipher.getBlockSize() * 8));
        }
        else {
            if (!upperCase.startsWith("CFB")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't support mode ");
                sb.append(s);
                throw new IllegalArgumentException(sb.toString());
            }
            this.ivLength = this.cipher.getUnderlyingCipher().getBlockSize();
            if (upperCase.length() != 3) {
                this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), Integer.parseInt(upperCase.substring(3))));
                return;
            }
            this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.cipher.getUnderlyingCipher(), this.cipher.getBlockSize() * 8));
        }
    }
    
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(s);
        BufferedBlockCipher cipher;
        if (upperCase.equals("NOPADDING")) {
            cipher = new BufferedBlockCipher(this.cipher.getUnderlyingCipher());
        }
        else if (!upperCase.equals("PKCS5PADDING") && !upperCase.equals("PKCS7PADDING") && !upperCase.equals("ISO10126PADDING")) {
            if (!upperCase.equals("WITHCTS")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Padding ");
                sb.append(s);
                sb.append(" unknown.");
                throw new NoSuchPaddingException(sb.toString());
            }
            cipher = new CTSBlockCipher(this.cipher.getUnderlyingCipher());
        }
        else {
            cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
        }
        this.cipher = cipher;
    }
    
    protected Key engineUnwrap(byte[] engineDoFinal, final String s, final int n) throws InvalidKeyException {
        try {
            engineDoFinal = this.engineDoFinal(engineDoFinal, 0, engineDoFinal.length);
            if (n == 3) {
                return new SecretKeySpec(engineDoFinal, s);
            }
            try {
                final KeyFactory instance = KeyFactory.getInstance(s, "SC");
                if (n == 1) {
                    return instance.generatePublic(new X509EncodedKeySpec(engineDoFinal));
                }
                if (n == 2) {
                    return instance.generatePrivate(new PKCS8EncodedKeySpec(engineDoFinal));
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown key type ");
                sb.append(n);
                throw new InvalidKeyException(sb.toString());
            }
            catch (InvalidKeySpecException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown key type ");
                sb2.append(ex.getMessage());
                throw new InvalidKeyException(sb2.toString());
            }
            catch (NoSuchAlgorithmException ex2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown key type ");
                sb3.append(ex2.getMessage());
                throw new InvalidKeyException(sb3.toString());
            }
            catch (NoSuchProviderException ex3) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Unknown key type ");
                sb4.append(ex3.getMessage());
                throw new InvalidKeyException(sb4.toString());
            }
        }
        catch (IllegalBlockSizeException ex4) {
            throw new InvalidKeyException(ex4.getMessage());
        }
        catch (BadPaddingException ex5) {
            throw new InvalidKeyException(ex5.getMessage());
        }
    }
    
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        return this.cipher.processBytes(array, n, n2, array2, n3);
    }
    
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        final int updateOutputSize = this.cipher.getUpdateOutputSize(n2);
        if (updateOutputSize > 0) {
            final byte[] array2 = new byte[updateOutputSize];
            this.cipher.processBytes(array, n, n2, array2, 0);
            return array2;
        }
        this.cipher.processBytes(array, n, n2, null, 0);
        return null;
    }
    
    protected byte[] engineWrap(final Key key) throws IllegalBlockSizeException, InvalidKeyException {
        final byte[] encoded = key.getEncoded();
        if (encoded != null) {
            try {
                return this.engineDoFinal(encoded, 0, encoded.length);
            }
            catch (BadPaddingException ex) {
                throw new IllegalBlockSizeException(ex.getMessage());
            }
        }
        throw new InvalidKeyException("Cannot wrap key, null encoding.");
    }
    
    public static class BrokePBEWithMD5AndDES extends BrokenJCEBlockCipher
    {
        public BrokePBEWithMD5AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 0, 64, 64);
        }
    }
    
    public static class BrokePBEWithSHA1AndDES extends BrokenJCEBlockCipher
    {
        public BrokePBEWithSHA1AndDES() {
            super(new CBCBlockCipher(new DESEngine()), 0, 1, 64, 64);
        }
    }
    
    public static class BrokePBEWithSHAAndDES2Key extends BrokenJCEBlockCipher
    {
        public BrokePBEWithSHAAndDES2Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 128, 64);
        }
    }
    
    public static class BrokePBEWithSHAAndDES3Key extends BrokenJCEBlockCipher
    {
        public BrokePBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 2, 1, 192, 64);
        }
    }
    
    public static class OldPBEWithSHAAndDES3Key extends BrokenJCEBlockCipher
    {
        public OldPBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()), 3, 1, 192, 64);
        }
    }
    
    public static class OldPBEWithSHAAndTwofish extends BrokenJCEBlockCipher
    {
        public OldPBEWithSHAAndTwofish() {
            super(new CBCBlockCipher(new TwofishEngine()), 3, 1, 256, 128);
        }
    }
}
