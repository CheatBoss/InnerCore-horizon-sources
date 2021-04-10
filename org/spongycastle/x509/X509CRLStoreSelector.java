package org.spongycastle.x509;

import java.math.*;
import java.util.*;
import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.x509.extension.*;
import org.spongycastle.asn1.*;
import java.security.cert.*;

public class X509CRLStoreSelector extends X509CRLSelector implements Selector
{
    private X509AttributeCertificate attrCertChecking;
    private boolean completeCRLEnabled;
    private boolean deltaCRLIndicator;
    private byte[] issuingDistributionPoint;
    private boolean issuingDistributionPointEnabled;
    private BigInteger maxBaseCRLNumber;
    
    public X509CRLStoreSelector() {
        this.deltaCRLIndicator = false;
        this.completeCRLEnabled = false;
        this.maxBaseCRLNumber = null;
        this.issuingDistributionPoint = null;
        this.issuingDistributionPointEnabled = false;
    }
    
    public static X509CRLStoreSelector getInstance(final X509CRLSelector x509CRLSelector) {
        if (x509CRLSelector != null) {
            final X509CRLStoreSelector x509CRLStoreSelector = new X509CRLStoreSelector();
            x509CRLStoreSelector.setCertificateChecking(x509CRLSelector.getCertificateChecking());
            x509CRLStoreSelector.setDateAndTime(x509CRLSelector.getDateAndTime());
            try {
                x509CRLStoreSelector.setIssuerNames(x509CRLSelector.getIssuerNames());
                x509CRLStoreSelector.setIssuers(x509CRLSelector.getIssuers());
                x509CRLStoreSelector.setMaxCRLNumber(x509CRLSelector.getMaxCRL());
                x509CRLStoreSelector.setMinCRLNumber(x509CRLSelector.getMinCRL());
                return x509CRLStoreSelector;
            }
            catch (IOException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
        throw new IllegalArgumentException("cannot create from null selector");
    }
    
    @Override
    public Object clone() {
        final X509CRLStoreSelector instance = getInstance(this);
        instance.deltaCRLIndicator = this.deltaCRLIndicator;
        instance.completeCRLEnabled = this.completeCRLEnabled;
        instance.maxBaseCRLNumber = this.maxBaseCRLNumber;
        instance.attrCertChecking = this.attrCertChecking;
        instance.issuingDistributionPointEnabled = this.issuingDistributionPointEnabled;
        instance.issuingDistributionPoint = Arrays.clone(this.issuingDistributionPoint);
        return instance;
    }
    
    public X509AttributeCertificate getAttrCertificateChecking() {
        return this.attrCertChecking;
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
    public boolean match(final Object o) {
        if (!(o instanceof X509CRL)) {
            return false;
        }
        final X509CRL x509CRL = (X509CRL)o;
        ASN1Integer instance = null;
        try {
            final byte[] extensionValue = x509CRL.getExtensionValue(X509Extensions.DeltaCRLIndicator.getId());
            if (extensionValue != null) {
                instance = ASN1Integer.getInstance(X509ExtensionUtil.fromExtensionValue(extensionValue));
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
                final byte[] extensionValue2 = x509CRL.getExtensionValue(X509Extensions.IssuingDistributionPoint.getId());
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
            return super.match(x509CRL);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean match(final CRL crl) {
        return this.match((Object)crl);
    }
    
    public void setAttrCertificateChecking(final X509AttributeCertificate attrCertChecking) {
        this.attrCertChecking = attrCertChecking;
    }
    
    public void setCompleteCRLEnabled(final boolean completeCRLEnabled) {
        this.completeCRLEnabled = completeCRLEnabled;
    }
    
    public void setDeltaCRLIndicatorEnabled(final boolean deltaCRLIndicator) {
        this.deltaCRLIndicator = deltaCRLIndicator;
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
