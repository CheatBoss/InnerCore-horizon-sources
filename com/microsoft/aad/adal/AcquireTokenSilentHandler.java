package com.microsoft.aad.adal;

import android.content.*;
import java.net.*;
import android.util.*;
import java.io.*;

class AcquireTokenSilentHandler
{
    private static final String TAG;
    private boolean mAttemptedWithMRRT;
    private final AuthenticationRequest mAuthRequest;
    private final Context mContext;
    private TokenCacheItem mMrrtTokenCacheItem;
    private final TokenCacheAccessor mTokenCacheAccessor;
    private IWebRequestHandler mWebRequestHandler;
    
    static {
        TAG = AcquireTokenSilentHandler.class.getSimpleName();
    }
    
    AcquireTokenSilentHandler(final Context mContext, final AuthenticationRequest mAuthRequest, final TokenCacheAccessor mTokenCacheAccessor) {
        this.mAttemptedWithMRRT = false;
        this.mWebRequestHandler = null;
        if (mContext == null) {
            throw new IllegalArgumentException("context");
        }
        if (mAuthRequest != null) {
            this.mContext = mContext;
            this.mAuthRequest = mAuthRequest;
            this.mTokenCacheAccessor = mTokenCacheAccessor;
            this.mWebRequestHandler = new WebRequestHandler();
            return;
        }
        throw new IllegalArgumentException("authRequest");
    }
    
