package com.microsoft.aad.adal;

import android.text.*;
import android.accounts.*;
import android.app.*;
import java.io.*;
import android.content.pm.*;
import java.util.*;
import java.security.*;
import android.util.*;
import java.security.cert.*;
import java.net.*;
import android.content.*;
import android.os.*;

class BrokerProxy implements IBrokerProxy
{
    private static final int ACCOUNT_MANAGER_ERROR_CODE_BAD_AUTHENTICATION = 9;
    private static final String AUTHENTICATOR_CANCELS_REQUEST = "Authenticator cancels the request";
    public static final String DATA_USER_INFO = "com.microsoft.workaccount.user.info";
    private static final String KEY_ACCOUNT_LIST_DELIM = "|";
    private static final String KEY_APP_ACCOUNTS_FOR_TOKEN_REMOVAL = "AppAccountsForTokenRemoval";
    private static final String KEY_SHARED_PREF_ACCOUNT_LIST = "com.microsoft.aad.adal.account.list";
    private static final String TAG = "BrokerProxy";
    private AccountManager mAcctManager;
    private final String mBrokerTag;
    private Context mContext;
    private Handler mHandler;
    
    public BrokerProxy() {
        this.mBrokerTag = AuthenticationSettings.INSTANCE.getBrokerSignature();
    }
    
    public BrokerProxy(final Context mContext) {
        this.mContext = mContext;
        this.mAcctManager = AccountManager.get(mContext);
        this.mHandler = new Handler(this.mContext.getMainLooper());
        this.mBrokerTag = AuthenticationSettings.INSTANCE.getBrokerSignature();
    }
    
    private boolean checkAccount(final AccountManager accountManager, final String s, final String s2) {
        final AuthenticatorDescription[] authenticatorTypes = accountManager.getAuthenticatorTypes();
        for (int length = authenticatorTypes.length, i = 0; i < length; ++i) {
            final AuthenticatorDescription authenticatorDescription = authenticatorTypes[i];
            if (authenticatorDescription.type.equals("com.microsoft.workaccount")) {
                final Account[] accountsByType = this.mAcctManager.getAccountsByType("com.microsoft.workaccount");
                if (authenticatorDescription.packageName.equalsIgnoreCase("com.azure.authenticator") || authenticatorDescription.packageName.equalsIgnoreCase("com.microsoft.windowsintune.companyportal") || authenticatorDescription.packageName.equalsIgnoreCase(AuthenticationSettings.INSTANCE.getBrokerPackageName())) {
                    if (this.hasSupportToAddUserThroughBroker(authenticatorDescription.packageName)) {
                        return true;
                    }
                    if (accountsByType.length > 0) {
                        return this.verifyAccount(accountsByType, s, s2);
                    }
                }
            }
        }
        return false;
    }
    
