package com.android.dx.util;

import java.io.*;

public final class TwoColumnOutput
{
    private final StringBuffer leftBuf;
    private final IndentingWriter leftColumn;
    private final int leftWidth;
    private final Writer out;
    private final StringBuffer rightBuf;
    private final IndentingWriter rightColumn;
    
    public TwoColumnOutput(final OutputStream outputStream, final int n, final int n2, final String s) {
        this(new OutputStreamWriter(outputStream), n, n2, s);
    }
    
    public TwoColumnOutput(final Writer out, final int leftWidth, final int n, final String s) {
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        if (leftWidth < 1) {
            throw new IllegalArgumentException("leftWidth < 1");
        }
        if (n < 1) {
            throw new IllegalArgumentException("rightWidth < 1");
        }
        if (s == null) {
            throw new NullPointerException("spacer == null");
        }
        final StringWriter stringWriter = new StringWriter(1000);
        final StringWriter stringWriter2 = new StringWriter(1000);
        this.out = out;
        this.leftWidth = leftWidth;
        this.leftBuf = stringWriter.getBuffer();
        this.rightBuf = stringWriter2.getBuffer();
        this.leftColumn = new IndentingWriter(stringWriter, leftWidth);
        this.rightColumn = new IndentingWriter(stringWriter2, n, s);
    }
    
    private static void appendNewlineIfNecessary(final StringBuffer sb, final Writer writer) throws IOException {
        final int length = sb.length();
        if (length != 0 && sb.charAt(length - 1) != '\n') {
            writer.write(10);
        }
    }
    
    private void flushLeft() throws IOException {
        appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
        while (this.leftBuf.length() != 0) {
            this.rightColumn.write(10);
            this.outputFullLines();
        }
    }
    
    private void flushRight() throws IOException {
        appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
        while (this.rightBuf.length() != 0) {
            this.leftColumn.write(10);
            this.outputFullLines();
        }
    }
    
    private void outputFullLines() throws IOException {
        while (true) {
            final int index = this.leftBuf.indexOf("\n");
            if (index < 0) {
                return;
            }
            final int index2 = this.rightBuf.indexOf("\n");
            if (index2 < 0) {
                return;
            }
            if (index != 0) {
                this.out.write(this.leftBuf.substring(0, index));
            }
            if (index2 != 0) {
                writeSpaces(this.out, this.leftWidth - index);
                this.out.write(this.rightBuf.substring(0, index2));
            }
            this.out.write(10);
            this.leftBuf.delete(0, index + 1);
            this.rightBuf.delete(0, index2 + 1);
        }
    }
    
    public static String toString(final String s, final int n, final String s2, final String s3, final int n2) {
        final StringWriter stringWriter = new StringWriter((s.length() + s3.length()) * 3);
        final TwoColumnOutput twoColumnOutput = new TwoColumnOutput(stringWriter, n, n2, s2);
        try {
            twoColumnOutput.getLeft().write(s);
            twoColumnOutput.getRight().write(s3);
            twoColumnOutput.flush();
            return stringWriter.toString();
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }
    
    private static void writeSpaces(final Writer writer, int i) throws IOException {
        while (i > 0) {
            writer.write(32);
            --i;
        }
    }
    
    public void flush() {
        try {
            appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
            appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
            this.outputFullLines();
            this.flushLeft();
            this.flushRight();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Writer getLeft() {
        return this.leftColumn;
    }
    
    public Writer getRight() {
        return this.rightColumn;
    }
}
