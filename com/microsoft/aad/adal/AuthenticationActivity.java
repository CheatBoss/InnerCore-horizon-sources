package com.microsoft.aad.adal;

import android.app.*;
import java.net.*;
import android.view.inputmethod.*;
import android.view.*;
import android.graphics.*;
import android.support.v4.content.*;
import android.content.*;
import android.webkit.*;
import android.security.*;
import java.security.cert.*;
import java.util.*;
import android.os.*;
import android.accounts.*;
import java.io.*;
import java.security.*;
import com.google.gson.*;

public class AuthenticationActivity extends Activity
{
    static final int BACK_PRESSED_CANCEL_DIALOG_STEPS = -2;
    private static final String TAG = "AuthenticationActivity";
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private AuthenticationRequest mAuthRequest;
    private Bundle mAuthenticatorResultBundle;
    private String mCallingPackage;
    private int mCallingUID;
    private final IJWSBuilder mJWSBuilder;
    private boolean mPkeyAuthRedirect;
    private ActivityBroadcastReceiver mReceiver;
    private String mRedirectUrl;
    private boolean mRegisterReceiver;
    private ProgressDialog mSpinner;
    private String mStartUrl;
    private StorageHelper mStorageHelper;
    private UIEvent mUIEvent;
    private int mWaitingRequestId;
    private final IWebRequestHandler mWebRequestHandler;
    private WebView mWebView;
    
    public AuthenticationActivity() {
        this.mRegisterReceiver = false;
        this.mReceiver = null;
        this.mAccountAuthenticatorResponse = null;
        this.mAuthenticatorResultBundle = null;
        this.mWebRequestHandler = new WebRequestHandler();
        this.mJWSBuilder = new JWSBuilder();
        this.mPkeyAuthRedirect = false;
        this.mUIEvent = null;
    }
    
    private void cancelRequest() {
        Logger.v("AuthenticationActivity", "Sending intent to cancel authentication activity");
        final Intent intent = new Intent();
        final UIEvent muiEvent = this.mUIEvent;
        if (muiEvent != null) {
            muiEvent.setUserCancel();
        }
        this.returnToCaller(2001, intent);
    }
    
    private void displaySpinner(final boolean b) {
        if (!this.isFinishing() && !this.isChangingConfigurations() && this.mSpinner != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("DisplaySpinner:");
            sb.append(b);
            sb.append(" showing:");
            sb.append(this.mSpinner.isShowing());
            Logger.v("AuthenticationActivity:displaySpinner", sb.toString());
            if (b && !this.mSpinner.isShowing()) {
                this.mSpinner.show();
            }
            if (!b && this.mSpinner.isShowing()) {
                this.mSpinner.dismiss();
            }
        }
    }
    
    private void displaySpinnerWithMessage(final CharSequence message) {
        if (!this.isFinishing()) {
            final ProgressDialog mSpinner = this.mSpinner;
            if (mSpinner != null) {
                mSpinner.show();
                this.mSpinner.setMessage(message);
            }
        }
    }
    
    private AuthenticationRequest getAuthenticationRequestFromIntent(final Intent intent) {
        final boolean brokerRequest = this.isBrokerRequest(intent);
        final AuthenticationRequest authenticationRequest = null;
        if (brokerRequest) {
            Logger.v("AuthenticationActivity:getAuthenticationRequestFromIntent", "It is a broker request. Get request info from bundle extras.");
            final String stringExtra = intent.getStringExtra("account.authority");
            final String stringExtra2 = intent.getStringExtra("account.resource");
            final String stringExtra3 = intent.getStringExtra("account.redirect");
            final String stringExtra4 = intent.getStringExtra("account.login.hint");
            final String stringExtra5 = intent.getStringExtra("account.name");
            final String stringExtra6 = intent.getStringExtra("account.clientid.key");
            final String stringExtra7 = intent.getStringExtra("account.correlationid");
            final String stringExtra8 = intent.getStringExtra("account.prompt");
            PromptBehavior prompt = PromptBehavior.Auto;
            if (!StringExtensions.isNullOrBlank(stringExtra8)) {
                prompt = PromptBehavior.valueOf(stringExtra8);
            }
            this.mWaitingRequestId = intent.getIntExtra("com.microsoft.aad.adal:RequestId", 0);
            UUID fromString = null;
            Label_0171: {
                if (!StringExtensions.isNullOrBlank(stringExtra7)) {
                    try {
                        fromString = UUID.fromString(stringExtra7);
                        break Label_0171;
                    }
                    catch (IllegalArgumentException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("CorrelationId is malformed: ");
                        sb.append(stringExtra7);
                        Logger.e("AuthenticationActivity:getAuthenticationRequestFromIntent", sb.toString(), "", ADALError.CORRELATION_ID_FORMAT);
                    }
                }
                fromString = null;
            }
            final AuthenticationRequest authenticationRequest2 = new AuthenticationRequest(stringExtra, stringExtra2, stringExtra6, stringExtra3, stringExtra4, fromString, false);
            authenticationRequest2.setBrokerAccountName(stringExtra5);
            authenticationRequest2.setPrompt(prompt);
            authenticationRequest2.setRequestId(this.mWaitingRequestId);
            return authenticationRequest2;
        }
        final Serializable serializableExtra = intent.getSerializableExtra("com.microsoft.aad.adal:BrowserRequestMessage");
        AuthenticationRequest authenticationRequest3 = authenticationRequest;
        if (serializableExtra instanceof AuthenticationRequest) {
            authenticationRequest3 = (AuthenticationRequest)serializableExtra;
        }
        return authenticationRequest3;
    }
    
