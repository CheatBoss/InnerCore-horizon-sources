package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.descriptor.*;
import java.io.*;
import org.apache.james.mime4j.*;
import org.apache.james.mime4j.codec.*;
import org.apache.james.mime4j.io.*;
import org.apache.james.mime4j.util.*;

public class MimeEntity extends AbstractEntity
{
    private static final int T_IN_BODYPART = -2;
    private static final int T_IN_MESSAGE = -3;
    private LineReaderInputStreamAdaptor dataStream;
    private final BufferedLineReaderInputStream inbuffer;
    private final LineNumberSource lineSource;
    private MimeBoundaryInputStream mimeStream;
    private int recursionMode;
    private boolean skipHeader;
    private byte[] tmpbuf;
    
    public MimeEntity(final LineNumberSource lineNumberSource, final BufferedLineReaderInputStream bufferedLineReaderInputStream, final BodyDescriptor bodyDescriptor, final int n, final int n2) {
        this(lineNumberSource, bufferedLineReaderInputStream, bodyDescriptor, n, n2, new MimeEntityConfig());
    }
    
    public MimeEntity(final LineNumberSource lineSource, final BufferedLineReaderInputStream inbuffer, final BodyDescriptor bodyDescriptor, final int n, final int n2, final MimeEntityConfig mimeEntityConfig) {
        super(bodyDescriptor, n, n2, mimeEntityConfig);
        this.lineSource = lineSource;
        this.inbuffer = inbuffer;
        this.dataStream = new LineReaderInputStreamAdaptor(inbuffer, mimeEntityConfig.getMaxLineLen());
        this.skipHeader = false;
    }
    
    private void advanceToBoundary() throws IOException {
        if (!this.dataStream.eof()) {
            if (this.tmpbuf == null) {
                this.tmpbuf = new byte[2048];
            }
            while (this.getLimitedContentStream().read(this.tmpbuf) != -1) {}
        }
    }
    
    private void clearMimeStream() {
        this.mimeStream = null;
        this.dataStream = new LineReaderInputStreamAdaptor(this.inbuffer, this.config.getMaxLineLen());
    }
    
    private void createMimeStream() throws MimeException, IOException {
        final String boundary = this.body.getBoundary();
        int n;
        if ((n = boundary.length() * 2) < 4096) {
            n = 4096;
        }
        try {
            if (this.mimeStream != null) {
                this.mimeStream = new MimeBoundaryInputStream(new BufferedLineReaderInputStream(this.mimeStream, n, this.config.getMaxLineLen()), boundary);
            }
            else {
                this.inbuffer.ensureCapacity(n);
                this.mimeStream = new MimeBoundaryInputStream(this.inbuffer, boundary);
            }
            this.dataStream = new LineReaderInputStreamAdaptor(this.mimeStream, this.config.getMaxLineLen());
        }
        catch (IllegalArgumentException ex) {
            throw new MimeException(ex.getMessage(), ex);
        }
    }
    
    private InputStream getLimitedContentStream() {
        final long maxContentLen = this.config.getMaxContentLen();
        if (maxContentLen >= 0L) {
            return new LimitedInputStream(this.dataStream, maxContentLen);
        }
        return this.dataStream;
    }
    
    private EntityStateMachine nextMessage() {
        final String transferEncoding = this.body.getTransferEncoding();
        InputStream dataStream;
        if (MimeUtil.isBase64Encoding(transferEncoding)) {
            this.log.debug((Object)"base64 encoded message/rfc822 detected");
            dataStream = new Base64InputStream(this.dataStream);
        }
        else if (MimeUtil.isQuotedPrintableEncoded(transferEncoding)) {
            this.log.debug((Object)"quoted-printable encoded message/rfc822 detected");
            dataStream = new QuotedPrintableInputStream(this.dataStream);
        }
        else {
            dataStream = this.dataStream;
        }
        if (this.recursionMode == 2) {
            return new RawEntity(dataStream);
        }
        final MimeEntity mimeEntity = new MimeEntity(this.lineSource, new BufferedLineReaderInputStream(dataStream, 4096, this.config.getMaxLineLen()), this.body, 0, 1, this.config);
        mimeEntity.setRecursionMode(this.recursionMode);
        return mimeEntity;
    }
    
