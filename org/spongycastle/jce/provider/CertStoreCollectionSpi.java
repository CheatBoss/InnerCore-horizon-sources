package org.spongycastle.jce.provider;

import java.security.*;
import java.util.*;
import java.security.cert.*;

public class CertStoreCollectionSpi extends CertStoreSpi
{
    private CollectionCertStoreParameters params;
    
    public CertStoreCollectionSpi(final CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException {
        super(certStoreParameters);
        if (certStoreParameters instanceof CollectionCertStoreParameters) {
            this.params = (CollectionCertStoreParameters)certStoreParameters;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("org.spongycastle.jce.provider.CertStoreCollectionSpi: parameter must be a CollectionCertStoreParameters object\n");
        sb.append(certStoreParameters.toString());
        throw new InvalidAlgorithmParameterException(sb.toString());
    }
    
    @Override
    public Collection engineGetCRLs(final CRLSelector crlSelector) throws CertStoreException {
        final ArrayList<CRL> list = new ArrayList<CRL>();
        final Iterator<?> iterator = this.params.getCollection().iterator();
        if (crlSelector == null) {
            while (iterator.hasNext()) {
                final Object next = iterator.next();
                if (next instanceof CRL) {
                    list.add((CRL)next);
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                final Object next2 = iterator.next();
                if (next2 instanceof CRL && crlSelector.match((CRL)next2)) {
                    list.add((CRL)next2);
                }
            }
        }
        return list;
    }
    
    @Override
    public Collection engineGetCertificates(final CertSelector certSelector) throws CertStoreException {
        final ArrayList<Certificate> list = new ArrayList<Certificate>();
        final Iterator<?> iterator = this.params.getCollection().iterator();
        if (certSelector == null) {
            while (iterator.hasNext()) {
                final Object next = iterator.next();
                if (next instanceof Certificate) {
                    list.add((Certificate)next);
                }
            }
        }
        else {
            while (iterator.hasNext()) {
                final Object next2 = iterator.next();
                if (next2 instanceof Certificate && certSelector.match((Certificate)next2)) {
                    list.add((Certificate)next2);
                }
            }
        }
        return list;
    }
}
