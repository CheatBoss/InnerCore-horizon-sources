package org.spongycastle.x509;

import org.spongycastle.jce.provider.*;
import org.spongycastle.util.*;
import java.util.*;
import java.security.cert.*;

class PKIXCRLUtil
{
    private final Collection findCRLs(final X509CRLStoreSelector x509CRLStoreSelector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        final Iterator<X509Store> iterator = list.iterator();
        Object o = null;
        boolean b = false;
    Label_0084_Outer:
        while (iterator.hasNext()) {
            final X509Store next = iterator.next();
            Label_0108: {
                while (true) {
                    Label_0087: {
                        if (!(next instanceof X509Store)) {
                            break Label_0087;
                        }
                        final X509Store x509Store = next;
                        try {
                            set.addAll(x509Store.getMatches(x509CRLStoreSelector));
                            break Label_0108;
                        }
                        catch (StoreException ex) {
                            o = new AnnotatedException("Exception searching in X.509 CRL store.", ex);
                        }
                        continue Label_0084_Outer;
                    }
                    final CertStore certStore = (CertStore)next;
                    try {
                        set.addAll(certStore.getCRLs(x509CRLStoreSelector));
                        b = true;
                        continue Label_0084_Outer;
                    }
                    catch (CertStoreException ex2) {
                        o = new AnnotatedException("Exception searching in X.509 CRL store.", ex2);
                        continue;
                    }
                    break;
                }
            }
            break;
        }
        if (b) {
            return set;
        }
        if (o == null) {
            return set;
        }
        throw o;
    }
    
    public Set findCRLs(final X509CRLStoreSelector x509CRLStoreSelector, final PKIXParameters pkixParameters) throws AnnotatedException {
        final HashSet set = new HashSet();
        try {
            set.addAll(this.findCRLs(x509CRLStoreSelector, pkixParameters.getCertStores()));
            return set;
        }
        catch (AnnotatedException ex) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", ex);
        }
    }
    
    public Set findCRLs(final X509CRLStoreSelector x509CRLStoreSelector, final ExtendedPKIXParameters extendedPKIXParameters, Date date) throws AnnotatedException {
        final HashSet<X509CRL> set = new HashSet<X509CRL>();
        try {
            set.addAll((Collection<?>)this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getAdditionalStores()));
            set.addAll((Collection<?>)this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getStores()));
            set.addAll((Collection<?>)this.findCRLs(x509CRLStoreSelector, extendedPKIXParameters.getCertStores()));
            final HashSet<X509CRL> set2 = new HashSet<X509CRL>();
            if (extendedPKIXParameters.getDate() != null) {
                date = extendedPKIXParameters.getDate();
            }
            for (final X509CRL x509CRL : set) {
                if (x509CRL.getNextUpdate().after(date)) {
                    final X509Certificate certificateChecking = x509CRLStoreSelector.getCertificateChecking();
                    if (certificateChecking != null && !x509CRL.getThisUpdate().before(certificateChecking.getNotAfter())) {
                        continue;
                    }
                    set2.add(x509CRL);
                }
            }
            return set2;
        }
        catch (AnnotatedException ex) {
            throw new AnnotatedException("Exception obtaining complete CRLs.", ex);
        }
    }
}
