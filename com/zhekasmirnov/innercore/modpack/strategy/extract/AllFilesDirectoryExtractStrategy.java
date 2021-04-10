package com.zhekasmirnov.innercore.modpack.strategy.extract;

import java.io.*;
import java.util.function.*;
import java.util.*;

public class AllFilesDirectoryExtractStrategy extends DirectoryExtractStrategy
{
    protected void addAllRecursive(final File file, final List<File> list, final Predicate<File> predicate) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    this.addAllRecursive(listFiles[i], list, predicate);
                }
            }
            return;
        }
        if (file.isFile() && (predicate == null || predicate.test(file))) {
            list.add(file);
        }
    }
    
    @Override
    public String getEntryName(final String s, final File file) {
        return s;
    }
    
    @Override
    public List<File> getFilesToExtract() {
        final ArrayList<File> list = new ArrayList<File>();
        this.addAllRecursive(this.getAssignedDirectory().getLocation(), list, null);
        return list;
    }
}
