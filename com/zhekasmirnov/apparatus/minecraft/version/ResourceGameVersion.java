package com.zhekasmirnov.apparatus.minecraft.version;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class ResourceGameVersion
{
    private final int maxVersion;
    private final int minVersion;
    private final int targetVersion;
    
    public ResourceGameVersion() {
        this(-1, -1, MinecraftVersions.getCurrent().getCode());
    }
    
    public ResourceGameVersion(final int minVersion, final int maxVersion, final int targetVersion) {
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.targetVersion = targetVersion;
    }
    
    public ResourceGameVersion(final File file) {
        this(readJsonFile(file));
    }
    
    public ResourceGameVersion(final JSONObject jsonObject) {
        if (jsonObject == null) {
            this.minVersion = -1;
            this.maxVersion = -1;
            this.targetVersion = MinecraftVersions.getCurrent().getCode();
            return;
        }
        final int optInt = jsonObject.optInt("minGameVersion", -1);
        final int optInt2 = jsonObject.optInt("maxGameVersion", -1);
        final int optInt3 = jsonObject.optInt("targetGameVersion", -1);
        if (optInt == -1 && optInt2 == -1 && optInt3 == -1) {
            this.minVersion = -1;
            this.maxVersion = -1;
            this.targetVersion = MinecraftVersions.getCurrent().getCode();
            return;
        }
        if (optInt == -1 && optInt2 == -1) {
            this.targetVersion = optInt3;
            this.maxVersion = optInt3;
            this.minVersion = optInt3;
            return;
        }
        int targetVersion;
        if ((targetVersion = optInt3) != -1) {
            int min = optInt3;
            if (optInt2 != -1) {
                min = Math.min(optInt2, optInt3);
            }
            targetVersion = min;
            if (optInt != -1) {
                targetVersion = Math.max(optInt, min);
            }
        }
        this.minVersion = optInt;
        this.maxVersion = optInt2;
        if (targetVersion == -1) {
            targetVersion = Math.max(optInt, optInt2);
        }
        this.targetVersion = targetVersion;
    }
    
    private static JSONObject readJsonFile(final File file) {
        try {
            return FileUtils.readJSON(file);
        }
        catch (IOException | JSONException ex) {
            return new JSONObject();
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final ResourceGameVersion resourceGameVersion = (ResourceGameVersion)o;
        return this.minVersion == resourceGameVersion.minVersion && this.maxVersion == resourceGameVersion.maxVersion && this.targetVersion == resourceGameVersion.targetVersion;
    }
    
    public int getMaxVersion() {
        return this.maxVersion;
    }
    
    public int getMinVersion() {
        return this.minVersion;
    }
    
    public int getTargetVersion() {
        return this.targetVersion;
    }
    
    public boolean isCompatible() {
        return this.isCompatible(MinecraftVersions.getCurrent());
    }
    
    public boolean isCompatible(final MinecraftVersion minecraftVersion) {
        final int code = minecraftVersion.getCode();
        return (this.minVersion == -1 || code >= this.minVersion) && (this.maxVersion == -1 || code <= this.maxVersion);
    }
    
    public boolean isCompatibleWithAnyVersion() {
        return this.minVersion == -1 && this.maxVersion == -1;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ResourceVersion{minVersion=");
        sb.append(this.minVersion);
        sb.append(", maxVersion=");
        sb.append(this.maxVersion);
        sb.append(", targetVersion=");
        sb.append(this.targetVersion);
        sb.append('}');
        return sb.toString();
    }
}
