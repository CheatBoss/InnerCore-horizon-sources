package com.cedarsoftware.util.io;

import java.io.*;

public class FastPushbackBufferedReader extends BufferedReader implements FastPushbackReader
{
    private final int[] buf;
    protected int col;
    private int idx;
    protected int line;
    private int unread;
    
    FastPushbackBufferedReader(final Reader reader) {
        super(reader);
        this.buf = new int[256];
        this.idx = 0;
        this.unread = Integer.MAX_VALUE;
        this.line = 1;
        this.col = 0;
    }
    
    private boolean appendChar(final StringBuilder sb, int n) {
        try {
            n = this.buf[n];
            if (n == 0) {
                return true;
            }
            sb.appendCodePoint(n);
            return false;
        }
        catch (Exception ex) {
            return true;
        }
    }
    
    @Override
    public int getCol() {
        return this.col;
    }
    
    @Override
    public String getLastSnippet() {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            for (int i = this.idx; i < this.buf.length; ++i) {
                if (this.appendChar(sb, i)) {
                    for (int j = 0; j < this.idx; ++j) {
                        if (this.appendChar(sb, j)) {
                            return sb.toString();
                        }
                    }
                    return sb.toString();
                }
            }
            continue;
        }
    }
    
    @Override
    public int getLine() {
        return this.line;
    }
    
    @Override
    public int read() throws IOException {
        int n;
        if (this.unread == Integer.MAX_VALUE) {
            n = super.read();
        }
        else {
            n = this.unread;
            this.unread = Integer.MAX_VALUE;
        }
        this.buf[this.idx++] = n;
        if (n == 10) {
            ++this.line;
            this.col = 0;
        }
        else {
            ++this.col;
        }
        if (this.idx >= this.buf.length) {
            this.idx = 0;
        }
        return n;
    }
    
    @Override
    public void unread(final int unread) throws IOException {
        this.unread = unread;
        if (unread == 10) {
            --this.line;
        }
        else {
            --this.col;
        }
        if (this.idx < 1) {
            this.idx = this.buf.length - 1;
            return;
        }
        --this.idx;
    }
}
