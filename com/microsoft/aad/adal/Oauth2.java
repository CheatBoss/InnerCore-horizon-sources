package com.microsoft.aad.adal;

import android.util.*;
import org.json.*;
import android.text.*;
import java.net.*;
import android.os.*;
import android.net.*;
import java.util.*;
import java.io.*;

class Oauth2
{
    private static final String DEFAULT_AUTHORIZE_ENDPOINT = "/oauth2/authorize";
    private static final String DEFAULT_TOKEN_ENDPOINT = "/oauth2/token";
    private static final int DELAY_TIME_PERIOD = 1000;
    private static final String HTTPS_PROTOCOL_STRING = "https";
    private static final int MAX_RESILIENCY_ERROR_CODE = 599;
    private static final String TAG = "Oauth";
    private IJWSBuilder mJWSBuilder;
    private AuthenticationRequest mRequest;
    private boolean mRetryOnce;
    private String mTokenEndpoint;
    private IWebRequestHandler mWebRequestHandler;
    
    Oauth2(final AuthenticationRequest mRequest) {
        this.mJWSBuilder = new JWSBuilder();
        this.mRetryOnce = true;
        this.mRequest = mRequest;
        this.mWebRequestHandler = null;
        this.mJWSBuilder = null;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append("/oauth2/token");
        this.setTokenEndpoint(sb.toString());
    }
    
    public Oauth2(final AuthenticationRequest mRequest, final IWebRequestHandler mWebRequestHandler) {
        this.mJWSBuilder = new JWSBuilder();
        this.mRetryOnce = true;
        this.mRequest = mRequest;
        this.mWebRequestHandler = mWebRequestHandler;
        this.mJWSBuilder = null;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append("/oauth2/token");
        this.setTokenEndpoint(sb.toString());
    }
    
    public Oauth2(final AuthenticationRequest mRequest, final IWebRequestHandler mWebRequestHandler, final IJWSBuilder mjwsBuilder) {
        this.mJWSBuilder = new JWSBuilder();
        this.mRetryOnce = true;
        this.mRequest = mRequest;
        this.mWebRequestHandler = mWebRequestHandler;
        this.mJWSBuilder = mjwsBuilder;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append("/oauth2/token");
        this.setTokenEndpoint(sb.toString());
    }
    
    public static String decodeProtocolState(final String s) throws UnsupportedEncodingException {
        if (!StringExtensions.isNullOrBlank(s)) {
            return new String(Base64.decode(s, 9), "UTF-8");
        }
        return null;
    }
    
