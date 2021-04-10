package org.spongycastle.jce;

import java.security.cert.*;
import java.util.*;

public class MultiCertStoreParameters implements CertStoreParameters
{
    private Collection certStores;
    private boolean searchAllStores;
    
    public MultiCertStoreParameters(final Collection collection) {
        this(collection, true);
    }
    
    public MultiCertStoreParameters(final Collection certStores, final boolean searchAllStores) {
        this.certStores = certStores;
        this.searchAllStores = searchAllStores;
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    public Collection getCertStores() {
        return this.certStores;
    }
    
    public boolean getSearchAllStores() {
        return this.searchAllStores;
    }
}
