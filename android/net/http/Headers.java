package android.net.http;

import java.util.*;
import org.apache.http.util.*;

public final class Headers
{
    public static final String ACCEPT_RANGES = "accept-ranges";
    public static final String CACHE_CONTROL = "cache-control";
    public static final int CONN_CLOSE = 1;
    public static final String CONN_DIRECTIVE = "connection";
    public static final int CONN_KEEP_ALIVE = 2;
    public static final String CONTENT_DISPOSITION = "content-disposition";
    public static final String CONTENT_ENCODING = "content-encoding";
    public static final String CONTENT_LEN = "content-length";
    public static final String CONTENT_TYPE = "content-type";
    public static final String ETAG = "etag";
    public static final String EXPIRES = "expires";
    public static final String LAST_MODIFIED = "last-modified";
    public static final String LOCATION = "location";
    public static final int NO_CONN_TYPE = 0;
    public static final long NO_CONTENT_LENGTH = -1L;
    public static final long NO_TRANSFER_ENCODING = 0L;
    public static final String PRAGMA = "pragma";
    public static final String PROXY_AUTHENTICATE = "proxy-authenticate";
    public static final String PROXY_CONNECTION = "proxy-connection";
    public static final String REFRESH = "refresh";
    public static final String SET_COOKIE = "set-cookie";
    public static final String TRANSFER_ENCODING = "transfer-encoding";
    public static final String WWW_AUTHENTICATE = "www-authenticate";
    public static final String X_PERMITTED_CROSS_DOMAIN_POLICIES = "x-permitted-cross-domain-policies";
    
    public Headers() {
        throw new RuntimeException("Stub!");
    }
    
    public String getAcceptRanges() {
        throw new RuntimeException("Stub!");
    }
    
    public String getCacheControl() {
        throw new RuntimeException("Stub!");
    }
    
    public int getConnectionType() {
        throw new RuntimeException("Stub!");
    }
    
    public String getContentDisposition() {
        throw new RuntimeException("Stub!");
    }
    
    public String getContentEncoding() {
        throw new RuntimeException("Stub!");
    }
    
    public long getContentLength() {
        throw new RuntimeException("Stub!");
    }
    
    public String getContentType() {
        throw new RuntimeException("Stub!");
    }
    
    public String getEtag() {
        throw new RuntimeException("Stub!");
    }
    
    public String getExpires() {
        throw new RuntimeException("Stub!");
    }
    
    public void getHeaders(final HeaderCallback headerCallback) {
        throw new RuntimeException("Stub!");
    }
    
    public String getLastModified() {
        throw new RuntimeException("Stub!");
    }
    
    public String getLocation() {
        throw new RuntimeException("Stub!");
    }
    
    public String getPragma() {
        throw new RuntimeException("Stub!");
    }
    
    public String getProxyAuthenticate() {
        throw new RuntimeException("Stub!");
    }
    
    public String getRefresh() {
        throw new RuntimeException("Stub!");
    }
    
    public ArrayList<String> getSetCookie() {
        throw new RuntimeException("Stub!");
    }
    
    public long getTransferEncoding() {
        throw new RuntimeException("Stub!");
    }
    
    public String getWwwAuthenticate() {
        throw new RuntimeException("Stub!");
    }
    
    public String getXPermittedCrossDomainPolicies() {
        throw new RuntimeException("Stub!");
    }
    
    public void parseHeader(final CharArrayBuffer charArrayBuffer) {
        throw new RuntimeException("Stub!");
    }
    
    public void setAcceptRanges(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setCacheControl(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentDisposition(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentEncoding(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentLength(final long n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setContentType(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setEtag(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setExpires(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setLastModified(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setLocation(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setProxyAuthenticate(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setWwwAuthenticate(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setXPermittedCrossDomainPolicies(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public interface HeaderCallback
    {
        void header(final String p0, final String p1);
    }
}
