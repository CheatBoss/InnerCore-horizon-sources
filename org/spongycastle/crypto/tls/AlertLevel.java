package org.spongycastle.crypto.tls;

public class AlertLevel
{
    public static final short fatal = 2;
    public static final short warning = 1;
    
    public static String getName(final short n) {
        if (n == 1) {
            return "warning";
        }
        if (n != 2) {
            return "UNKNOWN";
        }
        return "fatal";
    }
    
    public static String getText(final short n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getName(n));
        sb.append("(");
        sb.append(n);
        sb.append(")");
        return sb.toString();
    }
}
