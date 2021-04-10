package org.apache.james.mime4j.storage;

import java.io.*;
import org.apache.james.mime4j.util.*;

public class MemoryStorageProvider extends AbstractStorageProvider
{
    @Override
    public StorageOutputStream createStorageOutputStream() {
        return new MemoryStorageOutputStream();
    }
    
    static final class MemoryStorage implements Storage
    {
        private final int count;
        private byte[] data;
        
        public MemoryStorage(final byte[] data, final int count) {
            this.data = data;
            this.count = count;
        }
        
        @Override
        public void delete() {
            this.data = null;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            if (this.data != null) {
                return new ByteArrayInputStream(this.data, 0, this.count);
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    private static final class MemoryStorageOutputStream extends StorageOutputStream
    {
        ByteArrayBuffer bab;
        
        private MemoryStorageOutputStream() {
            this.bab = new ByteArrayBuffer(1024);
        }
        
        @Override
        protected Storage toStorage0() throws IOException {
            return new MemoryStorage(this.bab.buffer(), this.bab.length());
        }
        
        @Override
        protected void write0(final byte[] array, final int n, final int n2) throws IOException {
            this.bab.append(array, n, n2);
        }
    }
}
