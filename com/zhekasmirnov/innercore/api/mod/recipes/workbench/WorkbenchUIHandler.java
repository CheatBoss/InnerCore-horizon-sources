package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;

public class WorkbenchUIHandler
{
    private static final String PREFIX = "wbRecipeSlot";
    private int currentIndex;
    private int lastSlotCount;
    private float maxX;
    private int maximumRecipesShowed;
    private float minX;
    private float minY;
    private String prefix;
    private HashMap<Integer, WorkbenchRecipe> recipeByIndex;
    private IRefreshListener refreshListener;
    private ISelectionListener selectionListener;
    private int slotsPerLine;
    private final ScriptableObject target;
    private final Container targetCon;
    private final WorkbenchField targetField;
    
    public WorkbenchUIHandler(final ScriptableObject target, final Container targetCon, final WorkbenchField targetField) {
        this.minX = 60.0f;
        this.minY = 40.0f;
        this.maxX = 960.0f;
        this.slotsPerLine = 6;
        this.maximumRecipesShowed = 250;
        this.currentIndex = -1;
        this.lastSlotCount = 0;
        this.recipeByIndex = new HashMap<Integer, WorkbenchRecipe>();
        this.prefix = "";
        this.target = target;
        this.targetCon = targetCon;
        this.targetField = targetField;
    }
    
    private void _deselectCurrentRecipe() {
        this.currentIndex = -1;
        if (this.targetField != null) {
            WorkbenchRecipeRegistry.cleanupWorkbenchField(this.targetField, NativeAPI.getPlayer());
        }
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
    
    private void assureSlotAt(final int n, final boolean b) {
        final String slotName = this.getSlotName(n);
        if (this.target.has(slotName, (Scriptable)this.target)) {
            final Object value = this.target.get(slotName, (Scriptable)this.target);
            if (value instanceof ScriptableObject) {
                final ScriptableObject scriptableObject = (ScriptableObject)value;
                scriptableObject.put("darken", (Scriptable)scriptableObject, (Object)b);
                return;
            }
        }
        final ScriptableFunctionImpl scriptableFunctionImpl = new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                if (WorkbenchUIHandler.this.currentIndex == n) {
                    return null;
                }
                MainThreadQueue.serverThread.enqueue(new Runnable() {
                    @Override
                    public void run() {
                        WorkbenchUIHandler.this._deselectCurrentRecipe();
                    }
                });
                MainThreadQueue.serverThread.enqueueDelayed(1, new Runnable() {
                    @Override
                    public void run() {
                        WorkbenchUIHandler.this.currentIndex = n;
                        final WorkbenchRecipe access$200 = WorkbenchUIHandler.this.getRecipeByIndex(n);
                        if (access$200 != null) {
                            access$200.putIntoTheField(WorkbenchUIHandler.this.targetField, NativeAPI.getPlayer());
                            if (WorkbenchUIHandler.this.selectionListener != null) {
                                WorkbenchUIHandler.this.selectionListener.onRecipeSelected(access$200);
                            }
                        }
                        else {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("cannot select recipe: failed to find binding for index ");
                            sb.append(n);
                            ICLog.i("ERROR", sb.toString());
                        }
                    }
                });
                return null;
            }
        };
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
    
    private void assureSlotCount(final int lastSlotCount) {
        for (int i = lastSlotCount; i < this.lastSlotCount; ++i) {
            this.removeSlotAt(i);
        }
        for (int j = 0; j < lastSlotCount; ++j) {
            this.assureSlotAt(j, true);
        }
        this.lastSlotCount = lastSlotCount;
    }
    
    private WorkbenchRecipe getRecipeByIndex(final int n) {
        return this.recipeByIndex.get(n);
    }
    
    private String getSlotName(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("wbRecipeSlot");
        sb.append(n);
        return sb.toString();
    }
    
    private void putRecipeForIndex(final int n, final WorkbenchRecipe workbenchRecipe) {
        this.recipeByIndex.put(n, workbenchRecipe);
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
    
    public void deselectCurrentRecipe() {
        MainThreadQueue.serverThread.enqueue(new Runnable() {
            @Override
            public void run() {
                WorkbenchUIHandler.this._deselectCurrentRecipe();
            }
        });
    }
    
    public int refresh() {
        final WorkbenchRecipeRegistry.UIRecipeLists availableRecipesForPlayerInventory = WorkbenchRecipeRegistry.getAvailableRecipesForPlayerInventory(NativeAPI.getPlayer(), this.targetField, this.prefix);
        this.recipeByIndex.clear();
        this.assureSlotCount(Math.min(this.maximumRecipesShowed, availableRecipesForPlayerInventory.recipes.size()));
        this.currentIndex = -1;
        int n = 0;
        for (final WorkbenchRecipe workbenchRecipe : availableRecipesForPlayerInventory.recipes) {
            this.putRecipeForIndex(n, workbenchRecipe);
            final int possibleCount = availableRecipesForPlayerInventory.possibleCount;
            int count = 1;
            final boolean b = n >= possibleCount;
            this.refreshSlotAt(n, b);
            final Container targetCon = this.targetCon;
            final String slotName = this.getSlotName(n);
            final int id = workbenchRecipe.id;
            if (!b) {
                count = workbenchRecipe.count;
            }
            targetCon.setSlot(slotName, id, count, workbenchRecipe.data);
            ++n;
            if (n > this.maximumRecipesShowed) {
                return n;
            }
        }
        return n;
    }
    
    public void refreshAsync() {
        MainThreadQueue.serverThread.enqueueDelayed(1, new Runnable() {
            @Override
            public void run() {
                if (WorkbenchUIHandler.this.refreshListener != null) {
                    WorkbenchUIHandler.this.refreshListener.onRefreshStarted();
                }
                final int refresh = WorkbenchUIHandler.this.refresh();
                if (WorkbenchUIHandler.this.refreshListener != null) {
                    WorkbenchUIHandler.this.refreshListener.onRefreshCompleted(refresh);
                }
            }
        });
    }
    
    public void setMaximumRecipesToShow(final int maximumRecipesShowed) {
        this.maximumRecipesShowed = maximumRecipesShowed;
    }
    
    public void setOnRefreshListener(final IRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
    
    public void setOnSelectionListener(final ISelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
    
    public interface IRefreshListener
    {
        void onRefreshCompleted(final int p0);
        
        void onRefreshStarted();
    }
    
    public interface ISelectionListener
    {
        void onRecipeSelected(final WorkbenchRecipe p0);
    }
}
