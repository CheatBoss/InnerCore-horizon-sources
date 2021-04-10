package com.zhekasmirnov.horizon.modloader.repo.storage;

import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.modloader.repo.location.*;

public class DirectoryRepository extends ModRepository
{
    private final File directory;
    private final List<ModLocation> locations;
    
    public DirectoryRepository(final File directory) {
        this.locations = new ArrayList<ModLocation>();
        this.directory = directory;
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    @Override
    public void refresh(final EventLogger logger) {
        this.locations.clear();
        final File[] files = this.directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory() && new File(file, "manifest").exists()) {
                    this.locations.add(new LocalModLocation(file));
                }
            }
        }
    }
    
    @Override
    public List<ModLocation> getAllLocations() {
        return this.locations;
    }
}
