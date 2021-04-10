package com.zhekasmirnov.horizon.modloader.configuration;

import org.json.*;

public abstract class AbstractJsonConfiguration extends Configuration
{
    protected abstract JSONObject getData();
    
    @Override
    public <T> T get(final String key, final Class<T> tClass) {
        try {
            return (T)this.get(this.getData(), key);
        }
        catch (ClassCastException e) {
            return null;
        }
    }
    
    @Override
    public Object get(final String key) {
        return this.get(this.getData(), key);
    }
    
    private Object get(final JSONObject json, final String key) {
        final int dot = key.indexOf(46);
        if (dot == -1) {
            return json.opt(key);
        }
        final Object child = json.opt(key.substring(0, dot));
        if (child instanceof JSONObject) {
            return this.get((JSONObject)child, key.substring(dot + 1));
        }
        return null;
    }
    
    private boolean set(final JSONObject json, final String key, final Object value) {
        if (json == null || this.isReadOnly()) {
            return false;
        }
        final int dot = key.indexOf(46);
        if (dot != -1) {
            final String name = key.substring(0, dot);
            Object child = json.opt(name);
            if (child == null) {
                child = new JSONObject();
                try {
                    json.put(name, child);
                }
                catch (JSONException ex) {}
            }
            return child instanceof JSONObject && this.set((JSONObject)child, key.substring(dot + 1), value);
        }
        try {
            json.put(key, value);
            return true;
        }
        catch (JSONException e) {
            return false;
        }
    }
    
    @Override
    public boolean set(final String key, final Object value) {
        return this.set(this.getData(), key, value);
    }
    
    private Object delete(final JSONObject json, final String key) {
        if (json == null || this.isReadOnly()) {
            return null;
        }
        final int dot = key.indexOf(46);
        if (dot == -1) {
            return json.remove(key);
        }
        final String name = key.substring(0, dot);
        final Object child = json.opt(name);
        if (child instanceof JSONObject) {
            return this.delete((JSONObject)child, key.substring(dot + 1));
        }
        return null;
    }
    
    @Override
    public boolean isContainer(final String key) {
        return this.get(this.getData(), key) instanceof JSONObject;
    }
    
    @Override
    public Object delete(final String key) {
        return this.delete(this.getData(), key);
    }
    
    @Override
    public Configuration getChild(final String key) {
        final Object value = this.get(this.getData(), key);
        if (value instanceof JSONObject) {
            return new ChildJsonConfiguration(this, (JSONObject)value);
        }
        return null;
    }
    
    private static class ChildJsonConfiguration extends AbstractJsonConfiguration
    {
        final AbstractJsonConfiguration parent;
        final JSONObject data;
        final boolean isReadOnly;
        
        private ChildJsonConfiguration(final AbstractJsonConfiguration parent, final JSONObject data) {
            this.parent = parent;
            this.data = data;
            this.isReadOnly = parent.isReadOnly();
        }
        
        @Override
        protected JSONObject getData() {
            return this.data;
        }
        
        @Override
        public void refresh() {
            this.parent.refresh();
        }
        
        @Override
        public boolean isReadOnly() {
            return this.isReadOnly;
        }
        
        @Override
        public void save() {
            this.parent.save();
        }
        
        @Override
        public void load() {
            this.parent.load();
        }
    }
}
