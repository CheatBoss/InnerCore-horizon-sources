package org.apache.james.mime4j.io;

import org.apache.james.mime4j.util.*;
import java.io.*;

public abstract class LineReaderInputStream extends FilterInputStream
{
    protected LineReaderInputStream(final InputStream inputStream) {
        super(inputStream);
    }
    
    public abstract int readLine(final ByteArrayBuffer p0) throws IOException;
}
