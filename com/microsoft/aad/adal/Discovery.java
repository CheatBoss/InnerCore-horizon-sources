package com.microsoft.aad.adal;

import java.util.concurrent.locks.*;
import android.content.*;
import android.net.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;

final class Discovery
{
    private static final Set<String> AAD_WHITELISTED_HOSTS;
    private static final Map<String, Set<URI>> ADFS_VALIDATED_AUTHORITIES;
    private static final String API_VERSION_KEY = "api-version";
    private static final String API_VERSION_VALUE = "1.1";
    private static final String AUTHORIZATION_COMMON_ENDPOINT = "/common/oauth2/authorize";
    private static final String AUTHORIZATION_ENDPOINT_KEY = "authorization_endpoint";
    private static final String INSTANCE_DISCOVERY_SUFFIX = "common/discovery/instance";
    private static final String TAG = "Discovery";
    private static final String TRUSTED_QUERY_INSTANCE = "login.microsoftonline.com";
    private static volatile ReentrantLock sInstanceDiscoveryNetworkRequestLock;
    private Context mContext;
    private UUID mCorrelationId;
    private final IWebRequestHandler mWebrequestHandler;
    
    static {
        AAD_WHITELISTED_HOSTS = Collections.synchronizedSet(new HashSet<String>());
        ADFS_VALIDATED_AUTHORITIES = Collections.synchronizedMap(new HashMap<String, Set<URI>>());
    }
    
    public Discovery(final Context mContext) {
        this.initValidList();
        this.mContext = mContext;
        this.mWebrequestHandler = new WebRequestHandler();
    }
    
    private URL buildQueryString(final String s, final String s2) throws MalformedURLException {
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.scheme("https").authority(s);
        uri$Builder.appendEncodedPath("common/discovery/instance").appendQueryParameter("api-version", "1.1").appendQueryParameter("authorization_endpoint", s2);
        return new URL(uri$Builder.build().toString());
    }
    
    private String getAuthorizationCommonEndpoint(final URL url) {
        return new Uri$Builder().scheme("https").authority(url.getHost()).appendPath("/common/oauth2/authorize").build().toString();
    }
    
    private static ReentrantLock getLock() {
        if (Discovery.sInstanceDiscoveryNetworkRequestLock == null) {
            synchronized (Discovery.class) {
                if (Discovery.sInstanceDiscoveryNetworkRequestLock == null) {
                    Discovery.sInstanceDiscoveryNetworkRequestLock = new ReentrantLock();
                }
            }
        }
        return Discovery.sInstanceDiscoveryNetworkRequestLock;
    }
    
    static Set<String> getValidHosts() {
        return Discovery.AAD_WHITELISTED_HOSTS;
    }
    
    private void initValidList() {
        if (Discovery.AAD_WHITELISTED_HOSTS.isEmpty()) {
            Discovery.AAD_WHITELISTED_HOSTS.add("login.windows.net");
            Discovery.AAD_WHITELISTED_HOSTS.add("login.microsoftonline.com");
            Discovery.AAD_WHITELISTED_HOSTS.add("login.chinacloudapi.cn");
            Discovery.AAD_WHITELISTED_HOSTS.add("login.microsoftonline.de");
            Discovery.AAD_WHITELISTED_HOSTS.add("login-us.microsoftonline.com");
            Discovery.AAD_WHITELISTED_HOSTS.add("login.microsoftonline.us");
        }
    }
    
    private Map<String, String> parseResponse(final HttpWebResponse httpWebResponse) throws JSONException {
        return HashMapExtensions.getJsonResponse(httpWebResponse);
    }
    
