package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import org.json.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.nbt.*;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.*;

public class NativeItemInstanceExtra
{
    private static final int saverId;
    private JSONObject customData;
    private boolean customDataLoaded;
    private boolean isFinalizable;
    private long ptr;
    
    static {
        saverId = ObjectSaverRegistry.registerSaver("_item_instance_extra_data", new ObjectSaver() {
            @Override
            public Object read(final ScriptableObject scriptableObject) {
                if (scriptableObject == null) {
                    return null;
                }
                final NativeItemInstanceExtra nativeItemInstanceExtra = new NativeItemInstanceExtra();
                final NativeArray nativeArrayProperty = ScriptableObjectHelper.getNativeArrayProperty(scriptableObject, "eId", null);
                final NativeArray nativeArrayProperty2 = ScriptableObjectHelper.getNativeArrayProperty(scriptableObject, "eLvl", null);
                if (nativeArrayProperty != null && nativeArrayProperty2 != null) {
                    final Object[] array = nativeArrayProperty.toArray();
                    final Object[] array2 = nativeArrayProperty2.toArray();
                    for (int min = Math.min(array.length, array2.length), i = 0; i < min; ++i) {
                        nativeItemInstanceExtra.addEnchant((int)array[i], (int)array2[i]);
                    }
                }
                final String stringProperty = ScriptableObjectHelper.getStringProperty(scriptableObject, "$", null);
                if (stringProperty != null) {
                    nativeItemInstanceExtra.setAllCustomData(stringProperty);
                }
                final String stringProperty2 = ScriptableObjectHelper.getStringProperty(scriptableObject, "N", null);
                if (stringProperty2 != null) {
                    nativeItemInstanceExtra.setCustomName(stringProperty2);
                }
                if (nativeItemInstanceExtra.isEmpty()) {
                    return null;
                }
                return nativeItemInstanceExtra;
            }
            
            @Override
            public ScriptableObject save(final Object o) {
                if (o != null && o instanceof NativeItemInstanceExtra) {
                    final NativeItemInstanceExtra nativeItemInstanceExtra = (NativeItemInstanceExtra)o;
                    if (!nativeItemInstanceExtra.isEmpty()) {
                        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
                        if (nativeItemInstanceExtra.isEnchanted()) {
                            final int[][] rawEnchants = nativeItemInstanceExtra.getRawEnchants();
                            final Object[] array = new Object[rawEnchants[0].length];
                            final Object[] array2 = new Object[rawEnchants[1].length];
                            for (int i = 0; i < array.length; ++i) {
                                array[i] = rawEnchants[0][i];
                                array2[i] = rawEnchants[1][i];
                            }
                            empty.put("eId", (Scriptable)empty, (Object)ScriptableObjectHelper.createArray(array));
                            empty.put("eLvl", (Scriptable)empty, (Object)ScriptableObjectHelper.createArray(array2));
                        }
                        final String allCustomData = nativeItemInstanceExtra.getAllCustomData();
                        if (allCustomData != null) {
                            empty.put("$", (Scriptable)empty, (Object)allCustomData);
                        }
                        final String customName = nativeItemInstanceExtra.getCustomName();
                        if (customName != null) {
                            empty.put("N", (Scriptable)empty, (Object)customName);
                        }
                        return empty;
                    }
                }
                return null;
            }
        });
    }
    
    public NativeItemInstanceExtra() {
        this.isFinalizable = false;
        this.customData = null;
        this.customDataLoaded = false;
        ObjectSaverRegistry.registerObject(this, NativeItemInstanceExtra.saverId);
        this.ptr = constructNew();
        this.isFinalizable = nativeIsFinalizable(this.ptr);
    }
    
    public NativeItemInstanceExtra(final long ptr) {
        this.isFinalizable = false;
        this.customData = null;
        this.customDataLoaded = false;
        ObjectSaverRegistry.registerObject(this, NativeItemInstanceExtra.saverId);
        if (ptr != 0L) {
            this.ptr = ptr;
        }
        else {
            this.ptr = constructNew();
        }
        this.isFinalizable = nativeIsFinalizable(this.ptr);
    }
    
