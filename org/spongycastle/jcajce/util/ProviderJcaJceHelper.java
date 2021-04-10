package org.spongycastle.jcajce.util;

import java.security.cert.*;
import javax.crypto.*;
import java.security.*;

public class ProviderJcaJceHelper implements JcaJceHelper
{
    protected final Provider provider;
    
    public ProviderJcaJceHelper(final Provider provider) {
        this.provider = provider;
    }
    
    @Override
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(final String s) throws NoSuchAlgorithmException {
        return AlgorithmParameterGenerator.getInstance(s, this.provider);
    }
    
    @Override
    public AlgorithmParameters createAlgorithmParameters(final String s) throws NoSuchAlgorithmException {
        return AlgorithmParameters.getInstance(s, this.provider);
    }
    
    @Override
    public CertificateFactory createCertificateFactory(final String s) throws CertificateException {
        return CertificateFactory.getInstance(s, this.provider);
    }
    
    @Override
    public Cipher createCipher(final String s) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(s, this.provider);
    }
    
    @Override
    public MessageDigest createDigest(final String s) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(s, this.provider);
    }
    
    @Override
    public KeyAgreement createKeyAgreement(final String s) throws NoSuchAlgorithmException {
        return KeyAgreement.getInstance(s, this.provider);
    }
    
    @Override
    public KeyFactory createKeyFactory(final String s) throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(s, this.provider);
    }
    
    @Override
    public KeyGenerator createKeyGenerator(final String s) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(s, this.provider);
    }
    
    @Override
    public KeyPairGenerator createKeyPairGenerator(final String s) throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(s, this.provider);
    }
    
    @Override
    public Mac createMac(final String s) throws NoSuchAlgorithmException {
        return Mac.getInstance(s, this.provider);
    }
    
    @Override
    public SecretKeyFactory createSecretKeyFactory(final String s) throws NoSuchAlgorithmException {
        return SecretKeyFactory.getInstance(s, this.provider);
    }
    
    @Override
    public SecureRandom createSecureRandom(final String s) throws NoSuchAlgorithmException {
        return SecureRandom.getInstance(s, this.provider);
    }
    
    @Override
    public Signature createSignature(final String s) throws NoSuchAlgorithmException {
        return Signature.getInstance(s, this.provider);
    }
}
