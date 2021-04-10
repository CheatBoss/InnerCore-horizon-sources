package org.apache.james.mime4j.storage;

import java.io.*;

public interface StorageProvider
{
    StorageOutputStream createStorageOutputStream() throws IOException;
    
    Storage store(final InputStream p0) throws IOException;
}
