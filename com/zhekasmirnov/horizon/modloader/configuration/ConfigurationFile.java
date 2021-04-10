package com.zhekasmirnov.horizon.modloader.configuration;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class ConfigurationFile extends AbstractJsonConfiguration
{
    public final File file;
    private JSONObject data;
    private boolean isReadOnly;
    
    public ConfigurationFile(final File file, final boolean isReadOnly) {
        this.file = file;
        this.isReadOnly = isReadOnly;
    }
    
    @Override
    public void refresh() {
        this.load();
    }
    
    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }
    
    @Override
    public void save() {
        try {
            FileUtils.writeJSON(this.file, this.data);
        }
        catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }
    
    @Override
    public void load() {
        this.data = new JSONObject();
        try {
            this.data = FileUtils.readJSON(this.file);
        }
        catch (IOException ignore) {
            ignore.printStackTrace();
        }
        catch (JSONException ignore2) {
            ignore2.printStackTrace();
        }
    }
    
    @Override
    protected JSONObject getData() {
        return this.data;
    }
}