    public NativeItemInstanceExtra(final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.isFinalizable = false;
        this.customData = null;
        this.customDataLoaded = false;
        ObjectSaverRegistry.registerObject(this, NativeItemInstanceExtra.saverId);
        final long valueOrNullPtr = getValueOrNullPtr(nativeItemInstanceExtra);
        if (valueOrNullPtr != 0L) {
            this.ptr = constructClone(valueOrNullPtr);
        }
        else {
            this.ptr = constructNew();
        }
        this.isFinalizable = nativeIsFinalizable(this.ptr);
    }
    
    private void applyCustomDataJSON() {
        if (this.customData != null) {
            this.setAllCustomData(this.customData.toString());
            return;
        }
        this.setAllCustomData(null);
    }
    
    public static NativeItemInstanceExtra cloneExtra(final NativeItemInstanceExtra nativeItemInstanceExtra) {
        final long valueOrNullPtr = getValueOrNullPtr(nativeItemInstanceExtra);
        if (valueOrNullPtr != 0L) {
            return new NativeItemInstanceExtra(constructClone(valueOrNullPtr));
        }
        return null;
    }
    
    public static native long constructClone(final long p0);
    
    private static native long constructNew();
    
    private static native void finalizeNative(final long p0);
    
    public static NativeItemInstanceExtra fromJson(final JSONObject jsonObject) {
        if (jsonObject != null && jsonObject.length() != 0) {
            final NativeItemInstanceExtra nativeItemInstanceExtra = new NativeItemInstanceExtra();
            final JSONArray optJSONArray = jsonObject.optJSONArray("enchants");
            if (optJSONArray != null) {
                for (int i = 0; i < optJSONArray.length(); ++i) {
                    final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    nativeItemInstanceExtra.addEnchant(optJSONObject.optInt("id"), optJSONObject.optInt("l"));
                }
            }
            final String optString = jsonObject.optString("name");
            if (optString != null) {
                nativeItemInstanceExtra.setCustomName(optString);
            }
            final String optString2 = jsonObject.optString("data");
            if (optString2 != null) {
                nativeItemInstanceExtra.setAllCustomData(optString2);
            }
            return nativeItemInstanceExtra;
        }
        return null;
    }
    
    private JSONObject getCustomDataJSON() {
        if (!this.customDataLoaded) {
            final String allCustomData = this.getAllCustomData();
            if (allCustomData != null) {
                try {
                    this.customData = new JSONObject(allCustomData);
                }
                catch (JSONException ex) {
                    this.customData = null;
                }
            }
            this.customDataLoaded = true;
        }
        return this.customData;
    }
    
    public static NativeItemInstanceExtra getExtraOrNull(final long n) {
        if (n != 0L) {
            return new NativeItemInstanceExtra(n);
        }
        return null;
    }
    
    private static native long getExtraPtr(final long p0);
    
    private JSONObject getOrCreateCustomDataJSON() {
        JSONObject customDataJSON;
        if ((customDataJSON = this.getCustomDataJSON()) == null) {
            customDataJSON = new JSONObject();
            this.customData = customDataJSON;
        }
        return customDataJSON;
    }
    
    public static long getValueOrNullPtr(final NativeItemInstanceExtra nativeItemInstanceExtra) {
        if (nativeItemInstanceExtra != null) {
            return nativeItemInstanceExtra.getValue();
        }
        return 0L;
    }
    
    public static void initSaverId() {
    }
    
    private static native void nativeAddEnchant(final long p0, final int p1, final int p2);
    
    private static native String nativeEnchantToString(final int p0, final int p1);
    
    private static native void nativeGetAllEnchants(final long p0, final int[] p1, final int[] p2);
    
    private static native long nativeGetCompoundTag(final long p0);
    
    private static native String nativeGetCustomName(final long p0);
    
    private static native int nativeGetEnchantCount(final long p0);
    
    private static native int nativeGetEnchantLevel(final long p0, final int p1);
    
    private static native String nativeGetModExtra(final long p0);
    
    private static native boolean nativeIsEnchanted(final long p0);
    
    private static native boolean nativeIsFinalizable(final long p0);
    
    private static native void nativeRemoveAllEnchants(final long p0);
    
    private static native void nativeRemoveEnchant(final long p0, final int p1);
    
    private static native void nativeSetCompoundTag(final long p0, final long p1);
    
    private static native void nativeSetCustomName(final long p0, final String p1);
    
    private static native void nativeSetModExtra(final long p0, final String p1);
    
