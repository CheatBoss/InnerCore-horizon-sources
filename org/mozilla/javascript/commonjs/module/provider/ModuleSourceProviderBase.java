package org.mozilla.javascript.commonjs.module.provider;

import org.mozilla.javascript.*;
import java.net.*;
import java.io.*;

public abstract class ModuleSourceProviderBase implements ModuleSourceProvider, Serializable
{
    private static final long serialVersionUID = 1L;
    
    private static String ensureTrailingSlash(final String s) {
        if (s.endsWith("/")) {
            return s;
        }
        return s.concat("/");
    }
    
    private ModuleSource loadFromPathArray(final String s, final Scriptable scriptable, final Object o) throws IOException {
        final long uint32 = ScriptRuntime.toUint32(ScriptableObject.getProperty(scriptable, "length"));
        int n;
        if (uint32 > 2147483647L) {
            n = Integer.MAX_VALUE;
        }
        else {
            n = (int)uint32;
        }
        int i = 0;
        while (i < n) {
            final String ensureTrailingSlash = ensureTrailingSlash(ScriptableObject.getTypedProperty(scriptable, i, String.class));
            try {
                URI resolve;
                if (!(resolve = new URI(ensureTrailingSlash)).isAbsolute()) {
                    resolve = new File(ensureTrailingSlash).toURI().resolve("");
                }
                final ModuleSource loadFromUri = this.loadFromUri(resolve.resolve(s), resolve, o);
                if (loadFromUri != null) {
                    return loadFromUri;
                }
                ++i;
                continue;
            }
            catch (URISyntaxException ex) {
                throw new MalformedURLException(ex.getMessage());
            }
            break;
        }
        return null;
    }
    
    protected boolean entityNeedsRevalidation(final Object o) {
        return true;
    }
    
    protected ModuleSource loadFromFallbackLocations(final String s, final Object o) throws IOException, URISyntaxException {
        return null;
    }
    
    protected ModuleSource loadFromPrivilegedLocations(final String s, final Object o) throws IOException, URISyntaxException {
        return null;
    }
    
    protected abstract ModuleSource loadFromUri(final URI p0, final URI p1, final Object p2) throws IOException, URISyntaxException;
    
    @Override
    public ModuleSource loadSource(final String s, final Scriptable scriptable, final Object o) throws IOException, URISyntaxException {
        if (!this.entityNeedsRevalidation(o)) {
            return ModuleSourceProviderBase.NOT_MODIFIED;
        }
        final ModuleSource loadFromPrivilegedLocations = this.loadFromPrivilegedLocations(s, o);
        if (loadFromPrivilegedLocations != null) {
            return loadFromPrivilegedLocations;
        }
        if (scriptable != null) {
            final ModuleSource loadFromPathArray = this.loadFromPathArray(s, scriptable, o);
            if (loadFromPathArray != null) {
                return loadFromPathArray;
            }
        }
        return this.loadFromFallbackLocations(s, o);
    }
    
    @Override
    public ModuleSource loadSource(final URI uri, final URI uri2, final Object o) throws IOException, URISyntaxException {
        return this.loadFromUri(uri, uri2, o);
    }
}
