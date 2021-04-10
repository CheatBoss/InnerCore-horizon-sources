package com.zhekasmirnov.apparatus.api.container;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.json.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.apparatus.mcpe.*;

public class ItemContainerSlot implements AbstractSlot
{
    private ItemContainer container;
    public int count;
    public int data;
    public NativeItemInstanceExtra extra;
    public int id;
    private Boolean isSavingEnabled;
    private String name;
    
    public ItemContainerSlot() {
        this(0, 0, 0, null);
    }
    
    public ItemContainerSlot(final int n, final int n2, final int n3) {
        this(n, n2, n3, null);
    }
    
    public ItemContainerSlot(final int id, final int count, final int data, final NativeItemInstanceExtra extra) {
        this.isSavingEnabled = null;
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
    }
    
    public ItemContainerSlot(final JSONObject jsonObject, final boolean b) {
        int n;
        if (b) {
            n = IdConversionMap.serverToLocal(jsonObject.optInt("id", 0));
        }
        else {
            n = jsonObject.optInt("id", 0);
        }
        this(n, jsonObject.optInt("count", 0), jsonObject.optInt("data", 0), NativeItemInstanceExtra.fromJson(jsonObject.optJSONObject("extra")));
    }
    
    public ItemContainerSlot(final ScriptableObject scriptableObject) {
        this(ScriptableObjectHelper.getIntProperty(scriptableObject, "id", 0), ScriptableObjectHelper.getIntProperty(scriptableObject, "count", 0), ScriptableObjectHelper.getIntProperty(scriptableObject, "data", 0), NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(scriptableObject, "extra", null)));
    }
    
    public JSONObject asJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put("count", this.count);
            jsonObject.put("data", this.data);
            if (this.extra != null) {
                final JSONObject json = this.extra.asJson();
                if (json != null) {
                    jsonObject.put("extra", (Object)json);
                }
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    public ScriptableObject asScriptable() {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("id", (Scriptable)empty, (Object)this.id);
        empty.put("count", (Scriptable)empty, (Object)this.count);
        empty.put("data", (Scriptable)empty, (Object)this.data);
        empty.put("extra", (Scriptable)empty, (Object)this.extra);
        return empty;
    }
    
    public void clear() {
        this.data = 0;
        this.count = 0;
        this.id = 0;
        this.extra = null;
        this.markDirty();
    }
    
    public void dropAt(final NativeBlockSource nativeBlockSource, final float n, final float n2, final float n3) {
        nativeBlockSource.spawnDroppedItem(n, n2, n3, this.id, this.count, this.data, this.extra);
        this.clear();
    }
    
    public ItemContainer getContainer() {
        return this.container;
    }
    
    @Override
    public int getCount() {
        return this.count;
    }
    
    @Override
    public int getData() {
        return this.data;
    }
    
    @Override
    public NativeItemInstanceExtra getExtra() {
        return this.extra;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isEmpty() {
        return this.id == 0 && this.count == 0 && this.data == 0 && this.extra == null;
    }
    
    public boolean isSavingEnabled() {
        if (this.isSavingEnabled != null) {
            return this.isSavingEnabled;
        }
        return this.container != null && this.container.isGlobalSlotSavingEnabled();
    }
    
    public void markDirty() {
        if (this.container != null) {
            this.container.markSlotDirty(this.name);
        }
    }
    
    public void resetSavingEnabled() {
        this.isSavingEnabled = null;
    }
    
    @Override
    public void set(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.setSlot(n, n2, n3, nativeItemInstanceExtra);
    }
    
    void setContainer(final ItemContainer container, final String name) {
        this.name = name;
        this.container = container;
    }
    
    public void setSavingEnabled(final boolean b) {
        this.isSavingEnabled = b;
    }
    
    public void setSlot(final int n, final int n2, final int n3) {
        this.setSlot(n, n2, n3, null);
    }
    
    public void setSlot(final int id, final int count, final int data, final NativeItemInstanceExtra extra) {
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
        this.markDirty();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ItemContainerSlot{id=");
        sb.append(this.id);
        sb.append(", count=");
        sb.append(this.count);
        sb.append(", data=");
        sb.append(this.data);
        sb.append(", name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public void validate() {
        if (this.id == 0 || this.count <= 0) {
            this.clear();
        }
    }
}
