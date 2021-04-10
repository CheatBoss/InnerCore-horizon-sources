package okhttp3;

import java.util.*;

public enum TlsVersion
{
    SSL_3_0("SSLv3"), 
    TLS_1_0("TLSv1"), 
    TLS_1_1("TLSv1.1"), 
    TLS_1_2("TLSv1.2"), 
    TLS_1_3("TLSv1.3");
    
    final String javaName;
    
    private TlsVersion(final String javaName) {
        this.javaName = javaName;
    }
    
    public static TlsVersion forJavaName(final String s) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0119: {
            if (hashCode != 79201641) {
                if (hashCode != 79923350) {
                    switch (hashCode) {
                        case -503070501: {
                            if (s.equals("TLSv1.3")) {
                                n = 0;
                                break Label_0119;
                            }
                            break;
                        }
                        case -503070502: {
                            if (s.equals("TLSv1.2")) {
                                n = 1;
                                break Label_0119;
                            }
                            break;
                        }
                        case -503070503: {
                            if (s.equals("TLSv1.1")) {
                                n = 2;
                                break Label_0119;
                            }
                            break;
                        }
                    }
                }
                else if (s.equals("TLSv1")) {
                    n = 3;
                    break Label_0119;
                }
            }
            else if (s.equals("SSLv3")) {
                n = 4;
                break Label_0119;
            }
            n = -1;
        }
        if (n == 0) {
            return TlsVersion.TLS_1_3;
        }
        if (n == 1) {
            return TlsVersion.TLS_1_2;
        }
        if (n == 2) {
            return TlsVersion.TLS_1_1;
        }
        if (n == 3) {
            return TlsVersion.TLS_1_0;
        }
        if (n == 4) {
            return TlsVersion.SSL_3_0;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected TLS version: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    static List<TlsVersion> forJavaNames(final String... array) {
        final ArrayList<TlsVersion> list = new ArrayList<TlsVersion>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(forJavaName(array[i]));
        }
        return (List<TlsVersion>)Collections.unmodifiableList((List<?>)list);
    }
}
