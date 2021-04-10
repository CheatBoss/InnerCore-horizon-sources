package org.apache.http.client.params;

import org.apache.http.params.*;
import org.apache.http.conn.*;
import java.util.*;
import org.apache.http.*;

@Deprecated
public class ClientParamBean extends HttpAbstractParamBean
{
    public ClientParamBean(final HttpParams httpParams) {
        super(null);
        throw new RuntimeException("Stub!");
    }
    
    public void setAllowCircularRedirects(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setConnectionManagerFactory(final ClientConnectionManagerFactory clientConnectionManagerFactory) {
        throw new RuntimeException("Stub!");
    }
    
    public void setConnectionManagerFactoryClassName(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setCookiePolicy(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setDefaultHeaders(final Collection<Header> collection) {
        throw new RuntimeException("Stub!");
    }
    
    public void setDefaultHost(final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
    
    public void setHandleAuthentication(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setHandleRedirects(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setMaxRedirects(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void setRejectRelativeRedirect(final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setVirtualHost(final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
}
