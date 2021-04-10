package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import java.io.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import android.content.res.*;
import java.util.*;

public class FlipbookTextureAtlas implements ResourceProcessor, RuntimeResourceHandler
{
    public final String asset;
    private JSONArray flipbookTextures;
    public final String name;
    
    public FlipbookTextureAtlas(final String asset, final String name) {
        this.asset = asset;
        this.name = name;
    }
    
    private void insertJson(final Resource resource) {
        Object atlasPath = resource.getAtlasPath();
        try {
            final JSONObject json = FileUtils.readJSON(resource.file);
            if (json.has("name")) {
                final String string = json.getString("name");
                if (json.has("tile")) {
                    final String string2 = json.getString("tile");
                    int n;
                    if (json.has("delay")) {
                        final int optInt = json.optInt("delay");
                        if ((n = optInt) < 1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("animation json has invalid delay: ");
                            sb.append((String)atlasPath);
                            Logger.error("BlockTextureAnimation", sb.toString());
                            n = optInt;
                        }
                    }
                    else {
                        n = 1;
                    }
                    try {
                        atlasPath = new JSONObject();
                        ((JSONObject)atlasPath).put("flipbook_texture", (Object)string);
                        ((JSONObject)atlasPath).put("atlas_tile", (Object)string2);
                        ((JSONObject)atlasPath).put("ticks_per_frame", n);
                        this.flipbookTextures.put(atlasPath);
                        return;
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("animation json missing tile: ");
                sb2.append((String)atlasPath);
                Logger.error("BlockTextureAnimation", sb2.toString());
                return;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("animation json missing name: ");
            sb3.append((String)atlasPath);
            Logger.error("BlockTextureAnimation", sb3.toString());
        }
        catch (Exception ex2) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("animation json has invalid json: ");
            sb4.append((String)atlasPath);
            Logger.error("BlockTextureAnimation", sb4.toString());
            ex2.printStackTrace();
        }
    }
    
    private void insertPng(final Resource resource) {
        final String atlasPath = resource.getAtlasPath();
        final String[] split = resource.getName().split("\\.");
        Label_0113: {
            if (split.length > 2 && split[split.length - 2].equals("anim")) {
                final int intValue = 1;
                break Label_0113;
            }
            if (split.length <= 3 || !split[split.length - 3].equals("anim")) {
                break Label_0113;
            }
            try {
                final int intValue;
                if ((intValue = Integer.valueOf(split[split.length - 2])) < 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invalid animation delay: ");
                    sb.append(atlasPath);
                    Logger.error("BlockTextureAnimation", sb.toString());
                    return;
                }
                final String s = split[0];
                try {
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("flipbook_texture", (Object)atlasPath);
                    jsonObject.put("atlas_tile", (Object)s);
                    jsonObject.put("ticks_per_frame", intValue);
                    this.flipbookTextures.put((Object)jsonObject);
                    return;
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            catch (Exception ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("invalid animation delay: ");
                sb2.append(atlasPath);
                Logger.error("BlockTextureAnimation", sb2.toString());
                return;
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("invalid animation name: ");
        sb3.append(atlasPath);
        Logger.error("BlockTextureAnimation", sb3.toString());
    }
    
    public String getResourceName() {
        return this.name;
    }
    
    public String getResourcePath() {
        return this.asset;
    }
    
    public void handle(final RuntimeResource runtimeResource) {
        try {
            FileUtils.writeJSON(runtimeResource.getFile(), this.flipbookTextures);
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
            this.flipbookTextures = new JSONArray(FileUtils.readStringFromAsset(assets, sb.toString()));
        }
        catch (JSONException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to read json for flipbook atlas: ");
            sb2.append(this.asset);
            sb2.append(" ");
            sb2.append(ex);
            throw new RuntimeException(sb2.toString(), (Throwable)ex);
        }
        catch (IOException ex2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("failed to read json for flipbook atlas: ");
            sb3.append(this.asset);
            sb3.append(" ");
            sb3.append(ex2);
            throw new RuntimeException(sb3.toString(), ex2);
        }
    }
    
    public void process(final Resource resource, final Collection<Resource> collection) {
        collection.add(resource);
        final String name = resource.getName();
        if (name.matches("[^\\.]+\\.anim(\\.[0-9]+)?\\.png")) {
            this.insertPng(resource);
            return;
        }
        if (name.endsWith(".json") && name.contains(".anim.")) {
            this.insertJson(resource);
        }
    }
}
