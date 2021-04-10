package org.mozilla.javascript.commonjs.module;

import java.net.*;
import org.mozilla.javascript.*;

public class ModuleScope extends TopLevel
{
    private static final long serialVersionUID = 1L;
    private final URI base;
    private final URI uri;
    
    public ModuleScope(final Scriptable prototype, final URI uri, final URI base) {
        this.uri = uri;
        this.base = base;
        this.setPrototype(prototype);
        this.cacheBuiltins();
    }
    
    public URI getBase() {
        return this.base;
    }
    
    public URI getUri() {
        return this.uri;
    }
}
