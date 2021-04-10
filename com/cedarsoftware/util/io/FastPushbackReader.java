package com.cedarsoftware.util.io;

import java.io.*;

public interface FastPushbackReader extends Closeable
{
    int getCol();
    
    String getLastSnippet();
    
    int getLine();
    
    int read() throws IOException;
    
    void unread(final int p0) throws IOException;
}
