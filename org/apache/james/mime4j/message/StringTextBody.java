package org.apache.james.mime4j.message;

import java.nio.charset.*;
import org.apache.james.mime4j.util.*;
import java.io.*;

class StringTextBody extends TextBody
{
    private final Charset charset;
    private final String text;
    
    public StringTextBody(final String text, final Charset charset) {
        this.text = text;
        this.charset = charset;
    }
    
    @Override
    public StringTextBody copy() {
        return new StringTextBody(this.text, this.charset);
    }
    
    @Override
    public String getMimeCharset() {
        return CharsetUtil.toMimeCharset(this.charset.name());
    }
    
    @Override
    public Reader getReader() throws IOException {
        return new StringReader(this.text);
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            final StringReader stringReader = new StringReader(this.text);
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, this.charset);
            final char[] array = new char[1024];
            while (true) {
                final int read = stringReader.read(array);
                if (read == -1) {
                    break;
                }
                outputStreamWriter.write(array, 0, read);
            }
            stringReader.close();
            outputStreamWriter.flush();
            return;
        }
        throw new IllegalArgumentException();
    }
}
