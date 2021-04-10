package org.spongycastle.x509;

import java.security.*;
import org.spongycastle.util.*;
import java.util.*;
import java.security.cert.*;

public class ExtendedPKIXParameters extends PKIXParameters
{
    public static final int CHAIN_VALIDITY_MODEL = 1;
    public static final int PKIX_VALIDITY_MODEL = 0;
    private boolean additionalLocationsEnabled;
    private List additionalStores;
    private Set attrCertCheckers;
    private Set necessaryACAttributes;
    private Set prohibitedACAttributes;
    private Selector selector;
    private List stores;
    private Set trustedACIssuers;
    private boolean useDeltas;
    private int validityModel;
    
    public ExtendedPKIXParameters(final Set set) throws InvalidAlgorithmParameterException {
        super(set);
        this.validityModel = 0;
        this.useDeltas = false;
        this.stores = new ArrayList();
        this.additionalStores = new ArrayList();
        this.trustedACIssuers = new HashSet();
        this.necessaryACAttributes = new HashSet();
        this.prohibitedACAttributes = new HashSet();
        this.attrCertCheckers = new HashSet();
    }
    
    public static ExtendedPKIXParameters getInstance(final PKIXParameters params) {
        try {
            final ExtendedPKIXParameters extendedPKIXParameters = new ExtendedPKIXParameters(params.getTrustAnchors());
            extendedPKIXParameters.setParams(params);
            return extendedPKIXParameters;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public void addAddionalStore(final Store store) {
        this.addAdditionalStore(store);
    }
    
    public void addAdditionalStore(final Store store) {
        if (store != null) {
            this.additionalStores.add(store);
        }
    }
    
    public void addStore(final Store store) {
        if (store != null) {
            this.stores.add(store);
        }
    }
    
    @Override
    public Object clone() {
        try {
            final ExtendedPKIXParameters extendedPKIXParameters = new ExtendedPKIXParameters(this.getTrustAnchors());
            extendedPKIXParameters.setParams(this);
            return extendedPKIXParameters;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public List getAdditionalStores() {
        return Collections.unmodifiableList((List<?>)this.additionalStores);
    }
    
    public Set getAttrCertCheckers() {
        return Collections.unmodifiableSet((Set<?>)this.attrCertCheckers);
    }
    
    public Set getNecessaryACAttributes() {
        return Collections.unmodifiableSet((Set<?>)this.necessaryACAttributes);
    }
    
    public Set getProhibitedACAttributes() {
        return Collections.unmodifiableSet((Set<?>)this.prohibitedACAttributes);
    }
    
    public List getStores() {
        return Collections.unmodifiableList((List<?>)new ArrayList<Object>(this.stores));
    }
    
    public Selector getTargetConstraints() {
        final Selector selector = this.selector;
        if (selector != null) {
            return (Selector)selector.clone();
        }
        return null;
    }
    
    public Set getTrustedACIssuers() {
        return Collections.unmodifiableSet((Set<?>)this.trustedACIssuers);
    }
    
    public int getValidityModel() {
        return this.validityModel;
    }
    
    public boolean isAdditionalLocationsEnabled() {
        return this.additionalLocationsEnabled;
    }
    
    public boolean isUseDeltasEnabled() {
        return this.useDeltas;
    }
    
    public void setAdditionalLocationsEnabled(final boolean additionalLocationsEnabled) {
        this.additionalLocationsEnabled = additionalLocationsEnabled;
    }
    
    public void setAttrCertCheckers(final Set set) {
        if (set == null) {
            this.attrCertCheckers.clear();
            return;
        }
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof PKIXAttrCertChecker) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("All elements of set must be of type ");
            sb.append(PKIXAttrCertChecker.class.getName());
            sb.append(".");
            throw new ClassCastException(sb.toString());
        }
        this.attrCertCheckers.clear();
        this.attrCertCheckers.addAll(set);
    }
    
    @Override
    public void setCertStores(final List list) {
        if (list != null) {
            final Iterator<CertStore> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.addCertStore(iterator.next());
            }
        }
    }
    
    public void setNecessaryACAttributes(final Set set) {
        if (set == null) {
            this.necessaryACAttributes.clear();
            return;
        }
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof String) {
                continue;
            }
            throw new ClassCastException("All elements of set must be of type String.");
        }
        this.necessaryACAttributes.clear();
        this.necessaryACAttributes.addAll(set);
    }
    
    protected void setParams(final PKIXParameters pkixParameters) {
        this.setDate(pkixParameters.getDate());
        this.setCertPathCheckers(pkixParameters.getCertPathCheckers());
        this.setCertStores(pkixParameters.getCertStores());
        this.setAnyPolicyInhibited(pkixParameters.isAnyPolicyInhibited());
        this.setExplicitPolicyRequired(pkixParameters.isExplicitPolicyRequired());
        this.setPolicyMappingInhibited(pkixParameters.isPolicyMappingInhibited());
        this.setRevocationEnabled(pkixParameters.isRevocationEnabled());
        this.setInitialPolicies(pkixParameters.getInitialPolicies());
        this.setPolicyQualifiersRejected(pkixParameters.getPolicyQualifiersRejected());
        this.setSigProvider(pkixParameters.getSigProvider());
        this.setTargetCertConstraints(pkixParameters.getTargetCertConstraints());
        try {
            this.setTrustAnchors(pkixParameters.getTrustAnchors());
            if (pkixParameters instanceof ExtendedPKIXParameters) {
                final ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)pkixParameters;
                this.validityModel = extendedPKIXParameters.validityModel;
                this.useDeltas = extendedPKIXParameters.useDeltas;
                this.additionalLocationsEnabled = extendedPKIXParameters.additionalLocationsEnabled;
                final Selector selector = extendedPKIXParameters.selector;
                Selector selector2;
                if (selector == null) {
                    selector2 = null;
                }
                else {
                    selector2 = (Selector)selector.clone();
                }
                this.selector = selector2;
                this.stores = new ArrayList(extendedPKIXParameters.stores);
                this.additionalStores = new ArrayList(extendedPKIXParameters.additionalStores);
                this.trustedACIssuers = new HashSet(extendedPKIXParameters.trustedACIssuers);
                this.prohibitedACAttributes = new HashSet(extendedPKIXParameters.prohibitedACAttributes);
                this.necessaryACAttributes = new HashSet(extendedPKIXParameters.necessaryACAttributes);
                this.attrCertCheckers = new HashSet(extendedPKIXParameters.attrCertCheckers);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public void setProhibitedACAttributes(final Set set) {
        if (set == null) {
            this.prohibitedACAttributes.clear();
            return;
        }
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof String) {
                continue;
            }
            throw new ClassCastException("All elements of set must be of type String.");
        }
        this.prohibitedACAttributes.clear();
        this.prohibitedACAttributes.addAll(set);
    }
    
    public void setStores(final List list) {
        if (list == null) {
            this.stores = new ArrayList();
            return;
        }
        final Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof Store) {
                continue;
            }
            throw new ClassCastException("All elements of list must be of type org.spongycastle.util.Store.");
        }
        this.stores = new ArrayList(list);
    }
    
    @Override
    public void setTargetCertConstraints(final CertSelector targetCertConstraints) {
        super.setTargetCertConstraints(targetCertConstraints);
        X509CertStoreSelector instance;
        if (targetCertConstraints != null) {
            instance = X509CertStoreSelector.getInstance((X509CertSelector)targetCertConstraints);
        }
        else {
            instance = null;
        }
        this.selector = instance;
    }
    
    public void setTargetConstraints(Selector selector) {
        if (selector != null) {
            selector = (Selector)selector.clone();
        }
        else {
            selector = null;
        }
        this.selector = selector;
    }
    
    public void setTrustedACIssuers(final Set set) {
        if (set == null) {
            this.trustedACIssuers.clear();
            return;
        }
        final Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof TrustAnchor) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("All elements of set must be of type ");
            sb.append(TrustAnchor.class.getName());
            sb.append(".");
            throw new ClassCastException(sb.toString());
        }
        this.trustedACIssuers.clear();
        this.trustedACIssuers.addAll(set);
    }
    
    public void setUseDeltasEnabled(final boolean useDeltas) {
        this.useDeltas = useDeltas;
    }
    
    public void setValidityModel(final int validityModel) {
        this.validityModel = validityModel;
    }
}
