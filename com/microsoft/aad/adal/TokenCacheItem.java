package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;

public class TokenCacheItem implements Serializable
{
    private static final String TAG = "TokenCacheItem";
    private static final long serialVersionUID = 1L;
    private String mAccessToken;
    private String mAuthority;
    private String mClientId;
    private Date mExpiresOn;
    private Date mExtendedExpiresOn;
    private String mFamilyClientId;
    private boolean mIsMultiResourceRefreshToken;
    private String mRawIdToken;
    private String mRefreshtoken;
    private String mResource;
    private String mSpeRing;
    private String mTenantId;
    private UserInfo mUserInfo;
    
    public TokenCacheItem() {
    }
    
    TokenCacheItem(final TokenCacheItem tokenCacheItem) {
        this.mAuthority = tokenCacheItem.getAuthority();
        this.mResource = tokenCacheItem.getResource();
        this.mClientId = tokenCacheItem.getClientId();
        this.mAccessToken = tokenCacheItem.getAccessToken();
        this.mRefreshtoken = tokenCacheItem.getRefreshToken();
        this.mRawIdToken = tokenCacheItem.getRawIdToken();
        this.mUserInfo = tokenCacheItem.getUserInfo();
        this.mExpiresOn = tokenCacheItem.getExpiresOn();
        this.mIsMultiResourceRefreshToken = tokenCacheItem.getIsMultiResourceRefreshToken();
        this.mTenantId = tokenCacheItem.getTenantId();
        this.mFamilyClientId = tokenCacheItem.getFamilyClientId();
        this.mExtendedExpiresOn = tokenCacheItem.getExtendedExpiresOn();
        this.mSpeRing = tokenCacheItem.getSpeRing();
    }
    
    private TokenCacheItem(final String mAuthority, final AuthenticationResult authenticationResult) {
        if (authenticationResult == null) {
            throw new IllegalArgumentException("authenticationResult");
        }
        if (!StringExtensions.isNullOrBlank(mAuthority)) {
            this.mAuthority = mAuthority;
            this.mExpiresOn = authenticationResult.getExpiresOn();
            this.mIsMultiResourceRefreshToken = authenticationResult.getIsMultiResourceRefreshToken();
            this.mTenantId = authenticationResult.getTenantId();
            this.mUserInfo = authenticationResult.getUserInfo();
            this.mRawIdToken = authenticationResult.getIdToken();
            this.mRefreshtoken = authenticationResult.getRefreshToken();
            this.mFamilyClientId = authenticationResult.getFamilyClientId();
            this.mExtendedExpiresOn = authenticationResult.getExtendedExpiresOn();
            if (authenticationResult.getCliTelemInfo() != null) {
                this.mSpeRing = authenticationResult.getCliTelemInfo().getSpeRing();
            }
            return;
        }
        throw new IllegalArgumentException("authority");
    }
    
    public static TokenCacheItem createFRRTTokenCacheItem(final String s, final AuthenticationResult authenticationResult) {
        return new TokenCacheItem(s, authenticationResult);
    }
    
    public static TokenCacheItem createMRRTTokenCacheItem(final String s, final String clientId, final AuthenticationResult authenticationResult) {
        final TokenCacheItem tokenCacheItem = new TokenCacheItem(s, authenticationResult);
        tokenCacheItem.setClientId(clientId);
        return tokenCacheItem;
    }
    
    public static TokenCacheItem createRegularTokenCacheItem(final String s, final String resource, final String clientId, final AuthenticationResult authenticationResult) {
        final TokenCacheItem tokenCacheItem = new TokenCacheItem(s, authenticationResult);
        tokenCacheItem.setClientId(clientId);
        tokenCacheItem.setResource(resource);
        tokenCacheItem.setAccessToken(authenticationResult.getAccessToken());
        return tokenCacheItem;
    }
    
    public static boolean isTokenExpired(final Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.add(13, AuthenticationSettings.INSTANCE.getExpirationBuffer());
        final Date time = instance.getTime();
        final StringBuilder sb = new StringBuilder();
        sb.append("expiresOn:");
        sb.append(date);
        sb.append(" timeWithBuffer:");
        sb.append(instance.getTime());
        sb.append(" Buffer:");
        sb.append(AuthenticationSettings.INSTANCE.getExpirationBuffer());
        Logger.i("TokenCacheItem", "Check token expiration time.", sb.toString());
        return date != null && date.before(time);
    }
    
    public String getAccessToken() {
        return this.mAccessToken;
    }
    
    public String getAuthority() {
        return this.mAuthority;
    }
    
    public String getClientId() {
        return this.mClientId;
    }
    
    public Date getExpiresOn() {
        return Utility.getImmutableDateObject(this.mExpiresOn);
    }
    
    public final Date getExtendedExpiresOn() {
        return Utility.getImmutableDateObject(this.mExtendedExpiresOn);
    }
    
    public final String getFamilyClientId() {
        return this.mFamilyClientId;
    }
    
    public boolean getIsMultiResourceRefreshToken() {
        return this.mIsMultiResourceRefreshToken;
    }
    
    public String getRawIdToken() {
        return this.mRawIdToken;
    }
    
    public String getRefreshToken() {
        return this.mRefreshtoken;
    }
    
    public String getResource() {
        return this.mResource;
    }
    
    String getSpeRing() {
        return this.mSpeRing;
    }
    
    public String getTenantId() {
        return this.mTenantId;
    }
    
    TokenEntryType getTokenEntryType() {
        if (!StringExtensions.isNullOrBlank(this.getResource())) {
            return TokenEntryType.REGULAR_TOKEN_ENTRY;
        }
        if (StringExtensions.isNullOrBlank(this.getClientId())) {
            return TokenEntryType.FRT_TOKEN_ENTRY;
        }
        return TokenEntryType.MRRT_TOKEN_ENTRY;
    }
    
    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }
    
    public final boolean isExtendedLifetimeValid() {
        return this.mExtendedExpiresOn != null && !StringExtensions.isNullOrBlank(this.mAccessToken) && (isTokenExpired(this.mExtendedExpiresOn) ^ true);
    }
    
    boolean isFamilyToken() {
        return StringExtensions.isNullOrBlank(this.mFamilyClientId) ^ true;
    }
    
    public void setAccessToken(final String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }
    
    public void setAuthority(final String mAuthority) {
        this.mAuthority = mAuthority;
    }
    
    public void setClientId(final String mClientId) {
        this.mClientId = mClientId;
    }
    
    public void setExpiresOn(final Date date) {
        this.mExpiresOn = Utility.getImmutableDateObject(date);
    }
    
    public final void setExtendedExpiresOn(final Date date) {
        this.mExtendedExpiresOn = Utility.getImmutableDateObject(date);
    }
    
    public final void setFamilyClientId(final String mFamilyClientId) {
        this.mFamilyClientId = mFamilyClientId;
    }
    
    public void setIsMultiResourceRefreshToken(final boolean mIsMultiResourceRefreshToken) {
        this.mIsMultiResourceRefreshToken = mIsMultiResourceRefreshToken;
    }
    
    public void setRawIdToken(final String mRawIdToken) {
        this.mRawIdToken = mRawIdToken;
    }
    
    public void setRefreshToken(final String mRefreshtoken) {
        this.mRefreshtoken = mRefreshtoken;
    }
    
    public void setResource(final String mResource) {
        this.mResource = mResource;
    }
    
    void setSpeRing(final String mSpeRing) {
        this.mSpeRing = mSpeRing;
    }
    
    public void setTenantId(final String mTenantId) {
        this.mTenantId = mTenantId;
    }
    
    public void setUserInfo(final UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }
}
