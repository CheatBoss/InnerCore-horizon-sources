package org.spongycastle.crypto.tls;

public class FiniteFieldDHEGroup
{
    public static final short ffdhe2432 = 0;
    public static final short ffdhe3072 = 1;
    public static final short ffdhe4096 = 2;
    public static final short ffdhe6144 = 3;
    public static final short ffdhe8192 = 4;
    
    public static boolean isValid(final short n) {
        return n >= 0 && n <= 4;
    }
}
