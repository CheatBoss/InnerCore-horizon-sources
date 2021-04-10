package okhttp3;

import javax.annotation.*;
import java.util.*;
import java.util.regex.*;
import java.nio.charset.*;

public final class MediaType
{
    private static final Pattern PARAMETER;
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final Pattern TYPE_SUBTYPE;
    @Nullable
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;
    
    static {
        TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
        PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    }
    
    private MediaType(final String mediaType, final String type, final String subtype, @Nullable final String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }
    
    @Nullable
    public static MediaType parse(final String s) {
        final Matcher matcher = MediaType.TYPE_SUBTYPE.matcher(s);
        if (!matcher.lookingAt()) {
            return null;
        }
        final String lowerCase = matcher.group(1).toLowerCase(Locale.US);
        final String lowerCase2 = matcher.group(2).toLowerCase(Locale.US);
        final Matcher matcher2 = MediaType.PARAMETER.matcher(s);
        int i = matcher.end();
        String s2 = null;
        while (i < s.length()) {
            matcher2.region(i, s.length());
            if (!matcher2.lookingAt()) {
                return null;
            }
            final String group = matcher2.group(1);
            String s3 = s2;
            if (group != null) {
                if (!group.equalsIgnoreCase("charset")) {
                    s3 = s2;
                }
                else {
                    final String group2 = matcher2.group(2);
                    if (group2 != null) {
                        s3 = group2;
                        if (group2.startsWith("'")) {
                            s3 = group2;
                            if (group2.endsWith("'")) {
                                s3 = group2;
                                if (group2.length() > 2) {
                                    s3 = group2.substring(1, group2.length() - 1);
                                }
                            }
                        }
                    }
                    else {
                        s3 = matcher2.group(3);
                    }
                    if (s2 != null && !s3.equalsIgnoreCase(s2)) {
                        return null;
                    }
                }
            }
            i = matcher2.end();
            s2 = s3;
        }
        return new MediaType(s, lowerCase, lowerCase2, s2);
    }
    
    @Nullable
    public Charset charset() {
        return this.charset(null);
    }
    
    @Nullable
    public Charset charset(@Nullable final Charset charset) {
        Charset forName = charset;
        try {
            if (this.charset != null) {
                forName = Charset.forName(this.charset);
            }
            return forName;
        }
        catch (IllegalArgumentException ex) {
            return charset;
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof MediaType && ((MediaType)o).mediaType.equals(this.mediaType);
    }
    
    @Override
    public int hashCode() {
        return this.mediaType.hashCode();
    }
    
    public String subtype() {
        return this.subtype;
    }
    
    @Override
    public String toString() {
        return this.mediaType;
    }
    
    public String type() {
        return this.type;
    }
}
