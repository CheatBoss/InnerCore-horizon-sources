package com.microsoft.aad.adal;

import java.util.concurrent.*;
import java.net.*;
import android.content.*;
import java.util.*;
import android.util.*;
import android.os.*;
import java.io.*;

class AcquireTokenRequest
{
    private static final String TAG;
    private static final ExecutorService THREAD_EXECUTOR;
    private static Handler sHandler;
    private APIEvent mAPIEvent;
    private final AuthenticationContext mAuthContext;
    private final IBrokerProxy mBrokerProxy;
    private final Context mContext;
    private Discovery mDiscovery;
    private TokenCacheAccessor mTokenCacheAccessor;
    
    static {
        TAG = AcquireTokenRequest.class.getSimpleName();
        THREAD_EXECUTOR = Executors.newSingleThreadExecutor();
        AcquireTokenRequest.sHandler = null;
    }
    
    AcquireTokenRequest(final Context mContext, final AuthenticationContext mAuthContext, final APIEvent mapiEvent) {
        this.mContext = mContext;
        this.mAuthContext = mAuthContext;
        this.mDiscovery = new Discovery(this.mContext);
        if (mAuthContext.getCache() != null && mapiEvent != null) {
            this.mTokenCacheAccessor = new TokenCacheAccessor(mAuthContext.getCache(), mAuthContext.getAuthority(), mapiEvent.getTelemetryRequestId());
        }
        this.mBrokerProxy = new BrokerProxy(mContext);
        this.mAPIEvent = mapiEvent;
    }
    
    private void acquireTokenInteractiveFlow(final CallbackHandler callbackHandler, final IWindowComponent windowComponent, final boolean b, final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (windowComponent == null && !b) {
            final ADALError auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
            final StringBuilder sb = new StringBuilder();
            sb.append(authenticationRequest.getLogInfo());
            sb.append(" Cannot launch webview, acitivity is null.");
            throw new AuthenticationException(auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED, sb.toString());
        }
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        final int hashCode = callbackHandler.getCallback().hashCode();
        authenticationRequest.setRequestId(hashCode);
        this.mAuthContext.putWaitingRequest(hashCode, new AuthenticationRequestState(hashCode, authenticationRequest, callbackHandler.getCallback(), this.mAPIEvent));
        final BrokerProxy.SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
        final BrokerProxy.SwitchToBroker cannot_SWITCH_TO_BROKER = BrokerProxy.SwitchToBroker.CANNOT_SWITCH_TO_BROKER;
        final AuthenticationDialog authenticationDialog = null;
        if (canSwitchToBroker == cannot_SWITCH_TO_BROKER || !this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(AcquireTokenRequest.TAG);
            sb2.append(":acquireTokenInteractiveFlow");
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(" Callback is: ");
            sb3.append(callbackHandler.getCallback().hashCode());
            Logger.v(string, "Starting Authentication Activity for embedded flow. ", sb3.toString(), null);
            final AcquireTokenInteractiveRequest acquireTokenInteractiveRequest = new AcquireTokenInteractiveRequest(this.mContext, authenticationRequest, this.mTokenCacheAccessor);
            AuthenticationDialog authenticationDialog2 = authenticationDialog;
            if (b) {
                authenticationDialog2 = new AuthenticationDialog(this.getHandler(), this.mContext, this, authenticationRequest);
            }
            acquireTokenInteractiveRequest.acquireToken(windowComponent, authenticationDialog2);
            return;
        }
        if (canSwitchToBroker != BrokerProxy.SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(AcquireTokenRequest.TAG);
            sb4.append(":acquireTokenInteractiveFlow");
            final String string2 = sb4.toString();
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("");
            sb5.append(callbackHandler.getCallback().hashCode());
            Logger.v(string2, "Launch activity for interactive authentication via broker with callback. ", sb5.toString(), null);
            new AcquireTokenWithBrokerRequest(authenticationRequest, this.mBrokerProxy).acquireTokenWithBrokerInteractively(windowComponent);
            return;
        }
        throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS");
    }
    
