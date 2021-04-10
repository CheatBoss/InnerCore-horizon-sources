package org.apache.http.impl.client;

import org.apache.http.params.*;

@Deprecated
public class ClientParamsStack extends AbstractHttpParams
{
    protected final HttpParams applicationParams;
    protected final HttpParams clientParams;
    protected final HttpParams overrideParams;
    protected final HttpParams requestParams;
    
    public ClientParamsStack(final ClientParamsStack clientParamsStack) {
        throw new RuntimeException("Stub!");
    }
    
    public ClientParamsStack(final ClientParamsStack clientParamsStack, final HttpParams httpParams, final HttpParams httpParams2, final HttpParams httpParams3, final HttpParams httpParams4) {
        throw new RuntimeException("Stub!");
    }
    
    public ClientParamsStack(final HttpParams httpParams, final HttpParams httpParams2, final HttpParams httpParams3, final HttpParams httpParams4) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams copy() {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpParams getApplicationParams() {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpParams getClientParams() {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpParams getOverrideParams() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpParams getRequestParams() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean removeParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setParameter(final String s, final Object o) throws UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
}
