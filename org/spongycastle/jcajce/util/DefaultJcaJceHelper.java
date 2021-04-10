package org.spongycastle.jcajce.util;

import java.security.cert.*;
import javax.crypto.*;
import java.security.*;

public class DefaultJcaJceHelper implements JcaJceHelper
{
    @Override
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(final String s) throws NoSuchAlgorithmException {
        return AlgorithmParameterGenerator.getInstance(s);
    }
    
    @Override
    public AlgorithmParameters createAlgorithmParameters(final String s) throws NoSuchAlgorithmException {
        return AlgorithmParameters.getInstance(s);
    }
    
    @Override
    public CertificateFactory createCertificateFactory(final String s) throws CertificateException {
        return CertificateFactory.getInstance(s);
    }
    
    @Override
    public Cipher createCipher(final String s) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(s);
    }
    
    @Override
    public MessageDigest createDigest(final String s) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(s);
    }
    
    @Override
    public KeyAgreement createKeyAgreement(final String s) throws NoSuchAlgorithmException {
        return KeyAgreement.getInstance(s);
    }
    
    @Override
    public KeyFactory createKeyFactory(final String s) throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(s);
    }
    
    @Override
    public KeyGenerator createKeyGenerator(final String s) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(s);
    }
    
    @Override
    public KeyPairGenerator createKeyPairGenerator(final String s) throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(s);
    }
    
    @Override
    public Mac createMac(final String s) throws NoSuchAlgorithmException {
        return Mac.getInstance(s);
    }
    
    @Override
    public SecretKeyFactory createSecretKeyFactory(final String s) throws NoSuchAlgorithmException {
        return SecretKeyFactory.getInstance(s);
    }
    
    @Override
    public SecureRandom createSecureRandom(final String s) throws NoSuchAlgorithmException {
        return SecureRandom.getInstance(s);
    }
    
    @Override
    public Signature createSignature(final String s) throws NoSuchAlgorithmException {
        return Signature.getInstance(s);
    }
}
