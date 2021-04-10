package com.microsoft.aad.adal;

import android.os.*;
import android.app.*;
import android.graphics.*;
import android.view.*;
import android.content.*;
import android.webkit.*;
import java.io.*;
import android.widget.*;

class AuthenticationDialog
{
    protected static final String TAG = "AuthenticationDialog";
    private final AcquireTokenRequest mAcquireTokenRequest;
    private final Context mContext;
    private Dialog mDialog;
    private final Handler mHandlerInView;
    private final AuthenticationRequest mRequest;
    private WebView mWebView;
    
    public AuthenticationDialog(final Handler mHandlerInView, final Context mContext, final AcquireTokenRequest mAcquireTokenRequest, final AuthenticationRequest mRequest) {
        this.mHandlerInView = mHandlerInView;
        this.mContext = mContext;
        this.mAcquireTokenRequest = mAcquireTokenRequest;
        this.mRequest = mRequest;
    }
    
    private void cancelFlow() {
        Logger.i("AuthenticationDialog", "Cancelling dialog", "");
        final Intent intent = new Intent();
        intent.putExtra("com.microsoft.aad.adal:RequestId", this.mRequest.getRequestId());
        this.mAcquireTokenRequest.onActivityResult(1001, 2001, intent);
        final Handler mHandlerInView = this.mHandlerInView;
        if (mHandlerInView != null) {
            mHandlerInView.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (AuthenticationDialog.this.mDialog != null && AuthenticationDialog.this.mDialog.isShowing()) {
                        AuthenticationDialog.this.mDialog.dismiss();
                    }
                }
            });
        }
    }
    
    private int getResourceId(final String s, final String s2) {
        return this.mContext.getResources().getIdentifier(s, s2, this.mContext.getPackageName());
    }
    
    public void show() {
        this.mHandlerInView.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final LayoutInflater layoutInflater = (LayoutInflater)AuthenticationDialog.this.mContext.getSystemService("layout_inflater");
                final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(AuthenticationDialog.this.mContext);
                final View inflate = layoutInflater.inflate(AuthenticationDialog.this.getResourceId("dialog_authentication", "layout"), (ViewGroup)null);
                final AuthenticationDialog this$0 = AuthenticationDialog.this;
                this$0.mWebView = (WebView)inflate.findViewById(this$0.getResourceId("com_microsoft_aad_adal_webView1", "id"));
                if (AuthenticationDialog.this.mWebView == null) {
                    Logger.e("AuthenticationDialog:show", "Expected resource name for webview is com_microsoft_aad_adal_webView1. It is not in your layout file", "", ADALError.DEVELOPER_DIALOG_LAYOUT_INVALID);
                    final Intent intent = new Intent();
                    intent.putExtra("com.microsoft.aad.adal:RequestId", AuthenticationDialog.this.mRequest.getRequestId());
                    AuthenticationDialog.this.mAcquireTokenRequest.onActivityResult(1001, 2001, intent);
                    if (AuthenticationDialog.this.mHandlerInView != null) {
                        AuthenticationDialog.this.mHandlerInView.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                if (AuthenticationDialog.this.mDialog != null && AuthenticationDialog.this.mDialog.isShowing()) {
                                    AuthenticationDialog.this.mDialog.dismiss();
                                }
                            }
                        });
                    }
                    return;
                }
                if (!AuthenticationSettings.INSTANCE.getDisableWebViewHardwareAcceleration()) {
                    AuthenticationDialog.this.mWebView.setLayerType(1, (Paint)null);
                    Logger.d("AuthenticationDialog:show", "Hardware acceleration is disabled in WebView");
                }
                AuthenticationDialog.this.mWebView.getSettings().setJavaScriptEnabled(true);
                AuthenticationDialog.this.mWebView.requestFocus(130);
                final String userAgentString = AuthenticationDialog.this.mWebView.getSettings().getUserAgentString();
                final WebSettings settings = AuthenticationDialog.this.mWebView.getSettings();
                final StringBuilder sb = new StringBuilder();
                sb.append(userAgentString);
                sb.append(" PKeyAuth/1.0");
                settings.setUserAgentString(sb.toString());
                final String userAgentString2 = AuthenticationDialog.this.mWebView.getSettings().getUserAgentString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("UserAgent:");
                sb2.append(userAgentString2);
                Logger.v("AuthenticationDialog:show", sb2.toString());
                AuthenticationDialog.this.mWebView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                    public boolean onTouch(final View view, final MotionEvent motionEvent) {
                        final int action = motionEvent.getAction();
                        if ((action == 0 || action == 1) && !view.hasFocus()) {
                            view.requestFocus();
                        }
                        return false;
                    }
                });
                AuthenticationDialog.this.mWebView.getSettings().setLoadWithOverviewMode(true);
                AuthenticationDialog.this.mWebView.getSettings().setDomStorageEnabled(true);
                AuthenticationDialog.this.mWebView.getSettings().setUseWideViewPort(true);
                AuthenticationDialog.this.mWebView.getSettings().setBuiltInZoomControls(true);
                try {
                    final String codeRequestUrl = new Oauth2(AuthenticationDialog.this.mRequest).getCodeRequestUrl();
                    AuthenticationDialog.this.mWebView.setWebViewClient((WebViewClient)new DialogWebViewClient(AuthenticationDialog.this.mContext, AuthenticationDialog.this.mRequest.getRedirectUri(), AuthenticationDialog.this.mRequest));
                    AuthenticationDialog.this.mWebView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            AuthenticationDialog.this.mWebView.loadUrl("about:blank");
                            AuthenticationDialog.this.mWebView.loadUrl(codeRequestUrl);
                        }
                    });
                }
                catch (UnsupportedEncodingException ex) {
                    Logger.e("AuthenticationDialog:show", "Encoding error", "", ADALError.ENCODING_IS_NOT_SUPPORTED, ex);
                }
                alertDialog$Builder.setView(inflate).setCancelable(true);
                alertDialog$Builder.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                    public void onCancel(final DialogInterface dialogInterface) {
                        AuthenticationDialog.this.cancelFlow();
                    }
                });
                AuthenticationDialog.this.mDialog = (Dialog)alertDialog$Builder.create();
                Logger.i("AuthenticationDialog:show", "Showing authenticationDialog", "");
                AuthenticationDialog.this.mDialog.show();
            }
        });
    }
    
    class DialogWebViewClient extends BasicWebViewClient
    {
        public DialogWebViewClient(final Context context, final String s, final AuthenticationRequest authenticationRequest) {
            super(context, s, authenticationRequest, null);
        }
        
        @Override
        public void cancelWebViewRequest() {
            AuthenticationDialog.this.cancelFlow();
        }
        
        @Override
        public void postRunnable(final Runnable runnable) {
            AuthenticationDialog.this.mHandlerInView.post(runnable);
        }
        
        @Override
        public void prepareForBrokerResumeRequest() {
        }
        
        @Override
        public boolean processInvalidUrl(final WebView webView, final String s) {
            return false;
        }
        
        @Override
        public void processRedirectUrl(final WebView webView, final String s) {
            final Intent intent = new Intent();
            intent.putExtra("com.microsoft.aad.adal:BrowserFinalUrl", s);
            intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)AuthenticationDialog.this.mRequest);
            intent.putExtra("com.microsoft.aad.adal:RequestId", AuthenticationDialog.this.mRequest.getRequestId());
            this.sendResponse(2003, intent);
            webView.stopLoading();
        }
        
        @Override
        public void sendResponse(final int n, final Intent intent) {
            AuthenticationDialog.this.mDialog.dismiss();
            AuthenticationDialog.this.mAcquireTokenRequest.onActivityResult(1001, n, intent);
        }
        
        @Override
        public void setPKeyAuthStatus(final boolean b) {
        }
        
        @Override
        public void showSpinner(final boolean b) {
            if (AuthenticationDialog.this.mHandlerInView != null) {
                AuthenticationDialog.this.mHandlerInView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (AuthenticationDialog.this.mDialog != null && AuthenticationDialog.this.mDialog.isShowing()) {
                            final ProgressBar progressBar = (ProgressBar)AuthenticationDialog.this.mDialog.findViewById(AuthenticationDialog.this.getResourceId("com_microsoft_aad_adal_progressBar", "id"));
                            if (progressBar != null) {
                                int visibility;
                                if (b) {
                                    visibility = 0;
                                }
                                else {
                                    visibility = 4;
                                }
                                progressBar.setVisibility(visibility);
                            }
                        }
                    }
                });
            }
        }
    }
}
