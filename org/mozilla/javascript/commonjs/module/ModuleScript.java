package org.mozilla.javascript.commonjs.module;

import java.io.*;
import java.net.*;
import org.mozilla.javascript.*;

public class ModuleScript implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final URI base;
    private final Script script;
    private final URI uri;
    
    public ModuleScript(final Script script, final URI uri, final URI base) {
        this.script = script;
        this.uri = uri;
        this.base = base;
    }
    
    public URI getBase() {
        return this.base;
    }
    
    public Script getScript() {
        return this.script;
    }
    
    public URI getUri() {
        return this.uri;
    }
    
    public boolean isSandboxed() {
        return this.base != null && this.uri != null && !this.base.relativize(this.uri).isAbsolute();
    }
}
