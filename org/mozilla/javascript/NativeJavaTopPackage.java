package org.mozilla.javascript;

public class NativeJavaTopPackage extends NativeJavaPackage implements Function, IdFunctionCall
{
    private static final Object FTAG;
    private static final int Id_getClass = 1;
    private static final String[][] commonPackages;
    static final long serialVersionUID = -1455787259477709999L;
    
    static {
        commonPackages = new String[][] { { "java", "lang", "reflect" }, { "java", "io" }, { "java", "math" }, { "java", "net" }, { "java", "util", "zip" }, { "java", "text", "resources" }, { "java", "applet" }, { "javax", "swing" } };
        FTAG = "JavaTopPackage";
    }
    
    NativeJavaTopPackage(final ClassLoader classLoader) {
        super(true, "", classLoader);
    }
    
    public static void init(final Context context, final Scriptable parentScope, final boolean b) {
        final NativeJavaTopPackage nativeJavaTopPackage = new NativeJavaTopPackage(context.getApplicationClassLoader());
        nativeJavaTopPackage.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        nativeJavaTopPackage.setParentScope(parentScope);
        final int n = 0;
        for (int i = 0; i != NativeJavaTopPackage.commonPackages.length; ++i) {
            NativeJavaPackage forcePackage = nativeJavaTopPackage;
            for (int j = 0; j != NativeJavaTopPackage.commonPackages[i].length; ++j) {
                forcePackage = forcePackage.forcePackage(NativeJavaTopPackage.commonPackages[i][j], parentScope);
            }
        }
        final IdFunctionObject idFunctionObject = new IdFunctionObject(nativeJavaTopPackage, NativeJavaTopPackage.FTAG, 1, "getClass", 1, parentScope);
        final String[] topPackageNames = ScriptRuntime.getTopPackageNames();
        final NativeJavaPackage[] array = new NativeJavaPackage[topPackageNames.length];
        for (int k = 0; k < topPackageNames.length; ++k) {
            array[k] = (NativeJavaPackage)nativeJavaTopPackage.get(topPackageNames[k], nativeJavaTopPackage);
        }
        final ScriptableObject scriptableObject = (ScriptableObject)parentScope;
        if (b) {
            idFunctionObject.sealObject();
        }
        idFunctionObject.exportAsScopeProperty();
        scriptableObject.defineProperty("Packages", nativeJavaTopPackage, 2);
        for (int l = n; l < topPackageNames.length; ++l) {
            scriptableObject.defineProperty(topPackageNames[l], array[l], 2);
        }
    }
    
    private Scriptable js_getClass(final Context context, final Scriptable scriptable, final Object[] array) {
        if (array.length > 0) {
            int n = 0;
            if (array[0] instanceof Wrapper) {
                Scriptable scriptable2 = this;
                final String name = ((Wrapper)array[0]).unwrap().getClass().getName();
                while (true) {
                    final int index = name.indexOf(46, n);
                    String s;
                    if (index == -1) {
                        s = name.substring(n);
                    }
                    else {
                        s = name.substring(n, index);
                    }
                    final Object value = scriptable2.get(s, scriptable2);
                    if (!(value instanceof Scriptable)) {
                        break;
                    }
                    scriptable2 = (Scriptable)value;
                    if (index == -1) {
                        return scriptable2;
                    }
                    n = index + 1;
                }
            }
        }
        throw Context.reportRuntimeError0("msg.not.java.obj");
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return this.construct(context, scriptable, array);
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        ClassLoader classLoader2;
        final ClassLoader classLoader = classLoader2 = null;
        if (array.length != 0) {
            Object unwrap;
            final Object o = unwrap = array[0];
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
            classLoader2 = classLoader;
            if (unwrap instanceof ClassLoader) {
                classLoader2 = (ClassLoader)unwrap;
            }
        }
        if (classLoader2 == null) {
            Context.reportRuntimeError0("msg.not.classloader");
            return null;
        }
        final NativeJavaPackage nativeJavaPackage = new NativeJavaPackage(true, "", classLoader2);
        ScriptRuntime.setObjectProtoAndParent(nativeJavaPackage, scriptable);
        return nativeJavaPackage;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (idFunctionObject.hasTag(NativeJavaTopPackage.FTAG) && idFunctionObject.methodId() == 1) {
            return this.js_getClass(context, scriptable, array);
        }
        throw idFunctionObject.unknown();
    }
}
