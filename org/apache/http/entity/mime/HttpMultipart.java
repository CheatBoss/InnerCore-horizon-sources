package org.apache.http.entity.mime;

import org.apache.james.mime4j.parser.*;
import java.nio.charset.*;
import java.util.*;
import java.nio.*;
import org.apache.james.mime4j.field.*;
import org.apache.james.mime4j.util.*;
import org.apache.http.entity.mime.content.*;
import java.io.*;
import org.apache.james.mime4j.message.*;

public class HttpMultipart extends Multipart
{
    private static final ByteArrayBuffer CR_LF;
    private static final ByteArrayBuffer TWO_DASHES;
    private HttpMultipartMode mode;
    
    static {
        CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
        TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
    }
    
    public HttpMultipart(final String s) {
        super(s);
        this.mode = HttpMultipartMode.STRICT;
    }
    
    private void doWriteTo(final HttpMultipartMode httpMultipartMode, final OutputStream outputStream, final boolean b) throws IOException {
        final List<BodyPart> bodyParts = this.getBodyParts();
        final Charset charset = this.getCharset();
        final ByteArrayBuffer encode = encode(charset, this.getBoundary());
        final int n = HttpMultipart$1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[httpMultipartMode.ordinal()];
        final boolean b2 = false;
        int i = 0;
        if (n == 1) {
            final String preamble = this.getPreamble();
            int j = b2 ? 1 : 0;
            if (preamble != null) {
                j = (b2 ? 1 : 0);
                if (preamble.length() != 0) {
                    writeBytes(encode(charset, preamble), outputStream);
                    writeBytes(HttpMultipart.CR_LF, outputStream);
                    j = (b2 ? 1 : 0);
                }
            }
            while (j < bodyParts.size()) {
                writeBytes(HttpMultipart.TWO_DASHES, outputStream);
                writeBytes(encode, outputStream);
                writeBytes(HttpMultipart.CR_LF, outputStream);
                final BodyPart bodyPart = bodyParts.get(j);
                final Iterator<Field> iterator = bodyPart.getHeader().getFields().iterator();
                while (iterator.hasNext()) {
                    writeBytes(iterator.next().getRaw(), outputStream);
                    writeBytes(HttpMultipart.CR_LF, outputStream);
                }
                writeBytes(HttpMultipart.CR_LF, outputStream);
                if (b) {
                    MessageWriter.DEFAULT.writeBody(bodyPart.getBody(), outputStream);
                }
                writeBytes(HttpMultipart.CR_LF, outputStream);
                ++j;
            }
            writeBytes(HttpMultipart.TWO_DASHES, outputStream);
            writeBytes(encode, outputStream);
            writeBytes(HttpMultipart.TWO_DASHES, outputStream);
            writeBytes(HttpMultipart.CR_LF, outputStream);
            final String epilogue = this.getEpilogue();
            if (epilogue != null && epilogue.length() != 0) {
                writeBytes(encode(charset, epilogue), outputStream);
                writeBytes(HttpMultipart.CR_LF, outputStream);
            }
            return;
        }
        if (n != 2) {
            return;
        }
        while (i < bodyParts.size()) {
            writeBytes(HttpMultipart.TWO_DASHES, outputStream);
            writeBytes(encode, outputStream);
            writeBytes(HttpMultipart.CR_LF, outputStream);
            final BodyPart bodyPart2 = bodyParts.get(i);
            final Field field = bodyPart2.getHeader().getField("Content-Disposition");
            final StringBuilder sb = new StringBuilder();
            sb.append(field.getName());
            sb.append(": ");
            sb.append(field.getBody());
            writeBytes(encode(charset, sb.toString()), outputStream);
            writeBytes(HttpMultipart.CR_LF, outputStream);
            writeBytes(HttpMultipart.CR_LF, outputStream);
            if (b) {
                MessageWriter.DEFAULT.writeBody(bodyPart2.getBody(), outputStream);
            }
            writeBytes(HttpMultipart.CR_LF, outputStream);
            ++i;
        }
        writeBytes(HttpMultipart.TWO_DASHES, outputStream);
        writeBytes(encode, outputStream);
        writeBytes(HttpMultipart.TWO_DASHES, outputStream);
        writeBytes(HttpMultipart.CR_LF, outputStream);
    }
    
    private static ByteArrayBuffer encode(final Charset charset, final String s) {
        final ByteBuffer encode = charset.encode(CharBuffer.wrap(s));
        final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(encode.remaining());
        byteArrayBuffer.append(encode.array(), encode.position(), encode.remaining());
        return byteArrayBuffer;
    }
    
    private static void writeBytes(final ByteArrayBuffer byteArrayBuffer, final OutputStream outputStream) throws IOException {
        outputStream.write(byteArrayBuffer.buffer(), 0, byteArrayBuffer.length());
    }
    
    private static void writeBytes(final ByteSequence byteSequence, final OutputStream outputStream) throws IOException {
        if (byteSequence instanceof ByteArrayBuffer) {
            writeBytes((ByteArrayBuffer)byteSequence, outputStream);
            return;
        }
        outputStream.write(byteSequence.toByteArray());
    }
    
    protected String getBoundary() {
        return ((ContentTypeField)this.getParent().getHeader().getField("Content-Type")).getBoundary();
    }
    
    protected Charset getCharset() {
        final ContentTypeField contentTypeField = (ContentTypeField)this.getParent().getHeader().getField("Content-Type");
        final int n = HttpMultipart$1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[this.mode.ordinal()];
        if (n == 1) {
            return MIME.DEFAULT_CHARSET;
        }
        if (n != 2) {
            return null;
        }
        String charset;
        if (contentTypeField.getCharset() != null) {
            charset = contentTypeField.getCharset();
        }
        else {
            charset = "ISO-8859-1";
        }
        return CharsetUtil.getCharset(charset);
    }
    
    public HttpMultipartMode getMode() {
        return this.mode;
    }
    
    public long getTotalLength() {
        final List<BodyPart> bodyParts = this.getBodyParts();
        long n = 0L;
        int i = 0;
        while (i < bodyParts.size()) {
            final Body body = bodyParts.get(i).getBody();
            if (body instanceof ContentBody) {
                final long contentLength = ((ContentBody)body).getContentLength();
                if (contentLength >= 0L) {
                    ++i;
                    n += contentLength;
                    continue;
                }
            }
            return -1L;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            this.doWriteTo(this.mode, byteArrayOutputStream, false);
            return n + byteArrayOutputStream.toByteArray().length;
        }
        catch (IOException ex) {
            return -1L;
        }
    }
    
    public void setMode(final HttpMultipartMode mode) {
        this.mode = mode;
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        this.doWriteTo(this.mode, outputStream, true);
    }
}
