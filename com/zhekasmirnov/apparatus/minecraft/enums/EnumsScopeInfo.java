package com.zhekasmirnov.apparatus.minecraft.enums;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import java.util.*;

public class EnumsScopeInfo
{
    private static final Map<String, EnumsScopeInfo> scopeInfoMap;
    private MinecraftVersion jsScopeVersion;
    private String typeName;
    
    static {
        scopeInfoMap = new HashMap<String, EnumsScopeInfo>();
        try {
            final JSONObject assetAsJSON = FileTools.getAssetAsJSON("innercore/enum/enum-scopes.json");
            final Iterator keys = assetAsJSON.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                final JSONObject optJSONObject = assetAsJSON.optJSONObject(s);
                if (optJSONObject != null) {
                    EnumsScopeInfo.scopeInfoMap.put(s, new EnumsScopeInfo(optJSONObject));
                }
            }
        }
        catch (JSONException ex) {
            ICLog.e("ERROR", "EnumsScopeInfo failed to get scope info from assets", (Throwable)ex);
        }
    }
    
    public EnumsScopeInfo() {
    }
    
    public EnumsScopeInfo(final JSONObject jsonObject) {
        if (jsonObject != null) {
            final String optString = jsonObject.optString("typename");
            if (optString != null) {
                this.setTypeName(optString);
            }
            MinecraftVersion jsScopeVersion = MinecraftVersions.getVersionByCode(jsonObject.optInt("jsScopeVersion"));
            if (jsScopeVersion == null) {
                jsScopeVersion = MinecraftVersions.getCurrent();
            }
            this.setJsScopeVersion(jsScopeVersion);
        }
    }
    
    public static Set<String> getAllScopesWithInfo() {
        return EnumsScopeInfo.scopeInfoMap.keySet();
    }
    
    public static EnumsScopeInfo getForScope(final String s) {
        return EnumsScopeInfo.scopeInfoMap.get(s);
    }
    
    public MinecraftVersion getJsScopeVersion() {
        return this.jsScopeVersion;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public void setJsScopeVersion(final MinecraftVersion jsScopeVersion) {
        this.jsScopeVersion = jsScopeVersion;
    }
    
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }
}
