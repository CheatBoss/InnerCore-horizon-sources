package org.spongycastle.jcajce.util;

import java.security.cert.*;
import javax.crypto.*;
import java.security.*;

public interface JcaJceHelper
{
    AlgorithmParameterGenerator createAlgorithmParameterGenerator(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    AlgorithmParameters createAlgorithmParameters(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    CertificateFactory createCertificateFactory(final String p0) throws NoSuchProviderException, CertificateException;
    
    Cipher createCipher(final String p0) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException;
    
    MessageDigest createDigest(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    KeyAgreement createKeyAgreement(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    KeyFactory createKeyFactory(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    KeyGenerator createKeyGenerator(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    KeyPairGenerator createKeyPairGenerator(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    Mac createMac(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    SecretKeyFactory createSecretKeyFactory(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    SecureRandom createSecureRandom(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
    
    Signature createSignature(final String p0) throws NoSuchAlgorithmException, NoSuchProviderException;
}
