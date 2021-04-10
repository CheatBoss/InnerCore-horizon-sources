package org.apache.james.mime4j.parser;

import java.util.*;
import org.apache.commons.logging.*;
import java.io.*;
import org.apache.james.mime4j.*;
import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.io.*;
import org.apache.james.mime4j.util.*;

public abstract class AbstractEntity implements EntityStateMachine
{
    private static final int T_IN_BODYPART = -2;
    private static final int T_IN_MESSAGE = -3;
    private static final BitSet fieldChars;
    protected final MutableBodyDescriptor body;
    protected final MimeEntityConfig config;
    private boolean endOfHeader;
    protected final int endState;
    private Field field;
    private int headerCount;
    private int lineCount;
    private final ByteArrayBuffer linebuf;
    protected final Log log;
    protected final BodyDescriptor parent;
    protected final int startState;
    protected int state;
    
    static {
        fieldChars = new BitSet();
        for (int i = 33; i <= 57; ++i) {
            AbstractEntity.fieldChars.set(i);
        }
        for (int j = 59; j <= 126; ++j) {
            AbstractEntity.fieldChars.set(j);
        }
    }
    
    AbstractEntity(final BodyDescriptor parent, final int n, final int endState, final MimeEntityConfig config) {
        this.log = LogFactory.getLog((Class)this.getClass());
        this.parent = parent;
        this.state = n;
        this.startState = n;
        this.endState = endState;
        this.config = config;
        this.body = this.newBodyDescriptor(parent);
        this.linebuf = new ByteArrayBuffer(64);
        this.lineCount = 0;
        this.endOfHeader = false;
        this.headerCount = 0;
    }
    
    private ByteArrayBuffer fillFieldBuffer() throws IOException, MimeException {
        if (this.endOfHeader) {
            throw new IllegalStateException();
        }
        final int maxLineLen = this.config.getMaxLineLen();
        final LineReaderInputStream dataStream = this.getDataStream();
        final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(64);
        while (true) {
            final int length = this.linebuf.length();
            if (maxLineLen > 0 && byteArrayBuffer.length() + length >= maxLineLen) {
                throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
            if (length > 0) {
                byteArrayBuffer.append(this.linebuf.buffer(), 0, length);
            }
            this.linebuf.clear();
            if (dataStream.readLine(this.linebuf) == -1) {
                this.monitor(Event.HEADERS_PREMATURE_END);
                this.endOfHeader = true;
                return byteArrayBuffer;
            }
            final int length2 = this.linebuf.length();
            int n;
            if ((n = length2) > 0) {
                final ByteArrayBuffer linebuf = this.linebuf;
                final int n2 = length2 - 1;
                n = length2;
                if (linebuf.byteAt(n2) == 10) {
                    n = n2;
                }
            }
            int n3;
            if ((n3 = n) > 0) {
                final ByteArrayBuffer linebuf2 = this.linebuf;
                final int n4 = n - 1;
                n3 = n;
                if (linebuf2.byteAt(n4) == 13) {
                    n3 = n4;
                }
            }
            if (n3 == 0) {
                this.endOfHeader = true;
                return byteArrayBuffer;
            }
            if (++this.lineCount <= 1) {
                continue;
            }
            final byte byte1 = this.linebuf.byteAt(0);
            if (byte1 != 32 && byte1 != 9) {
                return byteArrayBuffer;
            }
        }
    }
    
    public static final String stateToString(final int n) {
        switch (n) {
            default: {
                return "Unknown";
            }
            case 12: {
                return "Body";
            }
            case 11: {
                return "End bodypart";
            }
            case 10: {
                return "Start bodypart";
            }
            case 9: {
                return "Epilogue";
            }
            case 8: {
                return "Preamble";
            }
            case 7: {
                return "End multipart";
            }
            case 6: {
                return "Start multipart";
            }
            case 5: {
                return "End header";
            }
            case 4: {
                return "Field";
            }
            case 3: {
                return "Start header";
            }
            case 2: {
                return "Raw entity";
            }
            case 1: {
                return "End message";
            }
            case 0: {
                return "Start message";
            }
            case -1: {
                return "End of stream";
            }
            case -2: {
                return "Bodypart";
            }
            case -3: {
                return "In message";
            }
        }
    }
    
    protected void debug(final Event event) {
        if (this.log.isDebugEnabled()) {
            this.log.debug((Object)this.message(event));
        }
    }
    
    @Override
    public BodyDescriptor getBodyDescriptor() {
        final int state = this.getState();
        if (state != -1 && state != 6 && state != 12 && state != 8 && state != 9) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid state :");
            sb.append(stateToString(this.state));
            throw new IllegalStateException(sb.toString());
        }
        return this.body;
    }
    
