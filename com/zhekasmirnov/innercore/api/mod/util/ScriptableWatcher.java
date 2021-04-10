package com.zhekasmirnov.innercore.api.mod.util;

import org.mozilla.javascript.*;
import java.util.*;

public class ScriptableWatcher
{
    private ArrayList<Object> cached;
    private int checkPosition;
    private boolean isDirty;
    public ScriptableObject object;
    
    public ScriptableWatcher(final ScriptableObject object) {
        this.isDirty = false;
        this.cached = new ArrayList<Object>();
        this.object = object;
        this.refresh();
        this.validate();
    }
    
    private void updateCached(final ScriptableObject scriptableObject) {
        if (this.checkPosition > 128) {
            return;
        }
        if (scriptableObject == null) {
            this.updateSymbol("null");
            return;
        }
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final Object value = scriptableObject.get(o);
            this.updateSymbol(o);
            if (value instanceof ScriptableObject) {
                this.updateSymbol("{");
                this.updateCached((ScriptableObject)value);
                this.updateSymbol("}");
            }
            else {
                this.updateSymbol(value);
            }
        }
    }
    
    private void updateSymbol(final Object o) {
        Object o2 = o;
        if (o == null) {
            o2 = "null";
        }
        if (this.cached.size() <= this.checkPosition) {
            this.cached.add(o2);
            this.isDirty = true;
        }
        else if (!this.cached.get(this.checkPosition).equals(o2)) {
            this.cached.set(this.checkPosition, o2);
            this.isDirty = true;
        }
        ++this.checkPosition;
    }
    
    public void invalidate() {
        this.isDirty = true;
    }
    
    public boolean isDirty() {
        return this.isDirty;
    }
    
    public void refresh() {
        this.checkPosition = 0;
        this.updateCached(this.object);
    }
    
    public void setTarget(final ScriptableObject object) {
        this.object = object;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Object> iterator = this.cached.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            sb.append(" ");
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("{");
        sb2.append((Object)sb);
        sb2.append("}");
        return sb2.toString();
    }
    
    public void validate() {
        this.isDirty = false;
    }
}