    private NativeItemInstanceExtra putObject(final String s, final Object o) {
        final JSONObject orCreateCustomDataJSON = this.getOrCreateCustomDataJSON();
        try {
            orCreateCustomDataJSON.put(s, o);
            this.applyCustomDataJSON();
            return this;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return this;
        }
    }
    
    public static NativeItemInstanceExtra unwrapObject(final Object o) {
        Object unwrap = o;
        if (o instanceof Wrapper) {
            unwrap = ((Wrapper)o).unwrap();
        }
        if (unwrap instanceof NativeItemInstanceExtra) {
            return (NativeItemInstanceExtra)unwrap;
        }
        return null;
    }
    
    public static long unwrapValue(final Object o) {
        if (o == null) {
            return 0L;
        }
        Object unwrap = o;
        if (o instanceof Wrapper) {
            unwrap = ((Wrapper)o).unwrap();
        }
        if (unwrap instanceof NativeItemInstanceExtra) {
            return ((NativeItemInstanceExtra)unwrap).getValue();
        }
        if (unwrap instanceof Number) {
            return ((Number)unwrap).intValue();
        }
        return 0L;
    }
    
    public void addEnchant(final int n, final int n2) {
        nativeAddEnchant(this.ptr, n, n2);
    }
    
    public void applyTo(final Scriptable scriptable) {
        if (scriptable != null) {
            scriptable.put("extra", scriptable, (Object)this.getValue());
        }
    }
    
    public JSONObject asJson() {
        final boolean empty = this.isEmpty();
        JSONObject jsonObject = null;
        if (empty) {
            return null;
        }
        final JSONObject jsonObject2 = new JSONObject();
        try {
            final int[][] rawEnchants = this.getRawEnchants();
            int i = 0;
            final int[] array = rawEnchants[0];
            final int[] array2 = rawEnchants[1];
            if (array.length > 0) {
                final JSONArray jsonArray = new JSONArray();
                while (i < array.length) {
                    final JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("id", array[i]);
                    jsonObject3.put("l", array2[i]);
                    jsonArray.put((Object)jsonObject3);
                    ++i;
                }
                jsonObject2.put("enchants", (Object)jsonArray);
            }
            final String customName = this.getCustomName();
            if (customName != null && customName.length() > 0) {
                jsonObject2.put("name", (Object)customName);
            }
            final String allCustomData = this.getAllCustomData();
            if (allCustomData != null && allCustomData.length() > 0) {
                jsonObject2.put("data", (Object)allCustomData);
            }
        }
        catch (JSONException ex) {}
        if (jsonObject2.length() > 0) {
            jsonObject = jsonObject2;
        }
        return jsonObject;
    }
    
