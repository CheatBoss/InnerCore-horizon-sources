package org.reflections.vfs;

import java.io.*;

public class SystemFile implements File
{
    private final java.io.File file;
    private final SystemDir root;
    
    public SystemFile(final SystemDir root, final java.io.File file) {
        this.root = root;
        this.file = file;
    }
    
    @Override
    public String getName() {
        return this.file.getName();
    }
    
    @Override
    public String getRelativePath() {
        final String replace = this.file.getPath().replace("\\", "/");
        if (replace.startsWith(this.root.getPath())) {
            return replace.substring(this.root.getPath().length() + 1);
        }
        return null;
    }
    
    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(this.file);
        }
        catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public String toString() {
        return this.file.toString();
    }
}
