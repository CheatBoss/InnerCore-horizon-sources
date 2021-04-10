package org.spongycastle.jcajce.provider.asymmetric.ec;

import java.io.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.jcajce.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.crypto.parsers.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;
import java.security.*;
import javax.crypto.*;
import org.spongycastle.crypto.agreement.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.paddings.*;
import org.spongycastle.crypto.engines.*;

public class IESCipher extends CipherSpi
{
    private ByteArrayOutputStream buffer;
    private boolean dhaesMode;
    private IESEngine engine;
    private AlgorithmParameters engineParam;
    private IESParameterSpec engineSpec;
    private final JcaJceHelper helper;
    private int ivLength;
    private AsymmetricKeyParameter key;
    private AsymmetricKeyParameter otherKeyParameter;
    private SecureRandom random;
    private int state;
    
    public IESCipher(final IESEngine engine) {
        this.helper = new BCJcaJceHelper();
        this.state = -1;
        this.buffer = new ByteArrayOutputStream();
        this.engineParam = null;
        this.engineSpec = null;
        this.dhaesMode = false;
        this.otherKeyParameter = null;
        this.engine = engine;
        this.ivLength = 0;
    }
    
    public IESCipher(final IESEngine engine, final int ivLength) {
        this.helper = new BCJcaJceHelper();
        this.state = -1;
        this.buffer = new ByteArrayOutputStream();
        this.engineParam = null;
        this.engineSpec = null;
        this.dhaesMode = false;
        this.otherKeyParameter = null;
        this.engine = engine;
        this.ivLength = ivLength;
    }
    
    public int engineDoFinal(byte[] engineDoFinal, final int n, final int n2, final byte[] array, final int n3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        engineDoFinal = this.engineDoFinal(engineDoFinal, n, n2);
        System.arraycopy(engineDoFinal, 0, array, n3, engineDoFinal.length);
        return engineDoFinal.length;
    }
    
    public byte[] engineDoFinal(byte[] array, int state, final int n) throws IllegalBlockSizeException, BadPaddingException {
        if (n != 0) {
            this.buffer.write(array, state, n);
        }
        final byte[] byteArray = this.buffer.toByteArray();
        this.buffer.reset();
        ParametersWithIV parametersWithIV;
        final IESWithCipherParameters iesWithCipherParameters = (IESWithCipherParameters)(parametersWithIV = (ParametersWithIV)new IESWithCipherParameters(this.engineSpec.getDerivationV(), this.engineSpec.getEncodingV(), this.engineSpec.getMacKeySize(), this.engineSpec.getCipherKeySize()));
        if (this.engineSpec.getNonce() != null) {
            parametersWithIV = new ParametersWithIV(iesWithCipherParameters, this.engineSpec.getNonce());
        }
        final ECDomainParameters parameters = ((ECKeyParameters)this.key).getParameters();
        final AsymmetricKeyParameter otherKeyParameter = this.otherKeyParameter;
        if (otherKeyParameter != null) {
            try {
                if (this.state != 1 && this.state != 3) {
                    this.engine.init(false, this.key, otherKeyParameter, parametersWithIV);
                }
                else {
                    this.engine.init(true, this.otherKeyParameter, this.key, parametersWithIV);
                }
                array = this.engine.processBlock(byteArray, 0, byteArray.length);
                return array;
            }
            catch (Exception ex) {
                throw new BadBlockException("unable to process block", ex);
            }
        }
        state = this.state;
        if (state != 1) {
            if (state != 3) {
                if (state != 2) {
                    if (state != 4) {
                        throw new IllegalStateException("cipher not initialised");
                    }
                }
                try {
                    this.engine.init(this.key, parametersWithIV, new ECIESPublicKeyParser(parameters));
                    array = this.engine.processBlock(byteArray, 0, byteArray.length);
                    return array;
                }
                catch (InvalidCipherTextException ex2) {
                    throw new BadBlockException("unable to process block", ex2);
                }
            }
        }
        final ECKeyPairGenerator ecKeyPairGenerator = new ECKeyPairGenerator();
        ecKeyPairGenerator.init(new ECKeyGenerationParameters(parameters, this.random));
        final EphemeralKeyPairGenerator ephemeralKeyPairGenerator = new EphemeralKeyPairGenerator(ecKeyPairGenerator, new KeyEncoder() {
            final /* synthetic */ boolean val$usePointCompression = IESCipher.this.engineSpec.getPointCompression();
            
            @Override
            public byte[] getEncoded(final AsymmetricKeyParameter asymmetricKeyParameter) {
                return ((ECPublicKeyParameters)asymmetricKeyParameter).getQ().getEncoded(this.val$usePointCompression);
            }
        });
        try {
            this.engine.init(this.key, parametersWithIV, ephemeralKeyPairGenerator);
            array = this.engine.processBlock(byteArray, 0, byteArray.length);
            return array;
        }
        catch (Exception ex3) {
            throw new BadBlockException("unable to process block", ex3);
        }
    }
    
