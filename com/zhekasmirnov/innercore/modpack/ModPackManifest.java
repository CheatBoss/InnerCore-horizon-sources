package com.zhekasmirnov.innercore.modpack;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.modpack.strategy.extract.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import android.text.*;
import java.io.*;
import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import com.zhekasmirnov.innercore.modpack.strategy.request.*;
import com.zhekasmirnov.innercore.modpack.strategy.update.*;

@SynthesizedClassMap({ -$$Lambda$ModPackManifest$Wm5hh6irX7JaMSGlFx8xkp09pY8.class })
public class ModPackManifest
{
    private String author;
    private final List<DeclaredDirectory> declaredDirectories;
    private String description;
    private String displayedName;
    private String packName;
    private File source;
    private int versionCode;
    private String versionName;
    
    public ModPackManifest() {
        this.declaredDirectories = new ArrayList<DeclaredDirectory>();
    }
    
    public List<ModPackDirectory> createDeclaredDirectoriesForModPack(final ModPack modPack) {
        return Java8BackComp.stream(this.declaredDirectories).map((Function<? super DeclaredDirectory, ?>)new -$$Lambda$ModPackManifest$Wm5hh6irX7JaMSGlFx8xkp09pY8(modPack)).collect((Collector<? super Object, ?, List<ModPackDirectory>>)Collectors.toList());
    }
    
    public ModPackManifestEditor edit() throws IOException, JSONException {
        return new ModPackManifestEditor(this, this.source);
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public List<DeclaredDirectory> getDeclaredDirectories() {
        return this.declaredDirectories;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getDisplayedName() {
        if (!TextUtils.isEmpty((CharSequence)this.displayedName)) {
            return this.displayedName;
        }
        return this.packName;
    }
    
    public String getPackName() {
        return this.packName;
    }
    
    public int getVersionCode() {
        return this.versionCode;
    }
    
    public String getVersionName() {
        return this.versionName;
    }
    
    public void loadFile(final File source) throws IOException, JSONException {
        this.source = source;
        this.loadInputStream(new FileInputStream(source));
    }
    
    public void loadInputStream(final InputStream inputStream) throws IOException, JSONException {
        this.loadJson(new JSONObject(FileUtils.convertStreamToString(inputStream)));
    }
    
    public void loadJson(JSONObject o) {
        this.packName = ((JSONObject)o).optString("packName", ((JSONObject)o).optString("name"));
        this.displayedName = LocaleUtils.resolveLocaleJsonProperty((JSONObject)o, "displayedName");
        this.versionName = LocaleUtils.resolveLocaleJsonProperty((JSONObject)o, "versionName");
        this.versionCode = ((JSONObject)o).optInt("versionCode");
        this.author = LocaleUtils.resolveLocaleJsonProperty((JSONObject)o, "author");
        this.description = LocaleUtils.resolveLocaleJsonProperty((JSONObject)o, "description");
        this.declaredDirectories.clear();
        final JSONArray optJSONArray = ((JSONObject)o).optJSONArray("directories");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); ++i) {
                o = optJSONArray.optJSONObject(i);
                if (o != null) {
                    final String optString = ((JSONObject)o).optString("path");
                    final String optString2 = ((JSONObject)o).optString("type");
                    if (TextUtils.isEmpty((CharSequence)optString) && !TextUtils.isEmpty((CharSequence)optString2)) {
                        o = DeclaredDirectoryType.INVALID;
                        try {
                            o = DeclaredDirectoryType.valueOf(optString2);
                        }
                        catch (IllegalArgumentException ex) {}
                        this.declaredDirectories.add(new DeclaredDirectory((DeclaredDirectoryType)o, optString));
                    }
                }
            }
        }
    }
    
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setDisplayedName(final String displayedName) {
        this.displayedName = displayedName;
    }
    
    public void setPackName(final String packName) {
        this.packName = packName;
    }
    
    public void setVersionCode(final int versionCode) {
        this.versionCode = versionCode;
    }
    
    public void setVersionName(final String versionName) {
        this.versionName = versionName;
    }
    
    public class DeclaredDirectory
    {
        public final String path;
        public final DeclaredDirectoryType type;
        
        public DeclaredDirectory(final DeclaredDirectoryType type, final String path) {
            this.type = type;
            this.path = path;
        }
        
        public String getPath() {
            return this.path;
        }
        
        public DeclaredDirectoryType getType() {
            return this.type;
        }
    }
    
    public enum DeclaredDirectoryType
    {
        CACHE, 
        CONFIG, 
        INVALID, 
        RESOURCE, 
        USER_DATA;
        
        public static DirectoryRequestStrategy createDirectoryRequestStrategy(final DeclaredDirectoryType declaredDirectoryType) {
            switch (declaredDirectoryType) {
                default: {
                    return new NoAccessDirectoryRequestStrategy();
                }
                case RESOURCE:
                case USER_DATA:
                case CACHE:
                case CONFIG: {
                    return new DefaultDirectoryRequestStrategy();
                }
            }
        }
        
        public static DirectoryUpdateStrategy createDirectoryUpdateStrategy(final DeclaredDirectoryType declaredDirectoryType) {
            final int n = ModPackManifest$1.$SwitchMap$com$zhekasmirnov$innercore$modpack$ModPackManifest$DeclaredDirectoryType[declaredDirectoryType.ordinal()];
            if (n == 1) {
                return new ResourceDirectoryUpdateStrategy();
            }
            switch (n) {
                default: {
                    return new UserDataDirectoryUpdateStrategy();
                }
                case 4: {
                    return new JsonMergeDirectoryUpdateStrategy();
                }
                case 3: {
                    return new CacheDirectoryUpdateStrategy();
                }
            }
        }
    }
}
