package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import org.json.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.resource.*;

public class ShaderUniformsListResource implements RuntimeResourceHandler
{
    public final String asset;
    public final String name;
    private HashMap<String, HashMap<String, String>> uniformSets;
    private JSONObject uniformsListJson;
    
    public ShaderUniformsListResource(final String asset, final String name) {
        this.uniformSets = new HashMap<String, HashMap<String, String>>();
        this.uniformsListJson = null;
        this.asset = asset;
        this.name = name;
    }
    
    public boolean addUniform(final String s, final String s2, final String s3) {
        HashMap<String, String> hashMap;
        if ((hashMap = this.uniformSets.get(s)) == null) {
            hashMap = new HashMap<String, String>();
            this.uniformSets.put(s, hashMap);
        }
        if (hashMap.containsKey(s2)) {
            hashMap.put(s2, s3);
            return false;
        }
        hashMap.put(s2, s3);
        return true;
    }
    
    public String getResourceName() {
        return this.name;
    }
    
    public String getResourcePath() {
        return this.asset;
    }
    
    public void handle(final RuntimeResource runtimeResource) {
        if (this.uniformsListJson == null) {
            return;
        }
        for (final String s : this.uniformSets.keySet()) {
            final JSONArray optJSONArray = this.uniformsListJson.optJSONArray(s);
            JSONArray jsonArray;
            if ((jsonArray = optJSONArray) == null) {
                jsonArray = optJSONArray;
                try {
                    final JSONArray jsonArray2 = jsonArray = new JSONArray();
                    this.uniformsListJson.put(s, (Object)jsonArray2);
                    jsonArray = jsonArray2;
                }
                catch (JSONException ex2) {}
            }
            final HashMap<String, String> hashMap = this.uniformSets.get(s);
            for (final String s2 : hashMap.keySet()) {
                try {
                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Name", (Object)s2);
                    jsonObject.put("Type", (Object)hashMap.get(s2));
                    jsonArray.put((Object)jsonObject);
                }
                catch (JSONException ex3) {}
            }
        }
        try {
            FileUtils.writeJSON(runtimeResource.getFile(), this.uniformsListJson);
        }
        catch (IOException ex) {
            throw new RuntimeException("unexpected exception inside MaterialProcessor.handle()", ex);
        }
    }
    
    public void initializeJson(final ResourceManager resourceManager) {
        try {
            this.uniformsListJson = new JSONObject(FileUtils.readStringFromAsset(resourceManager.getAssets(), this.asset));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }
}
