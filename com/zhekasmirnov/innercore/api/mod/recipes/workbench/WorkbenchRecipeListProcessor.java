package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.apparatus.api.container.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import org.json.*;

public class WorkbenchRecipeListProcessor
{
    private static final String PREFIX = "wbRecipeSlot";
    private int lastSlotCount;
    private OnRecipeSelectRequestedListener listener;
    private float maxX;
    private int maximumRecipesShowed;
    private float minX;
    private float minY;
    private int slotsPerLine;
    private final ScriptableObject target;
    
    public WorkbenchRecipeListProcessor(final ScriptableObject target) {
        this.listener = null;
        this.minX = 60.0f;
        this.minY = 40.0f;
        this.maxX = 960.0f;
        this.slotsPerLine = 6;
        this.maximumRecipesShowed = 250;
        this.lastSlotCount = 0;
        this.target = target;
    }
    
    private void applySlotPosition(final ScriptableObject scriptableObject, final int n) {
        final int slotsPerLine = this.slotsPerLine;
        final int n2 = n / this.slotsPerLine;
        final float n3 = (this.maxX - this.minX) / this.slotsPerLine;
        final float n4 = (float)(n % slotsPerLine);
        final float minX = this.minX;
        final float n5 = (float)n2;
        final float minY = this.minY;
        scriptableObject.put("x", (Scriptable)scriptableObject, (Object)(n4 * n3 + minX));
        scriptableObject.put("y", (Scriptable)scriptableObject, (Object)(n5 * n3 + minY));
        scriptableObject.put("size", (Scriptable)scriptableObject, (Object)n3);
    }
    
    private void assureSlotAt(final ItemContainer itemContainer, final int n, final boolean b, final long n2) {
        final ScriptableFunctionImpl scriptableFunctionImpl = new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                if (WorkbenchRecipeListProcessor.this.listener != null) {
                    WorkbenchRecipeListProcessor.this.listener.onSelectRequested(itemContainer, n2);
                }
                return null;
            }
        };
        final String slotName = this.getSlotName(n);
        if (this.target.has(slotName, (Scriptable)this.target)) {
            final Object value = this.target.get(slotName, (Scriptable)this.target);
            if (value instanceof ScriptableObject) {
                final ScriptableObject scriptableObject = (ScriptableObject)value;
                scriptableObject.put("darken", (Scriptable)scriptableObject, (Object)b);
                scriptableObject.put("onClick", (Scriptable)scriptableObject, (Object)scriptableFunctionImpl);
                scriptableObject.put("onLongClick", (Scriptable)scriptableObject, (Object)scriptableFunctionImpl);
                return;
            }
        }
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("type", (Scriptable)empty, (Object)"slot");
        empty.put("bitmap", (Scriptable)empty, (Object)"style:slot");
        empty.put("onClick", (Scriptable)empty, (Object)scriptableFunctionImpl);
        empty.put("onLongClick", (Scriptable)empty, (Object)scriptableFunctionImpl);
        empty.put("_index", (Scriptable)empty, (Object)n);
        empty.put("visual", (Scriptable)empty, (Object)true);
        this.applySlotPosition(empty, n);
        this.target.put(slotName, (Scriptable)this.target, (Object)empty);
    }
    
    private void assureSlotCount(final ItemContainer itemContainer, final int lastSlotCount) {
        for (int i = lastSlotCount; i < this.lastSlotCount; ++i) {
            this.removeSlotAt(i);
        }
        for (int j = 0; j < lastSlotCount; ++j) {
            this.assureSlotAt(itemContainer, j, true, 0L);
        }
        this.lastSlotCount = lastSlotCount;
    }
    
    private String getSlotName(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("wbRecipeSlot");
        sb.append(n);
        return sb.toString();
    }
    
    private void refreshSlotAt(final int n, final boolean b) {
        final String slotName = this.getSlotName(n);
        if (this.target.has(slotName, (Scriptable)this.target)) {
            final Object value = this.target.get(slotName, (Scriptable)this.target);
            if (value instanceof ScriptableObject) {
                final ScriptableObject scriptableObject = (ScriptableObject)value;
                scriptableObject.put("darken", (Scriptable)scriptableObject, (Object)b);
                this.applySlotPosition(scriptableObject, n);
            }
        }
    }
    
    private void removeSlotAt(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("wbRecipeSlot");
        sb.append(n);
        final String string = sb.toString();
        if (this.target.has(string, (Scriptable)this.target)) {
            this.target.put(string, (Scriptable)this.target, (Object)null);
        }
    }
    
    public int processRecipeListPacket(final ItemContainer itemContainer, final JSONObject jsonObject) {
        if (itemContainer.isServer) {
            throw new IllegalArgumentException("requires client container");
        }
        final JSONArray optJSONArray = jsonObject.optJSONArray("recipes");
        if (optJSONArray != null) {
            this.assureSlotCount(itemContainer, optJSONArray.length());
            for (int i = 0; i < optJSONArray.length(); ++i) {
                final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    final long optLong = optJSONObject.optLong("id");
                    final JSONObject optJSONObject2 = optJSONObject.optJSONObject("result");
                    if (optJSONObject2 != null && optLong != 0L) {
                        final ItemStack parse = ItemStack.parse(optJSONObject2);
                        if (parse != null) {
                            parse.id = IdConversionMap.serverToLocal(parse.id);
                            final StringBuilder sb = new StringBuilder();
                            sb.append("wbRecipeSlot");
                            sb.append(i);
                            itemContainer.setSlot(sb.toString(), parse.id, parse.count, parse.data, parse.extra);
                            this.assureSlotAt(itemContainer, i, optJSONObject.optBoolean("d"), optLong);
                            continue;
                        }
                    }
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("wbRecipeSlot");
                sb2.append(i);
                itemContainer.setSlot(sb2.toString(), 0, 0, 0);
            }
            return optJSONArray.length();
        }
        this.assureSlotCount(itemContainer, 0);
        return 0;
    }
    
    public void setListener(final OnRecipeSelectRequestedListener listener) {
        this.listener = listener;
    }
    
    public interface OnRecipeSelectRequestedListener
    {
        void onSelectRequested(final ItemContainer p0, final long p1);
    }
}
