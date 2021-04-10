package com.zhekasmirnov.innercore.modpack.strategy.request;

import java.io.*;
import java.util.*;

public class NoAccessDirectoryRequestStrategy extends DirectoryRequestStrategy
{
    @Override
    public File get(final String s) {
        return null;
    }
    
    @Override
    public File get(final String s, final String s2) {
        return null;
    }
    
    @Override
    public List<File> getAll(final String s) {
        return new ArrayList<File>();
    }
    
    @Override
    public List<String> getAllLocations() {
        return new ArrayList<String>();
    }
}