    private AuthenticationResult acquireTokenSilentFlow(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final AuthenticationResult tryAcquireTokenSilentLocally = this.tryAcquireTokenSilentLocally(authenticationRequest);
        if (this.isAccessTokenReturned(tryAcquireTokenSilentLocally)) {
            return tryAcquireTokenSilentLocally;
        }
        final BrokerProxy.SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
        if (canSwitchToBroker == BrokerProxy.SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            return tryAcquireTokenSilentLocally;
        }
        if (!this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId())) {
            return tryAcquireTokenSilentLocally;
        }
        if (canSwitchToBroker != BrokerProxy.SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenRequest.TAG);
            sb.append(":acquireTokenSilentFlow");
            Logger.d(sb.toString(), "Cannot get AT from local cache, switch to Broker for auth, clear tokens from local cache for the user.");
            this.removeTokensForUser(authenticationRequest);
            return this.tryAcquireTokenSilentWithBroker(authenticationRequest);
        }
        throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS");
    }
    
    private Handler getHandler() {
        synchronized (this) {
            if (AcquireTokenRequest.sHandler == null) {
                final HandlerThread handlerThread = new HandlerThread("AcquireTokenRequestHandlerThread");
                handlerThread.start();
                AcquireTokenRequest.sHandler = new Handler(handlerThread.getLooper());
            }
            return AcquireTokenRequest.sHandler;
        }
    }
    
    private boolean isAccessTokenReturned(final AuthenticationResult authenticationResult) {
        return authenticationResult != null && !StringExtensions.isNullOrBlank(authenticationResult.getAccessToken());
    }
    
    private void performAcquireTokenRequest(final CallbackHandler callbackHandler, final IWindowComponent windowComponent, final boolean b, final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final AuthenticationResult tryAcquireTokenSilent = this.tryAcquireTokenSilent(authenticationRequest);
        if (this.isAccessTokenReturned(tryAcquireTokenSilent)) {
            this.mAPIEvent.setWasApiCallSuccessful(true, null);
            this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
            this.mAPIEvent.setIdToken(tryAcquireTokenSilent.getIdToken());
            this.mAPIEvent.stopTelemetryAndFlush();
            callbackHandler.onSuccess(tryAcquireTokenSilent);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenRequest.TAG);
        sb.append(":performAcquireTokenRequest");
        Logger.d(sb.toString(), "Trying to acquire token interactively.");
        this.acquireTokenInteractiveFlow(callbackHandler, windowComponent, b, authenticationRequest);
    }
    
    private void performAuthorityValidation(final AuthenticationRequest authenticationRequest, final URL url) throws AuthenticationException {
        Telemetry.getInstance().startEvent(authenticationRequest.getTelemetryRequestId(), "Microsoft.ADAL.authority_validation");
        final APIEvent apiEvent = new APIEvent("Microsoft.ADAL.authority_validation");
        apiEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
        apiEvent.setRequestId(authenticationRequest.getTelemetryRequestId());
        Label_0152: {
            if (!this.mAuthContext.getValidateAuthority()) {
                break Label_0152;
            }
            try {
                try {
                    this.validateAuthority(url, authenticationRequest.getUpnSuffix(), authenticationRequest.isSilent(), authenticationRequest.getCorrelationId());
                    apiEvent.setValidationStatus("yes");
                }
                finally {}
            }
            catch (AuthenticationException ex) {
                String validationStatus;
                if (ex.getCode() != null && (ex.getCode().equals(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE) || ex.getCode().equals(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION))) {
                    validationStatus = "not_done";
                }
                else {
                    validationStatus = "no";
                }
                apiEvent.setValidationStatus(validationStatus);
                throw ex;
                InstanceDiscoveryMetadata cachedInstanceDiscoveryMetadata;
                StringBuilder sb;
                Label_0232_Outer:Block_9_Outer:
                while (true) {
                    return;
                    Label_0239: {
                        while (true) {
                            while (true) {
                                apiEvent.setValidationStatus("not_done");
                                break Label_0239;
                                try {
                                    this.mDiscovery.validateAuthority(url);
                                }
                                catch (AuthenticationException ex2) {
                                    AuthorityValidationMetadataCache.updateInstanceDiscoveryMap(url.getHost(), new InstanceDiscoveryMetadata(false));
                                    sb = new StringBuilder();
                                    sb.append(AcquireTokenRequest.TAG);
                                    sb.append(":performAuthorityValidation");
                                    Logger.v(sb.toString(), "Fail to get authority validation metadata back. Ignore the failure since authority validation is turned off.");
                                }
                                continue Block_9_Outer;
                            }
                            continue;
                        }
                        Telemetry.getInstance().stopEvent(authenticationRequest.getTelemetryRequestId(), apiEvent, "Microsoft.ADAL.authority_validation");
                        throw url;
                        Label_0277: {
                            return;
                        }
                        Label_0270:
                        this.updatePreferredNetworkLocation(url, authenticationRequest, cachedInstanceDiscoveryMetadata);
                        return;
                    }
                    Telemetry.getInstance().stopEvent(authenticationRequest.getTelemetryRequestId(), apiEvent, "Microsoft.ADAL.authority_validation");
                    cachedInstanceDiscoveryMetadata = AuthorityValidationMetadataCache.getCachedInstanceDiscoveryMetadata(url);
                    continue Label_0232_Outer;
                }
            }
            // iftrue(Label_0270:, cachedInstanceDiscoveryMetadata.isValidated())
            // iftrue(Label_0232:, UrlExtensions.isADFSAuthority(url) || AuthorityValidationMetadataCache.containsAuthorityHost(url))
            // iftrue(Label_0277:, cachedInstanceDiscoveryMetadata == null)
        }
    }
    
    private void removeTokensForUser(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (this.mTokenCacheAccessor == null) {
            return;
        }
        String s;
        if (!StringExtensions.isNullOrBlank(authenticationRequest.getUserId())) {
            s = authenticationRequest.getUserId();
        }
        else {
            s = authenticationRequest.getLoginHint();
        }
        try {
            final TokenCacheItem frtItem = this.mTokenCacheAccessor.getFRTItem("1", s);
            if (frtItem != null) {
                this.mTokenCacheAccessor.removeTokenCacheItem(frtItem, authenticationRequest.getResource());
            }
            try {
                final TokenCacheItem mrrtItem = this.mTokenCacheAccessor.getMRRTItem(authenticationRequest.getClientId(), s);
                final TokenCacheItem regularRefreshTokenCacheItem = this.mTokenCacheAccessor.getRegularRefreshTokenCacheItem(authenticationRequest.getResource(), authenticationRequest.getClientId(), s);
                if (mrrtItem != null) {
                    this.mTokenCacheAccessor.removeTokenCacheItem(mrrtItem, authenticationRequest.getResource());
                    return;
                }
                if (regularRefreshTokenCacheItem != null) {
                    this.mTokenCacheAccessor.removeTokenCacheItem(regularRefreshTokenCacheItem, authenticationRequest.getResource());
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenRequest.TAG);
                sb.append(":removeTokensForUser");
                Logger.v(sb.toString(), "No token items need to be deleted for the user.");
            }
            catch (MalformedURLException ex) {
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex.getMessage(), ex);
            }
        }
        catch (MalformedURLException ex2) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex2.getMessage(), ex2);
        }
    }
    
    private boolean shouldTrySilentFlow(final AuthenticationRequest authenticationRequest) {
        return (!Utility.isClaimsChallengePresent(authenticationRequest) && authenticationRequest.getPrompt() == PromptBehavior.Auto) || authenticationRequest.isSilent();
    }
    
    private AuthenticationResult tryAcquireTokenSilent(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        AuthenticationResult authenticationResult;
        if (this.shouldTrySilentFlow(authenticationRequest)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenRequest.TAG);
            sb.append(":tryAcquireTokenSilent");
            Logger.v(sb.toString(), "Try to acquire token silently, return valid AT or use RT in the cache.");
            final AuthenticationResult acquireTokenSilentFlow = this.acquireTokenSilentFlow(authenticationRequest);
            final boolean accessTokenReturned = this.isAccessTokenReturned(acquireTokenSilentFlow);
            if (!accessTokenReturned && authenticationRequest.isSilent()) {
                String string;
                if (acquireTokenSilentFlow == null) {
                    string = "No result returned from acquireTokenSilent";
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(" ErrorCode:");
                    sb2.append(acquireTokenSilentFlow.getErrorCode());
                    string = sb2.toString();
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(AcquireTokenRequest.TAG);
                sb3.append(":tryAcquireTokenSilent");
                final String string2 = sb3.toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Prompt is not allowed and failed to get token. ");
                sb4.append(string);
                Logger.e(string2, sb4.toString(), authenticationRequest.getLogInfo(), ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
                final ADALError auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(authenticationRequest.getLogInfo());
                sb5.append(" ");
                sb5.append(string);
                final AuthenticationException ex = new AuthenticationException(auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED, sb5.toString());
                ex.setHttpResponse(acquireTokenSilentFlow);
                throw ex;
            }
            authenticationResult = acquireTokenSilentFlow;
            if (accessTokenReturned) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(AcquireTokenRequest.TAG);
                sb6.append(":tryAcquireTokenSilent");
                Logger.v(sb6.toString(), "Token is successfully returned from silent flow. ");
                return acquireTokenSilentFlow;
            }
        }
        else {
            authenticationResult = null;
        }
        return authenticationResult;
    }
    
    private AuthenticationResult tryAcquireTokenSilentLocally(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenRequest.TAG);
        sb.append(":tryAcquireTokenSilentLocally");
        Logger.v(sb.toString(), "Try to silently get token from local cache.");
        return new AcquireTokenSilentHandler(this.mContext, authenticationRequest, this.mTokenCacheAccessor).getAccessToken();
    }
    
    private AuthenticationResult tryAcquireTokenSilentWithBroker(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        return new AcquireTokenWithBrokerRequest(authenticationRequest, this.mBrokerProxy).acquireTokenWithBrokerSilent();
    }
    
    private void updatePreferredNetworkLocation(final URL url, final AuthenticationRequest authenticationRequest, final InstanceDiscoveryMetadata instanceDiscoveryMetadata) throws AuthenticationException {
        if (instanceDiscoveryMetadata != null) {
            if (!instanceDiscoveryMetadata.isValidated()) {
                return;
            }
            if (instanceDiscoveryMetadata.getPreferredNetwork() != null && !url.getHost().equalsIgnoreCase(instanceDiscoveryMetadata.getPreferredNetwork())) {
                try {
                    authenticationRequest.setAuthority(Utility.constructAuthorityUrl(url, instanceDiscoveryMetadata.getPreferredNetwork()).toString());
                }
                catch (MalformedURLException ex) {
                    Logger.i(AcquireTokenRequest.TAG, "preferred network is invalid", "use exactly the same authority url that is passed");
                }
            }
        }
    }
    
    private void validateAcquireTokenRequest(final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final URL url = StringExtensions.getUrl(authenticationRequest.getAuthority());
        if (url == null) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
        }
        this.performAuthorityValidation(authenticationRequest, url);
        final BrokerProxy.SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
        if (canSwitchToBroker == BrokerProxy.SwitchToBroker.CANNOT_SWITCH_TO_BROKER || !this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId()) || authenticationRequest.isSilent()) {
            return;
        }
        if (canSwitchToBroker != BrokerProxy.SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
            this.verifyBrokerRedirectUri(authenticationRequest);
            return;
        }
        throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS.");
    }
    
    private void validateAuthority(final URL url, final String s, final boolean b, final UUID correlationId) throws AuthenticationException {
        final boolean adfsAuthority = UrlExtensions.isADFSAuthority(url);
        if (!AuthorityValidationMetadataCache.isAuthorityValidated(url)) {
            if (adfsAuthority && this.mAuthContext.getIsAuthorityValidated()) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(AcquireTokenRequest.TAG);
            sb.append(":validateAuthority");
            Logger.v(sb.toString(), "Start validating authority");
            this.mDiscovery.setCorrelationId(correlationId);
            Discovery.verifyAuthorityValidInstance(url);
            if (!b && adfsAuthority && s != null) {
                this.mDiscovery.validateAuthorityADFS(url, s);
            }
            else {
                if (b && UrlExtensions.isADFSAuthority(url)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(AcquireTokenRequest.TAG);
                    sb2.append(":validateAuthority");
                    Logger.v(sb2.toString(), "Silent request. Skipping AD FS authority validation");
                }
                this.mDiscovery.validateAuthority(url);
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(AcquireTokenRequest.TAG);
            sb3.append(":validateAuthority");
            Logger.v(sb3.toString(), "The passed in authority is valid.");
            this.mAuthContext.setIsAuthorityValidated(true);
        }
    }
    
    private void verifyBrokerRedirectUri(final AuthenticationRequest authenticationRequest) throws UsageAuthenticationException {
        final String redirectUri = authenticationRequest.getRedirectUri();
        final String redirectUriForBroker = this.mAuthContext.getRedirectUriForBroker();
        if (!StringExtensions.isNullOrBlank(redirectUri)) {
            if (redirectUri.startsWith("msauth://")) {
                final PackageHelper packageHelper = new PackageHelper(this.mContext);
                try {
                    final String encode = URLEncoder.encode(this.mContext.getPackageName(), "UTF_8");
                    final String encode2 = URLEncoder.encode(packageHelper.getCurrentSignatureForPackage(this.mContext.getPackageName()), "UTF_8");
                    final StringBuilder sb = new StringBuilder();
                    sb.append("msauth://");
                    sb.append(encode);
                    sb.append("/");
                    if (!redirectUri.startsWith(sb.toString())) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("This apps package name is: ");
                        sb2.append(encode);
                        sb2.append(" so the redirect uri is expected to be: ");
                        sb2.append(redirectUriForBroker);
                        final String string = sb2.toString();
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(AcquireTokenRequest.TAG);
                        sb3.append(":verifyBrokerRedirectUri");
                        Logger.e(sb3.toString(), "The base64 url encoded package name component of the redirect uri does not match the expected value. ", string, ADALError.DEVELOPER_REDIRECTURI_INVALID);
                        throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The base64 url encoded package name component of the redirect uri does not match the expected value. ");
                    }
                    if (redirectUri.equalsIgnoreCase(redirectUriForBroker)) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(AcquireTokenRequest.TAG);
                        sb4.append(":verifyBrokerRedirectUri");
                        Logger.v(sb4.toString(), "The broker redirect URI is valid.");
                        return;
                    }
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("This apps signature is: ");
                    sb5.append(encode2);
                    sb5.append(" so the redirect uri is expected to be: ");
                    sb5.append(redirectUriForBroker);
                    final String string2 = sb5.toString();
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(AcquireTokenRequest.TAG);
                    sb6.append(":verifyBrokerRedirectUri");
                    Logger.e(sb6.toString(), "The base64 url encoded signature component of the redirect uri does not match the expected value. ", string2, ADALError.DEVELOPER_REDIRECTURI_INVALID);
                    throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The base64 url encoded signature component of the redirect uri does not match the expected value.");
                }
                catch (UnsupportedEncodingException ex) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append(AcquireTokenRequest.TAG);
                    sb7.append(":verifyBrokerRedirectUri");
                    Logger.e(sb7.toString(), ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), ex.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, ex);
                    throw new UsageAuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, "The verifying BrokerRedirectUri process failed because the base64 url encoding is not supported.", ex);
                }
            }
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(" The valid broker redirect URI prefix: msauth so the redirect uri is expected to be: ");
            sb8.append(redirectUriForBroker);
            final String string3 = sb8.toString();
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(AcquireTokenRequest.TAG);
            sb9.append(":verifyBrokerRedirectUri");
            Logger.e(sb9.toString(), "The prefix of the redirect uri does not match the expected value. ", string3, ADALError.DEVELOPER_REDIRECTURI_INVALID);
            throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The prefix of the redirect uri does not match the expected value.");
        }
        final StringBuilder sb10 = new StringBuilder();
        sb10.append(AcquireTokenRequest.TAG);
        sb10.append(":verifyBrokerRedirectUri");
        final String string4 = sb10.toString();
        final StringBuilder sb11 = new StringBuilder();
        sb11.append("The redirect uri is expected to be:");
        sb11.append(redirectUriForBroker);
        Logger.e(string4, "The redirectUri is null or blank. ", sb11.toString(), ADALError.DEVELOPER_REDIRECTURI_INVALID);
        throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The redirectUri is null or blank.");
    }
    
    private void waitingRequestOnError(final CallbackHandler callbackHandler, final AuthenticationRequestState authenticationRequestState, final int n, final AuthenticationException ex) {
        if (authenticationRequestState != null) {
            try {
                if (authenticationRequestState.getDelegate() != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(AcquireTokenRequest.TAG);
                    sb.append(":waitingRequestOnError");
                    final String string = sb.toString();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Sending error to callback");
                    sb2.append(this.mAuthContext.getCorrelationInfoFromWaitingRequest(authenticationRequestState));
                    Logger.v(string, sb2.toString());
                    authenticationRequestState.getAPIEvent().setWasApiCallSuccessful(false, ex);
                    authenticationRequestState.getAPIEvent().setCorrelationId(authenticationRequestState.getRequest().getCorrelationId().toString());
                    authenticationRequestState.getAPIEvent().stopTelemetryAndFlush();
                    if (callbackHandler != null) {
                        callbackHandler.onError(ex);
                    }
                    else {
                        authenticationRequestState.getDelegate().onError(ex);
                    }
                }
            }
            finally {
                if (ex != null) {
                    this.mAuthContext.removeWaitingRequest(n);
                }
            }
        }
        if (ex != null) {
            this.mAuthContext.removeWaitingRequest(n);
        }
    }
    
    private void waitingRequestOnError(final AuthenticationRequestState authenticationRequestState, final int n, final AuthenticationException ex) {
        this.waitingRequestOnError(null, authenticationRequestState, n, ex);
    }
    
    void acquireToken(final IWindowComponent windowComponent, final boolean b, final AuthenticationRequest authenticationRequest, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        final CallbackHandler callbackHandler = new CallbackHandler(this.getHandler(), authenticationCallback);
        Logger.setCorrelationId(authenticationRequest.getCorrelationId());
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenRequest.TAG);
        sb.append(":acquireToken");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Sending async task from thread:");
        sb2.append(Process.myTid());
        Logger.v(string, sb2.toString());
        AcquireTokenRequest.THREAD_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenRequest.TAG);
                sb.append(":acquireToken");
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Running task in thread:");
                sb2.append(Process.myTid());
                Logger.v(string, sb2.toString());
                try {
                    AcquireTokenRequest.this.validateAcquireTokenRequest(authenticationRequest);
                    AcquireTokenRequest.this.performAcquireTokenRequest(callbackHandler, windowComponent, b, authenticationRequest);
                }
                catch (AuthenticationException ex) {
                    AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(false, ex);
                    AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                    AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
                    callbackHandler.onError(ex);
                }
            }
        });
    }
    
    void onActivityResult(int int1, final int n, final Intent intent) {
        if (int1 == 1001) {
            this.getHandler();
            if (intent == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenRequest.TAG);
                sb.append(":onActivityResult");
                Logger.e(sb.toString(), "BROWSER_FLOW data is null.", "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
                return;
            }
            final Bundle extras = intent.getExtras();
            int1 = extras.getInt("com.microsoft.aad.adal:RequestId");
            try {
                final AuthenticationRequestState waitingRequest = this.mAuthContext.getWaitingRequest(int1);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(AcquireTokenRequest.TAG);
                sb2.append(":onActivityResult");
                final String string = sb2.toString();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Waiting request found. RequestId:");
                sb3.append(int1);
                Logger.v(string, sb3.toString());
                final String correlationInfoFromWaitingRequest = this.mAuthContext.getCorrelationInfoFromWaitingRequest(waitingRequest);
                if (n == 2004) {
                    final String stringExtra = intent.getStringExtra("account.access.token");
                    this.mBrokerProxy.saveAccount(intent.getStringExtra("account.name"));
                    final Date date = new Date(intent.getLongExtra("account.expiredate", 0L));
                    final String stringExtra2 = intent.getStringExtra("account.idtoken");
                    final String stringExtra3 = intent.getStringExtra("account.userinfo.tenantid");
                    final UserInfo userInfoFromBrokerResult = UserInfo.getUserInfoFromBrokerResult(intent.getExtras());
                    final String stringExtra4 = intent.getStringExtra("cliteleminfo.server_error");
                    final String stringExtra5 = intent.getStringExtra("cliteleminfo.server_suberror");
                    final String stringExtra6 = intent.getStringExtra("cliteleminfo.rt_age");
                    final String stringExtra7 = intent.getStringExtra("cliteleminfo.spe_ring");
                    final AuthenticationResult authenticationResult = new AuthenticationResult(stringExtra, null, date, false, userInfoFromBrokerResult, stringExtra3, stringExtra2, null);
                    authenticationResult.setAuthority(intent.getStringExtra("account.authority"));
                    final TelemetryUtils.CliTelemInfo cliTelemInfo = new TelemetryUtils.CliTelemInfo();
                    cliTelemInfo.setServerErrorCode(stringExtra4);
                    cliTelemInfo.setServerSubErrorCode(stringExtra5);
                    cliTelemInfo.setRefreshTokenAge(stringExtra6);
                    cliTelemInfo.setSpeRing(stringExtra7);
                    authenticationResult.setCliTelemInfo(cliTelemInfo);
                    if (authenticationResult.getAccessToken() != null) {
                        waitingRequest.getAPIEvent().setWasApiCallSuccessful(true, null);
                        waitingRequest.getAPIEvent().setCorrelationId(waitingRequest.getRequest().getCorrelationId().toString());
                        waitingRequest.getAPIEvent().setIdToken(authenticationResult.getIdToken());
                        waitingRequest.getAPIEvent().setServerErrorCode(cliTelemInfo.getServerErrorCode());
                        waitingRequest.getAPIEvent().setServerSubErrorCode(cliTelemInfo.getServerSubErrorCode());
                        waitingRequest.getAPIEvent().setRefreshTokenAge(cliTelemInfo.getRefreshTokenAge());
                        waitingRequest.getAPIEvent().setSpeRing(cliTelemInfo.getSpeRing());
                        waitingRequest.getAPIEvent().stopTelemetryAndFlush();
                        waitingRequest.getDelegate().onSuccess(authenticationResult);
                    }
                }
                else {
                    if (n == 2001) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(AcquireTokenRequest.TAG);
                        sb4.append(":onActivityResult");
                        final String string2 = sb4.toString();
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("User cancelled the flow. RequestId:");
                        sb5.append(int1);
                        sb5.append(" ");
                        sb5.append(correlationInfoFromWaitingRequest);
                        Logger.v(string2, sb5.toString());
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("User cancelled the flow RequestId:");
                        sb6.append(int1);
                        sb6.append(correlationInfoFromWaitingRequest);
                        this.waitingRequestOnError(waitingRequest, int1, new AuthenticationCancelError(sb6.toString()));
                        return;
                    }
                    if (n == 2006) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append(AcquireTokenRequest.TAG);
                        sb7.append(":onActivityResult");
                        Logger.v(sb7.toString(), "Device needs to have broker installed, we expect the apps to call usback when the broker is installed");
                        this.waitingRequestOnError(waitingRequest, int1, new AuthenticationException(ADALError.BROKER_APP_INSTALLATION_STARTED));
                        return;
                    }
                    if (n == 2005) {
                        final Serializable serializable = extras.getSerializable("com.microsoft.aad.adal:AuthenticationException");
                        if (serializable != null && serializable instanceof AuthenticationException) {
                            final AuthenticationException ex = (AuthenticationException)serializable;
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append(AcquireTokenRequest.TAG);
                            sb8.append(":onActivityResult");
                            Logger.w(sb8.toString(), "Webview returned exception.", ex.getMessage(), ADALError.WEBVIEW_RETURNED_AUTHENTICATION_EXCEPTION);
                            this.waitingRequestOnError(waitingRequest, int1, ex);
                            return;
                        }
                        this.waitingRequestOnError(waitingRequest, int1, new AuthenticationException(ADALError.WEBVIEW_RETURNED_INVALID_AUTHENTICATION_EXCEPTION, correlationInfoFromWaitingRequest));
                    }
                    else {
                        if (n == 2002) {
                            final String string3 = extras.getString("com.microsoft.aad.adal:BrowserErrorCode");
                            final String string4 = extras.getString("com.microsoft.aad.adal:BrowserErrorMessage");
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append(AcquireTokenRequest.TAG);
                            sb9.append(":onActivityResult");
                            final String string5 = sb9.toString();
                            final StringBuilder sb10 = new StringBuilder();
                            sb10.append("Error info:");
                            sb10.append(string3);
                            sb10.append(" for requestId: ");
                            sb10.append(int1);
                            sb10.append(" ");
                            sb10.append(correlationInfoFromWaitingRequest);
                            Logger.v(string5, sb10.toString(), string4, null);
                            final ADALError server_INVALID_REQUEST = ADALError.SERVER_INVALID_REQUEST;
                            final StringBuilder sb11 = new StringBuilder();
                            sb11.append(string3);
                            sb11.append(" ");
                            sb11.append(string4);
                            sb11.append(correlationInfoFromWaitingRequest);
                            this.waitingRequestOnError(waitingRequest, int1, new AuthenticationException(server_INVALID_REQUEST, sb11.toString()));
                            return;
                        }
                        if (n == 2003) {
                            final AuthenticationRequest authenticationRequest = (AuthenticationRequest)extras.getSerializable("com.microsoft.aad.adal:BrowserRequestInfo");
                            final String string6 = extras.getString("com.microsoft.aad.adal:BrowserFinalUrl", "");
                            if (string6.isEmpty()) {
                                final StringBuilder sb12 = new StringBuilder("Webview did not reach the redirectUrl. ");
                                if (authenticationRequest != null) {
                                    sb12.append(authenticationRequest.getLogInfo());
                                }
                                sb12.append(correlationInfoFromWaitingRequest);
                                final AuthenticationException ex2 = new AuthenticationException(ADALError.WEBVIEW_RETURNED_EMPTY_REDIRECT_URL, sb12.toString());
                                final StringBuilder sb13 = new StringBuilder();
                                sb13.append(AcquireTokenRequest.TAG);
                                sb13.append(":onActivityResult");
                                Logger.e(sb13.toString(), "", ex2.getMessage(), ex2.getCode());
                                this.waitingRequestOnError(waitingRequest, int1, ex2);
                                return;
                            }
                            AcquireTokenRequest.THREAD_EXECUTOR.execute(new Runnable() {
                                final /* synthetic */ CallbackHandler val$callbackHandle = new CallbackHandler(AcquireTokenRequest.this.getHandler(), waitingRequest.getDelegate());
                                
                                @Override
                                public void run() {
                                    try {
                                        final AuthenticationResult acquireTokenWithAuthCode = new AcquireTokenInteractiveRequest(AcquireTokenRequest.this.mContext, waitingRequest.getRequest(), AcquireTokenRequest.this.mTokenCacheAccessor).acquireTokenWithAuthCode(string6);
                                        waitingRequest.getAPIEvent().setWasApiCallSuccessful(true, null);
                                        waitingRequest.getAPIEvent().setCorrelationId(waitingRequest.getRequest().getCorrelationId().toString());
                                        waitingRequest.getAPIEvent().setIdToken(acquireTokenWithAuthCode.getIdToken());
                                        waitingRequest.getAPIEvent().stopTelemetryAndFlush();
                                        if (waitingRequest.getDelegate() != null) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append(AcquireTokenRequest.TAG);
                                            sb.append(":onActivityResult");
                                            Logger.v(sb.toString(), "Sending result to callback. ", waitingRequest.getRequest().getLogInfo(), null);
                                            this.val$callbackHandle.onSuccess(acquireTokenWithAuthCode);
                                        }
                                    }
                                    catch (AuthenticationException ex) {
                                        final StringBuilder sb2 = new StringBuilder(ex.getMessage());
                                        if (ex.getCause() != null) {
                                            sb2.append(ex.getCause().getMessage());
                                        }
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append(AcquireTokenRequest.TAG);
                                        sb3.append(":onActivityResult");
                                        final String string = sb3.toString();
                                        ADALError adalError;
                                        if (ex.getCode() == null) {
                                            adalError = ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN;
                                        }
                                        else {
                                            adalError = ex.getCode();
                                        }
                                        final String description = adalError.getDescription();
                                        final StringBuilder sb4 = new StringBuilder();
                                        sb4.append(sb2.toString());
                                        sb4.append(' ');
                                        sb4.append(ExceptionExtensions.getExceptionMessage(ex));
                                        sb4.append(' ');
                                        sb4.append(Log.getStackTraceString((Throwable)ex));
                                        Logger.e(string, description, sb4.toString(), ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, null);
                                        AcquireTokenRequest.this.waitingRequestOnError(this.val$callbackHandle, waitingRequest, int1, null);
                                    }
                                }
                            });
                        }
                    }
                }
            }
            catch (AuthenticationException ex3) {
                final StringBuilder sb14 = new StringBuilder();
                sb14.append(AcquireTokenRequest.TAG);
                sb14.append(":onActivityResult");
                final String string7 = sb14.toString();
                final StringBuilder sb15 = new StringBuilder();
                sb15.append("Failed to find waiting request. RequestId:");
                sb15.append(int1);
                Logger.e(string7, sb15.toString(), "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
            }
        }
    }
    
    void refreshTokenWithoutCache(final String s, final AuthenticationRequest authenticationRequest, final AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        Logger.setCorrelationId(authenticationRequest.getCorrelationId());
        final StringBuilder sb = new StringBuilder();
        sb.append(AcquireTokenRequest.TAG);
        sb.append(":refreshTokenWithoutCache");
        Logger.v(sb.toString(), "Refresh token without cache");
        AcquireTokenRequest.THREAD_EXECUTOR.execute(new Runnable() {
            final /* synthetic */ CallbackHandler val$callbackHandle = new CallbackHandler(AcquireTokenRequest.this.getHandler(), authenticationCallback);
            
            @Override
            public void run() {
                try {
                    try {
                        AcquireTokenRequest.this.validateAcquireTokenRequest(authenticationRequest);
                        final AuthenticationResult acquireTokenWithRefreshToken = new AcquireTokenSilentHandler(AcquireTokenRequest.this.mContext, authenticationRequest, AcquireTokenRequest.this.mTokenCacheAccessor).acquireTokenWithRefreshToken(s);
                        AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(true, null);
                        AcquireTokenRequest.this.mAPIEvent.setIdToken(acquireTokenWithRefreshToken.getIdToken());
                        this.val$callbackHandle.onSuccess(acquireTokenWithRefreshToken);
                    }
                    finally {}
                }
                catch (AuthenticationException ex) {
                    AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(false, ex);
                    this.val$callbackHandle.onError(ex);
                }
                AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
                return;
                AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
            }
        });
    }
    
    private static class CallbackHandler
    {
        private AuthenticationCallback<AuthenticationResult> mCallback;
        private Handler mRefHandler;
        
        public CallbackHandler(final Handler mRefHandler, final AuthenticationCallback<AuthenticationResult> mCallback) {
            this.mRefHandler = mRefHandler;
            this.mCallback = mCallback;
        }
        
        AuthenticationCallback<AuthenticationResult> getCallback() {
            return this.mCallback;
        }
        
        public void onError(final AuthenticationException ex) {
            final AuthenticationCallback<AuthenticationResult> mCallback = this.mCallback;
            if (mCallback != null) {
                final Handler mRefHandler = this.mRefHandler;
                if (mRefHandler != null) {
                    mRefHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            CallbackHandler.this.mCallback.onError(ex);
                        }
                    });
                    return;
                }
                mCallback.onError(ex);
            }
        }
        
        public void onSuccess(final AuthenticationResult authenticationResult) {
            final AuthenticationCallback<AuthenticationResult> mCallback = this.mCallback;
            if (mCallback != null) {
                final Handler mRefHandler = this.mRefHandler;
                if (mRefHandler != null) {
                    mRefHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            CallbackHandler.this.mCallback.onSuccess(authenticationResult);
                        }
                    });
                    return;
                }
                mCallback.onSuccess(authenticationResult);
            }
        }
    }
}
