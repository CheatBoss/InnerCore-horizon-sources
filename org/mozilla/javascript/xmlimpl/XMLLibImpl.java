package org.mozilla.javascript.xmlimpl;

import java.io.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.mozilla.javascript.xml.*;
import org.mozilla.javascript.*;

public final class XMLLibImpl extends XMLLib implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Scriptable globalScope;
    private Namespace namespacePrototype;
    private XmlProcessor options;
    private QName qnamePrototype;
    private XMLList xmlListPrototype;
    private XML xmlPrototype;
    
    private XMLLibImpl(final Scriptable globalScope) {
        this.options = new XmlProcessor();
        this.globalScope = globalScope;
    }
    
    private static RuntimeException badXMLName(final Object o) {
        String s;
        if (o instanceof Number) {
            s = "Can not construct XML name from number: ";
        }
        else if (o instanceof Boolean) {
            s = "Can not construct XML name from boolean: ";
        }
        else {
            if (o != Undefined.instance && o != null) {
                throw new IllegalArgumentException(o.toString());
            }
            s = "Can not construct XML name from ";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(ScriptRuntime.toString(o));
        return ScriptRuntime.typeError(sb.toString());
    }
    
    private void exportToScope(final boolean b) {
        this.xmlPrototype = this.newXML(XmlNode.createText(this.options, ""));
        this.xmlListPrototype = this.newXMLList();
        this.namespacePrototype = Namespace.create(this.globalScope, null, XmlNode.Namespace.GLOBAL);
        this.qnamePrototype = QName.create(this, this.globalScope, null, XmlNode.QName.create(XmlNode.Namespace.create(""), ""));
        this.xmlPrototype.exportAsJSClass(b);
        this.xmlListPrototype.exportAsJSClass(b);
        this.namespacePrototype.exportAsJSClass(b);
        this.qnamePrototype.exportAsJSClass(b);
    }
    
    private String getDefaultNamespaceURI(final Context context) {
        return this.getDefaultNamespace(context).uri();
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        final XMLLibImpl xmlLibImpl = new XMLLibImpl(scriptable);
        if (xmlLibImpl.bindToScope(scriptable) == xmlLibImpl) {
            xmlLibImpl.exportToScope(b);
        }
    }
    
    private XML parse(final String s) {
        try {
            return this.newXML(XmlNode.createElement(this.options, this.getDefaultNamespaceURI(Context.getCurrentContext()), s));
        }
        catch (SAXException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot parse XML: ");
            sb.append(ex.getMessage());
            throw ScriptRuntime.typeError(sb.toString());
        }
    }
    
    public static Node toDomNode(final Object o) {
        if (o instanceof XML) {
            return ((XML)o).toDomNode();
        }
        throw new IllegalArgumentException("xmlObject is not an XML object in JavaScript.");
    }
    
    private Ref xmlPrimaryReference(final Context context, final XMLName xmlName, Scriptable scriptable) {
        XMLObjectImpl xmlObjectImpl = null;
        Scriptable parentScope;
        XMLObjectImpl xmlObjectImpl2;
        do {
            xmlObjectImpl2 = xmlObjectImpl;
            if (scriptable instanceof XMLWithScope) {
                final XMLObjectImpl xmlObjectImpl3 = (XMLObjectImpl)scriptable.getPrototype();
                if (xmlObjectImpl3.hasXMLProperty(xmlName)) {
                    xmlObjectImpl2 = xmlObjectImpl3;
                    break;
                }
                if ((xmlObjectImpl2 = xmlObjectImpl) == null) {
                    xmlObjectImpl2 = xmlObjectImpl3;
                }
            }
            parentScope = scriptable.getParentScope();
            xmlObjectImpl = xmlObjectImpl2;
        } while ((scriptable = parentScope) != null);
        if (xmlObjectImpl2 != null) {
            xmlName.initXMLObject(xmlObjectImpl2);
        }
        return xmlName;
    }
    
    Object addXMLObjects(final Context context, final XMLObject xmlObject, final XMLObject xmlObject2) {
        XMLList list = this.newXMLList();
        final boolean b = xmlObject instanceof XMLList;
        int i = 0;
        if (b) {
            final XMLList list2 = (XMLList)xmlObject;
            if (list2.length() == 1) {
                list.addToList(list2.item(0));
            }
            else {
                list = this.newXMLListFrom(xmlObject);
            }
        }
        else {
            list.addToList(xmlObject);
        }
        if (xmlObject2 instanceof XMLList) {
            for (XMLList list3 = (XMLList)xmlObject2; i < list3.length(); ++i) {
                list.addToList(list3.item(i));
            }
            return list;
        }
        if (xmlObject2 instanceof XML) {
            list.addToList(xmlObject2);
        }
        return list;
    }
    
    Namespace castToNamespace(final Context context, final Object o) {
        return this.namespacePrototype.castToNamespace(o);
    }
    
    QName castToQName(final Context context, final Object o) {
        return this.qnamePrototype.castToQName(this, context, o);
    }
    
    QName constructQName(final Context context, final Object o) {
        return this.qnamePrototype.constructQName(this, context, o);
    }
    
    QName constructQName(final Context context, final Object o, final Object o2) {
        return this.qnamePrototype.constructQName(this, context, o, o2);
    }
    
    Namespace[] createNamespaces(final XmlNode.Namespace[] array) {
        final Namespace[] array2 = new Namespace[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.namespacePrototype.newNamespace(array[i].getPrefix(), array[i].getUri());
        }
        return array2;
    }
    
    final XML ecmaToXml(final Object o) {
        if (o == null || o == Undefined.instance) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot convert ");
            sb.append(o);
            sb.append(" to XML");
            throw ScriptRuntime.typeError(sb.toString());
        }
        if (o instanceof XML) {
            return (XML)o;
        }
        if (o instanceof XMLList) {
            final XMLList list = (XMLList)o;
            if (list.getXML() != null) {
                return list.getXML();
            }
            throw ScriptRuntime.typeError("Cannot convert list of >1 element to XML");
        }
        else {
            Object unwrap = o;
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
            if (unwrap instanceof Node) {
                return this.newXML(XmlNode.createElementFromNode((Node)unwrap));
            }
            final String string = ScriptRuntime.toString(unwrap);
            if (string.length() > 0 && string.charAt(0) == '<') {
                return this.parse(string);
            }
            return this.newXML(XmlNode.createText(this.options, string));
        }
    }
    
    @Override
    public String escapeAttributeValue(final Object o) {
        return this.options.escapeAttributeValue(o);
    }
    
    @Override
    public String escapeTextValue(final Object o) {
        return this.options.escapeTextValue(o);
    }
    
    Namespace getDefaultNamespace(Context currentContext) {
        Context context = currentContext;
        if (currentContext == null) {
            currentContext = Context.getCurrentContext();
            if ((context = currentContext) == null) {
                return this.namespacePrototype;
            }
        }
        final Object searchDefaultNamespace = ScriptRuntime.searchDefaultNamespace(context);
        if (searchDefaultNamespace == null) {
            return this.namespacePrototype;
        }
        if (searchDefaultNamespace instanceof Namespace) {
            return (Namespace)searchDefaultNamespace;
        }
        return this.namespacePrototype;
    }
    
    @Override
    public int getPrettyIndent() {
        return this.options.getPrettyIndent();
    }
    
    XmlProcessor getProcessor() {
        return this.options;
    }
    
    @Deprecated
    Scriptable globalScope() {
        return this.globalScope;
    }
    
    @Override
    public boolean isIgnoreComments() {
        return this.options.isIgnoreComments();
    }
    
    @Override
    public boolean isIgnoreProcessingInstructions() {
        return this.options.isIgnoreProcessingInstructions();
    }
    
    @Override
    public boolean isIgnoreWhitespace() {
        return this.options.isIgnoreWhitespace();
    }
    
    @Override
    public boolean isPrettyPrinting() {
        return this.options.isPrettyPrinting();
    }
    
    @Override
    public boolean isXMLName(final Context context, final Object o) {
        return XMLName.accept(o);
    }
    
    @Override
    public Ref nameRef(final Context context, final Object o, final Object o2, final Scriptable scriptable, final int n) {
        final XMLName create = XMLName.create(this.toNodeQName(context, o, o2), false, false);
        if ((n & 0x2) != 0x0 && !create.isAttributeName()) {
            create.setAttributeName();
        }
        return this.xmlPrimaryReference(context, create, scriptable);
    }
    
    @Override
    public Ref nameRef(final Context context, final Object o, final Scriptable scriptable, final int n) {
        if ((n & 0x2) == 0x0) {
            throw Kit.codeBug();
        }
        return this.xmlPrimaryReference(context, this.toAttributeName(context, o), scriptable);
    }
    
    Namespace newNamespace(final String s) {
        return this.namespacePrototype.newNamespace(s);
    }
    
    QName newQName(final String s, final String s2, final String s3) {
        return this.qnamePrototype.newQName(this, s, s2, s3);
    }
    
    QName newQName(final XmlNode.QName qName) {
        return QName.create(this, this.globalScope, this.qnamePrototype, qName);
    }
    
    final XML newTextElementXML(final XmlNode xmlNode, final XmlNode.QName qName, final String s) {
        return this.newXML(XmlNode.newElementWithText(this.options, xmlNode, qName, s));
    }
    
    XML newXML(final XmlNode xmlNode) {
        return new XML(this, this.globalScope, this.xmlPrototype, xmlNode);
    }
    
    final XML newXMLFromJs(final Object o) {
        String s;
        if (o != null && o != Undefined.instance) {
            if (o instanceof XMLObjectImpl) {
                s = ((XMLObjectImpl)o).toXMLString();
            }
            else {
                s = ScriptRuntime.toString(o);
            }
        }
        else {
            s = "";
        }
        if (s.trim().startsWith("<>")) {
            throw ScriptRuntime.typeError("Invalid use of XML object anonymous tags <></>.");
        }
        if (s.indexOf("<") == -1) {
            return this.newXML(XmlNode.createText(this.options, s));
        }
        return this.parse(s);
    }
    
    XMLList newXMLList() {
        return new XMLList(this, this.globalScope, this.xmlListPrototype);
    }
    
    final XMLList newXMLListFrom(final Object o) {
        final XMLList xmlList = this.newXMLList();
        if (o == null) {
            return xmlList;
        }
        if (o instanceof Undefined) {
            return xmlList;
        }
        if (o instanceof XML) {
            xmlList.getNodeList().add((XML)o);
            return xmlList;
        }
        if (o instanceof XMLList) {
            xmlList.getNodeList().add(((XMLList)o).getNodeList());
            return xmlList;
        }
        String s2;
        final String s = s2 = ScriptRuntime.toString(o).trim();
        if (!s.startsWith("<>")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("<>");
            sb.append(s);
            sb.append("</>");
            s2 = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("<fragment>");
        sb2.append(s2.substring(2));
        final String string = sb2.toString();
        if (!string.endsWith("</>")) {
            throw ScriptRuntime.typeError("XML with anonymous tag missing end anonymous tag");
        }
        final StringBuilder sb3 = new StringBuilder();
        final int length = string.length();
        int i = 0;
        sb3.append(string.substring(0, length - 3));
        sb3.append("</fragment>");
        for (XMLList children = this.newXMLFromJs(sb3.toString()).children(); i < children.getNodeList().length(); ++i) {
            xmlList.getNodeList().add((XML)children.item(i).copy());
        }
        return xmlList;
    }
    
    @Deprecated
    QName qnamePrototype() {
        return this.qnamePrototype;
    }
    
    @Override
    public void setIgnoreComments(final boolean ignoreComments) {
        this.options.setIgnoreComments(ignoreComments);
    }
    
    @Override
    public void setIgnoreProcessingInstructions(final boolean ignoreProcessingInstructions) {
        this.options.setIgnoreProcessingInstructions(ignoreProcessingInstructions);
    }
    
    @Override
    public void setIgnoreWhitespace(final boolean ignoreWhitespace) {
        this.options.setIgnoreWhitespace(ignoreWhitespace);
    }
    
    @Override
    public void setPrettyIndent(final int prettyIndent) {
        this.options.setPrettyIndent(prettyIndent);
    }
    
    @Override
    public void setPrettyPrinting(final boolean prettyPrinting) {
        this.options.setPrettyPrinting(prettyPrinting);
    }
    
    @Deprecated
    XMLName toAttributeName(final Context context, final Object o) {
        if (o instanceof XMLName) {
            return (XMLName)o;
        }
        if (o instanceof QName) {
            return XMLName.create(((QName)o).getDelegate(), true, false);
        }
        if (!(o instanceof Boolean) && !(o instanceof Number) && o != Undefined.instance && o != null) {
            String string;
            if (o instanceof String) {
                string = (String)o;
            }
            else {
                string = ScriptRuntime.toString(o);
            }
            String s = string;
            if (string != null) {
                s = string;
                if (string.equals("*")) {
                    s = null;
                }
            }
            return XMLName.create(XmlNode.QName.create(XmlNode.Namespace.create(""), s), true, false);
        }
        throw badXMLName(o);
    }
    
    @Override
    public Object toDefaultXmlNamespace(final Context context, final Object o) {
        return this.namespacePrototype.constructNamespace(o);
    }
    
    XmlNode.QName toNodeQName(final Context context, final Object o, final Object o2) {
        String s;
        if (o2 instanceof QName) {
            s = ((QName)o2).localName();
        }
        else {
            s = ScriptRuntime.toString(o2);
        }
        XmlNode.Namespace namespace;
        if (o == Undefined.instance) {
            if ("*".equals(s)) {
                namespace = null;
            }
            else {
                namespace = this.getDefaultNamespace(context).getDelegate();
            }
        }
        else if (o == null) {
            namespace = null;
        }
        else if (o instanceof Namespace) {
            namespace = ((Namespace)o).getDelegate();
        }
        else {
            namespace = this.namespacePrototype.constructNamespace(o).getDelegate();
        }
        String s2 = s;
        if (s != null) {
            s2 = s;
            if (s.equals("*")) {
                s2 = null;
            }
        }
        return XmlNode.QName.create(namespace, s2);
    }
    
    XmlNode.QName toNodeQName(final Context context, final Object o, final boolean b) {
        if (o instanceof XMLName) {
            return ((XMLName)o).toQname();
        }
        if (o instanceof QName) {
            return ((QName)o).getDelegate();
        }
        if (!(o instanceof Boolean) && !(o instanceof Number) && o != Undefined.instance && o != null) {
            String string;
            if (o instanceof String) {
                string = (String)o;
            }
            else {
                string = ScriptRuntime.toString(o);
            }
            return this.toNodeQName(context, string, b);
        }
        throw badXMLName(o);
    }
    
    XmlNode.QName toNodeQName(final Context context, final String s, final boolean b) {
        final XmlNode.Namespace delegate = this.getDefaultNamespace(context).getDelegate();
        if (s != null && s.equals("*")) {
            return XmlNode.QName.create(null, null);
        }
        if (b) {
            return XmlNode.QName.create(XmlNode.Namespace.GLOBAL, s);
        }
        return XmlNode.QName.create(delegate, s);
    }
    
    XMLName toXMLName(final Context context, final Object o) {
        if (o instanceof XMLName) {
            return (XMLName)o;
        }
        if (o instanceof QName) {
            final QName qName = (QName)o;
            return XMLName.formProperty(qName.uri(), qName.localName());
        }
        if (o instanceof String) {
            return this.toXMLNameFromString(context, (String)o);
        }
        if (!(o instanceof Boolean) && !(o instanceof Number) && o != Undefined.instance && o != null) {
            return this.toXMLNameFromString(context, ScriptRuntime.toString(o));
        }
        throw badXMLName(o);
    }
    
    XMLName toXMLNameFromString(final Context context, final String s) {
        return XMLName.create(this.getDefaultNamespaceURI(context), s);
    }
    
    XMLName toXMLNameOrIndex(final Context context, final Object o) {
        if (o instanceof XMLName) {
            return (XMLName)o;
        }
        if (o instanceof String) {
            final String s = (String)o;
            final long testUint32String = ScriptRuntime.testUint32String(s);
            XMLName xmlNameFromString;
            if (testUint32String >= 0L) {
                ScriptRuntime.storeUint32Result(context, testUint32String);
                xmlNameFromString = null;
            }
            else {
                xmlNameFromString = this.toXMLNameFromString(context, s);
            }
            return xmlNameFromString;
        }
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            final long n = (long)doubleValue;
            if (n == doubleValue && 0L <= n && n <= 4294967295L) {
                ScriptRuntime.storeUint32Result(context, n);
                return null;
            }
            throw badXMLName(o);
        }
        else {
            if (o instanceof QName) {
                final QName qName = (QName)o;
                final String uri = qName.uri();
                boolean b2;
                final boolean b = b2 = false;
                if (uri != null) {
                    b2 = b;
                    if (uri.length() == 0) {
                        final long testUint32String2 = ScriptRuntime.testUint32String(uri);
                        b2 = b;
                        if (testUint32String2 >= 0L) {
                            ScriptRuntime.storeUint32Result(context, testUint32String2);
                            b2 = true;
                        }
                    }
                }
                XMLName formProperty;
                if (!b2) {
                    formProperty = XMLName.formProperty(uri, qName.localName());
                }
                else {
                    formProperty = null;
                }
                return formProperty;
            }
            if (o instanceof Boolean || o == Undefined.instance || o == null) {
                throw badXMLName(o);
            }
            final String string = ScriptRuntime.toString(o);
            final long testUint32String3 = ScriptRuntime.testUint32String(string);
            if (testUint32String3 >= 0L) {
                ScriptRuntime.storeUint32Result(context, testUint32String3);
                return null;
            }
            return this.toXMLNameFromString(context, string);
        }
    }
}
