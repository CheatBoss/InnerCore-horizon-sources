package org.mozilla.javascript.commonjs.module.provider;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import java.net.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.commonjs.module.*;
import java.lang.ref.*;

public class SoftCachingModuleScriptProvider extends CachingModuleScriptProviderBase
{
    private static final long serialVersionUID = 1L;
    private transient ReferenceQueue<Script> scriptRefQueue;
    private transient ConcurrentMap<String, ScriptReference> scripts;
    
    public SoftCachingModuleScriptProvider(final ModuleSourceProvider moduleSourceProvider) {
        super(moduleSourceProvider);
        this.scriptRefQueue = new ReferenceQueue<Script>();
        this.scripts = new ConcurrentHashMap<String, ScriptReference>(16, 0.75f, CachingModuleScriptProviderBase.getConcurrencyLevel());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.scriptRefQueue = new ReferenceQueue<Script>();
        this.scripts = new ConcurrentHashMap<String, ScriptReference>();
        for (final Map.Entry<K, CachedModuleScript> entry : ((Map)objectInputStream.readObject()).entrySet()) {
            final CachedModuleScript cachedModuleScript = entry.getValue();
            this.putLoadedModule((String)entry.getKey(), cachedModuleScript.getModule(), cachedModuleScript.getValidator());
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final HashMap<Object, CachedModuleScript> hashMap = new HashMap<Object, CachedModuleScript>();
        for (final Map.Entry<Object, Object> entry : this.scripts.entrySet()) {
            final CachedModuleScript cachedModuleScript = entry.getValue().getCachedModuleScript();
            if (cachedModuleScript != null) {
                hashMap.put(entry.getKey(), cachedModuleScript);
            }
        }
        objectOutputStream.writeObject(hashMap);
    }
    
    @Override
    protected CachedModuleScript getLoadedModule(final String s) {
        final ScriptReference scriptReference = this.scripts.get(s);
        if (scriptReference != null) {
            return scriptReference.getCachedModuleScript();
        }
        return null;
    }
    
    @Override
    public ModuleScript getModuleScript(final Context context, final String s, final URI uri, final URI uri2, final Scriptable scriptable) throws Exception {
        while (true) {
            final ScriptReference scriptReference = (ScriptReference)this.scriptRefQueue.poll();
            if (scriptReference == null) {
                break;
            }
            this.scripts.remove(scriptReference.getModuleId(), scriptReference);
        }
        return super.getModuleScript(context, s, uri, uri2, scriptable);
    }
    
    @Override
    protected void putLoadedModule(final String s, final ModuleScript moduleScript, final Object o) {
        this.scripts.put(s, new ScriptReference(moduleScript.getScript(), s, moduleScript.getUri(), moduleScript.getBase(), o, this.scriptRefQueue));
    }
    
    private static class ScriptReference extends SoftReference<Script>
    {
        private final URI base;
        private final String moduleId;
        private final URI uri;
        private final Object validator;
        
        ScriptReference(final Script script, final String moduleId, final URI uri, final URI base, final Object validator, final ReferenceQueue<Script> referenceQueue) {
            super(script, referenceQueue);
            this.moduleId = moduleId;
            this.uri = uri;
            this.base = base;
            this.validator = validator;
        }
        
        CachedModuleScript getCachedModuleScript() {
            final Script script = this.get();
            if (script == null) {
                return null;
            }
            return new CachedModuleScript(new ModuleScript(script, this.uri, this.base), this.validator);
        }
        
        String getModuleId() {
            return this.moduleId;
        }
    }
}
