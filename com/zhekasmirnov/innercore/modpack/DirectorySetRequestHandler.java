package com.zhekasmirnov.innercore.modpack;

import com.zhekasmirnov.innercore.modpack.strategy.request.*;
import java.util.*;
import java.io.*;

public class DirectorySetRequestHandler
{
    private final List<ModPackDirectory> directories;
    private final List<DirectoryRequestStrategy> strategies;
    
    public DirectorySetRequestHandler() {
        this.directories = new ArrayList<ModPackDirectory>();
        this.strategies = new ArrayList<DirectoryRequestStrategy>();
    }
    
    public DirectorySetRequestHandler(final Collection<ModPackDirectory> collection) {
        this.directories = new ArrayList<ModPackDirectory>();
        this.strategies = new ArrayList<DirectoryRequestStrategy>();
        final Iterator<ModPackDirectory> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }
    
    public DirectorySetRequestHandler(final ModPackDirectory... array) {
        this.directories = new ArrayList<ModPackDirectory>();
        this.strategies = new ArrayList<DirectoryRequestStrategy>();
        for (int length = array.length, i = 0; i < length; ++i) {
            this.add(array[i]);
        }
    }
    
    public void add(final ModPackDirectory modPackDirectory) {
        this.directories.add(modPackDirectory);
        this.strategies.add(modPackDirectory.getRequestStrategy());
    }
    
    public File get(final String s) {
        File file = null;
        final Iterator<DirectoryRequestStrategy> iterator = this.strategies.iterator();
        while (iterator.hasNext()) {
            final File value = iterator.next().get(s);
            File file2 = file;
            if (value != null) {
                if (value.exists()) {
                    return value;
                }
                if ((file2 = file) == null) {
                    file2 = value;
                }
            }
            file = file2;
        }
        return file;
    }
    
    public File get(final String s, final String s2) {
        File file = null;
        final Iterator<DirectoryRequestStrategy> iterator = this.strategies.iterator();
        while (iterator.hasNext()) {
            final File value = iterator.next().get(s, s2);
            File file2 = file;
            if (value != null) {
                if (value.exists()) {
                    return value;
                }
                if ((file2 = file) == null) {
                    file2 = value;
                }
            }
            file = file2;
        }
        return file;
    }
    
    public List<File> getAllAtLocation(final String s) {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<File>();
        final Iterator<DirectoryRequestStrategy> iterator = this.strategies.iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getAll(s));
        }
        return (List<File>)list;
    }
    
    public List<String> getAllLocations() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<String>();
        final Iterator<DirectoryRequestStrategy> iterator = this.strategies.iterator();
        while (iterator.hasNext()) {
            list.addAll(iterator.next().getAllLocations());
        }
        return (List<String>)list;
    }
    
    public List<ModPackDirectory> getDirectories() {
        return this.directories;
    }
}
