package org.reflections.vfs;

import java.util.zip.*;
import java.io.*;

public class JarInputFile implements File
{
    private final long endIndex;
    private final ZipEntry entry;
    private final long fromIndex;
    private final JarInputDir jarInputDir;
    
    public JarInputFile(final ZipEntry entry, final JarInputDir jarInputDir, final long fromIndex, final long endIndex) {
        this.entry = entry;
        this.jarInputDir = jarInputDir;
        this.fromIndex = fromIndex;
        this.endIndex = endIndex;
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
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (JarInputFile.this.jarInputDir.cursor >= JarInputFile.this.fromIndex && JarInputFile.this.jarInputDir.cursor <= JarInputFile.this.endIndex) {
                    final int read = JarInputFile.this.jarInputDir.jarInputStream.read();
                    final JarInputDir access$000 = JarInputFile.this.jarInputDir;
                    ++access$000.cursor;
                    return read;
                }
                return -1;
            }
        };
    }
}
