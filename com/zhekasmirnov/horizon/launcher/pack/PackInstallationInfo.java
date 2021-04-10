package com.zhekasmirnov.horizon.launcher.pack;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;
import java.util.*;

public class PackInstallationInfo
{
    public static final String KEY_UUID = "uuid";
    public static final String KEY_INTERNAL_ID = "internalId";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_CUSTOM_NAME = "customName";
    private static final Object LOCK;
    public final File file;
    private final HashMap<String, String> valueMap;
    
    public PackInstallationInfo(final File file) {
        this.valueMap = new HashMap<String, String>();
        this.file = file;
    }
    
    private void updateMapContents() {
        if (this.file.exists() && this.file.isFile()) {
            try {
                synchronized (PackInstallationInfo.LOCK) {
                    final JSONObject json = FileUtils.readJSON(this.file);
                    for (final String jsonKey : new JsonIterator<String>(json)) {
                        final String value = json.optString(jsonKey);
                        if (value != null) {
                            this.valueMap.put(jsonKey, value);
                        }
                    }
                }
            }
            catch (IOException ex) {}
            catch (JSONException ex2) {}
        }
    }
    
    private void updateFileContents() {
        try {
            synchronized (PackInstallationInfo.LOCK) {
                JSONObject json = new JSONObject();
                try {
                    json = FileUtils.readJSON(this.file);
                }
                catch (IOException ex) {}
                catch (JSONException ex2) {}
                for (final String key : this.valueMap.keySet()) {
                    final Object value = this.valueMap.get(key);
                    if (value != null) {
                        json.put(key, (Object)value.toString());
                    }
                    else {
                        json.remove(key);
                    }
                }
                FileUtils.writeJSON(this.file, json);
            }
        }
        catch (IOException ex3) {}
        catch (JSONException ex4) {}
    }
    
    public String getValue(final String key) {
        this.updateMapContents();
        return this.valueMap.get(key);
    }
    
    public void setValue(final String key, final String value) {
        this.updateMapContents();
        this.valueMap.put(key, value);
        this.updateFileContents();
    }
    
    static {
        LOCK = new Object();
    }
}
