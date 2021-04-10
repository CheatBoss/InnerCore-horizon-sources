package org.spongycastle.util.io;

import java.io.*;

public abstract class SimpleOutputStream extends OutputStream
{
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.write(new byte[] { (byte)n }, 0, 1);
    }
}
