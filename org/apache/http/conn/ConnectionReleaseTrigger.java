package org.apache.http.conn;

import java.io.*;

@Deprecated
public interface ConnectionReleaseTrigger
{
    void abortConnection() throws IOException;
    
    void releaseConnection() throws IOException;
}
