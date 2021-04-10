package org.apache.james.mime4j.parser;

import java.util.*;
import org.apache.james.mime4j.io.*;
import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.codec.*;
import java.nio.charset.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.*;

public class MimeTokenStream implements EntityStates, RecursionMode
{
    private final MimeEntityConfig config;
    private EntityStateMachine currentStateMachine;
    private final LinkedList<EntityStateMachine> entities;
    private BufferedLineReaderInputStream inbuffer;
    private int recursionMode;
    private int state;
    
    public MimeTokenStream() {
        this(new MimeEntityConfig());
    }
    
    protected MimeTokenStream(final MimeEntityConfig config) {
        this.entities = new LinkedList<EntityStateMachine>();
        this.state = -1;
        this.recursionMode = 0;
        this.config = config;
    }
    
    public static final MimeTokenStream createMaximalDescriptorStream() {
        final MimeEntityConfig mimeEntityConfig = new MimeEntityConfig();
        mimeEntityConfig.setMaximalBodyDescriptor(true);
        return new MimeTokenStream(mimeEntityConfig);
    }
    
    public static final MimeTokenStream createStrictValidationStream() {
        final MimeEntityConfig mimeEntityConfig = new MimeEntityConfig();
        mimeEntityConfig.setStrictParsing(true);
        return new MimeTokenStream(mimeEntityConfig);
    }
    
    private void doParse(InputStream inputStream, final String s) {
        this.entities.clear();
        LineNumberInputStream lineNumberInputStream;
        if (this.config.isCountLineNumbers()) {
            inputStream = (lineNumberInputStream = new LineNumberInputStream(inputStream));
        }
        else {
            lineNumberInputStream = null;
        }
        this.inbuffer = new BufferedLineReaderInputStream(inputStream, 4096, this.config.getMaxLineLen());
        final int recursionMode = this.recursionMode;
        Label_0151: {
            EntityStateMachine currentStateMachine = null;
            Label_0146: {
                if (recursionMode != 0 && recursionMode != 1) {
                    if (recursionMode == 2) {
                        currentStateMachine = new RawEntity(this.inbuffer);
                        break Label_0146;
                    }
                    if (recursionMode != 3) {
                        break Label_0151;
                    }
                }
                final MimeEntity mimeEntity = new MimeEntity(lineNumberInputStream, this.inbuffer, null, 0, 1, this.config);
                mimeEntity.setRecursionMode(this.recursionMode);
                currentStateMachine = mimeEntity;
                if (s != null) {
                    mimeEntity.skipHeader(s);
                    currentStateMachine = mimeEntity;
                }
            }
            this.currentStateMachine = currentStateMachine;
        }
        this.entities.add(this.currentStateMachine);
        this.state = this.currentStateMachine.getState();
    }
    
    public static final String stateToString(final int n) {
        return AbstractEntity.stateToString(n);
    }
    
    public BodyDescriptor getBodyDescriptor() {
        return this.currentStateMachine.getBodyDescriptor();
    }
    
    public InputStream getDecodedInputStream() {
        final String transferEncoding = this.getBodyDescriptor().getTransferEncoding();
        final InputStream contentStream = this.currentStateMachine.getContentStream();
        if (MimeUtil.isBase64Encoding(transferEncoding)) {
            return new Base64InputStream(contentStream);
        }
        if (MimeUtil.isQuotedPrintableEncoded(transferEncoding)) {
            return new QuotedPrintableInputStream(contentStream);
        }
        return contentStream;
    }
    
    public Field getField() {
        return this.currentStateMachine.getField();
    }
    
    public InputStream getInputStream() {
        return this.currentStateMachine.getContentStream();
    }
    
    public Reader getReader() {
        final String charset = this.getBodyDescriptor().getCharset();
        Charset charset2;
        if (charset != null && !"".equals(charset)) {
            charset2 = Charset.forName(charset);
        }
        else {
            charset2 = CharsetUtil.US_ASCII;
        }
        return new InputStreamReader(this.getDecodedInputStream(), charset2);
    }
    
    public int getRecursionMode() {
        return this.recursionMode;
    }
    
    public int getState() {
        return this.state;
    }
    
    public boolean isRaw() {
        return this.recursionMode == 2;
    }
    
    public int next() throws IOException, MimeException {
        if (this.state == -1 || this.currentStateMachine == null) {
            throw new IllegalStateException("No more tokens are available.");
        }
        while (true) {
            final EntityStateMachine currentStateMachine = this.currentStateMachine;
            if (currentStateMachine == null) {
                return this.state = -1;
            }
            final EntityStateMachine advance = currentStateMachine.advance();
            if (advance != null) {
                this.entities.add(advance);
                this.currentStateMachine = advance;
            }
            final int state = this.currentStateMachine.getState();
            if ((this.state = state) != -1) {
                return state;
            }
            this.entities.removeLast();
            if (this.entities.isEmpty()) {
                this.currentStateMachine = null;
            }
            else {
                (this.currentStateMachine = this.entities.getLast()).setRecursionMode(this.recursionMode);
            }
        }
    }
    
    public void parse(final InputStream inputStream) {
        this.doParse(inputStream, null);
    }
    
    public void parseHeadless(final InputStream inputStream, final String s) {
        if (s != null) {
            this.doParse(inputStream, s);
            return;
        }
        throw new IllegalArgumentException("Content type may not be null");
    }
    
    public void setRecursionMode(final int n) {
        this.recursionMode = n;
        final EntityStateMachine currentStateMachine = this.currentStateMachine;
        if (currentStateMachine != null) {
            currentStateMachine.setRecursionMode(n);
        }
    }
    
    public void stop() {
        this.inbuffer.truncate();
    }
}
