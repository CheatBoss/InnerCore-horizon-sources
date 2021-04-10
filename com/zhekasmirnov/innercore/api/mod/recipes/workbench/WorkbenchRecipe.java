package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.json.*;
import android.util.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public abstract class WorkbenchRecipe
{
    private static long recipeNextUid;
    private Function callback;
    protected int count;
    protected int data;
    protected HashMap<Character, RecipeEntry> entries;
    protected NativeItemInstanceExtra extra;
    protected int id;
    boolean isValid;
    private String prefix;
    private final long recipeUid;
    
    static {
        WorkbenchRecipe.recipeNextUid = 1L;
    }
    
    public WorkbenchRecipe(final int id, final int count, final int data, final NativeItemInstanceExtra extra) {
        this.entries = new HashMap<Character, RecipeEntry>();
        this.isValid = true;
        this.recipeUid = nextRecipeUid();
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
    }
    
    public WorkbenchRecipe(final AddonRecipeParser addonRecipeParser, final AddonRecipeParser.ParsedRecipe parsedRecipe) {
        this.entries = new HashMap<Character, RecipeEntry>();
        this.isValid = true;
        this.recipeUid = nextRecipeUid();
        final JSONObject optJSONObject = parsedRecipe.getContents().optJSONObject("result");
        if (optJSONObject != null) {
            final Pair<Integer, Integer> idAndDataForItemJson = addonRecipeParser.getIdAndDataForItemJson(optJSONObject, -1);
            if (idAndDataForItemJson != null) {
                this.id = (int)idAndDataForItemJson.first;
                this.data = (int)idAndDataForItemJson.second;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("cannot find vanilla numeric ID for ");
                sb.append(optJSONObject);
                Logger.debug(sb.toString());
                this.isValid = false;
            }
            this.count = optJSONObject.optInt("count", 1);
            this.extra = null;
        }
    }
    
    private static long nextRecipeUid() {
        synchronized (WorkbenchRecipe.class) {
            final long recipeNextUid = WorkbenchRecipe.recipeNextUid;
            WorkbenchRecipe.recipeNextUid = recipeNextUid + 1L;
            return recipeNextUid;
        }
    }
    
    public abstract void addVariants(final ArrayList<WorkbenchRecipe> p0);
    
    public Function getCallback() {
        return this.callback;
    }
    
    public RecipeEntry getEntry(final char c) {
        if (this.entries.containsKey(c)) {
            return this.entries.get(c);
        }
        return RecipeEntry.noentry;
    }
    
    public ArrayList<Long> getEntryCodes() {
        final ArrayList<Long> list = new ArrayList<Long>();
        final Iterator<RecipeEntry> iterator = this.getEntryCollection().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getCode());
        }
        return list;
    }
    
    public Collection<RecipeEntry> getEntryCollection() {
        return this.entries.values();
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public abstract String getRecipeMask();
    
    public long getRecipeUid() {
        return this.recipeUid;
    }
    
    public ItemInstance getResult() {
        return new ItemInstance(this.id, this.count, this.data, this.extra);
    }
    
    public abstract RecipeEntry[] getSortedEntries();
    
    public abstract boolean isMatchingField(final WorkbenchField p0);
    
    public boolean isMatchingPrefix(final String s) {
        if (s != null && !s.isEmpty() && !s.equals("undefined")) {
            return s.contains(this.prefix);
        }
        return this.prefix == null || this.prefix.isEmpty();
    }
    
    public boolean isMatchingResult(final int n, final int n2, final int n3) {
        return this.id == n && this.count == n2 && this.data == n3;
    }
    
    public boolean isPossibleForInventory(final HashMap<Long, Integer> hashMap) {
        final Iterator<RecipeEntry> iterator = this.getEntryCollection().iterator();
        while (iterator.hasNext()) {
            if (!hashMap.containsKey(iterator.next().getCode())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isValid() {
        return this.isValid && this.id != 0;
    }
    
    public ItemInstance provideRecipe(final WorkbenchField workbenchField) {
        return this.provideRecipeForPlayer(workbenchField, NativeAPI.getPlayer());
    }
    
    public ItemInstance provideRecipeForPlayer(final WorkbenchField workbenchField, final long n) {
        final ItemInstance result = this.getResult();
        final WorkbenchFieldAPI workbenchFieldAPI = new WorkbenchFieldAPI(workbenchField);
        Callback.invokeCallback("CraftRecipePreProvided", this, workbenchField, n);
        final Function callback = this.getCallback();
        if (callback != null) {
            final Scriptable parentScope = callback.getParentScope();
            callback.call(Compiler.assureContextForCurrentThread(), parentScope, parentScope, new Object[] { Context.javaToJS((Object)workbenchFieldAPI, parentScope), workbenchField.asScriptableField(), result, n });
        }
        else {
            for (int i = 0; i < 9; ++i) {
                workbenchFieldAPI.decreaseFieldSlot(i);
            }
        }
        Callback.invokeCallback("CraftRecipeProvided", this, workbenchField, workbenchFieldAPI.isPrevented(), n);
        if (workbenchFieldAPI.isPrevented()) {
            return null;
        }
        return result;
    }
    
    public void putIntoTheField(final WorkbenchField workbenchField, long code) {
        final InventoryPool inventoryPool = new InventoryPool(code);
        final RecipeEntry[] sortedEntries = this.getSortedEntries();
        for (int length = sortedEntries.length, i = 0; i < length; ++i) {
            final RecipeEntry recipeEntry = sortedEntries[i];
            if (recipeEntry.id != 0) {
                inventoryPool.addRecipeEntry(recipeEntry);
            }
        }
        inventoryPool.pullFromInventory();
        final HashMap<Long, ArrayList<Pair>> hashMap = new HashMap<Long, ArrayList<Pair>>();
        for (int length2 = sortedEntries.length, j = 0; j < length2; ++j) {
            final RecipeEntry recipeEntry2 = sortedEntries[j];
            if (recipeEntry2.id != 0) {
                code = recipeEntry2.getCode();
                if (!hashMap.containsKey(code)) {
                    final ArrayList<Pair> list = new ArrayList<Pair>();
                    for (int k = 0; k < sortedEntries.length; ++k) {
                        final RecipeEntry recipeEntry3 = sortedEntries[k];
                        if (recipeEntry2.id == recipeEntry3.id && recipeEntry2.data == recipeEntry3.data) {
                            list.add(new Pair((Object)k, (Object)recipeEntry2));
                        }
                    }
                    hashMap.put(code, list);
                }
            }
        }
        for (int l = 0; l < 9; ++l) {
            workbenchField.getFieldSlot(l).set(0, 0, 0, null);
        }
        final Iterator<Long> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            final ArrayList<Pair> list2 = hashMap.get(iterator.next());
            final InventoryPool.PoolEntrySet poolEntrySet = inventoryPool.getPoolEntrySet((RecipeEntry)list2.get(0).second);
            if (poolEntrySet != null && !poolEntrySet.isEmpty()) {
                final ArrayList<AbstractSlot> list3 = new ArrayList<AbstractSlot>();
                for (int n = 0; n < list2.size(); ++n) {
                    final int intValue = (int)list2.get(n).first;
                    if (intValue >= 0 && intValue < 9) {
                        list3.add(workbenchField.getFieldSlot(intValue));
                    }
                }
                poolEntrySet.spreadItems(list3);
            }
            else {
                for (final Pair pair : list2) {
                    final int intValue2 = (int)pair.first;
                    final RecipeEntry recipeEntry4 = (RecipeEntry)pair.second;
                    if (intValue2 > 8) {
                        continue;
                    }
                    final AbstractSlot fieldSlot = workbenchField.getFieldSlot(intValue2);
                    final int id = recipeEntry4.id;
                    int data;
                    if (recipeEntry4.data != -1) {
                        data = recipeEntry4.data;
                    }
                    else {
                        data = 0;
                    }
                    fieldSlot.set(id, 0, data, null);
                }
            }
        }
    }
    
    public void setCallback(final Function callback) {
        this.callback = callback;
    }
    
    public void setEntries(final HashMap<Character, RecipeEntry> entries) {
        this.entries = entries;
    }
    
    public void setPrefix(final String s) {
        String prefix = null;
        Label_0019: {
            if (s != null) {
                prefix = s;
                if (!s.equals("undefined")) {
                    break Label_0019;
                }
            }
            prefix = "";
        }
        this.prefix = prefix;
    }
}