    public NativeItemInstanceExtra copy() {
        return new NativeItemInstanceExtra(this);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.isFinalizable) {
            finalizeNative(this.ptr);
        }
    }
    
    public String getAllCustomData() {
        return nativeGetModExtra(this.ptr);
    }
    
    public String getAllEnchantNames() {
        final StringBuilder sb = new StringBuilder();
        final int[][] rawEnchants = this.getRawEnchants();
        int i = 0;
        final int[] array = rawEnchants[0];
        final int[] array2 = rawEnchants[1];
        while (i < array.length) {
            if (array2[i] > 0) {
                sb.append(this.getEnchantName(array[i], array2[i]));
                sb.append("\n");
            }
            ++i;
        }
        return sb.toString();
    }
    
    public boolean getBoolean(final String s) {
        return this.getBoolean(s, false);
    }
    
    public boolean getBoolean(final String s, final boolean b) {
        final JSONObject customDataJSON = this.getCustomDataJSON();
        if (customDataJSON != null) {
            return customDataJSON.optBoolean(s, b);
        }
        return b;
    }
    
    public NativeCompoundTag getCompoundTag() {
        final long nativeGetCompoundTag = nativeGetCompoundTag(this.ptr);
        if (nativeGetCompoundTag != 0L) {
            return new NativeCompoundTag(nativeGetCompoundTag);
        }
        return null;
    }
    
    public String getCustomName() {
        return nativeGetCustomName(this.ptr);
    }
    
    public int getEnchantCount() {
        return nativeGetEnchantCount(this.ptr);
    }
    
    public int getEnchantLevel(final int n) {
        return nativeGetEnchantLevel(this.ptr, n);
    }
    
    public String getEnchantName(final int n) {
        return this.getEnchantName(n, this.getEnchantLevel(n));
    }
    
    public String getEnchantName(final int n, final int n2) {
        return nativeEnchantToString(n, n2);
    }
    
    public ScriptableObject getEnchants() {
        final int[][] rawEnchants = this.getRawEnchants();
        int i = 0;
        final int[] array = rawEnchants[0];
        final int[] array2 = rawEnchants[1];
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        while (i < array.length) {
            if (array2[i] > 0) {
                empty.put(array[i], (Scriptable)empty, (Object)array2[i]);
            }
            ++i;
        }
        return empty;
    }
    
    public double getFloat(final String s) {
        return this.getFloat(s, 0.0);
    }
    
    public double getFloat(final String s, final double n) {
        final JSONObject customDataJSON = this.getCustomDataJSON();
        if (customDataJSON != null) {
            return customDataJSON.optDouble(s, n);
        }
        return n;
    }
    
    public int getInt(final String s) {
        return this.getInt(s, 0);
    }
    
    public int getInt(final String s, final int n) {
        final JSONObject customDataJSON = this.getCustomDataJSON();
        if (customDataJSON != null) {
            return customDataJSON.optInt(s, n);
        }
        return n;
    }
    
    public long getLong(final String s) {
        return this.getLong(s, 0L);
    }
    
    public long getLong(final String s, final long n) {
        final JSONObject customDataJSON = this.getCustomDataJSON();
        if (customDataJSON != null) {
            return customDataJSON.optLong(s, n);
        }
        return n;
    }
    
    public int[][] getRawEnchants() {
        ScriptableObjectHelper.createEmpty();
        final int enchantCount = this.getEnchantCount();
        final int[] array = new int[enchantCount];
        final int[] array2 = new int[enchantCount];
        nativeGetAllEnchants(this.ptr, array, array2);
        return new int[][] { array, array2 };
    }
    
    public Object getSerializable(final String s) {
        try {
            return ScriptableSerializer.scriptableFromJson(ScriptableSerializer.stringToJson(this.getString(s)));
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public String getString(final String s) {
        return this.getString(s, null);
    }
    
    public String getString(final String s, final String s2) {
        final JSONObject customDataJSON = this.getCustomDataJSON();
        if (customDataJSON != null) {
            return customDataJSON.optString(s, s2);
        }
        return s2;
    }
    
    public long getValue() {
        return getExtraPtr(this.ptr);
    }
    
    public boolean isEmpty() {
        return this.getValue() == 0L;
    }
    
    public boolean isEnchanted() {
        return nativeIsEnchanted(this.ptr);
    }
    
    public boolean isFinalizableInstance() {
        return this.isFinalizable;
    }
    
    public NativeItemInstanceExtra putBoolean(final String s, final boolean b) {
        return this.putObject(s, b);
    }
    
    public NativeItemInstanceExtra putFloat(final String s, final double n) {
        return this.putObject(s, n);
    }
    
    public NativeItemInstanceExtra putInt(final String s, final int n) {
        return this.putObject(s, n);
    }
    
    public NativeItemInstanceExtra putLong(final String s, final long n) {
        return this.putObject(s, n);
    }
    
    public NativeItemInstanceExtra putSerializable(final String s, final Object o) {
        return this.putString(s, ScriptableSerializer.jsonToString(ScriptableSerializer.scriptableToJson(o, null)));
    }
    
    public NativeItemInstanceExtra putString(final String s, final String s2) {
        return this.putObject(s, s2);
    }
    
    public void removeAllEnchants() {
        nativeRemoveAllEnchants(this.ptr);
    }
    
    public void removeCustomData() {
        this.customData = null;
        this.customDataLoaded = false;
        this.setAllCustomData(null);
    }
    
    public void removeEnchant(final int n) {
        nativeRemoveEnchant(this.ptr, n);
    }
    
    public void setAllCustomData(final String s) {
        nativeSetModExtra(this.ptr, s);
    }
    
    public void setCompoundTag(final NativeCompoundTag nativeCompoundTag) {
        final long ptr = this.ptr;
        long pointer;
        if (nativeCompoundTag != null) {
            pointer = nativeCompoundTag.pointer;
        }
        else {
            pointer = 0L;
        }
        nativeSetCompoundTag(ptr, pointer);
    }
    
    public void setCustomName(final String s) {
        nativeSetCustomName(this.ptr, s);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ItemExtra{json=");
        sb.append(this.asJson());
        sb.append("}");
        return sb.toString();
    }
}
