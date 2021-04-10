package org.apache.james.mime4j.io;

import java.io.*;
import org.apache.james.mime4j.util.*;

public class MimeBoundaryInputStream extends LineReaderInputStream
{
    private boolean atBoundary;
    private final byte[] boundary;
    private int boundaryLen;
    private BufferedLineReaderInputStream buffer;
    private boolean completed;
    private boolean eof;
    private boolean lastPart;
    private int limit;
    
    public MimeBoundaryInputStream(final BufferedLineReaderInputStream buffer, final String s) throws IOException {
        super(buffer);
        if (buffer.capacity() > s.length()) {
            this.buffer = buffer;
            int i = 0;
            this.eof = false;
            this.limit = -1;
            this.atBoundary = false;
            this.boundaryLen = 0;
            this.lastPart = false;
            this.completed = false;
            final byte[] boundary = new byte[s.length() + 2];
            (this.boundary = boundary)[1] = (boundary[0] = 45);
            while (i < s.length()) {
                final byte b = (byte)s.charAt(i);
                if (b == 13 || b == 10) {
                    throw new IllegalArgumentException("Boundary may not contain CR or LF");
                }
                this.boundary[i + 2] = b;
                ++i;
            }
            this.fillBuffer();
            return;
        }
        throw new IllegalArgumentException("Boundary is too long");
    }
    
    private void calculateBoundaryLen() throws IOException {
        this.boundaryLen = this.boundary.length;
        final int n = this.limit - this.buffer.pos();
        if (n > 0 && this.buffer.charAt(this.limit - 1) == 10) {
            ++this.boundaryLen;
            --this.limit;
        }
        if (n > 1 && this.buffer.charAt(this.limit - 1) == 13) {
            ++this.boundaryLen;
            --this.limit;
        }
    }
    
    private boolean endOfStream() {
        return this.eof || this.atBoundary;
    }
    
    private int fillBuffer() throws IOException {
        if (this.eof) {
            return -1;
        }
        final boolean hasData = this.hasData();
        boolean eof = false;
        int fillBuffer;
        if (!hasData) {
            fillBuffer = this.buffer.fillBuffer();
        }
        else {
            fillBuffer = 0;
        }
        if (fillBuffer == -1) {
            eof = true;
        }
        this.eof = eof;
        int limit;
        byte[] boundary;
        int n;
        BufferedLineReaderInputStream buffer;
        for (limit = this.buffer.indexOf(this.boundary); limit > 0 && this.buffer.charAt(limit - 1) != 10; limit = buffer.indexOf(boundary, n, buffer.limit() - n)) {
            boundary = this.boundary;
            n = limit + boundary.length;
            buffer = this.buffer;
        }
        if (limit != -1) {
            this.limit = limit;
            this.atBoundary = true;
            this.calculateBoundaryLen();
            return fillBuffer;
        }
        if (this.eof) {
            this.limit = this.buffer.limit();
            return fillBuffer;
        }
        this.limit = this.buffer.limit() - (this.boundary.length + 1);
        return fillBuffer;
    }
    
    private boolean hasData() {
        return this.limit > this.buffer.pos() && this.limit <= this.buffer.limit();
    }
    
    private void skipBoundary() throws IOException {
        if (this.completed) {
            return;
        }
        this.completed = true;
        this.buffer.skip(this.boundaryLen);
        int n = 1;
        while (true) {
            if (this.buffer.length() > 1) {
                final BufferedLineReaderInputStream buffer = this.buffer;
                final byte char1 = buffer.charAt(buffer.pos());
                final BufferedLineReaderInputStream buffer2 = this.buffer;
                final byte char2 = buffer2.charAt(buffer2.pos() + 1);
                if (n != 0 && char1 == 45 && char2 == 45) {
                    this.lastPart = true;
                    this.buffer.skip(2);
                    n = 0;
                }
                else {
                    if (char1 == 13 && char2 == 10) {
                        this.buffer.skip(2);
                        return;
                    }
                    if (char1 == 10) {
                        this.buffer.skip(1);
                        return;
                    }
                    this.buffer.skip(1);
                }
            }
            else {
                if (this.eof) {
                    return;
                }
                this.fillBuffer();
            }
        }
    }
    
    @Override
    public void close() throws IOException {
    }
    
    public boolean eof() {
        return this.eof && !this.buffer.hasBufferedData();
    }
    
    public boolean isLastPart() {
        return this.lastPart;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        if (this.completed) {
            return -1;
        }
        if (this.endOfStream() && !this.hasData()) {
            this.skipBoundary();
            return -1;
        }
        while (!this.hasData()) {
            if (this.endOfStream()) {
                this.skipBoundary();
                return -1;
            }
            this.fillBuffer();
        }
        return this.buffer.read();
    }
    
    @Override
    public int read(final byte[] array, final int n, int min) throws IOException {
        if (this.completed) {
            return -1;
        }
        if (this.endOfStream() && !this.hasData()) {
            this.skipBoundary();
            return -1;
        }
        this.fillBuffer();
        if (!this.hasData()) {
            return this.read(array, n, min);
        }
        min = Math.min(min, this.limit - this.buffer.pos());
        return this.buffer.read(array, n, min);
    }
    
    @Override
    public int readLine(final ByteArrayBuffer byteArrayBuffer) throws IOException {
        if (byteArrayBuffer == null) {
            throw new IllegalArgumentException("Destination buffer may not be null");
        }
        if (this.completed) {
            return -1;
        }
        if (this.endOfStream() && !this.hasData()) {
            this.skipBoundary();
            return -1;
        }
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n;
            if (n2 != 0) {
                break;
            }
            int fillBuffer = n;
            if (!this.hasData()) {
                final int n5 = fillBuffer = this.fillBuffer();
                if (!this.hasData()) {
                    fillBuffer = n5;
                    if (this.endOfStream()) {
                        this.skipBoundary();
                        n4 = -1;
                        break;
                    }
                }
            }
            int n6 = this.limit - this.buffer.pos();
            final BufferedLineReaderInputStream buffer = this.buffer;
            final int index = buffer.indexOf((byte)10, buffer.pos(), n6);
            int n7 = n2;
            if (index != -1) {
                n6 = index + 1 - this.buffer.pos();
                n7 = 1;
            }
            n = fillBuffer;
            n2 = n7;
            if (n6 <= 0) {
                continue;
            }
            byteArrayBuffer.append(this.buffer.buf(), this.buffer.pos(), n6);
            this.buffer.skip(n6);
            n3 += n6;
            n = fillBuffer;
            n2 = n7;
        }
        if (n3 == 0 && n4 == -1) {
            return -1;
        }
        return n3;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MimeBoundaryInputStream, boundary ");
        final byte[] boundary = this.boundary;
        for (int length = boundary.length, i = 0; i < length; ++i) {
            sb.append((char)boundary[i]);
        }
        return sb.toString();
    }
}
