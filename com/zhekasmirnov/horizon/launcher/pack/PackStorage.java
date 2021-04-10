package com.zhekasmirnov.horizon.launcher.pack;

import com.zhekasmirnov.horizon.launcher.*;
import java.io.*;
import java.util.*;

public class PackStorage
{
    public final ContextHolder contextHolder;
    public final File directory;
    private List<PackDirectory> packDirectories;
    public List<PackHolder> packHolders;
    
    public PackStorage(final ContextHolder contextHolder, final File directory) {
        this.packDirectories = new ArrayList<PackDirectory>();
        this.packHolders = new ArrayList<PackHolder>();
        this.contextHolder = contextHolder;
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!directory.isDirectory()) {
            throw new RuntimeException("Not directory passed to pack storage constructor: " + directory);
        }
        this.directory = directory;
        this.updateLocalDirectories();
    }
    
    private boolean addPackDirectory(final PackDirectory directory) {
        for (final PackDirectory dir : this.packDirectories) {
            if (dir.directory.equals(directory.directory)) {
                return false;
            }
        }
        this.packDirectories.add(directory);
        return true;
    }
    
    public void updateLocalDirectories() {
        for (final File file : this.directory.listFiles()) {
            if (file.isDirectory()) {
                this.addPackDirectory(new PackDirectory(file));
            }
        }
    }
    
    public PackDirectory makeNewPackDirectory(String name) {
        if (name == null || name.length() == 0) {
            name = "unnamed-pack";
        }
        name = name.replaceAll("[^a-zA-Z0-9_]", "_");
        int index = 0;
        PackDirectory packDirectory;
        while (true) {
            packDirectory = new PackDirectory(new File(this.directory, (index > 0) ? (name + "_" + index) : name));
            if (this.addPackDirectory(packDirectory)) {
                break;
            }
            ++index;
        }
        return packDirectory;
    }
    
    public PackDirectory addPackLocation(final IPackLocation location) {
        final PackManifest manifest = location.getManifest();
        if (manifest == null) {
            return null;
        }
        final PackDirectory directory = this.makeNewPackDirectory(manifest.pack);
        directory.setLocation(location);
        return directory;
    }
    
    public PackHolder loadNewPackHolderFromLocation(final IPackLocation location) {
        final PackDirectory directory = this.addPackLocation(location);
        if (directory != null) {
            directory.nominateForInstallation();
            return this.loadPackHolderForDirectory(directory);
        }
        return null;
    }
    
    public PackHolder loadPackHolderFromDirectory(final PackDirectory directory) {
        if (!this.packDirectories.contains(directory)) {
            throw new IllegalArgumentException("you must pass only directories from this pack storage");
        }
        return this.loadPackHolderForDirectory(directory);
    }
    
    public ContextHolder getContextHolder() {
        return this.contextHolder;
    }
    
    public List<PackDirectory> getPackDirectories() {
        return this.packDirectories;
    }
    
    public synchronized void unloadAll() {
        for (final PackHolder holder : this.packHolders) {
            holder.deselectAndUnload();
        }
        this.packHolders.clear();
    }
    
    private PackHolder loadPackHolderForDirectory(final PackDirectory directory) {
        final PackHolder holder = new PackHolder(this, directory);
        if (holder.getManifest() != null) {
            holder.initialize();
            this.packHolders.add(holder);
            return holder;
        }
        return null;
    }
    
    public synchronized List<PackHolder> reloadAll() {
        this.unloadAll();
        for (final PackDirectory directory : this.packDirectories) {
            this.loadPackHolderForDirectory(directory);
        }
        return this.packHolders;
    }
    
    public synchronized void loadAll() {
        for (final PackDirectory directory : this.packDirectories) {
            boolean loadRequired = true;
            for (final PackHolder holder : this.packHolders) {
                if (holder.packDirectory == directory) {
                    loadRequired = false;
                    break;
                }
            }
            if (loadRequired) {
                this.loadPackHolderForDirectory(directory);
            }
        }
    }
    
    public void fetchLocationsFromRepo(final PackRepository repo) {
        for (final PackDirectory directory : this.packDirectories) {
            directory.fetchFromRepo(repo);
        }
    }
}
