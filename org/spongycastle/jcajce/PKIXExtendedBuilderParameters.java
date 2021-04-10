package org.spongycastle.jcajce;

import java.security.cert.*;
import java.util.*;
import java.security.*;

public class PKIXExtendedBuilderParameters implements CertPathParameters
{
    private final PKIXExtendedParameters baseParameters;
    private final Set<X509Certificate> excludedCerts;
    private final int maxPathLength;
    
    private PKIXExtendedBuilderParameters(final Builder builder) {
        this.baseParameters = builder.baseParameters;
        this.excludedCerts = Collections.unmodifiableSet((Set<? extends X509Certificate>)builder.excludedCerts);
        this.maxPathLength = builder.maxPathLength;
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    public PKIXExtendedParameters getBaseParameters() {
        return this.baseParameters;
    }
    
    public Set getExcludedCerts() {
        return this.excludedCerts;
    }
    
    public int getMaxPathLength() {
        return this.maxPathLength;
    }
    
    public static class Builder
    {
        private final PKIXExtendedParameters baseParameters;
        private Set<X509Certificate> excludedCerts;
        private int maxPathLength;
        
        public Builder(final PKIXBuilderParameters pkixBuilderParameters) {
            this.maxPathLength = 5;
            this.excludedCerts = new HashSet<X509Certificate>();
            this.baseParameters = new PKIXExtendedParameters.Builder(pkixBuilderParameters).build();
            this.maxPathLength = pkixBuilderParameters.getMaxPathLength();
        }
        
        public Builder(final PKIXExtendedParameters baseParameters) {
            this.maxPathLength = 5;
            this.excludedCerts = new HashSet<X509Certificate>();
            this.baseParameters = baseParameters;
        }
        
        public Builder addExcludedCerts(final Set<X509Certificate> set) {
            this.excludedCerts.addAll(set);
            return this;
        }
        
        public PKIXExtendedBuilderParameters build() {
            return new PKIXExtendedBuilderParameters(this, null);
        }
        
        public Builder setMaxPathLength(final int maxPathLength) {
            if (maxPathLength >= -1) {
                this.maxPathLength = maxPathLength;
                return this;
            }
            throw new InvalidParameterException("The maximum path length parameter can not be less than -1.");
        }
    }
}
