package org.mozilla.javascript;

final class Arguments extends IdScriptableObject
{
    private static final String FTAG = "Arguments";
    private static final int Id_callee = 1;
    private static final int Id_caller = 3;
    private static final int Id_length = 2;
    private static final int MAX_INSTANCE_ID = 3;
    static final long serialVersionUID = 4275508002492040609L;
    private NativeCall activation;
    private Object[] args;
    private int calleeAttr;
    private Object calleeObj;
    private int callerAttr;
    private Object callerObj;
    private int lengthAttr;
    private Object lengthObj;
    
    public Arguments(final NativeCall activation) {
        this.callerAttr = 2;
        this.calleeAttr = 2;
        this.lengthAttr = 2;
        this.activation = activation;
        final Scriptable parentScope = activation.getParentScope();
        this.setParentScope(parentScope);
        this.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        this.args = activation.originalArgs;
        this.lengthObj = this.args.length;
        final NativeFunction function = activation.function;
        this.calleeObj = function;
        final int languageVersion = function.getLanguageVersion();
        if (languageVersion <= 130 && languageVersion != 0) {
            this.callerObj = null;
            return;
        }
        this.callerObj = Arguments.NOT_FOUND;
    }
    
    private Object arg(final int n) {
        if (n >= 0 && this.args.length > n) {
            return this.args[n];
        }
        return Arguments.NOT_FOUND;
    }
    
    private Object getFromActivation(final int n) {
        return this.activation.get(this.activation.function.getParamOrVarName(n), this.activation);
    }
    
    private void putIntoActivation(final int n, final Object o) {
        this.activation.put(this.activation.function.getParamOrVarName(n), this.activation, o);
    }
    
    private void removeArg(final int n) {
        synchronized (this) {
            if (this.args[n] != Arguments.NOT_FOUND) {
                if (this.args == this.activation.originalArgs) {
                    this.args = this.args.clone();
                }
                this.args[n] = Arguments.NOT_FOUND;
            }
        }
    }
    
    private void replaceArg(final int n, final Object o) {
        if (this.sharedWithActivation(n)) {
            this.putIntoActivation(n, o);
        }
        synchronized (this) {
            if (this.args == this.activation.originalArgs) {
                this.args = this.args.clone();
            }
            this.args[n] = o;
        }
    }
    
