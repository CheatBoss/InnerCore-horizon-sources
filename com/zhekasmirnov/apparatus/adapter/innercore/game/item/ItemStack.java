package com.zhekasmirnov.apparatus.adapter.innercore.game.item;

import com.zhekasmirnov.innercore.api.mod.*;
import org.json.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.*;

public class ItemStack
{
    public int count;
    public int data;
    public NativeItemInstanceExtra extra;
    public int id;
    
    public ItemStack() {
        this(0, 0, 0, null);
    }
    
    public ItemStack(final int n, final int n2, final int n3) {
        this(n, n2, n3, null);
    }
    
    public ItemStack(final int id, final int count, final int data, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = NativeItemInstanceExtra.cloneExtra(nativeItemInstanceExtra);
    }
    
    public ItemStack(final ItemStack itemStack) {
        this(itemStack.id, itemStack.count, itemStack.data, itemStack.extra);
    }
    
    public ItemStack(final NativeItemInstance nativeItemInstance) {
        this(nativeItemInstance.id, nativeItemInstance.count, nativeItemInstance.data, nativeItemInstance.extra);
    }
    
    public ItemStack(final ScriptableObject scriptableObject) {
        this(ScriptableObjectHelper.getIntProperty(scriptableObject, "id", 0), ScriptableObjectHelper.getIntProperty(scriptableObject, "count", 0), ScriptableObjectHelper.getIntProperty(scriptableObject, "data", 0), NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(scriptableObject, "extra", null)));
    }
    
    public static ItemStack fromPtr(final long n) {
        if (n != 0L) {
            return new ItemStack(new NativeItemInstance(n));
        }
        return new ItemStack();
    }
    
    public static ItemStack parse(Object unwrap) {
        while (unwrap instanceof Wrapper) {
            unwrap = ((Wrapper)unwrap).unwrap();
        }
        if (unwrap != null) {
            if ("undefined".equals(unwrap.toString().toLowerCase())) {
                return null;
            }
            if (unwrap instanceof ScriptableObject) {
                return new ItemStack((ScriptableObject)unwrap);
            }
            if (unwrap instanceof ItemStack) {
                return new ItemStack((ItemStack)unwrap);
            }
            if (!(unwrap instanceof JSONObject)) {
                if (!(unwrap instanceof CharSequence)) {
                    if (unwrap instanceof NativeItemInstance) {
                        return new ItemStack((NativeItemInstance)unwrap);
                    }
                    if (unwrap instanceof Long) {
                        return fromPtr((long)unwrap);
                    }
                    return null;
                }
            }
            try {
                if (unwrap instanceof JSONObject) {
                    unwrap = unwrap;
                }
                else {
                    unwrap = new JSONObject(unwrap.toString());
                }
                return new ItemStack(((JSONObject)unwrap).optInt("id", 0), ((JSONObject)unwrap).optInt("count", 0), ((JSONObject)unwrap).optInt("data", 0), NativeItemInstanceExtra.fromJson(((JSONObject)unwrap).optJSONObject("extra")));
            }
            catch (JSONException ex) {
                return null;
            }
        }
        return null;
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
    
    public long getExtraPtr() {
        return NativeItemInstanceExtra.getValueOrNullPtr(this.extra);
    }
    
    public NativeItemModel getItemModel() {
        return NativeItemModel.getForWithFallback(this.id, this.data);
    }
    
    public String getItemName() {
        return NativeItem.getNameForId(this.id, this.data, this.getExtraPtr());
    }
    
    public int getMaxDamage() {
        return NativeItem.getMaxDamageForId(this.id, this.data);
    }
    
    public int getMaxStackSize() {
        return NativeItem.getMaxStackForId(this.id, this.data);
    }
    
    public boolean isEmpty() {
        return this.id == 0 && this.count == 0 && this.data == 0 && this.extra == null;
    }
    
    public boolean isGlint() {
        return NativeItem.isGlintItemInstance(this.id, this.data, this.extra);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ItemStack{id=");
        sb.append(this.id);
        sb.append(", count=");
        sb.append(this.count);
        sb.append(", data=");
        sb.append(this.data);
        String string;
        if (this.extra != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(", extra=");
            sb2.append(this.extra);
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        sb.append('}');
        return sb.toString();
    }
}
