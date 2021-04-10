package okhttp3;

import java.nio.charset.*;
import okhttp3.internal.*;
import javax.annotation.*;
import okio.*;
import java.io.*;

public abstract class ResponseBody implements Closeable
{
    private Reader reader;
    
    private Charset charset() {
        final MediaType contentType = this.contentType();
        if (contentType != null) {
            return contentType.charset(Util.UTF_8);
        }
        return Util.UTF_8;
    }
    
    public static ResponseBody create(@Nullable final MediaType mediaType, final long n, final BufferedSource bufferedSource) {
        if (bufferedSource != null) {
            return new ResponseBody() {
                @Override
                public long contentLength() {
                    return n;
                }
                
                @Nullable
                @Override
                public MediaType contentType() {
                    return mediaType;
                }
                
                @Override
                public BufferedSource source() {
                    return bufferedSource;
                }
            };
        }
        throw new NullPointerException("source == null");
    }
    
    public static ResponseBody create(@Nullable final MediaType mediaType, final String s) {
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
        final Buffer writeString = new Buffer().writeString(s, charset);
        return create(parse, writeString.size(), writeString);
    }
    
    public static ResponseBody create(@Nullable final MediaType mediaType, final byte[] array) {
        return create(mediaType, array.length, new Buffer().write(array));
    }
    
    public final InputStream byteStream() {
        return this.source().inputStream();
    }
    
    public final byte[] bytes() throws IOException {
        final long contentLength = this.contentLength();
        if (contentLength <= 2147483647L) {
            Object source = this.source();
            try {
                final byte[] byteArray = ((BufferedSource)source).readByteArray();
                Util.closeQuietly((Closeable)source);
                if (contentLength == -1L) {
                    return byteArray;
                }
                if (contentLength == byteArray.length) {
                    return byteArray;
                }
                source = new StringBuilder();
                ((StringBuilder)source).append("Content-Length (");
                ((StringBuilder)source).append(contentLength);
                ((StringBuilder)source).append(") and stream length (");
                ((StringBuilder)source).append(byteArray.length);
                ((StringBuilder)source).append(") disagree");
                throw new IOException(((StringBuilder)source).toString());
            }
            finally {
                Util.closeQuietly((Closeable)source);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot buffer entire body for content length: ");
        sb.append(contentLength);
        throw new IOException(sb.toString());
    }
    
    public final Reader charStream() {
        final Reader reader = this.reader;
        if (reader != null) {
            return reader;
        }
        return this.reader = new BomAwareReader(this.source(), this.charset());
    }
    
    @Override
    public void close() {
        Util.closeQuietly(this.source());
    }
    
    public abstract long contentLength();
    
    @Nullable
    public abstract MediaType contentType();
    
    public abstract BufferedSource source();
    
    public final String string() throws IOException {
        final BufferedSource source = this.source();
        try {
            return source.readString(Util.bomAwareCharset(source, this.charset()));
        }
        finally {
            Util.closeQuietly(source);
        }
    }
    
    static final class BomAwareReader extends Reader
    {
        private final Charset charset;
        private boolean closed;
        private Reader delegate;
        private final BufferedSource source;
        
        BomAwareReader(final BufferedSource source, final Charset charset) {
            this.source = source;
            this.charset = charset;
        }
        
        @Override
        public void close() throws IOException {
            this.closed = true;
            final Reader delegate = this.delegate;
            if (delegate != null) {
                delegate.close();
                return;
            }
            this.source.close();
        }
        
        @Override
        public int read(final char[] array, final int n, final int n2) throws IOException {
            if (!this.closed) {
                Reader delegate;
                if ((delegate = this.delegate) == null) {
                    delegate = new InputStreamReader(this.source.inputStream(), Util.bomAwareCharset(this.source, this.charset));
                    this.delegate = delegate;
                }
                return delegate.read(array, n, n2);
            }
            throw new IOException("Stream closed");
        }
    }
}
