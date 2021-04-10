package org.spongycastle.jcajce;

import org.spongycastle.util.*;
import java.util.*;
import java.security.cert.*;
import java.io.*;

public class PKIXCertStoreSelector<T extends Certificate> implements Selector<T>
{
    private final CertSelector baseSelector;
    
    private PKIXCertStoreSelector(final CertSelector baseSelector) {
        this.baseSelector = baseSelector;
    }
    
    public static Collection<? extends Certificate> getCertificates(final PKIXCertStoreSelector pkixCertStoreSelector, final CertStore certStore) throws CertStoreException {
        return certStore.getCertificates(new SelectorClone(pkixCertStoreSelector));
    }
    
    @Override
    public Object clone() {
        return new PKIXCertStoreSelector(this.baseSelector);
    }
    
    @Override
    public boolean match(final Certificate certificate) {
        return this.baseSelector.match(certificate);
    }
    
    public static class Builder
    {
        private final CertSelector baseSelector;
        
        public Builder(final CertSelector certSelector) {
            this.baseSelector = (CertSelector)certSelector.clone();
        }
        
        public PKIXCertStoreSelector<? extends Certificate> build() {
            return new PKIXCertStoreSelector<Certificate>(this.baseSelector, null);
        }
    }
    
    private static class SelectorClone extends X509CertSelector
    {
        private final PKIXCertStoreSelector selector;
        
        SelectorClone(final PKIXCertStoreSelector selector) {
            this.selector = selector;
            if (selector.baseSelector instanceof X509CertSelector) {
                final X509CertSelector x509CertSelector = (X509CertSelector)selector.baseSelector;
                this.setAuthorityKeyIdentifier(x509CertSelector.getAuthorityKeyIdentifier());
                this.setBasicConstraints(x509CertSelector.getBasicConstraints());
                this.setCertificate(x509CertSelector.getCertificate());
                this.setCertificateValid(x509CertSelector.getCertificateValid());
                this.setKeyUsage(x509CertSelector.getKeyUsage());
                this.setMatchAllSubjectAltNames(x509CertSelector.getMatchAllSubjectAltNames());
                this.setPrivateKeyValid(x509CertSelector.getPrivateKeyValid());
                this.setSerialNumber(x509CertSelector.getSerialNumber());
                this.setSubjectKeyIdentifier(x509CertSelector.getSubjectKeyIdentifier());
                this.setSubjectPublicKey(x509CertSelector.getSubjectPublicKey());
                try {
                    this.setExtendedKeyUsage(x509CertSelector.getExtendedKeyUsage());
                    this.setIssuer(x509CertSelector.getIssuerAsBytes());
                    this.setNameConstraints(x509CertSelector.getNameConstraints());
                    this.setPathToNames(x509CertSelector.getPathToNames());
                    this.setPolicy(x509CertSelector.getPolicy());
                    this.setSubject(x509CertSelector.getSubjectAsBytes());
                    this.setSubjectAlternativeNames(x509CertSelector.getSubjectAlternativeNames());
                    this.setSubjectPublicKeyAlgID(x509CertSelector.getSubjectPublicKeyAlgID());
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("base selector invalid: ");
                    sb.append(ex.getMessage());
                    throw new IllegalStateException(sb.toString(), ex);
                }
            }
        }
        
        @Override
        public boolean match(final Certificate certificate) {
            final PKIXCertStoreSelector selector = this.selector;
            if (selector == null) {
                return certificate != null;
            }
            return selector.match(certificate);
        }
    }
}
