package com.microsoft.aad.adal;

import android.content.*;
import java.util.*;
import java.io.*;
import android.text.*;

public class WebviewHelper
{
    private static final String TAG = "WebviewHelper";
    private final Oauth2 mOauth;
    private final AuthenticationRequest mRequest;
    private final Intent mRequestIntent;
    
    public WebviewHelper(final Intent mRequestIntent) {
        this.mRequestIntent = mRequestIntent;
        this.mRequest = this.getAuthenticationRequestFromIntent(mRequestIntent);
        this.mOauth = new Oauth2(this.mRequest);
    }
    
    private AuthenticationRequest getAuthenticationRequestFromIntent(final Intent intent) {
        final Serializable serializableExtra = intent.getSerializableExtra("com.microsoft.aad.adal:BrowserRequestMessage");
        if (serializableExtra instanceof AuthenticationRequest) {
            return (AuthenticationRequest)serializableExtra;
        }
        return null;
    }
    
    public PreKeyAuthInfo getPreKeyAuthInfo(String string) throws UnsupportedEncodingException, AuthenticationException {
        final ChallengeResponseBuilder.ChallengeResponse challengeResponseFromUri = new ChallengeResponseBuilder(new JWSBuilder()).getChallengeResponseFromUri(string);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Authorization", challengeResponseFromUri.getAuthorizationHeaderValue());
        final String submitUrl = challengeResponseFromUri.getSubmitUrl();
        final HashMap<String, String> urlParameters = StringExtensions.getUrlParameters(challengeResponseFromUri.getSubmitUrl());
        final StringBuilder sb = new StringBuilder();
        sb.append("SubmitUrl:");
        sb.append(challengeResponseFromUri.getSubmitUrl());
        Logger.i("WebviewHelper", "Get submit url. ", sb.toString());
        string = submitUrl;
        if (!urlParameters.containsKey("client_id")) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(submitUrl);
            sb2.append("?");
            sb2.append(this.mOauth.getAuthorizationEndpointQueryParameters());
            string = sb2.toString();
        }
        return new PreKeyAuthInfo(hashMap, string);
    }
    
    public String getRedirectUrl() {
        return this.mRequest.getRedirectUri();
    }
    
    public Intent getResultIntent(final String s) {
        final Intent mRequestIntent = this.mRequestIntent;
        if (mRequestIntent != null) {
            final AuthenticationRequest authenticationRequestFromIntent = this.getAuthenticationRequestFromIntent(mRequestIntent);
            final Intent intent = new Intent();
            intent.putExtra("com.microsoft.aad.adal:BrowserFinalUrl", s);
            intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)authenticationRequestFromIntent);
            intent.putExtra("com.microsoft.aad.adal:RequestId", authenticationRequestFromIntent.getRequestId());
            return intent;
        }
        throw new IllegalArgumentException("requestIntent is null");
    }
    
    public String getStartUrl() throws UnsupportedEncodingException {
        return this.mOauth.getCodeRequestUrl();
    }
    
    public void validateRequestIntent() {
        final AuthenticationRequest mRequest = this.mRequest;
        if (mRequest == null) {
            Logger.v("WebviewHelper", "Request item is null, so it returns to caller");
            throw new IllegalArgumentException("Request is null");
        }
        if (TextUtils.isEmpty((CharSequence)mRequest.getAuthority())) {
            throw new IllegalArgumentException("Authority is null");
        }
        if (TextUtils.isEmpty((CharSequence)this.mRequest.getResource())) {
            throw new IllegalArgumentException("Resource is null");
        }
        if (TextUtils.isEmpty((CharSequence)this.mRequest.getClientId())) {
            throw new IllegalArgumentException("ClientId is null");
        }
        if (!TextUtils.isEmpty((CharSequence)this.mRequest.getRedirectUri())) {
            return;
        }
        throw new IllegalArgumentException("RedirectUri is null");
    }
    
    public static class PreKeyAuthInfo
    {
        private final HashMap<String, String> mHttpHeaders;
        private final String mLoadUrl;
        
        public PreKeyAuthInfo(final HashMap<String, String> mHttpHeaders, final String mLoadUrl) {
            this.mHttpHeaders = mHttpHeaders;
            this.mLoadUrl = mLoadUrl;
        }
        
        public HashMap<String, String> getHttpHeaders() {
            return this.mHttpHeaders;
        }
        
        public String getLoadUrl() {
            return this.mLoadUrl;
        }
    }
}
