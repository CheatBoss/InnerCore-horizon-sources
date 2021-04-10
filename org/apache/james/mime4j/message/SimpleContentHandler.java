package org.apache.james.mime4j.message;

import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.codec.*;
import java.io.*;
import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.field.*;
import org.apache.james.mime4j.*;

public abstract class SimpleContentHandler extends AbstractContentHandler
{
    private Header currHeader;
    
    @Override
    public final void body(final BodyDescriptor bodyDescriptor, final InputStream inputStream) throws IOException {
        InputStream inputStream2;
        if (MimeUtil.isBase64Encoding(bodyDescriptor.getTransferEncoding())) {
            inputStream2 = new Base64InputStream(inputStream);
        }
        else {
            if (!MimeUtil.isQuotedPrintableEncoded(bodyDescriptor.getTransferEncoding())) {
                this.bodyDecoded(bodyDescriptor, inputStream);
                return;
            }
            inputStream2 = new QuotedPrintableInputStream(inputStream);
        }
        this.bodyDecoded(bodyDescriptor, inputStream2);
    }
    
    public abstract void bodyDecoded(final BodyDescriptor p0, final InputStream p1) throws IOException;
    
    @Override
    public final void endHeader() {
        final Header currHeader = this.currHeader;
        this.currHeader = null;
        this.headers(currHeader);
    }
    
    @Override
    public final void field(final Field field) throws MimeException {
        this.currHeader.addField(AbstractField.parse(field.getRaw()));
    }
    
    public abstract void headers(final Header p0);
    
    @Override
    public final void startHeader() {
        this.currHeader = new Header();
    }
}
