package com.zhekasmirnov.horizon.activity.main.adapter;

import android.view.*;

public abstract class MenuEntry implements MenuHolder.EntryBuilder, MenuHolder.EntryHandler
{
    private int layoutId;
    private MenuHolder.Entry entry;
    private MenuHolder menu;
    
    public MenuEntry() {
        this.layoutId = -1;
    }
    
    public MenuEntry(final int layoutId) {
        this.layoutId = -1;
        this.layoutId = layoutId;
    }
    
    @Override
    public View create(final ViewGroup group) {
        final int layoutId = this.getLayoutId();
        if (layoutId == -1) {
            throw new IllegalStateException("failed to create view for " + this + " layoutId is not specified");
        }
        return LayoutInflater.from(group.getContext()).inflate(layoutId, group, false);
    }
    
    protected int getLayoutId() {
        return this.layoutId;
    }
    
    @Override
    public void bind(final View view) {
    }
    
    @Override
    public void onRemoved() {
    }
    
    @Override
    public boolean isDraggable() {
        return true;
    }
    
    @Override
    public boolean isRemovable() {
        return true;
    }
    
    public void onClick(final View v) {
    }
    
    @Override
    public void onCreated(final MenuHolder.Entry entry) {
        this.entry = entry;
    }
    
    public MenuHolder.Entry getEntry() {
        return this.entry;
    }
    
    void attachTo(final MenuHolder menu) {
        this.menu = menu;
    }
    
    public MenuHolder getMenu() {
        return this.menu;
    }
    
    protected void destroy() {
        this.menu.removeEntry(this.entry);
    }
}
