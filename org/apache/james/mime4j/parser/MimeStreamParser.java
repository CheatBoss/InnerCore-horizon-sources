package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.*;
import java.io.*;

public class MimeStreamParser
{
    private boolean contentDecoding;
    private ContentHandler handler;
    private final MimeTokenStream mimeTokenStream;
    
    public MimeStreamParser() {
        this(null);
    }
    
    public MimeStreamParser(MimeEntityConfig clone) {
        this.handler = null;
        if (clone != null) {
            clone = clone.clone();
        }
        else {
            clone = new MimeEntityConfig();
        }
        this.mimeTokenStream = new MimeTokenStream(clone);
        this.contentDecoding = false;
    }
    
    public boolean isContentDecoding() {
        return this.contentDecoding;
    }
    
    public boolean isRaw() {
        return this.mimeTokenStream.isRaw();
    }
    
    public void parse(InputStream inputStream) throws MimeException, IOException {
        this.mimeTokenStream.parse(inputStream);
        while (true) {
            final int state = this.mimeTokenStream.getState();
            switch (state) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid state: ");
                    sb.append(state);
                    throw new IllegalStateException(sb.toString());
                }
                case 12: {
                    final BodyDescriptor bodyDescriptor = this.mimeTokenStream.getBodyDescriptor();
                    if (this.contentDecoding) {
                        inputStream = this.mimeTokenStream.getDecodedInputStream();
                    }
                    else {
                        inputStream = this.mimeTokenStream.getInputStream();
                    }
                    this.handler.body(bodyDescriptor, inputStream);
                    break;
                }
                case 11: {
                    this.handler.endBodyPart();
                    break;
                }
                case 10: {
                    this.handler.startBodyPart();
                    break;
                }
                case 9: {
                    this.handler.epilogue(this.mimeTokenStream.getInputStream());
                    break;
                }
                case 8: {
                    this.handler.preamble(this.mimeTokenStream.getInputStream());
                    break;
                }
                case 7: {
                    this.handler.endMultipart();
                    break;
                }
                case 6: {
                    this.handler.startMultipart(this.mimeTokenStream.getBodyDescriptor());
                    break;
                }
                case 5: {
                    this.handler.endHeader();
                    break;
                }
                case 4: {
                    this.handler.field(this.mimeTokenStream.getField());
                    break;
                }
                case 3: {
                    this.handler.startHeader();
                    break;
                }
                case 2: {
                    this.handler.raw(this.mimeTokenStream.getInputStream());
                    break;
                }
                case 1: {
                    this.handler.endMessage();
                    break;
                }
                case 0: {
                    this.handler.startMessage();
                    break;
                }
                case -1: {
                    return;
                }
            }
            this.mimeTokenStream.next();
        }
    }
    
    public void setContentDecoding(final boolean contentDecoding) {
        this.contentDecoding = contentDecoding;
    }
    
    public void setContentHandler(final ContentHandler handler) {
        this.handler = handler;
    }
    
    public void setRaw(final boolean b) {
        this.mimeTokenStream.setRecursionMode(2);
    }
    
    public void stop() {
        this.mimeTokenStream.stop();
    }
}