    public int engineGetBlockSize() {
        if (this.engine.getCipher() != null) {
            return this.engine.getCipher().getBlockSize();
        }
        return 0;
    }
    
    public byte[] engineGetIV() {
        final IESParameterSpec engineSpec = this.engineSpec;
        if (engineSpec != null) {
            return engineSpec.getNonce();
        }
        return null;
    }
    
    public int engineGetKeySize(final Key key) {
        if (key instanceof ECKey) {
            return ((ECKey)key).getParameters().getCurve().getFieldSize();
        }
        throw new IllegalArgumentException("not an EC key");
    }
    
    public int engineGetOutputSize(int outputSize) {
        if (this.key != null) {
            final int macSize = this.engine.getMac().getMacSize();
            int n;
            if (this.otherKeyParameter == null) {
                n = (((ECKeyParameters)this.key).getParameters().getCurve().getFieldSize() + 7) / 8 * 2;
            }
            else {
                n = 0;
            }
            if (this.engine.getCipher() != null) {
                final int state = this.state;
                BufferedBlockCipher bufferedBlockCipher;
                if (state != 1 && state != 3) {
                    if (state != 2 && state != 4) {
                        throw new IllegalStateException("cipher not initialised");
                    }
                    bufferedBlockCipher = this.engine.getCipher();
                    outputSize = outputSize - macSize - n;
                }
                else {
                    bufferedBlockCipher = this.engine.getCipher();
                }
                outputSize = bufferedBlockCipher.getOutputSize(outputSize);
            }
            final int state2 = this.state;
            int n2;
            if (state2 != 1 && state2 != 3) {
                if (state2 != 2 && state2 != 4) {
                    throw new IllegalStateException("cipher not initialised");
                }
                n2 = this.buffer.size() - macSize - n;
            }
            else {
                n2 = this.buffer.size() + macSize + 1 + n;
            }
            return n2 + outputSize;
        }
        throw new IllegalStateException("cipher not initialised");
    }
    
