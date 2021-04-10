package org.spongycastle.x509;

import org.spongycastle.util.*;
import java.io.*;
import java.security.cert.*;

public class X509CertStoreSelector extends X509CertSelector implements Selector
{
    public static X509CertStoreSelector getInstance(final X509CertSelector x509CertSelector) {
        if (x509CertSelector != null) {
            final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
            x509CertStoreSelector.setAuthorityKeyIdentifier(x509CertSelector.getAuthorityKeyIdentifier());
            x509CertStoreSelector.setBasicConstraints(x509CertSelector.getBasicConstraints());
            x509CertStoreSelector.setCertificate(x509CertSelector.getCertificate());
            x509CertStoreSelector.setCertificateValid(x509CertSelector.getCertificateValid());
            x509CertStoreSelector.setMatchAllSubjectAltNames(x509CertSelector.getMatchAllSubjectAltNames());
            try {
                x509CertStoreSelector.setPathToNames(x509CertSelector.getPathToNames());
                x509CertStoreSelector.setExtendedKeyUsage(x509CertSelector.getExtendedKeyUsage());
                x509CertStoreSelector.setNameConstraints(x509CertSelector.getNameConstraints());
                x509CertStoreSelector.setPolicy(x509CertSelector.getPolicy());
                x509CertStoreSelector.setSubjectPublicKeyAlgID(x509CertSelector.getSubjectPublicKeyAlgID());
                x509CertStoreSelector.setSubjectAlternativeNames(x509CertSelector.getSubjectAlternativeNames());
                x509CertStoreSelector.setIssuer(x509CertSelector.getIssuer());
                x509CertStoreSelector.setKeyUsage(x509CertSelector.getKeyUsage());
                x509CertStoreSelector.setPrivateKeyValid(x509CertSelector.getPrivateKeyValid());
                x509CertStoreSelector.setSerialNumber(x509CertSelector.getSerialNumber());
                x509CertStoreSelector.setSubject(x509CertSelector.getSubject());
                x509CertStoreSelector.setSubjectKeyIdentifier(x509CertSelector.getSubjectKeyIdentifier());
                x509CertStoreSelector.setSubjectPublicKey(x509CertSelector.getSubjectPublicKey());
                return x509CertStoreSelector;
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in passed in selector: ");
                sb.append(ex);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        throw new IllegalArgumentException("cannot create from null selector");
    }
    
    @Override
    public Object clone() {
        return super.clone();
    }
    
    @Override
    public boolean match(final Object o) {
        return o instanceof X509Certificate && super.match((Certificate)o);
    }
    
    @Override
    public boolean match(final Certificate certificate) {
        return this.match((Object)certificate);
    }
}
