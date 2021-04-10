package org.apache.james.mime4j.message;

import java.util.*;
import org.apache.james.mime4j.storage.*;
import org.apache.james.mime4j.util.*;
import java.io.*;
import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.codec.*;
import org.apache.james.mime4j.*;
import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.field.*;

public class MessageBuilder implements ContentHandler
{
    private final BodyFactory bodyFactory;
    private final Entity entity;
    private Stack<Object> stack;
    
    public MessageBuilder(final Entity entity) {
        this.stack = new Stack<Object>();
        this.entity = entity;
        this.bodyFactory = new BodyFactory();
    }
    
    public MessageBuilder(final Entity entity, final StorageProvider storageProvider) {
        this.stack = new Stack<Object>();
        this.entity = entity;
        this.bodyFactory = new BodyFactory(storageProvider);
    }
    
    private void expect(final Class<?> clazz) {
        if (clazz.isInstance(this.stack.peek())) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Internal stack error: Expected '");
        sb.append(clazz.getName());
        sb.append("' found '");
        sb.append(this.stack.peek().getClass().getName());
        sb.append("'");
        throw new IllegalStateException(sb.toString());
    }
    
    private static ByteSequence loadStream(final InputStream inputStream) throws IOException {
        final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(64);
        while (true) {
            final int read = inputStream.read();
            if (read == -1) {
                break;
            }
            byteArrayBuffer.append(read);
        }
        return byteArrayBuffer;
    }
    
    @Override
    public void body(final BodyDescriptor bodyDescriptor, final InputStream inputStream) throws MimeException, IOException {
        this.expect(Entity.class);
        final String transferEncoding = bodyDescriptor.getTransferEncoding();
        InputStream inputStream3 = null;
        Label_0062: {
            InputStream inputStream2;
            if ("base64".equals(transferEncoding)) {
                inputStream2 = new Base64InputStream(inputStream);
            }
            else {
                inputStream3 = inputStream;
                if (!"quoted-printable".equals(transferEncoding)) {
                    break Label_0062;
                }
                inputStream2 = new QuotedPrintableInputStream(inputStream);
            }
            inputStream3 = inputStream2;
        }
        SingleBody body;
        if (bodyDescriptor.getMimeType().startsWith("text/")) {
            body = this.bodyFactory.textBody(inputStream3, bodyDescriptor.getCharset());
        }
        else {
            body = this.bodyFactory.binaryBody(inputStream3);
        }
        this.stack.peek().setBody(body);
    }
    
    @Override
    public void endBodyPart() throws MimeException {
        this.expect(BodyPart.class);
        this.stack.pop();
    }
    
    @Override
    public void endHeader() throws MimeException {
        this.expect(Header.class);
        final Header header = this.stack.pop();
        this.expect(Entity.class);
        this.stack.peek().setHeader(header);
    }
    
    @Override
    public void endMessage() throws MimeException {
        this.expect(Message.class);
        this.stack.pop();
    }
    
    @Override
    public void endMultipart() throws MimeException {
        this.stack.pop();
    }
    
    @Override
    public void epilogue(final InputStream inputStream) throws MimeException, IOException {
        this.expect(Multipart.class);
        this.stack.peek().setEpilogueRaw(loadStream(inputStream));
    }
    
    @Override
    public void field(final Field field) throws MimeException {
        this.expect(Header.class);
        this.stack.peek().addField(AbstractField.parse(field.getRaw()));
    }
    
    @Override
    public void preamble(final InputStream inputStream) throws MimeException, IOException {
        this.expect(Multipart.class);
        this.stack.peek().setPreambleRaw(loadStream(inputStream));
    }
    
    @Override
    public void raw(final InputStream inputStream) throws MimeException, IOException {
        throw new UnsupportedOperationException("Not supported");
    }
    
    @Override
    public void startBodyPart() throws MimeException {
        this.expect(Multipart.class);
        final BodyPart bodyPart = new BodyPart();
        this.stack.peek().addBodyPart(bodyPart);
        this.stack.push(bodyPart);
    }
    
    @Override
    public void startHeader() throws MimeException {
        this.stack.push(new Header());
    }
    
    @Override
    public void startMessage() throws MimeException {
        if (this.stack.isEmpty()) {
            this.stack.push(this.entity);
            return;
        }
        this.expect(Entity.class);
        final Message body = new Message();
        this.stack.peek().setBody(body);
        this.stack.push(body);
    }
    
    @Override
    public void startMultipart(final BodyDescriptor bodyDescriptor) throws MimeException {
        this.expect(Entity.class);
        final Entity entity = this.stack.peek();
        final Multipart body = new Multipart(bodyDescriptor.getSubType());
        entity.setBody(body);
        this.stack.push(body);
    }
}
