package org.mozilla.javascript.commonjs.module.provider;

import org.mozilla.javascript.*;
import java.io.*;
import java.net.*;

public interface ModuleSourceProvider
{
    public static final ModuleSource NOT_MODIFIED = new ModuleSource(null, null, null, null, null);
    
    ModuleSource loadSource(final String p0, final Scriptable p1, final Object p2) throws IOException, URISyntaxException;
    
    ModuleSource loadSource(final URI p0, final URI p1, final Object p2) throws IOException, URISyntaxException;
}
