package org.spongycastle.jce.provider;

import org.spongycastle.x509.util.*;
import java.security.cert.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.x509.*;
import org.spongycastle.jce.*;

public class X509StoreLDAPCerts extends X509StoreSpi
{
    private LDAPStoreHelper helper;
    
    private Collection getCertificatesFromCrossCertificatePairs(final X509CertStoreSelector forwardSelector) throws StoreException {
        final HashSet set = new HashSet();
        final X509CertPairStoreSelector x509CertPairStoreSelector = new X509CertPairStoreSelector();
        x509CertPairStoreSelector.setForwardSelector(forwardSelector);
        x509CertPairStoreSelector.setReverseSelector(new X509CertStoreSelector());
        final HashSet set2 = new HashSet<X509CertificatePair>(this.helper.getCrossCertificatePairs(x509CertPairStoreSelector));
        final HashSet<X509Certificate> set3 = new HashSet<X509Certificate>();
        final HashSet<X509Certificate> set4 = new HashSet<X509Certificate>();
        for (final X509CertificatePair x509CertificatePair : set2) {
            if (x509CertificatePair.getForward() != null) {
                set3.add(x509CertificatePair.getForward());
            }
            if (x509CertificatePair.getReverse() != null) {
                set4.add(x509CertificatePair.getReverse());
            }
        }
        set.addAll(set3);
        set.addAll(set4);
        return set;
    }
    
    @Override
    public Collection engineGetMatches(final Selector selector) throws StoreException {
        if (!(selector instanceof X509CertStoreSelector)) {
            return Collections.EMPTY_SET;
        }
        final X509CertStoreSelector x509CertStoreSelector = (X509CertStoreSelector)selector;
        final HashSet set = new HashSet();
        Collection collection = null;
        Label_0052: {
            if (x509CertStoreSelector.getBasicConstraints() <= 0) {
                if (x509CertStoreSelector.getBasicConstraints() == -2) {
                    collection = this.helper.getUserCertificates(x509CertStoreSelector);
                    break Label_0052;
                }
                set.addAll(this.helper.getUserCertificates(x509CertStoreSelector));
            }
            set.addAll(this.helper.getCACertificates(x509CertStoreSelector));
            collection = this.getCertificatesFromCrossCertificatePairs(x509CertStoreSelector);
        }
        set.addAll(collection);
        return set;
    }
    
    @Override
    public void engineInit(final X509StoreParameters x509StoreParameters) {
        if (x509StoreParameters instanceof X509LDAPCertStoreParameters) {
            this.helper = new LDAPStoreHelper((X509LDAPCertStoreParameters)x509StoreParameters);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Initialization parameters must be an instance of ");
        sb.append(X509LDAPCertStoreParameters.class.getName());
        sb.append(".");
        throw new IllegalArgumentException(sb.toString());
    }
}
