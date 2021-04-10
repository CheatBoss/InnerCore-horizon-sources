package org.mineprogramming.horizon.innercore.model;

import android.net.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import java.util.*;

public abstract class ItemSource
{
    protected static Uri missingIcon;
    private List<Item> items;
    private boolean loadedLastNotified;
    private ModSourceListener modSourceListener;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_PACK);
        sb.append("assets/innercore/ui/missing_mod_icon.png");
        ItemSource.missingIcon = Uri.fromFile(new File(sb.toString()));
    }
    
    public ItemSource() {
        this.loadedLastNotified = true;
        this.items = new ArrayList<Item>();
    }
    
    private void notifyChange() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onChange();
        }
    }
    
    protected void addItem(final Item item) {
        this.items.add(item);
        this.notifyChange();
    }
    
    protected void clearItems() {
        this.items.clear();
        this.notifyChange();
    }
    
    public boolean contains(final Item item) {
        return this.items.contains(item);
    }
    
    public Item get(final int n) {
        return this.items.get(n);
    }
    
    public int getItemCount() {
        return this.items.size();
    }
    
    protected void notifyLoadFailed() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadFailed();
        }
    }
    
    protected void notifyLoadInProgress() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadInProgress();
        }
    }
    
    protected void notifyLoadLast() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadLast();
            return;
        }
        this.loadedLastNotified = false;
    }
    
    public void requestMore() {
    }
    
    public void retryLoad() {
    }
    
    public void setOnChangeListener(final ModSourceListener modSourceListener) {
        this.modSourceListener = modSourceListener;
        if (!this.loadedLastNotified) {
            this.loadedLastNotified = true;
            modSourceListener.onLoadLast();
        }
    }
    
    public void updateList() {
    }
    
    public interface ModSourceListener
    {
        void onChange();
        
        void onLoadFailed();
        
        void onLoadInProgress();
        
        void onLoadLast();
    }
}
