package com.faendir.rhino_android;

import java.io.*;
import org.mozilla.javascript.*;

class NoSecurityController extends SecurityController implements Serializable
{
    public GeneratedClassLoader createClassLoader(final ClassLoader classLoader, final Object o) {
        return Context.getCurrentContext().createClassLoader(classLoader);
    }
    
    public Object getDynamicSecurityDomain(final Object o) {
        return null;
    }
}
