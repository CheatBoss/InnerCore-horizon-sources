package org.spongycastle.x509;

import java.security.cert.*;
import java.util.*;

public abstract class PKIXAttrCertChecker implements Cloneable
{
    public abstract void check(final X509AttributeCertificate p0, final CertPath p1, final CertPath p2, final Collection p3) throws CertPathValidatorException;
    
    public abstract Object clone();
    
    public abstract Set getSupportedExtensions();
}
