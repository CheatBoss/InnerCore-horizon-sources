package org.mineprogramming.horizon.innercore.model;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.json.*;

public class ModPreferences
{
    private File file;
    private JSONObject json;
    
    public ModPreferences(final File file) {
        this.file = new File(file, "preferences.json");
        try {
            this.json = FileTools.readJSON(this.file.getAbsolutePath());
        }
        catch (IOException | JSONException ex) {
            this.json = new JSONObject();
        }
    }
    
    public int getIcmodsId() {
        return this.json.optInt("icmods_id", 0);
    }
    
    public int getIcmodsVersion() {
        return this.json.optInt("icmods_version", 0);
    }
    
    public void setIcmodsData(final int n, final int n2) {
        try {
            this.json.put("icmods_id", n);
            this.json.put("icmods_version", n2);
            FileTools.writeJSON(this.file.getAbsolutePath(), this.json);
        }
        catch (JSONException | IOException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
}
