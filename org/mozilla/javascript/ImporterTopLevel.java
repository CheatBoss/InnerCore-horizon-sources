package org.mozilla.javascript;

public class ImporterTopLevel extends TopLevel
{
    private static final Object IMPORTER_TAG;
    private static final int Id_constructor = 1;
    private static final int Id_importClass = 2;
    private static final int Id_importPackage = 3;
    private static final int MAX_PROTOTYPE_ID = 3;
    static final long serialVersionUID = -9095380847465315412L;
    private ObjArray importedPackages;
    private boolean topScopeFlag;
    
    static {
        IMPORTER_TAG = "Importer";
    }
    
    public ImporterTopLevel() {
        this.importedPackages = new ObjArray();
    }
    
    public ImporterTopLevel(final Context context) {
        this(context, false);
    }
    
    public ImporterTopLevel(final Context context, final boolean b) {
        this.importedPackages = new ObjArray();
        this.initStandardObjects(context, b);
    }
    
    private Object getPackageProperty(final String s, final Scriptable scriptable) {
        Object not_FOUND = ImporterTopLevel.NOT_FOUND;
        Object importedPackages = this.importedPackages;
        synchronized (importedPackages) {
            // monitorexit(importedPackages)
            final Object[] array = this.importedPackages.toArray();
            for (int i = 0; i < array.length; ++i, not_FOUND = importedPackages) {
                final Object pkgProperty = ((NativeJavaPackage)array[i]).getPkgProperty(s, scriptable, false);
                importedPackages = not_FOUND;
                if (pkgProperty != null) {
                    importedPackages = not_FOUND;
                    if (!(pkgProperty instanceof NativeJavaPackage)) {
                        if (not_FOUND != ImporterTopLevel.NOT_FOUND) {
                            throw Context.reportRuntimeError2("msg.ambig.import", not_FOUND.toString(), pkgProperty.toString());
                        }
                        importedPackages = pkgProperty;
                    }
                }
            }
            return importedPackages;
        }
    }
    
    private void importClass(final NativeJavaClass nativeJavaClass) {
        final String name = nativeJavaClass.getClassObject().getName();
        final String substring = name.substring(name.lastIndexOf(46) + 1);
        final Object value = this.get(substring, this);
        if (value != ImporterTopLevel.NOT_FOUND && value != nativeJavaClass) {
            throw Context.reportRuntimeError1("msg.prop.defined", substring);
        }
        this.put(substring, this, nativeJavaClass);
    }
    
    private void importPackage(final NativeJavaPackage nativeJavaPackage) {
        if (nativeJavaPackage == null) {
            return;
        }
        final ObjArray importedPackages = this.importedPackages;
        // monitorenter(importedPackages)
        int n = 0;
        while (true) {
            try {
                if (n == this.importedPackages.size()) {
                    this.importedPackages.add(nativeJavaPackage);
                    return;
                }
                if (nativeJavaPackage.equals(this.importedPackages.get(n))) {
                    return;
                }
            }
            finally {
            }
            // monitorexit(importedPackages)
            ++n;
        }
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new ImporterTopLevel().exportAsJSClass(3, scriptable, b);
    }
    
    private Object js_construct(final Scriptable parentScope, final Object[] array) {
        final ImporterTopLevel importerTopLevel = new ImporterTopLevel();
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            if (o instanceof NativeJavaClass) {
                importerTopLevel.importClass((NativeJavaClass)o);
            }
            else {
                if (!(o instanceof NativeJavaPackage)) {
                    throw Context.reportRuntimeError1("msg.not.class.not.pkg", Context.toString(o));
                }
                importerTopLevel.importPackage((NativeJavaPackage)o);
            }
        }
        importerTopLevel.setParentScope(parentScope);
        importerTopLevel.setPrototype(this);
        return importerTopLevel;
    }
    
    private Object js_importClass(final Object[] array) {
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            if (!(o instanceof NativeJavaClass)) {
                throw Context.reportRuntimeError1("msg.not.class", Context.toString(o));
            }
            this.importClass((NativeJavaClass)o);
        }
        return Undefined.instance;
    }
    
    private Object js_importPackage(final Object[] array) {
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            if (!(o instanceof NativeJavaPackage)) {
                throw Context.reportRuntimeError1("msg.not.pkg", Context.toString(o));
            }
            this.importPackage((NativeJavaPackage)o);
        }
        return Undefined.instance;
    }
    
    private ImporterTopLevel realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (this.topScopeFlag) {
            return this;
        }
        if (!(scriptable instanceof ImporterTopLevel)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (ImporterTopLevel)scriptable;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(ImporterTopLevel.IMPORTER_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 3: {
                return this.realThis(scriptable2, idFunctionObject).js_importPackage(array);
            }
            case 2: {
                return this.realThis(scriptable2, idFunctionObject).js_importClass(array);
            }
            case 1: {
                return this.js_construct(scriptable, array);
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 11) {
            final char char1 = s.charAt(0);
            if (char1 == 'c') {
                s2 = "constructor";
                n = 1;
            }
            else if (char1 == 'i') {
                s2 = "importClass";
                n = 2;
            }
        }
        else if (length == 13) {
            s2 = "importPackage";
            n = 3;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        final Object value = super.get(s, scriptable);
        if (value != ImporterTopLevel.NOT_FOUND) {
            return value;
        }
        return this.getPackageProperty(s, scriptable);
    }
    
    @Override
    public String getClassName() {
        if (this.topScopeFlag) {
            return "global";
        }
        return "JavaImporter";
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return super.has(s, scriptable) || this.getPackageProperty(s, scriptable) != ImporterTopLevel.NOT_FOUND;
    }
    
    @Deprecated
    public void importPackage(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        this.js_importPackage(array);
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 3: {
                n2 = 1;
                s = "importPackage";
                break;
            }
            case 2: {
                n2 = 1;
                s = "importClass";
                break;
            }
            case 1: {
                n2 = 0;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(ImporterTopLevel.IMPORTER_TAG, n, s, n2);
    }
    
    public void initStandardObjects(final Context context, final boolean b) {
        context.initStandardObjects(this, b);
        this.topScopeFlag = true;
        final IdFunctionObject exportAsJSClass = this.exportAsJSClass(3, this, false);
        if (b) {
            exportAsJSClass.sealObject();
        }
        this.delete("constructor");
    }
}
