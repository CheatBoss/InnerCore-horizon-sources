package org.mozilla.javascript;

import java.io.*;
import java.util.*;

public class NativeJavaPackage extends ScriptableObject
{
    static final long serialVersionUID = 7445054382212031523L;
    private transient ClassLoader classLoader;
    private Set<String> negativeCache;
    private String packageName;
    
    @Deprecated
    public NativeJavaPackage(final String s) {
        this(false, s, Context.getCurrentContext().getApplicationClassLoader());
    }
    
    @Deprecated
    public NativeJavaPackage(final String s, final ClassLoader classLoader) {
        this(false, s, classLoader);
    }
    
    NativeJavaPackage(final boolean b, final String packageName, final ClassLoader classLoader) {
        this.negativeCache = null;
        this.packageName = packageName;
        this.classLoader = classLoader;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.classLoader = Context.getCurrentContext().getApplicationClassLoader();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof NativeJavaPackage;
        final boolean b2 = false;
        if (b) {
            final NativeJavaPackage nativeJavaPackage = (NativeJavaPackage)o;
            boolean b3 = b2;
            if (this.packageName.equals(nativeJavaPackage.packageName)) {
                b3 = b2;
                if (this.classLoader == nativeJavaPackage.classLoader) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    NativeJavaPackage forcePackage(final String s, final Scriptable scriptable) {
        final Object value = super.get(s, this);
        if (value != null && value instanceof NativeJavaPackage) {
            return (NativeJavaPackage)value;
        }
        String string;
        if (this.packageName.length() == 0) {
            string = s;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.packageName);
            sb.append(".");
            sb.append(s);
            string = sb.toString();
        }
        final NativeJavaPackage nativeJavaPackage = new NativeJavaPackage(true, string, this.classLoader);
        ScriptRuntime.setObjectProtoAndParent(nativeJavaPackage, scriptable);
        super.put(s, this, nativeJavaPackage);
        return nativeJavaPackage;
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        return NativeJavaPackage.NOT_FOUND;
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        return this.getPkgProperty(s, scriptable, true);
    }
    
    @Override
    public String getClassName() {
        return "JavaPackage";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        return this.toString();
    }
    
    Object getPkgProperty(final String s, final Scriptable scriptable, final boolean b) {
        synchronized (this) {
            final Object value = super.get(s, scriptable);
            if (value != NativeJavaPackage.NOT_FOUND) {
                return value;
            }
            if (this.negativeCache != null && this.negativeCache.contains(s)) {
                return null;
            }
            String string;
            if (this.packageName.length() == 0) {
                string = s;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.packageName);
                sb.append('.');
                sb.append(s);
                string = sb.toString();
            }
            final Context context = Context.getContext();
            final ClassShutter classShutter = context.getClassShutter();
            final ScriptableObject scriptableObject = null;
            Scriptable wrapJavaClass = null;
            Label_0214: {
                if (classShutter != null) {
                    wrapJavaClass = scriptableObject;
                    if (!classShutter.visibleToScripts(string)) {
                        break Label_0214;
                    }
                }
                Class<?> clazz;
                if (this.classLoader != null) {
                    clazz = Kit.classOrNull(this.classLoader, string);
                }
                else {
                    clazz = Kit.classOrNull(string);
                }
                wrapJavaClass = scriptableObject;
                if (clazz != null) {
                    wrapJavaClass = context.getWrapFactory().wrapJavaClass(context, ScriptableObject.getTopLevelScope(this), clazz);
                    wrapJavaClass.setPrototype(this.getPrototype());
                }
            }
            ScriptableObject scriptableObject2;
            if ((scriptableObject2 = (ScriptableObject)wrapJavaClass) == null) {
                if (b) {
                    scriptableObject2 = new NativeJavaPackage(true, string, this.classLoader);
                    ScriptRuntime.setObjectProtoAndParent(scriptableObject2, this.getParentScope());
                }
                else {
                    if (this.negativeCache == null) {
                        this.negativeCache = new HashSet<String>();
                    }
                    this.negativeCache.add(s);
                    scriptableObject2 = (ScriptableObject)wrapJavaClass;
                }
            }
            if (scriptableObject2 != null) {
                super.put(s, scriptable, scriptableObject2);
            }
            return scriptableObject2;
        }
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return false;
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return true;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.packageName.hashCode();
        int hashCode2;
        if (this.classLoader == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.classLoader.hashCode();
        }
        return hashCode ^ hashCode2;
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        throw Context.reportRuntimeError0("msg.pkg.int");
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[JavaPackage ");
        sb.append(this.packageName);
        sb.append("]");
        return sb.toString();
    }
}
