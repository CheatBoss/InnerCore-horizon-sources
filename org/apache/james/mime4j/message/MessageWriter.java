package org.apache.james.mime4j.message;

import org.apache.james.mime4j.field.*;
import java.io.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.codec.*;
import org.apache.james.mime4j.parser.*;
import java.util.*;

public class MessageWriter
{
    private static final byte[] CRLF;
    private static final byte[] DASHES;
    public static final MessageWriter DEFAULT;
    
    static {
        CRLF = new byte[] { 13, 10 };
        DASHES = new byte[] { 45, 45 };
        DEFAULT = new MessageWriter();
    }
    
    protected MessageWriter() {
    }
    
    private ByteSequence getBoundary(final ContentTypeField contentTypeField) {
        final String boundary = contentTypeField.getBoundary();
        if (boundary != null) {
            return ContentUtil.encode(boundary);
        }
        throw new IllegalArgumentException("Multipart boundary not specified");
    }
    
    private ContentTypeField getContentType(final Multipart multipart) {
        final Entity parent = multipart.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Missing parent entity in multipart");
        }
        final Header header = parent.getHeader();
        if (header == null) {
            throw new IllegalArgumentException("Missing header in parent entity");
        }
        final ContentTypeField contentTypeField = (ContentTypeField)header.getField("Content-Type");
        if (contentTypeField != null) {
            return contentTypeField;
        }
        throw new IllegalArgumentException("Content-Type field not specified");
    }
    
    private void writeBytes(final ByteSequence byteSequence, final OutputStream outputStream) throws IOException {
        if (byteSequence instanceof ByteArrayBuffer) {
            final ByteArrayBuffer byteArrayBuffer = (ByteArrayBuffer)byteSequence;
            outputStream.write(byteArrayBuffer.buffer(), 0, byteArrayBuffer.length());
            return;
        }
        outputStream.write(byteSequence.toByteArray());
    }
    
    protected OutputStream encodeStream(final OutputStream outputStream, final String s, final boolean b) throws IOException {
        if (MimeUtil.isBase64Encoding(s)) {
            return CodecUtil.wrapBase64(outputStream);
        }
        OutputStream wrapQuotedPrintable = outputStream;
        if (MimeUtil.isQuotedPrintableEncoded(s)) {
            wrapQuotedPrintable = CodecUtil.wrapQuotedPrintable(outputStream, b);
        }
        return wrapQuotedPrintable;
    }
    
    public void writeBody(final Body body, final OutputStream outputStream) throws IOException {
        if (body instanceof Message) {
            this.writeEntity((Entity)body, outputStream);
            return;
        }
        if (body instanceof Multipart) {
            this.writeMultipart((Multipart)body, outputStream);
            return;
        }
        if (body instanceof SingleBody) {
            ((SingleBody)body).writeTo(outputStream);
            return;
        }
        throw new IllegalArgumentException("Unsupported body class");
    }
    
    public void writeEntity(final Entity entity, final OutputStream outputStream) throws IOException {
        final Header header = entity.getHeader();
        if (header == null) {
            throw new IllegalArgumentException("Missing header");
        }
        this.writeHeader(header, outputStream);
        final Body body = entity.getBody();
        if (body != null) {
            final OutputStream encodeStream = this.encodeStream(outputStream, entity.getContentTransferEncoding(), body instanceof BinaryBody);
            this.writeBody(body, encodeStream);
            if (encodeStream != outputStream) {
                encodeStream.close();
            }
            return;
        }
        throw new IllegalArgumentException("Missing body");
    }
    
    public void writeHeader(final Header header, final OutputStream outputStream) throws IOException {
        final Iterator<Field> iterator = header.iterator();
        while (iterator.hasNext()) {
            this.writeBytes(iterator.next().getRaw(), outputStream);
            outputStream.write(MessageWriter.CRLF);
        }
        outputStream.write(MessageWriter.CRLF);
    }
    
    public void writeMultipart(final Multipart multipart, final OutputStream outputStream) throws IOException {
        final ByteSequence boundary = this.getBoundary(this.getContentType(multipart));
        this.writeBytes(multipart.getPreambleRaw(), outputStream);
        outputStream.write(MessageWriter.CRLF);
        for (final BodyPart bodyPart : multipart.getBodyParts()) {
            outputStream.write(MessageWriter.DASHES);
            this.writeBytes(boundary, outputStream);
            outputStream.write(MessageWriter.CRLF);
            this.writeEntity(bodyPart, outputStream);
            outputStream.write(MessageWriter.CRLF);
        }
        outputStream.write(MessageWriter.DASHES);
        this.writeBytes(boundary, outputStream);
        outputStream.write(MessageWriter.DASHES);
        outputStream.write(MessageWriter.CRLF);
        this.writeBytes(multipart.getEpilogueRaw(), outputStream);
    }
}
