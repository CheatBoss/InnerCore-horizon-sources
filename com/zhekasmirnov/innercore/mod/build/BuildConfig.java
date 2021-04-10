package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.mod.build.enums.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.mod.executable.*;

public class BuildConfig
{
    public ArrayList<BuildableDir> buildableDirs;
    private BuildConfigError configError;
    private File configFile;
    private JSONObject configJson;
    public DefaultConfig defaultConfig;
    private boolean isValid;
    public ArrayList<DeclaredDirectory> javaDirectories;
    public ArrayList<DeclaredDirectory> nativeDirectories;
    public ArrayList<ResourceDir> resourceDirs;
    public ArrayList<Source> sourcesToCompile;
    
    public BuildConfig() {
        this.isValid = false;
        this.configError = BuildConfigError.NONE;
        this.buildableDirs = new ArrayList<BuildableDir>();
        this.resourceDirs = new ArrayList<ResourceDir>();
        this.sourcesToCompile = new ArrayList<Source>();
        this.javaDirectories = new ArrayList<DeclaredDirectory>();
        this.nativeDirectories = new ArrayList<DeclaredDirectory>();
        this.configJson = new JSONObject();
        this.isValid = true;
    }
    
    public BuildConfig(final File configFile) {
        this.isValid = false;
        this.configError = BuildConfigError.NONE;
        this.buildableDirs = new ArrayList<BuildableDir>();
        this.resourceDirs = new ArrayList<ResourceDir>();
        this.sourcesToCompile = new ArrayList<Source>();
        this.javaDirectories = new ArrayList<DeclaredDirectory>();
        this.nativeDirectories = new ArrayList<DeclaredDirectory>();
        this.configFile = configFile;
        this.isValid = false;
        try {
            this.configJson = FileTools.readJSON(configFile.getAbsolutePath());
            this.isValid = true;
        }
        catch (JSONException ex) {
            this.configError = BuildConfigError.PARSE_ERROR;
            ex.printStackTrace();
        }
        catch (IOException ex2) {
            this.configError = BuildConfigError.FILE_ERROR;
            ex2.printStackTrace();
        }
    }
    
    public BuildConfig(final JSONObject configJson) {
        this.isValid = false;
        this.configError = BuildConfigError.NONE;
        this.buildableDirs = new ArrayList<BuildableDir>();
        this.resourceDirs = new ArrayList<ResourceDir>();
        this.sourcesToCompile = new ArrayList<Source>();
        this.javaDirectories = new ArrayList<DeclaredDirectory>();
        this.nativeDirectories = new ArrayList<DeclaredDirectory>();
        this.configJson = configJson;
        this.isValid = true;
    }
    
