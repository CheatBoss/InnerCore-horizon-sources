package org.spongycastle.crypto.tls;

public class HeartbeatMessageType
{
    public static final short heartbeat_request = 1;
    public static final short heartbeat_response = 2;
    
    public static boolean isValid(final short n) {
        return n >= 1 && n <= 2;
    }
}
