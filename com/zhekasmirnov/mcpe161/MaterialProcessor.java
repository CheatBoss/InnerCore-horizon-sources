package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import org.json.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import android.content.res.*;
import java.util.*;

public class MaterialProcessor implements ResourceProcessor, RuntimeResourceHandler
{
    public final String asset;
    private HashMap<String, String> availableShaderPaths;
    public final String materialDirectoryName;
    private JSONObject materialsJson;
    public final String name;
    private JSONObject rootJson;
    public final String shaderDirectoryName;
    private HashMap<String, List<ShaderPathInJson>> shaderPaths;
    private ShaderUniformsListResource shaderUniformList;
    
    public MaterialProcessor(final String asset, final String name, final String materialDirectoryName, final String shaderDirectoryName) {
        this.shaderPaths = new HashMap<String, List<ShaderPathInJson>>();
        this.availableShaderPaths = new HashMap<String, String>();
        this.shaderUniformList = null;
        this.asset = asset;
        this.name = name;
        this.materialDirectoryName = materialDirectoryName;
        this.shaderDirectoryName = shaderDirectoryName;
    }
    
    private void addShaderPathOverrideIfRequired(final JSONObject jsonObject, final String s) {
        final String optString = jsonObject.optString(s, (String)null);
        if (optString != null) {
            boolean b = false;
            String s2;
            if (optString.startsWith("./")) {
                s2 = optString.substring(2);
                b = true;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.shaderDirectoryName);
                sb.append("/");
                final String string = sb.toString();
                s2 = optString;
                if (optString.contains(string)) {
                    s2 = optString.substring(optString.indexOf(string) + string.length());
                    b = true;
                }
            }
            if (b) {
                final String trim = s2.toLowerCase().trim();
                if (this.availableShaderPaths.containsKey(trim)) {
                    try {
                        jsonObject.put(s, (Object)this.availableShaderPaths.get(trim));
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!this.shaderPaths.containsKey(trim)) {
                    this.shaderPaths.put(trim, new ArrayList<ShaderPathInJson>());
                }
                this.shaderPaths.get(trim).add(new ShaderPathInJson(jsonObject, s, trim));
            }
        }
    }
    
    private void addShaderUniforms(final Resource resource) {
        try {
            this.addShaderUniforms(FileUtils.readJSON(resource.file));
        }
        catch (JSONException | IOException ex) {
            final Object o2;
            final Object o = o2;
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to read json from: ");
            sb.append(resource.file.getAbsolutePath());
            ICLog.e("ERROR", sb.toString(), (Throwable)o);
        }
    }
    
    private void addShaderUniforms(final JSONObject jsonObject) {
        if (this.shaderUniformList != null) {
            for (final String s : new JsonIterator(jsonObject)) {
                final JSONObject optJSONObject = jsonObject.optJSONObject(s);
                if (optJSONObject != null) {
                    for (final String s2 : new JsonIterator(optJSONObject)) {
                        this.shaderUniformList.addUniform(s, s2, optJSONObject.optString(s2));
                    }
                }
            }
        }
    }
    
    private void mergeMaterials(final Resource resource) {
        try {
            this.mergeMaterials(FileUtils.readJSON(resource.file));
        }
        catch (JSONException | IOException ex) {
            final Object o2;
            final Object o = o2;
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to read json from: ");
            sb.append(resource.file.getAbsolutePath());
            ICLog.e("ERROR", sb.toString(), (Throwable)o);
        }
    }
    
    private void mergeMaterials(final JSONObject jsonObject) {
        for (final String s : new JsonIterator(jsonObject)) {
            final JSONObject optJSONObject = jsonObject.optJSONObject(s);
            try {
                this.materialsJson.put(s, (Object)optJSONObject);
                this.addShaderPathOverrideIfRequired(optJSONObject, "vertexShader");
                this.addShaderPathOverrideIfRequired(optJSONObject, "fragmentShader");
                this.addShaderPathOverrideIfRequired(optJSONObject, "vrGeometryShader");
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private Resource overrideShaderPath(final Resource resource) {
        final String path = resource.getPath();
        final String substring = path.substring(path.indexOf(this.shaderDirectoryName) + this.shaderDirectoryName.length() + 1);
        final StringBuilder sb = new StringBuilder();
        sb.append("shaders/glsl/");
        sb.append(substring);
        final String string = sb.toString();
        if (this.shaderPaths.containsKey(substring)) {
            for (final ShaderPathInJson shaderPathInJson : this.shaderPaths.get(substring)) {
                try {
                    shaderPathInJson.json.put(shaderPathInJson.shaderPathKey, (Object)string);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
        this.availableShaderPaths.put(substring, string);
        return resource.getLink(string);
    }
    
    public String getResourceName() {
        return this.name;
    }
    
    public String getResourcePath() {
        return this.asset;
    }
    
    public void handle(final RuntimeResource runtimeResource) {
        try {
            FileUtils.writeJSON(runtimeResource.getFile(), this.rootJson);
        }
        catch (IOException ex) {
            throw new RuntimeException("unexpected exception inside MaterialProcessor.handle()", ex);
        }
    }
    
    public void initialize(final ResourceManager resourceManager) {
        try {
            this.shaderPaths.clear();
            final AssetManager assets = resourceManager.getAssets();
            final StringBuilder sb = new StringBuilder();
            sb.append("resource_packs/vanilla/");
            sb.append(this.asset);
            this.rootJson = new JSONObject(FileUtils.readStringFromAsset(assets, sb.toString()));
            this.materialsJson = this.rootJson.optJSONObject("materials");
            if (this.materialsJson == null) {
                this.materialsJson = new JSONObject();
                try {
                    this.rootJson.put("materials", (Object)this.materialsJson);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
            if (this.shaderUniformList != null) {
                this.shaderUniformList.initializeJson(resourceManager);
            }
        }
        catch (JSONException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to read json for flipbook atlas: ");
            sb2.append(this.asset);
            sb2.append(" ");
            sb2.append(ex2);
            throw new RuntimeException(sb2.toString(), (Throwable)ex2);
        }
        catch (IOException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("failed to read json for flipbook atlas: ");
            sb3.append(this.asset);
            sb3.append(" ");
            sb3.append(ex3);
            throw new RuntimeException(sb3.toString(), ex3);
        }
    }
    
    public ShaderUniformsListResource newShaderUniformList(final String s, final String s2) {
        return this.shaderUniformList = new ShaderUniformsListResource(s, s2);
    }
    
    public void process(Resource overrideShaderPath, final Collection<Resource> collection) {
        collection.add(overrideShaderPath);
        final String path = overrideShaderPath.getPath();
        if (!path.contains(this.shaderDirectoryName)) {
            if (path.contains(this.materialDirectoryName)) {
                this.mergeMaterials(overrideShaderPath);
            }
            return;
        }
        if (path.endsWith(".uniforms")) {
            this.addShaderUniforms(overrideShaderPath);
            return;
        }
        overrideShaderPath = this.overrideShaderPath(overrideShaderPath);
        if (overrideShaderPath != null) {
            collection.add(overrideShaderPath);
        }
    }
    
    class ShaderPathInJson
    {
        public final JSONObject json;
        public final String originalPath;
        public final String shaderPathKey;
        
        ShaderPathInJson(final JSONObject json, final String shaderPathKey, final String originalPath) {
            this.json = json;
            this.shaderPathKey = shaderPathKey;
            this.originalPath = originalPath;
        }
    }
}
