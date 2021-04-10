package org.spongycastle.jcajce.provider.symmetric.util;

import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.cms.*;
import javax.crypto.interfaces.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.jcajce.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.engines.*;
import java.security.*;
import org.spongycastle.crypto.modes.*;
import javax.crypto.*;
import java.nio.*;
import java.lang.reflect.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.paddings.*;

public class BaseBlockCipher extends BaseWrapCipher implements PBE
{
    private static final Class gcmSpecClass;
    private AEADParameters aeadParams;
    private Class[] availableSpecs;
    private BlockCipher baseEngine;
    private GenericBlockCipher cipher;
    private int digest;
    private BlockCipherProvider engineProvider;
    private boolean fixedIv;
    private int ivLength;
    private ParametersWithIV ivParam;
    private int keySizeInBits;
    private String modeName;
    private boolean padded;
    private String pbeAlgorithm;
    private PBEParameterSpec pbeSpec;
    private int scheme;
    
    static {
        gcmSpecClass = ClassUtil.loadClass(BaseBlockCipher.class, "javax.crypto.spec.GCMParameterSpec");
    }
    
    protected BaseBlockCipher(final BlockCipher baseEngine) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = baseEngine;
        this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(baseEngine);
    }
    
    protected BaseBlockCipher(final BlockCipher baseEngine, final int n) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = baseEngine;
        this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(baseEngine);
        this.ivLength = n / 8;
    }
    
    protected BaseBlockCipher(final BlockCipher baseEngine, final int scheme, final int digest, final int keySizeInBits, final int ivLength) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = baseEngine;
        this.scheme = scheme;
        this.digest = digest;
        this.keySizeInBits = keySizeInBits;
        this.ivLength = ivLength;
        this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(baseEngine);
    }
    
    protected BaseBlockCipher(final BufferedBlockCipher bufferedBlockCipher, final int n) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = bufferedBlockCipher.getUnderlyingCipher();
        this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(bufferedBlockCipher);
        this.ivLength = n / 8;
    }
    
    protected BaseBlockCipher(final AEADBlockCipher aeadBlockCipher) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        final BlockCipher underlyingCipher = aeadBlockCipher.getUnderlyingCipher();
        this.baseEngine = underlyingCipher;
        this.ivLength = underlyingCipher.getBlockSize();
        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(aeadBlockCipher);
    }
    
    protected BaseBlockCipher(final AEADBlockCipher aeadBlockCipher, final boolean fixedIv, final int ivLength) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = aeadBlockCipher.getUnderlyingCipher();
        this.fixedIv = fixedIv;
        this.ivLength = ivLength;
        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(aeadBlockCipher);
    }
    
    protected BaseBlockCipher(final BlockCipherProvider engineProvider) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, BaseBlockCipher.gcmSpecClass, GOST28147ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.scheme = -1;
        this.ivLength = 0;
        this.fixedIv = true;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = engineProvider.get();
        this.engineProvider = engineProvider;
        this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(engineProvider.get());
    }
    
    private CipherParameters adjustParameters(final AlgorithmParameterSpec algorithmParameterSpec, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            final CipherParameters parameters = ((ParametersWithIV)cipherParameters).getParameters();
            ParametersWithIV ivParam;
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                ivParam = new ParametersWithIV(parameters, ((IvParameterSpec)algorithmParameterSpec).getIV());
            }
            else {
                if (!(algorithmParameterSpec instanceof GOST28147ParameterSpec)) {
                    return cipherParameters;
                }
                final GOST28147ParameterSpec gost28147ParameterSpec = (GOST28147ParameterSpec)algorithmParameterSpec;
                final ParametersWithSBox parametersWithSBox = new ParametersWithSBox(cipherParameters, gost28147ParameterSpec.getSbox());
                if (gost28147ParameterSpec.getIV() == null || this.ivLength == 0) {
                    return parametersWithSBox;
                }
                ivParam = new ParametersWithIV(parameters, gost28147ParameterSpec.getIV());
            }
            return this.ivParam = ivParam;
        }
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            return this.ivParam = new ParametersWithIV(cipherParameters, ((IvParameterSpec)algorithmParameterSpec).getIV());
        }
        if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
            final GOST28147ParameterSpec gost28147ParameterSpec2 = (GOST28147ParameterSpec)algorithmParameterSpec;
            final ParametersWithSBox parametersWithSBox2 = new ParametersWithSBox(cipherParameters, gost28147ParameterSpec2.getSbox());
            if (gost28147ParameterSpec2.getIV() != null && this.ivLength != 0) {
                return new ParametersWithIV(parametersWithSBox2, gost28147ParameterSpec2.getIV());
            }
            return parametersWithSBox2;
        }
        return cipherParameters;
    }
    
    private boolean isAEADModeName(final String s) {
        return "CCM".equals(s) || "EAX".equals(s) || "GCM".equals(s) || "OCB".equals(s);
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, int processBytes, int doFinal, final byte[] array2, final int n) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        while (true) {
            Label_0082: {
                if (this.engineGetOutputSize(doFinal) + n > array2.length) {
                    break Label_0082;
                }
                Label_0038: {
                    if (doFinal == 0) {
                        processBytes = 0;
                        break Label_0038;
                    }
                    try {
                        processBytes = this.cipher.processBytes(array, processBytes, doFinal, array2, n);
                        doFinal = this.cipher.doFinal(array2, n + processBytes);
                        return processBytes + doFinal;
                        throw new ShortBufferException("output buffer too short for input.");
                        final OutputLengthException ex;
                        throw new IllegalBlockSizeException(ex.getMessage());
                    }
                    catch (DataLengthException ex3) {}
                    catch (OutputLengthException ex2) {}
                }
            }
            final OutputLengthException ex2;
            final OutputLengthException ex = ex2;
            continue;
        }
    }
    
    @Override
    protected byte[] engineDoFinal(byte[] array, int processBytes, int doFinal) throws IllegalBlockSizeException, BadPaddingException {
        final int engineGetOutputSize = this.engineGetOutputSize(doFinal);
        final byte[] array2 = new byte[engineGetOutputSize];
        if (doFinal != 0) {
            processBytes = this.cipher.processBytes(array, processBytes, doFinal, array2, 0);
        }
        else {
            processBytes = 0;
        }
        try {
            doFinal = this.cipher.doFinal(array2, processBytes);
            processBytes += doFinal;
            if (processBytes == engineGetOutputSize) {
                return array2;
            }
            array = new byte[processBytes];
            System.arraycopy(array2, 0, array, 0, processBytes);
            return array;
        }
        catch (DataLengthException ex) {
            throw new IllegalBlockSizeException(ex.getMessage());
        }
    }
    
    @Override
    protected int engineGetBlockSize() {
        return this.baseEngine.getBlockSize();
    }
    
    @Override
    protected byte[] engineGetIV() {
        final AEADParameters aeadParams = this.aeadParams;
        if (aeadParams != null) {
            return aeadParams.getNonce();
        }
        final ParametersWithIV ivParam = this.ivParam;
        if (ivParam != null) {
            return ivParam.getIV();
        }
        return null;
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length * 8;
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return this.cipher.getOutputSize(n);
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null) {
            if (this.pbeSpec != null) {
                try {
                    (this.engineParams = this.createParametersInstance(this.pbeAlgorithm)).init(this.pbeSpec);
                    return this.engineParams;
                }
                catch (Exception ex3) {
                    return null;
                }
            }
            if (this.aeadParams != null) {
                try {
                    (this.engineParams = this.createParametersInstance("GCM")).init(new GCMParameters(this.aeadParams.getNonce(), this.aeadParams.getMacSize() / 8).getEncoded());
                    return this.engineParams;
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex.toString());
                }
            }
            if (this.ivParam != null) {
                String s2;
                final String s = s2 = this.cipher.getUnderlyingCipher().getAlgorithmName();
                if (s.indexOf(47) >= 0) {
                    s2 = s.substring(0, s.indexOf(47));
                }
                try {
                    (this.engineParams = this.createParametersInstance(s2)).init(new IvParameterSpec(this.ivParam.getIV()));
                }
                catch (Exception ex2) {
                    throw new RuntimeException(ex2.toString());
                }
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        final AlgorithmParameterSpec algorithmParameterSpec = null;
        if (engineParams != null) {
            int n2 = 0;
            while (true) {
                final Class[] availableSpecs = this.availableSpecs;
                parameterSpec = algorithmParameterSpec;
                if (n2 == availableSpecs.length) {
                    break;
                }
                if (availableSpecs[n2] != null) {
                    try {
                        parameterSpec = engineParams.getParameterSpec((Class<AlgorithmParameterSpec>)availableSpecs[n2]);
                        break;
                    }
                    catch (Exception ex) {}
                }
                ++n2;
            }
            if (parameterSpec == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't handle parameter ");
                sb.append(engineParams.toString());
                throw new InvalidAlgorithmParameterException(sb.toString());
            }
        }
        this.engineInit(n, key, parameterSpec, secureRandom);
        this.engineParams = engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidKeyException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        final AEADParameters aeadParameters = null;
        final String s = null;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        this.aeadParams = null;
        if (!(key instanceof SecretKey)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Key for algorithm ");
            String algorithm = s;
            if (key != null) {
                algorithm = key.getAlgorithm();
            }
            sb.append(algorithm);
            sb.append(" not suitable for symmetric enryption.");
            throw new InvalidKeyException(sb.toString());
        }
        if (algorithmParameterSpec == null && this.baseEngine.getAlgorithmName().startsWith("RC5-64")) {
            throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
        }
        final int scheme = this.scheme;
        Label_0619: {
            if (scheme == 2) {
                break Label_0619;
            }
            if (key instanceof PKCS12Key) {
                break Label_0619;
            }
            Label_0892: {
                CipherParameters cipherParameters2;
                if (key instanceof PBKDF1Key) {
                    final PBKDF1Key pbkdf1Key = (PBKDF1Key)key;
                    if (algorithmParameterSpec instanceof PBEParameterSpec) {
                        this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                    }
                    if (pbkdf1Key instanceof PBKDF1KeyWithParameters && this.pbeSpec == null) {
                        final PBKDF1KeyWithParameters pbkdf1KeyWithParameters = (PBKDF1KeyWithParameters)pbkdf1Key;
                        this.pbeSpec = new PBEParameterSpec(pbkdf1KeyWithParameters.getSalt(), pbkdf1KeyWithParameters.getIterationCount());
                    }
                    final CipherParameters cipherParameters = cipherParameters2 = Util.makePBEParameters(pbkdf1Key.getEncoded(), 0, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
                    if (!(cipherParameters instanceof ParametersWithIV)) {
                        break Label_0892;
                    }
                    cipherParameters2 = cipherParameters;
                }
                else if (key instanceof BCPBEKey) {
                    final BCPBEKey bcpbeKey = (BCPBEKey)key;
                    String pbeAlgorithm;
                    if (bcpbeKey.getOID() != null) {
                        pbeAlgorithm = bcpbeKey.getOID().getId();
                    }
                    else {
                        pbeAlgorithm = bcpbeKey.getAlgorithm();
                    }
                    this.pbeAlgorithm = pbeAlgorithm;
                    CipherParameters cipherParameters3;
                    if (bcpbeKey.getParam() != null) {
                        cipherParameters3 = this.adjustParameters(algorithmParameterSpec, bcpbeKey.getParam());
                    }
                    else {
                        if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                            throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                        }
                        this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                        cipherParameters3 = Util.makePBEParameters(bcpbeKey, algorithmParameterSpec, this.cipher.getUnderlyingCipher().getAlgorithmName());
                    }
                    cipherParameters2 = cipherParameters3;
                    if (!(cipherParameters3 instanceof ParametersWithIV)) {
                        break Label_0892;
                    }
                    cipherParameters2 = cipherParameters3;
                }
                else if (key instanceof PBEKey) {
                    final PBEKey pbeKey = (PBEKey)key;
                    final PBEParameterSpec pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                    this.pbeSpec = pbeSpec;
                    if (pbeKey instanceof PKCS12KeyWithParameters && pbeSpec == null) {
                        this.pbeSpec = new PBEParameterSpec(pbeKey.getSalt(), pbeKey.getIterationCount());
                    }
                    final CipherParameters cipherParameters4 = cipherParameters2 = Util.makePBEParameters(pbeKey.getEncoded(), this.scheme, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
                    if (!(cipherParameters4 instanceof ParametersWithIV)) {
                        break Label_0892;
                    }
                    cipherParameters2 = cipherParameters4;
                }
                else {
                    cipherParameters2 = aeadParameters;
                    if (key instanceof RepeatedSecretKeySpec) {
                        break Label_0892;
                    }
                    if (scheme != 0 && scheme != 4 && scheme != 1 && scheme != 5) {
                        cipherParameters2 = new KeyParameter(key.getEncoded());
                        break Label_0892;
                    }
                    throw new InvalidKeyException("Algorithm requires a PBE key");
                }
                while (true) {
                    this.ivParam = (ParametersWithIV)cipherParameters2;
                    break Label_0892;
                    try {
                        final SecretKey secretKey = (SecretKey)key;
                        if (algorithmParameterSpec instanceof PBEParameterSpec) {
                            this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                        }
                        final boolean b = secretKey instanceof PBEKey;
                        if (b && this.pbeSpec == null) {
                            final PBEKey pbeKey2 = (PBEKey)secretKey;
                            if (pbeKey2.getSalt() == null) {
                                throw new InvalidAlgorithmParameterException("PBEKey requires parameters to specify salt");
                            }
                            this.pbeSpec = new PBEParameterSpec(pbeKey2.getSalt(), pbeKey2.getIterationCount());
                        }
                        if (this.pbeSpec == null && !b) {
                            throw new InvalidKeyException("Algorithm requires a PBE key");
                        }
                        CipherParameters pbeParameters;
                        if (key instanceof BCPBEKey) {
                            CipherParameters cipherParameters5 = ((BCPBEKey)key).getParam();
                            if (!(cipherParameters5 instanceof ParametersWithIV)) {
                                if (cipherParameters5 != null) {
                                    throw new InvalidKeyException("Algorithm requires a PBE key suitable for PKCS12");
                                }
                                cipherParameters5 = Util.makePBEParameters(secretKey.getEncoded(), 2, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
                            }
                            pbeParameters = cipherParameters5;
                        }
                        else {
                            pbeParameters = Util.makePBEParameters(secretKey.getEncoded(), 2, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
                        }
                        cipherParameters2 = pbeParameters;
                        if (pbeParameters instanceof ParametersWithIV) {
                            cipherParameters2 = pbeParameters;
                            continue;
                        }
                        CipherParameters cipherParameters6 = null;
                        Label_1876: {
                            if (algorithmParameterSpec instanceof AEADParameterSpec) {
                                if (!this.isAEADModeName(this.modeName) && !(this.cipher instanceof AEADGenericBlockCipher)) {
                                    throw new InvalidAlgorithmParameterException("AEADParameterSpec can only be used with AEAD modes.");
                                }
                                final AEADParameterSpec aeadParameterSpec = (AEADParameterSpec)algorithmParameterSpec;
                                KeyParameter keyParameter;
                                if (cipherParameters2 instanceof ParametersWithIV) {
                                    keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters2).getParameters();
                                }
                                else {
                                    keyParameter = (KeyParameter)cipherParameters2;
                                }
                                cipherParameters6 = new AEADParameters(keyParameter, aeadParameterSpec.getMacSizeInBits(), aeadParameterSpec.getNonce(), aeadParameterSpec.getAssociatedData());
                                this.aeadParams = (AEADParameters)cipherParameters6;
                            }
                            else if (algorithmParameterSpec instanceof IvParameterSpec) {
                                if (this.ivLength != 0) {
                                    final IvParameterSpec ivParameterSpec = (IvParameterSpec)algorithmParameterSpec;
                                    if (ivParameterSpec.getIV().length != this.ivLength && !(this.cipher instanceof AEADGenericBlockCipher) && this.fixedIv) {
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("IV must be ");
                                        sb2.append(this.ivLength);
                                        sb2.append(" bytes long.");
                                        throw new InvalidAlgorithmParameterException(sb2.toString());
                                    }
                                    if (cipherParameters2 instanceof ParametersWithIV) {
                                        cipherParameters6 = new ParametersWithIV(((ParametersWithIV)cipherParameters2).getParameters(), ivParameterSpec.getIV());
                                    }
                                    else {
                                        cipherParameters6 = new ParametersWithIV(cipherParameters2, ivParameterSpec.getIV());
                                    }
                                    this.ivParam = (ParametersWithIV)cipherParameters6;
                                }
                                else {
                                    final String modeName = this.modeName;
                                    cipherParameters6 = cipherParameters2;
                                    if (modeName != null) {
                                        if (modeName.equals("ECB")) {
                                            throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
                                        }
                                        cipherParameters6 = cipherParameters2;
                                    }
                                }
                            }
                            else {
                                if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
                                    final GOST28147ParameterSpec gost28147ParameterSpec = (GOST28147ParameterSpec)algorithmParameterSpec;
                                    final ParametersWithSBox parametersWithSBox = (ParametersWithSBox)(cipherParameters6 = new ParametersWithSBox(new KeyParameter(key.getEncoded()), gost28147ParameterSpec.getSbox()));
                                    if (gost28147ParameterSpec.getIV() == null) {
                                        break Label_1876;
                                    }
                                    cipherParameters6 = parametersWithSBox;
                                    if (this.ivLength == 0) {
                                        break Label_1876;
                                    }
                                    if (parametersWithSBox instanceof ParametersWithIV) {
                                        cipherParameters6 = new ParametersWithIV(((ParametersWithIV)parametersWithSBox).getParameters(), gost28147ParameterSpec.getIV());
                                    }
                                    else {
                                        cipherParameters6 = new ParametersWithIV(parametersWithSBox, gost28147ParameterSpec.getIV());
                                    }
                                }
                                else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                                    final RC2ParameterSpec rc2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
                                    final RC2Parameters rc2Parameters = (RC2Parameters)(cipherParameters6 = new RC2Parameters(key.getEncoded(), rc2ParameterSpec.getEffectiveKeyBits()));
                                    if (rc2ParameterSpec.getIV() == null) {
                                        break Label_1876;
                                    }
                                    cipherParameters6 = rc2Parameters;
                                    if (this.ivLength == 0) {
                                        break Label_1876;
                                    }
                                    if (rc2Parameters instanceof ParametersWithIV) {
                                        cipherParameters6 = new ParametersWithIV(((ParametersWithIV)rc2Parameters).getParameters(), rc2ParameterSpec.getIV());
                                    }
                                    else {
                                        cipherParameters6 = new ParametersWithIV(rc2Parameters, rc2ParameterSpec.getIV());
                                    }
                                }
                                else if (algorithmParameterSpec instanceof RC5ParameterSpec) {
                                    final RC5ParameterSpec rc5ParameterSpec = (RC5ParameterSpec)algorithmParameterSpec;
                                    final RC5Parameters rc5Parameters = new RC5Parameters(key.getEncoded(), rc5ParameterSpec.getRounds());
                                    if (!this.baseEngine.getAlgorithmName().startsWith("RC5")) {
                                        throw new InvalidAlgorithmParameterException("RC5 parameters passed to a cipher that is not RC5.");
                                    }
                                    if (this.baseEngine.getAlgorithmName().equals("RC5-32")) {
                                        if (rc5ParameterSpec.getWordSize() != 32) {
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append("RC5 already set up for a word size of 32 not ");
                                            sb3.append(rc5ParameterSpec.getWordSize());
                                            sb3.append(".");
                                            throw new InvalidAlgorithmParameterException(sb3.toString());
                                        }
                                    }
                                    else if (this.baseEngine.getAlgorithmName().equals("RC5-64")) {
                                        if (rc5ParameterSpec.getWordSize() != 64) {
                                            final StringBuilder sb4 = new StringBuilder();
                                            sb4.append("RC5 already set up for a word size of 64 not ");
                                            sb4.append(rc5ParameterSpec.getWordSize());
                                            sb4.append(".");
                                            throw new InvalidAlgorithmParameterException(sb4.toString());
                                        }
                                    }
                                    cipherParameters6 = rc5Parameters;
                                    if (rc5ParameterSpec.getIV() == null) {
                                        break Label_1876;
                                    }
                                    cipherParameters6 = rc5Parameters;
                                    if (this.ivLength == 0) {
                                        break Label_1876;
                                    }
                                    if (rc5Parameters instanceof ParametersWithIV) {
                                        cipherParameters6 = new ParametersWithIV(((ParametersWithIV)rc5Parameters).getParameters(), rc5ParameterSpec.getIV());
                                    }
                                    else {
                                        cipherParameters6 = new ParametersWithIV(rc5Parameters, rc5ParameterSpec.getIV());
                                    }
                                }
                                else {
                                    final Class gcmSpecClass = BaseBlockCipher.gcmSpecClass;
                                    if (gcmSpecClass != null && gcmSpecClass.isInstance(algorithmParameterSpec)) {
                                        if (!this.isAEADModeName(this.modeName)) {
                                            if (!(this.cipher instanceof AEADGenericBlockCipher)) {
                                                throw new InvalidAlgorithmParameterException("GCMParameterSpec can only be used with AEAD modes.");
                                            }
                                        }
                                        try {
                                            final Method declaredMethod = BaseBlockCipher.gcmSpecClass.getDeclaredMethod("getTLen", (Class[])new Class[0]);
                                            final Method declaredMethod2 = BaseBlockCipher.gcmSpecClass.getDeclaredMethod("getIV", (Class[])new Class[0]);
                                            CipherParameters parameters = cipherParameters2;
                                            if (cipherParameters2 instanceof ParametersWithIV) {
                                                parameters = ((ParametersWithIV)cipherParameters2).getParameters();
                                            }
                                            cipherParameters6 = new AEADParameters((KeyParameter)parameters, (int)declaredMethod.invoke(algorithmParameterSpec, new Object[0]), (byte[])declaredMethod2.invoke(algorithmParameterSpec, new Object[0]));
                                            this.aeadParams = (AEADParameters)cipherParameters6;
                                            break Label_1876;
                                        }
                                        catch (Exception ex2) {
                                            throw new InvalidAlgorithmParameterException("Cannot process GCMParameterSpec.");
                                        }
                                    }
                                    cipherParameters6 = cipherParameters2;
                                    if (algorithmParameterSpec == null) {
                                        break Label_1876;
                                    }
                                    if (algorithmParameterSpec instanceof PBEParameterSpec) {
                                        cipherParameters6 = cipherParameters2;
                                        break Label_1876;
                                    }
                                    throw new InvalidAlgorithmParameterException("unknown parameter type.");
                                }
                                this.ivParam = (ParametersWithIV)cipherParameters6;
                            }
                        }
                        if (this.ivLength != 0 && !(cipherParameters6 instanceof ParametersWithIV) && !(cipherParameters6 instanceof AEADParameters)) {
                            SecureRandom secureRandom2;
                            if (secureRandom == null) {
                                secureRandom2 = new SecureRandom();
                            }
                            else {
                                secureRandom2 = secureRandom;
                            }
                            if (n != 1 && n != 3) {
                                if (this.cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") < 0) {
                                    throw new InvalidAlgorithmParameterException("no IV set when one expected");
                                }
                            }
                            else {
                                final byte[] array = new byte[this.ivLength];
                                secureRandom2.nextBytes(array);
                                cipherParameters6 = new ParametersWithIV(cipherParameters6, array);
                                this.ivParam = (ParametersWithIV)cipherParameters6;
                            }
                        }
                        CipherParameters cipherParameters7 = cipherParameters6;
                        if (secureRandom != null) {
                            cipherParameters7 = cipherParameters6;
                            if (this.padded) {
                                cipherParameters7 = new ParametersWithRandom(cipherParameters6, secureRandom);
                            }
                        }
                        Label_2108: {
                            if (n == 1) {
                                break Label_2108;
                            }
                            Label_2094: {
                                if (n == 2) {
                                    break Label_2094;
                                }
                                if (n == 3) {
                                    break Label_2108;
                                }
                                if (n == 4) {
                                    break Label_2094;
                                }
                                try {
                                    final StringBuilder sb5 = new StringBuilder();
                                    sb5.append("unknown opmode ");
                                    sb5.append(n);
                                    sb5.append(" passed");
                                    throw new InvalidParameterException(sb5.toString());
                                    // iftrue(Label_2185:, !this.cipher instanceof AEADGenericBlockCipher || this.aeadParams != null)
                                    while (true) {
                                        this.aeadParams = new AEADParameters((KeyParameter)this.ivParam.getParameters(), ((AEADGenericBlockCipher)this.cipher).cipher.getMac().length * 8, this.ivParam.getIV());
                                        Label_2185: {
                                            return;
                                        }
                                        this.cipher.init(true, cipherParameters7);
                                        continue;
                                    }
                                    this.cipher.init(false, cipherParameters7);
                                }
                                catch (Exception ex) {
                                    throw new InvalidKeyOrParametersException(ex.getMessage(), ex);
                                }
                            }
                        }
                    }
                    catch (Exception ex3) {
                        throw new InvalidKeyException("PKCS12 requires a SecretKey/PBEKey");
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    protected void engineSetMode(final String s) throws NoSuchAlgorithmException {
        final String upperCase = Strings.toUpperCase(s);
        this.modeName = upperCase;
        if (upperCase.equals("ECB")) {
            this.ivLength = 0;
            this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(this.baseEngine);
            return;
        }
        if (this.modeName.equals("CBC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new CBCBlockCipher(this.baseEngine));
            return;
        }
        if (this.modeName.startsWith("OFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
                return;
            }
            final BlockCipher baseEngine = this.baseEngine;
            this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new OFBBlockCipher(baseEngine, baseEngine.getBlockSize() * 8));
        }
        else if (this.modeName.startsWith("CFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
                return;
            }
            final BlockCipher baseEngine2 = this.baseEngine;
            this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new CFBBlockCipher(baseEngine2, baseEngine2.getBlockSize() * 8));
        }
        else {
            if (this.modeName.startsWith("PGP")) {
                final boolean equalsIgnoreCase = this.modeName.equalsIgnoreCase("PGPCFBwithIV");
                this.ivLength = this.baseEngine.getBlockSize();
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new PGPCFBBlockCipher(this.baseEngine, equalsIgnoreCase));
                return;
            }
            if (this.modeName.equalsIgnoreCase("OpenPGPCFB")) {
                this.ivLength = 0;
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
                return;
            }
            if (this.modeName.startsWith("SIC")) {
                if ((this.ivLength = this.baseEngine.getBlockSize()) >= 16) {
                    this.fixedIv = false;
                    this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
                    return;
                }
                throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
            }
            else if (this.modeName.startsWith("CTR")) {
                this.ivLength = this.baseEngine.getBlockSize();
                this.fixedIv = false;
                if (this.baseEngine instanceof DSTU7624Engine) {
                    this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new BufferedBlockCipher(new KCTRBlockCipher(this.baseEngine)));
                    return;
                }
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
            }
            else {
                if (this.modeName.startsWith("GOFB")) {
                    this.ivLength = this.baseEngine.getBlockSize();
                    this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine)));
                    return;
                }
                if (this.modeName.startsWith("GCFB")) {
                    this.ivLength = this.baseEngine.getBlockSize();
                    this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new BufferedBlockCipher(new GCFBBlockCipher(this.baseEngine)));
                    return;
                }
                if (this.modeName.startsWith("CTS")) {
                    this.ivLength = this.baseEngine.getBlockSize();
                    this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(new CTSBlockCipher(new CBCBlockCipher(this.baseEngine)));
                    return;
                }
                if (this.modeName.startsWith("CCM")) {
                    this.ivLength = 13;
                    if (this.baseEngine instanceof DSTU7624Engine) {
                        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new KCCMBlockCipher(this.baseEngine));
                        return;
                    }
                    this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new CCMBlockCipher(this.baseEngine));
                }
                else if (this.modeName.startsWith("OCB")) {
                    if (this.engineProvider != null) {
                        this.ivLength = 15;
                        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new OCBBlockCipher(this.baseEngine, this.engineProvider.get()));
                        return;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("can't support mode ");
                    sb.append(s);
                    throw new NoSuchAlgorithmException(sb.toString());
                }
                else {
                    if (this.modeName.startsWith("EAX")) {
                        this.ivLength = this.baseEngine.getBlockSize();
                        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new EAXBlockCipher(this.baseEngine));
                        return;
                    }
                    if (!this.modeName.startsWith("GCM")) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("can't support mode ");
                        sb2.append(s);
                        throw new NoSuchAlgorithmException(sb2.toString());
                    }
                    this.ivLength = this.baseEngine.getBlockSize();
                    if (this.baseEngine instanceof DSTU7624Engine) {
                        this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new KGCMBlockCipher(this.baseEngine));
                        return;
                    }
                    this.cipher = (GenericBlockCipher)new AEADGenericBlockCipher(new GCMBlockCipher(this.baseEngine));
                }
            }
        }
    }
    
    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(s);
        BufferedGenericBlockCipher cipher;
        if (upperCase.equals("NOPADDING")) {
            if (!this.cipher.wrapOnNoPadding()) {
                return;
            }
            cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(this.cipher.getUnderlyingCipher()));
        }
        else if (upperCase.equals("WITHCTS")) {
            cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(this.cipher.getUnderlyingCipher()));
        }
        else {
            this.padded = true;
            if (this.isAEADModeName(this.modeName)) {
                throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
            }
            if (upperCase.equals("PKCS5PADDING") || upperCase.equals("PKCS7PADDING")) {
                this.cipher = (GenericBlockCipher)new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher());
                return;
            }
            if (upperCase.equals("ZEROBYTEPADDING")) {
                cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
            }
            else if (!upperCase.equals("ISO10126PADDING") && !upperCase.equals("ISO10126-2PADDING")) {
                if (!upperCase.equals("X9.23PADDING") && !upperCase.equals("X923PADDING")) {
                    if (!upperCase.equals("ISO7816-4PADDING") && !upperCase.equals("ISO9797-1PADDING")) {
                        if (!upperCase.equals("TBCPADDING")) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Padding ");
                            sb.append(s);
                            sb.append(" unknown.");
                            throw new NoSuchPaddingException(sb.toString());
                        }
                        cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
                    }
                    else {
                        cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
                    }
                }
                else {
                    cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
                }
            }
            else {
                cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
            }
        }
        this.cipher = (GenericBlockCipher)cipher;
    }
    
    @Override
    protected int engineUpdate(final byte[] array, int processBytes, final int n, final byte[] array2, final int n2) throws ShortBufferException {
        if (this.cipher.getUpdateOutputSize(n) + n2 <= array2.length) {
            try {
                processBytes = this.cipher.processBytes(array, processBytes, n, array2, n2);
                return processBytes;
            }
            catch (DataLengthException ex) {
                throw new IllegalStateException(ex.toString());
            }
        }
        throw new ShortBufferException("output buffer too short for input.");
    }
    
    @Override
    protected byte[] engineUpdate(byte[] array, int processBytes, final int n) {
        final int updateOutputSize = this.cipher.getUpdateOutputSize(n);
        if (updateOutputSize <= 0) {
            this.cipher.processBytes(array, processBytes, n, null, 0);
            return null;
        }
        final byte[] array2 = new byte[updateOutputSize];
        processBytes = this.cipher.processBytes(array, processBytes, n, array2, 0);
        if (processBytes == 0) {
            return null;
        }
        if (processBytes != updateOutputSize) {
            array = new byte[processBytes];
            System.arraycopy(array2, 0, array, 0, processBytes);
            return array;
        }
        return array2;
    }
    
    @Override
    protected void engineUpdateAAD(final ByteBuffer byteBuffer) {
        this.engineUpdateAAD(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.limit() - byteBuffer.position());
    }
    
    @Override
    protected void engineUpdateAAD(final byte[] array, final int n, final int n2) {
        this.cipher.updateAAD(array, n, n2);
    }
    
    private static class AEADGenericBlockCipher implements GenericBlockCipher
    {
        private static final Constructor aeadBadTagConstructor;
        private AEADBlockCipher cipher;
        
        static {
            final Class loadClass = ClassUtil.loadClass(BaseBlockCipher.class, "javax.crypto.AEADBadTagException");
            Constructor exceptionConstructor;
            if (loadClass != null) {
                exceptionConstructor = findExceptionConstructor(loadClass);
            }
            else {
                exceptionConstructor = null;
            }
            aeadBadTagConstructor = exceptionConstructor;
        }
        
        AEADGenericBlockCipher(final AEADBlockCipher cipher) {
            this.cipher = cipher;
        }
        
        private static Constructor findExceptionConstructor(final Class clazz) {
            try {
                return clazz.getConstructor(String.class);
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        @Override
        public int doFinal(final byte[] array, int doFinal) throws IllegalStateException, BadPaddingException {
            try {
                doFinal = this.cipher.doFinal(array, doFinal);
                return doFinal;
            }
            catch (InvalidCipherTextException ex2) {
                final Constructor aeadBadTagConstructor = AEADGenericBlockCipher.aeadBadTagConstructor;
                if (aeadBadTagConstructor != null) {
                    BadPaddingException ex;
                    try {
                        ex = aeadBadTagConstructor.newInstance(ex2.getMessage());
                    }
                    catch (Exception ex3) {
                        ex = null;
                    }
                    if (ex != null) {
                        throw ex;
                    }
                }
                throw new BadPaddingException(ex2.getMessage());
            }
        }
        
        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }
        
        @Override
        public int getOutputSize(final int n) {
            return this.cipher.getOutputSize(n);
        }
        
        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }
        
        @Override
        public int getUpdateOutputSize(final int n) {
            return this.cipher.getUpdateOutputSize(n);
        }
        
        @Override
        public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(b, cipherParameters);
        }
        
        @Override
        public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
            return this.cipher.processByte(b, array, n);
        }
        
        @Override
        public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
            return this.cipher.processBytes(array, n, n2, array2, n3);
        }
        
        @Override
        public void updateAAD(final byte[] array, final int n, final int n2) {
            this.cipher.processAADBytes(array, n, n2);
        }
        
        @Override
        public boolean wrapOnNoPadding() {
            return false;
        }
    }
    
    private static class BufferedGenericBlockCipher implements GenericBlockCipher
    {
        private BufferedBlockCipher cipher;
        
        BufferedGenericBlockCipher(final BlockCipher blockCipher) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        }
        
        BufferedGenericBlockCipher(final BlockCipher blockCipher, final BlockCipherPadding blockCipherPadding) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher, blockCipherPadding);
        }
        
        BufferedGenericBlockCipher(final BufferedBlockCipher cipher) {
            this.cipher = cipher;
        }
        
        @Override
        public int doFinal(final byte[] array, int doFinal) throws IllegalStateException, BadPaddingException {
            try {
                doFinal = this.cipher.doFinal(array, doFinal);
                return doFinal;
            }
            catch (InvalidCipherTextException ex) {
                throw new BadPaddingException(ex.getMessage());
            }
        }
        
        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }
        
        @Override
        public int getOutputSize(final int n) {
            return this.cipher.getOutputSize(n);
        }
        
        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }
        
        @Override
        public int getUpdateOutputSize(final int n) {
            return this.cipher.getUpdateOutputSize(n);
        }
        
        @Override
        public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(b, cipherParameters);
        }
        
        @Override
        public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
            return this.cipher.processByte(b, array, n);
        }
        
        @Override
        public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
            return this.cipher.processBytes(array, n, n2, array2, n3);
        }
        
        @Override
        public void updateAAD(final byte[] array, final int n, final int n2) {
            throw new UnsupportedOperationException("AAD is not supported in the current mode.");
        }
        
        @Override
        public boolean wrapOnNoPadding() {
            return this.cipher instanceof CTSBlockCipher ^ true;
        }
    }
    
    private interface GenericBlockCipher
    {
        int doFinal(final byte[] p0, final int p1) throws IllegalStateException, BadPaddingException;
        
        String getAlgorithmName();
        
        int getOutputSize(final int p0);
        
        BlockCipher getUnderlyingCipher();
        
        int getUpdateOutputSize(final int p0);
        
        void init(final boolean p0, final CipherParameters p1) throws IllegalArgumentException;
        
        int processByte(final byte p0, final byte[] p1, final int p2) throws DataLengthException;
        
        int processBytes(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws DataLengthException;
        
        void updateAAD(final byte[] p0, final int p1, final int p2);
        
        boolean wrapOnNoPadding();
    }
    
    private static class InvalidKeyOrParametersException extends InvalidKeyException
    {
        private final Throwable cause;
        
        InvalidKeyOrParametersException(final String s, final Throwable cause) {
            super(s);
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
