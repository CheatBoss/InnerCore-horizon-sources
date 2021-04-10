package org.apache.james.mime4j.io;

import java.io.*;

public class LineNumberInputStream extends FilterInputStream implements LineNumberSource
{
    private int lineNumber;
    
    public LineNumberInputStream(final InputStream inputStream) {
        super(inputStream);
        this.lineNumber = 1;
    }
    
    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read == 10) {
            ++this.lineNumber;
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array, final int n, int i) throws IOException {
        int read;
        for (read = this.in.read(array, n, i), i = n; i < n + read; ++i) {
            if (array[i] == 10) {
                ++this.lineNumber;
            }
        }
        return read;
    }
}
