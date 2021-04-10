package com.microsoft.aad.adal;

import java.security.cert.*;
import java.security.interfaces.*;
import java.util.*;

public interface IDeviceCertificate
{
    X509Certificate getCertificate();
    
    RSAPrivateKey getRSAPrivateKey();
    
    RSAPublicKey getRSAPublicKey();
    
    String getThumbPrint();
    
    boolean isValidIssuer(final List<String> p0);
}
