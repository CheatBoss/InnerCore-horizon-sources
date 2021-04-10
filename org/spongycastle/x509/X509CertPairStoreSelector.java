package org.spongycastle.x509;

import org.spongycastle.util.*;

public class X509CertPairStoreSelector implements Selector
{
    private X509CertificatePair certPair;
    private X509CertStoreSelector forwardSelector;
    private X509CertStoreSelector reverseSelector;
    
    @Override
    public Object clone() {
        final X509CertPairStoreSelector x509CertPairStoreSelector = new X509CertPairStoreSelector();
        x509CertPairStoreSelector.certPair = this.certPair;
        final X509CertStoreSelector forwardSelector = this.forwardSelector;
        if (forwardSelector != null) {
            x509CertPairStoreSelector.setForwardSelector((X509CertStoreSelector)forwardSelector.clone());
        }
        final X509CertStoreSelector reverseSelector = this.reverseSelector;
        if (reverseSelector != null) {
            x509CertPairStoreSelector.setReverseSelector((X509CertStoreSelector)reverseSelector.clone());
        }
        return x509CertPairStoreSelector;
    }
    
    public X509CertificatePair getCertPair() {
        return this.certPair;
    }
    
    public X509CertStoreSelector getForwardSelector() {
        return this.forwardSelector;
    }
    
    public X509CertStoreSelector getReverseSelector() {
        return this.reverseSelector;
    }
    
    @Override
    public boolean match(final Object o) {
        try {
            if (!(o instanceof X509CertificatePair)) {
                return false;
            }
            final X509CertificatePair x509CertificatePair = (X509CertificatePair)o;
            return (this.forwardSelector == null || this.forwardSelector.match((Object)x509CertificatePair.getForward())) && (this.reverseSelector == null || this.reverseSelector.match((Object)x509CertificatePair.getReverse())) && (this.certPair == null || this.certPair.equals(o));
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public void setCertPair(final X509CertificatePair certPair) {
        this.certPair = certPair;
    }
    
    public void setForwardSelector(final X509CertStoreSelector forwardSelector) {
        this.forwardSelector = forwardSelector;
    }
    
    public void setReverseSelector(final X509CertStoreSelector reverseSelector) {
        this.reverseSelector = reverseSelector;
    }
}
