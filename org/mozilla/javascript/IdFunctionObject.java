package org.mozilla.javascript;

public class IdFunctionObject extends BaseFunction
{
    static final long serialVersionUID = -5332312783643935019L;
    private int arity;
    private String functionName;
    private final IdFunctionCall idcall;
    private final int methodId;
    private final Object tag;
    private boolean useCallAsConstructor;
    
    public IdFunctionObject(final IdFunctionCall idcall, final Object tag, final int methodId, final int arity) {
        if (arity < 0) {
            throw new IllegalArgumentException();
        }
        this.idcall = idcall;
        this.tag = tag;
        this.methodId = methodId;
        if ((this.arity = arity) < 0) {
            throw new IllegalArgumentException();
        }
    }
    
    public IdFunctionObject(final IdFunctionCall idcall, final Object tag, final int methodId, final String functionName, final int arity, final Scriptable scriptable) {
        super(scriptable, null);
        if (arity < 0) {
            throw new IllegalArgumentException();
        }
        if (functionName == null) {
            throw new IllegalArgumentException();
        }
        this.idcall = idcall;
        this.tag = tag;
        this.methodId = methodId;
        this.arity = arity;
        this.functionName = functionName;
    }
    
    public final void addAsProperty(final Scriptable scriptable) {
        ScriptableObject.defineProperty(scriptable, this.functionName, this, 2);
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return this.idcall.execIdCall(this, context, scriptable, scriptable2, array);
    }
    
    @Override
    public Scriptable createObject(final Context context, final Scriptable scriptable) {
        if (this.useCallAsConstructor) {
            return null;
        }
        throw ScriptRuntime.typeError1("msg.not.ctor", this.functionName);
    }
    
    @Override
    String decompile(int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        if ((n2 & 0x1) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            sb.append("function ");
            sb.append(this.getFunctionName());
            sb.append("() { ");
        }
        sb.append("[native code for ");
        if (this.idcall instanceof Scriptable) {
            sb.append(((Scriptable)this.idcall).getClassName());
            sb.append('.');
        }
        sb.append(this.getFunctionName());
        sb.append(", arity=");
        sb.append(this.getArity());
        String s;
        if (n != 0) {
            s = "]\n";
        }
        else {
            s = "] }\n";
        }
        sb.append(s);
        return sb.toString();
    }
    
    public void exportAsScopeProperty() {
        this.addAsProperty(this.getParentScope());
    }
    
    @Override
    public int getArity() {
        return this.arity;
    }
    
    @Override
    public String getFunctionName() {
        if (this.functionName == null) {
            return "";
        }
        return this.functionName;
    }
    
    @Override
    public int getLength() {
        return this.getArity();
    }
    
    @Override
    public Scriptable getPrototype() {
        Scriptable prototype;
        if ((prototype = super.getPrototype()) == null) {
            prototype = ScriptableObject.getFunctionPrototype(this.getParentScope());
            this.setPrototype(prototype);
        }
        return prototype;
    }
    
    public Object getTag() {
        return this.tag;
    }
    
    public final boolean hasTag(final Object o) {
        if (o == null) {
            return this.tag == null;
        }
        return o.equals(this.tag);
    }
    
    public void initFunction(final String functionName, final Scriptable parentScope) {
        if (functionName == null) {
            throw new IllegalArgumentException();
        }
        if (parentScope == null) {
            throw new IllegalArgumentException();
        }
        this.functionName = functionName;
        this.setParentScope(parentScope);
    }
    
    public final void markAsConstructor(final Scriptable immunePrototypeProperty) {
        this.useCallAsConstructor = true;
        this.setImmunePrototypeProperty(immunePrototypeProperty);
    }
    
    public final int methodId() {
        return this.methodId;
    }
    
    public final RuntimeException unknown() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BAD FUNCTION ID=");
        sb.append(this.methodId);
        sb.append(" MASTER=");
        sb.append(this.idcall);
        return new IllegalArgumentException(sb.toString());
    }
}
