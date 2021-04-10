package org.mineprogramming.horizon.innercore.model;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;

public class VersionManager
{
    private static String VERSIONS_FILE;
    private final JSONObject versions;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("mods/versions.json");
        VersionManager.VERSIONS_FILE = sb.toString();
        if (!FileTools.exists(VersionManager.VERSIONS_FILE)) {
            try {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(FileTools.DIR_WORK);
                sb2.append("mods/");
                FileTools.mkdirs(sb2.toString());
                FileTools.writeJSON(VersionManager.VERSIONS_FILE, new JSONObject());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public VersionManager() {
        try {
            this.versions = FileTools.readJSON(VersionManager.VERSIONS_FILE);
        }
        catch (IOException | JSONException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public String getDirectory(final String s) {
        final JSONObject optJSONObject = this.versions.optJSONObject(s);
        if (optJSONObject != null) {
            return optJSONObject.optString("directory", (String)null);
        }
        return null;
    }
    
    public String getIdByDirectory(final String s) {
        final JSONArray names = this.versions.names();
        if (names == null) {
            return null;
        }
        for (int i = 0; i < names.length(); ++i) {
            try {
                final String string = names.getString(i);
                if (this.versions.getJSONObject(string).optString("directory").equals(s)) {
                    return string;
                }
            }
            catch (JSONException ex) {
                Logger.debug("MODS_MANAGER", "Unable to read mod info, versions JSON might be corrupt");
            }
        }
        return null;
    }
    
    public int getVersion(final String s) {
        final JSONObject optJSONObject = this.versions.optJSONObject(s);
        if (optJSONObject != null) {
            return optJSONObject.optInt("version");
        }
        return 0;
    }
    
    public boolean isInstalled(final String s) {
        return this.versions.optJSONObject(s) != null;
    }
    
    public void putVersion(final String s, final String s2, final int n) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("directory", (Object)s2);
            jsonObject.put("version", n);
            this.versions.put(s, (Object)jsonObject);
            FileTools.writeJSON(VersionManager.VERSIONS_FILE, this.versions);
        }
        catch (IOException | JSONException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    public void removeVersion(final String s) {
        try {
            this.versions.remove(s);
            FileTools.writeJSON(VersionManager.VERSIONS_FILE, this.versions);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
