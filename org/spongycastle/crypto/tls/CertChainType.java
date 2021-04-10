package org.spongycastle.crypto.tls;

public class CertChainType
{
    public static final short individual_certs = 0;
    public static final short pkipath = 1;
    
    public static boolean isValid(final short n) {
        return n >= 0 && n <= 1;
    }
}
