package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;

public final class CacheKey implements Serializable
{
    static final String FRT_ENTRY_PREFIX = "foci-";
    private static final long serialVersionUID = 8067972995583126404L;
    private String mAuthority;
    private String mClientId;
    private String mFamilyClientId;
    private boolean mIsMultipleResourceRefreshToken;
    private String mResource;
    private String mUserId;
    
    private CacheKey() {
    }
    
    public static String createCacheKey(final TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (tokenCacheItem == null) {
            throw new IllegalArgumentException("TokenCacheItem");
        }
        String userId = null;
        if (tokenCacheItem.getUserInfo() != null) {
            userId = tokenCacheItem.getUserInfo().getUserId();
        }
        final int n = CacheKey$1.$SwitchMap$com$microsoft$aad$adal$TokenEntryType[tokenCacheItem.getTokenEntryType().ordinal()];
        if (n == 1) {
            return createCacheKeyForRTEntry(tokenCacheItem.getAuthority(), tokenCacheItem.getResource(), tokenCacheItem.getClientId(), userId);
        }
        if (n == 2) {
            return createCacheKeyForMRRT(tokenCacheItem.getAuthority(), tokenCacheItem.getClientId(), userId);
        }
        if (n == 3) {
            return createCacheKeyForFRT(tokenCacheItem.getAuthority(), tokenCacheItem.getFamilyClientId(), userId);
        }
        throw new AuthenticationException(ADALError.INVALID_TOKEN_CACHE_ITEM, "Cannot create cachekey from given token item");
    }
    
    public static String createCacheKey(String mAuthority, final String mResource, final String s, final boolean mIsMultipleResourceRefreshToken, final String s2, final String s3) {
        if (mAuthority == null) {
            throw new IllegalArgumentException("authority");
        }
        if (s == null && s3 == null) {
            throw new IllegalArgumentException("both clientId and familyClientId are null");
        }
        final CacheKey cacheKey = new CacheKey();
        if (!mIsMultipleResourceRefreshToken) {
            if (mResource == null) {
                throw new IllegalArgumentException("resource");
            }
            cacheKey.mResource = mResource;
        }
        mAuthority = mAuthority.toLowerCase(Locale.US);
        cacheKey.mAuthority = mAuthority;
        if (mAuthority.endsWith("/")) {
            mAuthority = cacheKey.mAuthority;
            cacheKey.mAuthority = (String)mAuthority.subSequence(0, mAuthority.length() - 1);
        }
        if (s != null) {
            cacheKey.mClientId = s.toLowerCase(Locale.US);
        }
        if (s3 != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("foci-");
            sb.append(s3);
            cacheKey.mFamilyClientId = sb.toString().toLowerCase(Locale.US);
        }
        cacheKey.mIsMultipleResourceRefreshToken = mIsMultipleResourceRefreshToken;
        if (!StringExtensions.isNullOrBlank(s2)) {
            cacheKey.mUserId = s2.toLowerCase(Locale.US);
        }
        return cacheKey.toString();
    }
    
    public static String createCacheKeyForFRT(final String s, final String s2, final String s3) {
        return createCacheKey(s, null, null, true, s3, s2);
    }
    
    public static String createCacheKeyForMRRT(final String s, final String s2, final String s3) {
        return createCacheKey(s, null, s2, true, s3, null);
    }
    
    public static String createCacheKeyForRTEntry(final String s, final String s2, final String s3, final String s4) {
        return createCacheKey(s, s2, s3, false, s4, null);
    }
    
    public String getAuthority() {
        return this.mAuthority;
    }
    
    public String getClientId() {
        return this.mClientId;
    }
    
    public boolean getIsMultipleResourceRefreshToken() {
        return this.mIsMultipleResourceRefreshToken;
    }
    
    public String getResource() {
        return this.mResource;
    }
    
    public String getUserId() {
        return this.mUserId;
    }
    
    @Override
    public String toString() {
        final boolean nullOrBlank = StringExtensions.isNullOrBlank(this.mFamilyClientId);
        String s = "y";
        if (nullOrBlank) {
            final Locale us = Locale.US;
            final String mAuthority = this.mAuthority;
            final String mResource = this.mResource;
            final String mClientId = this.mClientId;
            if (!this.mIsMultipleResourceRefreshToken) {
                s = "n";
            }
            return String.format(us, "%s$%s$%s$%s$%s", mAuthority, mResource, mClientId, s, this.mUserId);
        }
        final Locale us2 = Locale.US;
        final String mAuthority2 = this.mAuthority;
        final String mResource2 = this.mResource;
        final String mClientId2 = this.mClientId;
        if (!this.mIsMultipleResourceRefreshToken) {
            s = "n";
        }
        return String.format(us2, "%s$%s$%s$%s$%s$%s", mAuthority2, mResource2, mClientId2, s, this.mUserId, this.mFamilyClientId);
    }
}
