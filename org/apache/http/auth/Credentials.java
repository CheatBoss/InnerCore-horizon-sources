package org.apache.http.auth;

import java.security.*;

@Deprecated
public interface Credentials
{
    String getPassword();
    
    Principal getUserPrincipal();
}
