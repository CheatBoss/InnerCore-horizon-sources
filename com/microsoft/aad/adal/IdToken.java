package com.microsoft.aad.adal;

import java.util.*;
import android.util.*;
import org.json.*;
import java.io.*;

class IdToken
{
    private static final String TAG = "IdToken";
    private String mEmail;
    private String mFamilyName;
    private String mGivenName;
    private String mIdentityProvider;
    private String mObjectId;
    private String mPasswordChangeUrl;
    private long mPasswordExpiration;
    private String mSubject;
    private String mTenantId;
    private String mUpn;
    
    public IdToken(final String s) throws AuthenticationException {
        final Map<String, String> jwt = this.parseJWT(s);
        if (jwt != null && !jwt.isEmpty()) {
            this.mSubject = jwt.get("sub");
            this.mTenantId = jwt.get("tid");
            this.mUpn = jwt.get("upn");
            this.mEmail = jwt.get("email");
            this.mGivenName = jwt.get("given_name");
            this.mFamilyName = jwt.get("family_name");
            this.mIdentityProvider = jwt.get("idp");
            this.mObjectId = jwt.get("oid");
            final String s2 = jwt.get("pwd_exp");
            if (!StringExtensions.isNullOrBlank(s2)) {
                this.mPasswordExpiration = Long.parseLong(s2);
            }
            this.mPasswordChangeUrl = jwt.get("pwd_url");
        }
    }
    
    private String extractJWTBody(final String s) throws AuthenticationException {
        final int index = s.indexOf(46);
        final int n = index + 1;
        final int index2 = s.indexOf(46, n);
        if (s.indexOf(46, index2 + 1) == -1 && index > 0 && index2 > 0) {
            return s.substring(n, index2);
        }
        throw new AuthenticationException(ADALError.IDTOKEN_PARSING_FAILURE, "Failed to extract the ClientID");
    }
    
    private Map<String, String> parseJWT(final String s) throws AuthenticationException {
        final byte[] decode = Base64.decode(this.extractJWTBody(s), 8);
        try {
            return HashMapExtensions.jsonStringAsMap(new String(decode, "UTF-8"));
        }
        catch (JSONException ex) {
            Logger.e("IdToken:parseJWT", "Failed to parse the decoded body into JsonObject.", "", ADALError.JSON_PARSE_ERROR, (Throwable)ex);
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR, ex.getMessage(), (Throwable)ex);
        }
        catch (UnsupportedEncodingException ex2) {
            Logger.e("IdToken:parseJWT", "The encoding is not supported.", "", ADALError.ENCODING_IS_NOT_SUPPORTED, ex2);
            throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, ex2.getMessage(), ex2);
        }
    }
    
    public String getEmail() {
        return this.mEmail;
    }
    
    public String getFamilyName() {
        return this.mFamilyName;
    }
    
    public String getGivenName() {
        return this.mGivenName;
    }
    
    public String getIdentityProvider() {
        return this.mIdentityProvider;
    }
    
    public String getObjectId() {
        return this.mObjectId;
    }
    
    public String getPasswordChangeUrl() {
        return this.mPasswordChangeUrl;
    }
    
    public long getPasswordExpiration() {
        return this.mPasswordExpiration;
    }
    
    public String getSubject() {
        return this.mSubject;
    }
    
    public String getTenantId() {
        return this.mTenantId;
    }
    
    public String getUpn() {
        return this.mUpn;
    }
}
