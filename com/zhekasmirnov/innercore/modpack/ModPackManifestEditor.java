package com.zhekasmirnov.innercore.modpack;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class ModPackManifestEditor
{
    private final File file;
    private final JSONObject json;
    private final ModPackManifest manifest;
    
    ModPackManifestEditor(final ModPackManifest manifest, final File file) throws IOException, JSONException {
        this.manifest = manifest;
        this.file = file;
        if (file == null) {
            throw new IllegalStateException("Manifest wasn't loaded from a file");
        }
        if (file.isFile()) {
            this.json = FileUtils.readJSON(file);
            return;
        }
        this.json = new JSONObject();
    }
    
    public ModPackManifestEditor addIfMissing(final String s, final Object o) throws JSONException {
        final String optString = this.json.optString(s, (String)null);
        if (optString == null || optString.isEmpty()) {
            this.json.put(s, o);
        }
        return this;
    }
    
    public void commit() throws IOException, JSONException {
        FileUtils.writeJSON(this.file, this.json);
        this.manifest.loadFile(this.file);
    }
    
    public ModPackManifestEditor put(final String s, final Object o) throws JSONException {
        this.json.put(s, o);
        return this;
    }
}
