package com.microsoft.aad.adal;

import java.util.*;

final class InstanceDiscoveryMetadata
{
    private final List<String> mAliases;
    private final boolean mIsValidated;
    private final String mPreferredCache;
    private final String mPreferredNetwork;
    
    InstanceDiscoveryMetadata(final String mPreferredNetwork, final String mPreferredCache) {
        this.mAliases = new ArrayList<String>();
        this.mPreferredNetwork = mPreferredNetwork;
        this.mPreferredCache = mPreferredCache;
        this.mIsValidated = true;
    }
    
    InstanceDiscoveryMetadata(final String mPreferredNetwork, final String mPreferredCache, final List<String> list) {
        final ArrayList<Object> mAliases = new ArrayList<Object>();
        this.mAliases = (List<String>)mAliases;
        this.mPreferredNetwork = mPreferredNetwork;
        this.mPreferredCache = mPreferredCache;
        mAliases.addAll(list);
        this.mIsValidated = true;
    }
    
    InstanceDiscoveryMetadata(final boolean mIsValidated) {
        this.mAliases = new ArrayList<String>();
        this.mIsValidated = mIsValidated;
        this.mPreferredNetwork = null;
        this.mPreferredCache = null;
    }
    
    List<String> getAliases() {
        return this.mAliases;
    }
    
    String getPreferredCache() {
        return this.mPreferredCache;
    }
    
    String getPreferredNetwork() {
        return this.mPreferredNetwork;
    }
    
    boolean isValidated() {
        return this.mIsValidated;
    }
}