    private String checkPermission(final String s) {
        if (this.mContext.getPackageManager().checkPermission(s, this.mContext.getPackageName()) != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Broker related permissions are missing for ");
            sb.append(s);
            Logger.w("BrokerProxy", sb.toString(), "", ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(' ');
            return sb2.toString();
        }
        return "";
    }
    
    private Account findAccount(final String s, final Account[] array) {
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final Account account = array[i];
                if (account != null && account.name != null && account.name.equalsIgnoreCase(s)) {
                    return account;
                }
            }
        }
        return null;
    }
    
    private UserInfo findUserInfo(final String s, final UserInfo[] array) {
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final UserInfo userInfo = array[i];
                if (userInfo != null && !TextUtils.isEmpty((CharSequence)userInfo.getUserId()) && userInfo.getUserId().equalsIgnoreCase(s)) {
                    return userInfo;
                }
            }
        }
        return null;
    }
    
    private Bundle getAuthTokenFromAccountManager(final AuthenticationRequest authenticationRequest, final Bundle bundle) throws AuthenticationException {
        final Account targetAccount = this.getTargetAccount(authenticationRequest);
        if (targetAccount != null) {
            try {
                final AccountManagerFuture authToken = this.mAcctManager.getAuthToken(targetAccount, "adal.authtoken.type", bundle, false, (AccountManagerCallback)null, this.mHandler);
                Logger.v("BrokerProxy:getAuthTokenFromAccountManager", "Received result from broker");
                final Bundle bundle2 = (Bundle)authToken.getResult();
                Logger.v("BrokerProxy:getAuthTokenFromAccountManager", "Returning result from broker");
                return bundle2;
            }
            catch (IOException ex) {
                Logger.e("BrokerProxy:getAuthTokenFromAccountManager", "Authenticator cancels the request", "", ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION);
                if (ex.getMessage() != null && ex.getMessage().contains(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE.getDescription())) {
                    final ADALError device_CONNECTION_IS_NOT_AVAILABLE = ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Received error from broker, errorCode: ");
                    sb.append(ex.getMessage());
                    throw new AuthenticationException(device_CONNECTION_IS_NOT_AVAILABLE, sb.toString());
                }
                if (ex.getMessage() != null && ex.getMessage().contains(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription())) {
                    final ADALError no_NETWORK_CONNECTION_POWER_OPTIMIZATION = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Received error from broker, errorCode: ");
                    sb2.append(ex.getMessage());
                    throw new AuthenticationException(no_NETWORK_CONNECTION_POWER_OPTIMIZATION, sb2.toString());
                }
                throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION, ex.getMessage(), ex);
            }
            catch (AuthenticatorException ex2) {
                if (!StringExtensions.isNullOrBlank(ex2.getMessage()) && ex2.getMessage().contains("invalid_grant")) {
                    Logger.e("BrokerProxy:getAuthTokenFromAccountManager", "Authenticator cancels the request", "Acquire token failed with 'invalid grant' error, cannot proceed with silent request.", ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
                    throw new AuthenticationException(ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED, ex2.getMessage());
                }
                Logger.e("BrokerProxy:getAuthTokenFromAccountManager", "Authenticator cancels the request", "", ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN);
                throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN, ex2.getMessage());
            }
            catch (OperationCanceledException ex3) {
                Logger.e("BrokerProxy:getAuthTokenFromAccountManager", "Authenticator cancels the request", "", ADALError.AUTH_FAILED_CANCELLED, (Throwable)ex3);
                throw new AuthenticationException(ADALError.AUTH_FAILED_CANCELLED, ex3.getMessage(), (Throwable)ex3);
            }
        }
        Logger.v("BrokerProxy:getAuthTokenFromAccountManager", "Target account is not found");
        return null;
    }
    
    private Bundle getBrokerOptions(final AuthenticationRequest authenticationRequest) {
        final Bundle bundle = new Bundle();
        bundle.putInt("com.microsoft.aad.adal:RequestId", authenticationRequest.getRequestId());
        bundle.putInt("expiration.buffer", AuthenticationSettings.INSTANCE.getExpirationBuffer());
        bundle.putString("account.authority", authenticationRequest.getAuthority());
        bundle.putString("account.resource", authenticationRequest.getResource());
        bundle.putString("account.redirect", authenticationRequest.getRedirectUri());
        bundle.putString("account.clientid.key", authenticationRequest.getClientId());
        bundle.putString("adal.version.key", authenticationRequest.getVersion());
        bundle.putString("account.userinfo.userid", authenticationRequest.getUserId());
        bundle.putString("account.extra.query.param", authenticationRequest.getExtraQueryParamsAuthentication());
        if (authenticationRequest.getCorrelationId() != null) {
            bundle.putString("account.correlationid", authenticationRequest.getCorrelationId().toString());
        }
        String s;
        if (StringExtensions.isNullOrBlank(s = authenticationRequest.getBrokerAccountName())) {
            s = authenticationRequest.getLoginHint();
        }
        bundle.putString("account.login.hint", s);
        bundle.putString("account.name", s);
        if (authenticationRequest.getPrompt() != null) {
            bundle.putString("account.prompt", authenticationRequest.getPrompt().name());
        }
        if (Utility.isClaimsChallengePresent(authenticationRequest)) {
            bundle.putString("skip.cache", Boolean.toString(true));
            bundle.putString("account.claims", authenticationRequest.getClaimsChallenge());
        }
        return bundle;
    }
    
    private Intent getIntentForBrokerActivityFromAccountManager(final Bundle bundle) {
        ADALError adalError;
        try {
            return (Intent)((Bundle)this.mAcctManager.addAccount("com.microsoft.workaccount", "adal.authtoken.type", (String[])null, bundle, (Activity)null, (AccountManagerCallback)null, this.mHandler).getResult()).getParcelable("intent");
        }
        catch (IOException ex) {
            adalError = ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION;
        }
        catch (AuthenticatorException ex) {
            adalError = ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING;
        }
        catch (OperationCanceledException ex) {
            adalError = ADALError.AUTH_FAILED_CANCELLED;
        }
        final IOException ex;
        Logger.e("BrokerProxy:getIntentForBrokerActivityFromAccountManager", "Authenticator cancels the request", "", adalError, ex);
        return null;
    }
    
    private AuthenticationResult getResultFromBrokerResponse(final Bundle bundle, final AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (bundle == null) {
            throw new IllegalArgumentException("bundleResult");
        }
        final int int1 = bundle.getInt("errorCode");
        final String string = bundle.getString("errorMessage");
        final String string2 = bundle.getString("error");
        final String string3 = bundle.getString("error_description");
        if (!StringExtensions.isNullOrBlank(string)) {
            ADALError adalError;
            if (int1 != 3) {
                if (int1 != 4) {
                    if (int1 != 6) {
                        if (int1 != 7) {
                            if (int1 != 9) {
                                adalError = ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN;
                            }
                            else {
                                adalError = ADALError.BROKER_AUTHENTICATOR_BAD_AUTHENTICATION;
                            }
                        }
                        else {
                            adalError = ADALError.BROKER_AUTHENTICATOR_BAD_ARGUMENTS;
                        }
                    }
                    else {
                        adalError = ADALError.BROKER_AUTHENTICATOR_UNSUPPORTED_OPERATION;
                    }
                }
                else {
                    adalError = ADALError.AUTH_FAILED_CANCELLED;
                }
            }
            else if (!string.contains(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription())) {
                if (string.contains(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE.getDescription())) {
                    adalError = ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE;
                }
                else {
                    adalError = ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION;
                }
            }
            else {
                adalError = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
            }
            throw new AuthenticationException(adalError, string);
        }
        if (!StringExtensions.isNullOrBlank(string2) && authenticationRequest.isSilent()) {
            final ADALError auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
            final StringBuilder sb = new StringBuilder();
            sb.append("Received error from broker, errorCode: ");
            sb.append(string2);
            sb.append("; ErrorDescription: ");
            sb.append(string3);
            final AuthenticationException ex = new AuthenticationException(auth_REFRESH_FAILED_PROMPT_NOT_ALLOWED, sb.toString());
            final Serializable serializable = bundle.getSerializable("response_body");
            final Serializable serializable2 = bundle.getSerializable("response_headers");
            if (serializable != null && serializable instanceof HashMap) {
                ex.setHttpResponseBody((HashMap<String, String>)serializable);
            }
            if (serializable2 != null && serializable2 instanceof HashMap) {
                ex.setHttpResponseHeaders((HashMap<String, List<String>>)serializable2);
            }
            ex.setServiceStatusCode(bundle.getInt("status_code"));
            throw ex;
        }
        if (bundle.getBoolean("account.initial.request")) {
            return AuthenticationResult.createResultForInitialRequest();
        }
        final UserInfo userInfoFromBrokerResult = UserInfo.getUserInfoFromBrokerResult(bundle);
        final String string4 = bundle.getString("account.userinfo.tenantid", "");
        Date time;
        if (bundle.getLong("account.expiredate") == 0L) {
            Logger.v("BrokerProxy:getResultFromBrokerResponse", "Broker doesn't return expire date, set it current date plus one hour");
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.add(13, 3600);
            time = gregorianCalendar.getTime();
        }
        else {
            time = new Date(bundle.getLong("account.expiredate"));
        }
        final AuthenticationResult authenticationResult = new AuthenticationResult(bundle.getString("authtoken"), "", time, false, userInfoFromBrokerResult, string4, "", null);
        final TelemetryUtils.CliTelemInfo cliTelemInfo = new TelemetryUtils.CliTelemInfo();
        cliTelemInfo.setServerErrorCode(bundle.getString("cliteleminfo.server_error"));
        cliTelemInfo.setServerSubErrorCode(bundle.getString("cliteleminfo.server_suberror"));
        cliTelemInfo.setRefreshTokenAge(bundle.getString("cliteleminfo.rt_age"));
        cliTelemInfo.setSpeRing(bundle.getString("cliteleminfo.spe_ring"));
        authenticationResult.setCliTelemInfo(cliTelemInfo);
        return authenticationResult;
    }
    
    private X509Certificate getSelfSignedCert(final List<X509Certificate> list) throws AuthenticationException {
        final Iterator<X509Certificate> iterator = list.iterator();
        int n = 0;
        X509Certificate x509Certificate = null;
        while (iterator.hasNext()) {
            final X509Certificate x509Certificate2 = iterator.next();
            if (x509Certificate2.getSubjectDN().equals(x509Certificate2.getIssuerDN())) {
                ++n;
                x509Certificate = x509Certificate2;
            }
        }
        if (n <= 1 && x509Certificate != null) {
            return x509Certificate;
        }
        throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED, "Multiple self signed certs found or no self signed cert existed.");
    }
    
    private Account getTargetAccount(final AuthenticationRequest authenticationRequest) {
        final Account[] accountsByType = this.mAcctManager.getAccountsByType("com.microsoft.workaccount");
        if (!TextUtils.isEmpty((CharSequence)authenticationRequest.getBrokerAccountName())) {
            return this.findAccount(authenticationRequest.getBrokerAccountName(), accountsByType);
        }
        try {
            final UserInfo userInfo = this.findUserInfo(authenticationRequest.getUserId(), this.getBrokerUsers());
            if (userInfo != null) {
                return this.findAccount(userInfo.getDisplayableId(), accountsByType);
            }
        }
        catch (IOException | AuthenticatorException | OperationCanceledException ex2) {
            final OperationCanceledException ex;
            final Throwable t = (Throwable)ex;
            Logger.e("BrokerProxy:getTargetAccount", "Exception is thrown when trying to get target account.", t.getMessage(), ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION, t);
        }
        return null;
    }
    
    private UserInfo[] getUserInfoFromAccountManager() throws OperationCanceledException, AuthenticatorException, IOException {
        final Account[] accountsByType = this.mAcctManager.getAccountsByType("com.microsoft.workaccount");
        final Bundle bundle = new Bundle();
        bundle.putBoolean("com.microsoft.workaccount.user.info", true);
        final StringBuilder sb = new StringBuilder();
        sb.append("Retrieve all the accounts from account manager with broker account type, and the account length is: ");
        sb.append(accountsByType.length);
        Logger.v("BrokerProxy:getUserInfoFromAccountManager", sb.toString());
        final UserInfo[] array = new UserInfo[accountsByType.length];
        for (int i = 0; i < accountsByType.length; ++i) {
            final AccountManagerFuture updateCredentials = this.mAcctManager.updateCredentials(accountsByType[i], "adal.authtoken.type", bundle, (Activity)null, (AccountManagerCallback)null, (Handler)null);
            Logger.v("BrokerProxy:getUserInfoFromAccountManager", "Waiting for userinfo retrieval result from Broker.");
            final Bundle bundle2 = (Bundle)updateCredentials.getResult();
            array[i] = new UserInfo(bundle2.getString("account.userinfo.userid"), bundle2.getString("account.userinfo.given.name"), bundle2.getString("account.userinfo.family.name"), bundle2.getString("account.userinfo.identity.provider"), bundle2.getString("account.userinfo.userid.displayable"));
        }
        return array;
    }
    
    private boolean hasSupportToAddUserThroughBroker(final String package1) {
        final Intent intent = new Intent();
        intent.setPackage(package1);
        final StringBuilder sb = new StringBuilder();
        sb.append(package1);
        sb.append(".ui.AccountChooserActivity");
        intent.setClassName(package1, sb.toString());
        final PackageManager packageManager = this.mContext.getPackageManager();
        boolean b = false;
        if (packageManager.queryIntentActivities(intent, 0).size() > 0) {
            b = true;
        }
        return b;
    }
    
    private boolean isBrokerAccountServiceSupported() {
        return this.isServiceSupported(this.mContext, BrokerAccountServiceHandler.getIntentForBrokerAccountService(this.mContext));
    }
    
    private boolean isBrokerWithPRTSupport(final Intent intent) {
        if (intent != null) {
            return "v2".equalsIgnoreCase(intent.getStringExtra("broker.version"));
        }
        throw new IllegalArgumentException("intent");
    }
    
    private boolean isServiceSupported(final Context context, final Intent intent) {
        final boolean b = false;
        if (intent == null) {
            return false;
        }
        final List queryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
        boolean b2 = b;
        if (queryIntentServices != null) {
            b2 = b;
            if (queryIntentServices.size() > 0) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private List<X509Certificate> readCertDataForBrokerApp(final String s) throws PackageManager$NameNotFoundException, AuthenticationException, IOException, GeneralSecurityException {
        final PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(s, 64);
        if (packageInfo == null) {
            throw new AuthenticationException(ADALError.APP_PACKAGE_NAME_NOT_FOUND, "No broker package existed.");
        }
        if (packageInfo.signatures != null && packageInfo.signatures.length != 0) {
            final ArrayList list = new ArrayList<X509Certificate>(packageInfo.signatures.length);
            final Signature[] signatures = packageInfo.signatures;
            final int length = signatures.length;
            int i = 0;
            while (i < length) {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signatures[i].toByteArray());
                try {
                    list.add((X509Certificate)CertificateFactory.getInstance("X509").generateCertificate(byteArrayInputStream));
                    ++i;
                    continue;
                }
                catch (CertificateException ex) {
                    throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED);
                }
                break;
            }
            return (List<X509Certificate>)list;
        }
        throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED, "No signature associated with the broker package.");
    }
    
    private void removeAccountFromAccountManager() {
        Logger.v("BrokerProxy:removeAccountFromAccountManager", "Try to remove account from account manager.");
        final Account[] accountsByType = this.mAcctManager.getAccountsByType("com.microsoft.workaccount");
        if (accountsByType.length != 0) {
            for (int length = accountsByType.length, i = 0; i < length; ++i) {
                final Account account = accountsByType[i];
                final StringBuilder sb = new StringBuilder();
                sb.append("Account: ");
                sb.append(account.name);
                Logger.v("BrokerProxy:removeAccountFromAccountManager", "Remove tokens for account. ", sb.toString(), null);
                final Bundle bundle = new Bundle();
                bundle.putString("account.remove.tokens", "account.remove.tokens.value");
                this.mAcctManager.getAuthToken(account, "adal.authtoken.type", bundle, false, (AccountManagerCallback)null, this.mHandler);
            }
        }
    }
    
    private boolean verifyAccount(final Account[] array, final String s, final String s2) {
        if (!StringExtensions.isNullOrBlank(s)) {
            return s.equalsIgnoreCase(array[0].name);
        }
        if (!StringExtensions.isNullOrBlank(s2)) {
            try {
                return this.findUserInfo(s2, this.getBrokerUsers()) != null;
            }
            catch (IOException | AuthenticatorException | OperationCanceledException ex2) {
                final OperationCanceledException ex;
                final Throwable t = (Throwable)ex;
                Logger.e("BrokerProxy:verifyAccount", "Exception thrown when verifying accounts in broker. ", t.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, t);
                Logger.v("BrokerProxy:verifyAccount", "It could not check the uniqueid from broker. It is not using broker");
                return false;
            }
        }
        return true;
    }
    
    private boolean verifyAuthenticator(final AccountManager accountManager) {
        final AuthenticatorDescription[] authenticatorTypes = accountManager.getAuthenticatorTypes();
        for (int length = authenticatorTypes.length, i = 0; i < length; ++i) {
            final AuthenticatorDescription authenticatorDescription = authenticatorTypes[i];
            if (authenticatorDescription.type.equals("com.microsoft.workaccount") && this.verifySignature(authenticatorDescription.packageName)) {
                return true;
            }
        }
        return false;
    }
    
    private void verifyCertificateChain(final List<X509Certificate> list) throws GeneralSecurityException, AuthenticationException {
        final PKIXParameters pkixParameters = new PKIXParameters(Collections.singleton(new TrustAnchor(this.getSelfSignedCert(list), null)));
        pkixParameters.setRevocationEnabled(false);
        CertPathValidator.getInstance("PKIX").validate(CertificateFactory.getInstance("X.509").generateCertPath(list), pkixParameters);
    }
    
    private void verifyNotOnMainThread() {
        final Looper myLooper = Looper.myLooper();
        if (myLooper == null || myLooper != this.mContext.getMainLooper()) {
            return;
        }
        final IllegalStateException ex = new IllegalStateException("calling this from your main thread can lead to deadlock");
        Logger.e("BrokerProxy", "calling this from your main thread can lead to deadlock and/or ANRs", "", ADALError.DEVELOPER_CALLING_ON_MAIN_THREAD, ex);
        if (this.mContext.getApplicationInfo().targetSdkVersion < 8) {
            return;
        }
        throw ex;
    }
    
    private boolean verifySignature(String s) {
        ADALError adalError;
        try {
            final List<X509Certificate> certDataForBrokerApp = this.readCertDataForBrokerApp(s);
            this.verifySignatureHash(certDataForBrokerApp);
            if (certDataForBrokerApp.size() > 1) {
                this.verifyCertificateChain(certDataForBrokerApp);
            }
            return true;
        }
        catch (AuthenticationException | IOException | GeneralSecurityException ex3) {
            final GeneralSecurityException ex2;
            final GeneralSecurityException ex = ex2;
            Logger.e("BrokerProxy:verifySignature", ADALError.BROKER_VERIFICATION_FAILED.getDescription(), ex.getMessage(), ADALError.BROKER_VERIFICATION_FAILED, ex);
            return false;
        }
        catch (NoSuchAlgorithmException ex4) {
            s = "Digest SHA algorithm does not exists";
            adalError = ADALError.DEVICE_NO_SUCH_ALGORITHM;
        }
        catch (PackageManager$NameNotFoundException ex5) {
            s = "Broker related package does not exist";
            adalError = ADALError.BROKER_PACKAGE_NAME_NOT_FOUND;
        }
        Logger.e("BrokerProxy:verifySignature", s, "", adalError);
        return false;
    }
    
    private void verifySignatureHash(final List<X509Certificate> list) throws NoSuchAlgorithmException, CertificateEncodingException, AuthenticationException {
        for (final X509Certificate x509Certificate : list) {
            final MessageDigest instance = MessageDigest.getInstance("SHA");
            instance.update(x509Certificate.getEncoded());
            final String encodeToString = Base64.encodeToString(instance.digest(), 2);
            if (this.mBrokerTag.equals(encodeToString) || "ho040S3ffZkmxqtQrSwpTVOn9r0=".equals(encodeToString)) {
                return;
            }
        }
        throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED);
    }
    
    @Override
    public SwitchToBroker canSwitchToBroker(final String s) {
        try {
            final URL url = new URL(s);
            final String packageName = this.mContext.getPackageName();
            final boolean useBroker = AuthenticationSettings.INSTANCE.getUseBroker();
            final boolean b = false;
            final boolean b2 = useBroker && !packageName.equalsIgnoreCase(AuthenticationSettings.INSTANCE.getBrokerPackageName()) && !packageName.equalsIgnoreCase("com.azure.authenticator") && this.verifyAuthenticator(this.mAcctManager) && !UrlExtensions.isADFSAuthority(url);
            if (!b2) {
                Logger.v("BrokerProxy:canSwitchToBroker", "Broker auth is turned off or no valid broker is available on the device, cannot switch to broker.");
                return SwitchToBroker.CANNOT_SWITCH_TO_BROKER;
            }
            if (!this.isBrokerAccountServiceSupported()) {
                boolean b3 = b;
                if (b2) {
                    b3 = b;
                    if (this.checkAccount(this.mAcctManager, "", "")) {
                        b3 = true;
                    }
                }
                if (!b3) {
                    Logger.v("BrokerProxy:canSwitchToBroker", "No valid account existed in broker, cannot switch to broker for auth.");
                    return SwitchToBroker.CANNOT_SWITCH_TO_BROKER;
                }
                try {
                    this.verifyBrokerPermissionsAPI23AndHigher();
                }
                catch (UsageAuthenticationException ex) {
                    Logger.v("BrokerProxy:canSwitchToBroker", "Missing GET_ACCOUNTS permission, cannot switch to broker.");
                    return SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER;
                }
            }
            return SwitchToBroker.CAN_SWITCH_TO_BROKER;
        }
        catch (MalformedURLException ex2) {
            throw new IllegalArgumentException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL.name());
        }
    }
    
    @Override
    public boolean canUseLocalCache(String s) {
        if (this.canSwitchToBroker(s) == SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            s = "It does not use broker";
        }
        else {
            if (!this.verifySignature(this.mContext.getPackageName())) {
                return false;
            }
            s = "Broker installer can use local cache";
        }
        Logger.v("BrokerProxy:canUseLocalCache", s);
        return true;
    }
    
    @Override
    public AuthenticationResult getAuthTokenInBackground(final AuthenticationRequest authenticationRequest, final BrokerEvent brokerEvent) throws AuthenticationException {
        this.verifyNotOnMainThread();
        final Bundle brokerOptions = this.getBrokerOptions(authenticationRequest);
        Bundle bundle;
        if (this.isBrokerAccountServiceSupported()) {
            bundle = BrokerAccountServiceHandler.getInstance().getAuthToken(this.mContext, brokerOptions, brokerEvent);
        }
        else {
            bundle = this.getAuthTokenFromAccountManager(authenticationRequest, brokerOptions);
        }
        if (bundle == null) {
            Logger.v("BrokerProxy", "No bundle result returned from broker for silent request.");
            return null;
        }
        return this.getResultFromBrokerResponse(bundle, authenticationRequest);
    }
    
    @Override
    public String getBrokerAppVersion(final String s) throws PackageManager$NameNotFoundException {
        final PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(s, 0);
        final StringBuilder sb = new StringBuilder();
        sb.append("VersionName=");
        sb.append(packageInfo.versionName);
        sb.append(";VersonCode=");
        sb.append(packageInfo.versionCode);
        sb.append(".");
        return sb.toString();
    }
    
    @Override
    public UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalArgumentException("Calling getBrokerUsers on main thread");
        }
        if (this.isBrokerAccountServiceSupported()) {
            return BrokerAccountServiceHandler.getInstance().getBrokerUsers(this.mContext);
        }
        return this.getUserInfoFromAccountManager();
    }
    
    @Override
    public String getCurrentActiveBrokerPackageName() {
        final AuthenticatorDescription[] authenticatorTypes = this.mAcctManager.getAuthenticatorTypes();
        for (int length = authenticatorTypes.length, i = 0; i < length; ++i) {
            final AuthenticatorDescription authenticatorDescription = authenticatorTypes[i];
            if (authenticatorDescription.type.equals("com.microsoft.workaccount")) {
                return authenticatorDescription.packageName;
            }
        }
        return null;
    }
    
    @Override
    public String getCurrentUser() {
        if (this.isBrokerAccountServiceSupported()) {
            this.verifyNotOnMainThread();
            try {
                final UserInfo[] brokerUsers = BrokerAccountServiceHandler.getInstance().getBrokerUsers(this.mContext);
                if (brokerUsers.length == 0) {
                    return null;
                }
                return brokerUsers[0].getDisplayableId();
            }
            catch (IOException ex) {
                Logger.e("BrokerProxy:getCurrentUser", "No current user could be retrieved.", "", null, ex);
                return null;
            }
        }
        final Account[] accountsByType = this.mAcctManager.getAccountsByType("com.microsoft.workaccount");
        if (accountsByType.length > 0) {
            return accountsByType[0].name;
        }
        return null;
    }
    
    @Override
    public Intent getIntentForBrokerActivity(final AuthenticationRequest authenticationRequest, final BrokerEvent brokerEvent) throws AuthenticationException {
        final Bundle brokerOptions = this.getBrokerOptions(authenticationRequest);
        Intent intent;
        if (this.isBrokerAccountServiceSupported()) {
            intent = BrokerAccountServiceHandler.getInstance().getIntentForInteractiveRequest(this.mContext, brokerEvent);
            if (intent == null) {
                Logger.e("BrokerProxy", "Received null intent from broker interactive request.", null, ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING);
                throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, "Received null intent from broker interactive request.");
            }
            intent.putExtras(brokerOptions);
        }
        else {
            intent = this.getIntentForBrokerActivityFromAccountManager(brokerOptions);
        }
        if (intent != null) {
            intent.putExtra("com.microsoft.aadbroker.adal.broker.request", "com.microsoft.aadbroker.adal.broker.request");
            if (!this.isBrokerWithPRTSupport(intent) && PromptBehavior.FORCE_PROMPT == authenticationRequest.getPrompt()) {
                Logger.v("BrokerProxy:getIntentForBrokerActivity", "FORCE_PROMPT is set for broker auth via old version of broker app, reset to ALWAYS.");
                intent.putExtra("account.prompt", PromptBehavior.Always.name());
            }
        }
        return intent;
    }
    
    @Override
    public void removeAccounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (BrokerProxy.this.isBrokerAccountServiceSupported()) {
                    BrokerAccountServiceHandler.getInstance().removeAccounts(BrokerProxy.this.mContext);
                    return;
                }
                BrokerProxy.this.removeAccountFromAccountManager();
            }
        }).start();
    }
    
    @Override
    public void saveAccount(String string) {
        if (string != null) {
            if (string.isEmpty()) {
                return;
            }
            final SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("com.microsoft.aad.adal.account.list", 0);
            final String string2 = sharedPreferences.getString("AppAccountsForTokenRemoval", "");
            final StringBuilder sb = new StringBuilder();
            sb.append("|");
            sb.append(string);
            if (!string2.contains(sb.toString())) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string2);
                sb2.append("|");
                sb2.append(string);
                string = sb2.toString();
                final SharedPreferences$Editor edit = sharedPreferences.edit();
                edit.putString("AppAccountsForTokenRemoval", string);
                edit.apply();
            }
        }
    }
    
    public boolean verifyBrokerPermissionsAPI22AndLess() throws UsageAuthenticationException {
        final StringBuilder sb = new StringBuilder();
        if (Build$VERSION.SDK_INT >= 23) {
            Logger.v("BrokerProxy", "Device runs on 23 and above, skip the check for 22 and below.");
            return true;
        }
        sb.append(this.checkPermission("android.permission.GET_ACCOUNTS"));
        sb.append(this.checkPermission("android.permission.MANAGE_ACCOUNTS"));
        sb.append(this.checkPermission("android.permission.USE_CREDENTIALS"));
        if (sb.length() == 0) {
            return true;
        }
        final ADALError developer_BROKER_PERMISSIONS_MISSING = ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Broker related permissions are missing for ");
        sb2.append(sb.toString());
        throw new UsageAuthenticationException(developer_BROKER_PERMISSIONS_MISSING, sb2.toString());
    }
    
    public boolean verifyBrokerPermissionsAPI23AndHigher() throws UsageAuthenticationException {
        final StringBuilder sb = new StringBuilder();
        if (Build$VERSION.SDK_INT < 23) {
            Logger.v("BrokerProxy", "Device is lower than 23, skip the GET_ACCOUNTS permission check.");
            return true;
        }
        sb.append(this.checkPermission("android.permission.GET_ACCOUNTS"));
        if (sb.length() == 0) {
            return true;
        }
        final ADALError developer_BROKER_PERMISSIONS_MISSING = ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Broker related permissions are missing for ");
        sb2.append(sb.toString());
        throw new UsageAuthenticationException(developer_BROKER_PERMISSIONS_MISSING, sb2.toString());
    }
    
    @Override
    public boolean verifyUser(final String s, final String s2) {
        return this.isBrokerAccountServiceSupported() || this.checkAccount(this.mAcctManager, s, s2);
    }
    
    enum SwitchToBroker
    {
        CANNOT_SWITCH_TO_BROKER, 
        CAN_SWITCH_TO_BROKER, 
        NEED_PERMISSIONS_TO_SWITCH_TO_BROKER;
    }
}
