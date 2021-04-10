package org.spongycastle.jce.provider;

import org.spongycastle.asn1.x500.*;
import javax.security.auth.x500.*;
import org.spongycastle.x509.*;
import java.security.cert.*;

class PrincipalUtils
{
    static X500Name getCA(final TrustAnchor trustAnchor) {
        return X500Name.getInstance(trustAnchor.getCA().getEncoded());
    }
    
    static X500Name getEncodedIssuerPrincipal(final Object o) {
        if (o instanceof X509Certificate) {
            return getIssuerPrincipal((X509Certificate)o);
        }
        return X500Name.getInstance(((X500Principal)((X509AttributeCertificate)o).getIssuer().getPrincipals()[0]).getEncoded());
    }
    
    static X500Name getIssuerPrincipal(final X509CRL x509CRL) {
        return X500Name.getInstance(x509CRL.getIssuerX500Principal().getEncoded());
    }
    
    static X500Name getIssuerPrincipal(final X509Certificate x509Certificate) {
        return X500Name.getInstance(x509Certificate.getIssuerX500Principal().getEncoded());
    }
    
    static X500Name getSubjectPrincipal(final X509Certificate x509Certificate) {
        return X500Name.getInstance(x509Certificate.getSubjectX500Principal().getEncoded());
    }
}
