package org.apache.james.mime4j.message;

import org.apache.james.mime4j.storage.*;
import java.io.*;
import org.apache.james.mime4j.codec.*;

class StorageBinaryBody extends BinaryBody
{
    private MultiReferenceStorage storage;
    
    public StorageBinaryBody(final MultiReferenceStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public StorageBinaryBody copy() {
        this.storage.addReference();
        return new StorageBinaryBody(this.storage);
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
    public InputStream getInputStream() throws IOException {
        return this.storage.getInputStream();
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
