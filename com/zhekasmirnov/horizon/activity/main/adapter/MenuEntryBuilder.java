package com.zhekasmirnov.horizon.activity.main.adapter;

import com.zhekasmirnov.horizon.runtime.task.*;
import java.util.*;

public abstract class MenuEntryBuilder
{
    private static final Object lock;
    private TaskManager manager;
    private MenuHolder menu;
    private boolean isPrepared;
    
    public MenuEntryBuilder() {
        this.isPrepared = false;
    }
    
    public void attachTo(final TaskManager manager, final MenuHolder menu) {
        this.manager = manager;
        if (this.menu != menu) {
            this.menu = menu;
            this.manager.addTask(this.getPreparationTask());
        }
    }
    
    protected String getTaskDescription() {
        return null;
    }
    
    protected boolean prepare(final List<MenuEntry> entries) {
        return false;
    }
    
    protected Object getLock() {
        return MenuEntryBuilder.lock;
    }
    
    protected abstract String getEntryName();
    
    public Task getPreparationTask() {
        return new Task() {
            @Override
            public Object getLock() {
                return MenuEntryBuilder.this.getLock();
            }
            
            @Override
            public String getDescription() {
                return MenuEntryBuilder.this.getTaskDescription();
            }
            
            @Override
            public void run() {
                if (!MenuEntryBuilder.this.isPrepared) {
                    final List<MenuEntry> entries = new ArrayList<MenuEntry>();
                    if (MenuEntryBuilder.this.prepare(entries)) {
                        MenuEntryBuilder.this.isPrepared = true;
                        MenuEntryBuilder.this.onPrepared(entries);
                    }
                }
            }
        };
    }
    
    private void onPrepared(final List<MenuEntry> entries) {
        for (final MenuEntry entry : entries) {
            final String name = this.getEntryName();
            entry.attachTo(this.menu);
            this.menu.addEntry(name, entry, entry);
        }
    }
    
    static {
        lock = new Object();
    }
}
