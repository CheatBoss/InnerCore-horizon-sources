package okhttp3.internal.http2;

import okio.*;
import okhttp3.internal.*;

public final class Header
{
    public static final ByteString PSEUDO_PREFIX;
    public static final ByteString RESPONSE_STATUS;
    public static final ByteString TARGET_AUTHORITY;
    public static final ByteString TARGET_METHOD;
    public static final ByteString TARGET_PATH;
    public static final ByteString TARGET_SCHEME;
    final int hpackSize;
    public final ByteString name;
    public final ByteString value;
    
    static {
        PSEUDO_PREFIX = ByteString.encodeUtf8(":");
        RESPONSE_STATUS = ByteString.encodeUtf8(":status");
        TARGET_METHOD = ByteString.encodeUtf8(":method");
        TARGET_PATH = ByteString.encodeUtf8(":path");
        TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
        TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
    }
    
    public Header(final String s, final String s2) {
        this(ByteString.encodeUtf8(s), ByteString.encodeUtf8(s2));
    }
    
    public Header(final ByteString byteString, final String s) {
        this(byteString, ByteString.encodeUtf8(s));
    }
    
    public Header(final ByteString name, final ByteString value) {
        this.name = name;
        this.value = value;
        this.hpackSize = name.size() + 32 + value.size();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Header) {
            final Header header = (Header)o;
            if (this.name.equals(header.name) && this.value.equals(header.value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.name.hashCode() + 527) * 31 + this.value.hashCode();
    }
    
    @Override
    public String toString() {
        return Util.format("%s: %s", this.name.utf8(), this.value.utf8());
    }
}
