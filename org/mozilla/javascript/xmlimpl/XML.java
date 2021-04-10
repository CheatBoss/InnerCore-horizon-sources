package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.xml.*;
import org.mozilla.javascript.*;
import org.w3c.dom.*;

class XML extends XMLObjectImpl
{
    static final long serialVersionUID = -630969919086449092L;
    private XmlNode node;
    
    XML(final XMLLibImpl xmlLibImpl, final Scriptable scriptable, final XMLObject xmlObject, final XmlNode xmlNode) {
        super(xmlLibImpl, scriptable, xmlObject);
        this.initialize(xmlNode);
    }
    
    private XmlNode.Namespace adapt(final Namespace namespace) {
        if (namespace.prefix() == null) {
            return XmlNode.Namespace.create(namespace.uri());
        }
        return XmlNode.Namespace.create(namespace.prefix(), namespace.uri());
    }
    
    private void addInScopeNamespace(final Namespace namespace) {
        if (!this.isElement()) {
            return;
        }
        if (namespace.prefix() == null) {
            return;
        }
        if (namespace.prefix().length() == 0 && namespace.uri().length() == 0) {
            return;
        }
        if (this.node.getQname().getNamespace().getPrefix().equals(namespace.prefix())) {
            this.node.invalidateNamespacePrefix();
        }
        this.node.declareNamespace(namespace.prefix(), namespace.uri());
    }
    
