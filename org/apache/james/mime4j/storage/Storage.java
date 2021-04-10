package org.apache.james.mime4j.storage;

import java.io.*;

public interface Storage
{
    void delete();
    
    InputStream getInputStream() throws IOException;
}
