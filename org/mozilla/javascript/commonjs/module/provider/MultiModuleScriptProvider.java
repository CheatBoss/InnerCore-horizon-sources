package org.mozilla.javascript.commonjs.module.provider;

import java.util.*;
import java.net.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.commonjs.module.*;

public class MultiModuleScriptProvider implements ModuleScriptProvider
{
    private final ModuleScriptProvider[] providers;
    
    public MultiModuleScriptProvider(final Iterable<? extends ModuleScriptProvider> iterable) {
        final LinkedList<ModuleScriptProvider> list = new LinkedList<ModuleScriptProvider>();
        final Iterator<? extends ModuleScriptProvider> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add((ModuleScriptProvider)iterator.next());
        }
        this.providers = list.toArray(new ModuleScriptProvider[list.size()]);
    }
    
    @Override
    public ModuleScript getModuleScript(final Context context, final String s, final URI uri, final URI uri2, final Scriptable scriptable) throws Exception {
        final ModuleScriptProvider[] providers = this.providers;
        for (int length = providers.length, i = 0; i < length; ++i) {
            final ModuleScript moduleScript = providers[i].getModuleScript(context, s, uri, uri2, scriptable);
            if (moduleScript != null) {
                return moduleScript;
            }
        }
        return null;
    }
}
