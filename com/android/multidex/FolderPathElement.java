package com.android.multidex;

import java.util.*;
import java.io.*;

class FolderPathElement implements ClassPathElement
{
    private File baseFolder;
    
    public FolderPathElement(final File baseFolder) {
        this.baseFolder = baseFolder;
    }
    
    private void collect(final File file, final String s, final ArrayList<String> list) {
        final File[] listFiles = file.listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file2 = listFiles[i];
            if (file2.isDirectory()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append('/');
                sb.append(file2.getName());
                this.collect(file2, sb.toString(), list);
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append('/');
                sb2.append(file2.getName());
                list.add(sb2.toString());
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public Iterable<String> list() {
        final ArrayList<String> list = new ArrayList<String>();
        this.collect(this.baseFolder, "", list);
        return list;
    }
    
    @Override
    public InputStream open(final String s) throws FileNotFoundException {
        return new FileInputStream(new File(this.baseFolder, s.replace('/', File.separatorChar)));
    }
}
