package com.zhekasmirnov.horizon.modloader.library;

import java.io.*;
import java.util.*;

public class LibraryMakeFile
{
    private File file;
    private String cppFlags;
    private List<String> files;
    
    public LibraryMakeFile(final File file) {
        this.cppFlags = null;
        this.files = null;
        this.file = file;
    }
    
    public String getCppFlags() {
        return this.cppFlags;
    }
    
    public List<String> getFiles() {
        return this.files;
    }
}
