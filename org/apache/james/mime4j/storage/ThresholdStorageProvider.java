package org.apache.james.mime4j.storage;

import java.io.*;
import org.apache.james.mime4j.util.*;

public class ThresholdStorageProvider extends AbstractStorageProvider
{
    private final StorageProvider backend;
    private final int thresholdSize;
    
    public ThresholdStorageProvider(final StorageProvider storageProvider) {
        this(storageProvider, 2048);
    }
    
    public ThresholdStorageProvider(final StorageProvider backend, final int thresholdSize) {
        if (backend == null) {
            throw new IllegalArgumentException();
        }
        if (thresholdSize >= 1) {
            this.backend = backend;
            this.thresholdSize = thresholdSize;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public StorageOutputStream createStorageOutputStream() {
        return new ThresholdStorageOutputStream();
    }
    
    private static final class ThresholdStorage implements Storage
    {
        private byte[] head;
        private final int headLen;
        private Storage tail;
        
        public ThresholdStorage(final byte[] head, final int headLen, final Storage tail) {
            this.head = head;
            this.headLen = headLen;
            this.tail = tail;
        }
        
        @Override
        public void delete() {
            if (this.head != null) {
                this.head = null;
                this.tail.delete();
                this.tail = null;
            }
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            if (this.head != null) {
                return new SequenceInputStream(new ByteArrayInputStream(this.head, 0, this.headLen), this.tail.getInputStream());
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    private final class ThresholdStorageOutputStream extends StorageOutputStream
    {
        private final ByteArrayBuffer head;
        private StorageOutputStream tail;
        
        public ThresholdStorageOutputStream() {
            this.head = new ByteArrayBuffer(Math.min(ThresholdStorageProvider.this.thresholdSize, 1024));
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            final StorageOutputStream tail = this.tail;
            if (tail != null) {
                tail.close();
            }
        }
        
        @Override
        protected Storage toStorage0() throws IOException {
            if (this.tail == null) {
                return new MemoryStorageProvider.MemoryStorage(this.head.buffer(), this.head.length());
            }
            return new ThresholdStorage(this.head.buffer(), this.head.length(), this.tail.toStorage());
        }
        
        @Override
        protected void write0(final byte[] array, final int n, final int n2) throws IOException {
            final int n3 = ThresholdStorageProvider.this.thresholdSize - this.head.length();
            int n4 = n;
            int n5 = n2;
            if (n3 > 0) {
                final int min = Math.min(n3, n2);
                this.head.append(array, n, min);
                n4 = n + min;
                n5 = n2 - min;
            }
            if (n5 > 0) {
                if (this.tail == null) {
                    this.tail = ThresholdStorageProvider.this.backend.createStorageOutputStream();
                }
                this.tail.write(array, n4, n5);
            }
        }
    }
}
