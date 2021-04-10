package org.mozilla.javascript.commonjs.module.provider;

import java.net.*;
import org.mozilla.javascript.commonjs.module.*;
import java.io.*;
import org.mozilla.javascript.*;

public abstract class CachingModuleScriptProviderBase implements ModuleScriptProvider, Serializable
{
    private static final int loadConcurrencyLevel;
    private static final int loadLockCount;
    private static final int loadLockMask;
    private static final int loadLockShift;
    private static final long serialVersionUID = 1L;
    private final Object[] loadLocks;
    private final ModuleSourceProvider moduleSourceProvider;
    
    static {
        loadConcurrencyLevel = Runtime.getRuntime().availableProcessors() * 8;
        int n = 0;
        int i;
        for (i = 1; i < CachingModuleScriptProviderBase.loadConcurrencyLevel; i <<= 1) {
            ++n;
        }
        loadLockShift = 32 - n;
        loadLockMask = i - 1;
        loadLockCount = i;
    }
    
    protected CachingModuleScriptProviderBase(final ModuleSourceProvider moduleSourceProvider) {
        this.loadLocks = new Object[CachingModuleScriptProviderBase.loadLockCount];
        for (int i = 0; i < this.loadLocks.length; ++i) {
            this.loadLocks[i] = new Object();
        }
        this.moduleSourceProvider = moduleSourceProvider;
    }
    
    private static boolean equal(final Object o, final Object o2) {
        if (o == null) {
            return o2 == null;
        }
        return o.equals(o2);
    }
    
    protected static int getConcurrencyLevel() {
        return CachingModuleScriptProviderBase.loadLockCount;
    }
    
    private static Object getValidator(final CachedModuleScript cachedModuleScript) {
        if (cachedModuleScript == null) {
            return null;
        }
        return cachedModuleScript.getValidator();
    }
    
    protected abstract CachedModuleScript getLoadedModule(final String p0);
    
    @Override
    public ModuleScript getModuleScript(final Context context, final String s, URI s2, URI reader, Scriptable scriptable) throws Exception {
        final CachedModuleScript loadedModule = this.getLoadedModule(s);
        final Object validator = getValidator(loadedModule);
        if (s2 == null) {
            s2 = this.moduleSourceProvider.loadSource(s, scriptable, validator);
        }
        else {
            s2 = this.moduleSourceProvider.loadSource((URI)s2, reader, validator);
        }
        if (s2 == ModuleSourceProvider.NOT_MODIFIED) {
            return loadedModule.getModule();
        }
        if (s2 == null) {
            return null;
        }
        reader = (URI)((ModuleSource)s2).getReader();
        try {
            scriptable = (Scriptable)this.loadLocks[s.hashCode() >>> CachingModuleScriptProviderBase.loadLockShift & CachingModuleScriptProviderBase.loadLockMask];
            // monitorenter(scriptable)
            Label_0212: {
                URI uri;
                String string;
                Object securityDomain;
                try {
                    final CachedModuleScript loadedModule2 = this.getLoadedModule(s);
                    if (loadedModule2 != null) {
                        try {
                            if (!equal(validator, getValidator(loadedModule2))) {
                                final ModuleScript module = loadedModule2.getModule();
                                // monitorexit(scriptable)
                                ((Reader)reader).close();
                                return module;
                            }
                        }
                        finally {
                            break Label_0212;
                        }
                    }
                    uri = ((ModuleSource)s2).getUri();
                    string = uri.toString();
                    securityDomain = ((ModuleSource)s2).getSecurityDomain();
                    final Context context2 = context;
                    final URI uri2 = reader;
                    final String s3 = string;
                    final int n = 1;
                    final Object o = securityDomain;
                    final Script script = context2.compileReader((Reader)uri2, s3, n, o);
                    final URI uri3 = uri;
                    final Serializable s4 = s2;
                    final URI uri4 = ((ModuleSource)s4).getBase();
                    final ModuleScript moduleScript = new ModuleScript(script, uri3, uri4);
                    final CachingModuleScriptProviderBase cachingModuleScriptProviderBase = this;
                    final String s5 = s;
                    final ModuleScript moduleScript2 = moduleScript;
                    final Serializable s6 = s2;
                    final Object o2 = ((ModuleSource)s6).getValidator();
                    cachingModuleScriptProviderBase.putLoadedModule(s5, moduleScript2, o2);
                    final Scriptable scriptable2 = scriptable;
                    // monitorexit(scriptable2)
                    final URI uri5 = reader;
                    ((Reader)uri5).close();
                    return moduleScript;
                }
                finally {
                    final Object o4;
                    final Object o3 = o4;
                }
                try {
                    final Context context2 = context;
                    final URI uri2 = reader;
                    final String s3 = string;
                    final int n = 1;
                    final Object o = securityDomain;
                    final Script script = context2.compileReader((Reader)uri2, s3, n, o);
                    final URI uri3 = uri;
                    final Serializable s4 = s2;
                    final URI uri4 = ((ModuleSource)s4).getBase();
                    final ModuleScript moduleScript = new ModuleScript(script, uri3, uri4);
                    final CachingModuleScriptProviderBase cachingModuleScriptProviderBase = this;
                    final String s5 = s;
                    final ModuleScript moduleScript2 = moduleScript;
                    final Serializable s6 = s2;
                    final Object o2 = ((ModuleSource)s6).getValidator();
                    cachingModuleScriptProviderBase.putLoadedModule(s5, moduleScript2, o2);
                    final Scriptable scriptable2 = scriptable;
                    // monitorexit(scriptable2)
                    final URI uri5 = reader;
                    ((Reader)uri5).close();
                    return moduleScript;
                    // monitorexit(scriptable)
                    try {
                        throw;
                    }
                    finally {}
                }
                finally {}
            }
        }
        finally {}
        ((Reader)reader).close();
    }
    
    protected abstract void putLoadedModule(final String p0, final ModuleScript p1, final Object p2);
    
    public static class CachedModuleScript
    {
        private final ModuleScript moduleScript;
        private final Object validator;
        
        public CachedModuleScript(final ModuleScript moduleScript, final Object validator) {
            this.moduleScript = moduleScript;
            this.validator = validator;
        }
        
        ModuleScript getModule() {
            return this.moduleScript;
        }
        
        Object getValidator() {
            return this.validator;
        }
    }
}
