package org.spongycastle.jcajce;

import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import java.util.*;

public class PKIXExtendedParameters implements CertPathParameters
{
    public static final int CHAIN_VALIDITY_MODEL = 1;
    public static final int PKIX_VALIDITY_MODEL = 0;
    private final PKIXParameters baseParameters;
    private final Date date;
    private final List<PKIXCRLStore> extraCRLStores;
    private final List<PKIXCertStore> extraCertStores;
    private final Map<GeneralName, PKIXCRLStore> namedCRLStoreMap;
    private final Map<GeneralName, PKIXCertStore> namedCertificateStoreMap;
    private final boolean revocationEnabled;
    private final PKIXCertStoreSelector targetConstraints;
    private final Set<TrustAnchor> trustAnchors;
    private final boolean useDeltas;
    private final int validityModel;
    
    private PKIXExtendedParameters(final Builder builder) {
        this.baseParameters = builder.baseParameters;
        this.date = builder.date;
        this.extraCertStores = (List<PKIXCertStore>)Collections.unmodifiableList((List<? extends PKIXCertStore>)builder.extraCertStores);
        this.namedCertificateStoreMap = (Map<GeneralName, PKIXCertStore>)Collections.unmodifiableMap((Map<? extends GeneralName, ? extends PKIXCertStore>)new HashMap<GeneralName, PKIXCertStore>(builder.namedCertificateStoreMap));
        this.extraCRLStores = (List<PKIXCRLStore>)Collections.unmodifiableList((List<? extends PKIXCRLStore>)builder.extraCRLStores);
        this.namedCRLStoreMap = (Map<GeneralName, PKIXCRLStore>)Collections.unmodifiableMap((Map<? extends GeneralName, ? extends PKIXCRLStore>)new HashMap<GeneralName, PKIXCRLStore>(builder.namedCRLStoreMap));
        this.targetConstraints = builder.targetConstraints;
        this.revocationEnabled = builder.revocationEnabled;
        this.useDeltas = builder.useDeltas;
        this.validityModel = builder.validityModel;
        this.trustAnchors = Collections.unmodifiableSet((Set<? extends TrustAnchor>)builder.trustAnchors);
    }
    
    @Override
    public Object clone() {
        return this;
    }
    
    public List<PKIXCRLStore> getCRLStores() {
        return this.extraCRLStores;
    }
    
    public List getCertPathCheckers() {
        return this.baseParameters.getCertPathCheckers();
    }
    
    public List<CertStore> getCertStores() {
        return this.baseParameters.getCertStores();
    }
    
    public List<PKIXCertStore> getCertificateStores() {
        return this.extraCertStores;
    }
    
    public Date getDate() {
        return new Date(this.date.getTime());
    }
    
    public Set getInitialPolicies() {
        return this.baseParameters.getInitialPolicies();
    }
    
    public Map<GeneralName, PKIXCRLStore> getNamedCRLStoreMap() {
        return this.namedCRLStoreMap;
    }
    
    public Map<GeneralName, PKIXCertStore> getNamedCertificateStoreMap() {
        return this.namedCertificateStoreMap;
    }
    
    public String getSigProvider() {
        return this.baseParameters.getSigProvider();
    }
    
    public PKIXCertStoreSelector getTargetConstraints() {
        return this.targetConstraints;
    }
    
    public Set getTrustAnchors() {
        return this.trustAnchors;
    }
    
    public int getValidityModel() {
        return this.validityModel;
    }
    
    public boolean isAnyPolicyInhibited() {
        return this.baseParameters.isAnyPolicyInhibited();
    }
    
    public boolean isExplicitPolicyRequired() {
        return this.baseParameters.isExplicitPolicyRequired();
    }
    
    public boolean isPolicyMappingInhibited() {
        return this.baseParameters.isPolicyMappingInhibited();
    }
    
    public boolean isRevocationEnabled() {
        return this.revocationEnabled;
    }
    
    public boolean isUseDeltasEnabled() {
        return this.useDeltas;
    }
    
    public static class Builder
    {
        private final PKIXParameters baseParameters;
        private final Date date;
        private List<PKIXCRLStore> extraCRLStores;
        private List<PKIXCertStore> extraCertStores;
        private Map<GeneralName, PKIXCRLStore> namedCRLStoreMap;
        private Map<GeneralName, PKIXCertStore> namedCertificateStoreMap;
        private boolean revocationEnabled;
        private PKIXCertStoreSelector targetConstraints;
        private Set<TrustAnchor> trustAnchors;
        private boolean useDeltas;
        private int validityModel;
        
