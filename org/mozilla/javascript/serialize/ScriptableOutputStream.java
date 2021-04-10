package org.mozilla.javascript.serialize;

import java.util.*;
import org.mozilla.javascript.*;
import java.io.*;

public class ScriptableOutputStream extends ObjectOutputStream
{
    private Scriptable scope;
    private Map<Object, String> table;
    
    public ScriptableOutputStream(final OutputStream outputStream, final Scriptable scope) throws IOException {
        super(outputStream);
        this.scope = scope;
        (this.table = new HashMap<Object, String>()).put(scope, "");
        this.enableReplaceObject(true);
        this.excludeStandardObjectNames();
    }
    
    static Object lookupQualifiedName(Scriptable property, String nextToken) {
        final StringTokenizer stringTokenizer = new StringTokenizer(nextToken, ".");
        Scriptable scriptable;
        while (true) {
            scriptable = property;
            if (!stringTokenizer.hasMoreTokens()) {
                break;
            }
            nextToken = stringTokenizer.nextToken();
            property = (Scriptable)ScriptableObject.getProperty(property, nextToken);
            if ((scriptable = property) == null) {
                break;
            }
            if (!(property instanceof Scriptable)) {
                return property;
            }
        }
        return scriptable;
    }
    
    public void addExcludedName(final String s) {
        final Object lookupQualifiedName = lookupQualifiedName(this.scope, s);
        if (!(lookupQualifiedName instanceof Scriptable)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Object for excluded name ");
            sb.append(s);
            sb.append(" not found.");
            throw new IllegalArgumentException(sb.toString());
        }
        this.table.put(lookupQualifiedName, s);
    }
    
    public void addOptionalExcludedName(final String s) {
        final Object lookupQualifiedName = lookupQualifiedName(this.scope, s);
        if (lookupQualifiedName != null && lookupQualifiedName != UniqueTag.NOT_FOUND) {
            if (!(lookupQualifiedName instanceof Scriptable)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Object for excluded name ");
                sb.append(s);
                sb.append(" is not a Scriptable, it is ");
                sb.append(((UniqueTag)lookupQualifiedName).getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            this.table.put(lookupQualifiedName, s);
        }
    }
    
    public void excludeAllIds(final Object[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Object o = array[i];
            if (o instanceof String && this.scope.get((String)o, this.scope) instanceof Scriptable) {
                this.addExcludedName((String)o);
            }
        }
    }
    
    public void excludeStandardObjectNames() {
        final String[] array = new String[21];
        final int n = 0;
        array[0] = "Object";
        array[1] = "Object.prototype";
        array[2] = "Function";
        array[3] = "Function.prototype";
        array[4] = "String";
        array[5] = "String.prototype";
        array[6] = "Math";
        array[7] = "Array";
        array[8] = "Array.prototype";
        array[9] = "Error";
        array[10] = "Error.prototype";
        array[11] = "Number";
        array[12] = "Number.prototype";
        array[13] = "Date";
        array[14] = "Date.prototype";
        array[15] = "RegExp";
        array[16] = "RegExp.prototype";
        array[17] = "Script";
        array[18] = "Script.prototype";
        array[19] = "Continuation";
        array[20] = "Continuation.prototype";
        for (int i = 0; i < array.length; ++i) {
            this.addExcludedName(array[i]);
        }
        final String[] array2 = { "XML", "XML.prototype", "XMLList", "XMLList.prototype" };
        for (int j = n; j < array2.length; ++j) {
            this.addOptionalExcludedName(array2[j]);
        }
    }
    
    public boolean hasExcludedName(final String s) {
        return this.table.get(s) != null;
    }
    
    public void removeExcludedName(final String s) {
        this.table.remove(s);
    }
    
    @Override
    protected Object replaceObject(final Object o) throws IOException {
        final String s = this.table.get(o);
        if (s == null) {
            return o;
        }
        return new PendingLookup(s);
    }
    
    static class PendingLookup implements Serializable
    {
        static final long serialVersionUID = -2692990309789917727L;
        private String name;
        
        PendingLookup(final String name) {
            this.name = name;
        }
        
        String getName() {
            return this.name;
        }
    }
}