    protected abstract LineReaderInputStream getDataStream();
    
    @Override
    public Field getField() {
        if (this.getState() == 4) {
            return this.field;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid state :");
        sb.append(stateToString(this.state));
        throw new IllegalStateException(sb.toString());
    }
    
    protected abstract int getLineNumber();
    
    @Override
    public int getState() {
        return this.state;
    }
    
    protected String message(final Event event) {
        String string;
        if (event == null) {
            string = "Event is unexpectedly null.";
        }
        else {
            string = event.toString();
        }
        final int lineNumber = this.getLineNumber();
        if (lineNumber <= 0) {
            return string;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Line ");
        sb.append(lineNumber);
        sb.append(": ");
        sb.append(string);
        return sb.toString();
    }
    
    protected void monitor(final Event event) throws MimeException, IOException {
        if (!this.config.isStrictParsing()) {
            this.warn(event);
            return;
        }
        throw new MimeParseEventException(event);
    }
    
    protected MutableBodyDescriptor newBodyDescriptor(final BodyDescriptor bodyDescriptor) {
        if (this.config.isMaximalBodyDescriptor()) {
            return new MaximalBodyDescriptor(bodyDescriptor);
        }
        return new DefaultBodyDescriptor(bodyDescriptor);
    }
    
    protected boolean parseField() throws MimeException, IOException {
        final int maxHeaderCount = this.config.getMaxHeaderCount();
        boolean b;
        ByteArrayBuffer fillFieldBuffer;
        int index;
        do {
            final boolean endOfHeader = this.endOfHeader;
            final boolean b2 = false;
            if (endOfHeader) {
                return false;
            }
            if (this.headerCount >= maxHeaderCount) {
                throw new MaxHeaderLimitException("Maximum header limit exceeded");
            }
            fillFieldBuffer = this.fillFieldBuffer();
            ++this.headerCount;
            final int length = fillFieldBuffer.length();
            int n;
            if ((n = length) > 0) {
                final int n2 = length - 1;
                n = length;
                if (fillFieldBuffer.byteAt(n2) == 10) {
                    n = n2;
                }
            }
            int length2;
            if ((length2 = n) > 0) {
                final int n3 = n - 1;
                length2 = n;
                if (fillFieldBuffer.byteAt(n3) == 13) {
                    length2 = n3;
                }
            }
            fillFieldBuffer.setLength(length2);
            index = fillFieldBuffer.indexOf((byte)58);
            Label_0129: {
                if (index > 0) {
                    for (int i = 0; i < index; ++i) {
                        if (!AbstractEntity.fieldChars.get(fillFieldBuffer.byteAt(i) & 0xFF)) {
                            break Label_0129;
                        }
                    }
                    b = true;
                    continue;
                }
            }
            this.monitor(Event.INALID_HEADER);
            b = b2;
        } while (!b);
        final RawField field = new RawField(fillFieldBuffer, index);
        this.field = field;
        this.body.addField(field);
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" [");
        sb.append(stateToString(this.state));
        sb.append("][");
        sb.append(this.body.getMimeType());
        sb.append("][");
        sb.append(this.body.getBoundary());
        sb.append("]");
        return sb.toString();
    }
    
    protected void warn(final Event event) {
        if (this.log.isWarnEnabled()) {
            this.log.warn((Object)this.message(event));
        }
    }
}