    private String ecmaToString() {
        if (this.isAttribute() || this.isText()) {
            return this.ecmaValue();
        }
        if (this.hasSimpleContent()) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.node.getChildCount(); ++i) {
                final XmlNode child = this.node.getChild(i);
                if (!child.isProcessingInstructionType() && !child.isCommentType()) {
                    sb.append(new XML(this.getLib(), this.getParentScope(), (XMLObject)this.getPrototype(), child).toString());
                }
            }
            return sb.toString();
        }
        return this.toXMLString();
    }
    
    private String ecmaValue() {
        return this.node.ecmaValue();
    }
    
    private int getChildIndexOf(final XML xml) {
        for (int i = 0; i < this.node.getChildCount(); ++i) {
            if (this.node.getChild(i).isSameNode(xml.node)) {
                return i;
            }
        }
        return -1;
    }
    
    private XmlNode[] getNodesForInsert(final Object o) {
        final boolean b = o instanceof XML;
        int i = 0;
        if (b) {
            return new XmlNode[] { ((XML)o).node };
        }
        if (o instanceof XMLList) {
            final XMLList list = (XMLList)o;
            final XmlNode[] array = new XmlNode[list.length()];
            while (i < list.length()) {
                array[i] = list.item(i).node;
                ++i;
            }
            return array;
        }
        return new XmlNode[] { XmlNode.createText(this.getProcessor(), ScriptRuntime.toString(o)) };
    }
    
    private XML toXML(final XmlNode xmlNode) {
        if (xmlNode.getXml() == null) {
            xmlNode.setXml(this.newXML(xmlNode));
        }
        return xmlNode.getXml();
    }
    
    @Override
    void addMatches(final XMLList list, final XMLName xmlName) {
        xmlName.addMatches(list, this);
    }
    
    XML addNamespace(final Namespace namespace) {
        this.addInScopeNamespace(namespace);
        return this;
    }
    
    XML appendChild(final Object o) {
        if (this.node.isParentType()) {
            this.node.insertChildrenAt(this.node.getChildCount(), this.getNodesForInsert(o));
        }
        return this;
    }
    
    @Override
    XMLList child(final int n) {
        final XMLList xmlList = this.newXMLList();
        xmlList.setTargets(this, null);
        if (n >= 0 && n < this.node.getChildCount()) {
            xmlList.addToList(this.getXmlChild(n));
        }
        return xmlList;
    }
    
    @Override
    XMLList child(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        final XmlNode[] matchingChildren = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
        for (int i = 0; i < matchingChildren.length; ++i) {
            if (xmlName.matchesElement(matchingChildren[i].getQname())) {
                xmlList.addToList(this.toXML(matchingChildren[i]));
            }
        }
        xmlList.setTargets(this, xmlName.toQname());
        return xmlList;
    }
    
    int childIndex() {
        return this.node.getChildIndex();
    }
    
    @Override
    XMLList children() {
        final XMLList xmlList = this.newXMLList();
        xmlList.setTargets(this, XMLName.formStar().toQname());
        final XmlNode[] matchingChildren = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
        for (int i = 0; i < matchingChildren.length; ++i) {
            xmlList.addToList(this.toXML(matchingChildren[i]));
        }
        return xmlList;
    }
    
    @Override
    XMLList comments() {
        final XMLList xmlList = this.newXMLList();
        this.node.addMatchingChildren(xmlList, XmlNode.Filter.COMMENT);
        return xmlList;
    }
    
    @Override
    boolean contains(final Object o) {
        return o instanceof XML && this.equivalentXml(o);
    }
    
    @Override
    XMLObjectImpl copy() {
        return this.newXML(this.node.copy());
    }
    
    @Override
    public void delete(final int n) {
        if (n == 0) {
            this.remove();
        }
    }
    
    @Override
    void deleteXMLProperty(final XMLName xmlName) {
        final XMLList propertyList = this.getPropertyList(xmlName);
        for (int i = 0; i < propertyList.length(); ++i) {
            propertyList.item(i).node.deleteMe();
        }
    }
    
    final String ecmaClass() {
        if (this.node.isTextType()) {
            return "text";
        }
        if (this.node.isAttributeType()) {
            return "attribute";
        }
        if (this.node.isCommentType()) {
            return "comment";
        }
        if (this.node.isProcessingInstructionType()) {
            return "processing-instruction";
        }
        if (this.node.isElementType()) {
            return "element";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized type: ");
        sb.append(this.node);
        throw new RuntimeException(sb.toString());
    }
    
    @Override
    XMLList elements(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        xmlList.setTargets(this, xmlName.toQname());
        final XmlNode[] matchingChildren = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
        for (int i = 0; i < matchingChildren.length; ++i) {
            if (xmlName.matches(this.toXML(matchingChildren[i]))) {
                xmlList.addToList(this.toXML(matchingChildren[i]));
            }
        }
        return xmlList;
    }
    
    @Override
    boolean equivalentXml(final Object o) {
        final boolean b = false;
        boolean equivalentXml = false;
        if (o instanceof XML) {
            return this.node.toXmlString(this.getProcessor()).equals(((XML)o).node.toXmlString(this.getProcessor()));
        }
        if (o instanceof XMLList) {
            final XMLList list = (XMLList)o;
            if (list.length() == 1) {
                equivalentXml = this.equivalentXml(list.getXML());
            }
            return equivalentXml;
        }
        boolean equals = b;
        if (this.hasSimpleContent()) {
            equals = this.toString().equals(ScriptRuntime.toString(o));
        }
        return equals;
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (n == 0) {
            return this;
        }
        return Scriptable.NOT_FOUND;
    }
    
    XmlNode getAnnotation() {
        return this.node;
    }
    
    XML[] getAttributes() {
        final XmlNode[] attributes = this.node.getAttributes();
        final XML[] array = new XML[attributes.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.toXML(attributes[i]);
        }
        return array;
    }
    
    XML[] getChildren() {
        if (!this.isElement()) {
            return null;
        }
        final XmlNode[] matchingChildren = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
        final XML[] array = new XML[matchingChildren.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.toXML(matchingChildren[i]);
        }
        return array;
    }
    
    @Override
    public String getClassName() {
        return "XML";
    }
    
    @Override
    public Scriptable getExtraMethodSource(final Context context) {
        if (this.hasSimpleContent()) {
            return ScriptRuntime.toObjectOrNull(context, this.toString());
        }
        return null;
    }
    
    @Override
    public Object[] getIds() {
        if (this.isPrototype()) {
            return new Object[0];
        }
        return new Object[] { 0 };
    }
    
    XML getLastXmlChild() {
        final int n = this.node.getChildCount() - 1;
        if (n < 0) {
            return null;
        }
        return this.getXmlChild(n);
    }
    
    XmlNode.QName getNodeQname() {
        return this.node.getQname();
    }
    
    XMLList getPropertyList(final XMLName xmlName) {
        return xmlName.getMyValueOn(this);
    }
    
    @Override
    final XML getXML() {
        return this;
    }
    
    @Override
    Object getXMLProperty(final XMLName xmlName) {
        return this.getPropertyList(xmlName);
    }
    
    XML getXmlChild(final int n) {
        final XmlNode child = this.node.getChild(n);
        if (child.getXml() == null) {
            child.setXml(this.newXML(child));
        }
        return child.getXml();
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return n == 0;
    }
    
    @Override
    boolean hasComplexContent() {
        return this.hasSimpleContent() ^ true;
    }
    
    @Override
    boolean hasOwnProperty(final XMLName xmlName) {
        final boolean prototype = this.isPrototype();
        final boolean b = false;
        boolean b2 = false;
        if (prototype) {
            if (this.findPrototypeId(xmlName.localName()) != 0) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.getPropertyList(xmlName).length() > 0) {
            b3 = true;
        }
        return b3;
    }
    
    @Override
    boolean hasSimpleContent() {
        return !this.isComment() && !this.isProcessingInstruction() && (this.isText() || this.node.isAttributeType() || (this.node.hasChildElement() ^ true));
    }
    
    @Override
    boolean hasXMLProperty(final XMLName xmlName) {
        return this.getPropertyList(xmlName).length() > 0;
    }
    
    Namespace[] inScopeNamespaces() {
        return this.createNamespaces(this.node.getInScopeNamespaces());
    }
    
    void initialize(final XmlNode node) {
        (this.node = node).setXml(this);
    }
    
    XML insertChildAfter(final XML xml, final Object o) {
        if (xml == null) {
            this.prependChild(o);
            return this;
        }
        final XmlNode[] nodesForInsert = this.getNodesForInsert(o);
        final int childIndex = this.getChildIndexOf(xml);
        if (childIndex != -1) {
            this.node.insertChildrenAt(childIndex + 1, nodesForInsert);
        }
        return this;
    }
    
    XML insertChildBefore(final XML xml, final Object o) {
        if (xml == null) {
            this.appendChild(o);
            return this;
        }
        final XmlNode[] nodesForInsert = this.getNodesForInsert(o);
        final int childIndex = this.getChildIndexOf(xml);
        if (childIndex != -1) {
            this.node.insertChildrenAt(childIndex, nodesForInsert);
        }
        return this;
    }
    
    boolean is(final XML xml) {
        return this.node.isSameNode(xml.node);
    }
    
    final boolean isAttribute() {
        return this.node.isAttributeType();
    }
    
    final boolean isComment() {
        return this.node.isCommentType();
    }
    
    final boolean isElement() {
        return this.node.isElementType();
    }
    
    final boolean isProcessingInstruction() {
        return this.node.isProcessingInstructionType();
    }
    
    final boolean isText() {
        return this.node.isTextType();
    }
    
    @Override
    protected Object jsConstructor(final Context context, final boolean b, final Object[] array) {
        Object[] array2 = null;
        Label_0033: {
            if (array.length != 0 && array[0] != null) {
                array2 = array;
                if (array[0] != Undefined.instance) {
                    break Label_0033;
                }
            }
            array2 = new Object[] { "" };
        }
        final XML ecmaToXml = this.ecmaToXml(array2[0]);
        if (b) {
            return ecmaToXml.copy();
        }
        return ecmaToXml;
    }
    
    @Override
    int length() {
        return 1;
    }
    
    String localName() {
        if (this.name() == null) {
            return null;
        }
        return this.name().localName();
    }
    
    XML makeXmlFromString(final XMLName xmlName, final String s) {
        try {
            return this.newTextElementXML(this.node, xmlName.toQname(), s);
        }
        catch (Exception ex) {
            throw ScriptRuntime.typeError(ex.getMessage());
        }
    }
    
    QName name() {
        if (this.isText()) {
            return null;
        }
        if (this.isComment()) {
            return null;
        }
        if (this.isProcessingInstruction()) {
            return this.newQName("", this.node.getQname().getLocalName(), null);
        }
        return this.newQName(this.node.getQname());
    }
    
    Namespace namespace(final String s) {
        if (s == null) {
            return this.createNamespace(this.node.getNamespaceDeclaration());
        }
        return this.createNamespace(this.node.getNamespaceDeclaration(s));
    }
    
    Namespace[] namespaceDeclarations() {
        return this.createNamespaces(this.node.getNamespaceDeclarations());
    }
    
    Object nodeKind() {
        return this.ecmaClass();
    }
    
    @Override
    void normalize() {
        this.node.normalize();
    }
    
    @Override
    Object parent() {
        if (this.node.parent() == null) {
            return null;
        }
        return this.newXML(this.node.parent());
    }
    
    XML prependChild(final Object o) {
        if (this.node.isParentType()) {
            this.node.insertChildrenAt(0, this.getNodesForInsert(o));
        }
        return this;
    }
    
    @Override
    XMLList processingInstructions(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        this.node.addMatchingChildren(xmlList, XmlNode.Filter.PROCESSING_INSTRUCTION(xmlName));
        return xmlList;
    }
    
    @Override
    boolean propertyIsEnumerable(final Object o) {
        final boolean b = o instanceof Integer;
        final boolean b2 = false;
        boolean b3 = false;
        if (b) {
            if ((int)o == 0) {
                b3 = true;
            }
            return b3;
        }
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            boolean b4 = b2;
            if (doubleValue == 0.0) {
                b4 = b2;
                if (1.0 / doubleValue > 0.0) {
                    b4 = true;
                }
            }
            return b4;
        }
        return ScriptRuntime.toString(o).equals("0");
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        throw ScriptRuntime.typeError("Assignment to indexed XML is not allowed");
    }
    
    @Override
    void putXMLProperty(final XMLName xmlName, final Object o) {
        if (this.isPrototype()) {
            return;
        }
        xmlName.setMyValueOn(this, o);
    }
    
    void remove() {
        this.node.deleteMe();
    }
    
    void removeChild(final int n) {
        this.node.removeChild(n);
    }
    
    XML removeNamespace(final Namespace namespace) {
        if (!this.isElement()) {
            return this;
        }
        this.node.removeNamespace(this.adapt(namespace));
        return this;
    }
    
    XML replace(final int n, final Object o) {
        final XMLList child = this.child(n);
        if (child.length() > 0) {
            this.insertChildAfter(child.item(0), o);
            this.removeChild(n);
        }
        return this;
    }
    
    XML replace(final XMLName xmlName, final Object o) {
        this.putXMLProperty(xmlName, o);
        return this;
    }
    
    void replaceWith(final XML xml) {
        if (this.node.parent() == null) {
            this.initialize(xml.node);
            return;
        }
        this.node.replaceWith(xml.node);
    }
    
    void setAttribute(final XMLName xmlName, final Object o) {
        if (!this.isElement()) {
            throw new IllegalStateException("Can only set attributes on elements.");
        }
        if (xmlName.uri() == null && xmlName.localName().equals("*")) {
            throw ScriptRuntime.typeError("@* assignment not supported.");
        }
        this.node.setAttribute(xmlName.toQname(), ScriptRuntime.toString(o));
    }
    
    XML setChildren(final Object o) {
        if (!this.isElement()) {
            return this;
        }
        while (this.node.getChildCount() > 0) {
            this.node.removeChild(0);
        }
        this.node.insertChildrenAt(0, this.getNodesForInsert(o));
        return this;
    }
    
    void setLocalName(final String localName) {
        if (this.isText()) {
            return;
        }
        if (this.isComment()) {
            return;
        }
        this.node.setLocalName(localName);
    }
    
    void setName(final QName qName) {
        if (this.isText()) {
            return;
        }
        if (this.isComment()) {
            return;
        }
        if (this.isProcessingInstruction()) {
            this.node.setLocalName(qName.localName());
            return;
        }
        this.node.renameNode(qName.getDelegate());
    }
    
    void setNamespace(final Namespace namespace) {
        if (this.isText() || this.isComment()) {
            return;
        }
        if (this.isProcessingInstruction()) {
            return;
        }
        this.setName(this.newQName(namespace.uri(), this.localName(), namespace.prefix()));
    }
    
    @Override
    XMLList text() {
        final XMLList xmlList = this.newXMLList();
        this.node.addMatchingChildren(xmlList, XmlNode.Filter.TEXT);
        return xmlList;
    }
    
    Node toDomNode() {
        return this.node.toDomNode();
    }
    
    @Override
    String toSource(final int n) {
        return this.toXMLString();
    }
    
    @Override
    public String toString() {
        return this.ecmaToString();
    }
    
    @Override
    String toXMLString() {
        return this.node.ecmaToXMLString(this.getProcessor());
    }
    
    @Override
    Object valueOf() {
        return this;
    }
}