    private void performInstanceDiscovery(final URL url, final String s) throws AuthenticationException {
        if (AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
            return;
        }
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        try {
            AuthorityValidationMetadataCache.processInstanceDiscoveryMetadata(url, this.sendRequest(this.buildQueryString(s, this.getAuthorizationCommonEndpoint(url))));
            if (!AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
                final ArrayList<String> list = new ArrayList<String>();
                list.add(url.getHost());
                AuthorityValidationMetadataCache.updateInstanceDiscoveryMap(url.getHost(), new InstanceDiscoveryMetadata(url.getHost(), url.getHost(), list));
            }
            if (AuthorityValidationMetadataCache.isAuthorityValidated(url)) {
                return;
            }
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
        }
        catch (IOException | JSONException ex2) {
            final JSONException ex;
            final Throwable t = (Throwable)ex;
            Logger.e("Discovery:performInstanceDiscovery", "Error when validating authority. ", "", ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, t);
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE, t.getMessage(), t);
        }
    }
    
    private Map<String, String> sendRequest(final URL url) throws IOException, JSONException, AuthenticationException {
        final StringBuilder sb = new StringBuilder();
        sb.append("queryUrl: ");
        sb.append(url);
        Logger.v("Discovery", "Sending discovery request to query url. ", sb.toString(), null);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Accept", "application/json");
        final UUID mCorrelationId = this.mCorrelationId;
        if (mCorrelationId != null) {
            hashMap.put("client-request-id", mCorrelationId.toString());
            hashMap.put("return-client-request-id", "true");
        }
        try {
            ClientMetrics.INSTANCE.beginClientMetricsRecord(url, this.mCorrelationId, hashMap);
            final HttpWebResponse sendGet = this.mWebrequestHandler.sendGet(url, hashMap);
            ClientMetrics.INSTANCE.setLastError(null);
            final Map<String, String> response = this.parseResponse(sendGet);
            if (!response.containsKey("error_codes")) {
                return response;
            }
            final String lastError = response.get("error_codes");
            ClientMetrics.INSTANCE.setLastError(lastError);
            final ADALError developer_AUTHORITY_IS_NOT_VALID_INSTANCE = ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Fail to valid authority with errors: ");
            sb2.append(lastError);
            throw new AuthenticationException(developer_AUTHORITY_IS_NOT_VALID_INSTANCE, sb2.toString());
        }
        finally {
            ClientMetrics.INSTANCE.endClientMetricsRecord("instance", this.mCorrelationId);
        }
    }
    
    private static void validateADFS(final URL url, final String s) throws AuthenticationException {
        try {
            final URI uri = url.toURI();
            if (Discovery.ADFS_VALIDATED_AUTHORITIES.get(s) != null && Discovery.ADFS_VALIDATED_AUTHORITIES.get(s).contains(uri)) {
                return;
            }
            if (ADFSWebFingerValidator.realmIsTrusted(uri, new WebFingerMetadataRequestor().requestMetadata(new WebFingerMetadataRequestParameters(url, new DRSMetadataRequestor().requestMetadata(s))))) {
                if (Discovery.ADFS_VALIDATED_AUTHORITIES.get(s) == null) {
                    Discovery.ADFS_VALIDATED_AUTHORITIES.put(s, new HashSet<URI>());
                }
                Discovery.ADFS_VALIDATED_AUTHORITIES.get(s).add(uri);
                return;
            }
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
        }
        catch (URISyntaxException ex) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, "Authority URL/URI must be RFC 2396 compliant to use AD FS validation");
        }
    }
    
    static void verifyAuthorityValidInstance(final URL url) throws AuthenticationException {
        if (url != null && !StringExtensions.isNullOrBlank(url.getHost()) && url.getProtocol().equals("https") && StringExtensions.isNullOrBlank(url.getQuery()) && StringExtensions.isNullOrBlank(url.getRef()) && !StringExtensions.isNullOrBlank(url.getPath())) {
            return;
        }
        throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
    }
    
    public void setCorrelationId(final UUID mCorrelationId) {
        this.mCorrelationId = mCorrelationId;
    }
    
    void validateAuthority(final URL url) throws AuthenticationException {
        verifyAuthorityValidInstance(url);
        if (AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
            return;
        }
        String lowerCase = url.getHost().toLowerCase(Locale.US);
        if (!Discovery.AAD_WHITELISTED_HOSTS.contains(url.getHost().toLowerCase(Locale.US))) {
            lowerCase = "login.microsoftonline.com";
        }
        try {
            (Discovery.sInstanceDiscoveryNetworkRequestLock = getLock()).lock();
            this.performInstanceDiscovery(url, lowerCase);
        }
        finally {
            Discovery.sInstanceDiscoveryNetworkRequestLock.unlock();
        }
    }
    
    void validateAuthorityADFS(final URL url, final String s) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(s)) {
            validateADFS(url, s);
            return;
        }
        throw new IllegalArgumentException("Cannot validate AD FS Authority with domain [null]");
    }
}
