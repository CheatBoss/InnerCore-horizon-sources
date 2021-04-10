package android.net.http;

import android.content.*;
import org.apache.http.*;
import android.net.compatibility.*;
import java.util.*;
import java.io.*;

public class RequestQueue implements RequestFeeder
{
    public RequestQueue(final Context context) {
        throw new RuntimeException("Stub!");
    }
    
    public RequestQueue(final Context context, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void disablePlatformNotifications() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void enablePlatformNotifications() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public HttpHost getProxyHost() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Request getRequest() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public Request getRequest(final HttpHost httpHost) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public boolean haveRequest(final HttpHost httpHost) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public RequestHandle queueRequest(final String s, final WebAddress webAddress, final String s2, final Map<String, String> map, final EventHandler eventHandler, final InputStream inputStream, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public RequestHandle queueRequest(final String s, final String s2, final Map<String, String> map, final EventHandler eventHandler, final InputStream inputStream, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    protected void queueRequest(final Request request, final boolean b) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public RequestHandle queueSynchronousRequest(final String s, final WebAddress webAddress, final String s2, final Map<String, String> map, final EventHandler eventHandler, final InputStream inputStream, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void requeueRequest(final Request request) {
        throw new RuntimeException("Stub!");
    }
    
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
    
    public void startTiming() {
        throw new RuntimeException("Stub!");
    }
    
    public void stopTiming() {
        throw new RuntimeException("Stub!");
    }
}
