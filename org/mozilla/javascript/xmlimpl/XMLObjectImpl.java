package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.xml.*;
import java.io.*;
import org.mozilla.javascript.*;

abstract class XMLObjectImpl extends XMLObject
{
    private static final int Id_addNamespace = 2;
    private static final int Id_appendChild = 3;
    private static final int Id_attribute = 4;
    private static final int Id_attributes = 5;
    private static final int Id_child = 6;
    private static final int Id_childIndex = 7;
    private static final int Id_children = 8;
    private static final int Id_comments = 9;
    private static final int Id_constructor = 1;
    private static final int Id_contains = 10;
    private static final int Id_copy = 11;
    private static final int Id_descendants = 12;
    private static final int Id_elements = 13;
    private static final int Id_hasComplexContent = 18;
    private static final int Id_hasOwnProperty = 17;
    private static final int Id_hasSimpleContent = 19;
    private static final int Id_inScopeNamespaces = 14;
    private static final int Id_insertChildAfter = 15;
    private static final int Id_insertChildBefore = 16;
    private static final int Id_length = 20;
    private static final int Id_localName = 21;
    private static final int Id_name = 22;
    private static final int Id_namespace = 23;
    private static final int Id_namespaceDeclarations = 24;
    private static final int Id_nodeKind = 25;
    private static final int Id_normalize = 26;
    private static final int Id_parent = 27;
    private static final int Id_prependChild = 28;
    private static final int Id_processingInstructions = 29;
    private static final int Id_propertyIsEnumerable = 30;
    private static final int Id_removeNamespace = 31;
    private static final int Id_replace = 32;
    private static final int Id_setChildren = 33;
    private static final int Id_setLocalName = 34;
    private static final int Id_setName = 35;
    private static final int Id_setNamespace = 36;
    private static final int Id_text = 37;
    private static final int Id_toSource = 39;
    private static final int Id_toString = 38;
    private static final int Id_toXMLString = 40;
    private static final int Id_valueOf = 41;
    private static final int MAX_PROTOTYPE_ID = 41;
    private static final Object XMLOBJECT_TAG;
    private XMLLibImpl lib;
    private boolean prototypeFlag;
    
    static {
        XMLOBJECT_TAG = "XMLObject";
    }
    
    protected XMLObjectImpl(final XMLLibImpl xmlLibImpl, final Scriptable scriptable, final XMLObject xmlObject) {
        this.initialize(xmlLibImpl, scriptable, xmlObject);
    }
    
    private static Object arg(final Object[] array, final int n) {
        if (n < array.length) {
            return array[n];
        }
        return Undefined.instance;
    }
    
    private XMLList getMatches(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        this.addMatches(xmlList, xmlName);
        return xmlList;
    }
    