    public AlgorithmParameters engineGetParameters() {
        if (this.engineParam == null && this.engineSpec != null) {
            try {
                (this.engineParam = this.helper.createAlgorithmParameters("IES")).init(this.engineSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParam;
    }
    
    public void engineInit(final int n, final Key key, final AlgorithmParameters engineParam, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        Label_0056: {
            if (engineParam != null) {
                try {
                    parameterSpec = engineParam.getParameterSpec(IESParameterSpec.class);
                    break Label_0056;
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("cannot recognise parameters: ");
                    sb.append(ex.toString());
                    throw new InvalidAlgorithmParameterException(sb.toString());
                }
            }
            parameterSpec = null;
        }
        this.engineParam = engineParam;
        this.engineInit(n, key, parameterSpec, secureRandom);
    }
    
    public void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot handle supplied parameter spec: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void engineInit(final int state, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidAlgorithmParameterException, InvalidKeyException {
        final byte[] array = null;
        this.otherKeyParameter = null;
        IESParameterSpec guessParameterSpec;
        if (algorithmParameterSpec == null) {
            final int ivLength = this.ivLength;
            byte[] array2 = array;
            if (ivLength != 0) {
                array2 = array;
                if (state == 1) {
                    array2 = new byte[ivLength];
                    random.nextBytes(array2);
                }
            }
            guessParameterSpec = IESUtil.guessParameterSpec(this.engine.getCipher(), array2);
        }
        else {
            if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
                throw new InvalidAlgorithmParameterException("must be passed IES parameters");
            }
            guessParameterSpec = (IESParameterSpec)algorithmParameterSpec;
        }
        this.engineSpec = guessParameterSpec;
        final byte[] nonce = this.engineSpec.getNonce();
        final int ivLength2 = this.ivLength;
        if (ivLength2 != 0 && (nonce == null || nonce.length != ivLength2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NONCE in IES Parameters needs to be ");
            sb.append(this.ivLength);
            sb.append(" bytes long");
            throw new InvalidAlgorithmParameterException(sb.toString());
        }
        Label_0322: {
            AsymmetricKeyParameter key2;
            if (state != 1 && state != 3) {
                if (state != 2 && state != 4) {
                    throw new InvalidKeyException("must be passed EC key");
                }
                PrivateKey private1;
                if (key instanceof PrivateKey) {
                    private1 = (PrivateKey)key;
                }
                else {
                    if (!(key instanceof IESKey)) {
                        throw new InvalidKeyException("must be passed recipient's private EC key for decryption");
                    }
                    final IESKey iesKey = (IESKey)key;
                    this.otherKeyParameter = ECUtils.generatePublicKeyParameter(iesKey.getPublic());
                    private1 = iesKey.getPrivate();
                }
                key2 = ECUtil.generatePrivateKeyParameter(private1);
            }
            else if (key instanceof PublicKey) {
                key2 = ECUtils.generatePublicKeyParameter((PublicKey)key);
            }
            else {
                if (key instanceof IESKey) {
                    final IESKey iesKey2 = (IESKey)key;
                    this.key = ECUtils.generatePublicKeyParameter(iesKey2.getPublic());
                    this.otherKeyParameter = ECUtil.generatePrivateKeyParameter(iesKey2.getPrivate());
                    break Label_0322;
                }
                throw new InvalidKeyException("must be passed recipient's public EC key for encryption");
            }
            this.key = key2;
        }
        this.random = random;
        this.state = state;
        this.buffer.reset();
    }
    
    public void engineSetMode(final String s) throws NoSuchAlgorithmException {
        final String upperCase = Strings.toUpperCase(s);
        boolean dhaesMode;
        if (upperCase.equals("NONE")) {
            dhaesMode = false;
        }
        else {
            if (!upperCase.equals("DHAES")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't support mode ");
                sb.append(s);
                throw new IllegalArgumentException(sb.toString());
            }
            dhaesMode = true;
        }
        this.dhaesMode = dhaesMode;
    }
    
    public void engineSetPadding(String upperCase) throws NoSuchPaddingException {
        upperCase = Strings.toUpperCase(upperCase);
        if (upperCase.equals("NOPADDING")) {
            return;
        }
        if (upperCase.equals("PKCS5PADDING")) {
            return;
        }
        if (upperCase.equals("PKCS7PADDING")) {
            return;
        }
        throw new NoSuchPaddingException("padding not available with IESCipher");
    }
    
    public int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        this.buffer.write(array, n, n2);
        return 0;
    }
    
    public byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        this.buffer.write(array, n, n2);
        return null;
    }
    
    public static class ECIES extends IESCipher
    {
        public ECIES() {
            super(new IESEngine(new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()), new HMac(DigestFactory.createSHA1())));
        }
    }
    
    public static class ECIESwithAESCBC extends ECIESwithCipher
    {
        public ECIESwithAESCBC() {
            super(new CBCBlockCipher(new AESEngine()), 16);
        }
    }
    
    public static class ECIESwithCipher extends IESCipher
    {
        public ECIESwithCipher(final BlockCipher blockCipher, final int n) {
            super(new IESEngine(new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()), new HMac(DigestFactory.createSHA1()), new PaddedBufferedBlockCipher(blockCipher)), n);
        }
    }
    
    public static class ECIESwithDESedeCBC extends ECIESwithCipher
    {
        public ECIESwithDESedeCBC() {
            super(new CBCBlockCipher(new DESedeEngine()), 8);
        }
    }
}
