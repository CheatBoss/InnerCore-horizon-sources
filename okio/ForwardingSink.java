package okio;

import java.io.*;

public abstract class ForwardingSink implements Sink
{
    private final Sink delegate;
    
    public ForwardingSink(final Sink delegate) {
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
    
    @Override
    public void flush() throws IOException {
        this.delegate.flush();
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
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        this.delegate.write(buffer, n);
    }
}
