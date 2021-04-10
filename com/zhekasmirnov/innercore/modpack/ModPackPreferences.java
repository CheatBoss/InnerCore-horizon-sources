package com.zhekasmirnov.innercore.modpack;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class ModPackPreferences
{
    private final File file;
    private JSONObject json;
    private final ModPack modPack;
    
    public ModPackPreferences(final ModPack modPack, final String s) {
        this.modPack = modPack;
        this.file = new File(modPack.getRootDirectory(), s);
    }
    
    private void reloadIfRequired() {
        if (this.json == null) {
            this.reload();
        }
    }
    
    public boolean getBoolean(final String s, final boolean b) {
        this.reloadIfRequired();
        return this.json.optBoolean(s, b);
    }
    
    public double getDouble(final String s, final double n) {
        this.reloadIfRequired();
        return this.json.optDouble(s, n);
    }
    
    public File getFile() {
        return this.file;
    }
    
    public int getInt(final String s, final int n) {
        this.reloadIfRequired();
        return this.json.optInt(s, n);
    }
    
    public long getLong(final String s, final long n) {
        this.reloadIfRequired();
        return this.json.optLong(s, n);
    }
    
    public ModPack getModPack() {
        return this.modPack;
    }
    
    public String getString(final String s, final String s2) {
        this.reloadIfRequired();
        return this.json.optString(s, s2);
    }
    
    public ModPackPreferences reload() {
        try {
            this.json = FileUtils.readJSON(this.file);
            return this;
        }
        catch (IOException | JSONException ex) {
            if (this.json == null) {
                this.json = new JSONObject();
            }
            return this;
        }
    }
    
    public ModPackPreferences save() {
        this.reloadIfRequired();
        try {
            FileUtils.writeJSON(this.file, this.json);
            return this;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }
    
    public ModPackPreferences setBoolean(final String s, final boolean b) {
        this.reloadIfRequired();
        try {
            this.json.put(s, b);
            return this;
        }
        catch (JSONException ex) {
            return this;
        }
    }
    
    public ModPackPreferences setDouble(final String s, final double n) {
        this.reloadIfRequired();
        try {
            this.json.put(s, n);
            return this;
        }
        catch (JSONException ex) {
            return this;
        }
    }
    
    public ModPackPreferences setInt(final String s, final int n) {
        this.reloadIfRequired();
        try {
            this.json.put(s, n);
            return this;
        }
        catch (JSONException ex) {
            return this;
        }
    }
    
    public ModPackPreferences setLong(final String s, final long n) {
        this.reloadIfRequired();
        try {
            this.json.put(s, n);
            return this;
        }
        catch (JSONException ex) {
            return this;
        }
    }
    
    public ModPackPreferences setString(final String s, final String s2) {
        this.reloadIfRequired();
        try {
            this.json.put(s, (Object)s2);
            return this;
        }
        catch (JSONException ex) {
            return this;
        }
    }
}
