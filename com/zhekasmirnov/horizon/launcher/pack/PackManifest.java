package com.zhekasmirnov.horizon.launcher.pack;

import android.os.*;
import com.zhekasmirnov.horizon.*;
import java.util.*;
import org.json.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;

public class PackManifest
{
    private JSONObject content;
    public final String game;
    public final String gameVersion;
    public final String pack;
    public final String packVersion;
    public final String packUUID;
    public final int packVersionCode;
    public final String developer;
    public final String description;
    public final boolean optClearActivityStack;
    public final List<ClassInfo> activities;
    public final List<ClassInfo> environmentClasses;
    public final List<String> keepDirectories;
    public final HashMap<String, String> savesHoldersInfo;
    
    public PackManifest(final JSONObject json) throws JSONException {
        this.activities = new ArrayList<ClassInfo>();
        this.environmentClasses = new ArrayList<ClassInfo>();
        this.keepDirectories = new ArrayList<String>();
        this.savesHoldersInfo = new HashMap<String, String>();
        this.content = json;
        this.game = this.content.getString("game");
        this.gameVersion = this.content.optString("gameVersion", (String)null);
        this.pack = this.content.getString("pack");
        this.packVersion = this.content.optString("packVersion", (String)null);
        this.packUUID = this.content.optString("uuid", (String)null);
        this.packVersionCode = this.content.getInt("packVersionCode");
        this.developer = LocaleUtils.resolveLocaleJsonProperty(this.content, "developer");
        this.description = LocaleUtils.resolveLocaleJsonProperty(this.content, "description");
        this.optClearActivityStack = this.content.optBoolean("clearActivityStack", true);
        final Object activityJson = this.content.opt("activity");
        if (activityJson != null) {
            this.activities.add(ClassInfo.fromObject(activityJson));
        }
        final JSONArray activitiesJson = this.content.optJSONArray("activities");
        if (activitiesJson != null) {
            for (final Object object : new JsonIterator<Object>(activitiesJson)) {
                this.activities.add(ClassInfo.fromObject(object));
            }
        }
        final Object envClassJson = this.content.opt("environmentClass");
        if (envClassJson != null) {
            this.environmentClasses.add(ClassInfo.fromObject(envClassJson));
        }
        final JSONArray envClassesJson = this.content.optJSONArray("environmentClasses");
        if (envClassesJson != null) {
            for (final Object object2 : new JsonIterator<Object>(envClassesJson)) {
                this.environmentClasses.add(ClassInfo.fromObject(object2));
            }
        }
        final JSONArray keepDirectoriesJson = this.content.optJSONArray("keepDirectories");
        if (keepDirectoriesJson != null) {
            for (final String dir : new JsonIterator<String>(keepDirectoriesJson)) {
                if (dir != null) {
                    this.keepDirectories.add(dir);
                }
            }
        }
        final JSONArray savesJson = this.content.optJSONArray("saves");
        if (savesJson != null) {
            for (final JSONObject savesDir : new JsonIterator<JSONObject>(savesJson)) {
                if (savesDir != null) {
                    final String name = savesDir.optString("name");
                    String path = savesDir.optString("path");
                    if (name == null || path == null || name.equals("data")) {
                        continue;
                    }
                    path = path.replaceAll("\\{storage\\}", Environment.getExternalStorageDirectory().getAbsolutePath());
                    path = path.replaceAll("\\{internal\\}", com.zhekasmirnov.horizon.compiler.packages.Environment.getDataDirFile().getAbsolutePath());
                    path = path.replaceAll("\\{package_name\\}", HorizonApplication.getAppPackageName());
                    this.savesHoldersInfo.put(name, path);
                }
            }
        }
    }
    
    public PackManifest(final File file) throws IOException, JSONException {
        this(FileUtils.readJSON(file));
    }
    
    public ClassInfo getActivityInfoForName(final String name) {
        for (final ClassInfo info : this.activities) {
            if (name == null || name.equals(info.name)) {
                return info;
            }
        }
        return null;
    }
    
    public JSONObject getContent() {
        return this.content;
    }
    
    public String getPackVersionString() {
        return (this.packVersion != null) ? this.packVersion : ("" + this.packVersionCode);
    }
    
    public static class ClassInfo
    {
        public final String clazz;
        public final String name;
        public final String description;
        
        public ClassInfo(final String str) {
            this.clazz = str;
            this.name = str;
            this.description = null;
        }
        
        public ClassInfo(final JSONObject json) {
            this.clazz = json.optString("class");
            if (this.clazz != null) {
                this.name = json.optString("name", this.clazz);
                this.description = json.optString("description");
                return;
            }
            throw new IllegalArgumentException("class info missing class package ('class' field)");
        }
        
        public static ClassInfo fromObject(final Object obj) {
            if (obj instanceof JSONObject) {
                return new ClassInfo((JSONObject)obj);
            }
            if (obj instanceof String) {
                return new ClassInfo((String)obj);
            }
            throw new IllegalArgumentException("failed to parse class info: " + obj);
        }
        
        public Class getDeclaredClass(final ClassLoader loader) {
            try {
                return loader.loadClass(this.clazz);
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException("failed to load declared class: " + this.clazz, e);
            }
        }
        
        public Class getDeclaredClass() {
            return this.getDeclaredClass(this.getClass().getClassLoader());
        }
    }
}
