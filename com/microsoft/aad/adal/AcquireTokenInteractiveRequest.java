package com.microsoft.aad.adal;

import android.content.pm.*;
import android.content.*;
import java.net.*;
import java.io.*;

final class AcquireTokenInteractiveRequest
{
    private static final String TAG;
    private final AuthenticationRequest mAuthRequest;
    private final Context mContext;
    private final TokenCacheAccessor mTokenCacheAccessor;
    
    static {
        TAG = AcquireTokenInteractiveRequest.class.getSimpleName();
    }
    
    AcquireTokenInteractiveRequest(final Context mContext, final AuthenticationRequest mAuthRequest, final TokenCacheAccessor mTokenCacheAccessor) {
        this.mContext = mContext;
        this.mTokenCacheAccessor = mTokenCacheAccessor;
        this.mAuthRequest = mAuthRequest;
    }
    
    private Intent getAuthenticationActivityIntent() {
        final Intent intent = new Intent();
        if (AuthenticationSettings.INSTANCE.getActivityPackageName() != null) {
            intent.setClassName(AuthenticationSettings.INSTANCE.getActivityPackageName(), AuthenticationActivity.class.getName());
        }
        else {
            intent.setClass(this.mContext, (Class)AuthenticationActivity.class);
        }
        intent.putExtra("com.microsoft.aad.adal:BrowserRequestMessage", (Serializable)this.mAuthRequest);
        return intent;
    }
    
    private String getCorrelationInfo() {
        return String.format(" CorrelationId: %s", this.mAuthRequest.getCorrelationId().toString());
    }
    
    private boolean resolveIntent(final Intent intent) {
        final PackageManager packageManager = this.mContext.getPackageManager();
        boolean b = false;
        if (packageManager.resolveActivity(intent, 0) != null) {
            b = true;
        }
        return b;
    }
    
    private boolean startAuthenticationActivity(final IWindowComponent windowComponent) {
        final Intent authenticationActivityIntent = this.getAuthenticationActivityIntent();
        if (!this.resolveIntent(authenticationActivityIntent)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenInteractiveRequest.TAG);
            sb.append(":startAuthenticationActivity");
            Logger.e(sb.toString(), "Intent is not resolved", "", ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
            return false;
        }
        try {
            windowComponent.startActivityForResult(authenticationActivityIntent, 1001);
            return true;
        }
        catch (ActivityNotFoundException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenInteractiveRequest.TAG);
            sb2.append(":startAuthenticationActivity");
            Logger.e(sb2.toString(), "Activity login is not found after resolving intent", "", ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED, (Throwable)ex);
            return false;
        }
    }
    
    void acquireToken(final IWindowComponent windowComponent, final AuthenticationDialog authenticationDialog) throws AuthenticationException {
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        if (PromptBehavior.FORCE_PROMPT == this.mAuthRequest.getPrompt()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenInteractiveRequest.TAG);
            sb.append(":acquireToken");
            Logger.v(sb.toString(), "FORCE_PROMPT is set for embedded flow, reset it as Always.");
            this.mAuthRequest.setPrompt(PromptBehavior.Always);
        }
        if (authenticationDialog != null) {
            authenticationDialog.show();
            return;
        }
        if (this.startAuthenticationActivity(windowComponent)) {
            return;
        }
        throw new AuthenticationException(ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
    }
    
    AuthenticationResult acquireTokenWithAuthCode(String string) throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenInteractiveRequest.TAG);
        sb.append(":acquireTokenWithAuthCode");
        Logger.v(sb.toString(), "Start token acquisition with auth code.", this.mAuthRequest.getLogInfo(), null);
        final Oauth2 oauth2 = new Oauth2(this.mAuthRequest, new WebRequestHandler());
        try {
            final AuthenticationResult token = oauth2.getToken(string);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenInteractiveRequest.TAG);
            sb2.append(":acquireTokenWithAuthCode");
            Logger.v(sb2.toString(), "OnActivityResult processed the result.");
            if (token == null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(AcquireTokenInteractiveRequest.TAG);
                sb3.append(":acquireTokenWithAuthCode");
                string = sb3.toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Returned result with exchanging auth code for token is null");
                sb4.append(this.getCorrelationInfo());
                Logger.e(string, sb4.toString(), "", ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN);
                throw new AuthenticationException(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, this.getCorrelationInfo());
            }
            if (StringExtensions.isNullOrBlank(token.getErrorCode())) {
                if (!StringExtensions.isNullOrBlank(token.getAccessToken())) {
                    final TokenCacheAccessor mTokenCacheAccessor = this.mTokenCacheAccessor;
                    if (mTokenCacheAccessor != null) {
                        try {
                            mTokenCacheAccessor.updateTokenCache(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), token);
                            return token;
                        }
                        catch (MalformedURLException ex) {
                            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
                        }
                    }
                }
                return token;
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(AcquireTokenInteractiveRequest.TAG);
            sb5.append(":acquireTokenWithAuthCode");
            final String string2 = sb5.toString();
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(" ErrorCode:");
            sb6.append(token.getErrorCode());
            final String string3 = sb6.toString();
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(" ErrorDescription:");
            sb7.append(token.getErrorDescription());
            Logger.e(string2, string3, sb7.toString(), ADALError.AUTH_FAILED);
            final ADALError auth_FAILED = ADALError.AUTH_FAILED;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(" ErrorCode:");
            sb8.append(token.getErrorCode());
            throw new AuthenticationException(auth_FAILED, sb8.toString());
        }
        catch (IOException | AuthenticationException ex4) {
            final AuthenticationException ex3;
            final AuthenticationException ex2 = ex3;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Error in processing code to get token. ");
            sb9.append(this.getCorrelationInfo());
            throw new AuthenticationException(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, sb9.toString(), ex2);
        }
    }
}
