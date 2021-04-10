package com.zhekasmirnov.innercore.mod.resource.pack;

import java.util.*;
import com.zhekasmirnov.innercore.mod.resource.types.*;
import java.io.*;

public class ResourcePack implements IResourcePack
{
    private String dir;
    public boolean isLoaded;
    public ArrayList<ResourceFile> resourceFiles;
    
    public ResourcePack(final String dir) {
        this.resourceFiles = new ArrayList<ResourceFile>();
        this.isLoaded = false;
        this.dir = dir;
    }
    
    private void findFilesInDir(final File file, final ArrayList<ResourceFile> list) throws IOException {
        final File[] listFiles = file.listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file2 = listFiles[i];
            if (file2.isDirectory()) {
                this.findFilesInDir(file2, list);
            }
            else {
                list.add(new ResourceFile(this, file2));
            }
        }
    }
    
    @Override
    public String getAbsolutePath() {
        return this.dir;
    }
    
    @Override
    public String getPackName() {
        return this.dir.substring(this.dir.lastIndexOf("/") + 1);
    }
    
    public void readAllFiles() {
        this.resourceFiles.clear();
        try {
            this.findFilesInDir(new File(this.dir), this.resourceFiles);
            this.isLoaded = true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
