package org.mozilla.javascript;

public class BoundFunction extends BaseFunction
{
    static final long serialVersionUID = 2118137342826470729L;
    private final Object[] boundArgs;
    private final Scriptable boundThis;
    private final int length;
    private final Callable targetFunction;
    
    public BoundFunction(final Context context, final Scriptable scriptable, final Callable targetFunction, final Scriptable boundThis, final Object[] boundArgs) {
        this.targetFunction = targetFunction;
        this.boundThis = boundThis;
        this.boundArgs = boundArgs;
        if (targetFunction instanceof BaseFunction) {
            this.length = Math.max(0, ((BaseFunction)targetFunction).getLength() - boundArgs.length);
        }
        else {
            this.length = 0;
        }
        ScriptRuntime.setFunctionProtoAndParent(this, scriptable);
        final BaseFunction typeErrorThrower = ScriptRuntime.typeErrorThrower(context);
        final NativeObject nativeObject = new NativeObject();
        nativeObject.put("get", nativeObject, typeErrorThrower);
        nativeObject.put("set", nativeObject, typeErrorThrower);
        nativeObject.put("enumerable", nativeObject, false);
        nativeObject.put("configurable", nativeObject, false);
        nativeObject.preventExtensions();
        this.defineOwnProperty(context, "caller", nativeObject, false);
        this.defineOwnProperty(context, "arguments", nativeObject, false);
    }
    
    private Object[] concat(final Object[] array, final Object[] array2) {
        final Object[] array3 = new Object[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, Scriptable scriptable2, final Object[] array) {
        if (this.boundThis != null) {
            scriptable2 = this.boundThis;
        }
        else {
            scriptable2 = ScriptRuntime.getTopCallScope(context);
        }
        return this.targetFunction.call(context, scriptable, scriptable2, this.concat(this.boundArgs, array));
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        if (this.targetFunction instanceof Function) {
            return ((Function)this.targetFunction).construct(context, scriptable, this.concat(this.boundArgs, array));
        }
        throw ScriptRuntime.typeError0("msg.not.ctor");
    }
    
    @Override
    public int getLength() {
        return this.length;
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        if (this.targetFunction instanceof Function) {
            return ((Function)this.targetFunction).hasInstance(scriptable);
        }
        throw ScriptRuntime.typeError0("msg.not.ctor");
    }
}
