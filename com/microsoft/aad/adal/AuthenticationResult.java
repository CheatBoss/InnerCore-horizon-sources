package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;
import org.json.*;

public class AuthenticationResult implements Serializable
{
    private static final long serialVersionUID = 2243372613182536368L;
    private String mAccessToken;
    private String mAuthority;
    private TelemetryUtils.CliTelemInfo mCliTelemInfo;
    private String mCode;
    private String mErrorCode;
    private String mErrorCodes;
    private String mErrorDescription;
    private Date mExpiresOn;
    private Date mExtendedExpiresOn;
    private String mFamilyClientId;
    private HashMap<String, String> mHttpResponseBody;
    private HashMap<String, List<String>> mHttpResponseHeaders;
    private String mIdToken;
    private boolean mInitialRequest;
    private boolean mIsExtendedLifeTimeToken;
    private boolean mIsMultiResourceRefreshToken;
    private String mRefreshToken;
    private int mServiceStatusCode;
    private AuthenticationStatus mStatus;
    private String mTenantId;
    private String mTokenType;
    private UserInfo mUserInfo;
    
    AuthenticationResult() {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
    }
    
    AuthenticationResult(final String mCode) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = mCode;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mAccessToken = null;
        this.mRefreshToken = null;
    }
    
    AuthenticationResult(final String mErrorCode, final String mErrorDescription, final String mErrorCodes) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mErrorCode = mErrorCode;
        this.mErrorDescription = mErrorDescription;
        this.mErrorCodes = mErrorCodes;
        this.mStatus = AuthenticationStatus.Failed;
    }
    
    AuthenticationResult(final String mAccessToken, final String mRefreshToken, final Date mExpiresOn, final boolean mIsMultiResourceRefreshToken, final UserInfo mUserInfo, final String mTenantId, final String mIdToken, final Date mExtendedExpiresOn) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
        this.mAccessToken = mAccessToken;
        this.mRefreshToken = mRefreshToken;
        this.mExpiresOn = mExpiresOn;
        this.mIsMultiResourceRefreshToken = mIsMultiResourceRefreshToken;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mUserInfo = mUserInfo;
        this.mTenantId = mTenantId;
        this.mIdToken = mIdToken;
        this.mExtendedExpiresOn = mExtendedExpiresOn;
    }
    
    AuthenticationResult(final String mAccessToken, final String mRefreshToken, final Date mExpiresOn, final boolean mIsMultiResourceRefreshToken, final Date mExtendedExpiresOn) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
        this.mAccessToken = mAccessToken;
        this.mRefreshToken = mRefreshToken;
        this.mExpiresOn = mExpiresOn;
        this.mIsMultiResourceRefreshToken = mIsMultiResourceRefreshToken;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mExtendedExpiresOn = mExtendedExpiresOn;
    }
    
    static AuthenticationResult createExtendedLifeTimeResult(final TokenCacheItem tokenCacheItem) {
        final AuthenticationResult result = createResult(tokenCacheItem);
        result.setExpiresOn(result.getExtendedExpiresOn());
        result.setIsExtendedLifeTimeToken(true);
        return result;
    }
    
    static AuthenticationResult createResult(final TokenCacheItem tokenCacheItem) {
        if (tokenCacheItem == null) {
            final AuthenticationResult authenticationResult = new AuthenticationResult();
            authenticationResult.mStatus = AuthenticationStatus.Failed;
            return authenticationResult;
        }
        return new AuthenticationResult(tokenCacheItem.getAccessToken(), tokenCacheItem.getRefreshToken(), tokenCacheItem.getExpiresOn(), tokenCacheItem.getIsMultiResourceRefreshToken(), tokenCacheItem.getUserInfo(), tokenCacheItem.getTenantId(), tokenCacheItem.getRawIdToken(), tokenCacheItem.getExtendedExpiresOn());
    }
    
    static AuthenticationResult createResultForInitialRequest() {
        final AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.mInitialRequest = true;
        return authenticationResult;
    }
    
    public String createAuthorizationHeader() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Bearer ");
        sb.append(this.getAccessToken());
        return sb.toString();
    }
    
    public String getAccessToken() {
        return this.mAccessToken;
    }
    
    public String getAccessTokenType() {
        return this.mTokenType;
    }
    
    public final String getAuthority() {
        return this.mAuthority;
    }
    
    final TelemetryUtils.CliTelemInfo getCliTelemInfo() {
        return this.mCliTelemInfo;
    }
    
    String getCode() {
        return this.mCode;
    }
    
    public String getErrorCode() {
        return this.mErrorCode;
    }
    
    String[] getErrorCodes() {
        final String mErrorCodes = this.mErrorCodes;
        if (mErrorCodes != null) {
            return mErrorCodes.replaceAll("[\\[\\]]", "").split("([^,]),");
        }
        return null;
    }
    
    public String getErrorDescription() {
        return this.mErrorDescription;
    }
    
    public String getErrorLogInfo() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" ErrorCode:");
        sb.append(this.getErrorCode());
        return sb.toString();
    }
    
    public Date getExpiresOn() {
        return Utility.getImmutableDateObject(this.mExpiresOn);
    }
    
    final Date getExtendedExpiresOn() {
        return this.mExtendedExpiresOn;
    }
    
    final String getFamilyClientId() {
        return this.mFamilyClientId;
    }
    
    public HashMap<String, String> getHttpResponseBody() {
        return this.mHttpResponseBody;
    }
    
    public HashMap<String, List<String>> getHttpResponseHeaders() {
        return this.mHttpResponseHeaders;
    }
    
    public String getIdToken() {
        return this.mIdToken;
    }
    
    public boolean getIsMultiResourceRefreshToken() {
        return this.mIsMultiResourceRefreshToken;
    }
    
    public String getRefreshToken() {
        return this.mRefreshToken;
    }
    
    public int getServiceStatusCode() {
        return this.mServiceStatusCode;
    }
    
    public AuthenticationStatus getStatus() {
        return this.mStatus;
    }
    
    public String getTenantId() {
        return this.mTenantId;
    }
    
    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }
    
    public boolean isExpired() {
        Date date;
        if (this.mIsExtendedLifeTimeToken) {
            date = this.getExtendedExpiresOn();
        }
        else {
            date = this.getExpiresOn();
        }
        return TokenCacheItem.isTokenExpired(date);
    }
    
    public boolean isExtendedLifeTimeToken() {
        return this.mIsExtendedLifeTimeToken;
    }
    
    boolean isInitialRequest() {
        return this.mInitialRequest;
    }
    
    final void setAuthority(final String mAuthority) {
        if (!StringExtensions.isNullOrBlank(mAuthority)) {
            this.mAuthority = mAuthority;
        }
    }
    
    final void setCliTelemInfo(final TelemetryUtils.CliTelemInfo mCliTelemInfo) {
        this.mCliTelemInfo = mCliTelemInfo;
    }
    
    void setCode(final String mCode) {
        this.mCode = mCode;
    }
    
    final void setExpiresOn(final Date mExpiresOn) {
        this.mExpiresOn = mExpiresOn;
    }
    
    final void setExtendedExpiresOn(final Date mExtendedExpiresOn) {
        this.mExtendedExpiresOn = mExtendedExpiresOn;
    }
    
    final void setFamilyClientId(final String mFamilyClientId) {
        this.mFamilyClientId = mFamilyClientId;
    }
    
    void setHttpResponse(final HttpWebResponse httpWebResponse) {
        if (httpWebResponse != null) {
            this.mServiceStatusCode = httpWebResponse.getStatusCode();
            if (httpWebResponse.getResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<String, List<String>>(httpWebResponse.getResponseHeaders());
            }
            if (httpWebResponse.getBody() != null) {
                try {
                    this.mHttpResponseBody = new HashMap<String, String>(HashMapExtensions.getJsonResponse(httpWebResponse));
                }
                catch (JSONException ex) {
                    Logger.e(AuthenticationException.class.getSimpleName(), "Json exception", ExceptionExtensions.getExceptionMessage((Exception)ex), ADALError.SERVER_INVALID_JSON_RESPONSE);
                }
            }
        }
    }
    
    void setHttpResponseBody(final HashMap<String, String> mHttpResponseBody) {
        this.mHttpResponseBody = mHttpResponseBody;
    }
    
    void setHttpResponseHeaders(final HashMap<String, List<String>> mHttpResponseHeaders) {
        this.mHttpResponseHeaders = mHttpResponseHeaders;
    }
    
    void setIdToken(final String mIdToken) {
        this.mIdToken = mIdToken;
    }
    
    final void setIsExtendedLifeTimeToken(final boolean mIsExtendedLifeTimeToken) {
        this.mIsExtendedLifeTimeToken = mIsExtendedLifeTimeToken;
    }
    
    void setRefreshToken(final String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }
    
    void setServiceStatusCode(final int mServiceStatusCode) {
        this.mServiceStatusCode = mServiceStatusCode;
    }
    
    void setTenantId(final String mTenantId) {
        this.mTenantId = mTenantId;
    }
    
    void setUserInfo(final UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }
    
    public enum AuthenticationStatus
    {
        Cancelled, 
        Failed, 
        Succeeded;
    }
}
