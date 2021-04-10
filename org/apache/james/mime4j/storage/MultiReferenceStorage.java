package org.apache.james.mime4j.storage;

import java.io.*;

public class MultiReferenceStorage implements Storage
{
    private int referenceCounter;
    private final Storage storage;
    
    public MultiReferenceStorage(final Storage storage) {
        if (storage != null) {
            this.storage = storage;
            this.referenceCounter = 1;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private boolean decrementCounter() {
        synchronized (this) {
            if (this.referenceCounter != 0) {
                final int referenceCounter = this.referenceCounter;
                boolean b = true;
                if ((this.referenceCounter = referenceCounter - 1) != 0) {
                    b = false;
                }
                return b;
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    private void incrementCounter() {
        synchronized (this) {
            if (this.referenceCounter != 0) {
                ++this.referenceCounter;
                return;
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    public void addReference() {
        this.incrementCounter();
    }
    
    @Override
    public void delete() {
        if (this.decrementCounter()) {
            this.storage.delete();
        }
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return this.storage.getInputStream();
    }
}
