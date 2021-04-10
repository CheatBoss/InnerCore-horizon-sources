package org.mineprogramming.horizon.innercore.model;

import java.io.*;
import android.net.*;

public class Mod
{
    private String author;
    private final String description;
    private File directory;
    private String id;
    private final Uri image;
    private boolean isLocal;
    private boolean networkAdapted;
    private boolean optimized;
    private final String title;
    
    public Mod(final String title, final String description, final Uri image, final boolean isLocal) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.isLocal = isLocal;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Uri getImage() {
        return this.image;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public boolean isLocal() {
        return this.isLocal;
    }
    
    public boolean isNetworkAdapted() {
        return this.networkAdapted;
    }
    
    public boolean isOptimized() {
        return this.optimized;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    public void setDirectory(final File directory) {
        this.directory = directory;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setNetworkAdapted(final boolean networkAdapted) {
        this.networkAdapted = networkAdapted;
    }
    
    public void setOptimized(final boolean optimized) {
        this.optimized = optimized;
    }
}