    private boolean sharedWithActivation(int i) {
        final NativeFunction function = this.activation.function;
        final int paramCount = function.getParamCount();
        if (i < paramCount) {
            if (i < paramCount - 1) {
                final String paramOrVarName = function.getParamOrVarName(i);
                for (++i; i < paramCount; ++i) {
                    if (paramOrVarName.equals(function.getParamOrVarName(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected void defineOwnProperty(final Context context, final Object o, final ScriptableObject scriptableObject, final boolean b) {
        super.defineOwnProperty(context, o, scriptableObject, b);
        final double number = ScriptRuntime.toNumber(o);
        final int n = (int)number;
        if (number != n) {
            return;
        }
        if (this.arg(n) == Arguments.NOT_FOUND) {
            return;
        }
        if (this.isAccessorDescriptor(scriptableObject)) {
            this.removeArg(n);
            return;
        }
        final Object property = ScriptableObject.getProperty(scriptableObject, "value");
        if (property == Arguments.NOT_FOUND) {
            return;
        }
        this.replaceArg(n, property);
        if (ScriptableObject.isFalse(ScriptableObject.getProperty(scriptableObject, "writable"))) {
            this.removeArg(n);
        }
    }
    
    @Override
    public void delete(final int n) {
        if (n >= 0 && n < this.args.length) {
            this.removeArg(n);
        }
        super.delete(n);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = b ? 1 : 0;
        String s3 = s2;
        if (s.length() == 6) {
            final char char1 = s.charAt(5);
            if (char1 == 'e') {
                s3 = "callee";
                n = 1;
            }
            else if (char1 == 'h') {
                s3 = "length";
                n = 2;
            }
            else {
                n = (b ? 1 : 0);
                s3 = s2;
                if (char1 == 'r') {
                    s3 = "caller";
                    n = 3;
                }
            }
        }
        int n2 = n;
        if (s3 != null) {
            n2 = n;
            if (s3 != s) {
                n2 = n;
                if (!s3.equals(s)) {
                    n2 = 0;
                }
            }
        }
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        int n3 = 0;
        switch (n2) {
            default: {
                throw new IllegalStateException();
            }
            case 3: {
                n3 = this.callerAttr;
                break;
            }
            case 2: {
                n3 = this.lengthAttr;
                break;
            }
            case 1: {
                n3 = this.calleeAttr;
                break;
            }
        }
        return IdScriptableObject.instanceIdInfo(n3, n2);
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        final Object arg = this.arg(n);
        if (arg == Arguments.NOT_FOUND) {
            return super.get(n, scriptable);
        }
        if (this.sharedWithActivation(n)) {
            return this.getFromActivation(n);
        }
        return arg;
    }
    
    @Override
    public String getClassName() {
        return "Arguments";
    }
    
    @Override
    Object[] getIds(final boolean b) {
        Object[] ids;
        final Object[] array = ids = super.getIds(b);
        if (this.args.length != 0) {
            final boolean[] array2 = new boolean[this.args.length];
            int length = this.args.length;
            final int n = 0;
            int n2;
            for (int i = 0; i != array.length; ++i, length = n2) {
                final Object o = array[i];
                n2 = length;
                if (o instanceof Integer) {
                    final int intValue = (int)o;
                    n2 = length;
                    if (intValue >= 0) {
                        n2 = length;
                        if (intValue < this.args.length) {
                            n2 = length;
                            if (!array2[intValue]) {
                                array2[intValue] = true;
                                n2 = length - 1;
                            }
                        }
                    }
                }
            }
            int n3 = length;
            if (!b) {
                int n4 = 0;
                while (true) {
                    n3 = length;
                    if (n4 >= array2.length) {
                        break;
                    }
                    int n5 = length;
                    if (!array2[n4]) {
                        n5 = length;
                        if (super.has(n4, this)) {
                            array2[n4] = true;
                            n5 = length - 1;
                        }
                    }
                    ++n4;
                    length = n5;
                }
            }
            ids = array;
            if (n3 != 0) {
                final Object[] array3 = new Object[array.length + n3];
                System.arraycopy(array, 0, array3, n3, array.length);
                final Object[] array4 = array3;
                int n6 = 0;
                int n7;
                for (int j = n; j != this.args.length; ++j, n6 = n7) {
                    if (array2 != null) {
                        n7 = n6;
                        if (array2[j]) {
                            continue;
                        }
                    }
                    array4[n6] = j;
                    n7 = n6 + 1;
                }
                ids = array4;
                if (n6 != n3) {
                    Kit.codeBug();
                    ids = array4;
                }
            }
        }
        return ids;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 3: {
                return "caller";
            }
            case 2: {
                return "length";
            }
            case 1: {
                return "callee";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 3: {
                final Object callerObj = this.callerObj;
                if (callerObj == UniqueTag.NULL_VALUE) {
                    return null;
                }
                Object value;
                if ((value = callerObj) == null) {
                    final NativeCall parentActivationCall = this.activation.parentActivationCall;
                    value = callerObj;
                    if (parentActivationCall != null) {
                        value = parentActivationCall.get("arguments", parentActivationCall);
                    }
                }
                return value;
            }
            case 2: {
                return this.lengthObj;
            }
            case 1: {
                return this.calleeObj;
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 3;
    }
    
    @Override
    protected ScriptableObject getOwnPropertyDescriptor(final Context context, final Object o) {
        final double number = ScriptRuntime.toNumber(o);
        final int n = (int)number;
        if (number != n) {
            return super.getOwnPropertyDescriptor(context, o);
        }
        Object o2 = this.arg(n);
        if (o2 == Arguments.NOT_FOUND) {
            return super.getOwnPropertyDescriptor(context, o);
        }
        if (this.sharedWithActivation(n)) {
            o2 = this.getFromActivation(n);
        }
        if (super.has(n, this)) {
            final ScriptableObject ownPropertyDescriptor = super.getOwnPropertyDescriptor(context, o);
            ownPropertyDescriptor.put("value", ownPropertyDescriptor, o2);
            return ownPropertyDescriptor;
        }
        Scriptable parentScope;
        if ((parentScope = this.getParentScope()) == null) {
            parentScope = this;
        }
        return ScriptableObject.buildDataDescriptor(parentScope, o2, 0);
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return this.arg(n) != Arguments.NOT_FOUND || super.has(n, scriptable);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        if (this.arg(n) == Arguments.NOT_FOUND) {
            super.put(n, scriptable, o);
            return;
        }
        this.replaceArg(n, o);
    }
    
    @Override
    protected void setInstanceIdAttributes(final int n, final int calleeAttr) {
        switch (n) {
            default: {
                super.setInstanceIdAttributes(n, calleeAttr);
            }
            case 3: {
                this.callerAttr = calleeAttr;
            }
            case 2: {
                this.lengthAttr = calleeAttr;
            }
            case 1: {
                this.calleeAttr = calleeAttr;
            }
        }
    }
    
    @Override
    protected void setInstanceIdValue(final int n, Object null_VALUE) {
        switch (n) {
            default: {
                super.setInstanceIdValue(n, null_VALUE);
            }
            case 3: {
                if (null_VALUE == null) {
                    null_VALUE = UniqueTag.NULL_VALUE;
                }
                this.callerObj = null_VALUE;
            }
            case 2: {
                this.lengthObj = null_VALUE;
            }
            case 1: {
                this.calleeObj = null_VALUE;
            }
        }
    }
}
