package com.android.multidex;

import java.io.*;

interface ClassPathElement
{
    public static final char SEPARATOR_CHAR = '/';
    
    void close() throws IOException;
    
    Iterable<String> list();
    
    InputStream open(final String p0) throws IOException;
}
