package org.spongycastle.crypto.tls;

import java.io.*;
import org.spongycastle.util.*;

public final class ProtocolVersion
{
    public static final ProtocolVersion DTLSv10;
    public static final ProtocolVersion DTLSv12;
    public static final ProtocolVersion SSLv3;
    public static final ProtocolVersion TLSv10;
    public static final ProtocolVersion TLSv11;
    public static final ProtocolVersion TLSv12;
    private String name;
    private int version;
    
    static {
        SSLv3 = new ProtocolVersion(768, "SSL 3.0");
        TLSv10 = new ProtocolVersion(769, "TLS 1.0");
        TLSv11 = new ProtocolVersion(770, "TLS 1.1");
        TLSv12 = new ProtocolVersion(771, "TLS 1.2");
        DTLSv10 = new ProtocolVersion(65279, "DTLS 1.0");
        DTLSv12 = new ProtocolVersion(65277, "DTLS 1.2");
    }
    
    private ProtocolVersion(final int n, final String name) {
        this.version = (n & 0xFFFF);
        this.name = name;
    }
    
    public static ProtocolVersion get(final int n, final int n2) throws IOException {
        String s = null;
        if (n != 3) {
            if (n != 254) {
                throw new TlsFatalAlert((short)47);
            }
            switch (n2) {
                default: {
                    s = "DTLS";
                    break;
                }
                case 255: {
                    return ProtocolVersion.DTLSv10;
                }
                case 254: {
                    throw new TlsFatalAlert((short)47);
                }
                case 253: {
                    return ProtocolVersion.DTLSv12;
                }
            }
        }
        else {
            if (n2 == 0) {
                return ProtocolVersion.SSLv3;
            }
            if (n2 == 1) {
                return ProtocolVersion.TLSv10;
            }
            if (n2 == 2) {
                return ProtocolVersion.TLSv11;
            }
            if (n2 == 3) {
                return ProtocolVersion.TLSv12;
            }
            s = "TLS";
        }
        return getUnknownVersion(n, n2, s);
    }
    
    private static ProtocolVersion getUnknownVersion(int n, final int n2, final String s) throws IOException {
        TlsUtils.checkUint8(n);
        TlsUtils.checkUint8(n2);
        n = (n << 8 | n2);
        final String upperCase = Strings.toUpperCase(Integer.toHexString(0x10000 | n).substring(1));
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" 0x");
        sb.append(upperCase);
        return new ProtocolVersion(n, sb.toString());
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof ProtocolVersion && this.equals((ProtocolVersion)o));
    }
    
    public boolean equals(final ProtocolVersion protocolVersion) {
        return protocolVersion != null && this.version == protocolVersion.version;
    }
    
    public ProtocolVersion getEquivalentTLSVersion() {
        if (!this.isDTLS()) {
            return this;
        }
        if (this == ProtocolVersion.DTLSv10) {
            return ProtocolVersion.TLSv11;
        }
        return ProtocolVersion.TLSv12;
    }
    
    public int getFullVersion() {
        return this.version;
    }
    
    public int getMajorVersion() {
        return this.version >> 8;
    }
    
    public int getMinorVersion() {
        return this.version & 0xFF;
    }
    
    @Override
    public int hashCode() {
        return this.version;
    }
    
    public boolean isDTLS() {
        return this.getMajorVersion() == 254;
    }
    
    public boolean isEqualOrEarlierVersionOf(final ProtocolVersion protocolVersion) {
        final int majorVersion = this.getMajorVersion();
        final int majorVersion2 = protocolVersion.getMajorVersion();
        boolean b = false;
        if (majorVersion != majorVersion2) {
            return false;
        }
        final int n = protocolVersion.getMinorVersion() - this.getMinorVersion();
        if (this.isDTLS()) {
            if (n > 0) {
                return b;
            }
        }
        else if (n < 0) {
            return b;
        }
        b = true;
        return b;
    }
    
    public boolean isLaterVersionOf(final ProtocolVersion protocolVersion) {
        final int majorVersion = this.getMajorVersion();
        final int majorVersion2 = protocolVersion.getMajorVersion();
        boolean b = false;
        if (majorVersion != majorVersion2) {
            return false;
        }
        final int n = protocolVersion.getMinorVersion() - this.getMinorVersion();
        if (this.isDTLS()) {
            if (n <= 0) {
                return b;
            }
        }
        else if (n >= 0) {
            return b;
        }
        b = true;
        return b;
    }
    
    public boolean isSSL() {
        return this == ProtocolVersion.SSLv3;
    }
    
    public boolean isTLS() {
        return this.getMajorVersion() == 3;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
