package org.apache.http.client;

import org.apache.http.auth.*;

@Deprecated
public interface CredentialsProvider
{
    void clear();
    
    Credentials getCredentials(final AuthScope p0);
    
    void setCredentials(final AuthScope p0, final Credentials p1);
}
