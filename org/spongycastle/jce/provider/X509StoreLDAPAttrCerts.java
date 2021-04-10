package org.spongycastle.jce.provider;

import org.spongycastle.x509.util.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.x509.*;
import org.spongycastle.jce.*;

public class X509StoreLDAPAttrCerts extends X509StoreSpi
{
    private LDAPStoreHelper helper;
    
    @Override
    public Collection engineGetMatches(final Selector selector) throws StoreException {
        if (!(selector instanceof X509AttributeCertStoreSelector)) {
            return Collections.EMPTY_SET;
        }
        final X509AttributeCertStoreSelector x509AttributeCertStoreSelector = (X509AttributeCertStoreSelector)selector;
        final HashSet set = new HashSet();
        set.addAll(this.helper.getAACertificates(x509AttributeCertStoreSelector));
        set.addAll(this.helper.getAttributeCertificateAttributes(x509AttributeCertStoreSelector));
        set.addAll(this.helper.getAttributeDescriptorCertificates(x509AttributeCertStoreSelector));
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
