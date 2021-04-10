package com.zhekasmirnov.innercore.api.runtime.saver.world;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import org.mozilla.javascript.*;
import org.json.*;
import java.util.*;

public class WorldDataScopeRegistry
{
    private static final WorldDataScopeRegistry instance;
    private final Map<String, SaverScope> scopeMap;
    
    static {
        (instance = new WorldDataScopeRegistry()).addScope("_legacy_global", (SaverScope)new ScriptableSaverScope() {
            @Override
            public void read(final Object o) {
                Object empty = o;
                if (o == null) {
                    empty = ScriptableObjectHelper.createEmpty();
                }
                Callback.invokeAPICallback("ReadSaves", empty);
            }
            
            @Override
            public ScriptableObject save() {
                final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
                Callback.invokeAPICallback("WriteSaves", empty);
                return empty;
            }
        });
    }
    
    public WorldDataScopeRegistry() {
        this.scopeMap = new HashMap<String, SaverScope>();
    }
    
    public static WorldDataScopeRegistry getInstance() {
        return WorldDataScopeRegistry.instance;
    }
    
    public void addScope(String string, final SaverScope saverScope) {
        if (saverScope == null) {
            return;
        }
        while (this.scopeMap.containsKey(string)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(string.hashCode() & 0xFF);
            string = sb.toString();
        }
        this.scopeMap.put(string, saverScope);
    }
    
    public void readAllScopes(final JSONObject jsonObject, final SavesErrorHandler savesErrorHandler, final MissingScopeHandler missingScopeHandler) {
        for (final Map.Entry<String, SaverScope> entry : this.scopeMap.entrySet()) {
            final String s = entry.getKey();
            final Object opt = jsonObject.opt(s);
            try {
                entry.getValue().readJson(opt);
            }
            catch (Throwable t) {
                savesErrorHandler.handle(s, t);
            }
        }
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s2 = keys.next();
            if (!this.scopeMap.containsKey(s2)) {
                missingScopeHandler.handle(s2, jsonObject.opt(s2));
            }
        }
    }
    
    public void saveAllScopes(final JSONObject jsonObject, final SavesErrorHandler savesErrorHandler) {
        for (final Map.Entry<String, SaverScope> entry : this.scopeMap.entrySet()) {
            try {
                jsonObject.put((String)entry.getKey(), entry.getValue().saveAsJson());
            }
            catch (Throwable t) {
                savesErrorHandler.handle(entry.getKey(), t);
            }
        }
    }
    
    public interface MissingScopeHandler
    {
        void handle(final String p0, final Object p1);
    }
    
    public interface SaverScope
    {
        void readJson(final Object p0) throws Exception;
        
        Object saveAsJson() throws Exception;
    }
    
    public interface SavesErrorHandler
    {
        void handle(final String p0, final Throwable p1);
    }
}