    private AuthenticationResult acquireTokenWithCachedItem(final TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(tokenCacheItem.getRefreshToken())) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenSilentHandler.TAG);
            sb.append(":acquireTokenWithCachedItem");
            Logger.v(sb.toString(), "Token cache item contains empty refresh token, cannot continue refresh token request", this.mAuthRequest.getLogInfo(), null);
            return null;
        }
        final AuthenticationResult acquireTokenWithRefreshToken = this.acquireTokenWithRefreshToken(tokenCacheItem.getRefreshToken());
        if (acquireTokenWithRefreshToken != null && !acquireTokenWithRefreshToken.isExtendedLifeTimeToken()) {
            this.mTokenCacheAccessor.updateCachedItemWithResult(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), acquireTokenWithRefreshToken, tokenCacheItem);
        }
        return acquireTokenWithRefreshToken;
    }
    
    private boolean isMRRTEntryExisted() throws AuthenticationException {
        try {
            final TokenCacheItem mrrtItem = this.mTokenCacheAccessor.getMRRTItem(this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            return mrrtItem != null && !StringExtensions.isNullOrBlank(mrrtItem.getRefreshToken());
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    private boolean isTokenRequestFailed(final AuthenticationResult authenticationResult) {
        return authenticationResult != null && !StringExtensions.isNullOrBlank(authenticationResult.getErrorCode());
    }
    
    private AuthenticationResult tryFRT(final String s, AuthenticationResult authenticationResult) throws AuthenticationException {
        try {
            final TokenCacheItem frtItem = this.mTokenCacheAccessor.getFRTItem(s, this.mAuthRequest.getUserFromRequest());
            if (frtItem != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenSilentHandler.TAG);
                sb.append(":tryFRT");
                Logger.v(sb.toString(), "Send request to use FRT for new AT.");
                AuthenticationResult authenticationResult2;
                authenticationResult = (authenticationResult2 = this.acquireTokenWithCachedItem(frtItem));
                if (this.isTokenRequestFailed(authenticationResult)) {
                    authenticationResult2 = authenticationResult;
                    if (!this.mAttemptedWithMRRT) {
                        authenticationResult2 = this.useMRRT();
                        if (authenticationResult2 == null) {
                            return authenticationResult;
                        }
                    }
                }
                return authenticationResult2;
            }
            if (!this.mAttemptedWithMRRT) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(AcquireTokenSilentHandler.TAG);
                sb2.append(":tryFRT");
                Logger.v(sb2.toString(), "FRT cache item does not exist, fall back to try MRRT.");
                return this.useMRRT();
            }
            return authenticationResult;
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    private AuthenticationResult tryMRRT() throws AuthenticationException {
        try {
            final TokenCacheItem mrrtItem = this.mTokenCacheAccessor.getMRRTItem(this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            this.mMrrtTokenCacheItem = mrrtItem;
            final String s = "1";
            String familyClientId;
            if (mrrtItem == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenSilentHandler.TAG);
                sb.append(":tryMRRT");
                Logger.v(sb.toString(), "MRRT token does not exist, try with FRT");
                familyClientId = "1";
            }
            else if (mrrtItem.isFamilyToken()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(AcquireTokenSilentHandler.TAG);
                sb2.append(":tryMRRT");
                Logger.v(sb2.toString(), "MRRT item exists but it's also a FRT, try with FRT.");
                familyClientId = this.mMrrtTokenCacheItem.getFamilyClientId();
            }
            else {
                AuthenticationResult authenticationResult2;
                final AuthenticationResult authenticationResult = authenticationResult2 = this.useMRRT();
                if (this.isTokenRequestFailed(authenticationResult)) {
                    String familyClientId2;
                    if (StringExtensions.isNullOrBlank(this.mMrrtTokenCacheItem.getFamilyClientId())) {
                        familyClientId2 = s;
                    }
                    else {
                        familyClientId2 = this.mMrrtTokenCacheItem.getFamilyClientId();
                    }
                    authenticationResult2 = this.tryFRT(familyClientId2, authenticationResult);
                }
                if (!StringExtensions.isNullOrBlank(this.mAuthRequest.getUserFromRequest())) {
                    return authenticationResult2;
                }
                if (!this.mTokenCacheAccessor.isMultipleMRRTsMatchingGivenApp(this.mAuthRequest.getClientId())) {
                    return authenticationResult2;
                }
                throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH, "No User provided and multiple MRRTs exist for the given client id");
            }
            return this.tryFRT(familyClientId, null);
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    private AuthenticationResult tryRT() throws AuthenticationException {
        try {
            final TokenCacheItem regularRefreshTokenCacheItem = this.mTokenCacheAccessor.getRegularRefreshTokenCacheItem(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            if (regularRefreshTokenCacheItem == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenSilentHandler.TAG);
                sb.append(":tryRT");
                Logger.v(sb.toString(), "Regular token cache entry does not exist, try with MRRT.");
                return this.tryMRRT();
            }
            if (regularRefreshTokenCacheItem.getIsMultiResourceRefreshToken() || this.isMRRTEntryExisted()) {
                String s;
                if (regularRefreshTokenCacheItem.getIsMultiResourceRefreshToken()) {
                    s = "Found RT and it's also a MRRT, retry with MRRT";
                }
                else {
                    s = "RT is found and there is a MRRT entry existed, try with MRRT";
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(AcquireTokenSilentHandler.TAG);
                sb2.append(":tryRT");
                Logger.v(sb2.toString(), s);
                return this.tryMRRT();
            }
            if (StringExtensions.isNullOrBlank(this.mAuthRequest.getUserFromRequest()) && this.mTokenCacheAccessor.isMultipleRTsMatchingGivenAppAndResource(this.mAuthRequest.getClientId(), this.mAuthRequest.getResource())) {
                throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH, "Multiple refresh tokens exists for the given client id and resource");
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(AcquireTokenSilentHandler.TAG);
            sb3.append(":tryRT");
            Logger.v(sb3.toString(), "Send request to use regular RT for new AT.");
            return this.acquireTokenWithCachedItem(regularRefreshTokenCacheItem);
        }
        catch (MalformedURLException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
        }
    }
    
    private AuthenticationResult useMRRT() throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenSilentHandler.TAG);
        sb.append(":useMRRT");
        Logger.v(sb.toString(), "Send request to use MRRT for new AT.");
        this.mAttemptedWithMRRT = true;
        final TokenCacheItem mMrrtTokenCacheItem = this.mMrrtTokenCacheItem;
        if (mMrrtTokenCacheItem == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenSilentHandler.TAG);
            sb2.append(":useMRRT");
            Logger.v(sb2.toString(), "MRRT does not exist, cannot proceed with MRRT for new AT.");
            return null;
        }
        return this.acquireTokenWithCachedItem(mMrrtTokenCacheItem);
    }
    
    AuthenticationResult acquireTokenWithRefreshToken(final String refreshToken) throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenSilentHandler.TAG);
        sb.append(":acquireTokenWithRefreshToken");
        Logger.v(sb.toString(), "Try to get new access token with the found refresh token.", this.mAuthRequest.getLogInfo(), null);
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        try {
            final AuthenticationResult refreshToken2 = new Oauth2(this.mAuthRequest, this.mWebRequestHandler, new JWSBuilder()).refreshToken(refreshToken);
            if (refreshToken2 != null && StringExtensions.isNullOrBlank(refreshToken2.getRefreshToken())) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(AcquireTokenSilentHandler.TAG);
                sb2.append(":acquireTokenWithRefreshToken");
                Logger.i(sb2.toString(), "Refresh token is not returned or empty", "");
                refreshToken2.setRefreshToken(refreshToken);
            }
            return refreshToken2;
        }
        catch (IOException | AuthenticationException ex4) {
            final AuthenticationException ex2;
            final AuthenticationException ex = ex2;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(AcquireTokenSilentHandler.TAG);
            sb3.append(":acquireTokenWithRefreshToken");
            final String string = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Request: ");
            sb4.append(this.mAuthRequest.getLogInfo());
            sb4.append(" ");
            sb4.append(ExceptionExtensions.getExceptionMessage(ex));
            sb4.append(" ");
            sb4.append(Log.getStackTraceString((Throwable)ex));
            Logger.e(string, "Error in refresh token for request.", sb4.toString(), ADALError.AUTH_FAILED_NO_TOKEN, null);
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_TOKEN, ExceptionExtensions.getExceptionMessage(ex), new AuthenticationException(ADALError.SERVER_ERROR, ex.getMessage(), ex));
        }
        catch (ServerRespondingWithRetryableException ex3) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(AcquireTokenSilentHandler.TAG);
            sb5.append(":acquireTokenWithRefreshToken");
            final String string2 = sb5.toString();
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("The server is not responding after the retry with error code: ");
            sb6.append(ex3.getCode());
            Logger.i(string2, sb6.toString(), "");
            final TokenCacheItem staleToken = this.mTokenCacheAccessor.getStaleToken(this.mAuthRequest);
            if (staleToken != null) {
                final AuthenticationResult extendedLifeTimeResult = AuthenticationResult.createExtendedLifeTimeResult(staleToken);
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(AcquireTokenSilentHandler.TAG);
                sb7.append(":acquireTokenWithRefreshToken");
                Logger.i(sb7.toString(), "The result with stale access token is returned.", "");
                return extendedLifeTimeResult;
            }
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(AcquireTokenSilentHandler.TAG);
            sb8.append(":acquireTokenWithRefreshToken");
            final String string3 = sb8.toString();
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Request: ");
            sb9.append(this.mAuthRequest.getLogInfo());
            sb9.append(" ");
            sb9.append(ExceptionExtensions.getExceptionMessage(ex3));
            sb9.append(" ");
            sb9.append(Log.getStackTraceString((Throwable)ex3));
            Logger.e(string3, "Error in refresh token for request. ", sb9.toString(), ADALError.AUTH_FAILED_NO_TOKEN, null);
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_TOKEN, ExceptionExtensions.getExceptionMessage(ex3), new AuthenticationException(ADALError.SERVER_ERROR, ex3.getMessage(), ex3));
        }
    }
    
    AuthenticationResult getAccessToken() throws AuthenticationException {
        final TokenCacheAccessor mTokenCacheAccessor = this.mTokenCacheAccessor;
        if (mTokenCacheAccessor == null) {
            return null;
        }
        final TokenCacheItem atFromCache = mTokenCacheAccessor.getATFromCache(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
        if (atFromCache == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenSilentHandler.TAG);
            sb.append(":getAccessToken");
            Logger.v(sb.toString(), "No valid access token exists, try with refresh token.");
            return this.tryRT();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(AcquireTokenSilentHandler.TAG);
        sb2.append(":getAccessToken");
        Logger.v(sb2.toString(), "Return AT from cache.");
        return AuthenticationResult.createResult(atFromCache);
    }
    
    void setWebRequestHandler(final IWebRequestHandler mWebRequestHandler) {
        this.mWebRequestHandler = mWebRequestHandler;
    }
}
