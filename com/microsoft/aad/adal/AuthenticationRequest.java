package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;

class AuthenticationRequest implements Serializable
{
    private static final int DELIM_NOT_FOUND = -1;
    private static final String UPN_DOMAIN_SUFFIX_DELIM = "@";
    private static final long serialVersionUID = 1L;
    private String mAuthority;
    private String mBrokerAccountName;
    private String mClaimsChallenge;
    private String mClientId;
    private UUID mCorrelationId;
    private String mExtraQueryParamsAuthentication;
    private UserIdentifierType mIdentifierType;
    private transient InstanceDiscoveryMetadata mInstanceDiscoveryMetadata;
    private boolean mIsExtendedLifetimeEnabled;
    private String mLoginHint;
    private PromptBehavior mPrompt;
    private String mRedirectUri;
    private int mRequestId;
    private String mResource;
    private boolean mSilent;
    private String mTelemetryRequestId;
    private String mUserId;
    private String mVersion;
    
    public AuthenticationRequest() {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mIdentifierType = UserIdentifierType.NoUser;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final String mRedirectUri, final String s, final PromptBehavior mPrompt, final String mExtraQueryParamsAuthentication, final UUID mCorrelationId, final boolean mIsExtendedLifetimeEnabled, final String mClaimsChallenge) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mResource = mResource;
        this.mClientId = mClientId;
        this.mRedirectUri = mRedirectUri;
        this.mLoginHint = s;
        this.mBrokerAccountName = s;
        this.mPrompt = mPrompt;
        this.mExtraQueryParamsAuthentication = mExtraQueryParamsAuthentication;
        this.mCorrelationId = mCorrelationId;
        this.mIdentifierType = UserIdentifierType.NoUser;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
        this.mClaimsChallenge = mClaimsChallenge;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final String mRedirectUri, final String s, final UUID mCorrelationId, final boolean mIsExtendedLifetimeEnabled) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mResource = mResource;
        this.mClientId = mClientId;
        this.mRedirectUri = mRedirectUri;
        this.mLoginHint = s;
        this.mBrokerAccountName = s;
        this.mCorrelationId = mCorrelationId;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final String mRedirectUri, final String s, final boolean mIsExtendedLifetimeEnabled) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mResource = mResource;
        this.mClientId = mClientId;
        this.mRedirectUri = mRedirectUri;
        this.mLoginHint = s;
        this.mBrokerAccountName = s;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final String mUserId, final UUID mCorrelationId, final boolean mIsExtendedLifetimeEnabled) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mResource = mResource;
        this.mClientId = mClientId;
        this.mUserId = mUserId;
        this.mCorrelationId = mCorrelationId;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final UUID mCorrelationId, final boolean mIsExtendedLifetimeEnabled) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mClientId = mClientId;
        this.mResource = mResource;
        this.mCorrelationId = mCorrelationId;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
    }
    
    public AuthenticationRequest(final String mAuthority, final String mResource, final String mClientId, final boolean mIsExtendedLifetimeEnabled) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = mAuthority;
        this.mResource = mResource;
        this.mClientId = mClientId;
        this.mIsExtendedLifetimeEnabled = mIsExtendedLifetimeEnabled;
    }
    
    public String getAuthority() {
        return this.mAuthority;
    }
    
    public String getBrokerAccountName() {
        return this.mBrokerAccountName;
    }
    
    public String getClaimsChallenge() {
        return this.mClaimsChallenge;
    }
    
    public String getClientId() {
        return this.mClientId;
    }
    
    public UUID getCorrelationId() {
        return this.mCorrelationId;
    }
    
    public String getExtraQueryParamsAuthentication() {
        return this.mExtraQueryParamsAuthentication;
    }
    
    InstanceDiscoveryMetadata getInstanceDiscoveryMetadata() {
        return this.mInstanceDiscoveryMetadata;
    }
    
    public boolean getIsExtendedLifetimeEnabled() {
        return this.mIsExtendedLifetimeEnabled;
    }
    
    public String getLogInfo() {
        return String.format("Request authority:%s clientid:%s", this.mAuthority, this.mClientId);
    }
    
    public String getLoginHint() {
        return this.mLoginHint;
    }
    
    public PromptBehavior getPrompt() {
        return this.mPrompt;
    }
    
    public String getRedirectUri() {
        return this.mRedirectUri;
    }
    
    public int getRequestId() {
        return this.mRequestId;
    }
    
    public String getResource() {
        return this.mResource;
    }
    
    String getTelemetryRequestId() {
        return this.mTelemetryRequestId;
    }
    
    String getUpnSuffix() {
        final String loginHint = this.getLoginHint();
        String substring = null;
        if (loginHint != null) {
            final int lastIndex = loginHint.lastIndexOf("@");
            if (-1 == lastIndex) {
                return null;
            }
            substring = loginHint.substring(lastIndex + 1);
        }
        return substring;
    }
    
    String getUserFromRequest() {
        if (UserIdentifierType.LoginHint == this.mIdentifierType) {
            return this.mLoginHint;
        }
        if (UserIdentifierType.UniqueId == this.mIdentifierType) {
            return this.mUserId;
        }
        return null;
    }
    
    public String getUserId() {
        return this.mUserId;
    }
    
    public UserIdentifierType getUserIdentifierType() {
        return this.mIdentifierType;
    }
    
    public String getVersion() {
        return this.mVersion;
    }
    
    public boolean isSilent() {
        return this.mSilent;
    }
    
    public void setAuthority(final String mAuthority) {
        this.mAuthority = mAuthority;
    }
    
    public void setBrokerAccountName(final String mBrokerAccountName) {
        this.mBrokerAccountName = mBrokerAccountName;
    }
    
    public void setClaimsChallenge(final String mClaimsChallenge) {
        this.mClaimsChallenge = mClaimsChallenge;
    }
    
    void setInstanceDiscoveryMetadata(final InstanceDiscoveryMetadata mInstanceDiscoveryMetadata) {
        this.mInstanceDiscoveryMetadata = mInstanceDiscoveryMetadata;
    }
    
    void setLoginHint(final String mLoginHint) {
        this.mLoginHint = mLoginHint;
    }
    
    public void setPrompt(final PromptBehavior mPrompt) {
        this.mPrompt = mPrompt;
    }
    
    public void setRequestId(final int mRequestId) {
        this.mRequestId = mRequestId;
    }
    
    public void setSilent(final boolean mSilent) {
        this.mSilent = mSilent;
    }
    
    void setTelemetryRequestId(final String mTelemetryRequestId) {
        this.mTelemetryRequestId = mTelemetryRequestId;
    }
    
    public void setUserId(final String mUserId) {
        this.mUserId = mUserId;
    }
    
    public void setUserIdentifierType(final UserIdentifierType mIdentifierType) {
        this.mIdentifierType = mIdentifierType;
    }
    
    public void setVersion(final String mVersion) {
        this.mVersion = mVersion;
    }
    
    enum UserIdentifierType
    {
        LoginHint, 
        NoUser, 
        UniqueId;
    }
}
