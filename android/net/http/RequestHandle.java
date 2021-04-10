package android.net.http;

import android.net.compatibility.*;
import java.util.*;
import java.io.*;

public class RequestHandle
{
    public static final int MAX_REDIRECT_COUNT = 16;
    
    public RequestHandle(final RequestQueue requestQueue, final String s, final WebAddress webAddress, final String s2, final Map<String, String> map, final InputStream inputStream, final int n, final Request request) {
        throw new RuntimeException("Stub!");
    }
    
    public RequestHandle(final RequestQueue requestQueue, final String s, final WebAddress webAddress, final String s2, final Map<String, String> map, final InputStream inputStream, final int n, final Request request, final Connection connection) {
        throw new RuntimeException("Stub!");
    }
    
    public static String authorizationHeader(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public static String computeBasicAuthResponse(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public void cancel() {
        throw new RuntimeException("Stub!");
    }
    
    public String getMethod() {
        throw new RuntimeException("Stub!");
    }
    
    public int getRedirectCount() {
        throw new RuntimeException("Stub!");
    }
    
    public void handleSslErrorResponse(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isRedirectMax() {
        throw new RuntimeException("Stub!");
    }
    
    public void pauseRequest(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void processRequest() {
        throw new RuntimeException("Stub!");
    }
    
    public void setRedirectCount(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setupBasicAuthResponse(final boolean b, final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public void setupDigestAuthResponse(final boolean b, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean setupRedirect(final String s, final int n, final Map<String, String> map) {
        throw new RuntimeException("Stub!");
    }
    
    public void waitUntilComplete() {
        throw new RuntimeException("Stub!");
    }
}
