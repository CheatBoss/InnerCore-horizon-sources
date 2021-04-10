package okhttp3;

import java.util.*;
import java.net.*;

public interface Dns
{
    public static final Dns SYSTEM = new Dns() {
        @Override
        public List<InetAddress> lookup(final String s) throws UnknownHostException {
            if (s != null) {
                try {
                    return Arrays.asList(InetAddress.getAllByName(s));
                }
                catch (NullPointerException ex2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Broken system behaviour for dns lookup of ");
                    sb.append(s);
                    final UnknownHostException ex = new UnknownHostException(sb.toString());
                    ex.initCause(ex2);
                    throw ex;
                }
            }
            throw new UnknownHostException("hostname == null");
        }
    };
    
    List<InetAddress> lookup(final String p0) throws UnknownHostException;
}
