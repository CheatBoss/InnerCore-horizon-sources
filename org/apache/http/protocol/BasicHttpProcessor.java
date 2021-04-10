package org.apache.http.protocol;

import java.util.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public final class BasicHttpProcessor implements HttpProcessor, HttpRequestInterceptorList, HttpResponseInterceptorList
{
    protected List requestInterceptors;
    protected List responseInterceptors;
    
    public BasicHttpProcessor() {
        throw new RuntimeException("Stub!");
    }
    
    public final void addInterceptor(final HttpRequestInterceptor httpRequestInterceptor) {
        throw new RuntimeException("Stub!");
    }
    
    public final void addInterceptor(final HttpRequestInterceptor httpRequestInterceptor, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public final void addInterceptor(final HttpResponseInterceptor httpResponseInterceptor) {
        throw new RuntimeException("Stub!");
    }
    
    public final void addInterceptor(final HttpResponseInterceptor httpResponseInterceptor, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addRequestInterceptor(final HttpRequestInterceptor httpRequestInterceptor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addRequestInterceptor(final HttpRequestInterceptor httpRequestInterceptor, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addResponseInterceptor(final HttpResponseInterceptor httpResponseInterceptor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addResponseInterceptor(final HttpResponseInterceptor httpResponseInterceptor, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void clearInterceptors() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void clearRequestInterceptors() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void clearResponseInterceptors() {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpProcessor copy() {
        throw new RuntimeException("Stub!");
    }
    
    protected void copyInterceptors(final BasicHttpProcessor basicHttpProcessor) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRequestInterceptor getRequestInterceptor(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getRequestInterceptorCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponseInterceptor getResponseInterceptor(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getResponseInterceptorCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpRequest httpRequest, final HttpContext httpContext) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void process(final HttpResponse httpResponse, final HttpContext httpContext) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void removeRequestInterceptorByClass(final Class clazz) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void removeResponseInterceptorByClass(final Class clazz) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setInterceptors(final List list) {
        throw new RuntimeException("Stub!");
    }
}
