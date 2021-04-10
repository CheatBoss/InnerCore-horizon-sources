package com.zhekasmirnov.innercore.modpack.strategy.extract;

import com.zhekasmirnov.innercore.modpack.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.util.*;

public abstract class DirectoryExtractStrategy
{
    private ModPackDirectory directory;
    
    public void assignToDirectory(final ModPackDirectory directory) {
        if (this.directory != null) {
            throw new IllegalStateException();
        }
        this.directory = directory;
    }
    
    public ModPackDirectory getAssignedDirectory() {
        return this.directory;
    }
    
    public abstract String getEntryName(final String p0, final File p1);
    
    public abstract List<File> getFilesToExtract();
    
    public String getFullEntryName(final File file) {
        final String absolutePath = file.getAbsolutePath();
        final String absolutePath2 = this.getAssignedDirectory().getLocation().getAbsolutePath();
        if (!absolutePath.startsWith(absolutePath2)) {
            throw new IllegalArgumentException("getEntryNameForFile got file, not contained in assigned directory");
        }
        final String cleanupPath = FileUtils.cleanupPath(absolutePath.substring(absolutePath2.length()));
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getAssignedDirectory().getPathPattern());
        sb.append("/");
        sb.append(FileUtils.cleanupPath(this.getEntryName(cleanupPath, file)));
        return sb.toString().replaceAll("//", "/");
    }
}
