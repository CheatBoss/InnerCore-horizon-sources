package com.android.dx.util;

import java.io.*;

public final class IndentingWriter extends FilterWriter
{
    private boolean collectingIndent;
    private int column;
    private int indent;
    private final int maxIndent;
    private final String prefix;
    private final int width;
    
    public IndentingWriter(final Writer writer, final int n) {
        this(writer, n, "");
    }
    
    public IndentingWriter(final Writer writer, final int n, String prefix) {
        super(writer);
        if (writer == null) {
            throw new NullPointerException("out == null");
        }
        if (n < 0) {
            throw new IllegalArgumentException("width < 0");
        }
        if (prefix == null) {
            throw new NullPointerException("prefix == null");
        }
        int width;
        if (n != 0) {
            width = n;
        }
        else {
            width = Integer.MAX_VALUE;
        }
        this.width = width;
        this.maxIndent = n >> 1;
        if (prefix.length() == 0) {
            prefix = null;
        }
        this.prefix = prefix;
        this.bol();
    }
    
    private void bol() {
        this.column = 0;
        this.collectingIndent = (this.maxIndent != 0);
        this.indent = 0;
    }
    
    @Override
    public void write(final int n) throws IOException {
        synchronized (this.lock) {
            final boolean collectingIndent = this.collectingIndent;
            int i = 0;
            if (collectingIndent) {
                if (n == 32) {
                    ++this.indent;
                    if (this.indent >= this.maxIndent) {
                        this.indent = this.maxIndent;
                        this.collectingIndent = false;
                    }
                }
                else {
                    this.collectingIndent = false;
                }
            }
            if (this.column == this.width && n != 10) {
                this.out.write(10);
                this.column = 0;
            }
            if (this.column == 0) {
                if (this.prefix != null) {
                    this.out.write(this.prefix);
                }
                if (!this.collectingIndent) {
                    while (i < this.indent) {
                        this.out.write(32);
                        ++i;
                    }
                    this.column = this.indent;
                }
            }
            this.out.write(n);
            if (n == 10) {
                this.bol();
            }
            else {
                ++this.column;
            }
        }
    }
    
    @Override
    public void write(final String s, int n, int n2) throws IOException {
        final Object lock = this.lock;
        // monitorenter(lock)
    Label_0037_Outer:
        while (true) {
            Label_0033: {
                if (n2 <= 0) {
                    break Label_0033;
                }
                while (true) {
                    try {
                        this.write(s.charAt(n));
                        ++n;
                        --n2;
                        continue Label_0037_Outer;
                        // monitorexit(lock)
                        return;
                        // monitorexit(lock)
                        throw s;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void write(final char[] array, int n, int n2) throws IOException {
        final Object lock = this.lock;
        // monitorenter(lock)
    Label_0035_Outer:
        while (true) {
            Label_0031: {
                if (n2 <= 0) {
                    break Label_0031;
                }
                while (true) {
                    try {
                        this.write(array[n]);
                        ++n;
                        --n2;
                        continue Label_0035_Outer;
                        // monitorexit(lock)
                        throw array;
                    }
                    // monitorexit(lock)
                    finally {
                        continue;
                    }
                    break;
                }
            }
        }
    }
}
