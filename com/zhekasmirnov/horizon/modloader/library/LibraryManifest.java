package com.zhekasmirnov.horizon.modloader.library;

import java.util.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class LibraryManifest
{
    private File file;
    private JSONObject content;
    private String name;
    private boolean isSharedLibrary;
    private int version;
    private List<String> include;
    private List<String> dependencies;
    
    public LibraryManifest(final File file) throws IOException, JSONException {
        this.name = null;
        this.isSharedLibrary = false;
        this.version = 0;
        this.include = new ArrayList<String>();
        this.dependencies = new ArrayList<String>();
        this.file = file;
        this.content = FileUtils.readJSON(file);
        final JSONObject shared = this.content.optJSONObject("shared");
        if (shared != null) {
            this.name = shared.optString("name");
            if ("unnamed".equals(this.name)) {
                throw new IllegalArgumentException("name 'unnamed' of shared library is illegal");
            }
            if (this.name == null) {
                throw new IllegalArgumentException("name of shared library cannot be null");
            }
            final JSONArray include = shared.optJSONArray("include");
            if (include != null) {
                this.include = JSONUtils.toList(include);
            }
        }
        final JSONObject library = this.content.optJSONObject("library");
        if (library != null) {
            this.version = library.optInt("version", -1);
            if (this.version < 0) {
                throw new IllegalArgumentException("no library version is defined or its incorrect");
            }
            if (this.name == null) {
                throw new IllegalArgumentException("library must have a shared name");
            }
            this.isSharedLibrary = true;
        }
        else {
            this.isSharedLibrary = false;
        }
        final JSONArray dependencies = this.content.optJSONArray("depends");
        if (dependencies != null) {
            this.dependencies = JSONUtils.toList(dependencies);
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSoName() {
        if (this.name != null) {
            return "lib" + this.name + ".so";
        }
        return "unnamed";
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public List<String> getDependencies() {
        return this.dependencies;
    }
    
    public List<String> getInclude() {
        return this.include;
    }
    
    public boolean isSharedLibrary() {
        return this.isSharedLibrary;
    }
    
    public boolean isShared() {
        return this.name != null;
    }
}
