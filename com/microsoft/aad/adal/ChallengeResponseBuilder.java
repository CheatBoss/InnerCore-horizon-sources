package com.microsoft.aad.adal;

import java.util.*;
import java.io.*;
import java.security.interfaces.*;
import java.lang.reflect.*;

class ChallengeResponseBuilder
{
    private static final String TAG = "ChallengeResponseBuilder";
    private final IJWSBuilder mJWSBuilder;
    
    ChallengeResponseBuilder(final IJWSBuilder mjwsBuilder) {
        this.mJWSBuilder = mjwsBuilder;
    }
    
    private ChallengeRequest getChallengeRequest(final String s) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(s)) {
            final ChallengeRequest challengeRequest = new ChallengeRequest();
            final HashMap<String, String> urlParameters = StringExtensions.getUrlParameters(s);
            this.validateChallengeRequest(urlParameters, true);
            challengeRequest.mNonce = urlParameters.get(RequestField.Nonce.name());
            if (StringExtensions.isNullOrBlank(challengeRequest.mNonce)) {
                challengeRequest.mNonce = urlParameters.get(RequestField.Nonce.name().toLowerCase(Locale.US));
            }
            final String s2 = urlParameters.get(RequestField.CertAuthorities.name());
            final StringBuilder sb = new StringBuilder();
            sb.append("Authorities: ");
            sb.append(s2);
            Logger.v("ChallengeResponseBuilder:getChallengeRequest", "Get cert authorities. ", sb.toString(), null);
            challengeRequest.mCertAuthorities = StringExtensions.getStringTokens(s2, ";");
            challengeRequest.mVersion = urlParameters.get(RequestField.Version.name());
            challengeRequest.mSubmitUrl = urlParameters.get(RequestField.SubmitUrl.name());
            challengeRequest.mContext = urlParameters.get(RequestField.Context.name());
            return challengeRequest;
        }
        throw new AuthenticationServerProtocolException("redirectUri");
    }
    
    private ChallengeRequest getChallengeRequestFromHeader(String substring) throws UnsupportedEncodingException, AuthenticationException {
        if (StringExtensions.isNullOrBlank(substring)) {
            throw new AuthenticationServerProtocolException("headerValue");
        }
        if (StringExtensions.hasPrefixInHeader(substring, "PKeyAuth")) {
            final ChallengeRequest challengeRequest = new ChallengeRequest();
            substring = substring.substring(8);
            final ArrayList<String> splitWithQuotes = StringExtensions.splitWithQuotes(substring, ',');
            final HashMap<Object, String> hashMap = new HashMap<Object, String>();
            final Iterator<String> iterator = splitWithQuotes.iterator();
            while (iterator.hasNext()) {
                final ArrayList<String> splitWithQuotes2 = StringExtensions.splitWithQuotes(iterator.next(), '=');
                if (splitWithQuotes2.size() == 2 && !StringExtensions.isNullOrBlank(splitWithQuotes2.get(0)) && !StringExtensions.isNullOrBlank(splitWithQuotes2.get(1))) {
                    hashMap.put(StringExtensions.urlFormDecode(splitWithQuotes2.get(0)).trim(), StringExtensions.removeQuoteInHeaderValue(StringExtensions.urlFormDecode(splitWithQuotes2.get(1)).trim()));
                }
                else {
                    if (splitWithQuotes2.size() != 1 || StringExtensions.isNullOrBlank(splitWithQuotes2.get(0))) {
                        throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, substring);
                    }
                    hashMap.put(StringExtensions.urlFormDecode(splitWithQuotes2.get(0)).trim(), StringExtensions.urlFormDecode(""));
                }
            }
            this.validateChallengeRequest((Map<String, String>)hashMap, false);
            challengeRequest.mNonce = (String)hashMap.get(RequestField.Nonce.name());
            if (StringExtensions.isNullOrBlank(challengeRequest.mNonce)) {
                challengeRequest.mNonce = (String)hashMap.get(RequestField.Nonce.name().toLowerCase(Locale.US));
            }
            if (!this.isWorkplaceJoined()) {
                Logger.v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "Device is not workplace joined. ");
            }
            else if (!StringExtensions.isNullOrBlank((String)hashMap.get(RequestField.CertThumbprint.name()))) {
                Logger.v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "CertThumbprint exists in the device auth challenge.");
                challengeRequest.mThumbprint = (String)hashMap.get(RequestField.CertThumbprint.name());
            }
            else {
                if (!hashMap.containsKey(RequestField.CertAuthorities.name())) {
                    throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Both certThumbprint and certauthorities are not present");
                }
                Logger.v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "CertAuthorities exists in the device auth challenge.");
                challengeRequest.mCertAuthorities = StringExtensions.getStringTokens((String)hashMap.get(RequestField.CertAuthorities.name()), ";");
            }
            challengeRequest.mVersion = (String)hashMap.get(RequestField.Version.name());
            challengeRequest.mContext = (String)hashMap.get(RequestField.Context.name());
            return challengeRequest;
        }
        throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, substring);
    }
    
    private ChallengeResponse getDeviceCertResponse(final ChallengeRequest challengeRequest) throws AuthenticationException {
        final ChallengeResponse noDeviceCertResponse = this.getNoDeviceCertResponse(challengeRequest);
        noDeviceCertResponse.mSubmitUrl = challengeRequest.mSubmitUrl;
        final Class<?> deviceCertificateProxy = AuthenticationSettings.INSTANCE.getDeviceCertificateProxy();
        if (deviceCertificateProxy != null) {
            final IDeviceCertificate wpjapiInstance = this.getWPJAPIInstance((Class<IDeviceCertificate>)deviceCertificateProxy);
            if (wpjapiInstance.isValidIssuer(challengeRequest.mCertAuthorities) || (wpjapiInstance.getThumbPrint() != null && wpjapiInstance.getThumbPrint().equalsIgnoreCase(challengeRequest.mThumbprint))) {
                final RSAPrivateKey rsaPrivateKey = wpjapiInstance.getRSAPrivateKey();
                if (rsaPrivateKey != null) {
                    noDeviceCertResponse.mAuthorizationHeaderValue = String.format("%s AuthToken=\"%s\",Context=\"%s\",Version=\"%s\"", "PKeyAuth", this.mJWSBuilder.generateSignedJWT(challengeRequest.mNonce, challengeRequest.mSubmitUrl, rsaPrivateKey, wpjapiInstance.getRSAPublicKey(), wpjapiInstance.getCertificate()), challengeRequest.mContext, challengeRequest.mVersion);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Challenge response:");
                    sb.append(noDeviceCertResponse.mAuthorizationHeaderValue);
                    Logger.v("ChallengeResponseBuilder", "Receive challenge response. ", sb.toString(), null);
                    return noDeviceCertResponse;
                }
                throw new AuthenticationException(ADALError.KEY_CHAIN_PRIVATE_KEY_EXCEPTION);
            }
        }
        return noDeviceCertResponse;
    }
    
    private ChallengeResponse getNoDeviceCertResponse(final ChallengeRequest challengeRequest) {
        final ChallengeResponse challengeResponse = new ChallengeResponse();
        challengeResponse.mSubmitUrl = challengeRequest.mSubmitUrl;
        challengeResponse.mAuthorizationHeaderValue = String.format("%s Context=\"%s\",Version=\"%s\"", "PKeyAuth", challengeRequest.mContext, challengeRequest.mVersion);
        return challengeResponse;
    }
    
    private IDeviceCertificate getWPJAPIInstance(final Class<IDeviceCertificate> clazz) throws AuthenticationException {
        try {
            return clazz.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance((Object[])null);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            final Object o;
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_API_EXCEPTION, "WPJ Api constructor is not defined", (Throwable)o);
        }
    }
    
    private boolean isWorkplaceJoined() {
        return AuthenticationSettings.INSTANCE.getDeviceCertificateProxy() != null;
    }
    
    private void validateChallengeRequest(final Map<String, String> map, final boolean b) throws AuthenticationException {
        if (!map.containsKey(RequestField.Nonce.name()) && !map.containsKey(RequestField.Nonce.name().toLowerCase(Locale.US))) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Nonce");
        }
        if (!map.containsKey(RequestField.Version.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Version");
        }
        if (b && !map.containsKey(RequestField.SubmitUrl.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "SubmitUrl");
        }
        if (!map.containsKey(RequestField.Context.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Context");
        }
        if (!b) {
            return;
        }
        if (map.containsKey(RequestField.CertAuthorities.name())) {
            return;
        }
        throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "CertAuthorities");
    }
    
    public ChallengeResponse getChallengeResponseFromHeader(final String s, final String s2) throws UnsupportedEncodingException, AuthenticationException {
        final ChallengeRequest challengeRequestFromHeader = this.getChallengeRequestFromHeader(s);
        challengeRequestFromHeader.mSubmitUrl = s2;
        return this.getDeviceCertResponse(challengeRequestFromHeader);
    }
    
    public ChallengeResponse getChallengeResponseFromUri(final String s) throws AuthenticationException {
        return this.getDeviceCertResponse(this.getChallengeRequest(s));
    }
    
    class ChallengeRequest
    {
        private List<String> mCertAuthorities;
        private String mContext;
        private String mNonce;
        private String mSubmitUrl;
        private String mThumbprint;
        private String mVersion;
        
        ChallengeRequest() {
            this.mNonce = "";
            this.mContext = "";
            this.mThumbprint = "";
            this.mVersion = null;
            this.mSubmitUrl = "";
        }
    }
    
    class ChallengeResponse
    {
        private String mAuthorizationHeaderValue;
        private String mSubmitUrl;
        
        String getAuthorizationHeaderValue() {
            return this.mAuthorizationHeaderValue;
        }
        
        String getSubmitUrl() {
            return this.mSubmitUrl;
        }
        
        void setAuthorizationHeaderValue(final String mAuthorizationHeaderValue) {
            this.mAuthorizationHeaderValue = mAuthorizationHeaderValue;
        }
        
        void setSubmitUrl(final String mSubmitUrl) {
            this.mSubmitUrl = mSubmitUrl;
        }
    }
    
    enum RequestField
    {
        CertAuthorities, 
        CertThumbprint, 
        Context, 
        Nonce, 
        SubmitUrl, 
        Version;
    }
}
