package com.android.internal.http.multipart;

import java.io.*;

public interface PartSource
{
    InputStream createInputStream() throws IOException;
    
    String getFileName();
    
    long getLength();
}
