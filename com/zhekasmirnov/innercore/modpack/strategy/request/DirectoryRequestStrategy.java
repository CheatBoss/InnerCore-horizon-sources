package com.zhekasmirnov.innercore.modpack.strategy.request;

import com.zhekasmirnov.innercore.modpack.*;
import java.io.*;
import java.util.*;

public abstract class DirectoryRequestStrategy
{
    private ModPackDirectory directory;
    
    public void assignToDirectory(final ModPackDirectory directory) {
        if (this.directory != null) {
            throw new IllegalStateException();
        }
        this.directory = directory;
    }
    
    public File assure(final String s, final String s2) {
        final File value = this.get(s, s2);
        value.getParentFile().mkdirs();
        return value;
    }
    
    public abstract File get(final String p0);
    
    public abstract File get(final String p0, final String p1);
    
    public abstract List<File> getAll(final String p0);
    
    public List<File> getAllFiles() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<File>();
        final Iterator<String> iterator = this.getAllLocations().iterator();
        while (iterator.hasNext()) {
            list.addAll(this.getAll(iterator.next()));
        }
        return (List<File>)list;
    }
    
    public abstract List<String> getAllLocations();
    
    public ModPackDirectory getAssignedDirectory() {
        return this.directory;
    }
    
    public boolean remove(final String s, final String s2) {
        return this.get(s, s2).delete();
    }
}