        public Builder(final PKIXParameters pkixParameters) {
            this.extraCertStores = new ArrayList<PKIXCertStore>();
            this.namedCertificateStoreMap = new HashMap<GeneralName, PKIXCertStore>();
            this.extraCRLStores = new ArrayList<PKIXCRLStore>();
            this.namedCRLStoreMap = new HashMap<GeneralName, PKIXCRLStore>();
            this.validityModel = 0;
            this.useDeltas = false;
            this.baseParameters = (PKIXParameters)pkixParameters.clone();
            final CertSelector targetCertConstraints = pkixParameters.getTargetCertConstraints();
            if (targetCertConstraints != null) {
                this.targetConstraints = new PKIXCertStoreSelector.Builder(targetCertConstraints).build();
            }
            Date date;
            if ((date = pkixParameters.getDate()) == null) {
                date = new Date();
            }
            this.date = date;
            this.revocationEnabled = pkixParameters.isRevocationEnabled();
            this.trustAnchors = pkixParameters.getTrustAnchors();
        }
        
        public Builder(final PKIXExtendedParameters pkixExtendedParameters) {
            this.extraCertStores = new ArrayList<PKIXCertStore>();
            this.namedCertificateStoreMap = new HashMap<GeneralName, PKIXCertStore>();
            this.extraCRLStores = new ArrayList<PKIXCRLStore>();
            this.namedCRLStoreMap = new HashMap<GeneralName, PKIXCRLStore>();
            this.validityModel = 0;
            this.useDeltas = false;
            this.baseParameters = pkixExtendedParameters.baseParameters;
            this.date = pkixExtendedParameters.date;
            this.targetConstraints = pkixExtendedParameters.targetConstraints;
            this.extraCertStores = new ArrayList<PKIXCertStore>(pkixExtendedParameters.extraCertStores);
            this.namedCertificateStoreMap = new HashMap<GeneralName, PKIXCertStore>(pkixExtendedParameters.namedCertificateStoreMap);
            this.extraCRLStores = new ArrayList<PKIXCRLStore>(pkixExtendedParameters.extraCRLStores);
            this.namedCRLStoreMap = new HashMap<GeneralName, PKIXCRLStore>(pkixExtendedParameters.namedCRLStoreMap);
            this.useDeltas = pkixExtendedParameters.useDeltas;
            this.validityModel = pkixExtendedParameters.validityModel;
            this.revocationEnabled = pkixExtendedParameters.isRevocationEnabled();
            this.trustAnchors = (Set<TrustAnchor>)pkixExtendedParameters.getTrustAnchors();
        }
        
        public Builder addCRLStore(final PKIXCRLStore pkixcrlStore) {
            this.extraCRLStores.add(pkixcrlStore);
            return this;
        }
        
        public Builder addCertificateStore(final PKIXCertStore pkixCertStore) {
            this.extraCertStores.add(pkixCertStore);
            return this;
        }
        
        public Builder addNamedCRLStore(final GeneralName generalName, final PKIXCRLStore pkixcrlStore) {
            this.namedCRLStoreMap.put(generalName, pkixcrlStore);
            return this;
        }
        
        public Builder addNamedCertificateStore(final GeneralName generalName, final PKIXCertStore pkixCertStore) {
            this.namedCertificateStoreMap.put(generalName, pkixCertStore);
            return this;
        }
        
        public PKIXExtendedParameters build() {
            return new PKIXExtendedParameters(this, null);
        }
        
        public void setRevocationEnabled(final boolean revocationEnabled) {
            this.revocationEnabled = revocationEnabled;
        }
        
        public Builder setTargetConstraints(final PKIXCertStoreSelector targetConstraints) {
            this.targetConstraints = targetConstraints;
            return this;
        }
        
        public Builder setTrustAnchor(final TrustAnchor trustAnchor) {
            this.trustAnchors = Collections.singleton(trustAnchor);
            return this;
        }
        
        public Builder setTrustAnchors(final Set<TrustAnchor> trustAnchors) {
            this.trustAnchors = trustAnchors;
            return this;
        }
        
        public Builder setUseDeltasEnabled(final boolean useDeltas) {
            this.useDeltas = useDeltas;
            return this;
        }
        
        public Builder setValidityModel(final int validityModel) {
            this.validityModel = validityModel;
            return this;
        }
    }
}
