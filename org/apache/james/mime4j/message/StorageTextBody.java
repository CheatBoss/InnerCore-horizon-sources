package org.apache.james.mime4j.message;

import java.nio.charset.*;
import org.apache.james.mime4j.storage.*;
import org.apache.james.mime4j.util.*;
import org.apache.james.mime4j.codec.*;
import java.io.*;

class StorageTextBody extends TextBody
{
    private Charset charset;
    private MultiReferenceStorage storage;
    
    public StorageTextBody(final MultiReferenceStorage storage, final Charset charset) {
        this.storage = storage;
        this.charset = charset;
    }
    
    @Override
    public StorageTextBody copy() {
        this.storage.addReference();
        return new StorageTextBody(this.storage, this.charset);
    }
    
    @Override
    public void dispose() {
        final MultiReferenceStorage storage = this.storage;
        if (storage != null) {
            storage.delete();
            this.storage = null;
        }
    }
    
    @Override
    public String getMimeCharset() {
        return CharsetUtil.toMimeCharset(this.charset.name());
    }
    
    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(this.storage.getInputStream(), this.charset);
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            final InputStream inputStream = this.storage.getInputStream();
            CodecUtil.copy(inputStream, outputStream);
            inputStream.close();
            return;
        }
        throw new IllegalArgumentException();
    }
}
