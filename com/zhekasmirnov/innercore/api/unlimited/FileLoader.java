package com.zhekasmirnov.innercore.api.unlimited;

import org.json.*;
import java.io.*;
import com.zhekasmirnov.innercore.utils.*;

public class FileLoader
{
    private JSONObject data;
    private File file;
    private JSONObject uids;
    
    public FileLoader(final File file) {
        this.file = file;
        try {
            this.data = FileTools.readJSON(file.getAbsolutePath());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            this.data = new JSONObject();
        }
        this.uids = this.data.optJSONObject("id");
        if (this.uids != null) {
            IDRegistry.fromJson(this.uids);
        }
    }
    
    public void save() {
        try {
            this.data.put("id", (Object)IDRegistry.toJson());
            FileTools.writeJSON(this.file.getAbsolutePath(), this.data);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
