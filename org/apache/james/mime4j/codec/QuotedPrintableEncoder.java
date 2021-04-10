package org.apache.james.mime4j.codec;

import java.io.*;

final class QuotedPrintableEncoder
{
    private static final byte CR = 13;
    private static final byte EQUALS = 61;
    private static final byte[] HEX_DIGITS;
    private static final byte LF = 10;
    private static final byte QUOTED_PRINTABLE_LAST_PLAIN = 126;
    private static final int QUOTED_PRINTABLE_MAX_LINE_LENGTH = 76;
    private static final int QUOTED_PRINTABLE_OCTETS_PER_ESCAPE = 3;
    private static final byte SPACE = 32;
    private static final byte TAB = 9;
    private final boolean binary;
    private final byte[] inBuffer;
    private int nextSoftBreak;
    private OutputStream out;
    private final byte[] outBuffer;
    private int outputIndex;
    private boolean pendingCR;
    private boolean pendingSpace;
    private boolean pendingTab;
    
    static {
        HEX_DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
    }
    
    public QuotedPrintableEncoder(final int n, final boolean binary) {
        this.inBuffer = new byte[n];
        this.outBuffer = new byte[n * 3];
        this.outputIndex = 0;
        this.nextSoftBreak = 77;
        this.out = null;
        this.binary = binary;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }
    
    private void clearPending() throws IOException {
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
    }
    
    private void encode(final byte b) throws IOException {
        if (b == 10) {
            if (this.binary) {
                this.writePending();
                this.escape(b);
                return;
            }
            if (this.pendingCR) {
                if (this.pendingSpace) {
                    this.escape((byte)32);
                }
                else if (this.pendingTab) {
                    this.escape((byte)9);
                }
                this.lineBreak();
                this.clearPending();
                return;
            }
            this.writePending();
            this.plain(b);
        }
        else if (b == 13) {
            if (this.binary) {
                this.escape(b);
                return;
            }
            this.pendingCR = true;
        }
        else {
            this.writePending();
            if (b == 32) {
                if (this.binary) {
                    this.escape(b);
                    return;
                }
                this.pendingSpace = true;
            }
            else if (b == 9) {
                if (this.binary) {
                    this.escape(b);
                    return;
                }
                this.pendingTab = true;
            }
            else {
                if (b < 32) {
                    this.escape(b);
                    return;
                }
                if (b > 126) {
                    this.escape(b);
                    return;
                }
                if (b == 61) {
                    this.escape(b);
                    return;
                }
                this.plain(b);
            }
        }
    }
    
    private void escape(final byte b) throws IOException {
        final int nextSoftBreak = this.nextSoftBreak - 1;
        this.nextSoftBreak = nextSoftBreak;
        if (nextSoftBreak <= 3) {
            this.softBreak();
        }
        final int n = b & 0xFF;
        this.write((byte)61);
        --this.nextSoftBreak;
        this.write(QuotedPrintableEncoder.HEX_DIGITS[n >> 4]);
        --this.nextSoftBreak;
        this.write(QuotedPrintableEncoder.HEX_DIGITS[n % 16]);
    }
    
    private void lineBreak() throws IOException {
        this.write((byte)13);
        this.write((byte)10);
        this.nextSoftBreak = 76;
    }
    
    private void plain(final byte b) throws IOException {
        final int nextSoftBreak = this.nextSoftBreak - 1;
        this.nextSoftBreak = nextSoftBreak;
        if (nextSoftBreak <= 1) {
            this.softBreak();
        }
        this.write(b);
    }
    
    private void softBreak() throws IOException {
        this.write((byte)61);
        this.lineBreak();
    }
    
    private void write(final byte b) throws IOException {
        final byte[] outBuffer = this.outBuffer;
        final int outputIndex = this.outputIndex;
        final int outputIndex2 = outputIndex + 1;
        this.outputIndex = outputIndex2;
        outBuffer[outputIndex] = b;
        if (outputIndex2 >= outBuffer.length) {
            this.flushOutput();
        }
    }
    
    private void writePending() throws IOException {
        Label_0044: {
            byte b;
            if (this.pendingSpace) {
                b = 32;
            }
            else if (this.pendingTab) {
                b = 9;
            }
            else {
                if (!this.pendingCR) {
                    break Label_0044;
                }
                b = 13;
            }
            this.plain(b);
        }
        this.clearPending();
    }
    
    void completeEncoding() throws IOException {
        this.writePending();
        this.flushOutput();
    }
    
    public void encode(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        this.initEncoding(outputStream);
        while (true) {
            final int read = inputStream.read(this.inBuffer);
            if (read <= -1) {
                break;
            }
            this.encodeChunk(this.inBuffer, 0, read);
        }
        this.completeEncoding();
    }
    
    void encodeChunk(final byte[] array, final int n, final int n2) throws IOException {
        for (int i = n; i < n2 + n; ++i) {
            this.encode(array[i]);
        }
    }
    
    void flushOutput() throws IOException {
        final int outputIndex = this.outputIndex;
        final byte[] outBuffer = this.outBuffer;
        if (outputIndex < outBuffer.length) {
            this.out.write(outBuffer, 0, outputIndex);
        }
        else {
            this.out.write(outBuffer);
        }
        this.outputIndex = 0;
    }
    
    void initEncoding(final OutputStream out) {
        this.out = out;
        this.pendingSpace = false;
        this.pendingTab = false;
        this.pendingCR = false;
        this.nextSoftBreak = 77;
    }
}
