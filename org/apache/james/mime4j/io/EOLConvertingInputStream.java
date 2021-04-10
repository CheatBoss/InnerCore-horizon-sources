package org.apache.james.mime4j.io;

import java.io.*;

public class EOLConvertingInputStream extends InputStream
{
    public static final int CONVERT_BOTH = 3;
    public static final int CONVERT_CR = 1;
    public static final int CONVERT_LF = 2;
    private int flags;
    private PushbackInputStream in;
    private int previous;
    
    public EOLConvertingInputStream(final InputStream inputStream) {
        this(inputStream, 3);
    }
    
    public EOLConvertingInputStream(final InputStream inputStream, final int flags) {
        this.in = null;
        this.previous = 0;
        this.flags = 3;
        this.in = new PushbackInputStream(inputStream, 2);
        this.flags = flags;
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read == -1) {
            return -1;
        }
        int previous;
        if ((this.flags & 0x1) != 0x0 && read == 13) {
            final int read2 = this.in.read();
            if (read2 != -1) {
                this.in.unread(read2);
            }
            previous = read;
            if (read2 != 10) {
                this.in.unread(10);
                previous = read;
            }
        }
        else {
            previous = read;
            if ((this.flags & 0x2) != 0x0 && (previous = read) == 10) {
                previous = read;
                if (this.previous != 13) {
                    this.in.unread(10);
                    previous = 13;
                }
            }
        }
        return this.previous = previous;
    }
}
