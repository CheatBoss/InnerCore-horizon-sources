package okhttp3.internal.http;

import javax.annotation.*;
import okio.*;
import okhttp3.*;

public final class RealResponseBody extends ResponseBody
{
    private final long contentLength;
    @Nullable
    private final String contentTypeString;
    private final BufferedSource source;
    
    public RealResponseBody(@Nullable final String contentTypeString, final long contentLength, final BufferedSource source) {
        this.contentTypeString = contentTypeString;
        this.contentLength = contentLength;
        this.source = source;
    }
    
    @Override
    public long contentLength() {
        return this.contentLength;
    }
    
    @Override
    public MediaType contentType() {
        final String contentTypeString = this.contentTypeString;
        if (contentTypeString != null) {
            return MediaType.parse(contentTypeString);
        }
        return null;
    }
    
    @Override
    public BufferedSource source() {
        return this.source;
    }
}