    private Object[] toObjectArray(final Object[] array) {
        final Object[] array2 = new Object[array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }
    
    private void xmlMethodNotFound(final Object o, final String s) {
        throw ScriptRuntime.notFunctionError(o, s);
    }
    
    abstract void addMatches(final XMLList p0, final XMLName p1);
    
    @Override
    public final Object addValues(final Context context, final boolean b, final Object o) {
        if (o instanceof XMLObject) {
            XMLObject xmlObject;
            XMLObject xmlObject2;
            if (b) {
                xmlObject = this;
                xmlObject2 = (XMLObject)o;
            }
            else {
                xmlObject = (XMLObject)o;
                xmlObject2 = this;
            }
            return this.lib.addXMLObjects(context, xmlObject, xmlObject2);
        }
        if (o == Undefined.instance) {
            return ScriptRuntime.toString(this);
        }
        return super.addValues(context, b, o);
    }
    
    abstract XMLList child(final int p0);
    
    abstract XMLList child(final XMLName p0);
    
    abstract XMLList children();
    
    abstract XMLList comments();
    
    abstract boolean contains(final Object p0);
    
    abstract XMLObjectImpl copy();
    
    final XML createEmptyXML() {
        return this.newXML(XmlNode.createEmpty(this.getProcessor()));
    }
    
    final Namespace createNamespace(final XmlNode.Namespace namespace) {
        if (namespace == null) {
            return null;
        }
        return this.lib.createNamespaces(new XmlNode.Namespace[] { namespace })[0];
    }
    
    final Namespace[] createNamespaces(final XmlNode.Namespace[] array) {
        return this.lib.createNamespaces(array);
    }
    
    @Override
    public void delete(final String s) {
        this.deleteXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), s));
    }
    
    @Override
    public final boolean delete(final Context context, final Object o) {
        Context currentContext = context;
        if (context == null) {
            currentContext = Context.getCurrentContext();
        }
        final XMLName xmlNameOrIndex = this.lib.toXMLNameOrIndex(currentContext, o);
        if (xmlNameOrIndex == null) {
            this.delete((int)ScriptRuntime.lastUint32Result(currentContext));
            return true;
        }
        this.deleteXMLProperty(xmlNameOrIndex);
        return true;
    }
    
    abstract void deleteXMLProperty(final XMLName p0);
    
    final String ecmaEscapeAttributeValue(String escapeAttributeValue) {
        escapeAttributeValue = this.lib.escapeAttributeValue(escapeAttributeValue);
        return escapeAttributeValue.substring(1, escapeAttributeValue.length() - 1);
    }
    
    final XML ecmaToXml(final Object o) {
        return this.lib.ecmaToXml(o);
    }
    
    abstract XMLList elements(final XMLName p0);
    
    @Override
    public NativeWith enterDotQuery(final Scriptable scriptable) {
        final XMLWithScope xmlWithScope = new XMLWithScope(this.lib, scriptable, this);
        xmlWithScope.initAsDotQuery();
        return xmlWithScope;
    }
    
    @Override
    public NativeWith enterWith(final Scriptable scriptable) {
        return new XMLWithScope(this.lib, scriptable, this);
    }
    
    @Override
    protected final Object equivalentValues(final Object o) {
        if (this.equivalentXml(o)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    abstract boolean equivalentXml(final Object p0);
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(XMLObjectImpl.XMLOBJECT_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        boolean b = true;
        if (methodId == 1) {
            if (scriptable2 != null) {
                b = false;
            }
            return this.jsConstructor(context, b, array);
        }
        if (!(scriptable2 instanceof XMLObjectImpl)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        final XMLObjectImpl xmlObjectImpl = (XMLObjectImpl)scriptable2;
        final XML xml = xmlObjectImpl.getXML();
        String string = null;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 41: {
                return xmlObjectImpl.valueOf();
            }
            case 40: {
                return xmlObjectImpl.toXMLString();
            }
            case 39: {
                return xmlObjectImpl.toSource(ScriptRuntime.toInt32(array, 0));
            }
            case 38: {
                return xmlObjectImpl.toString();
            }
            case 37: {
                return xmlObjectImpl.text();
            }
            case 36: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "setNamespace");
                }
                xml.setNamespace(this.lib.castToNamespace(context, arg(array, 0)));
                return Undefined.instance;
            }
            case 35: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "setName");
                }
                Object instance;
                if (array.length != 0) {
                    instance = array[0];
                }
                else {
                    instance = Undefined.instance;
                }
                xml.setName(this.lib.constructQName(context, instance));
                return Undefined.instance;
            }
            case 34: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "setLocalName");
                }
                final Object arg = arg(array, 0);
                String localName;
                if (arg instanceof QName) {
                    localName = ((QName)arg).localName();
                }
                else {
                    localName = ScriptRuntime.toString(arg);
                }
                xml.setLocalName(localName);
                return Undefined.instance;
            }
            case 33: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "setChildren");
                }
                return xml.setChildren(arg(array, 0));
            }
            case 32: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "replace");
                }
                final XMLName xmlNameOrIndex = this.lib.toXMLNameOrIndex(context, arg(array, 0));
                final Object arg2 = arg(array, 1);
                if (xmlNameOrIndex == null) {
                    return xml.replace((int)ScriptRuntime.lastUint32Result(context), arg2);
                }
                return xml.replace(xmlNameOrIndex, arg2);
            }
            case 31: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "removeNamespace");
                }
                return xml.removeNamespace(this.lib.castToNamespace(context, arg(array, 0)));
            }
            case 30: {
                return ScriptRuntime.wrapBoolean(xmlObjectImpl.propertyIsEnumerable(arg(array, 0)));
            }
            case 29: {
                XMLName xmlName;
                if (array.length > 0) {
                    xmlName = this.lib.toXMLName(context, array[0]);
                }
                else {
                    xmlName = XMLName.formStar();
                }
                return xmlObjectImpl.processingInstructions(xmlName);
            }
            case 28: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "prependChild");
                }
                return xml.prependChild(arg(array, 0));
            }
            case 27: {
                return xmlObjectImpl.parent();
            }
            case 26: {
                xmlObjectImpl.normalize();
                return Undefined.instance;
            }
            case 25: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "nodeKind");
                }
                return xml.nodeKind();
            }
            case 24: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "namespaceDeclarations");
                }
                return context.newArray(scriptable, this.toObjectArray(xml.namespaceDeclarations()));
            }
            case 23: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "namespace");
                }
                if (array.length > 0) {
                    string = ScriptRuntime.toString(array[0]);
                }
                final Namespace namespace = xml.namespace(string);
                if (namespace == null) {
                    return Undefined.instance;
                }
                return namespace;
            }
            case 22: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "name");
                }
                return xml.name();
            }
            case 21: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "localName");
                }
                return xml.localName();
            }
            case 20: {
                return ScriptRuntime.wrapInt(xmlObjectImpl.length());
            }
            case 19: {
                return ScriptRuntime.wrapBoolean(xmlObjectImpl.hasSimpleContent());
            }
            case 18: {
                return ScriptRuntime.wrapBoolean(xmlObjectImpl.hasComplexContent());
            }
            case 17: {
                return ScriptRuntime.wrapBoolean(xmlObjectImpl.hasOwnProperty(this.lib.toXMLName(context, arg(array, 0))));
            }
            case 16: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "insertChildBefore");
                }
                final Object arg3 = arg(array, 0);
                if (arg3 != null && !(arg3 instanceof XML)) {
                    return Undefined.instance;
                }
                return xml.insertChildBefore((XML)arg3, arg(array, 1));
            }
            case 15: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "insertChildAfter");
                }
                final Object arg4 = arg(array, 0);
                if (arg4 != null && !(arg4 instanceof XML)) {
                    return Undefined.instance;
                }
                return xml.insertChildAfter((XML)arg4, arg(array, 1));
            }
            case 14: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "inScopeNamespaces");
                }
                return context.newArray(scriptable, this.toObjectArray(xml.inScopeNamespaces()));
            }
            case 13: {
                XMLName xmlName2;
                if (array.length == 0) {
                    xmlName2 = XMLName.formStar();
                }
                else {
                    xmlName2 = this.lib.toXMLName(context, array[0]);
                }
                return xmlObjectImpl.elements(xmlName2);
            }
            case 12: {
                Serializable s;
                if (array.length == 0) {
                    s = XmlNode.QName.create(null, null);
                }
                else {
                    s = this.lib.toNodeQName(context, array[0], false);
                }
                return xmlObjectImpl.getMatches(XMLName.create((XmlNode.QName)s, false, true));
            }
            case 11: {
                return xmlObjectImpl.copy();
            }
            case 10: {
                return ScriptRuntime.wrapBoolean(xmlObjectImpl.contains(arg(array, 0)));
            }
            case 9: {
                return xmlObjectImpl.comments();
            }
            case 8: {
                return xmlObjectImpl.children();
            }
            case 7: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "childIndex");
                }
                return ScriptRuntime.wrapInt(xml.childIndex());
            }
            case 6: {
                final XMLName xmlNameOrIndex2 = this.lib.toXMLNameOrIndex(context, arg(array, 0));
                if (xmlNameOrIndex2 == null) {
                    return xmlObjectImpl.child((int)ScriptRuntime.lastUint32Result(context));
                }
                return xmlObjectImpl.child(xmlNameOrIndex2);
            }
            case 5: {
                return xmlObjectImpl.getMatches(XMLName.create(XmlNode.QName.create(null, null), true, false));
            }
            case 4: {
                return xmlObjectImpl.getMatches(XMLName.create(this.lib.toNodeQName(context, arg(array, 0), true), true, false));
            }
            case 3: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "appendChild");
                }
                return xml.appendChild(arg(array, 0));
            }
            case 2: {
                if (xml == null) {
                    this.xmlMethodNotFound(xmlObjectImpl, "addNamespace");
                }
                return xml.addNamespace(this.lib.castToNamespace(context, arg(array, 0)));
            }
        }
    }
    
    final void exportAsJSClass(final boolean b) {
        this.prototypeFlag = true;
        this.exportAsJSClass(41, this.getParentScope(), b);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = 0;
        String s3 = null;
        Label_1001: {
            switch (s.length()) {
                default: {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    break;
                }
                case 22: {
                    s3 = "processingInstructions";
                    n = 29;
                    break;
                }
                case 21: {
                    s3 = "namespaceDeclarations";
                    n = 24;
                    break;
                }
                case 20: {
                    s3 = "propertyIsEnumerable";
                    n = 30;
                    break;
                }
                case 17: {
                    final char char1 = s.charAt(3);
                    if (char1 == 'C') {
                        s3 = "hasComplexContent";
                        n = 18;
                        break;
                    }
                    if (char1 == 'c') {
                        s3 = "inScopeNamespaces";
                        n = 14;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 == 'e') {
                        s3 = "insertChildBefore";
                        n = 16;
                        break;
                    }
                    break;
                }
                case 16: {
                    final char char2 = s.charAt(0);
                    if (char2 == 'h') {
                        s3 = "hasSimpleContent";
                        n = 19;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char2 == 'i') {
                        s3 = "insertChildAfter";
                        n = 15;
                        break;
                    }
                    break;
                }
                case 15: {
                    s3 = "removeNamespace";
                    n = 31;
                    break;
                }
                case 14: {
                    s3 = "hasOwnProperty";
                    n = 17;
                    break;
                }
                case 12: {
                    final char char3 = s.charAt(0);
                    if (char3 == 'a') {
                        s3 = "addNamespace";
                        n = 2;
                        break;
                    }
                    if (char3 == 'p') {
                        s3 = "prependChild";
                        n = 28;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char3 != 's') {
                        break;
                    }
                    final char char4 = s.charAt(3);
                    if (char4 == 'L') {
                        s3 = "setLocalName";
                        n = 34;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char4 == 'N') {
                        s3 = "setNamespace";
                        n = 36;
                        break;
                    }
                    break;
                }
                case 11: {
                    switch (s.charAt(0)) {
                        default: {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            break Label_1001;
                        }
                        case 't': {
                            s3 = "toXMLString";
                            n = 40;
                            break Label_1001;
                        }
                        case 's': {
                            s3 = "setChildren";
                            n = 33;
                            break Label_1001;
                        }
                        case 'd': {
                            s3 = "descendants";
                            n = 12;
                            break Label_1001;
                        }
                        case 'c': {
                            s3 = "constructor";
                            n = 1;
                            break Label_1001;
                        }
                        case 'a': {
                            s3 = "appendChild";
                            n = 3;
                            break Label_1001;
                        }
                    }
                    break;
                }
                case 10: {
                    final char char5 = s.charAt(0);
                    if (char5 == 'a') {
                        s3 = "attributes";
                        n = 5;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char5 == 'c') {
                        s3 = "childIndex";
                        n = 7;
                        break;
                    }
                    break;
                }
                case 9: {
                    final char char6 = s.charAt(2);
                    if (char6 == 'c') {
                        s3 = "localName";
                        n = 21;
                        break;
                    }
                    if (char6 == 'm') {
                        s3 = "namespace";
                        n = 23;
                        break;
                    }
                    if (char6 == 'r') {
                        s3 = "normalize";
                        n = 26;
                        break;
                    }
                    if (char6 != 't') {
                        n = (b ? 1 : 0);
                        s3 = s2;
                        break;
                    }
                    s3 = "attribute";
                    n = 4;
                    break;
                }
                case 8: {
                    switch (s.charAt(2)) {
                        default: {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            break Label_1001;
                        }
                        case 'n': {
                            s3 = "contains";
                            n = 10;
                            break Label_1001;
                        }
                        case 'm': {
                            s3 = "comments";
                            n = 9;
                            break Label_1001;
                        }
                        case 'i': {
                            s3 = "children";
                            n = 8;
                            break Label_1001;
                        }
                        case 'e': {
                            s3 = "elements";
                            n = 13;
                            break Label_1001;
                        }
                        case 'd': {
                            s3 = "nodeKind";
                            n = 25;
                            break Label_1001;
                        }
                        case 'S': {
                            final char char7 = s.charAt(7);
                            if (char7 == 'e') {
                                s3 = "toSource";
                                n = 39;
                                break Label_1001;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (char7 == 'g') {
                                s3 = "toString";
                                n = 38;
                                break Label_1001;
                            }
                            break Label_1001;
                        }
                    }
                    break;
                }
                case 7: {
                    final char char8 = s.charAt(0);
                    if (char8 == 'r') {
                        s3 = "replace";
                        n = 32;
                        break;
                    }
                    if (char8 == 's') {
                        s3 = "setName";
                        n = 35;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char8 == 'v') {
                        s3 = "valueOf";
                        n = 41;
                        break;
                    }
                    break;
                }
                case 6: {
                    final char char9 = s.charAt(0);
                    if (char9 == 'l') {
                        s3 = "length";
                        n = 20;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char9 == 'p') {
                        s3 = "parent";
                        n = 27;
                        break;
                    }
                    break;
                }
                case 5: {
                    s3 = "child";
                    n = 6;
                    break;
                }
                case 4: {
                    final char char10 = s.charAt(0);
                    if (char10 == 'c') {
                        s3 = "copy";
                        n = 11;
                        break;
                    }
                    if (char10 == 'n') {
                        s3 = "name";
                        n = 22;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char10 == 't') {
                        s3 = "text";
                        n = 37;
                        break;
                    }
                    break;
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
        return n2;
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        return this.getXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), s));
    }
    
    @Override
    public final Object get(final Context context, Object value) {
        Context currentContext = context;
        if (context == null) {
            currentContext = Context.getCurrentContext();
        }
        final XMLName xmlNameOrIndex = this.lib.toXMLNameOrIndex(currentContext, value);
        if (xmlNameOrIndex == null) {
            value = this.get((int)ScriptRuntime.lastUint32Result(currentContext), this);
            Object instance;
            if ((instance = value) == Scriptable.NOT_FOUND) {
                instance = Undefined.instance;
            }
            return instance;
        }
        return this.getXMLProperty(xmlNameOrIndex);
    }
    
    @Override
    public final Object getDefaultValue(final Class<?> clazz) {
        return this.toString();
    }
    
    @Override
    public Object getFunctionProperty(final Context context, final int n) {
        if (this.isPrototype()) {
            return super.get(n, this);
        }
        final Scriptable prototype = this.getPrototype();
        if (prototype instanceof XMLObject) {
            return ((XMLObject)prototype).getFunctionProperty(context, n);
        }
        return XMLObjectImpl.NOT_FOUND;
    }
    
    @Override
    public Object getFunctionProperty(final Context context, final String s) {
        if (this.isPrototype()) {
            return super.get(s, this);
        }
        final Scriptable prototype = this.getPrototype();
        if (prototype instanceof XMLObject) {
            return ((XMLObject)prototype).getFunctionProperty(context, s);
        }
        return XMLObjectImpl.NOT_FOUND;
    }
    
    XMLLibImpl getLib() {
        return this.lib;
    }
    
    @Override
    public final Scriptable getParentScope() {
        return super.getParentScope();
    }
    
    final XmlProcessor getProcessor() {
        return this.lib.getProcessor();
    }
    
    @Override
    public final Scriptable getPrototype() {
        return super.getPrototype();
    }
    
    abstract XML getXML();
    
    abstract Object getXMLProperty(final XMLName p0);
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return this.hasXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), s));
    }
    
    @Override
    public final boolean has(final Context context, final Object o) {
        Context currentContext = context;
        if (context == null) {
            currentContext = Context.getCurrentContext();
        }
        final XMLName xmlNameOrIndex = this.lib.toXMLNameOrIndex(currentContext, o);
        if (xmlNameOrIndex == null) {
            return this.has((int)ScriptRuntime.lastUint32Result(currentContext), this);
        }
        return this.hasXMLProperty(xmlNameOrIndex);
    }
    
    abstract boolean hasComplexContent();
    
    @Override
    public final boolean hasInstance(final Scriptable scriptable) {
        return super.hasInstance(scriptable);
    }
    
    abstract boolean hasOwnProperty(final XMLName p0);
    
    abstract boolean hasSimpleContent();
    
    abstract boolean hasXMLProperty(final XMLName p0);
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 41: {
                n2 = 0;
                s = "valueOf";
                break;
            }
            case 40: {
                n2 = 1;
                s = "toXMLString";
                break;
            }
            case 39: {
                n2 = 1;
                s = "toSource";
                break;
            }
            case 38: {
                n2 = 0;
                s = "toString";
                break;
            }
            case 37: {
                n2 = 0;
                s = "text";
                break;
            }
            case 36: {
                n2 = 1;
                s = "setNamespace";
                break;
            }
            case 35: {
                n2 = 1;
                s = "setName";
                break;
            }
            case 34: {
                n2 = 1;
                s = "setLocalName";
                break;
            }
            case 33: {
                n2 = 1;
                s = "setChildren";
                break;
            }
            case 32: {
                n2 = 2;
                s = "replace";
                break;
            }
            case 31: {
                n2 = 1;
                s = "removeNamespace";
                break;
            }
            case 30: {
                n2 = 1;
                s = "propertyIsEnumerable";
                break;
            }
            case 29: {
                n2 = 1;
                s = "processingInstructions";
                break;
            }
            case 28: {
                n2 = 1;
                s = "prependChild";
                break;
            }
            case 27: {
                n2 = 0;
                s = "parent";
                break;
            }
            case 26: {
                n2 = 0;
                s = "normalize";
                break;
            }
            case 25: {
                n2 = 0;
                s = "nodeKind";
                break;
            }
            case 24: {
                n2 = 0;
                s = "namespaceDeclarations";
                break;
            }
            case 23: {
                n2 = 1;
                s = "namespace";
                break;
            }
            case 22: {
                n2 = 0;
                s = "name";
                break;
            }
            case 21: {
                n2 = 0;
                s = "localName";
                break;
            }
            case 20: {
                n2 = 0;
                s = "length";
                break;
            }
            case 19: {
                n2 = 0;
                s = "hasSimpleContent";
                break;
            }
            case 18: {
                n2 = 0;
                s = "hasComplexContent";
                break;
            }
            case 17: {
                n2 = 1;
                s = "hasOwnProperty";
                break;
            }
            case 16: {
                n2 = 2;
                s = "insertChildBefore";
                break;
            }
            case 15: {
                n2 = 2;
                s = "insertChildAfter";
                break;
            }
            case 14: {
                n2 = 0;
                s = "inScopeNamespaces";
                break;
            }
            case 13: {
                n2 = 1;
                s = "elements";
                break;
            }
            case 12: {
                n2 = 1;
                s = "descendants";
                break;
            }
            case 11: {
                n2 = 0;
                s = "copy";
                break;
            }
            case 10: {
                n2 = 1;
                s = "contains";
                break;
            }
            case 9: {
                n2 = 0;
                s = "comments";
                break;
            }
            case 8: {
                n2 = 0;
                s = "children";
                break;
            }
            case 7: {
                n2 = 0;
                s = "childIndex";
                break;
            }
            case 6: {
                n2 = 1;
                s = "child";
                break;
            }
            case 5: {
                n2 = 0;
                s = "attributes";
                break;
            }
            case 4: {
                n2 = 1;
                s = "attribute";
                break;
            }
            case 3: {
                n2 = 1;
                s = "appendChild";
                break;
            }
            case 2: {
                n2 = 1;
                s = "addNamespace";
                break;
            }
            case 1: {
                IdFunctionObject idFunctionObject;
                if (this instanceof XML) {
                    idFunctionObject = new XMLCtor((XML)this, XMLObjectImpl.XMLOBJECT_TAG, n, 1);
                }
                else {
                    idFunctionObject = new IdFunctionObject(this, XMLObjectImpl.XMLOBJECT_TAG, n, 1);
                }
                this.initPrototypeConstructor(idFunctionObject);
                return;
            }
        }
        this.initPrototypeMethod(XMLObjectImpl.XMLOBJECT_TAG, n, s, n2);
    }
    
    final void initialize(final XMLLibImpl lib, final Scriptable parentScope, final XMLObject prototype) {
        this.setParentScope(parentScope);
        this.setPrototype(prototype);
        this.prototypeFlag = (prototype == null);
        this.lib = lib;
    }
    
    final boolean isPrototype() {
        return this.prototypeFlag;
    }
    
    protected abstract Object jsConstructor(final Context p0, final boolean p1, final Object[] p2);
    
    abstract int length();
    
    @Override
    public Ref memberRef(final Context context, final Object o, final int n) {
        boolean b = false;
        final boolean b2 = (n & 0x2) != 0x0;
        if ((n & 0x4) != 0x0) {
            b = true;
        }
        if (!b2 && !b) {
            throw Kit.codeBug();
        }
        final XMLName create = XMLName.create(this.lib.toNodeQName(context, o, b2), b2, b);
        create.initXMLObject(this);
        return create;
    }
    
    @Override
    public Ref memberRef(final Context context, final Object o, final Object o2, final int n) {
        boolean b = false;
        final boolean b2 = (n & 0x2) != 0x0;
        if ((n & 0x4) != 0x0) {
            b = true;
        }
        final XMLName create = XMLName.create(this.lib.toNodeQName(context, o, o2), b2, b);
        create.initXMLObject(this);
        return create;
    }
    
    final QName newQName(final String s, final String s2, final String s3) {
        return this.lib.newQName(s, s2, s3);
    }
    
    final QName newQName(final XmlNode.QName qName) {
        return this.lib.newQName(qName);
    }
    
    final XML newTextElementXML(final XmlNode xmlNode, final XmlNode.QName qName, final String s) {
        return this.lib.newTextElementXML(xmlNode, qName, s);
    }
    
    final XML newXML(final XmlNode xmlNode) {
        return this.lib.newXML(xmlNode);
    }
    
    final XML newXMLFromJs(final Object o) {
        return this.lib.newXMLFromJs(o);
    }
    
    final XMLList newXMLList() {
        return this.lib.newXMLList();
    }
    
    final XMLList newXMLListFrom(final Object o) {
        return this.lib.newXMLListFrom(o);
    }
    
    abstract void normalize();
    
    abstract Object parent();
    
    abstract XMLList processingInstructions(final XMLName p0);
    
    abstract boolean propertyIsEnumerable(final Object p0);
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        this.putXMLProperty(this.lib.toXMLNameFromString(Context.getCurrentContext(), s), o);
    }
    
    @Override
    public final void put(final Context context, final Object o, final Object o2) {
        Context currentContext = context;
        if (context == null) {
            currentContext = Context.getCurrentContext();
        }
        final XMLName xmlNameOrIndex = this.lib.toXMLNameOrIndex(currentContext, o);
        if (xmlNameOrIndex == null) {
            this.put((int)ScriptRuntime.lastUint32Result(currentContext), this, o2);
            return;
        }
        this.putXMLProperty(xmlNameOrIndex, o2);
    }
    
    abstract void putXMLProperty(final XMLName p0, final Object p1);
    
    @Override
    public final void setParentScope(final Scriptable parentScope) {
        super.setParentScope(parentScope);
    }
    
    @Override
    public final void setPrototype(final Scriptable prototype) {
        super.setPrototype(prototype);
    }
    
    abstract XMLList text();
    
    abstract String toSource(final int p0);
    
    @Override
    public abstract String toString();
    
    abstract String toXMLString();
    
    abstract Object valueOf();
    
    XML xmlFromNode(final XmlNode xmlNode) {
        if (xmlNode.getXml() == null) {
            xmlNode.setXml(this.newXML(xmlNode));
        }
        return xmlNode.getXml();
    }
}
