package okhttp3;

import javax.annotation.*;
import okhttp3.internal.*;
import java.io.*;
import java.nio.charset.*;
import okio.*;

public abstract class RequestBody
{
    public static RequestBody create(@Nullable final MediaType mediaType, final File file) {
        if (file != null) {
            return new RequestBody() {
                @Override
                public long contentLength() {
                    return file.length();
                }
                
                @Nullable
                @Override
                public MediaType contentType() {
                    return mediaType;
                }
                
                @Override
                public void writeTo(final BufferedSink bufferedSink) throws IOException {
                    final Closeable closeable = null;
                    Closeable closeable2;
                    try {
                        final Source source = Okio.source(file);
                        try {
                            bufferedSink.writeAll(source);
                            Util.closeQuietly(source);
                            return;
                        }
                        finally {}
                    }
                    finally {
                        closeable2 = closeable;
                    }
                    Util.closeQuietly(closeable2);
                }
            };
        }
        throw new NullPointerException("content == null");
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final String s) {
        Charset charset = Util.UTF_8;
        MediaType parse = mediaType;
        if (mediaType != null) {
            charset = mediaType.charset();
            parse = mediaType;
            if (charset == null) {
                charset = Util.UTF_8;
                final StringBuilder sb = new StringBuilder();
                sb.append(mediaType);
                sb.append("; charset=utf-8");
                parse = MediaType.parse(sb.toString());
            }
        }
        return create(parse, s.getBytes(charset));
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final ByteString byteString) {
        return new RequestBody() {
            @Override
            public long contentLength() throws IOException {
                return byteString.size();
            }
            
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }
            
            @Override
            public void writeTo(final BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(byteString);
            }
        };
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final byte[] array) {
        return create(mediaType, array, 0, array.length);
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final byte[] array, final int n, final int n2) {
        if (array != null) {
            Util.checkOffsetAndCount(array.length, n, n2);
            return new RequestBody() {
                @Override
                public long contentLength() {
                    return n2;
                }
                
                @Nullable
                @Override
                public MediaType contentType() {
                    return mediaType;
                }
                
                @Override
                public void writeTo(final BufferedSink bufferedSink) throws IOException {
                    bufferedSink.write(array, n, n2);
                }
            };
        }
        throw new NullPointerException("content == null");
    }
    
    public long contentLength() throws IOException {
        return -1L;
    }
    
    @Nullable
    public abstract MediaType contentType();
    
    public abstract void writeTo(final BufferedSink p0) throws IOException;
}
