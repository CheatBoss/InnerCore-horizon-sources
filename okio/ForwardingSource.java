package okio;

import java.io.*;

public abstract class ForwardingSource implements Source
{
    private final Source delegate;
    
    public ForwardingSource(final Source delegate) {
        if (delegate != null) {
            this.delegate = delegate;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    
    public final Source delegate() {
        return this.delegate;
    }
    
    @Override
    public long read(final Buffer buffer, final long n) throws IOException {
        return this.delegate.read(buffer, n);
    }
    
    @Override
    public Timeout timeout() {
        return this.delegate.timeout();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("(");
        sb.append(this.delegate.toString());
        sb.append(")");
        return sb.toString();
    }
}
