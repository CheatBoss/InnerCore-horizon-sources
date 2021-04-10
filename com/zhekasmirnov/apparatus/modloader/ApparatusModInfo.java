package com.zhekasmirnov.apparatus.modloader;

import java.util.*;
import com.zhekasmirnov.innercore.mod.build.*;
import java.io.*;

public class ApparatusModInfo
{
    private final Map<String, Object> properties;
    
    public ApparatusModInfo() {
        this.properties = new HashMap<String, Object>();
    }
    
    public boolean getBoolean(final String s) {
        return this.getBoolean(s, false);
    }
    
    public boolean getBoolean(final String s, final boolean b) {
        return this.getProperty(s, Boolean.class, b);
    }
    
    public float getFloat(final String s) {
        return this.getFloat(s, 0.0f);
    }
    
    public float getFloat(final String s, final float n) {
        return this.getProperty(s, Float.class, n);
    }
    
    public int getInt(final String s) {
        return this.getInt(s, 0);
    }
    
    public int getInt(final String s, final int n) {
        return this.getProperty(s, Integer.class, n);
    }
    
    public <T> T getProperty(final String s, final Class<T> clazz, final T t) {
        final Object value = this.properties.get(s);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        return t;
    }
    
    public String getString(final String s) {
        return this.getString(s, null);
    }
    
    public String getString(final String s, final String s2) {
        return this.getProperty(s, String.class, s2);
    }
    
    public void pullLegacyModProperties(final Mod mod) {
        this.putProperty("multiplayer_supported", mod.isConfiguredForMultiplayer());
        this.putProperty("client_only", mod.isClientOnly());
        this.putProperty("name", mod.getMultiplayerName());
        this.putProperty("displayed_name", mod.getName());
        this.putProperty("version", mod.getMultiplayerVersion());
        this.putProperty("description", mod.getInfoProperty("description"));
        this.putProperty("developer", mod.getInfoProperty("author"));
        this.putProperty("icon_path", new File(mod.dir, "icon.png").getAbsolutePath());
        this.putProperty("icon_name", mod.getGuiIcon());
        this.putProperty("directory_root", mod.dir);
    }
    
    public void putProperty(final String s, final Object o) {
        this.properties.put(s, o);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ApparatusModInfo");
        sb.append(this.properties);
        return sb.toString();
    }
}
