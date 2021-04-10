package org.mineprogramming.horizon.innercore.model;

import android.net.*;
import java.util.*;
import org.json.*;
import android.text.*;

public abstract class Item
{
    private int author;
    private String authorName;
    private String descriptionFull;
    private String descriptionShort;
    private Uri icon;
    private int id;
    private boolean installed;
    private boolean networkAdapted;
    private boolean optimized;
    private List<Tag> tags;
    private String title;
    private int versionCode;
    private String versionName;
    
    public Item() {
        this.tags = new ArrayList<Tag>();
    }
    
    public Item(final JSONObject jsonObject) {
        this.tags = new ArrayList<Tag>();
        this.setTitle(jsonObject.optString("title"));
        this.setDescriptionShort(jsonObject.optString("description"));
        this.setId(jsonObject.optInt("id"));
        final int optInt = jsonObject.optInt("horizon_optimized");
        final boolean b = false;
        this.setOptimized(optInt == 1);
        boolean networkAdapted = b;
        if (jsonObject.optInt("multiplayer") == 1) {
            networkAdapted = true;
        }
        this.setNetworkAdapted(networkAdapted);
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/img/");
        sb.append(jsonObject.optString("icon"));
        this.setIcon(Uri.parse(sb.toString()));
    }
    
    public int getAuthor() {
        return this.author;
    }
    
    public String getAuthorName() {
        return this.authorName;
    }
    
    public String getDescription() {
        return this.descriptionShort;
    }
    
    public String getDescriptionFull() {
        if (this.descriptionFull == null) {
            return this.descriptionShort;
        }
        return this.descriptionFull;
    }
    
    public Uri getIcon() {
        return this.icon;
    }
    
    public int getId() {
        return this.id;
    }
    
    public List<Tag> getTags() {
        return this.tags;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getVersionCode() {
        return this.versionCode;
    }
    
    public String getVersionName() {
        return this.versionName;
    }
    
    public boolean isInstalled() {
        return this.installed;
    }
    
    public boolean isNetworkAdapted() {
        return this.networkAdapted;
    }
    
    public boolean isOptimized() {
        return this.optimized;
    }
    
    public void setAuthor(final int author) {
        this.author = author;
    }
    
    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }
    
    public void setDescriptionFull(final String descriptionFull) {
        this.descriptionFull = descriptionFull;
    }
    
    public void setDescriptionShort(final String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }
    
    public void setIcon(final Uri icon) {
        this.icon = icon;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setInstalled(final boolean installed) {
        this.installed = installed;
    }
    
    public void setNetworkAdapted(final boolean networkAdapted) {
        this.networkAdapted = networkAdapted;
    }
    
    public void setOptimized(final boolean optimized) {
        this.optimized = optimized;
    }
    
    protected void setTags(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return;
        }
        this.tags.clear();
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.tags.add(new Tag(jsonArray.optString(i)));
        }
    }
    
    public void setTitle(final String title) {
        this.title = title;
        if (TextUtils.isEmpty((CharSequence)title)) {
            this.title = "Untitled";
        }
    }
    
    public void setVersionCode(final int versionCode) {
        this.versionCode = versionCode;
    }
    
    public void setVersionName(final String versionName) {
        this.versionName = versionName;
    }
    
    public void updateInfo(final JSONObject jsonObject) {
        this.setTitle(jsonObject.optString("title"));
        this.setDescriptionFull(jsonObject.optString("description_full"));
        this.setVersionCode(jsonObject.optInt("version"));
        this.setVersionName(jsonObject.optString("version_name"));
        this.setAuthor(jsonObject.optInt("author"));
        this.setAuthorName(jsonObject.optString("author_name"));
        this.setTags(jsonObject.optJSONArray("tags"));
    }
}
