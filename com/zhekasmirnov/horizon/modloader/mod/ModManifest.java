package com.zhekasmirnov.horizon.modloader.mod;

import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.horizon.modloader.repo.location.*;

public class ModManifest
{
    private final File file;
    private List<Directory> directories;
    private List<Module> modules;
    private Module mainModule;
    
    public ModManifest(final File file) throws IOException, JSONException {
        this.directories = new ArrayList<Directory>();
        this.modules = new ArrayList<Module>();
        this.mainModule = null;
        this.file = file;
        final JSONObject manifest = FileUtils.readJSON(file);
        final JSONArray dirs = manifest.optJSONArray("directories");
        if (dirs != null) {
            for (final Object dir : new JsonIterator<Object>(dirs)) {
                if (dir instanceof JSONObject) {
                    this.directories.add(new Directory((JSONObject)dir));
                }
            }
        }
        final JSONObject mods = manifest.optJSONObject("modules");
        if (mods != null) {
            for (final String nameId : new JsonIterator<String>(mods)) {
                final JSONObject module = mods.optJSONObject(nameId);
                if (module != null) {
                    this.modules.add(new Module(nameId, module));
                }
            }
        }
        final JSONObject info = manifest.optJSONObject("info");
        if (info != null) {
            this.mainModule = new Module("<mod-info>", info);
        }
        else if (this.modules.size() > 0) {
            this.mainModule = this.modules.get(0);
        }
        else {
            final JSONObject defaultInf = new JSONObject();
            defaultInf.put("name", (Object)file.getParentFile().getName());
            this.mainModule = new Module("<default-mod-info>", defaultInf);
        }
    }
    
    public File getParentDirectory() {
        return this.file.getParentFile();
    }
    
    public List<Directory> getDirectories() {
        return this.directories;
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    public Module getMainModule() {
        return this.mainModule;
    }
    
    public String getName() {
        return this.mainModule.name;
    }
    
    public enum DirectoryType
    {
        LIBRARY, 
        RESOURCE, 
        SUBMOD, 
        JAVA;
        
        public static DirectoryType byName(final String name) {
            final String lowerCase = name.toLowerCase();
            switch (lowerCase) {
                case "library":
                case "executable":
                case "native": {
                    return DirectoryType.LIBRARY;
                }
                case "resource": {
                    return DirectoryType.RESOURCE;
                }
                case "submod":
                case "merge": {
                    return DirectoryType.SUBMOD;
                }
                case "java": {
                    return DirectoryType.JAVA;
                }
                default: {
                    return null;
                }
            }
        }
    }
    
    public class Directory
    {
        public final File file;
        public final DirectoryType type;
        
        public Directory(final File file, final DirectoryType type) {
            this.file = file;
            this.type = type;
        }
        
        public Directory(final JSONObject json) {
            final String pathS = json.optString("path");
            if (pathS == null) {
                throw new RuntimeException("mod manifest directory parameter is missing: path");
            }
            final String typeS = json.optString("type");
            if (typeS == null) {
                throw new RuntimeException("mod manifest directory parameter is missing: type");
            }
            final File path = new File(ModManifest.this.file.getParent(), pathS);
            if (!path.exists()) {
                throw new RuntimeException("mod manifest directory parameter is invalid: path " + pathS + " (" + path + ") does not exist");
            }
            if (!path.isDirectory()) {
                throw new RuntimeException("mod manifest directory parameter is invalid: path " + pathS + " (" + path + ") is not a directory");
            }
            final DirectoryType type = DirectoryType.byName(typeS);
            if (type == null) {
                throw new RuntimeException("mod manifest directory parameter is invalid: type " + typeS + " does not exist");
            }
            this.file = path;
            this.type = type;
        }
        
        public ModLocation asModLocation() {
            return new LocalModLocation(this.file);
        }
    }
    
    public class Module
    {
        public final String nameId;
        public final String name;
        public final String author;
        public final String description;
        public final String versionName;
        public final int versionCode;
        
        public Module(final String nameId, final JSONObject json) {
            this.nameId = nameId;
            this.name = json.optString("name");
            this.author = json.optString("author");
            this.description = json.optString("description");
            final JSONObject version = json.optJSONObject("version");
            if (version != null) {
                this.versionName = version.optString("name");
                this.versionCode = version.optInt("code");
            }
            else {
                this.versionName = "unknown";
                this.versionCode = 0;
            }
        }
        
        public String getDisplayedDescription() {
            return (this.description != null && this.description.length() > 0) ? this.description : "No description provided";
        }
    }
}
