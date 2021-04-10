package org.mineprogramming.horizon.innercore.model;

import org.json.*;

public class ModDependency
{
    private final int id;
    private final String title;
    private final int version;
    
    public ModDependency(final int id, final String title, final int version) {
        this.id = id;
        this.title = title;
        this.version = version;
    }
    
    public ModDependency(final JSONObject jsonObject) {
        this.id = jsonObject.optInt("id");
        this.title = jsonObject.optString("title");
        this.version = jsonObject.optInt("version");
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getVersion() {
        return this.version;
    }
}
