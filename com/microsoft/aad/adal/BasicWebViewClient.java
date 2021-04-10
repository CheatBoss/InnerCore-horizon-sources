package com.microsoft.aad.adal;

import android.text.*;
import android.net.*;
import android.graphics.*;
import android.content.*;
import java.io.*;
import android.webkit.*;
import android.net.http.*;
import java.util.*;

abstract class BasicWebViewClient extends WebViewClient
{
    public static final String BLANK_PAGE = "about:blank";
    private static final String INSTALL_URL_KEY = "app_link";
    private static final String TAG = "BasicWebViewClient";
    private final Context mCallingContext;
    private final String mRedirect;
    private final AuthenticationRequest mRequest;
    private final UIEvent mUIEvent;
    
    public BasicWebViewClient(final Context mCallingContext, final String mRedirect, final AuthenticationRequest mRequest, final UIEvent muiEvent) {
        this.mCallingContext = mCallingContext;
        this.mRedirect = mRedirect;
        this.mRequest = mRequest;
        this.mUIEvent = muiEvent;
    }
    
    private boolean hasCancelError(String s) {
        final HashMap<String, String> urlParameters = StringExtensions.getUrlParameters(s);
        s = (String)urlParameters.get("error");
        final String s2 = urlParameters.get("error_description");
        if (!StringExtensions.isNullOrBlank(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cancel error: ");
            sb.append(s);
            Logger.w("BasicWebViewClient", sb.toString(), s2, null);
            return true;
        }
        return false;
    }
    
    private void logPageStartLoadingUrl(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            Logger.v("BasicWebViewClient:logPageStartLoadingUrl", "onPageStarted: Null url for page to load.");
            return;
        }
        final Uri parse = Uri.parse(s);
        if (parse.isOpaque()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Url: ");
            sb.append(s);
            Logger.v("BasicWebViewClient:logPageStartLoadingUrl", "onPageStarted: Non-hierarchical loading uri. ", sb.toString(), null);
            return;
        }
        if (StringExtensions.isNullOrBlank(parse.getQueryParameter("code"))) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" Host: ");
            sb2.append(parse.getHost());
            sb2.append(" Path: ");
            sb2.append(parse.getPath());
            sb2.append(" Full loading url is: ");
            sb2.append(s);
            Logger.v("BasicWebViewClient:logPageStartLoadingUrl", "Webview starts loading. ", sb2.toString(), null);
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(" Host: ");
        sb3.append(parse.getHost());
        sb3.append(" Path: ");
        sb3.append(parse.getPath());
        sb3.append(" Auth code is returned for the loading url.");
        Logger.v("BasicWebViewClient:logPageStartLoadingUrl", "Webview starts loading. ", sb3.toString(), null);
    }
    
    public abstract void cancelWebViewRequest();
    
    final Context getCallingContext() {
        return this.mCallingContext;
    }
    
    public void onPageFinished(final WebView webView, final String s) {
        super.onPageFinished(webView, s);
        webView.setVisibility(0);
        if (!s.startsWith("about:blank")) {
            this.showSpinner(false);
        }
    }
    
    public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
        this.logPageStartLoadingUrl(s);
        super.onPageStarted(webView, s, bitmap);
        this.showSpinner(true);
    }
    
    public void onReceivedError(final WebView webView, final int n, final String s, final String s2) {
        super.onReceivedError(webView, n, s, s2);
        this.showSpinner(false);
        final StringBuilder sb = new StringBuilder();
        sb.append("Webview received an error. ErrorCode:");
        sb.append(n);
        Logger.e("BasicWebViewClient", sb.toString(), s, ADALError.ERROR_WEBVIEW);
        final Intent intent = new Intent();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Error Code:");
        sb2.append(n);
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorCode", sb2.toString());
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorMessage", s);
        intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)this.mRequest);
        this.sendResponse(2002, intent);
    }
    
    public void onReceivedHttpAuthRequest(final WebView webView, final HttpAuthHandler httpAuthHandler, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Host:");
        sb.append(s);
        Logger.i("BasicWebViewClient:onReceivedHttpAuthRequest", "Start. ", sb.toString());
        this.mUIEvent.setNTLM(true);
        final HttpAuthDialog httpAuthDialog = new HttpAuthDialog(this.mCallingContext, s, s2);
        httpAuthDialog.setOkListener((HttpAuthDialog.OkListener)new HttpAuthDialog.OkListener() {
            @Override
            public void onOk(final String s, final String s2, final String s3, final String s4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Host: ");
                sb.append(s);
                Logger.i("BasicWebViewClient:onReceivedHttpAuthRequest", "Handler proceed. ", sb.toString());
                httpAuthHandler.proceed(s3, s4);
            }
        });
        httpAuthDialog.setCancelListener((HttpAuthDialog.CancelListener)new HttpAuthDialog.CancelListener() {
            @Override
            public void onCancel() {
                Logger.i("BasicWebViewClient:onReceivedHttpAuthRequest", "Handler cancelled", "");
                httpAuthHandler.cancel();
                BasicWebViewClient.this.cancelWebViewRequest();
            }
        });
        Logger.i("BasicWebViewClient:onReceivedHttpAuthRequest", "Show dialog. ", "");
        httpAuthDialog.show();
    }
    
    public void onReceivedSslError(final WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        this.showSpinner(false);
        sslErrorHandler.cancel();
        Logger.e("BasicWebViewClient", "Received ssl error. ", "", ADALError.ERROR_FAILED_SSL_HANDSHAKE);
        final Intent intent = new Intent();
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorCode", "Code:-11");
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorMessage", sslError.toString());
        intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)this.mRequest);
        this.sendResponse(2002, intent);
    }
    
    protected void openLinkInBrowser(final String s) {
        this.mCallingContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s.replace("browser://", "https://"))));
    }
    
    public abstract void postRunnable(final Runnable p0);
    
    public abstract void prepareForBrokerResumeRequest();
    
    public abstract boolean processInvalidUrl(final WebView p0, final String p1);
    
    public abstract void processRedirectUrl(final WebView p0, final String p1);
    
    public abstract void sendResponse(final int p0, final Intent p1);
    
    public abstract void setPKeyAuthStatus(final boolean p0);
    
    public boolean shouldOverrideUrlLoading(final WebView webView, String urlParameters) {
        Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "Navigation is detected");
        if (urlParameters.startsWith("urn:http-auth:PKeyAuth")) {
            Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "Webview detected request for pkeyauth challenge.");
            webView.stopLoading();
            this.setPKeyAuthStatus(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final ChallengeResponseBuilder.ChallengeResponse challengeResponseFromUri = new ChallengeResponseBuilder(new JWSBuilder()).getChallengeResponseFromUri(urlParameters);
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("Authorization", challengeResponseFromUri.getAuthorizationHeaderValue());
                        BasicWebViewClient.this.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                final String submitUrl = challengeResponseFromUri.getSubmitUrl();
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Challenge submit url:");
                                sb.append(challengeResponseFromUri.getSubmitUrl());
                                Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "Respond to pkeyAuth challenge", sb.toString(), null);
                                webView.loadUrl(submitUrl, hashMap);
                            }
                        });
                    }
                    catch (AuthenticationException ex) {
                        Logger.e("BasicWebViewClient:shouldOverrideUrlLoading", "It is failed to create device certificate response", ex.getMessage(), ADALError.DEVICE_CERTIFICATE_RESPONSE_FAILED, ex);
                        new Intent().putExtra("com.microsoft.aad.adal:AuthenticationException", (Serializable)ex);
                        if (BasicWebViewClient.this.mRequest == null) {
                            goto Label_0121;
                        }
                    }
                    catch (AuthenticationServerProtocolException ex2) {
                        Logger.e("BasicWebViewClient:shouldOverrideUrlLoading", "Argument exception. ", ex2.getMessage(), ADALError.ARGUMENT_EXCEPTION, ex2);
                        new Intent().putExtra("com.microsoft.aad.adal:AuthenticationException", (Serializable)ex2);
                        if (BasicWebViewClient.this.mRequest != null) {
                            goto Label_0107;
                        }
                        goto Label_0121;
                    }
                }
            }).start();
            return true;
        }
        if (urlParameters.toLowerCase(Locale.US).startsWith(this.mRedirect.toLowerCase(Locale.US))) {
            Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "Navigation starts with the redirect uri.");
            if (this.hasCancelError(urlParameters)) {
                Logger.i("BasicWebViewClient:shouldOverrideUrlLoading", "Sending intent to cancel authentication activity", "");
                webView.stopLoading();
                this.cancelWebViewRequest();
                return true;
            }
            this.processRedirectUrl(webView, urlParameters);
            return true;
        }
        else {
            if (urlParameters.startsWith("browser://")) {
                Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "It is an external website request");
                this.openLinkInBrowser(urlParameters);
                webView.stopLoading();
                this.cancelWebViewRequest();
                return true;
            }
            if (urlParameters.startsWith("msauth://")) {
                Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "It is an install request");
                urlParameters = (String)StringExtensions.getUrlParameters(urlParameters);
                this.prepareForBrokerResumeRequest();
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {
                    Logger.v("BasicWebViewClient:shouldOverrideUrlLoading", "Error occurred when having thread sleeping for 1 second.");
                }
                this.openLinkInBrowser(((HashMap<K, String>)urlParameters).get((Object)"app_link"));
                webView.stopLoading();
                return true;
            }
            return this.processInvalidUrl(webView, urlParameters);
        }
    }
    
    public abstract void showSpinner(final boolean p0);
}
