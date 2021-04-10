package org.spongycastle.jcajce;

import java.math.*;
import java.util.*;
import org.spongycastle.util.*;
import java.security.cert.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class PKIXCRLStoreSelector<T extends CRL> implements Selector<T>
{
    private final CRLSelector baseSelector;
    private final boolean completeCRLEnabled;
    private final boolean deltaCRLIndicator;
    private final byte[] issuingDistributionPoint;
    private final boolean issuingDistributionPointEnabled;
    private final BigInteger maxBaseCRLNumber;
    
    private PKIXCRLStoreSelector(final Builder builder) {
        this.baseSelector = builder.baseSelector;
        this.deltaCRLIndicator = builder.deltaCRLIndicator;
        this.completeCRLEnabled = builder.completeCRLEnabled;
        this.maxBaseCRLNumber = builder.maxBaseCRLNumber;
        this.issuingDistributionPoint = builder.issuingDistributionPoint;
        this.issuingDistributionPointEnabled = builder.issuingDistributionPointEnabled;
    }
    
    public static Collection<? extends CRL> getCRLs(final PKIXCRLStoreSelector pkixcrlStoreSelector, final CertStore certStore) throws CertStoreException {
        return certStore.getCRLs(new SelectorClone(pkixcrlStoreSelector));
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    public X509Certificate getCertificateChecking() {
        final CRLSelector baseSelector = this.baseSelector;
        if (baseSelector instanceof X509CRLSelector) {
            return ((X509CRLSelector)baseSelector).getCertificateChecking();
        }
        return null;
    }
    
    public byte[] getIssuingDistributionPoint() {
        return Arrays.clone(this.issuingDistributionPoint);
    }
    
    public BigInteger getMaxBaseCRLNumber() {
        return this.maxBaseCRLNumber;
    }
    
    public boolean isCompleteCRLEnabled() {
        return this.completeCRLEnabled;
    }
    
    public boolean isDeltaCRLIndicatorEnabled() {
        return this.deltaCRLIndicator;
    }
    
    public boolean isIssuingDistributionPointEnabled() {
        return this.issuingDistributionPointEnabled;
    }
    
    @Override
    public boolean match(final CRL crl) {
        if (crl instanceof X509CRL) {
            final X509CRL x509CRL = (X509CRL)crl;
            ASN1Integer instance = null;
            try {
                final byte[] extensionValue = x509CRL.getExtensionValue(Extension.deltaCRLIndicator.getId());
                if (extensionValue != null) {
                    instance = ASN1Integer.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets());
                }
                if (this.isDeltaCRLIndicatorEnabled() && instance == null) {
                    return false;
                }
                if (this.isCompleteCRLEnabled() && instance != null) {
                    return false;
                }
                if (instance != null && this.maxBaseCRLNumber != null && instance.getPositiveValue().compareTo(this.maxBaseCRLNumber) == 1) {
                    return false;
                }
                if (this.issuingDistributionPointEnabled) {
                    final byte[] extensionValue2 = x509CRL.getExtensionValue(Extension.issuingDistributionPoint.getId());
                    final byte[] issuingDistributionPoint = this.issuingDistributionPoint;
                    if (issuingDistributionPoint == null) {
                        if (extensionValue2 != null) {
                            return false;
                        }
                    }
                    else if (!Arrays.areEqual(extensionValue2, issuingDistributionPoint)) {
                        return false;
                    }
                }
            }
            catch (Exception ex) {
                return false;
            }
        }
        return this.baseSelector.match(crl);
    }
    
    public static class Builder
    {
        private final CRLSelector baseSelector;
        private boolean completeCRLEnabled;
        private boolean deltaCRLIndicator;
        private byte[] issuingDistributionPoint;
        private boolean issuingDistributionPointEnabled;
        private BigInteger maxBaseCRLNumber;
        
        public Builder(final CRLSelector crlSelector) {
            this.deltaCRLIndicator = false;
            this.completeCRLEnabled = false;
            this.maxBaseCRLNumber = null;
            this.issuingDistributionPoint = null;
            this.issuingDistributionPointEnabled = false;
            this.baseSelector = (CRLSelector)crlSelector.clone();
        }
        
        public PKIXCRLStoreSelector<? extends CRL> build() {
            return new PKIXCRLStoreSelector<CRL>(this, null);
        }
        
        public Builder setCompleteCRLEnabled(final boolean completeCRLEnabled) {
            this.completeCRLEnabled = completeCRLEnabled;
            return this;
        }
        
        public Builder setDeltaCRLIndicatorEnabled(final boolean deltaCRLIndicator) {
            this.deltaCRLIndicator = deltaCRLIndicator;
            return this;
        }
        
        public void setIssuingDistributionPoint(final byte[] array) {
            this.issuingDistributionPoint = Arrays.clone(array);
        }
        
        public void setIssuingDistributionPointEnabled(final boolean issuingDistributionPointEnabled) {
            this.issuingDistributionPointEnabled = issuingDistributionPointEnabled;
        }
        
        public void setMaxBaseCRLNumber(final BigInteger maxBaseCRLNumber) {
            this.maxBaseCRLNumber = maxBaseCRLNumber;
        }
    }
    
    private static class SelectorClone extends X509CRLSelector
    {
        private final PKIXCRLStoreSelector selector;
        
        SelectorClone(final PKIXCRLStoreSelector selector) {
            this.selector = selector;
            if (selector.baseSelector instanceof X509CRLSelector) {
                final X509CRLSelector x509CRLSelector = (X509CRLSelector)selector.baseSelector;
                this.setCertificateChecking(x509CRLSelector.getCertificateChecking());
                this.setDateAndTime(x509CRLSelector.getDateAndTime());
                this.setIssuers(x509CRLSelector.getIssuers());
                this.setMinCRLNumber(x509CRLSelector.getMinCRL());
                this.setMaxCRLNumber(x509CRLSelector.getMaxCRL());
            }
        }
        
        @Override
        public boolean match(final CRL crl) {
            final PKIXCRLStoreSelector selector = this.selector;
            if (selector == null) {
                return crl != null;
            }
            return selector.match(crl);
        }
    }
}
