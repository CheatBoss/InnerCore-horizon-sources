package org.spongycastle.jcajce.util;

import java.security.cert.*;
import javax.crypto.*;
import java.security.*;

public class NamedJcaJceHelper implements JcaJceHelper
{
    protected final String providerName;
    
    public NamedJcaJceHelper(final String providerName) {
        this.providerName = providerName;
    }
    
    @Override
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return AlgorithmParameterGenerator.getInstance(s, this.providerName);
    }
    
    @Override
    public AlgorithmParameters createAlgorithmParameters(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return AlgorithmParameters.getInstance(s, this.providerName);
    }
    
    @Override
    public CertificateFactory createCertificateFactory(final String s) throws CertificateException, NoSuchProviderException {
        return CertificateFactory.getInstance(s, this.providerName);
    }
    
    @Override
    public Cipher createCipher(final String s) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        return Cipher.getInstance(s, this.providerName);
    }
    
    @Override
    public MessageDigest createDigest(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return MessageDigest.getInstance(s, this.providerName);
    }
    
    @Override
    public KeyAgreement createKeyAgreement(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyAgreement.getInstance(s, this.providerName);
    }
    
    @Override
    public KeyFactory createKeyFactory(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyFactory.getInstance(s, this.providerName);
    }
    
    @Override
    public KeyGenerator createKeyGenerator(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyGenerator.getInstance(s, this.providerName);
    }
    
    @Override
    public KeyPairGenerator createKeyPairGenerator(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyPairGenerator.getInstance(s, this.providerName);
    }
    
    @Override
    public Mac createMac(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return Mac.getInstance(s, this.providerName);
    }
    
    @Override
    public SecretKeyFactory createSecretKeyFactory(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return SecretKeyFactory.getInstance(s, this.providerName);
    }
    
    @Override
    public SecureRandom createSecureRandom(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return SecureRandom.getInstance(s, this.providerName);
    }
    
    @Override
    public Signature createSignature(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return Signature.getInstance(s, this.providerName);
    }
}
