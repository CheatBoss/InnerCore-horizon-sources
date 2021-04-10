package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.*;
import java.io.*;

final class QName extends IdScriptableObject
{
    private static final int Id_constructor = 1;
    private static final int Id_localName = 1;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int Id_uri = 2;
    private static final int MAX_INSTANCE_ID = 2;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final Object QNAME_TAG;
    static final long serialVersionUID = 416745167693026750L;
    private XmlNode.QName delegate;
    private XMLLibImpl lib;
    private QName prototype;
    
    static {
        QNAME_TAG = "QName";
    }
    
    private QName() {
    }
    
    static QName create(final XMLLibImpl lib, final Scriptable parentScope, final QName prototype, final XmlNode.QName delegate) {
        final QName qName = new QName();
        qName.lib = lib;
        qName.setParentScope(parentScope);
        qName.setPrototype(qName.prototype = prototype);
        qName.delegate = delegate;
        return qName;
    }
    
    private boolean equals(final QName qName) {
        return this.delegate.equals(qName.delegate);
    }
    
    private Object jsConstructor(final Context context, final boolean b, final Object[] array) {
        if (!b && array.length == 1) {
            return this.castToQName(this.lib, context, array[0]);
        }
        if (array.length == 0) {
            return this.constructQName(this.lib, context, Undefined.instance);
        }
        if (array.length == 1) {
            return this.constructQName(this.lib, context, array[0]);
        }
        return this.constructQName(this.lib, context, array[0], array[1]);
    }
    
    private String js_toSource() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        toSourceImpl(this.uri(), this.localName(), this.prefix(), sb);
        sb.append(')');
        return sb.toString();
    }
    
    private QName realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof QName)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (QName)scriptable;
    }
    
    private static void toSourceImpl(final String s, final String s2, final String s3, final StringBuilder sb) {
        sb.append("new QName(");
        if (s == null && s3 == null) {
            if (!"*".equals(s2)) {
                sb.append("null, ");
            }
        }
        else {
            Namespace.toSourceImpl(s3, s, sb);
            sb.append(", ");
        }
        sb.append('\'');
        sb.append(ScriptRuntime.escapeString(s2, '\''));
        sb.append("')");
    }
    
    QName castToQName(final XMLLibImpl xmlLibImpl, final Context context, final Object o) {
        if (o instanceof QName) {
            return (QName)o;
        }
        return this.constructQName(xmlLibImpl, context, o);
    }
    
    QName constructQName(final XMLLibImpl xmlLibImpl, final Context context, final Object o) {
        return this.constructQName(xmlLibImpl, context, Undefined.instance, o);
    }
    
    QName constructQName(final XMLLibImpl xmlLibImpl, final Context context, final Object o, Object defaultNamespace) {
        if (defaultNamespace instanceof QName) {
            if (o == Undefined.instance) {
                return (QName)defaultNamespace;
            }
            ((QName)defaultNamespace).localName();
        }
        String string;
        if (defaultNamespace == Undefined.instance) {
            string = "";
        }
        else {
            string = ScriptRuntime.toString(defaultNamespace);
        }
        defaultNamespace = o;
        if (o == Undefined.instance) {
            if ("*".equals(string)) {
                defaultNamespace = null;
            }
            else {
                defaultNamespace = xmlLibImpl.getDefaultNamespace(context);
            }
        }
        Namespace namespace = null;
        if (defaultNamespace != null) {
            if (defaultNamespace instanceof Namespace) {
                namespace = (Namespace)defaultNamespace;
            }
            else {
                namespace = xmlLibImpl.newNamespace(ScriptRuntime.toString(defaultNamespace));
            }
        }
        String s;
        String prefix;
        if (defaultNamespace == null) {
            s = null;
            prefix = null;
        }
        else {
            final String uri = namespace.uri();
            prefix = namespace.prefix();
            s = uri;
        }
        return this.newQName(xmlLibImpl, s, string, prefix);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof QName && this.equals((QName)o);
    }
    
    @Override
    protected Object equivalentValues(final Object o) {
        if (!(o instanceof QName)) {
            return Scriptable.NOT_FOUND;
        }
        if (this.equals((QName)o)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(QName.QNAME_TAG)) {
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
    
    void exportAsJSClass(final boolean b) {
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
        else if (length == 9) {
            s2 = "localName";
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
        return "QName";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        return this.toString();
    }
    
    final XmlNode.QName getDelegate() {
        return this.delegate;
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
                return "localName";
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
                return this.uri();
            }
            case 1: {
                return this.localName();
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return super.getMaxInstanceId() + 2;
    }
    
    @Override
    public int hashCode() {
        return this.delegate.hashCode();
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
        this.initPrototypeMethod(QName.QNAME_TAG, n, s, n2);
    }
    
    public String localName() {
        if (this.delegate.getLocalName() == null) {
            return "*";
        }
        return this.delegate.getLocalName();
    }
    
    QName newQName(final XMLLibImpl xmlLibImpl, final String s, final String s2, String s3) {
        QName prototype;
        if ((prototype = this.prototype) == null) {
            prototype = this;
        }
        Serializable s4;
        if (s3 != null) {
            s4 = XmlNode.Namespace.create(s3, s);
        }
        else if (s != null) {
            s4 = XmlNode.Namespace.create(s);
        }
        else {
            s4 = null;
        }
        s3 = s2;
        if (s2 != null) {
            s3 = s2;
            if (s2.equals("*")) {
                s3 = null;
            }
        }
        return create(xmlLibImpl, this.getParentScope(), prototype, XmlNode.QName.create((XmlNode.Namespace)s4, s3));
    }
    
    String prefix() {
        if (this.delegate.getNamespace() == null) {
            return null;
        }
        return this.delegate.getNamespace().getPrefix();
    }
    
    @Deprecated
    final XmlNode.QName toNodeQname() {
        return this.delegate;
    }
    
    @Override
    public String toString() {
        if (this.delegate.getNamespace() == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("*::");
            sb.append(this.localName());
            return sb.toString();
        }
        if (this.delegate.getNamespace().isGlobal()) {
            return this.localName();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.uri());
        sb2.append("::");
        sb2.append(this.localName());
        return sb2.toString();
    }
    
    String uri() {
        if (this.delegate.getNamespace() == null) {
            return null;
        }
        return this.delegate.getNamespace().getUri();
    }
}
