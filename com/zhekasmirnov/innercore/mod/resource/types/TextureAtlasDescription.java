package com.zhekasmirnov.innercore.mod.resource.types;

import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.utils.*;
import java.util.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;

public class TextureAtlasDescription
{
    public JSONObject jsonObject;
    public JSONObject textureData;
    
    public TextureAtlasDescription(final String s) {
        this.textureData = new JSONObject();
        this.jsonObject = new JSONObject();
        try {
            this.jsonObject.put("texture_data", (Object)this.textureData);
        }
        catch (JSONException ex2) {}
        final String[] vanillaResourcePacksDirs = MinecraftVersions.getCurrent().getVanillaResourcePacksDirs();
        for (int length = vanillaResourcePacksDirs.length, i = 0; i < length; ++i) {
            final String s2 = vanillaResourcePacksDirs[i];
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append(s);
                final JSONObject optJSONObject = FileTools.getAssetAsJSON(sb.toString()).optJSONObject("texture_data");
                if (optJSONObject != null) {
                    final Iterator keys = optJSONObject.keys();
                    while (keys.hasNext()) {
                        final String s3 = keys.next();
                        this.textureData.put(s3, optJSONObject.opt(s3));
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public TextureAtlasDescription(final JSONObject jsonObject) {
        try {
            this.jsonObject = jsonObject;
            this.textureData = this.jsonObject.getJSONObject("texture_data");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void addTextureFile(final File file, final String ex) throws JSONException {
        try {
            try {
                final String name = file.getName();
                final String substring = name.substring(0, name.lastIndexOf(46));
                this.addTexturePath(substring.substring(0, substring.lastIndexOf(95)), Integer.valueOf(substring.substring(substring.lastIndexOf(95) + 1)), (String)ex);
            }
            catch (Exception ex) {}
        }
        catch (StringIndexOutOfBoundsException | NumberFormatException ex2) {
            final String name2 = file.getName();
            final String substring2 = name2.substring(0, name2.lastIndexOf(46));
            final int textureCount = this.getTextureCount(substring2);
            if (textureCount > 0) {
                ICLog.i("ERROR", "found texture with no index that conflicts with already added texture, add aborted");
            }
            else {
                this.addTexturePath(substring2, textureCount, (String)ex);
            }
        }
        return;
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid texture file name: ");
        sb.append(file.getName());
        sb.append(", failed with error ");
        sb.append(ex);
        ICLog.i("ERROR", sb.toString());
    }
    
    public void addTexturePath(final String s, final int n, final String s2) throws JSONException {
        JSONObject jsonObject;
        if (this.textureData.has(s)) {
            jsonObject = this.textureData.getJSONObject(s);
        }
        else {
            jsonObject = new JSONObject();
            jsonObject.put("textures", (Object)new JSONArray());
        }
        JSONArray optJSONArray;
        if ((optJSONArray = jsonObject.optJSONArray("textures")) == null) {
            optJSONArray = new JSONArray();
            optJSONArray.put(0, (Object)jsonObject.getString("textures"));
        }
        optJSONArray.put(n, (Object)s2);
        jsonObject.put("textures", (Object)optJSONArray);
        this.textureData.put(s, (Object)jsonObject);
    }
    
    public int getTextureCount(final String s) throws JSONException {
        if (this.textureData.has(s)) {
            return this.textureData.getJSONObject(s).optJSONArray("textures").length();
        }
        return 0;
    }
    
    public String getTextureName(String optString, final int n) {
        if (this.textureData.has(optString)) {
            final JSONObject optJSONObject = this.textureData.optJSONObject(optString);
            if (optJSONObject != null) {
                final JSONArray optJSONArray = optJSONObject.optJSONArray("textures");
                if (optJSONArray != null) {
                    return optJSONArray.optString(n, (String)null);
                }
                optString = optJSONObject.optString("textures");
                if (optString != null) {
                    return optString;
                }
            }
        }
        return null;
    }
}
