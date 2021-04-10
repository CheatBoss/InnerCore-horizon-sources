package org.mozilla.javascript.commonjs.module.provider;

import java.util.*;
import java.util.concurrent.*;
import org.mozilla.javascript.commonjs.module.*;

public class StrongCachingModuleScriptProvider extends CachingModuleScriptProviderBase
{
    private static final long serialVersionUID = 1L;
    private final Map<String, CachedModuleScript> modules;
    
    public StrongCachingModuleScriptProvider(final ModuleSourceProvider moduleSourceProvider) {
        super(moduleSourceProvider);
        this.modules = new ConcurrentHashMap<String, CachedModuleScript>(16, 0.75f, CachingModuleScriptProviderBase.getConcurrencyLevel());
    }
    
    @Override
    protected CachedModuleScript getLoadedModule(final String s) {
        return this.modules.get(s);
    }
    
    @Override
    protected void putLoadedModule(final String s, final ModuleScript moduleScript, final Object o) {
        this.modules.put(s, new CachedModuleScript(moduleScript, o));
    }
}
