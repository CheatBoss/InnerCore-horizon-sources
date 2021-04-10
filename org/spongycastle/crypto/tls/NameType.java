package org.spongycastle.crypto.tls;

public class NameType
{
    public static final short host_name = 0;
    
    public static boolean isValid(final short n) {
        return n == 0;
    }
}
