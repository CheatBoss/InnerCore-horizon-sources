package org.apache.james.mime4j.message;

import java.io.*;

public abstract class BinaryBody extends SingleBody
{
    protected BinaryBody() {
    }
    
    public abstract InputStream getInputStream() throws IOException;
}
