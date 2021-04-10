package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.*;
import org.spongycastle.util.*;
import java.util.*;
import java.security.cert.*;

class PKIXCRLUtil
{
    private final Collection findCRLs(final PKIXCRLStoreSelector pkixcrlStoreSelector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        final Iterator<Store<? extends E>> iterator = list.iterator();
        Object o = null;
        boolean b = false;
    Label_0086_Outer:
        while (iterator.hasNext()) {
            final Store<? extends E> next = iterator.next();
            Label_0110: {
                while (true) {
                    Label_0089: {
                        if (!(next instanceof Store)) {
                            break Label_0089;
                        }
                        final Store<? extends E> store = next;
                        try {
                            set.addAll(store.getMatches(pkixcrlStoreSelector));
                            break Label_0110;
                        }
                        catch (StoreException ex) {
                            o = new AnnotatedException("Exception searching in X.509 CRL store.", ex);
                        }
                        continue Label_0086_Outer;
                    }
                    final CertStore certStore = (CertStore)next;
                    try {
                        set.addAll(PKIXCRLStoreSelector.getCRLs(pkixcrlStoreSelector, certStore));
                        b = true;
                        continue Label_0086_Outer;
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
    
    public Set findCRLs(final PKIXCRLStoreSelector pkixcrlStoreSelector, final Date date, final List list, final List list2) throws AnnotatedException {
        final HashSet<X509CRL> set = new HashSet<X509CRL>();
        try {
            set.addAll((Collection<?>)this.findCRLs(pkixcrlStoreSelector, list2));
            set.addAll((Collection<?>)this.findCRLs(pkixcrlStoreSelector, list));
            final HashSet<X509CRL> set2 = new HashSet<X509CRL>();
            for (final X509CRL x509CRL : set) {
                if (x509CRL.getNextUpdate().after(date)) {
                    final X509Certificate certificateChecking = pkixcrlStoreSelector.getCertificateChecking();
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
