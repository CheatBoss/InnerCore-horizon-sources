package org.spongycastle.x509;

import java.util.*;
import java.io.*;
import java.math.*;
import java.security.cert.*;
import java.security.*;

public interface X509AttributeCertificate extends X509Extension
{
    void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException;
    
    void checkValidity(final Date p0) throws CertificateExpiredException, CertificateNotYetValidException;
    
    X509Attribute[] getAttributes();
    
    X509Attribute[] getAttributes(final String p0);
    
    byte[] getEncoded() throws IOException;
    
    AttributeCertificateHolder getHolder();
    
    AttributeCertificateIssuer getIssuer();
    
    boolean[] getIssuerUniqueID();
    
    Date getNotAfter();
    
    Date getNotBefore();
    
    BigInteger getSerialNumber();
    
    byte[] getSignature();
    
    int getVersion();
    
    void verify(final PublicKey p0, final String p1) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
}