    private String getBrokerStartUrl(final String s, String string, final String s2) {
        if (!StringExtensions.isNullOrBlank(string) && !StringExtensions.isNullOrBlank(s2)) {
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("&package_name=");
                sb.append(URLEncoder.encode(string, "UTF_8"));
                sb.append("&signature=");
                sb.append(URLEncoder.encode(s2, "UTF_8"));
                string = sb.toString();
                return string;
            }
            catch (UnsupportedEncodingException ex) {
                Logger.e("AuthenticationActivity", "Encoding", ex);
            }
        }
        return s;
    }
    
    private void hideKeyBoard() {
        if (this.mWebView != null) {
            ((InputMethodManager)this.getSystemService("input_method")).hideSoftInputFromWindow(this.mWebView.getApplicationWindowToken(), 0);
        }
    }
    
    private boolean isBrokerRequest(final Intent intent) {
        return intent != null && !StringExtensions.isNullOrBlank(intent.getStringExtra("com.microsoft.aadbroker.adal.broker.request"));
    }
    
    private boolean isCallerBrokerInstaller() {
        final PackageHelper packageHelper = new PackageHelper((Context)this);
        final String callingPackage = this.getCallingPackage();
        final boolean nullOrBlank = StringExtensions.isNullOrBlank(callingPackage);
        boolean b = true;
        if (!nullOrBlank) {
            if (callingPackage.equals(AuthenticationSettings.INSTANCE.getBrokerPackageName())) {
                Logger.v("AuthenticationActivity:isCallerBrokerInstaller", "Same package as broker.");
                return true;
            }
            final String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(callingPackage);
            final StringBuilder sb = new StringBuilder();
            sb.append("Check signature for ");
            sb.append(callingPackage);
            sb.append(" signature:");
            sb.append(currentSignatureForPackage);
            sb.append(" brokerSignature:");
            sb.append(AuthenticationSettings.INSTANCE.getBrokerSignature());
            Logger.v("AuthenticationActivity:isCallerBrokerInstaller", "Checking broker signature. ", sb.toString(), null);
            if (currentSignatureForPackage.equals(AuthenticationSettings.INSTANCE.getBrokerSignature())) {
                return b;
            }
            if (currentSignatureForPackage.equals("ho040S3ffZkmxqtQrSwpTVOn9r0=")) {
                return true;
            }
        }
        b = false;
        return b;
    }
    
    private void prepareForBrokerResume() {
        Logger.v("AuthenticationActivity:prepareForBrokerResume", "Return to caller with BROKER_REQUEST_RESUME, and waiting for result.");
        this.returnToCaller(2006, new Intent());
    }
    
    private void returnError(final ADALError adalError, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Argument error:");
        sb.append(s);
        Logger.w("AuthenticationActivity", sb.toString());
        final Intent intent = new Intent();
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorCode", adalError.name());
        intent.putExtra("com.microsoft.aad.adal:BrowserErrorMessage", s);
        if (this.mAuthRequest != null) {
            intent.putExtra("com.microsoft.aad.adal:RequestId", this.mWaitingRequestId);
            intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)this.mAuthRequest);
        }
        this.setResult(2002, intent);
        this.finish();
    }
    
    private void returnResult(final int n, final Intent intent) {
        this.setAccountAuthenticatorResult(intent.getExtras());
        this.setResult(n, intent);
        this.finish();
    }
    
    private void returnToCaller(final int n, final Intent intent) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Return To Caller:");
        sb.append(n);
        Logger.v("AuthenticationActivity:returnToCaller", sb.toString());
        this.displaySpinner(false);
        Intent intent2 = intent;
        if (intent == null) {
            intent2 = new Intent();
        }
        if (this.mAuthRequest == null) {
            Logger.w("AuthenticationActivity:returnToCaller", "Request object is null", "", ADALError.ACTIVITY_REQUEST_INTENT_DATA_IS_NULL);
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Set request id related to response. REQUEST_ID for caller returned to:");
            sb2.append(this.mAuthRequest.getRequestId());
            Logger.v("AuthenticationActivity:returnToCaller", sb2.toString());
            intent2.putExtra("com.microsoft.aad.adal:RequestId", this.mAuthRequest.getRequestId());
        }
        this.setResult(n, intent2);
        this.finish();
    }
    
    private void setAccountAuthenticatorResult(final Bundle mAuthenticatorResultBundle) {
        this.mAuthenticatorResultBundle = mAuthenticatorResultBundle;
    }
    
    private void setupWebView() {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus(130);
        this.mWebView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if ((action == 0 || action == 1) && !view.hasFocus()) {
                    view.requestFocus();
                }
                return false;
            }
        });
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.setWebViewClient((WebViewClient)new CustomWebViewClient());
        this.mWebView.setVisibility(4);
    }
    
    public void finish() {
        if (this.isBrokerRequest(this.getIntent()) && this.mAccountAuthenticatorResponse != null) {
            Logger.v("AuthenticationActivity", "It is a broker request");
            final Bundle mAuthenticatorResultBundle = this.mAuthenticatorResultBundle;
            if (mAuthenticatorResultBundle == null) {
                this.mAccountAuthenticatorResponse.onError(4, "canceled");
            }
            else {
                this.mAccountAuthenticatorResponse.onResult(mAuthenticatorResultBundle);
            }
            this.mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }
    
    public void onBackPressed() {
        Logger.v("AuthenticationActivity", "Back button is pressed");
        if (!this.mPkeyAuthRedirect && this.mWebView.canGoBackOrForward(-2)) {
            this.mWebView.goBack();
            return;
        }
        this.cancelRequest();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(this.getResources().getIdentifier("activity_authentication", "layout", this.getPackageName()));
        CookieSyncManager.createInstance(this.getApplicationContext());
        CookieSyncManager.getInstance().sync();
        CookieManager.getInstance().setAcceptCookie(true);
        Logger.v("AuthenticationActivity:onCreate", "AuthenticationActivity was created.");
        final AuthenticationRequest authenticationRequestFromIntent = this.getAuthenticationRequestFromIntent(this.getIntent());
        this.mAuthRequest = authenticationRequestFromIntent;
        if (authenticationRequestFromIntent == null) {
            Logger.d("AuthenticationActivity:onCreate", "Intent for Authentication Activity doesn't have the request details, returning to caller");
            final Intent intent = new Intent();
            intent.putExtra("com.microsoft.aad.adal:BrowserErrorCode", "Invalid request");
            intent.putExtra("com.microsoft.aad.adal:BrowserErrorMessage", "Intent does not have request details");
            this.returnToCaller(2002, intent);
            return;
        }
        if (authenticationRequestFromIntent.getAuthority() == null || this.mAuthRequest.getAuthority().isEmpty()) {
            this.returnError(ADALError.ARGUMENT_EXCEPTION, "account.authority");
            return;
        }
        if (this.mAuthRequest.getResource() == null || this.mAuthRequest.getResource().isEmpty()) {
            this.returnError(ADALError.ARGUMENT_EXCEPTION, "account.resource");
            return;
        }
        if (this.mAuthRequest.getClientId() != null && !this.mAuthRequest.getClientId().isEmpty()) {
            if (this.mAuthRequest.getRedirectUri() != null) {
                if (!this.mAuthRequest.getRedirectUri().isEmpty()) {
                    this.mRedirectUrl = this.mAuthRequest.getRedirectUri();
                    Telemetry.getInstance().startEvent(this.mAuthRequest.getTelemetryRequestId(), "Microsoft.ADAL.ui_event");
                    (this.mUIEvent = new UIEvent("Microsoft.ADAL.ui_event")).setRequestId(this.mAuthRequest.getTelemetryRequestId());
                    this.mUIEvent.setCorrelationId(this.mAuthRequest.getCorrelationId().toString());
                    this.mWebView = (WebView)this.findViewById(this.getResources().getIdentifier("webView1", "id", this.getPackageName()));
                    if (!AuthenticationSettings.INSTANCE.getDisableWebViewHardwareAcceleration()) {
                        this.mWebView.setLayerType(1, (Paint)null);
                        Logger.d("AuthenticationActivity:onCreate", "Hardware acceleration is disabled in WebView");
                    }
                    this.mStartUrl = "about:blank";
                    try {
                        this.mStartUrl = new Oauth2(this.mAuthRequest).getCodeRequestUrl();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Init broadcastReceiver with request. RequestId:");
                        sb.append(this.mAuthRequest.getRequestId());
                        Logger.v("AuthenticationActivity:onCreate", sb.toString(), this.mAuthRequest.getLogInfo(), null);
                        (this.mReceiver = new ActivityBroadcastReceiver()).mWaitingRequestId = this.mAuthRequest.getRequestId();
                        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mReceiver, new IntentFilter("com.microsoft.aad.adal:BrowserCancel"));
                        final String userAgentString = this.mWebView.getSettings().getUserAgentString();
                        final WebSettings settings = this.mWebView.getSettings();
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(userAgentString);
                        sb2.append(" PKeyAuth/1.0");
                        settings.setUserAgentString(sb2.toString());
                        final String userAgentString2 = this.mWebView.getSettings().getUserAgentString();
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("UserAgent:");
                        sb3.append(userAgentString2);
                        Logger.v("AuthenticationActivity:onCreate", "", sb3.toString(), null);
                        if (this.isBrokerRequest(this.getIntent())) {
                            if ((this.mCallingPackage = this.getCallingPackage()) == null) {
                                Logger.v("AuthenticationActivity:onCreate", "Calling package is null, startActivityForResult is not used to call this activity");
                                final Intent intent2 = new Intent();
                                intent2.putExtra("com.microsoft.aad.adal:BrowserErrorCode", "Invalid request");
                                intent2.putExtra("com.microsoft.aad.adal:BrowserErrorMessage", "startActivityForResult is not used to call this activity");
                                this.returnToCaller(2002, intent2);
                                return;
                            }
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("It is a broker request for package:");
                            sb4.append(this.mCallingPackage);
                            Logger.i("AuthenticationActivity:onCreate", sb4.toString(), "");
                            final AccountAuthenticatorResponse mAccountAuthenticatorResponse = (AccountAuthenticatorResponse)this.getIntent().getParcelableExtra("accountAuthenticatorResponse");
                            if ((this.mAccountAuthenticatorResponse = mAccountAuthenticatorResponse) != null) {
                                mAccountAuthenticatorResponse.onRequestContinued();
                            }
                            final PackageHelper packageHelper = new PackageHelper((Context)this);
                            final String callingPackage = this.getCallingPackage();
                            this.mCallingPackage = callingPackage;
                            this.mCallingUID = packageHelper.getUIDForPackage(callingPackage);
                            final String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(this.mCallingPackage);
                            this.mStartUrl = this.getBrokerStartUrl(this.mStartUrl, this.mCallingPackage, currentSignatureForPackage);
                            if (!this.isCallerBrokerInstaller()) {
                                Logger.v("AuthenticationActivity:onCreate", "Caller needs to be verified using special redirectUri");
                                this.mRedirectUrl = PackageHelper.getBrokerRedirectUrl(this.mCallingPackage, currentSignatureForPackage);
                            }
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("Broker redirectUrl: ");
                            sb5.append(this.mRedirectUrl);
                            sb5.append(" The calling package is: ");
                            sb5.append(this.mCallingPackage);
                            sb5.append(" Signature hash for calling package is: ");
                            sb5.append(currentSignatureForPackage);
                            sb5.append(" Current context package: ");
                            sb5.append(this.getPackageName());
                            sb5.append(" Start url: ");
                            sb5.append(this.mStartUrl);
                            Logger.v("AuthenticationActivity:onCreate", "", sb5.toString(), null);
                        }
                        else {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("Non-broker request for package ");
                            sb6.append(this.getCallingPackage());
                            final String string = sb6.toString();
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append(" Start url: ");
                            sb7.append(this.mStartUrl);
                            Logger.v("AuthenticationActivity:onCreate", string, sb7.toString(), null);
                        }
                        this.mRegisterReceiver = false;
                        final String mStartUrl = this.mStartUrl;
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append("Device info:");
                        sb8.append(Build$VERSION.RELEASE);
                        sb8.append(" ");
                        sb8.append(Build.MANUFACTURER);
                        sb8.append(Build.MODEL);
                        Logger.i("AuthenticationActivity:onCreate", sb8.toString(), "");
                        this.mStorageHelper = new StorageHelper(this.getApplicationContext());
                        this.setupWebView();
                        if (this.mAuthRequest.getCorrelationId() == null) {
                            Logger.v("AuthenticationActivity:onCreate", "Null correlation id in the request.");
                        }
                        else {
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append("Correlation id for request sent is:");
                            sb9.append(this.mAuthRequest.getCorrelationId().toString());
                            Logger.v("AuthenticationActivity:onCreate", sb9.toString());
                        }
                        if (bundle == null) {
                            this.mWebView.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    Logger.v("AuthenticationActivity:onCreate", "Launching webview for acquiring auth code.");
                                    AuthenticationActivity.this.mWebView.loadUrl("about:blank");
                                    AuthenticationActivity.this.mWebView.loadUrl(mStartUrl);
                                }
                            });
                            return;
                        }
                        Logger.v("AuthenticationActivity:onCreate", "Reuse webview");
                        return;
                    }
                    catch (UnsupportedEncodingException ex) {
                        Logger.v("AuthenticationActivity:onCreate", "Encoding format is not supported. ", ex.getMessage(), null);
                        final Intent intent3 = new Intent();
                        intent3.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)this.mAuthRequest);
                        this.returnToCaller(2002, intent3);
                        return;
                    }
                }
            }
            this.returnError(ADALError.ARGUMENT_EXCEPTION, "account.redirect");
            return;
        }
        this.returnError(ADALError.ARGUMENT_EXCEPTION, "account.clientid.key");
    }
    
    protected void onDestroy() {
        super.onDestroy();
        if (this.mUIEvent != null) {
            Telemetry.getInstance().stopEvent(this.mAuthRequest.getTelemetryRequestId(), this.mUIEvent, "Microsoft.ADAL.ui_event");
        }
    }
    
    protected void onPause() {
        Logger.v("AuthenticationActivity:onPause", "AuthenticationActivity onPause unregister receiver");
        super.onPause();
        if (this.mReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mReceiver);
        }
        this.mRegisterReceiver = true;
        if (this.mSpinner != null) {
            Logger.v("AuthenticationActivity:onPause", "Spinner at onPause will dismiss");
            this.mSpinner.dismiss();
        }
        this.hideKeyBoard();
    }
    
    protected void onRestart() {
        Logger.v("AuthenticationActivity", "AuthenticationActivity onRestart");
        super.onRestart();
        this.mRegisterReceiver = true;
    }
    
    protected void onRestoreInstanceState(final Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mWebView.restoreState(bundle);
    }
    
    protected void onResume() {
        super.onResume();
        if (this.mRegisterReceiver) {
            final StringBuilder sb = new StringBuilder();
            sb.append("StartUrl: ");
            sb.append(this.mStartUrl);
            Logger.v("AuthenticationActivity:onResume", "Webview onResume will register receiver. ", sb.toString(), null);
            if (this.mReceiver != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Webview onResume register broadcast receiver for request. RequestId: ");
                sb2.append(this.mReceiver.mWaitingRequestId);
                Logger.v("AuthenticationActivity:onResume", sb2.toString());
                LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mReceiver, new IntentFilter("com.microsoft.aad.adal:BrowserCancel"));
            }
        }
        this.mRegisterReceiver = false;
        (this.mSpinner = new ProgressDialog((Context)this)).requestWindowFeature(1);
        this.mSpinner.setMessage(this.getText(this.getResources().getIdentifier("app_loading", "string", this.getPackageName())));
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mWebView.saveState(bundle);
    }
    
    private class ActivityBroadcastReceiver extends BroadcastReceiver
    {
        private int mWaitingRequestId;
        
        private ActivityBroadcastReceiver() {
            this.mWaitingRequestId = -1;
        }
        
        public void onReceive(final Context context, final Intent intent) {
            Logger.v("AuthenticationActivity:onReceive", "ActivityBroadcastReceiver onReceive");
            if (intent.getAction().equalsIgnoreCase("com.microsoft.aad.adal:BrowserCancel")) {
                Logger.v("AuthenticationActivity:onReceive", "ActivityBroadcastReceiver onReceive action is for cancelling Authentication Activity");
                if (intent.getIntExtra("com.microsoft.aad.adal:RequestId", 0) == this.mWaitingRequestId) {
                    Logger.v("AuthenticationActivity:onReceive", "Waiting requestId is same and cancelling this activity");
                    AuthenticationActivity.this.finish();
                }
            }
        }
    }
    
    class CustomWebViewClient extends BasicWebViewClient
    {
        public CustomWebViewClient() {
            super((Context)AuthenticationActivity.this, AuthenticationActivity.this.mRedirectUrl, AuthenticationActivity.this.mAuthRequest, AuthenticationActivity.this.mUIEvent);
        }
        
        @Override
        public void cancelWebViewRequest() {
            AuthenticationActivity.this.cancelRequest();
        }
        
        public void onReceivedClientCertRequest(final WebView webView, final ClientCertRequest clientCertRequest) {
            Logger.v("AuthenticationActivity:onReceivedClientCertRequest", "Webview receives client TLS request.");
            final Principal[] principals = clientCertRequest.getPrincipals();
            if (principals != null) {
                for (int length = principals.length, i = 0; i < length; ++i) {
                    if (principals[i].getName().contains("CN=MS-Organization-Access")) {
                        Logger.v("AuthenticationActivity:onReceivedClientCertRequest", "Cancelling the TLS request, not respond to TLS challenge triggered by device authentication.");
                        clientCertRequest.cancel();
                        return;
                    }
                }
            }
            KeyChain.choosePrivateKeyAlias((Activity)AuthenticationActivity.this, (KeyChainAliasCallback)new KeyChainAliasCallback() {
                public void alias(String s) {
                    if (s == null) {
                        Logger.v("AuthenticationActivity:onReceivedClientCertRequest", "No certificate chosen by user, cancelling the TLS request.");
                    }
                    else {
                        try {
                            final X509Certificate[] certificateChain = KeyChain.getCertificateChain(AuthenticationActivity.this.getApplicationContext(), s);
                            final PrivateKey privateKey = KeyChain.getPrivateKey(CustomWebViewClient.this.getCallingContext(), s);
                            Logger.v("AuthenticationActivity:onReceivedClientCertRequest", "Certificate is chosen by user, proceed with TLS request.");
                            clientCertRequest.proceed(privateKey, certificateChain);
                            return;
                        }
                        catch (InterruptedException ex) {
                            s = "InterruptedException exception";
                        }
                        catch (KeyChainException ex) {
                            s = "KeyChain exception";
                        }
                        final InterruptedException ex;
                        Logger.e("AuthenticationActivity:onReceivedClientCertRequest", s, ex);
                    }
                    clientCertRequest.cancel();
                }
            }, clientCertRequest.getKeyTypes(), clientCertRequest.getPrincipals(), clientCertRequest.getHost(), clientCertRequest.getPort(), (String)null);
        }
        
        @Override
        public void postRunnable(final Runnable runnable) {
            AuthenticationActivity.this.mWebView.post(runnable);
        }
        
        @Override
        public void prepareForBrokerResumeRequest() {
            AuthenticationActivity.this.prepareForBrokerResume();
        }
        
        @Override
        public boolean processInvalidUrl(final WebView webView, final String s) {
            final AuthenticationActivity this$0 = AuthenticationActivity.this;
            if (this$0.isBrokerRequest(this$0.getIntent()) && s.startsWith("msauth")) {
                Logger.e("AuthenticationActivity:processInvalidUrl", "The RedirectUri is not as expected.", String.format("Received %s and expected %s", s, AuthenticationActivity.this.mRedirectUrl), ADALError.DEVELOPER_REDIRECTURI_INVALID);
                AuthenticationActivity.this.returnError(ADALError.DEVELOPER_REDIRECTURI_INVALID, String.format("The RedirectUri is not as expected. Received %s and expected %s", s, AuthenticationActivity.this.mRedirectUrl));
                webView.stopLoading();
                return true;
            }
            if (s.toLowerCase(Locale.US).equals("about:blank")) {
                Logger.v("AuthenticationActivity:processInvalidUrl", "It is an blank page request");
                return true;
            }
            if (!s.toLowerCase(Locale.US).startsWith("https://")) {
                Logger.e("AuthenticationActivity:processInvalidUrl", "The webview was redirected to an unsafe URL.", "", ADALError.WEBVIEW_REDIRECTURL_NOT_SSL_PROTECTED);
                AuthenticationActivity.this.returnError(ADALError.WEBVIEW_REDIRECTURL_NOT_SSL_PROTECTED, "The webview was redirected to an unsafe URL.");
                webView.stopLoading();
                return true;
            }
            return false;
        }
        
        @Override
        public void processRedirectUrl(final WebView webView, final String s) {
            final AuthenticationActivity this$0 = AuthenticationActivity.this;
            if (!this$0.isBrokerRequest(this$0.getIntent())) {
                Logger.i("AuthenticationActivity:processRedirectUrl", "It is not a broker request", "");
                final Intent intent = new Intent();
                intent.putExtra("com.microsoft.aad.adal:BrowserFinalUrl", s);
                intent.putExtra("com.microsoft.aad.adal:BrowserRequestInfo", (Serializable)AuthenticationActivity.this.mAuthRequest);
                AuthenticationActivity.this.returnToCaller(2003, intent);
                webView.stopLoading();
                return;
            }
            Logger.i("AuthenticationActivity:processRedirectUrl", "It is a broker request", "");
            final AuthenticationActivity this$2 = AuthenticationActivity.this;
            this$2.displaySpinnerWithMessage(this$2.getText(this$2.getResources().getIdentifier("broker_processing", "string", AuthenticationActivity.this.getPackageName())));
            webView.stopLoading();
            final AuthenticationActivity this$3 = AuthenticationActivity.this;
            this$3.new TokenTask(this$3.mWebRequestHandler, AuthenticationActivity.this.mAuthRequest, AuthenticationActivity.this.mCallingPackage, AuthenticationActivity.this.mCallingUID).execute((Object[])new String[] { s });
        }
        
        @Override
        public void sendResponse(final int n, final Intent intent) {
            AuthenticationActivity.this.returnToCaller(n, intent);
        }
        
        @Override
        public void setPKeyAuthStatus(final boolean b) {
            AuthenticationActivity.this.mPkeyAuthRedirect = b;
        }
        
        @Override
        public void showSpinner(final boolean b) {
            AuthenticationActivity.this.displaySpinner(b);
        }
    }
    
    class TokenTask extends AsyncTask<String, String, TokenTaskResult>
    {
        private AccountManager mAccountManager;
        private int mAppCallingUID;
        private String mPackageName;
        private AuthenticationRequest mRequest;
        private IWebRequestHandler mRequestHandler;
        
        public TokenTask() {
        }
        
        public TokenTask(final IWebRequestHandler mRequestHandler, final AuthenticationRequest mRequest, final String mPackageName, final int mAppCallingUID) {
            this.mRequestHandler = mRequestHandler;
            this.mRequest = mRequest;
            this.mPackageName = mPackageName;
            this.mAppCallingUID = mAppCallingUID;
            this.mAccountManager = AccountManager.get((Context)AuthenticationActivity.this);
        }
        
        private void appendAppUIDToAccount(final Account account) throws GeneralSecurityException, IOException {
            final String userData = this.mAccountManager.getUserData(account, "account.uid.caches");
            String decrypt = "";
            if (userData != null) {
                try {
                    decrypt = AuthenticationActivity.this.mStorageHelper.decrypt(userData);
                }
                catch (GeneralSecurityException | IOException ex) {
                    final Object o2;
                    final Object o = o2;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("appIdList:");
                    sb.append(userData);
                    Logger.e("AuthenticationActivity:appendAppUIDToAccount", "appUIDList failed to decrypt", sb.toString(), ADALError.ENCRYPTION_FAILED, (Throwable)o);
                    Logger.i("AuthenticationActivity:appendAppUIDToAccount", "Reset the appUIDlist", "");
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("App UID: ");
            sb2.append(this.mAppCallingUID);
            sb2.append("appIdList:");
            sb2.append(decrypt);
            Logger.i("AuthenticationActivity:appendAppUIDToAccount", "Add calling UID. ", sb2.toString(), null);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("calling.uid.key");
            sb3.append(this.mAppCallingUID);
            if (!decrypt.contains(sb3.toString())) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("App UID: ");
                sb4.append(this.mAppCallingUID);
                Logger.i("AuthenticationActivity:appendAppUIDToAccount", "Account has new calling UID. ", sb4.toString(), null);
                final StorageHelper access$2000 = AuthenticationActivity.this.mStorageHelper;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(decrypt);
                sb5.append("calling.uid.key");
                sb5.append(this.mAppCallingUID);
                this.mAccountManager.setUserData(account, "account.uid.caches", access$2000.encrypt(sb5.toString()));
            }
        }
        
        private String getBrokerAppCacheKey(final String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            final StringBuilder sb = new StringBuilder();
            sb.append("calling.uid.key");
            sb.append(this.mAppCallingUID);
            sb.append(s);
            final String hash = StringExtensions.createHash(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Key hash is:");
            sb2.append(hash);
            sb2.append(" calling app UID:");
            sb2.append(this.mAppCallingUID);
            sb2.append(" Key is: ");
            sb2.append(s);
            Logger.v("AuthenticationActivity", "Get broker app cache key.", sb2.toString(), null);
            return hash;
        }
        
        private void saveCacheKey(String string, final Account account, final int n) {
            Logger.v("AuthenticationActivity:saveCacheKey", "Get CacheKeys for account");
            final AccountManager mAccountManager = this.mAccountManager;
            final StringBuilder sb = new StringBuilder();
            sb.append("userdata.caller.cachekeys");
            sb.append(n);
            String userData;
            if ((userData = mAccountManager.getUserData(account, sb.toString())) == null) {
                userData = "";
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("|");
            sb2.append(string);
            if (!userData.contains(sb2.toString())) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("callerUID: ");
                sb3.append(n);
                sb3.append("The key to be saved is: ");
                sb3.append(string);
                Logger.v("AuthenticationActivity:saveCacheKey", "Account does not have the cache key. Saving it to account for the caller. ", sb3.toString(), null);
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(userData);
                sb4.append("|");
                sb4.append(string);
                string = sb4.toString();
                final AccountManager mAccountManager2 = this.mAccountManager;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("userdata.caller.cachekeys");
                sb5.append(n);
                mAccountManager2.setUserData(account, sb5.toString(), string);
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("keylist:");
                sb6.append(string);
                Logger.v("AuthenticationActivity:saveCacheKey", "Cache key saved into key list for the caller.", sb6.toString(), null);
            }
        }
        
        private void setAccount(final TokenTaskResult tokenTaskResult) throws GeneralSecurityException, IOException {
            final String brokerAccountName = this.mRequest.getBrokerAccountName();
            final Account[] accountsByType = this.mAccountManager.getAccountsByType("com.microsoft.workaccount");
            if (accountsByType.length != 1) {
                tokenTaskResult.mTaskResult = null;
                tokenTaskResult.mTaskException = new AuthenticationException(ADALError.BROKER_SINGLE_USER_EXPECTED);
                return;
            }
            final Account account = accountsByType[0];
            final UserInfo userInfo = tokenTaskResult.mTaskResult.getUserInfo();
            if (userInfo != null && !StringExtensions.isNullOrBlank(userInfo.getUserId())) {
                Logger.i("AuthenticationActivity:setAccount", "Saving userinfo to account", "");
                this.mAccountManager.setUserData(account, "account.userinfo.userid", userInfo.getUserId());
                this.mAccountManager.setUserData(account, "account.userinfo.given.name", userInfo.getGivenName());
                this.mAccountManager.setUserData(account, "account.userinfo.family.name", userInfo.getFamilyName());
                this.mAccountManager.setUserData(account, "account.userinfo.identity.provider", userInfo.getIdentityProvider());
                this.mAccountManager.setUserData(account, "account.userinfo.userid.displayable", userInfo.getDisplayableId());
            }
            else {
                Logger.i("AuthenticationActivity:setAccount", "Set userinfo from account", "");
                tokenTaskResult.mTaskResult.setUserInfo(new UserInfo(brokerAccountName, brokerAccountName, "", "", brokerAccountName));
                this.mRequest.setLoginHint(brokerAccountName);
            }
            tokenTaskResult.mAccountName = brokerAccountName;
            final StringBuilder sb = new StringBuilder();
            sb.append("Package: ");
            sb.append(this.mPackageName);
            sb.append(" calling app UID:");
            sb.append(this.mAppCallingUID);
            sb.append(" Account name: ");
            sb.append(brokerAccountName);
            Logger.i("AuthenticationActivity:setAccount", "Setting account in account manager. ", sb.toString());
            final Gson gson = new Gson();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("app context:");
            sb2.append(AuthenticationActivity.this.getApplicationContext().getPackageName());
            sb2.append(" context:");
            sb2.append(AuthenticationActivity.this.getPackageName());
            sb2.append(" calling packagename:");
            sb2.append(AuthenticationActivity.this.getCallingPackage());
            Logger.i("AuthenticationActivity:setAccount", sb2.toString(), "");
            if (AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
                Logger.i("AuthenticationActivity:setAccount", "Calling app doesn't provide the secret key.", "");
            }
            final String encrypt = AuthenticationActivity.this.mStorageHelper.encrypt(gson.toJson((Object)TokenCacheItem.createRegularTokenCacheItem(this.mRequest.getAuthority(), this.mRequest.getResource(), this.mRequest.getClientId(), tokenTaskResult.mTaskResult)));
            final String cacheKeyForRTEntry = CacheKey.createCacheKeyForRTEntry(AuthenticationActivity.this.mAuthRequest.getAuthority(), AuthenticationActivity.this.mAuthRequest.getResource(), AuthenticationActivity.this.mAuthRequest.getClientId(), null);
            this.saveCacheKey(cacheKeyForRTEntry, account, this.mAppCallingUID);
            this.mAccountManager.setUserData(account, this.getBrokerAppCacheKey(cacheKeyForRTEntry), encrypt);
            if (tokenTaskResult.mTaskResult.getIsMultiResourceRefreshToken()) {
                final String encrypt2 = AuthenticationActivity.this.mStorageHelper.encrypt(gson.toJson((Object)TokenCacheItem.createMRRTTokenCacheItem(this.mRequest.getAuthority(), this.mRequest.getClientId(), tokenTaskResult.mTaskResult)));
                final String cacheKeyForMRRT = CacheKey.createCacheKeyForMRRT(AuthenticationActivity.this.mAuthRequest.getAuthority(), AuthenticationActivity.this.mAuthRequest.getClientId(), null);
                this.saveCacheKey(cacheKeyForMRRT, account, this.mAppCallingUID);
                this.mAccountManager.setUserData(account, this.getBrokerAppCacheKey(cacheKeyForMRRT), encrypt2);
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Set calling uid:");
            sb3.append(this.mAppCallingUID);
            Logger.i("AuthenticationActivity:setAccount", sb3.toString(), "");
            this.appendAppUIDToAccount(account);
        }
        
        protected TokenTaskResult doInBackground(final String... array) {
            final Oauth2 oauth2 = new Oauth2(this.mRequest, this.mRequestHandler, AuthenticationActivity.this.mJWSBuilder);
            final TokenTaskResult account = new TokenTaskResult();
            try {
                account.mTaskResult = oauth2.getToken(array[0]);
                Logger.v("AuthenticationActivity", "Process result returned from TokenTask. ", this.mRequest.getLogInfo(), null);
            }
            catch (IOException | AuthenticationException ex5) {
                final AuthenticationException ex2;
                final AuthenticationException ex = ex2;
                Logger.e("AuthenticationActivity", "Error in processing code to get a token. ", this.mRequest.getLogInfo(), ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, ex);
                account.mTaskException = ex;
            }
            if (account.mTaskResult != null && account.mTaskResult.getAccessToken() != null) {
                Logger.v("AuthenticationActivity", "Token task successfully returns access token.", this.mRequest.getLogInfo(), null);
                try {
                    this.setAccount(account);
                    return account;
                }
                catch (GeneralSecurityException | IOException ex6) {
                    final IOException ex4;
                    final IOException ex3 = ex4;
                    Logger.e("AuthenticationActivity", "Error in setting the account", this.mRequest.getLogInfo(), ADALError.BROKER_ACCOUNT_SAVE_FAILED, ex3);
                    account.mTaskException = ex3;
                }
            }
            return account;
        }
        
        protected void onPostExecute(final TokenTaskResult tokenTaskResult) {
            Logger.v("AuthenticationActivity", "Token task returns the result");
            AuthenticationActivity.this.displaySpinner(false);
            final Intent intent = new Intent();
            if (tokenTaskResult.mTaskResult == null) {
                Logger.v("AuthenticationActivity", "Token task has exception");
                AuthenticationActivity.this.returnError(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, tokenTaskResult.mTaskException.getMessage());
                return;
            }
            if (tokenTaskResult.mTaskResult.getStatus().equals(AuthenticationResult.AuthenticationStatus.Succeeded)) {
                intent.putExtra("com.microsoft.aad.adal:RequestId", AuthenticationActivity.this.mWaitingRequestId);
                intent.putExtra("account.access.token", tokenTaskResult.mTaskResult.getAccessToken());
                intent.putExtra("account.name", tokenTaskResult.mAccountName);
                if (tokenTaskResult.mTaskResult.getExpiresOn() != null) {
                    intent.putExtra("account.expiredate", tokenTaskResult.mTaskResult.getExpiresOn().getTime());
                }
                if (tokenTaskResult.mTaskResult.getTenantId() != null) {
                    intent.putExtra("account.userinfo.tenantid", tokenTaskResult.mTaskResult.getTenantId());
                }
                final UserInfo userInfo = tokenTaskResult.mTaskResult.getUserInfo();
                if (userInfo != null) {
                    intent.putExtra("account.userinfo.userid", userInfo.getUserId());
                    intent.putExtra("account.userinfo.given.name", userInfo.getGivenName());
                    intent.putExtra("account.userinfo.family.name", userInfo.getFamilyName());
                    intent.putExtra("account.userinfo.identity.provider", userInfo.getIdentityProvider());
                    intent.putExtra("account.userinfo.userid.displayable", userInfo.getDisplayableId());
                }
                AuthenticationActivity.this.returnResult(2004, intent);
                return;
            }
            AuthenticationActivity.this.returnError(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, tokenTaskResult.mTaskResult.getErrorDescription());
        }
    }
    
    class TokenTaskResult
    {
        private String mAccountName;
        private Exception mTaskException;
        private AuthenticationResult mTaskResult;
    }
}
