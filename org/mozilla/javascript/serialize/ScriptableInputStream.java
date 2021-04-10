package org.mozilla.javascript.serialize;

import java.io.*;
import org.mozilla.javascript.*;

public class ScriptableInputStream extends ObjectInputStream
{
    private ClassLoader classLoader;
    private Scriptable scope;
    
    public ScriptableInputStream(final InputStream inputStream, final Scriptable scope) throws IOException {
        super(inputStream);
        this.scope = scope;
        this.enableResolveObject(true);
        final Context currentContext = Context.getCurrentContext();
        if (currentContext != null) {
            this.classLoader = currentContext.getApplicationClassLoader();
        }
    }
    
    @Override
    protected Class<?> resolveClass(final ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        final String name = objectStreamClass.getName();
        if (this.classLoader != null) {
            try {
                return this.classLoader.loadClass(name);
            }
            catch (ClassNotFoundException ex) {}
        }
        return super.resolveClass(objectStreamClass);
    }
    
    @Override
    protected Object resolveObject(final Object o) throws IOException {
        if (o instanceof ScriptableOutputStream.PendingLookup) {
            final String name = ((ScriptableOutputStream.PendingLookup)o).getName();
            final Object lookupQualifiedName = ScriptableOutputStream.lookupQualifiedName(this.scope, name);
            if (lookupQualifiedName == Scriptable.NOT_FOUND) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Object ");
                sb.append(name);
                sb.append(" not found upon ");
                sb.append("deserialization.");
                throw new IOException(sb.toString());
            }
            return lookupQualifiedName;
        }
        else {
            if (o instanceof UniqueTag) {
                return ((UniqueTag)o).readResolve();
            }
            Object resolve = o;
            if (o instanceof Undefined) {
                resolve = ((Undefined)o).readResolve();
            }
            return resolve;
        }
    }
}
