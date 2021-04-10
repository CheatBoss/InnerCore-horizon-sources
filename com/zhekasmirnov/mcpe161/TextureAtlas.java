package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import com.zhekasmirnov.innercore.mod.resource.types.enums.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import org.json.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.content.res.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.resource.*;

public class TextureAtlas implements ResourceProcessor, RuntimeResourceHandler
{
    public final String asset;
    private JSONObject atlas;
    private final String directoryName;
    public final String name;
    private JSONObject textureData;
    private TextureType type;
    
    public TextureAtlas(final TextureType type, final String asset, final String name, final String directoryName) {
        this.type = type;
        this.asset = asset;
        this.name = name;
        this.directoryName = directoryName;
    }
    
    private void insert(final Resource resource) {
        final String nameWithoutExtension = resource.getNameWithoutExtension();
        final String atlasPath = resource.getAtlasPath();
        final int index = resource.getIndex();
        try {
            this.insert(nameWithoutExtension, index, atlasPath);
        }
        catch (JSONException ex) {
            throw new RuntimeException("unexpected exception inside TextureAtlas.insert(Resource)", (Throwable)ex);
        }
    }
    
    private void insert(final String s, final int n, final String s2) throws JSONException {
        JSONObject optJSONObject;
        if ((optJSONObject = this.textureData.optJSONObject(s)) == null) {
            optJSONObject = new JSONObject();
            optJSONObject.put("textures", (Object)new JSONArray());
            this.textureData.put(s, (Object)optJSONObject);
        }
        final Object opt = optJSONObject.opt("textures");
        JSONArray jsonArray;
        if (opt instanceof JSONArray) {
            jsonArray = (JSONArray)opt;
        }
        else if (opt instanceof String) {
            jsonArray = new JSONArray();
            jsonArray.put(opt);
        }
        else {
            jsonArray = new JSONArray();
        }
        for (int i = 0; i < n; ++i) {
            if (jsonArray.optString(i) == "") {
                jsonArray.put(i, (Object)s2);
            }
        }
        jsonArray.put(n, (Object)s2);
        optJSONObject.put("textures", (Object)jsonArray);
    }
    
    public String getResourceName() {
        return this.name;
    }
    
    public String getResourcePath() {
        return this.asset;
    }
    
    public void handle(final RuntimeResource runtimeResource) {
        try {
            FileUtils.writeJSON(runtimeResource.getFile(), this.atlas);
        }
        catch (IOException ex) {
            throw new RuntimeException("unexpected exception inside TextureAtlas.handle()", ex);
        }
    }
    
    public void initialize(final ResourceManager resourceManager) {
        try {
            final AssetManager assets = resourceManager.getAssets();
            final StringBuilder sb = new StringBuilder();
            sb.append("resource_packs/vanilla/");
            sb.append(this.asset);
            this.atlas = FileUtils.readJSONFromAssets(assets, sb.toString());
            this.textureData = this.atlas.getJSONObject("texture_data");
        }
        catch (IOException | JSONException ex) {
            final Object o2;
            final Object o = o2;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to read json for texture atlas: ");
            sb2.append(this.asset);
            sb2.append(" ");
            sb2.append(o);
            Logger.debug(sb2.toString());
        }
    }
    
    public void process(final Resource resource, final Collection<Resource> collection) {
        collection.add(resource);
        if (resource.getPath().contains(this.directoryName) && "png".equals(resource.getExtension())) {
            this.insert(resource);
            ResourcePackManager.instance.resourceStorage.addResourceFile(this.type, resource);
        }
    }
}
