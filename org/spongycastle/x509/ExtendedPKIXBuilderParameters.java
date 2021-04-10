package org.spongycastle.x509;

import org.spongycastle.util.*;
import java.util.*;
import java.security.*;
import java.security.cert.*;

public class ExtendedPKIXBuilderParameters extends ExtendedPKIXParameters
{
    private Set excludedCerts;
    private int maxPathLength;
    
    public ExtendedPKIXBuilderParameters(final Set set, final Selector targetConstraints) throws InvalidAlgorithmParameterException {
        super(set);
        this.maxPathLength = 5;
        this.excludedCerts = Collections.EMPTY_SET;
        this.setTargetConstraints(targetConstraints);
    }
    
    public static ExtendedPKIXParameters getInstance(final PKIXParameters params) {
        try {
            final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = new ExtendedPKIXBuilderParameters(params.getTrustAnchors(), X509CertStoreSelector.getInstance((X509CertSelector)params.getTargetCertConstraints()));
            extendedPKIXBuilderParameters.setParams(params);
            return extendedPKIXBuilderParameters;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @Override
    public Object clone() {
        try {
            final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = new ExtendedPKIXBuilderParameters(this.getTrustAnchors(), this.getTargetConstraints());
            extendedPKIXBuilderParameters.setParams(this);
            return extendedPKIXBuilderParameters;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public Set getExcludedCerts() {
        return Collections.unmodifiableSet((Set<?>)this.excludedCerts);
    }
    
    public int getMaxPathLength() {
        return this.maxPathLength;
    }
    
    public void setExcludedCerts(Set empty_SET) {
        if (empty_SET == null) {
            empty_SET = Collections.EMPTY_SET;
            return;
        }
        this.excludedCerts = new HashSet(empty_SET);
    }
    
    public void setMaxPathLength(final int maxPathLength) {
        if (maxPathLength >= -1) {
            this.maxPathLength = maxPathLength;
            return;
        }
        throw new InvalidParameterException("The maximum path length parameter can not be less than -1.");
    }
    
    @Override
    protected void setParams(final PKIXParameters params) {
        super.setParams(params);
        if (params instanceof ExtendedPKIXBuilderParameters) {
            final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)params;
            this.maxPathLength = extendedPKIXBuilderParameters.maxPathLength;
            this.excludedCerts = new HashSet(extendedPKIXBuilderParameters.excludedCerts);
        }
        if (params instanceof PKIXBuilderParameters) {
            this.maxPathLength = ((PKIXBuilderParameters)params).getMaxPathLength();
        }
    }
}
