package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class RecipeEntry
{
    public static final RecipeEntry noentry;
    public final int data;
    public final int id;
    
    static {
        noentry = new RecipeEntry(0, 0);
    }
    
    public RecipeEntry(final int id, final int data) {
        this.id = id;
        this.data = data;
    }
    
    public RecipeEntry(final Slot slot) {
        this(slot.getId(), slot.getData());
    }
    
    public static long getCodeByItem(final int n, final int n2) {
        return n2 << 16 | n;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof RecipeEntry) {
            return this.isMatching((RecipeEntry)o);
        }
        return super.equals(o);
    }
    
    public long getCode() {
        return getCodeByItem(this.id, this.data);
    }
    
    public Character getMask() {
        return (char)this.id;
    }
    
    @Override
    public int hashCode() {
        return (int)this.getCode();
    }
    
    public boolean isMatching(final RecipeEntry recipeEntry) {
        return recipeEntry.id == this.id && (this.data == -1 || recipeEntry.data == -1 || recipeEntry.data == this.data);
    }
    
    public boolean isMatching(final AbstractSlot abstractSlot) {
        return abstractSlot.getId() == this.id && (this.data == -1 || abstractSlot.getData() == -1 || abstractSlot.getData() == this.data);
    }
}
