package org.apache.james.mime4j.io;

import org.apache.james.mime4j.util.*;
import java.io.*;

public class LineReaderInputStreamAdaptor extends LineReaderInputStream
{
    private final LineReaderInputStream bis;
    private boolean eof;
    private final int maxLineLen;
    private boolean used;
    
    public LineReaderInputStreamAdaptor(final InputStream inputStream) {
        this(inputStream, -1);
    }
    
    public LineReaderInputStreamAdaptor(final InputStream inputStream, final int maxLineLen) {
        super(inputStream);
        this.used = false;
        this.eof = false;
        LineReaderInputStream bis;
        if (inputStream instanceof LineReaderInputStream) {
            bis = (LineReaderInputStream)inputStream;
        }
        else {
            bis = null;
        }
        this.bis = bis;
        this.maxLineLen = maxLineLen;
    }
    
    private int doReadLine(final ByteArrayBuffer byteArrayBuffer) throws IOException {
        int n = 0;
        int i;
        int n2;
        do {
            i = this.in.read();
            n2 = n;
            if (i == -1) {
                break;
            }
            byteArrayBuffer.append(i);
            n2 = n + 1;
            if (this.maxLineLen > 0 && byteArrayBuffer.length() >= this.maxLineLen) {
                throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
            n = n2;
        } while (i != 10);
        if (n2 == 0 && i == -1) {
            return -1;
        }
        return n2;
    }
    
    public boolean eof() {
        return this.eof;
    }
    
    public boolean isUsed() {
        return this.used;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        this.eof = (read == -1);
        this.used = true;
        return read;
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        read = this.in.read(array, read, n);
        this.eof = (read == -1);
        this.used = true;
        return read;
    }
    
    @Override
    public int readLine(final ByteArrayBuffer byteArrayBuffer) throws IOException {
        final LineReaderInputStream bis = this.bis;
        int n;
        if (bis != null) {
            n = bis.readLine(byteArrayBuffer);
        }
        else {
            n = this.doReadLine(byteArrayBuffer);
        }
        this.eof = (n == -1);
        this.used = true;
        return n;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[LineReaderInputStreamAdaptor: ");
        sb.append(this.bis);
        sb.append("]");
        return sb.toString();
    }
}