    private EntityStateMachine nextMimeEntity() {
        if (this.recursionMode == 2) {
            return new RawEntity(this.mimeStream);
        }
        final MimeEntity mimeEntity = new MimeEntity(this.lineSource, new BufferedLineReaderInputStream(this.mimeStream, 4096, this.config.getMaxLineLen()), this.body, 10, 11, this.config);
        mimeEntity.setRecursionMode(this.recursionMode);
        return mimeEntity;
    }
    
    @Override
    public EntityStateMachine advance() throws IOException, MimeException {
        final int state = this.state;
        int endState = 0;
        Label_0393: {
            Label_0388: {
                if (state != -3) {
                    if (state != -2) {
                        int state2 = 5;
                        Label_0307: {
                            if (state != 0) {
                                if (state == 12) {
                                    break Label_0388;
                                }
                                while (true) {
                                    switch (state) {
                                        default: {
                                            if (this.state == this.endState) {
                                                endState = -1;
                                                break Label_0393;
                                            }
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("Invalid state: ");
                                            sb.append(AbstractEntity.stateToString(this.state));
                                            throw new IllegalStateException(sb.toString());
                                        }
                                        case 9: {
                                            this.state = 7;
                                            return null;
                                        }
                                        case 8: {
                                            this.advanceToBoundary();
                                            if (this.mimeStream.isLastPart()) {
                                                this.clearMimeStream();
                                                continue;
                                            }
                                            this.clearMimeStream();
                                            this.createMimeStream();
                                            this.state = -2;
                                            return this.nextMimeEntity();
                                        }
                                        case 6: {
                                            if (this.dataStream.isUsed()) {
                                                this.advanceToBoundary();
                                                continue;
                                            }
                                            this.createMimeStream();
                                            endState = 8;
                                            break Label_0393;
                                        }
                                        case 5: {
                                            final String mimeType = this.body.getMimeType();
                                            if (this.recursionMode != 3) {
                                                if (MimeUtil.isMultipart(mimeType)) {
                                                    this.state = 6;
                                                    this.clearMimeStream();
                                                    return null;
                                                }
                                                if (this.recursionMode != 1 && MimeUtil.isMessage(mimeType)) {
                                                    this.state = -3;
                                                    return this.nextMessage();
                                                }
                                            }
                                            this.state = 12;
                                            return null;
                                        }
                                        case 3:
                                        case 4: {
                                            if (this.parseField()) {
                                                state2 = 4;
                                                break;
                                            }
                                            break;
                                        }
                                        case 10: {
                                            break Label_0307;
                                        }
                                        case 7: {
                                            break Label_0388;
                                        }
                                    }
                                    break;
                                }
                            }
                            else if (!this.skipHeader) {
                                break Label_0307;
                            }
                            this.state = state2;
                            return null;
                        }
                        this.state = 3;
                        return null;
                    }
                    this.advanceToBoundary();
                    if (this.mimeStream.eof() && !this.mimeStream.isLastPart()) {
                        this.monitor(Event.MIME_BODY_PREMATURE_END);
                    }
                    else if (!this.mimeStream.isLastPart()) {
                        this.clearMimeStream();
                        this.createMimeStream();
                        this.state = -2;
                        return this.nextMimeEntity();
                    }
                    this.clearMimeStream();
                    endState = 9;
                    break Label_0393;
                }
            }
            endState = this.endState;
        }
        this.state = endState;
        return null;
    }
    
    @Override
    public InputStream getContentStream() {
        final int state = this.state;
        if (state != 6 && state != 12 && state != 8 && state != 9) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid state: ");
            sb.append(AbstractEntity.stateToString(this.state));
            throw new IllegalStateException(sb.toString());
        }
        return this.getLimitedContentStream();
    }
    
    @Override
    protected LineReaderInputStream getDataStream() {
        return this.dataStream;
    }
    
    @Override
    protected int getLineNumber() {
        final LineNumberSource lineSource = this.lineSource;
        if (lineSource == null) {
            return -1;
        }
        return lineSource.getLineNumber();
    }
    
    public int getRecursionMode() {
        return this.recursionMode;
    }
    
    @Override
    public void setRecursionMode(final int recursionMode) {
        this.recursionMode = recursionMode;
    }
    
    public void skipHeader(final String s) {
        if (this.state == 0) {
            this.skipHeader = true;
            final StringBuilder sb = new StringBuilder();
            sb.append("Content-Type: ");
            sb.append(s);
            this.body.addField(new RawField(ContentUtil.encode(sb.toString()), 12));
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid state: ");
        sb2.append(AbstractEntity.stateToString(this.state));
        throw new IllegalStateException(sb2.toString());
    }
}
