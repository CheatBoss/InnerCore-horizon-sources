package org.apache.james.mime4j.codec;

import org.apache.commons.logging.*;
import java.io.*;

public class QuotedPrintableInputStream extends InputStream
{
    private static Log log;
    ByteQueue byteq;
    private boolean closed;
    ByteQueue pushbackq;
    private byte state;
    private InputStream stream;
    
    static {
        QuotedPrintableInputStream.log = LogFactory.getLog((Class)QuotedPrintableInputStream.class);
    }
    
    public QuotedPrintableInputStream(final InputStream stream) {
        this.byteq = new ByteQueue();
        this.pushbackq = new ByteQueue();
        this.state = 0;
        this.closed = false;
        this.stream = stream;
    }
    
    private byte asciiCharToNumericValue(final byte b) {
        int n;
        if (b >= 48 && b <= 57) {
            n = b - 48;
        }
        else {
            byte b2 = 97;
            if (b >= 65 && b <= 90) {
                b2 = 65;
            }
            else if (b < 97 || b > 122) {
                final StringBuilder sb = new StringBuilder();
                sb.append((char)b);
                sb.append(" is not a hexadecimal digit");
                throw new IllegalArgumentException(sb.toString());
            }
            n = b - b2 + 10;
        }
        return (byte)n;
    }
    
    private void fillBuffer() throws IOException {
        byte b = 0;
        while (this.byteq.count() == 0) {
            if (this.pushbackq.count() == 0) {
                this.populatePushbackQueue();
                if (this.pushbackq.count() == 0) {
                    return;
                }
            }
            final byte dequeue = this.pushbackq.dequeue();
            final byte state = this.state;
            if (state != 0) {
                if (state != 1) {
                    if (state != 2) {
                        if (state != 3) {
                            final Log log = QuotedPrintableInputStream.log;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Illegal state: ");
                            sb.append(this.state);
                            log.error((Object)sb.toString());
                            this.state = 0;
                        }
                        else {
                            if ((dequeue >= 48 && dequeue <= 57) || (dequeue >= 65 && dequeue <= 70) || (dequeue >= 97 && dequeue <= 102)) {
                                final byte asciiCharToNumericValue = this.asciiCharToNumericValue(b);
                                final byte asciiCharToNumericValue2 = this.asciiCharToNumericValue(dequeue);
                                this.state = 0;
                                this.byteq.enqueue((byte)(asciiCharToNumericValue2 | asciiCharToNumericValue << 4));
                                continue;
                            }
                            if (QuotedPrintableInputStream.log.isWarnEnabled()) {
                                final Log log2 = QuotedPrintableInputStream.log;
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Malformed MIME; expected [0-9A-Z], got ");
                                sb2.append(dequeue);
                                log2.warn((Object)sb2.toString());
                            }
                            this.state = 0;
                            this.byteq.enqueue((byte)61);
                            this.byteq.enqueue(b);
                        }
                    }
                    else {
                        if (dequeue == 10) {
                            this.state = 0;
                            continue;
                        }
                        if (QuotedPrintableInputStream.log.isWarnEnabled()) {
                            final Log log3 = QuotedPrintableInputStream.log;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Malformed MIME; expected 10, got ");
                            sb3.append(dequeue);
                            log3.warn((Object)sb3.toString());
                        }
                        this.state = 0;
                        this.byteq.enqueue((byte)61);
                        this.byteq.enqueue((byte)13);
                    }
                }
                else {
                    if (dequeue == 13) {
                        this.state = 2;
                        continue;
                    }
                    if ((dequeue >= 48 && dequeue <= 57) || (dequeue >= 65 && dequeue <= 70) || (dequeue >= 97 && dequeue <= 102)) {
                        this.state = 3;
                        b = dequeue;
                        continue;
                    }
                    if (dequeue == 61) {
                        if (QuotedPrintableInputStream.log.isWarnEnabled()) {
                            QuotedPrintableInputStream.log.warn((Object)"Malformed MIME; got ==");
                        }
                        this.byteq.enqueue((byte)61);
                        continue;
                    }
                    if (QuotedPrintableInputStream.log.isWarnEnabled()) {
                        final Log log4 = QuotedPrintableInputStream.log;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Malformed MIME; expected \\r or [0-9A-Z], got ");
                        sb4.append(dequeue);
                        log4.warn((Object)sb4.toString());
                    }
                    this.state = 0;
                    this.byteq.enqueue((byte)61);
                }
            }
            else if (dequeue == 61) {
                this.state = 1;
                continue;
            }
            this.byteq.enqueue(dequeue);
        }
    }
    
    private void populatePushbackQueue() throws IOException {
        if (this.pushbackq.count() != 0) {
            return;
        }
        Label_0087: {
            int read;
            while (true) {
                read = this.stream.read();
                if (read == -1) {
                    break Label_0087;
                }
                if (read == 13) {
                    break;
                }
                if (read != 32 && read != 9) {
                    if (read != 10) {
                        this.pushbackq.enqueue((byte)read);
                        return;
                    }
                    break;
                }
                else {
                    this.pushbackq.enqueue((byte)read);
                }
            }
            this.pushbackq.clear();
            this.pushbackq.enqueue((byte)read);
            return;
        }
        this.pushbackq.clear();
    }
    
    @Override
    public void close() throws IOException {
        this.closed = true;
    }
    
    @Override
    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("QuotedPrintableInputStream has been closed");
        }
        this.fillBuffer();
        if (this.byteq.count() == 0) {
            return -1;
        }
        final byte dequeue = this.byteq.dequeue();
        if (dequeue >= 0) {
            return dequeue;
        }
        return dequeue & 0xFF;
    }
}
