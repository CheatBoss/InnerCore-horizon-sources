package org.spongycastle.jcajce.provider.symmetric.util;

import org.spongycastle.jcajce.spec.*;
import org.spongycastle.jcajce.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jce.provider.*;
import java.security.spec.*;
import org.spongycastle.crypto.*;
import java.security.*;

public abstract class BaseWrapCipher extends CipherSpi implements PBE
{
    private Class[] availableSpecs;
    protected AlgorithmParameters engineParams;
    private final JcaJceHelper helper;
    private byte[] iv;
    private int ivSize;
    protected int pbeHash;
    protected int pbeIvSize;
    protected int pbeKeySize;
    protected int pbeType;
    protected Wrapper wrapEngine;
    
    protected BaseWrapCipher() {
        this.availableSpecs = new Class[] { GOST28147WrapParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.engineParams = null;
        this.wrapEngine = null;
        this.helper = new BCJcaJceHelper();
    }
    
    protected BaseWrapCipher(final Wrapper wrapper) {
        this(wrapper, 0);
    }
    
    protected BaseWrapCipher(final Wrapper wrapEngine, final int ivSize) {
        this.availableSpecs = new Class[] { GOST28147WrapParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.engineParams = null;
        this.wrapEngine = null;
        this.helper = new BCJcaJceHelper();
        this.wrapEngine = wrapEngine;
        this.ivSize = ivSize;
    }
    
    protected final AlgorithmParameters createParametersInstance(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return this.helper.createAlgorithmParameters(s);
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws IllegalBlockSizeException, BadPaddingException, ShortBufferException {
        return 0;
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        return null;
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        return Arrays.clone(this.iv);
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length * 8;
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return -1;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
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
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        CipherParameters cipherParameters;
        if (key instanceof BCPBEKey) {
            final BCPBEKey bcpbeKey = (BCPBEKey)key;
            if (algorithmParameterSpec instanceof PBEParameterSpec) {
                cipherParameters = Util.makePBEParameters(bcpbeKey, algorithmParameterSpec, this.wrapEngine.getAlgorithmName());
            }
            else {
                if (bcpbeKey.getParam() == null) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters = bcpbeKey.getParam();
            }
        }
        else {
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        CipherParameters cipherParameters2 = cipherParameters;
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters2 = new ParametersWithIV(cipherParameters, ((IvParameterSpec)algorithmParameterSpec).getIV());
        }
        CipherParameters cipherParameters3 = cipherParameters2;
        if (algorithmParameterSpec instanceof GOST28147WrapParameterSpec) {
            final GOST28147WrapParameterSpec gost28147WrapParameterSpec = (GOST28147WrapParameterSpec)algorithmParameterSpec;
            final byte[] sBox = gost28147WrapParameterSpec.getSBox();
            CipherParameters cipherParameters4 = cipherParameters2;
            if (sBox != null) {
                cipherParameters4 = new ParametersWithSBox(cipherParameters2, sBox);
            }
            cipherParameters3 = new ParametersWithUKM(cipherParameters4, gost28147WrapParameterSpec.getUKM());
        }
        CipherParameters cipherParameters5 = cipherParameters3;
        if (cipherParameters3 instanceof KeyParameter) {
            final int ivSize = this.ivSize;
            cipherParameters5 = cipherParameters3;
            if (ivSize != 0) {
                secureRandom.nextBytes(this.iv = new byte[ivSize]);
                cipherParameters5 = new ParametersWithIV(cipherParameters3, this.iv);
            }
        }
        CipherParameters cipherParameters6 = cipherParameters5;
        if (secureRandom != null) {
            cipherParameters6 = new ParametersWithRandom(cipherParameters5, secureRandom);
        }
        if (n == 1 || n == 2) {
            throw new IllegalArgumentException("engine only valid for wrapping");
        }
        if (n == 3) {
            this.wrapEngine.init(true, cipherParameters6);
            return;
        }
        if (n != 4) {
            System.out.println("eeek!");
            return;
        }
        this.wrapEngine.init(false, cipherParameters6);
    }
    
    @Override
    protected void engineSetMode(final String s) throws NoSuchAlgorithmException {
        final StringBuilder sb = new StringBuilder();
        sb.append("can't support mode ");
        sb.append(s);
        throw new NoSuchAlgorithmException(sb.toString());
    }
    
    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Padding ");
        sb.append(s);
        sb.append(" unknown.");
        throw new NoSuchPaddingException(sb.toString());
    }
    
    @Override
    protected Key engineUnwrap(byte[] array, final String s, final int n) throws InvalidKeyException, NoSuchAlgorithmException {
        try {
            if (this.wrapEngine == null) {
                array = this.engineDoFinal(array, 0, array.length);
            }
            else {
                array = this.wrapEngine.unwrap(array, 0, array.length);
            }
            if (n == 3) {
                return new SecretKeySpec(array, s);
            }
            if (s.equals("") && n == 2) {
                try {
                    final PrivateKeyInfo instance = PrivateKeyInfo.getInstance(array);
                    final PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(instance);
                    if (privateKey != null) {
                        return privateKey;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("algorithm ");
                    sb.append(instance.getPrivateKeyAlgorithm().getAlgorithm());
                    sb.append(" not supported");
                    throw new InvalidKeyException(sb.toString());
                }
                catch (Exception ex6) {
                    throw new InvalidKeyException("Invalid key encoding.");
                }
            }
            try {
                final KeyFactory keyFactory = this.helper.createKeyFactory(s);
                if (n == 1) {
                    return keyFactory.generatePublic(new X509EncodedKeySpec(array));
                }
                if (n == 2) {
                    return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(array));
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown key type ");
                sb2.append(n);
                throw new InvalidKeyException(sb2.toString());
            }
            catch (InvalidKeySpecException ex) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown key type ");
                sb3.append(ex.getMessage());
                throw new InvalidKeyException(sb3.toString());
            }
            catch (NoSuchProviderException ex2) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Unknown key type ");
                sb4.append(ex2.getMessage());
                throw new InvalidKeyException(sb4.toString());
            }
        }
        catch (IllegalBlockSizeException ex3) {
            throw new InvalidKeyException(ex3.getMessage());
        }
        catch (BadPaddingException ex4) {
            throw new InvalidKeyException(ex4.getMessage());
        }
        catch (InvalidCipherTextException ex5) {
            throw new InvalidKeyException(ex5.getMessage());
        }
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        throw new RuntimeException("not supported for wrapping");
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        throw new RuntimeException("not supported for wrapping");
    }
    
    @Override
    protected byte[] engineWrap(final Key key) throws IllegalBlockSizeException, InvalidKeyException {
        final byte[] encoded = key.getEncoded();
        if (encoded != null) {
            try {
                if (this.wrapEngine == null) {
                    return this.engineDoFinal(encoded, 0, encoded.length);
                }
                return this.wrapEngine.wrap(encoded, 0, encoded.length);
            }
            catch (BadPaddingException ex) {
                throw new IllegalBlockSizeException(ex.getMessage());
            }
        }
        throw new InvalidKeyException("Cannot wrap key, null encoding.");
    }
}
