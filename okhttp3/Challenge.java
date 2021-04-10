package okhttp3;

import java.nio.charset.*;
import okhttp3.internal.*;
import javax.annotation.*;

public final class Challenge
{
    private final Charset charset;
    private final String realm;
    private final String scheme;
    
    public Challenge(final String s, final String s2) {
        this(s, s2, Util.ISO_8859_1);
    }
    
    private Challenge(final String scheme, final String realm, final Charset charset) {
        if (scheme == null) {
            throw new NullPointerException("scheme == null");
        }
        if (realm == null) {
            throw new NullPointerException("realm == null");
        }
        if (charset != null) {
            this.scheme = scheme;
            this.realm = realm;
            this.charset = charset;
            return;
        }
        throw new NullPointerException("charset == null");
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof Challenge) {
            final Challenge challenge = (Challenge)o;
            if (challenge.scheme.equals(this.scheme) && challenge.realm.equals(this.realm) && challenge.charset.equals(this.charset)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return ((this.realm.hashCode() + 899) * 31 + this.scheme.hashCode()) * 31 + this.charset.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.scheme);
        sb.append(" realm=\"");
        sb.append(this.realm);
        sb.append("\" charset=\"");
        sb.append(this.charset);
        sb.append("\"");
        return sb.toString();
    }
    
    public Challenge withCharset(final Charset charset) {
        return new Challenge(this.scheme, this.realm, charset);
    }
}
