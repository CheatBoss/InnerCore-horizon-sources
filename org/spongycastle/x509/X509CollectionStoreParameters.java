package org.spongycastle.x509;

import java.util.*;

public class X509CollectionStoreParameters implements X509StoreParameters
{
    private Collection collection;
    
    public X509CollectionStoreParameters(final Collection collection) {
        if (collection != null) {
            this.collection = collection;
            return;
        }
        throw new NullPointerException("collection cannot be null");
    }
    
    public Object clone() {
        return new X509CollectionStoreParameters(this.collection);
    }
    
    public Collection getCollection() {
        return new ArrayList(this.collection);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("X509CollectionStoreParameters: [\n");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("  collection: ");
        sb2.append(this.collection);
        sb2.append("\n");
        sb.append(sb2.toString());
        sb.append("]");
        return sb.toString();
    }
}
