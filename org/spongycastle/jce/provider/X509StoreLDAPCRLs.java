package org.spongycastle.jce.provider;

import org.spongycastle.x509.util.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.x509.*;
import org.spongycastle.jce.*;

public class X509StoreLDAPCRLs extends X509StoreSpi
{
    private LDAPStoreHelper helper;
    
    @Override
    public Collection engineGetMatches(final Selector selector) throws StoreException {
        if (!(selector instanceof X509CRLStoreSelector)) {
            return Collections.EMPTY_SET;
        }
        final X509CRLStoreSelector x509CRLStoreSelector = (X509CRLStoreSelector)selector;
        final HashSet set = new HashSet();
        Collection collection;
        if (x509CRLStoreSelector.isDeltaCRLIndicatorEnabled()) {
            collection = this.helper.getDeltaCertificateRevocationLists(x509CRLStoreSelector);
        }
        else {
            set.addAll(this.helper.getDeltaCertificateRevocationLists(x509CRLStoreSelector));
            set.addAll(this.helper.getAttributeAuthorityRevocationLists(x509CRLStoreSelector));
            set.addAll(this.helper.getAttributeCertificateRevocationLists(x509CRLStoreSelector));
            set.addAll(this.helper.getAuthorityRevocationLists(x509CRLStoreSelector));
            collection = this.helper.getCertificateRevocationLists(x509CRLStoreSelector);
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
