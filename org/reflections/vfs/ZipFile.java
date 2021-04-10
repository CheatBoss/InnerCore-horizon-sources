package org.reflections.vfs;

import java.util.zip.*;
import java.io.*;

public class ZipFile implements File
{
    private final ZipEntry entry;
    private final ZipDir root;
    
    public ZipFile(final ZipDir root, final ZipEntry entry) {
        this.root = root;
        this.entry = entry;
    }
    
    @Override
    public String getName() {
        final String name = this.entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }
    
    @Override
    public String getRelativePath() {
        return this.entry.getName();
    }
    
    @Override
    public InputStream openInputStream() throws IOException {
        return this.root.jarFile.getInputStream(this.entry);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.root.getPath());
        sb.append("!");
        sb.append(java.io.File.separatorChar);
        sb.append(this.entry.toString());
        return sb.toString();
    }
}
