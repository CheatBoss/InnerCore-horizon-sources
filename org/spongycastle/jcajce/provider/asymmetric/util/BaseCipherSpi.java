package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jcajce.util.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jce.provider.*;
import java.security.spec.*;
import javax.crypto.*;
import org.spongycastle.crypto.*;
import java.security.*;

public abstract class BaseCipherSpi extends CipherSpi
{
    private Class[] availableSpecs;
    protected AlgorithmParameters engineParams;
    private final JcaJceHelper helper;
    private byte[] iv;
    private int ivSize;
    protected Wrapper wrapEngine;
    
    protected BaseCipherSpi() {
        this.availableSpecs = new Class[] { IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class };
        this.helper = new BCJcaJceHelper();
        this.engineParams = null;
        this.wrapEngine = null;
    }
    
    protected final AlgorithmParameters createParametersInstance(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return this.helper.createAlgorithmParameters(s);
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        return null;
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length;
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
    protected Key engineUnwrap(byte[] array, final String s, final int n) throws InvalidKeyException {
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
                catch (Exception ex7) {
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
            catch (NoSuchProviderException ex) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown key type ");
                sb3.append(ex.getMessage());
                throw new InvalidKeyException(sb3.toString());
            }
            catch (InvalidKeySpecException ex2) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Unknown key type ");
                sb4.append(ex2.getMessage());
                throw new InvalidKeyException(sb4.toString());
            }
            catch (NoSuchAlgorithmException ex3) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Unknown key type ");
                sb5.append(ex3.getMessage());
                throw new InvalidKeyException(sb5.toString());
            }
        }
        catch (IllegalBlockSizeException ex4) {
            throw new InvalidKeyException(ex4.getMessage());
        }
        catch (BadPaddingException ex5) {
            throw new InvalidKeyException("unable to unwrap") {
                @Override
                public Throwable getCause() {
                    synchronized (this) {
                        return ex5;
                    }
                }
            };
        }
        catch (InvalidCipherTextException ex6) {
            throw new InvalidKeyException(ex6.getMessage());
        }
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