    public static API getAPIFromJSON(final JSONObject jsonObject) {
        if (jsonObject.has("api")) {
            try {
                return API.getInstanceByName(jsonObject.getString("api"));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    
    public static BuildType getBuildTypeFromJSON(final JSONObject jsonObject) {
        BuildType buildType2;
        BuildType buildType = buildType2 = BuildType.DEVELOP;
        try {
            if (jsonObject.has("buildType")) {
                buildType = buildType;
                buildType2 = BuildType.fromString(jsonObject.getString("buildType"));
            }
            buildType = buildType2;
            jsonObject.put("buildType", (Object)buildType2.toString());
            return buildType2;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return buildType;
        }
    }
    
    public static int getOptimizationLevelFromJSON(final JSONObject jsonObject) {
        if (jsonObject.has("optimizationLevel")) {
            return validateOptimizationLevel(jsonObject.optInt("optimizationLevel", -1));
        }
        return 9;
    }
    
    public static ResourceDirType getResourceDirTypeFromJSON(final JSONObject jsonObject) {
        ResourceDirType resourceDirType2;
        ResourceDirType resourceDirType = resourceDirType2 = ResourceDirType.RESOURCE;
        try {
            if (jsonObject.has("resourceType")) {
                resourceDirType = resourceDirType;
                resourceDirType2 = ResourceDirType.fromString(jsonObject.getString("resourceType"));
            }
            resourceDirType = resourceDirType2;
            jsonObject.put("resourceType", (Object)resourceDirType2.toString());
            return resourceDirType2;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return resourceDirType;
        }
    }
    
    public static SourceType getSourceTypeFromJSON(final JSONObject jsonObject) {
        SourceType sourceType2;
        SourceType sourceType = sourceType2 = SourceType.MOD;
        try {
            if (jsonObject.has("sourceType")) {
                sourceType = sourceType;
                sourceType2 = SourceType.fromString(jsonObject.getString("sourceType"));
            }
            sourceType = sourceType2;
            jsonObject.put("sourceType", (Object)sourceType2.toString());
            return sourceType2;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return sourceType;
        }
    }
    
    public static int validateOptimizationLevel(final int n) {
        return Math.min(9, Math.max(-1, n));
    }
    
    public BuildableDir findRelatedBuildableDir(final Source source) {
        for (final BuildableDir buildableDir : this.buildableDirs) {
            if (buildableDir.isRelatedSource(source)) {
                return buildableDir;
            }
        }
        return null;
    }
    
    public ArrayList<Source> getAllSourcesToCompile(final boolean b) {
        final ArrayList<Source> list = new ArrayList<Source>(this.sourcesToCompile);
        if (this.defaultConfig.libDir != null) {
            final File file = new File(this.configFile.getParent(), this.defaultConfig.libDir);
            if (file.exists() && file.isDirectory()) {
                final File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (int length = listFiles.length, i = 0; i < length; ++i) {
                        final File file2 = listFiles[i];
                        final StringBuilder sb = new StringBuilder();
                        sb.append("found library file ");
                        sb.append(file2);
                        sb.append(" local_path=");
                        sb.append(this.defaultConfig.libDir);
                        sb.append(file2.getName());
                        Logger.debug("LIB-DIR", sb.toString());
                        if (!file2.isDirectory()) {
                            final Source source = new Source(new JSONObject());
                            if (b) {
                                source.setAPI(this.defaultConfig.apiInstance);
                            }
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(this.defaultConfig.libDir);
                            sb2.append(file2.getName());
                            source.setPath(sb2.toString());
                            source.setSourceName(file2.getName());
                            source.setSourceType(SourceType.LIBRARY);
                            list.add(source);
                        }
                    }
                }
            }
        }
        return list;
    }
    
    public BuildType getBuildType() {
        return this.defaultConfig.buildType;
    }
    
    public API getDefaultAPI() {
        return this.defaultConfig.apiInstance;
    }
    
    public String getName() {
        if (this.configFile != null) {
            return this.configFile.getParentFile().getName();
        }
        return "Unknown Mod";
    }
    
    public boolean isValid() {
        return this.isValid;
    }
    
    public boolean read() {
        if (this.isValid) {
            this.validate();
        }
        final boolean isValid = this.isValid;
        final int n = 0;
        if (!isValid) {
            return false;
        }
        this.defaultConfig = DefaultConfig.fromJson(this.configJson.optJSONObject("defaultConfig"));
        this.buildableDirs.clear();
        final JSONArray optJSONArray = this.configJson.optJSONArray("buildDirs");
        for (int i = 0; i < optJSONArray.length(); ++i) {
            this.buildableDirs.add(BuildableDir.fromJson(optJSONArray.optJSONObject(i)));
        }
        this.resourceDirs.clear();
        final JSONArray optJSONArray2 = this.configJson.optJSONArray("resources");
        for (int j = 0; j < optJSONArray2.length(); ++j) {
            this.resourceDirs.add(ResourceDir.fromJson(optJSONArray2.optJSONObject(j)));
        }
        this.sourcesToCompile.clear();
        final JSONArray optJSONArray3 = this.configJson.optJSONArray("compile");
        for (int k = 0; k < optJSONArray3.length(); ++k) {
            this.sourcesToCompile.add(Source.fromJson(optJSONArray3.optJSONObject(k), this.defaultConfig));
        }
        this.javaDirectories.clear();
        final JSONArray optJSONArray4 = this.configJson.optJSONArray("javaDirs");
        for (int l = 0; l < optJSONArray4.length(); ++l) {
            final JSONObject optJSONObject = optJSONArray4.optJSONObject(l);
            try {
                final DeclaredDirectory fromJson = DeclaredDirectory.fromJson(optJSONObject, "path");
                if (fromJson != null) {
                    this.javaDirectories.add(fromJson);
                }
            }
            catch (Exception ex) {
                ICLog.e("InnerCore-BuildConfig", "invalid java directory object", ex);
            }
        }
        this.nativeDirectories.clear();
        final JSONArray optJSONArray5 = this.configJson.optJSONArray("nativeDirs");
        for (int n2 = n; n2 < optJSONArray5.length(); ++n2) {
            final JSONObject optJSONObject2 = optJSONArray5.optJSONObject(n2);
            try {
                final DeclaredDirectory fromJson2 = DeclaredDirectory.fromJson(optJSONObject2, "path");
                if (fromJson2 != null) {
                    this.nativeDirectories.add(fromJson2);
                }
            }
            catch (Exception ex2) {
                ICLog.e("InnerCore-BuildConfig", "invalid native directory object", ex2);
            }
        }
        return true;
    }
    
    public void save() {
        this.save(this.configFile);
    }
    
    public void save(final File file) {
        try {
            FileTools.writeJSON(file.getAbsolutePath(), this.configJson);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void validate() {
        try {
            if (this.configJson.optJSONObject("defaultConfig") == null) {
                this.configJson.put("defaultConfig", (Object)new JSONObject());
            }
            if (this.configJson.optJSONArray("buildDirs") == null) {
                this.configJson.put("buildDirs", (Object)new JSONArray());
            }
            if (this.configJson.optJSONArray("compile") == null) {
                this.configJson.put("compile", (Object)new JSONArray());
            }
            if (this.configJson.optJSONArray("resources") == null) {
                this.configJson.put("resources", (Object)new JSONArray());
            }
            if (this.configJson.optJSONArray("nativeDirs") == null) {
                this.configJson.put("nativeDirs", (Object)new JSONArray());
            }
            if (this.configJson.optJSONArray("javaDirs") == null) {
                this.configJson.put("javaDirs", (Object)new JSONArray());
            }
        }
        catch (JSONException ex) {
            this.isValid = false;
            ex.printStackTrace();
        }
    }
    
    public static class BuildableDir
    {
        public String dir;
        public JSONObject json;
        public String targetSource;
        
        private BuildableDir(final JSONObject json) {
            this.json = json;
        }
        
        public static BuildableDir fromJson(final JSONObject jsonObject) {
            final BuildableDir buildableDir = new BuildableDir(jsonObject);
            buildableDir.dir = jsonObject.optString("dir");
            buildableDir.targetSource = jsonObject.optString("targetSource");
            return buildableDir;
        }
        
        public boolean isRelatedSource(final Source source) {
            return source != null && this.targetSource != null && this.targetSource.equals(source.path);
        }
        
        public void setDir(final String dir) {
            this.dir = dir;
            try {
                this.json.put("dir", (Object)dir);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setTargetSource(final String targetSource) {
            this.targetSource = targetSource;
            try {
                this.json.put("targetSource", (Object)targetSource);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static class DeclaredDirectory
    {
        public final String path;
        public final ResourceGameVersion version;
        
        public DeclaredDirectory(final String path, final ResourceGameVersion version) {
            this.path = path;
            this.version = version;
        }
        
        public static DeclaredDirectory fromJson(final JSONObject jsonObject, String optString) {
            optString = jsonObject.optString(optString);
            if (optString == null) {
                return null;
            }
            return new DeclaredDirectory(optString, new ResourceGameVersion(jsonObject));
        }
        
        public File getFile(final File file) {
            return new File(file, this.path);
        }
    }
    
    public static class DefaultConfig
    {
        public API apiInstance;
        public String behaviorPacksDir;
        public BuildType buildType;
        public final ResourceGameVersion gameVersion;
        public JSONObject json;
        public String libDir;
        public int optimizationLevel;
        public String resourcePacksDir;
        public String setupScriptDir;
        
        private DefaultConfig(final JSONObject json) {
            this.libDir = null;
            this.resourcePacksDir = null;
            this.behaviorPacksDir = null;
            this.setupScriptDir = null;
            this.json = json;
            this.gameVersion = new ResourceGameVersion(json);
        }
        
        public static DefaultConfig fromJson(final JSONObject jsonObject) {
            final DefaultConfig defaultConfig = new DefaultConfig(jsonObject);
            defaultConfig.apiInstance = BuildConfig.getAPIFromJSON(jsonObject);
            defaultConfig.optimizationLevel = BuildConfig.getOptimizationLevelFromJSON(jsonObject);
            defaultConfig.buildType = BuildConfig.getBuildTypeFromJSON(jsonObject);
            defaultConfig.libDir = jsonObject.optString("libraryDir", (String)null);
            defaultConfig.resourcePacksDir = jsonObject.optString("resourcePacksDir", (String)null);
            defaultConfig.behaviorPacksDir = jsonObject.optString("behaviorPacksDir", (String)null);
            defaultConfig.setupScriptDir = jsonObject.optString("setupScript", (String)null);
            return defaultConfig;
        }
        
        public void setAPI(final API apiInstance) {
            this.apiInstance = apiInstance;
            try {
                this.json.put("api", (Object)apiInstance.getName());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setBuildType(final BuildType buildType) {
            this.buildType = buildType;
            try {
                this.json.put("buildType", (Object)buildType.toString());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setLibDir(final String libDir) {
            this.libDir = libDir;
            try {
                this.json.put("libraryDir", (Object)libDir);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setMinecraftBehaviorPacksDir(final String behaviorPacksDir) {
            this.behaviorPacksDir = behaviorPacksDir;
            try {
                this.json.put("behaviorPacksDir", (Object)behaviorPacksDir);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setMinecraftResourcePacksDir(final String resourcePacksDir) {
            this.resourcePacksDir = resourcePacksDir;
            try {
                this.json.put("resourcePacksDir", (Object)resourcePacksDir);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setOptimizationLevel(final int n) {
            this.optimizationLevel = BuildConfig.validateOptimizationLevel(n);
            try {
                this.json.put("optimizationLevel", n);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setSetupScriptDir(final String setupScriptDir) {
            this.setupScriptDir = setupScriptDir;
            try {
                this.json.put("setupScript", (Object)setupScriptDir);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static class ResourceDir
    {
        public final ResourceGameVersion gameVersion;
        public JSONObject json;
        public String path;
        public ResourceDirType resourceType;
        
        private ResourceDir(final JSONObject json) {
            this.json = json;
            this.gameVersion = new ResourceGameVersion(json);
        }
        
        public static ResourceDir fromJson(final JSONObject jsonObject) {
            final ResourceDir resourceDir = new ResourceDir(jsonObject);
            resourceDir.path = jsonObject.optString("path");
            resourceDir.resourceType = BuildConfig.getResourceDirTypeFromJSON(jsonObject);
            return resourceDir;
        }
        
        public void setPath(final String path) {
            this.path = path;
            try {
                this.json.put("path", (Object)path);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setResourceType(final ResourceDirType resourceType) {
            this.resourceType = resourceType;
            try {
                this.json.put("resourceType", (Object)resourceType.toString());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static class Source
    {
        public API apiInstance;
        public final ResourceGameVersion gameVersion;
        public JSONObject json;
        public int optimizationLevel;
        public String path;
        public String sourceName;
        public SourceType sourceType;
        
        private Source(final JSONObject json) {
            this.json = json;
            this.gameVersion = new ResourceGameVersion(json);
        }
        
        public static Source fromJson(final JSONObject jsonObject, final DefaultConfig defaultConfig) {
            final Source source = new Source(jsonObject);
            source.path = jsonObject.optString("path");
            source.sourceType = BuildConfig.getSourceTypeFromJSON(jsonObject);
            if (jsonObject.has("sourceName")) {
                source.sourceName = jsonObject.optString("sourceName", "Unknown Source");
            }
            else {
                source.sourceName = source.path.substring(source.path.lastIndexOf("/") + 1);
            }
            if (jsonObject.has("api")) {
                source.apiInstance = BuildConfig.getAPIFromJSON(jsonObject);
            }
            else if (source.sourceType == SourceType.PRELOADER) {
                source.apiInstance = API.getInstanceByName("Preloader");
            }
            else {
                source.apiInstance = defaultConfig.apiInstance;
            }
            if (jsonObject.has("optimizationLevel")) {
                source.optimizationLevel = BuildConfig.getOptimizationLevelFromJSON(jsonObject);
                return source;
            }
            source.optimizationLevel = defaultConfig.optimizationLevel;
            return source;
        }
        
        public CompilerConfig getCompilerConfig() {
            final CompilerConfig compilerConfig = new CompilerConfig(this.apiInstance);
            compilerConfig.setName(this.sourceName);
            compilerConfig.setOptimizationLevel(this.optimizationLevel);
            compilerConfig.isLibrary = (this.sourceType == SourceType.LIBRARY);
            return compilerConfig;
        }
        
        public void setAPI(final API apiInstance) {
            this.apiInstance = apiInstance;
            try {
                this.json.put("api", (Object)apiInstance.getName());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setOptimizationLevel(final int n) {
            this.optimizationLevel = BuildConfig.validateOptimizationLevel(n);
            try {
                this.json.put("optimizationLevel", n);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setPath(final String path) {
            this.path = path;
            try {
                this.json.put("path", (Object)path);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setSourceName(final String sourceName) {
            this.sourceName = sourceName;
            try {
                this.json.put("sourceName", (Object)sourceName);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        
        public void setSourceType(final SourceType sourceType) {
            this.sourceType = sourceType;
            try {
                this.json.put("sourceType", (Object)sourceType.toString());
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
