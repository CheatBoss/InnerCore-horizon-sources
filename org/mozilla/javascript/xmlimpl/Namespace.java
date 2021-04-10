package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.*;

class Namespace extends IdScriptableObject
{
    private static final int Id_constructor = 1;
    private static final int Id_prefix = 1;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int Id_uri = 2;
    private static final int MAX_INSTANCE_ID = 2;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final Object NAMESPACE_TAG;
    static final long serialVersionUID = -5765755238131301744L;
    private XmlNode.Namespace ns;
    private Namespace prototype;
    
    static {
        NAMESPACE_TAG = "Namespace";
    }
    
    private Namespace() {
    }
    
    private Namespace constructNamespace() {
        return this.newNamespace("", "");
    }
    
    private Namespace constructNamespace(Object o, final Object o2) {
        String s;
        if (o2 instanceof QName) {
            final QName qName = (QName)o2;
            if ((s = qName.uri()) == null) {
                s = qName.toString();
            }
        }
        else {
            s = ScriptRuntime.toString(o2);
        }
        if (s.length() == 0) {
            if (o != Undefined.instance) {
                final String s3;
                final String s2 = s3 = ScriptRuntime.toString(o);
                if (s2.length() != 0) {
                    o = new StringBuilder();
                    ((StringBuilder)o).append("Illegal prefix '");
                    ((StringBuilder)o).append(s2);
                    ((StringBuilder)o).append("' for 'no namespace'.");
                    throw ScriptRuntime.typeError(((StringBuilder)o).toString());
                }
                return this.newNamespace(s3, s);
            }
        }
        else if (o != Undefined.instance) {
            if (XMLName.accept(o)) {
                final String s3 = ScriptRuntime.toString(o);
                return this.newNamespace(s3, s);
            }
        }
        String s3 = "";
        return this.newNamespace(s3, s);
    }
    
    static Namespace create(final Scriptable parentScope, final Namespace prototype, final XmlNode.Namespace ns) {
        final Namespace namespace = new Namespace();
        namespace.setParentScope(parentScope);
        namespace.setPrototype(namespace.prototype = prototype);
        namespace.ns = ns;
        return namespace;
    }
    
    private boolean equals(final Namespace namespace) {
        return this.uri().equals(namespace.uri());
    }
    
    private Object jsConstructor(final Context context, final boolean b, final Object[] array) {
        if (!b && array.length == 1) {
            return this.castToNamespace(array[0]);
        }
        if (array.length == 0) {
            return this.constructNamespace();
        }
        if (array.length == 1) {
            return this.constructNamespace(array[0]);
        }
        return this.constructNamespace(array[0], array[1]);
    }
    
    private String js_toSource() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        toSourceImpl(this.ns.getPrefix(), this.ns.getUri(), sb);
        sb.append(')');
        return sb.toString();
    }
    
    private Namespace realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof Namespace)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (Namespace)scriptable;
    }
    
    static void toSourceImpl(final String s, final String s2, final StringBuilder sb) {
        sb.append("new Namespace(");
        if (s2.length() == 0) {
            if (!"".equals(s)) {
                throw new IllegalArgumentException(s);
            }
        }
        else {
            sb.append('\'');
            if (s != null) {
                sb.append(ScriptRuntime.escapeString(s, '\''));
                sb.append("', '");
            }
            sb.append(ScriptRuntime.escapeString(s2, '\''));
            sb.append('\'');
        }
        sb.append(')');
    }
    
    Namespace castToNamespace(final Object o) {
        if (o instanceof Namespace) {
            return (Namespace)o;
        }
        return this.constructNamespace(o);
    }
    
    Namespace constructNamespace(final Object o) {
        String prefix;
        String s;
        if (o instanceof Namespace) {
            final Namespace namespace = (Namespace)o;
            prefix = namespace.prefix();
            s = namespace.uri();
        }
        else {
            final boolean b = o instanceof QName;
            final String s2 = null;
            final String s3 = null;
            if (b) {
                final QName qName = (QName)o;
                final String uri = qName.uri();
                if (uri != null) {
                    final String prefix2 = qName.prefix();
                    s = uri;
                    prefix = prefix2;
                }
                else {
                    final String string = qName.toString();
                    prefix = s3;
                    s = string;
                }
            }
            else {
                final String s4 = s = ScriptRuntime.toString(o);
                prefix = s2;
                if (s4.length() == 0) {
                    prefix = "";
                    s = s4;
                }
            }
        }
        return this.newNamespace(prefix, s);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Namespace && this.equals((Namespace)o);
    }
    
    @Override
    protected Object equivalentValues(final Object o) {
        if (!(o instanceof Namespace)) {
            return Scriptable.NOT_FOUND;
        }
        if (this.equals((Namespace)o)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(Namespace.NAMESPACE_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 3: {
                return this.realThis(scriptable2, idFunctionObject).js_toSource();
            }
            case 2: {
                return this.realThis(scriptable2, idFunctionObject).toString();
            }
            case 1: {
                return this.jsConstructor(context, scriptable2 == null, array);
            }
        }
    }
    
    public void exportAsJSClass(final boolean b) {
        this.exportAsJSClass(3, this.getParentScope(), b);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 3) {
            s2 = "uri";
            n = 2;
        }
        else if (length == 6) {
            s2 = "prefix";
            n = 1;
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
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        switch (n2) {
            default: {
                throw new IllegalStateException();
            }
            case 1:
            case 2: {
                return IdScriptableObject.instanceIdInfo(5, super.getMaxInstanceId() + n2);
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 8) {
            final char char1 = s.charAt(3);
            if (char1 == 'o') {
                s2 = "toSource";
                n = 3;
            }
            else if (char1 == 't') {
                s2 = "toString";
                n = 2;
            }
        }
        else if (length == 11) {
            s2 = "constructor";
            n = 1;
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
    public String getClassName() {
        return "Namespace";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        return this.uri();
    }
    
    final XmlNode.Namespace getDelegate() {
        return this.ns;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n - super.getMaxInstanceId()) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 2: {
                return "uri";
            }
            case 1: {
                return "prefix";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n - super.getMaxInstanceId()) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 2: {
                return this.ns.getUri();
            }
            case 1: {
                if (this.ns.getPrefix() == null) {
                    return Undefined.instance;
                }
                return this.ns.getPrefix();
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return super.getMaxInstanceId() + 2;
    }
    
    @Override
    public int hashCode() {
        return this.uri().hashCode();
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
                n2 = 0;
                s = "toSource";
                break;
            }
            case 2: {
                n2 = 0;
                s = "toString";
                break;
            }
            case 1: {
                n2 = 2;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(Namespace.NAMESPACE_TAG, n, s, n2);
    }
    
    Namespace newNamespace(final String s) {
        Namespace prototype;
        if (this.prototype == null) {
            prototype = this;
        }
        else {
            prototype = this.prototype;
        }
        return create(this.getParentScope(), prototype, XmlNode.Namespace.create(s));
    }
    
    Namespace newNamespace(final String s, final String s2) {
        if (s == null) {
            return this.newNamespace(s2);
        }
        Namespace prototype;
        if (this.prototype == null) {
            prototype = this;
        }
        else {
            prototype = this.prototype;
        }
        return create(this.getParentScope(), prototype, XmlNode.Namespace.create(s, s2));
    }
    
    public String prefix() {
        return this.ns.getPrefix();
    }
    
    public String toLocaleString() {
        return this.toString();
    }
    
    @Override
    public String toString() {
        return this.uri();
    }
    
    public String uri() {
        return this.ns.getUri();
    }
}
