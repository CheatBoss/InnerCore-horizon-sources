package com.microsoft.aad.adal;

import java.security.interfaces.*;
import java.security.cert.*;

public interface IJWSBuilder
{
    String generateSignedJWT(final String p0, final String p1, final RSAPrivateKey p2, final RSAPublicKey p3, final X509Certificate p4) throws AuthenticationException;
}
