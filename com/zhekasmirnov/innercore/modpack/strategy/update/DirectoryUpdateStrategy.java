package com.zhekasmirnov.innercore.modpack.strategy.update;

import com.zhekasmirnov.innercore.modpack.*;
import java.io.*;

public abstract class DirectoryUpdateStrategy
{
    private ModPackDirectory directory;
    
    public void assignToDirectory(final ModPackDirectory directory) {
        if (this.directory != null) {
            throw new IllegalStateException();
        }
        this.directory = directory;
    }
    
    public abstract void beginUpdate() throws IOException;
    
    public abstract void finishUpdate() throws IOException;
    
    public ModPackDirectory getAssignedDirectory() {
        return this.directory;
    }
    
    public abstract void updateFile(final String p0, final InputStream p1) throws IOException;
}
