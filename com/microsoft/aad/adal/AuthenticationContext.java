package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;
import android.app.*;
import android.content.*;
import java.util.concurrent.atomic.*;
import android.os.*;
import android.support.v4.content.*;
import android.accounts.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class AuthenticationContext
{
    private static final SparseArray<AuthenticationRequestState> DELEGATE_MAP;
    private static final int EXCLUDE_INDEX = 8;
    private static final String REQUEST_ID = "requestId:";
    private static final String TAG = "AuthenticationContext";
    private String mAuthority;
    private BrokerProxy mBrokerProxy;
    private Context mContext;
    private boolean mExtendedLifetimeEnabled;
    private boolean mIsAuthorityValidated;
    private UUID mRequestCorrelationId;
    private ITokenCacheStore mTokenCacheStore;
    private boolean mValidateAuthority;
    
    static {
        DELEGATE_MAP = new SparseArray();
    }
    
    public AuthenticationContext(final Context context, final String s, final ITokenCacheStore tokenCacheStore) {
        this.mBrokerProxy = null;
        this.mExtendedLifetimeEnabled = false;
        this.mRequestCorrelationId = null;
        this.initialize(context, s, tokenCacheStore, true, false);
    }
    
    public AuthenticationContext(final Context context, final String s, final boolean b) {
        this.mBrokerProxy = null;
        this.mExtendedLifetimeEnabled = false;
        this.mRequestCorrelationId = null;
        PRNGFixes.apply();
        this.initialize(context, s, new DefaultTokenCacheStore(context), b, true);
    }
    
    public AuthenticationContext(final Context context, final String s, final boolean b, final ITokenCacheStore tokenCacheStore) {
        this.mBrokerProxy = null;
        this.mExtendedLifetimeEnabled = false;
        this.mRequestCorrelationId = null;
        this.initialize(context, s, tokenCacheStore, b, false);
    }
    
    private boolean checkADFSValidationRequirements(final String s) throws AuthenticationException {
        final URL url = StringExtensions.getUrl(this.mAuthority);
        if (this.mAuthority == null || url == null) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
        }
        if (UrlExtensions.isADFSAuthority(url) && this.mValidateAuthority && !this.mIsAuthorityValidated && s == null) {
            final ADALError developer_AUTHORITY_CAN_NOT_BE_VALIDED = ADALError.DEVELOPER_AUTHORITY_CAN_NOT_BE_VALIDED;
            final StringBuilder sb = new StringBuilder();
            sb.append("AD FS validation requires a loginHint be provided or an ");
            sb.append(this.getClass().getSimpleName());
            sb.append(" in which the current authority has previously been validated.");
            throw new AuthenticationException(developer_AUTHORITY_CAN_NOT_BE_VALIDED, sb.toString());
        }
        return true;
    }
    
    private boolean checkADFSValidationRequirements(final String s, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        try {
            return this.checkADFSValidationRequirements(s);
        }
        catch (AuthenticationException ex) {
            authenticationCallback.onError(ex);
            return false;
        }
    }
    
    private void checkInternetPermission() {
        if (this.mContext.getPackageManager().checkPermission("android.permission.INTERNET", this.mContext.getPackageName()) == 0) {
            return;
        }
        throw new IllegalStateException(new AuthenticationException(ADALError.DEVELOPER_INTERNET_PERMISSION_MISSING));
    }
    
    private boolean checkPreRequirements(final String s, final String s2) throws AuthenticationException {
        if (this.mContext == null) {
            throw new IllegalArgumentException("context", new AuthenticationException(ADALError.DEVELOPER_CONTEXT_IS_NOT_PROVIDED));
        }
        if (AuthenticationSettings.INSTANCE.getUseBroker()) {
            this.mBrokerProxy.verifyBrokerPermissionsAPI22AndLess();
        }
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("resource");
        }
        if (!StringExtensions.isNullOrBlank(s2)) {
            return true;
        }
        throw new IllegalArgumentException("clientId");
    }
    
    private boolean checkPreRequirements(final String s, final String s2, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (authenticationCallback != null) {
            try {
                return this.checkPreRequirements(s, s2);
            }
            catch (AuthenticationException ex) {
                authenticationCallback.onError(ex);
                return false;
            }
        }
        throw new IllegalArgumentException("callback");
    }
    
    private AcquireTokenRequest createAcquireTokenRequest(final APIEvent apiEvent) {
        return new AcquireTokenRequest(this.mContext, this, apiEvent);
    }
    
    private APIEvent createApiEvent(final Context context, final String s, final String requestId, final String apiId) {
        final APIEvent apiEvent = new APIEvent("Microsoft.ADAL.api_event", context, s);
        apiEvent.setRequestId(requestId);
        apiEvent.setAPIId(apiId);
        apiEvent.setAuthority(this.getAuthority());
        Telemetry.getInstance().startEvent(requestId, apiEvent.getEventName());
        return apiEvent;
    }
    
    private static String extractAuthority(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            final int index = s.indexOf(47, 8);
            if (index >= 0 && index != s.length() - 1) {
                final int n = index + 1;
                final int index2 = s.indexOf("/", n);
                if (index2 < 0 || index2 > n) {
                    String substring = s;
                    if (index2 >= 0) {
                        substring = s.substring(0, index2);
                    }
                    return substring;
                }
            }
        }
        throw new IllegalArgumentException("authority");
    }
    
    private String getRedirectUri(final String s) {
        String packageName = s;
        if (StringExtensions.isNullOrBlank(s)) {
            packageName = this.mContext.getApplicationContext().getPackageName();
        }
        return packageName;
    }
    
    public static String getVersionName() {
        return "1.14.0";
    }
    
    private void initialize(final Context mContext, final String s, final ITokenCacheStore mTokenCacheStore, final boolean mValidateAuthority, final boolean b) {
        if (mContext == null) {
            throw new IllegalArgumentException("appContext");
        }
        if (s == null) {
            throw new IllegalArgumentException("authority");
        }
        final BrokerProxy mBrokerProxy = new BrokerProxy(mContext);
        this.mBrokerProxy = mBrokerProxy;
        if (!b && !mBrokerProxy.canUseLocalCache(s)) {
            throw new UnsupportedOperationException("Local cache is not supported for broker usage");
        }
        this.mContext = mContext;
        this.checkInternetPermission();
        this.mAuthority = extractAuthority(s);
        this.mValidateAuthority = mValidateAuthority;
        this.mTokenCacheStore = mTokenCacheStore;
    }
    
    private void throwIfClaimsInBothExtraQpAndClaimsParameter(final String s, final String s2) {
        if (StringExtensions.isNullOrBlank(s) || StringExtensions.isNullOrBlank(s2)) {
            return;
        }
        if (!s2.contains("claims")) {
            return;
        }
        throw new IllegalArgumentException("claims cannot be sent in claims parameter and extra qp.");
    }
    
    private IWindowComponent wrapActivity(final Activity activity) {
        if (activity != null) {
            return new IWindowComponent() {
                private Activity mRefActivity = activity;
                
                @Override
                public void startActivityForResult(final Intent intent, final int n) {
                    final Activity mRefActivity = this.mRefActivity;
                    if (mRefActivity != null) {
                        mRefActivity.startActivityForResult(intent, n);
                    }
                }
            };
        }
        throw new IllegalArgumentException("activity");
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String registerNewRequest, final PromptBehavior promptBehavior, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(null, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "108");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, null, promptBehavior, null, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String registerNewRequest, final PromptBehavior promptBehavior, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(null, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "111");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, null, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String redirectUri, final String loginHint, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "100");
            apiEvent.setLoginHint(loginHint);
            redirectUri = this.getRedirectUri(redirectUri);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, PromptBehavior.Auto, null, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "115");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final String s4, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        this.throwIfClaimsInBothExtraQpAndClaimsParameter(s4, s3);
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "118");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), s4);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final Activity activity, final String s, final String s2, String redirectUri, final String loginHint, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "104");
            apiEvent.setLoginHint(loginHint);
            redirectUri = this.getRedirectUri(redirectUri);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, PromptBehavior.Auto, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(this.wrapActivity(activity), false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final IWindowComponent windowComponent, final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "116");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(windowComponent, false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final IWindowComponent windowComponent, final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final String s4, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        this.throwIfClaimsInBothExtraQpAndClaimsParameter(s4, s3);
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "119");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), s4);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(windowComponent, false, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "117");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(null, true, authenticationRequest, authenticationCallback);
        }
    }
    
    public void acquireToken(final String s, final String s2, String registerNewRequest, final String loginHint, final PromptBehavior promptBehavior, final String s3, final String s4, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        this.throwIfClaimsInBothExtraQpAndClaimsParameter(s4, s3);
        if (this.checkPreRequirements(s, s2, authenticationCallback) && this.checkADFSValidationRequirements(loginHint, authenticationCallback)) {
            final String redirectUri = this.getRedirectUri(registerNewRequest);
            registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "120");
            apiEvent.setPromptBehavior(promptBehavior.toString());
            apiEvent.setLoginHint(loginHint);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, redirectUri, loginHint, promptBehavior, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled(), s4);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(null, false, authenticationRequest, authenticationCallback);
        }
    }
    
    @Deprecated
    public void acquireTokenByRefreshToken(final String s, final String s2, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (!this.checkADFSValidationRequirements(null, authenticationCallback)) {
            return;
        }
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("Refresh token is not provided");
        }
        if (StringExtensions.isNullOrBlank(s2)) {
            throw new IllegalArgumentException("ClientId is not provided");
        }
        if (authenticationCallback != null) {
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "4");
            apiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
            apiEvent.setIsDeprecated(true);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, null, s2, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled());
            authenticationRequest.setSilent(true);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).refreshTokenWithoutCache(s, authenticationRequest, authenticationCallback);
            return;
        }
        throw new IllegalArgumentException("Callback is not provided");
    }
    
    @Deprecated
    public void acquireTokenByRefreshToken(final String s, final String s2, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (!this.checkADFSValidationRequirements(null, authenticationCallback)) {
            return;
        }
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("Refresh token is not provided");
        }
        if (StringExtensions.isNullOrBlank(s2)) {
            throw new IllegalArgumentException("ClientId is not provided");
        }
        if (authenticationCallback != null) {
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "5");
            apiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
            apiEvent.setIsDeprecated(true);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s3, s2, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled());
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            authenticationRequest.setSilent(true);
            this.createAcquireTokenRequest(apiEvent).refreshTokenWithoutCache(s, authenticationRequest, authenticationCallback);
            return;
        }
        throw new IllegalArgumentException("Callback is not provided");
    }
    
    @Deprecated
    public Future<AuthenticationResult> acquireTokenSilent(final String s, final String s2, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        final SettableFuture settableFuture = (SettableFuture)new SettableFuture<AuthenticationResult>();
        try {
            this.checkPreRequirements(s, s2);
            this.checkADFSValidationRequirements(null);
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "2");
            apiEvent.setIsDeprecated(true);
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled());
            authenticationRequest.setSilent(true);
            authenticationRequest.setPrompt(PromptBehavior.Auto);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.UniqueId);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(null, false, authenticationRequest, new AuthenticationCallback<AuthenticationResult>() {
                @Override
                public void onError(final Exception exception) {
                    apiEvent.setWasApiCallSuccessful(false, exception);
                    apiEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                    apiEvent.stopTelemetryAndFlush();
                    final AuthenticationCallback val$callback = authenticationCallback;
                    if (val$callback != null) {
                        val$callback.onError(exception);
                    }
                    settableFuture.setException(exception);
                }
                
                @Override
                public void onSuccess(final AuthenticationResult authenticationResult) {
                    apiEvent.setWasApiCallSuccessful(true, null);
                    apiEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                    apiEvent.setIdToken(authenticationResult.getIdToken());
                    apiEvent.stopTelemetryAndFlush();
                    final AuthenticationCallback val$callback = authenticationCallback;
                    if (val$callback != null) {
                        val$callback.onSuccess(authenticationResult);
                    }
                    settableFuture.set(authenticationResult);
                }
            });
            return (Future<AuthenticationResult>)settableFuture;
        }
        catch (AuthenticationException exception) {
            authenticationCallback.onError(exception);
            settableFuture.setException(exception);
            return (Future<AuthenticationResult>)settableFuture;
        }
    }
    
    public void acquireTokenSilentAsync(final String s, final String s2, final String s3, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (this.checkPreRequirements(s, s2, authenticationCallback)) {
            if (!this.checkADFSValidationRequirements(null, authenticationCallback)) {
                return;
            }
            final String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "3");
            apiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
            final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled());
            authenticationRequest.setSilent(true);
            authenticationRequest.setPrompt(PromptBehavior.Auto);
            authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.UniqueId);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            this.createAcquireTokenRequest(apiEvent).acquireToken(null, false, authenticationRequest, authenticationCallback);
        }
    }
    
    public AuthenticationResult acquireTokenSilentSync(final String s, final String s2, final String s3) throws AuthenticationException, InterruptedException {
        this.checkPreRequirements(s, s2);
        this.checkADFSValidationRequirements(null);
        final AtomicReference<AuthenticationResult> atomicReference = new AtomicReference<AuthenticationResult>();
        final AtomicReference<Exception> atomicReference2 = new AtomicReference<Exception>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String registerNewRequest = Telemetry.registerNewRequest();
        final APIEvent apiEvent = this.createApiEvent(this.mContext, s2, registerNewRequest, "1");
        apiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, s, s2, s3, this.getRequestCorrelationId(), this.getExtendedLifetimeEnabled());
        authenticationRequest.setSilent(true);
        authenticationRequest.setPrompt(PromptBehavior.Auto);
        authenticationRequest.setUserIdentifierType(AuthenticationRequest.UserIdentifierType.UniqueId);
        authenticationRequest.setTelemetryRequestId(registerNewRequest);
        final Looper myLooper = Looper.myLooper();
        if (myLooper != null && myLooper == this.mContext.getMainLooper()) {
            Logger.e("AuthenticationContext:acquireTokenSilentSync", "Sync network calls must not be invoked in main thread. This method will throw android.os.NetworkOnMainThreadException in next major release", (Throwable)new NetworkOnMainThreadException());
        }
        this.createAcquireTokenRequest(apiEvent).acquireToken(null, false, authenticationRequest, new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onError(final Exception ex) {
                atomicReference2.set(ex);
                countDownLatch.countDown();
            }
            
            @Override
            public void onSuccess(final AuthenticationResult authenticationResult) {
                atomicReference.set(authenticationResult);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        final Exception ex = atomicReference2.get();
        if (ex == null) {
            return atomicReference.get();
        }
        if (ex instanceof AuthenticationException) {
            throw (AuthenticationException)ex;
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException)ex;
        }
        if (ex.getCause() == null) {
            throw new AuthenticationException(ADALError.ERROR_SILENT_REQUEST, ex.getMessage(), ex);
        }
        if (ex.getCause() instanceof AuthenticationException) {
            throw (AuthenticationException)ex.getCause();
        }
        if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException)ex.getCause();
        }
        throw new AuthenticationException(ADALError.ERROR_SILENT_REQUEST, ex.getCause().getMessage(), ex.getCause());
    }
    
    public boolean cancelAuthenticationActivity(final int n) throws AuthenticationException {
        final AuthenticationRequestState waitingRequest = this.getWaitingRequest(n);
        if (waitingRequest == null || waitingRequest.getDelegate() == null) {
            Logger.v("AuthenticationContext:cancelAuthenticationActivity", "Current callback is empty. There is not any active authentication.");
            return true;
        }
        String format;
        if (waitingRequest.getRequest() != null) {
            format = String.format(" CorrelationId: %s", waitingRequest.getRequest().getCorrelationId().toString());
        }
        else {
            format = "No correlation id associated with waiting request";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Current callback is not empty. There is an active authentication Activity.");
        sb.append(format);
        Logger.v("AuthenticationContext:cancelAuthenticationActivity", sb.toString());
        final Intent intent = new Intent("com.microsoft.aad.adal:BrowserCancel");
        intent.putExtras(new Bundle());
        intent.putExtra("com.microsoft.aad.adal:RequestId", n);
        final boolean sendBroadcast = LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        if (sendBroadcast) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cancel broadcast message was successful.");
            sb2.append(format);
            Logger.v("AuthenticationContext:cancelAuthenticationActivity", sb2.toString());
            waitingRequest.setCancelled(true);
            waitingRequest.getDelegate().onError(new AuthenticationCancelError("Cancel broadcast message was successful."));
            return sendBroadcast;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Cancel broadcast message was not successful.");
        sb3.append(format);
        Logger.w("AuthenticationContext:cancelAuthenticationActivity", sb3.toString(), "", ADALError.BROADCAST_CANCEL_NOT_SUCCESSFUL);
        return sendBroadcast;
    }
    
    void deserialize(final String s) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(s)) {
            throw new IllegalArgumentException("serializedBlob");
        }
        if (this.mBrokerProxy.canSwitchToBroker(this.mAuthority) == BrokerProxy.SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            final TokenCacheItem deserialize = SSOStateSerializer.deserialize(s);
            this.getCache().setItem(CacheKey.createCacheKey(deserialize), deserialize);
            return;
        }
        throw new UsageAuthenticationException(ADALError.FAIL_TO_IMPORT, "Failed to import the serialized blob because broker is enabled.");
    }
    
    public String getAuthority() {
        return this.mAuthority;
    }
    
    public String getBrokerUser() {
        final BrokerProxy mBrokerProxy = this.mBrokerProxy;
        if (mBrokerProxy != null) {
            return mBrokerProxy.getCurrentUser();
        }
        return null;
    }
    
    public UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException {
        final BrokerProxy mBrokerProxy = this.mBrokerProxy;
        if (mBrokerProxy != null) {
            return mBrokerProxy.getBrokerUsers();
        }
        return null;
    }
    
    public ITokenCacheStore getCache() {
        return this.mTokenCacheStore;
    }
    
    String getCorrelationInfoFromWaitingRequest(final AuthenticationRequestState authenticationRequestState) {
        UUID uuid = this.getRequestCorrelationId();
        if (authenticationRequestState.getRequest() != null) {
            uuid = authenticationRequestState.getRequest().getCorrelationId();
        }
        return String.format(" CorrelationId: %s", uuid.toString());
    }
    
    public boolean getExtendedLifetimeEnabled() {
        return this.mExtendedLifetimeEnabled;
    }
    
    boolean getIsAuthorityValidated() {
        return this.mIsAuthorityValidated;
    }
    
    public String getRedirectUriForBroker() {
        final PackageHelper packageHelper = new PackageHelper(this.mContext);
        final String packageName = this.mContext.getPackageName();
        final String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(packageName);
        final String brokerRedirectUrl = PackageHelper.getBrokerRedirectUrl(packageName, currentSignatureForPackage);
        final StringBuilder sb = new StringBuilder();
        sb.append("Broker redirectUri:");
        sb.append(brokerRedirectUrl);
        sb.append(" packagename:");
        sb.append(packageName);
        sb.append(" signatureDigest:");
        sb.append(currentSignatureForPackage);
        Logger.v("AuthenticationContext:getRedirectUriForBroker", "Get expected redirect Uri. ", sb.toString(), null);
        return brokerRedirectUrl;
    }
    
    public UUID getRequestCorrelationId() {
        UUID uuid;
        if ((uuid = this.mRequestCorrelationId) == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }
    
    public boolean getValidateAuthority() {
        return this.mValidateAuthority;
    }
    
    AuthenticationRequestState getWaitingRequest(final int n) throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Get waiting request. requestId:");
        sb.append(n);
        Logger.v("AuthenticationContext:getWaitingRequest", sb.toString());
        Object o = AuthenticationContext.DELEGATE_MAP;
        synchronized (o) {
            final AuthenticationRequestState authenticationRequestState = (AuthenticationRequestState)AuthenticationContext.DELEGATE_MAP.get(n);
            // monitorexit(o)
            if (authenticationRequestState != null) {
                return authenticationRequestState;
            }
            o = new StringBuilder();
            ((StringBuilder)o).append("Request callback is not available. requestId:");
            ((StringBuilder)o).append(n);
            Logger.e("AuthenticationContext:getWaitingRequest", ((StringBuilder)o).toString(), null, ADALError.CALLBACK_IS_NOT_FOUND);
            o = ADALError.CALLBACK_IS_NOT_FOUND;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Request callback is not available for requestId:");
            sb2.append(n);
            throw new AuthenticationException((ADALError)o, sb2.toString());
        }
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 1001) {
            if (intent == null) {
                Logger.e("AuthenticationContext:onActivityResult", "onActivityResult BROWSER_FLOW data is null.", "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
                return;
            }
            final int int1 = intent.getExtras().getInt("com.microsoft.aad.adal:RequestId");
            synchronized (AuthenticationContext.DELEGATE_MAP) {
                final AuthenticationRequestState authenticationRequestState = (AuthenticationRequestState)AuthenticationContext.DELEGATE_MAP.get(int1);
                // monitorexit(AuthenticationContext.DELEGATE_MAP)
                if (authenticationRequestState != null) {
                    new AcquireTokenRequest(this.mContext, this, authenticationRequestState.getAPIEvent()).onActivityResult(n, n2, intent);
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("onActivityResult did not find the waiting request. requestId:");
                sb.append(int1);
                Logger.e("AuthenticationContext:onActivityResult", sb.toString(), null, ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
            }
        }
    }
    
    void putWaitingRequest(final int n, final AuthenticationRequestState authenticationRequestState) {
        if (authenticationRequestState == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Put waiting request. requestId:");
        sb.append(n);
        sb.append(" ");
        sb.append(this.getCorrelationInfoFromWaitingRequest(authenticationRequestState));
        Logger.v("AuthenticationContext", sb.toString());
        synchronized (AuthenticationContext.DELEGATE_MAP) {
            AuthenticationContext.DELEGATE_MAP.put(n, (Object)authenticationRequestState);
        }
    }
    
    void removeWaitingRequest(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Remove waiting request. requestId:");
        sb.append(n);
        Logger.v("AuthenticationContext", sb.toString());
        synchronized (AuthenticationContext.DELEGATE_MAP) {
            AuthenticationContext.DELEGATE_MAP.remove(n);
        }
    }
    
    String serialize(final String s) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(s)) {
            if (this.mBrokerProxy.canSwitchToBroker(this.mAuthority) == BrokerProxy.SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
                final TokenCacheAccessor tokenCacheAccessor = new TokenCacheAccessor(this.mTokenCacheStore, this.getAuthority(), Telemetry.registerNewRequest());
                try {
                    final TokenCacheItem frtItem = tokenCacheAccessor.getFRTItem("1", s);
                    if (frtItem == null) {
                        Logger.i("AuthenticationContext:serialize", "Cannot find the family token cache item for this userID", "");
                        throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "Failed to export the FID because no family token cache item is found.");
                    }
                    if (!StringExtensions.isNullOrBlank(frtItem.getFamilyClientId())) {
                        return SSOStateSerializer.serialize(frtItem);
                    }
                    throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "tokenItem does not contain family refresh token");
                }
                catch (MalformedURLException ex) {
                    throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
                }
            }
            throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "Failed to export the family refresh token cache item because broker is enabled.");
        }
        throw new IllegalArgumentException("uniqueUserId");
    }
    
    public void setExtendedLifetimeEnabled(final boolean mExtendedLifetimeEnabled) {
        this.mExtendedLifetimeEnabled = mExtendedLifetimeEnabled;
    }
    
    void setIsAuthorityValidated(final boolean mIsAuthorityValidated) {
        this.mIsAuthorityValidated = mIsAuthorityValidated;
    }
    
    public void setRequestCorrelationId(final UUID mRequestCorrelationId) {
        Logger.setCorrelationId(this.mRequestCorrelationId = mRequestCorrelationId);
    }
    
    static final class SettableFuture<V> extends FutureTask<V>
    {
        SettableFuture() {
            super(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return null;
                }
            });
        }
        
        public void set(final V v) {
            super.set(v);
        }
        
        public void setException(final Throwable exception) {
            super.setException(exception);
        }
    }
}
