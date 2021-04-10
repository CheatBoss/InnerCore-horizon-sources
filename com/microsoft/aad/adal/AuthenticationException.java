package com.microsoft.aad.adal;

import java.util.*;
import android.content.*;
import org.json.*;

public class AuthenticationException extends Exception
{
    static final long serialVersionUID = 1L;
    private ADALError mCode;
    private HashMap<String, String> mHttpResponseBody;
    private HashMap<String, List<String>> mHttpResponseHeaders;
    private int mServiceStatusCode;
    
    public AuthenticationException() {
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
    }
    
    public AuthenticationException(final ADALError mCode) {
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = mCode;
    }
    
    public AuthenticationException(final ADALError mCode, final String s) {
        super(s);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = mCode;
    }
    
    public AuthenticationException(final ADALError mCode, final String s, final HttpWebResponse httpResponse) {
        super(s);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = mCode;
        this.setHttpResponse(httpResponse);
    }
    
    public AuthenticationException(final ADALError adalError, final String s, final HttpWebResponse httpResponse, final Throwable t) {
        this(adalError, s, t);
        this.setHttpResponse(httpResponse);
    }
    
    public AuthenticationException(final ADALError mCode, final String s, final Throwable t) {
        super(s, t);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = mCode;
        if (t == null) {
            return;
        }
        if (t instanceof AuthenticationException) {
            final AuthenticationException ex = (AuthenticationException)t;
            this.mServiceStatusCode = ex.getServiceStatusCode();
            if (ex.getHttpResponseBody() != null) {
                this.mHttpResponseBody = new HashMap<String, String>(ex.getHttpResponseBody());
            }
            if (ex.getHttpResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<String, List<String>>(ex.getHttpResponseHeaders());
            }
        }
    }
    
    public ADALError getCode() {
        return this.mCode;
    }
    
    public HashMap<String, String> getHttpResponseBody() {
        return this.mHttpResponseBody;
    }
    
    public HashMap<String, List<String>> getHttpResponseHeaders() {
        return this.mHttpResponseHeaders;
    }
    
    public String getLocalizedMessage(final Context context) {
        if (!StringExtensions.isNullOrBlank(super.getMessage())) {
            return super.getMessage();
        }
        final ADALError mCode = this.mCode;
        if (mCode != null) {
            return mCode.getLocalizedDescription(context);
        }
        return null;
    }
    
    @Override
    public String getMessage() {
        return this.getLocalizedMessage(null);
    }
    
    public int getServiceStatusCode() {
        return this.mServiceStatusCode;
    }
    
    void setHttpResponse(final AuthenticationResult authenticationResult) {
        if (authenticationResult != null) {
            this.mHttpResponseBody = authenticationResult.getHttpResponseBody();
            this.mHttpResponseHeaders = authenticationResult.getHttpResponseHeaders();
            this.mServiceStatusCode = authenticationResult.getServiceStatusCode();
        }
    }
    
    void setHttpResponse(final HttpWebResponse httpWebResponse) {
        if (httpWebResponse != null) {
            this.mServiceStatusCode = httpWebResponse.getStatusCode();
            if (httpWebResponse.getResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<String, List<String>>(httpWebResponse.getResponseHeaders());
            }
            if (httpWebResponse.getBody() != null) {
                try {
                    this.mHttpResponseBody = new HashMap<String, String>(HashMapExtensions.getJsonResponse(httpWebResponse));
                }
                catch (JSONException ex) {
                    Logger.e(AuthenticationException.class.getSimpleName(), "Json exception", ExceptionExtensions.getExceptionMessage((Exception)ex), ADALError.SERVER_INVALID_JSON_RESPONSE);
                }
            }
        }
    }
    
    void setHttpResponseBody(final HashMap<String, String> mHttpResponseBody) {
        this.mHttpResponseBody = mHttpResponseBody;
    }
    
    void setHttpResponseHeaders(final HashMap<String, List<String>> mHttpResponseHeaders) {
        this.mHttpResponseHeaders = mHttpResponseHeaders;
    }
    
    void setServiceStatusCode(final int mServiceStatusCode) {
        this.mServiceStatusCode = mServiceStatusCode;
    }
}
