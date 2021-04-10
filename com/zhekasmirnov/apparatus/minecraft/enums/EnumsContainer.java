package com.zhekasmirnov.apparatus.minecraft.enums;

import com.zhekasmirnov.apparatus.minecraft.version.*;
import org.json.*;
import java.util.*;

public class EnumsContainer
{
    private final Map<String, Scope> scopeMap;
    private final MinecraftVersion version;
    
    public EnumsContainer(final MinecraftVersion version) {
        this.scopeMap = new HashMap<String, Scope>();
        this.version = version;
    }
    
    public void addEnumsFromJson(final JSONObject jsonObject) {
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final JSONObject optJSONObject = jsonObject.optJSONObject(s);
            if (optJSONObject != null) {
                this.getOrAddScope(s).addEnumsFromJson(optJSONObject);
            }
        }
    }
    
    public Set<String> getAllScopeNames() {
        return this.scopeMap.keySet();
    }
    
    public Object getEnum(final String s, final String s2) {
        final Scope scope = this.scopeMap.get(s);
        if (scope != null) {
            return scope.getEnum(s2);
        }
        return null;
    }
    
    public String getKeyForEnum(final String s, final Object o) {
        final Scope scope = this.scopeMap.get(s);
        if (scope != null) {
            return scope.getKeyForEnum(o);
        }
        return null;
    }
    
    public Scope getOrAddScope(final String s) {
        Scope scope;
        if ((scope = this.scopeMap.get(s)) == null) {
            this.scopeMap.put(s, scope = new Scope());
        }
        return scope;
    }
    
    public Scope getScope(final String s) {
        return this.scopeMap.get(s);
    }
    
    public MinecraftVersion getVersion() {
        return this.version;
    }
    
    public static class Scope
    {
        private final Map<Object, String> inverseMap;
        private final Map<String, Object> map;
        
        public Scope() {
            this.map = new HashMap<String, Object>();
            this.inverseMap = new HashMap<Object, String>();
        }
        
        public void addEnumsFromJson(final JSONObject jsonObject) {
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                final Object opt = jsonObject.opt(s);
                if (opt != null) {
                    this.put(s, opt);
                }
            }
        }
        
        public Set<String> getAllEnumNames() {
            return this.map.keySet();
        }
        
        public Object getEnum(final String s) {
            return this.map.get(s);
        }
        
        public String getKeyForEnum(final Object o) {
            return this.inverseMap.get(o);
        }
        
        public void put(final String s, final Object o) {
            this.map.put(s, o);
            this.inverseMap.put(o, s);
        }
    }
}
