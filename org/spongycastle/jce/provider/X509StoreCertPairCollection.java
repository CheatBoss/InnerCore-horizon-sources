package org.spongycastle.jce.provider;

import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.x509.*;

public class X509StoreCertPairCollection extends X509StoreSpi
{
    private CollectionStore _store;
    
    @Override
    public Collection engineGetMatches(final Selector selector) {
        return this._store.getMatches(selector);
    }
    
    @Override
    public void engineInit(final X509StoreParameters x509StoreParameters) {
        if (x509StoreParameters instanceof X509CollectionStoreParameters) {
            this._store = new CollectionStore(((X509CollectionStoreParameters)x509StoreParameters).getCollection());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Initialization parameters must be an instance of ");
        sb.append(X509CollectionStoreParameters.class.getName());
        sb.append(".");
        throw new IllegalArgumentException(sb.toString());
    }
}
