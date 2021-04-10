package com.zhekasmirnov.innercore.modpack.installation;

import java.util.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.json.*;
import java.io.*;

public abstract class ModpackInstallationSource
{
    public abstract Enumeration<Entry> entries();
    
    public abstract int getEntryCount();
    
    public abstract String getManifestContent() throws IOException;
    
    public ModPackManifest getTempManifest() throws IOException, JSONException {
        final ModPackManifest modPackManifest = new ModPackManifest();
        modPackManifest.loadJson(new JSONObject(this.getManifestContent()));
        return modPackManifest;
    }
    
    public interface Entry
    {
        InputStream getInputStream() throws IOException;
        
        String getName();
    }
}
