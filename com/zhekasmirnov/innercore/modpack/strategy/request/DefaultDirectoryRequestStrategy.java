package com.zhekasmirnov.innercore.modpack.strategy.request;

import java.io.*;
import java.util.*;

public class DefaultDirectoryRequestStrategy extends DirectoryRequestStrategy
{
    @Override
    public File get(final String s) {
        return new File(this.getAssignedDirectory().getLocation(), s);
    }
    
    @Override
    public File get(final String s, final String s2) {
        return new File(this.get(s), s2);
    }
    
    @Override
    public List<File> getAll(final String s) {
        final ArrayList<File> list = new ArrayList<File>();
        final File[] listFiles = new File(this.getAssignedDirectory().getLocation(), s).listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.isFile()) {
                    list.add(file);
                }
            }
        }
        return list;
    }
    
    @Override
    public List<String> getAllLocations() {
        final ArrayList<String> list = new ArrayList<String>();
        final File[] listFiles = this.getAssignedDirectory().getLocation().listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.isDirectory()) {
                    list.add(file.getName());
                }
            }
        }
        return list;
    }
}
