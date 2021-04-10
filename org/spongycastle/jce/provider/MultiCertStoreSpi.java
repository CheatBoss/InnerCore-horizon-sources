package org.spongycastle.jce.provider;

import org.spongycastle.jce.*;
import java.security.*;
import java.util.*;
import java.security.cert.*;

public class MultiCertStoreSpi extends CertStoreSpi
{
    private MultiCertStoreParameters params;
    
    public MultiCertStoreSpi(final CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        if (certStoreParameters instanceof MultiCertStoreParameters) {
            this.params = (MultiCertStoreParameters)certStoreParameters;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("org.spongycastle.jce.provider.MultiCertStoreSpi: parameter must be a MultiCertStoreParameters object\n");
        sb.append(certStoreParameters.toString());
        throw new InvalidAlgorithmParameterException(sb.toString());
    }
    
    @Override
    public Collection engineGetCRLs(final CRLSelector crlSelector) throws CertStoreException {
        final boolean searchAllStores = this.params.getSearchAllStores();
        final Iterator<CertStore> iterator = this.params.getCertStores().iterator();
        List empty_LIST;
        if (searchAllStores) {
            empty_LIST = new ArrayList();
        }
        else {
            empty_LIST = Collections.EMPTY_LIST;
        }
        while (iterator.hasNext()) {
            final Collection<? extends CRL> crLs = iterator.next().getCRLs(crlSelector);
            if (searchAllStores) {
                empty_LIST.addAll(crLs);
            }
            else {
                if (!crLs.isEmpty()) {
                    return crLs;
                }
                continue;
            }
        }
        return empty_LIST;
    }
    
    @Override
    public Collection engineGetCertificates(final CertSelector certSelector) throws CertStoreException {
        final boolean searchAllStores = this.params.getSearchAllStores();
        final Iterator<CertStore> iterator = this.params.getCertStores().iterator();
        List empty_LIST;
        if (searchAllStores) {
            empty_LIST = new ArrayList();
        }
        else {
            empty_LIST = Collections.EMPTY_LIST;
        }
        while (iterator.hasNext()) {
            final Collection<? extends Certificate> certificates = iterator.next().getCertificates(certSelector);
            if (searchAllStores) {
                empty_LIST.addAll(certificates);
            }
            else {
                if (!certificates.isEmpty()) {
                    return certificates;
                }
                continue;
            }
        }
        return empty_LIST;
    }
}
