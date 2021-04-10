package okio;

import java.io.*;

public interface Source extends Closeable
{
    void close() throws IOException;
    
    long read(final Buffer p0, final long p1) throws IOException;
    
    Timeout timeout();
}
