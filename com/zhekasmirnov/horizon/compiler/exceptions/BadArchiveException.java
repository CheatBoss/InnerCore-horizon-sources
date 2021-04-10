package com.zhekasmirnov.horizon.compiler.exceptions;

import java.io.*;

public class BadArchiveException extends IOException
{
    private final String fileName;
    
    public BadArchiveException(final String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return this.fileName;
    }
}