    private static void extractJsonObjects(final Map<String, String> map, final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject(s);
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s2 = keys.next();
            map.put(s2, jsonObject.getString(s2));
        }
    }
    
    private Map<String, String> getRequestHeaders() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Accept", "application/json");
        return hashMap;
    }
    
    private AuthenticationResult parseJsonResponse(final String s) throws JSONException, AuthenticationException {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        extractJsonObjects(hashMap, s);
        return this.processUIResponseParams(hashMap);
    }
    
    private AuthenticationResult postMessage(final String s, final Map<String, String> map) throws IOException, AuthenticationException {
        Object o = this.startHttpEvent();
        final URL url = StringExtensions.getUrl(this.getTokenEndpoint());
        if (url != null) {
        Label_0603_Outer:
            while (true) {
                ((HttpEvent)o).setHttpPath(url);
                while (true) {
                Label_0927:
                    while (true) {
                        Label_0924: {
                            try {
                                try {
                                    this.mWebRequestHandler.setRequestCorrelationId(this.mRequest.getCorrelationId());
                                    ClientMetrics.INSTANCE.beginClientMetricsRecord(url, this.mRequest.getCorrelationId(), map);
                                    Object o2 = this.mWebRequestHandler.sendPost(url, map, s.getBytes("UTF_8"), "application/x-www-form-urlencoded");
                                    ((HttpEvent)o).setResponseCode(((HttpWebResponse)o2).getStatusCode());
                                    ((DefaultEvent)o).setCorrelationId(this.mRequest.getCorrelationId().toString());
                                    this.stopHttpEvent((HttpEvent)o);
                                    if (((HttpWebResponse)o2).getStatusCode() != 401) {
                                        break Label_0924;
                                    }
                                    if (((HttpWebResponse)o2).getResponseHeaders() == null || !((HttpWebResponse)o2).getResponseHeaders().containsKey("WWW-Authenticate")) {
                                        Logger.v("Oauth:postMessage", "401 http status code is returned without authorization header.");
                                        break Label_0924;
                                    }
                                    final String s2 = ((HttpWebResponse)o2).getResponseHeaders().get("WWW-Authenticate").get(0);
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Challenge header: ");
                                    sb.append(s2);
                                    Logger.i("Oauth:postMessage", "Device certificate challenge request. ", sb.toString());
                                    if (StringExtensions.isNullOrBlank(s2)) {
                                        throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Challenge header is empty", (HttpWebResponse)o2);
                                    }
                                    if (!StringExtensions.hasPrefixInHeader(s2, "PKeyAuth")) {
                                        break Label_0924;
                                    }
                                    final HttpEvent startHttpEvent = this.startHttpEvent();
                                    startHttpEvent.setHttpPath(url);
                                    Logger.v("Oauth:postMessage", "Received pkeyAuth device challenge.");
                                    o2 = new ChallengeResponseBuilder(this.mJWSBuilder);
                                    Logger.v("Oauth:postMessage", "Processing device challenge.");
                                    map.put("Authorization", ((ChallengeResponseBuilder)o2).getChallengeResponseFromHeader(s2, url.toString()).getAuthorizationHeaderValue());
                                    Logger.v("Oauth:postMessage", "Sending request with challenge response.");
                                    o2 = this.mWebRequestHandler.sendPost(url, map, s.getBytes("UTF_8"), "application/x-www-form-urlencoded");
                                    startHttpEvent.setResponseCode(((HttpWebResponse)o2).getStatusCode());
                                    startHttpEvent.setCorrelationId(this.mRequest.getCorrelationId().toString());
                                    this.stopHttpEvent(startHttpEvent);
                                    final boolean empty = TextUtils.isEmpty((CharSequence)((HttpWebResponse)o2).getBody());
                                    if (empty) {
                                        break Label_0927;
                                    }
                                    Logger.v("Oauth:postMessage", "Token request does not have exception.");
                                    try {
                                        o = this.processTokenResponse((HttpWebResponse)o2, (HttpEvent)o);
                                        ClientMetrics.INSTANCE.setLastError(null);
                                    }
                                    catch (ServerRespondingWithRetryableException o) {
                                        final AuthenticationResult retry = this.retry(s, map);
                                        if (retry != null) {
                                            ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                                            return retry;
                                        }
                                        if (this.mRequest.getIsExtendedLifetimeEnabled()) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("WebResponse is not a success due to: ");
                                            sb2.append(((HttpWebResponse)o2).getStatusCode());
                                            Logger.v("Oauth:postMessage", sb2.toString());
                                            throw o;
                                        }
                                        o = new StringBuilder();
                                        ((StringBuilder)o).append("WebResponse is not a success due to: ");
                                        ((StringBuilder)o).append(((HttpWebResponse)o2).getStatusCode());
                                        Logger.v("Oauth:postMessage", ((StringBuilder)o).toString());
                                        o = ADALError.SERVER_ERROR;
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append("WebResponse is not a success due to: ");
                                        sb3.append(((HttpWebResponse)o2).getStatusCode());
                                        throw new AuthenticationException((ADALError)o, sb3.toString(), (HttpWebResponse)o2);
                                    }
                                    if (o == null) {
                                        if (empty) {
                                            o = new StringBuilder();
                                            ((StringBuilder)o).append("Status code:");
                                            ((StringBuilder)o).append(((HttpWebResponse)o2).getStatusCode());
                                            o = ((StringBuilder)o).toString();
                                        }
                                        else {
                                            o = ((HttpWebResponse)o2).getBody();
                                        }
                                        Logger.e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), (String)o, ADALError.SERVER_ERROR);
                                        throw new AuthenticationException(ADALError.SERVER_ERROR, (String)o, (HttpWebResponse)o2);
                                    }
                                    ClientMetrics.INSTANCE.setLastErrorCodes(((AuthenticationResult)o).getErrorCodes());
                                    ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                                    return (AuthenticationResult)o;
                                }
                                finally {}
                            }
                            catch (IOException ex) {
                                ClientMetrics.INSTANCE.setLastError(null);
                                Logger.e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), ex.getMessage(), ADALError.SERVER_ERROR, ex);
                                throw ex;
                            }
                            catch (SocketTimeoutException ex2) {
                                final AuthenticationResult retry2 = this.retry(s, map);
                                if (retry2 != null) {
                                    ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                                    return retry2;
                                }
                                ClientMetrics.INSTANCE.setLastError(null);
                                if (this.mRequest.getIsExtendedLifetimeEnabled()) {
                                    Logger.e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), ex2.getMessage(), ADALError.SERVER_ERROR, ex2);
                                    throw new ServerRespondingWithRetryableException(ex2.getMessage(), ex2);
                                }
                                Logger.e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), ex2.getMessage(), ADALError.SERVER_ERROR, ex2);
                                throw ex2;
                            }
                            catch (UnsupportedEncodingException ex3) {
                                ClientMetrics.INSTANCE.setLastError(null);
                                Logger.e("Oauth:postMessage", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), ex3.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, ex3);
                                throw ex3;
                            }
                            break;
                        }
                        continue Label_0603_Outer;
                    }
                    o = null;
                    continue;
                }
            }
            ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
            throw s;
        }
        this.stopHttpEvent((HttpEvent)o);
        throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
    }
    
    private AuthenticationResult processTokenResponse(final HttpWebResponse httpResponse, final HttpEvent httpEvent) throws AuthenticationException {
        final Map<String, List<String>> responseHeaders = ((HttpWebResponse)httpResponse).getResponseHeaders();
        final String s = null;
        String s3;
        String speRing;
        if (responseHeaders != null) {
            String s2 = null;
            Label_0080: {
                if (((HttpWebResponse)httpResponse).getResponseHeaders().containsKey("client-request-id")) {
                    final List<String> list = ((HttpWebResponse)httpResponse).getResponseHeaders().get("client-request-id");
                    if (list != null && list.size() > 0) {
                        s2 = list.get(0);
                        break Label_0080;
                    }
                }
                s2 = null;
            }
            if (((HttpWebResponse)httpResponse).getResponseHeaders().containsKey("x-ms-request-id")) {
                final List<String> list2 = ((HttpWebResponse)httpResponse).getResponseHeaders().get("x-ms-request-id");
                if (list2 != null && list2.size() > 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Set request id header. x-ms-request-id: ");
                    sb.append(list2.get(0));
                    Logger.v("Oauth:processTokenResponse", sb.toString());
                    httpEvent.setRequestIdHeader(list2.get(0));
                }
            }
            s3 = s2;
            speRing = s;
            if (((HttpWebResponse)httpResponse).getResponseHeaders().get("x-ms-clitelem") != null) {
                s3 = s2;
                speRing = s;
                if (!((HttpWebResponse)httpResponse).getResponseHeaders().get("x-ms-clitelem").isEmpty()) {
                    final TelemetryUtils.CliTelemInfo xMsCliTelemHeader = TelemetryUtils.parseXMsCliTelemHeader(((HttpWebResponse)httpResponse).getResponseHeaders().get("x-ms-clitelem").get(0));
                    s3 = s2;
                    speRing = s;
                    if (xMsCliTelemHeader != null) {
                        httpEvent.setXMsCliTelemData(xMsCliTelemHeader);
                        speRing = xMsCliTelemHeader.getSpeRing();
                        s3 = s2;
                    }
                }
            }
        }
        else {
            s3 = null;
            speRing = s;
        }
        final int statusCode = ((HttpWebResponse)httpResponse).getStatusCode();
        if (statusCode != 200 && statusCode != 400) {
            if (statusCode != 401) {
                if (statusCode >= 500 && statusCode <= 599) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Server Error ");
                    sb2.append(statusCode);
                    sb2.append(" ");
                    sb2.append(((HttpWebResponse)httpResponse).getBody());
                    throw new ServerRespondingWithRetryableException(sb2.toString(), (HttpWebResponse)httpResponse);
                }
                final ADALError server_ERROR = ADALError.SERVER_ERROR;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unexpected server response ");
                sb3.append(statusCode);
                sb3.append(" ");
                sb3.append(((HttpWebResponse)httpResponse).getBody());
                throw new AuthenticationException(server_ERROR, sb3.toString(), (HttpWebResponse)httpResponse);
            }
        }
        try {
            final AuthenticationResult jsonResponse = this.parseJsonResponse(((HttpWebResponse)httpResponse).getBody());
            if (jsonResponse != null) {
                if (jsonResponse.getErrorCode() != null) {
                    jsonResponse.setHttpResponse((HttpWebResponse)httpResponse);
                }
                final TelemetryUtils.CliTelemInfo cliTelemInfo = new TelemetryUtils.CliTelemInfo();
                cliTelemInfo.setSpeRing(speRing);
                jsonResponse.setCliTelemInfo(cliTelemInfo);
                httpEvent.setOauthErrorCode(jsonResponse.getErrorCode());
            }
            if (s3 != null && !s3.isEmpty()) {
                try {
                    if (!UUID.fromString(s3).equals(this.mRequest.getCorrelationId())) {
                        Logger.w("Oauth:processTokenResponse", "CorrelationId is not matching", "", ADALError.CORRELATION_ID_NOT_MATCHING_REQUEST_RESPONSE);
                    }
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Response correlationId:");
                    sb4.append(s3);
                    Logger.v("Oauth:processTokenResponse", sb4.toString());
                    return jsonResponse;
                }
                catch (IllegalArgumentException httpResponse) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("Wrong format of the correlation ID:");
                    sb5.append(s3);
                    Logger.e("Oauth:processTokenResponse", sb5.toString(), "", ADALError.CORRELATION_ID_FORMAT, httpResponse);
                }
            }
            return jsonResponse;
        }
        catch (JSONException ex) {
            final ADALError server_INVALID_JSON_RESPONSE = ADALError.SERVER_INVALID_JSON_RESPONSE;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Can't parse server response. ");
            sb6.append(((HttpWebResponse)httpResponse).getBody());
            throw new AuthenticationException(server_INVALID_JSON_RESPONSE, sb6.toString(), (HttpWebResponse)httpResponse, (Throwable)ex);
        }
    }
    
    private AuthenticationResult retry(final String s, final Map<String, String> map) throws IOException, AuthenticationException {
        if (this.mRetryOnce) {
            this.mRetryOnce = false;
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ex) {
                Logger.v("Oauth:retry", "The thread is interrupted while it is sleeping. ");
            }
            Logger.v("Oauth:retry", "Try again...");
            return this.postMessage(s, map);
        }
        return null;
    }
    
    private void setTokenEndpoint(final String mTokenEndpoint) {
        this.mTokenEndpoint = mTokenEndpoint;
    }
    
    private HttpEvent startHttpEvent() {
        final HttpEvent httpEvent = new HttpEvent("Microsoft.ADAL.http_event");
        httpEvent.setRequestId(this.mRequest.getTelemetryRequestId());
        httpEvent.setMethod("Microsoft.ADAL.post");
        Telemetry.getInstance().startEvent(this.mRequest.getTelemetryRequestId(), "Microsoft.ADAL.http_event");
        return httpEvent;
    }
    
    private void stopHttpEvent(final HttpEvent httpEvent) {
        Telemetry.getInstance().stopEvent(this.mRequest.getTelemetryRequestId(), httpEvent, "Microsoft.ADAL.http_event");
    }
    
    public String buildRefreshTokenRequestMessage(String s) throws UnsupportedEncodingException {
        Logger.v("Oauth", "Building request message for redeeming token with refresh token.");
        final String s2 = s = String.format("%s=%s&%s=%s&%s=%s", "grant_type", StringExtensions.urlFormEncode("refresh_token"), "refresh_token", StringExtensions.urlFormEncode(s), "client_id", StringExtensions.urlFormEncode(this.mRequest.getClientId()));
        if (!StringExtensions.isNullOrBlank(this.mRequest.getResource())) {
            s = String.format("%s&%s=%s", s2, "resource", StringExtensions.urlFormEncode(this.mRequest.getResource()));
        }
        return s;
    }
    
    public String buildTokenRequestMessage(final String s) throws UnsupportedEncodingException {
        Logger.v("Oauth", "Building request message for redeeming token with auth code.");
        return String.format("%s=%s&%s=%s&%s=%s&%s=%s", "grant_type", StringExtensions.urlFormEncode("authorization_code"), "code", StringExtensions.urlFormEncode(s), "client_id", StringExtensions.urlFormEncode(this.mRequest.getClientId()), "redirect_uri", StringExtensions.urlFormEncode(this.mRequest.getRedirectUri()));
    }
    
    public String encodeProtocolState() throws UnsupportedEncodingException {
        return Base64.encodeToString(String.format("a=%s&r=%s", this.mRequest.getAuthority(), this.mRequest.getResource()).getBytes("UTF-8"), 9);
    }
    
    public String getAuthorizationEndpoint() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append("/oauth2/authorize");
        return sb.toString();
    }
    
    public String getAuthorizationEndpointQueryParameters() throws UnsupportedEncodingException {
        final Uri$Builder uri$Builder = new Uri$Builder();
        uri$Builder.appendQueryParameter("response_type", "code").appendQueryParameter("client_id", URLEncoder.encode(this.mRequest.getClientId(), "UTF_8")).appendQueryParameter("resource", URLEncoder.encode(this.mRequest.getResource(), "UTF_8")).appendQueryParameter("redirect_uri", URLEncoder.encode(this.mRequest.getRedirectUri(), "UTF_8")).appendQueryParameter("state", this.encodeProtocolState());
        if (!StringExtensions.isNullOrBlank(this.mRequest.getLoginHint())) {
            uri$Builder.appendQueryParameter("login_hint", URLEncoder.encode(this.mRequest.getLoginHint(), "UTF_8"));
        }
        uri$Builder.appendQueryParameter("x-client-SKU", "Android").appendQueryParameter("x-client-Ver", URLEncoder.encode(AuthenticationContext.getVersionName(), "UTF_8")).appendQueryParameter("x-client-OS", URLEncoder.encode(String.valueOf(Build$VERSION.SDK_INT), "UTF_8")).appendQueryParameter("x-client-DM", URLEncoder.encode(Build.MODEL, "UTF_8"));
        if (this.mRequest.getCorrelationId() != null) {
            uri$Builder.appendQueryParameter("client-request-id", URLEncoder.encode(this.mRequest.getCorrelationId().toString(), "UTF_8"));
        }
        Label_0259: {
            String s;
            if (this.mRequest.getPrompt() == PromptBehavior.Always) {
                s = "login";
            }
            else {
                if (this.mRequest.getPrompt() != PromptBehavior.REFRESH_SESSION) {
                    break Label_0259;
                }
                s = "refresh_session";
            }
            uri$Builder.appendQueryParameter("prompt", URLEncoder.encode(s, "UTF_8"));
        }
        final String extraQueryParamsAuthentication = this.mRequest.getExtraQueryParamsAuthentication();
        if (StringExtensions.isNullOrBlank(extraQueryParamsAuthentication) || !extraQueryParamsAuthentication.contains("haschrome")) {
            uri$Builder.appendQueryParameter("haschrome", "1");
        }
        if (!StringExtensions.isNullOrBlank(this.mRequest.getClaimsChallenge())) {
            uri$Builder.appendQueryParameter("claims", this.mRequest.getClaimsChallenge());
        }
        String s2 = uri$Builder.build().getQuery();
        if (!StringExtensions.isNullOrBlank(extraQueryParamsAuthentication)) {
            String string = extraQueryParamsAuthentication;
            if (!extraQueryParamsAuthentication.startsWith("&")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("&");
                sb.append(extraQueryParamsAuthentication);
                string = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s2);
            sb2.append(string);
            s2 = sb2.toString();
        }
        return s2;
    }
    
    public String getCodeRequestUrl() throws UnsupportedEncodingException {
        return String.format("%s?%s", this.getAuthorizationEndpoint(), this.getAuthorizationEndpointQueryParameters());
    }
    
    public AuthenticationResult getToken(String authority) throws IOException, AuthenticationException {
        if (StringExtensions.isNullOrBlank(authority)) {
            throw new IllegalArgumentException("authorizationUrl");
        }
        final HashMap<String, String> urlParameters = StringExtensions.getUrlParameters(authority);
        final String decodeProtocolState = decodeProtocolState(urlParameters.get("state"));
        if (StringExtensions.isNullOrBlank(decodeProtocolState)) {
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_STATE);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("http://state/path?");
        sb.append(decodeProtocolState);
        final Uri parse = Uri.parse(sb.toString());
        final String queryParameter = parse.getQueryParameter("a");
        final String queryParameter2 = parse.getQueryParameter("r");
        if (StringExtensions.isNullOrBlank(queryParameter) || StringExtensions.isNullOrBlank(queryParameter2) || !queryParameter2.equalsIgnoreCase(this.mRequest.getResource())) {
            throw new AuthenticationException(ADALError.AUTH_FAILED_BAD_STATE);
        }
        final AuthenticationResult processUIResponseParams = this.processUIResponseParams(urlParameters);
        if (processUIResponseParams != null && processUIResponseParams.getCode() != null && !processUIResponseParams.getCode().isEmpty()) {
            final AuthenticationResult tokenForCode = this.getTokenForCode(processUIResponseParams.getCode());
            if (!StringExtensions.isNullOrBlank(processUIResponseParams.getAuthority())) {
                authority = processUIResponseParams.getAuthority();
            }
            else {
                authority = this.mRequest.getAuthority();
            }
            tokenForCode.setAuthority(authority);
            return tokenForCode;
        }
        return processUIResponseParams;
    }
    
    public String getTokenEndpoint() {
        return this.mTokenEndpoint;
    }
    
    public AuthenticationResult getTokenForCode(String buildTokenRequestMessage) throws IOException, AuthenticationException {
        if (this.mWebRequestHandler != null) {
            try {
                buildTokenRequestMessage = this.buildTokenRequestMessage(buildTokenRequestMessage);
                final Map<String, String> requestHeaders = this.getRequestHeaders();
                Logger.v("Oauth:getTokenForCode", "Sending request to redeem token with auth code.");
                return this.postMessage(buildTokenRequestMessage, requestHeaders);
            }
            catch (UnsupportedEncodingException ex) {
                Logger.e("Oauth:getTokenForCode", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), ex.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, ex);
                return null;
            }
        }
        throw new IllegalArgumentException("webRequestHandler");
    }
    
    public AuthenticationResult processUIResponseParams(final Map<String, String> map) throws AuthenticationException {
        if (map.containsKey("error")) {
            final String s = map.get("correlation_id");
            if (!StringExtensions.isNullOrBlank(s)) {
                try {
                    Logger.setCorrelationId(UUID.fromString(s));
                }
                catch (IllegalArgumentException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("CorrelationId is malformed: ");
                    sb.append(s);
                    Logger.e("Oauth", sb.toString(), "", ADALError.CORRELATION_ID_FORMAT);
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("OAuth2 error:");
            sb2.append(map.get("error"));
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(" Description:");
            sb3.append(map.get("error_description"));
            Logger.i("Oauth", string, sb3.toString());
            return new AuthenticationResult(map.get("error"), map.get("error_description"), map.get("error_codes"));
        }
        final boolean containsKey = map.containsKey("code");
        AuthenticationResult authenticationResult = null;
        String familyClientId = null;
        if (containsKey) {
            final AuthenticationResult authenticationResult2 = new AuthenticationResult(map.get("code"));
            final String s2 = map.get("cloud_instance_host_name");
            authenticationResult = authenticationResult2;
            if (!StringExtensions.isNullOrBlank(s2)) {
                final String string2 = new Uri$Builder().scheme("https").authority(s2).path(StringExtensions.getUrl(this.mRequest.getAuthority()).getPath()).build().toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string2);
                sb4.append("/oauth2/token");
                this.setTokenEndpoint(sb4.toString());
                authenticationResult2.setAuthority(string2);
                return authenticationResult2;
            }
        }
        else if (map.containsKey("access_token")) {
            final String s3 = map.get("expires_in");
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            final int n = 3600;
            int int1;
            if (s3 != null && !s3.isEmpty()) {
                int1 = Integer.parseInt(s3);
            }
            else {
                int1 = 3600;
            }
            gregorianCalendar.add(13, int1);
            final String s4 = map.get("refresh_token");
            final boolean b = map.containsKey("resource") && !StringExtensions.isNullOrBlank(s4);
            Serializable s5;
            String tenantId;
            Serializable s6;
            if (map.containsKey("id_token")) {
                s5 = map.get("id_token");
                if (!StringExtensions.isNullOrBlank((String)s5)) {
                    Logger.v("Oauth", "Id token was returned, parsing id token.");
                    final IdToken idToken = new IdToken((String)s5);
                    tenantId = idToken.getTenantId();
                    s6 = new UserInfo(idToken);
                }
                else {
                    Logger.v("Oauth", "IdToken was not returned from token request.");
                    s6 = (tenantId = null);
                }
            }
            else {
                s6 = null;
                tenantId = (String)(s5 = s6);
            }
            if (map.containsKey("foci")) {
                familyClientId = map.get("foci");
            }
            authenticationResult = new AuthenticationResult(map.get("access_token"), s4, gregorianCalendar.getTime(), b, (UserInfo)s6, tenantId, (String)s5, null);
            if (map.containsKey("ext_expires_in")) {
                final String s7 = map.get("ext_expires_in");
                final GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                int int2;
                if (StringExtensions.isNullOrBlank(s7)) {
                    int2 = n;
                }
                else {
                    int2 = Integer.parseInt(s7);
                }
                gregorianCalendar2.add(13, int2);
                authenticationResult.setExtendedExpiresOn(gregorianCalendar2.getTime());
            }
            authenticationResult.setFamilyClientId(familyClientId);
        }
        return authenticationResult;
    }
    
    public AuthenticationResult refreshToken(String buildRefreshTokenRequestMessage) throws IOException, AuthenticationException {
        if (this.mWebRequestHandler != null) {
            try {
                buildRefreshTokenRequestMessage = this.buildRefreshTokenRequestMessage(buildRefreshTokenRequestMessage);
                final Map<String, String> requestHeaders = this.getRequestHeaders();
                requestHeaders.put("x-ms-PKeyAuth", "1.0");
                Logger.v("Oauth", "Sending request to redeem token with refresh token.");
                return this.postMessage(buildRefreshTokenRequestMessage, requestHeaders);
            }
            catch (UnsupportedEncodingException ex) {
                Logger.e("Oauth", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), ex.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, ex);
                return null;
            }
        }
        Logger.v("Oauth", "Web request is not set correctly.");
        throw new IllegalArgumentException("webRequestHandler is null.");
    }
}
