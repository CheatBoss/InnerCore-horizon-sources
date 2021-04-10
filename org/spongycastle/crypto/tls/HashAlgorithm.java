package org.spongycastle.crypto.tls;

public class HashAlgorithm
{
    public static final short md5 = 1;
    public static final short none = 0;
    public static final short sha1 = 2;
    public static final short sha224 = 3;
    public static final short sha256 = 4;
    public static final short sha384 = 5;
    public static final short sha512 = 6;
    
    public static String getName(final short n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 6: {
                return "sha512";
            }
            case 5: {
                return "sha384";
            }
            case 4: {
                return "sha256";
            }
            case 3: {
                return "sha224";
            }
            case 2: {
                return "sha1";
            }
            case 1: {
                return "md5";
            }
            case 0: {
                return "none";
            }
        }
    }
    
    public static String getText(final short n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getName(n));
        sb.append("(");
        sb.append(n);
        sb.append(")");
        return sb.toString();
    }
    
    public static boolean isPrivate(final short n) {
        return 224 <= n && n <= 255;
    }
}
