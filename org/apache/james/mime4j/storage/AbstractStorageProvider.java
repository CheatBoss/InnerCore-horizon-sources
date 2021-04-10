package org.apache.james.mime4j.storage;

import org.apache.james.mime4j.codec.*;
import java.io.*;

public abstract class AbstractStorageProvider implements StorageProvider
{
    protected AbstractStorageProvider() {
    }
    
    @Override
    public final Storage store(final InputStream inputStream) throws IOException {
        final StorageOutputStream storageOutputStream = this.createStorageOutputStream();
        CodecUtil.copy(inputStream, storageOutputStream);
        return storageOutputStream.toStorage();
    }
}
