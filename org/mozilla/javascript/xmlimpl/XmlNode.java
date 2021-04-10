package org.mozilla.javascript.xmlimpl;

import java.io.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.mozilla.javascript.*;
import java.util.*;

class XmlNode implements Serializable
{
    private static final boolean DOM_LEVEL_3 = true;
    private static final String USER_DATA_XMLNODE_KEY;
    private static final String XML_NAMESPACES_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
    private static final long serialVersionUID = 1L;
    private Node dom;
    private UserDataHandler events;
    private XML xml;
    
    static {
        USER_DATA_XMLNODE_KEY = XmlNode.class.getName();
    }
    
    private XmlNode() {
        this.events = new XmlNodeUserDataHandler();
    }
    
    private void addNamespaces(final Namespaces namespaces, final Element element) {
        if (element == null) {
            throw new RuntimeException("element must not be null");
        }
        final String uri = this.toUri(element.lookupNamespaceURI(null));
        String uri2 = "";
        if (element.getParentNode() != null) {
            uri2 = this.toUri(element.getParentNode().lookupNamespaceURI(null));
        }
        if (!uri.equals(uri2) || !(element.getParentNode() instanceof Element)) {
            namespaces.declare(Namespace.create("", uri));
        }
        final NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++i) {
            final Attr attr = (Attr)attributes.item(i);
            if (attr.getPrefix() != null && attr.getPrefix().equals("xmlns")) {
                namespaces.declare(Namespace.create(attr.getLocalName(), attr.getValue()));
            }
        }
    }
    
    private static XmlNode copy(final XmlNode xmlNode) {
        return createImpl(xmlNode.dom.cloneNode(true));
    }
    
    static XmlNode createElement(final XmlProcessor xmlProcessor, final String s, final String s2) throws SAXException {
        return createImpl(xmlProcessor.toXml(s, s2));
    }
    
    static XmlNode createElementFromNode(final Node node) {
        Node documentElement = node;
        if (node instanceof Document) {
            documentElement = ((Document)node).getDocumentElement();
        }
        return createImpl(documentElement);
    }
    
    static XmlNode createEmpty(final XmlProcessor xmlProcessor) {
        return createText(xmlProcessor, "");
    }
    
    private static XmlNode createImpl(final Node dom) {
        if (dom instanceof Document) {
            throw new IllegalArgumentException();
        }
        if (getUserData(dom) == null) {
            final XmlNode xmlNode = new XmlNode();
            setUserData(xmlNode.dom = dom, xmlNode);
            return xmlNode;
        }
        return getUserData(dom);
    }
    
    static XmlNode createText(final XmlProcessor xmlProcessor, final String s) {
        return createImpl(xmlProcessor.newDocument().createTextNode(s));
    }
    
    private void declareNamespace(final Element element, final String s, final String s2) {
        if (s.length() > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("xmlns:");
            sb.append(s);
            element.setAttributeNS("http://www.w3.org/2000/xmlns/", sb.toString(), s2);
            return;
        }
        element.setAttribute("xmlns", s2);
    }
    
    private Namespaces getAllNamespaces() {
        final Namespaces namespaces = new Namespaces();
        Node node2;
        final Node node = node2 = this.dom;
        if (node instanceof Attr) {
            node2 = ((Attr)node).getOwnerElement();
        }
        while (node2 != null) {
            if (node2 instanceof Element) {
                this.addNamespaces(namespaces, (Element)node2);
            }
            node2 = node2.getParentNode();
        }
        namespaces.declare(Namespace.create("", ""));
        return namespaces;
    }
    
    private Namespace getDefaultNamespace() {
        String lookupNamespaceURI;
        if (this.dom.lookupNamespaceURI(null) == null) {
            lookupNamespaceURI = "";
        }
        else {
            lookupNamespaceURI = this.dom.lookupNamespaceURI(null);
        }
        return Namespace.create("", lookupNamespaceURI);
    }
    
    private String getExistingPrefixFor(final Namespace namespace) {
        if (this.getDefaultNamespace().getUri().equals(namespace.getUri())) {
            return "";
        }
        return this.dom.lookupPrefix(namespace.getUri());
    }
    
    private Namespace getNodeNamespace() {
        final String namespaceURI = this.dom.getNamespaceURI();
        final String prefix = this.dom.getPrefix();
        String s = namespaceURI;
        if (namespaceURI == null) {
            s = "";
        }
        String s2;
        if ((s2 = prefix) == null) {
            s2 = "";
        }
        return Namespace.create(s2, s);
    }
    
    private static XmlNode getUserData(final Node node) {
        return (XmlNode)node.getUserData(XmlNode.USER_DATA_XMLNODE_KEY);
    }
    
    static XmlNode newElementWithText(final XmlProcessor xmlProcessor, final XmlNode xmlNode, final QName qName, final String s) {
        if (xmlNode instanceof Document) {
            throw new IllegalArgumentException("Cannot use Document node as reference");
        }
        Document document;
        if (xmlNode != null) {
            document = xmlNode.dom.getOwnerDocument();
        }
        else {
            document = xmlProcessor.newDocument();
        }
        final String s2 = null;
        Node dom;
        if (xmlNode != null) {
            dom = xmlNode.dom;
        }
        else {
            dom = null;
        }
        final Namespace namespace = qName.getNamespace();
        String s3;
        String s4;
        if (namespace != null && namespace.getUri().length() != 0) {
            final String uri = namespace.getUri();
            s3 = qName.qualify(dom);
            s4 = uri;
        }
        else {
            s3 = qName.getLocalName();
            s4 = s2;
        }
        final Element elementNS = document.createElementNS(s4, s3);
        if (s != null) {
            elementNS.appendChild(document.createTextNode(s));
        }
        return createImpl(elementNS);
    }
    
    private void setProcessingInstructionName(final String s) {
        final ProcessingInstruction processingInstruction = (ProcessingInstruction)this.dom;
        processingInstruction.getParentNode().replaceChild(processingInstruction, processingInstruction.getOwnerDocument().createProcessingInstruction(s, processingInstruction.getData()));
    }
    
    private static void setUserData(final Node node, final XmlNode xmlNode) {
        node.setUserData(XmlNode.USER_DATA_XMLNODE_KEY, xmlNode, xmlNode.events);
    }
    
    private String toUri(final String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
    
    void addMatchingChildren(final XMLList list, final Filter filter) {
        final NodeList childNodes = this.dom.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            final Node item = childNodes.item(i);
            final XmlNode impl = createImpl(item);
            if (filter.accept(item)) {
                list.addToList(impl);
            }
        }
    }
    
    final XmlNode copy() {
        return copy(this);
    }
    
    String debug() {
        final XmlProcessor xmlProcessor = new XmlProcessor();
        xmlProcessor.setIgnoreComments(false);
        xmlProcessor.setIgnoreProcessingInstructions(false);
        xmlProcessor.setIgnoreWhitespace(false);
        xmlProcessor.setPrettyPrinting(false);
        return xmlProcessor.ecmaToXmlString(this.dom);
    }
    
    void declareNamespace(final String s, final String s2) {
        if (!(this.dom instanceof Element)) {
            throw new IllegalStateException();
        }
        if (this.dom.lookupNamespaceURI(s2) != null && this.dom.lookupNamespaceURI(s2).equals(s)) {
            return;
        }
        this.declareNamespace((Element)this.dom, s, s2);
    }
    
    void deleteMe() {
        if (this.dom instanceof Attr) {
            final Attr attr = (Attr)this.dom;
            attr.getOwnerElement().getAttributes().removeNamedItemNS(attr.getNamespaceURI(), attr.getLocalName());
            return;
        }
        if (this.dom.getParentNode() != null) {
            this.dom.getParentNode().removeChild(this.dom);
        }
    }
    
    String ecmaToXMLString(final XmlProcessor xmlProcessor) {
        if (this.isElementType()) {
            final Element element = (Element)this.dom.cloneNode(true);
            final Namespace[] inScopeNamespaces = this.getInScopeNamespaces();
            for (int i = 0; i < inScopeNamespaces.length; ++i) {
                this.declareNamespace(element, inScopeNamespaces[i].getPrefix(), inScopeNamespaces[i].getUri());
            }
            return xmlProcessor.ecmaToXmlString(element);
        }
        return xmlProcessor.ecmaToXmlString(this.dom);
    }
    
    String ecmaValue() {
        if (this.isTextType()) {
            return ((Text)this.dom).getData();
        }
        if (this.isAttributeType()) {
            return ((Attr)this.dom).getValue();
        }
        if (this.isProcessingInstructionType()) {
            return ((ProcessingInstruction)this.dom).getData();
        }
        if (this.isCommentType()) {
            return ((Comment)this.dom).getNodeValue();
        }
        if (this.isElementType()) {
            throw new RuntimeException("Unimplemented ecmaValue() for elements.");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unimplemented for node ");
        sb.append(this.dom);
        throw new RuntimeException(sb.toString());
    }
    
    String getAttributeValue() {
        return ((Attr)this.dom).getValue();
    }
    
    XmlNode[] getAttributes() {
        final NamedNodeMap attributes = this.dom.getAttributes();
        if (attributes == null) {
            throw new IllegalStateException("Must be element.");
        }
        final XmlNode[] array = new XmlNode[attributes.getLength()];
        for (int i = 0; i < attributes.getLength(); ++i) {
            array[i] = createImpl(attributes.item(i));
        }
        return array;
    }
    
    XmlNode getChild(final int n) {
        return createImpl(this.dom.getChildNodes().item(n));
    }
    
    int getChildCount() {
        return this.dom.getChildNodes().getLength();
    }
    
    int getChildIndex() {
        if (this.isAttributeType()) {
            return -1;
        }
        if (this.parent() == null) {
            return -1;
        }
        final NodeList childNodes = this.dom.getParentNode().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i) == this.dom) {
                return i;
            }
        }
        throw new RuntimeException("Unreachable.");
    }
    
    Namespace[] getInScopeNamespaces() {
        return this.getAllNamespaces().getNamespaces();
    }
    
    XmlNode[] getMatchingChildren(final Filter filter) {
        final ArrayList<XmlNode> list = new ArrayList<XmlNode>();
        final NodeList childNodes = this.dom.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            final Node item = childNodes.item(i);
            if (filter.accept(item)) {
                list.add(createImpl(item));
            }
        }
        return list.toArray(new XmlNode[list.size()]);
    }
    
    Namespace getNamespace() {
        return this.getNodeNamespace();
    }
    
    Namespace getNamespaceDeclaration() {
        if (this.dom.getPrefix() == null) {
            return this.getNamespaceDeclaration("");
        }
        return this.getNamespaceDeclaration(this.dom.getPrefix());
    }
    
    Namespace getNamespaceDeclaration(final String s) {
        if (s.equals("") && this.dom instanceof Attr) {
            return Namespace.create("", "");
        }
        return this.getAllNamespaces().getNamespace(s);
    }
    
    Namespace[] getNamespaceDeclarations() {
        if (this.dom instanceof Element) {
            final Namespaces namespaces = new Namespaces();
            this.addNamespaces(namespaces, (Element)this.dom);
            return namespaces.getNamespaces();
        }
        return new Namespace[0];
    }
    
    final QName getQname() {
        String namespaceURI;
        if (this.dom.getNamespaceURI() == null) {
            namespaceURI = "";
        }
        else {
            namespaceURI = this.dom.getNamespaceURI();
        }
        String prefix;
        if (this.dom.getPrefix() == null) {
            prefix = "";
        }
        else {
            prefix = this.dom.getPrefix();
        }
        return QName.create(namespaceURI, this.dom.getLocalName(), prefix);
    }
    
    XML getXml() {
        return this.xml;
    }
    
    boolean hasChildElement() {
        final NodeList childNodes = this.dom.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeType() == 1) {
                return true;
            }
        }
        return false;
    }
    
    void insertChildAt(final int n, final XmlNode xmlNode) {
        final Node dom = this.dom;
        final Node importNode = dom.getOwnerDocument().importNode(xmlNode.dom, true);
        if (dom.getChildNodes().getLength() < n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("index=");
            sb.append(n);
            sb.append(" length=");
            sb.append(dom.getChildNodes().getLength());
            throw new IllegalArgumentException(sb.toString());
        }
        if (dom.getChildNodes().getLength() == n) {
            dom.appendChild(importNode);
            return;
        }
        dom.insertBefore(importNode, dom.getChildNodes().item(n));
    }
    
    void insertChildrenAt(final int n, final XmlNode[] array) {
        for (int i = 0; i < array.length; ++i) {
            this.insertChildAt(n + i, array[i]);
        }
    }
    
    void invalidateNamespacePrefix() {
        if (!(this.dom instanceof Element)) {
            throw new IllegalStateException();
        }
        final String prefix = this.dom.getPrefix();
        this.renameNode(QName.create(this.dom.getNamespaceURI(), this.dom.getLocalName(), null));
        final NamedNodeMap attributes = this.dom.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++i) {
            if (attributes.item(i).getPrefix().equals(prefix)) {
                createImpl(attributes.item(i)).renameNode(QName.create(attributes.item(i).getNamespaceURI(), attributes.item(i).getLocalName(), null));
            }
        }
    }
    
    final boolean isAttributeType() {
        return this.dom.getNodeType() == 2;
    }
    
    final boolean isCommentType() {
        return this.dom.getNodeType() == 8;
    }
    
    final boolean isElementType() {
        return this.dom.getNodeType() == 1;
    }
    
    final boolean isParentType() {
        return this.isElementType();
    }
    
    final boolean isProcessingInstructionType() {
        return this.dom.getNodeType() == 7;
    }
    
    boolean isSameNode(final XmlNode xmlNode) {
        return this.dom == xmlNode.dom;
    }
    
    final boolean isTextType() {
        return this.dom.getNodeType() == 3 || this.dom.getNodeType() == 4;
    }
    
    void normalize() {
        this.dom.normalize();
    }
    
    XmlNode parent() {
        final Node parentNode = this.dom.getParentNode();
        if (parentNode instanceof Document) {
            return null;
        }
        if (parentNode == null) {
            return null;
        }
        return createImpl(parentNode);
    }
    
    void removeChild(final int n) {
        this.dom.removeChild(this.dom.getChildNodes().item(n));
    }
    
    void removeNamespace(final Namespace namespace) {
        if (namespace.is(this.getNodeNamespace())) {
            return;
        }
        final NamedNodeMap attributes = this.dom.getAttributes();
        for (int i = 0; i < attributes.getLength(); ++i) {
            if (namespace.is(createImpl(attributes.item(i)).getNodeNamespace())) {
                return;
            }
        }
        final String existingPrefix = this.getExistingPrefixFor(namespace);
        if (existingPrefix != null) {
            if (namespace.isUnspecifiedPrefix()) {
                this.declareNamespace(existingPrefix, this.getDefaultNamespace().getUri());
                return;
            }
            if (existingPrefix.equals(namespace.getPrefix())) {
                this.declareNamespace(existingPrefix, this.getDefaultNamespace().getUri());
            }
        }
    }
    
    final void renameNode(final QName qName) {
        this.dom = this.dom.getOwnerDocument().renameNode(this.dom, qName.getNamespace().getUri(), qName.qualify(this.dom));
    }
    
    void replaceWith(final XmlNode xmlNode) {
        Node node2;
        final Node node = node2 = xmlNode.dom;
        if (node.getOwnerDocument() != this.dom.getOwnerDocument()) {
            node2 = this.dom.getOwnerDocument().importNode(node, true);
        }
        this.dom.getParentNode().replaceChild(node2, this.dom);
    }
    
    void setAttribute(final QName qName, final String s) {
        if (!(this.dom instanceof Element)) {
            throw new IllegalStateException("Can only set attribute on elements.");
        }
        qName.setAttribute((Element)this.dom, s);
    }
    
    final void setLocalName(final String processingInstructionName) {
        if (this.dom instanceof ProcessingInstruction) {
            this.setProcessingInstructionName(processingInstructionName);
            return;
        }
        String prefix;
        if ((prefix = this.dom.getPrefix()) == null) {
            prefix = "";
        }
        this.dom = this.dom.getOwnerDocument().renameNode(this.dom, this.dom.getNamespaceURI(), QName.qualify(prefix, processingInstructionName));
    }
    
    void setXml(final XML xml) {
        this.xml = xml;
    }
    
    Node toDomNode() {
        return this.dom;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("XmlNode: type=");
        sb.append(this.dom.getNodeType());
        sb.append(" dom=");
        sb.append(this.dom.toString());
        return sb.toString();
    }
    
    String toXmlString(final XmlProcessor xmlProcessor) {
        return xmlProcessor.ecmaToXmlString(this.dom);
    }
    
    abstract static class Filter
    {
        static final Filter COMMENT;
        static Filter ELEMENT;
        static final Filter TEXT;
        static Filter TRUE;
        
        static {
            COMMENT = new Filter() {
                @Override
                boolean accept(final Node node) {
                    return node.getNodeType() == 8;
                }
            };
            TEXT = new Filter() {
                @Override
                boolean accept(final Node node) {
                    return node.getNodeType() == 3;
                }
            };
            Filter.ELEMENT = new Filter() {
                @Override
                boolean accept(final Node node) {
                    return node.getNodeType() == 1;
                }
            };
            Filter.TRUE = new Filter() {
                @Override
                boolean accept(final Node node) {
                    return true;
                }
            };
        }
        
        static Filter PROCESSING_INSTRUCTION(final XMLName xmlName) {
            return new Filter() {
                @Override
                boolean accept(final Node node) {
                    return node.getNodeType() == 7 && xmlName.matchesLocalName(((ProcessingInstruction)node).getTarget());
                }
            };
        }
        
        abstract boolean accept(final Node p0);
    }
    
    static class InternalList implements Serializable
    {
        private static final long serialVersionUID = -3633151157292048978L;
        private List<XmlNode> list;
        
        InternalList() {
            this.list = new ArrayList<XmlNode>();
        }
        
        private void _add(final XmlNode xmlNode) {
            this.list.add(xmlNode);
        }
        
        void add(final XML xml) {
            this._add(xml.getAnnotation());
        }
        
        void add(final InternalList list) {
            for (int i = 0; i < list.length(); ++i) {
                this._add(list.item(i));
            }
        }
        
        void add(final InternalList list, int i, final int n) {
            while (i < n) {
                this._add(list.item(i));
                ++i;
            }
        }
        
        void add(final XmlNode xmlNode) {
            this._add(xmlNode);
        }
        
        void addToList(final Object o) {
            if (o instanceof Undefined) {
                return;
            }
            if (o instanceof XMLList) {
                final XMLList list = (XMLList)o;
                for (int i = 0; i < list.length(); ++i) {
                    this._add(list.item(i).getAnnotation());
                }
                return;
            }
            if (o instanceof XML) {
                this._add(((XML)o).getAnnotation());
                return;
            }
            if (o instanceof XmlNode) {
                this._add((XmlNode)o);
            }
        }
        
        XmlNode item(final int n) {
            return this.list.get(n);
        }
        
        int length() {
            return this.list.size();
        }
        
        void remove(final int n) {
            this.list.remove(n);
        }
    }
    
    static class Namespace implements Serializable
    {
        static final Namespace GLOBAL;
        private static final long serialVersionUID = 4073904386884677090L;
        private String prefix;
        private String uri;
        
        static {
            GLOBAL = create("", "");
        }
        
        private Namespace() {
        }
        
        static Namespace create(final String uri) {
            final Namespace namespace = new Namespace();
            namespace.uri = uri;
            if (uri == null || uri.length() == 0) {
                namespace.prefix = "";
            }
            return namespace;
        }
        
        static Namespace create(final String prefix, final String uri) {
            if (prefix == null) {
                throw new IllegalArgumentException("Empty string represents default namespace prefix");
            }
            if (uri == null) {
                throw new IllegalArgumentException("Namespace may not lack a URI");
            }
            final Namespace namespace = new Namespace();
            namespace.prefix = prefix;
            namespace.uri = uri;
            return namespace;
        }
        
        private void setPrefix(final String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException();
            }
            this.prefix = prefix;
        }
        
        String getPrefix() {
            return this.prefix;
        }
        
        String getUri() {
            return this.uri;
        }
        
        boolean is(final Namespace namespace) {
            return this.prefix != null && namespace.prefix != null && this.prefix.equals(namespace.prefix) && this.uri.equals(namespace.uri);
        }
        
        boolean isDefault() {
            return this.prefix != null && this.prefix.equals("");
        }
        
        boolean isEmpty() {
            return this.prefix != null && this.prefix.equals("") && this.uri.equals("");
        }
        
        boolean isGlobal() {
            return this.uri != null && this.uri.equals("");
        }
        
        boolean isUnspecifiedPrefix() {
            return this.prefix == null;
        }
        
        @Override
        public String toString() {
            if (this.prefix == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("XmlNode.Namespace [");
                sb.append(this.uri);
                sb.append("]");
                return sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("XmlNode.Namespace [");
            sb2.append(this.prefix);
            sb2.append("{");
            sb2.append(this.uri);
            sb2.append("}]");
            return sb2.toString();
        }
    }
    
    private static class Namespaces
    {
        private Map<String, String> map;
        private Map<String, String> uriToPrefix;
        
        Namespaces() {
            this.map = new HashMap<String, String>();
            this.uriToPrefix = new HashMap<String, String>();
        }
        
        void declare(final Namespace namespace) {
            if (this.map.get(namespace.prefix) == null) {
                this.map.put(namespace.prefix, namespace.uri);
            }
            if (this.uriToPrefix.get(namespace.uri) == null) {
                this.uriToPrefix.put(namespace.uri, namespace.prefix);
            }
        }
        
        Namespace getNamespace(final String s) {
            if (this.map.get(s) == null) {
                return null;
            }
            return Namespace.create(s, this.map.get(s));
        }
        
        Namespace getNamespaceByUri(final String s) {
            if (this.uriToPrefix.get(s) == null) {
                return null;
            }
            return Namespace.create(s, this.uriToPrefix.get(s));
        }
        
        Namespace[] getNamespaces() {
            final ArrayList<Namespace> list = new ArrayList<Namespace>();
            for (final String s : this.map.keySet()) {
                final Namespace create = Namespace.create(s, this.map.get(s));
                if (!create.isEmpty()) {
                    list.add(create);
                }
            }
            return list.toArray(new Namespace[list.size()]);
        }
    }
    
    static class QName implements Serializable
    {
        private static final long serialVersionUID = -6587069811691451077L;
        private String localName;
        private Namespace namespace;
        
        private QName() {
        }
        
        @Deprecated
        static QName create(final String s, final String s2, final String s3) {
            return create(Namespace.create(s3, s), s2);
        }
        
        static QName create(final Namespace namespace, final String localName) {
            if (localName != null && localName.equals("*")) {
                throw new RuntimeException("* is not valid localName");
            }
            final QName qName = new QName();
            qName.namespace = namespace;
            qName.localName = localName;
            return qName;
        }
        
        private boolean equals(final String s, final String s2) {
            return (s == null && s2 == null) || (s != null && s2 != null && s.equals(s2));
        }
        
        private boolean namespacesEqual(final Namespace namespace, final Namespace namespace2) {
            return (namespace == null && namespace2 == null) || (namespace != null && namespace2 != null && this.equals(namespace.getUri(), namespace2.getUri()));
        }
        
        static String qualify(final String s, final String s2) {
            if (s == null) {
                throw new IllegalArgumentException("prefix must not be null");
            }
            if (s.length() > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(":");
                sb.append(s2);
                return sb.toString();
            }
            return s2;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof QName && this.equals((QName)o);
        }
        
        final boolean equals(final QName qName) {
            return this.namespacesEqual(this.namespace, qName.namespace) && this.equals(this.localName, qName.localName);
        }
        
        String getLocalName() {
            return this.localName;
        }
        
        Namespace getNamespace() {
            return this.namespace;
        }
        
        @Override
        public int hashCode() {
            if (this.localName == null) {
                return 0;
            }
            return this.localName.hashCode();
        }
        
        void lookupPrefix(final Node node) {
            if (node == null) {
                throw new IllegalArgumentException("node must not be null");
            }
            final String lookupPrefix = node.lookupPrefix(this.namespace.getUri());
            String s;
            if ((s = lookupPrefix) == null) {
                String lookupNamespaceURI;
                if ((lookupNamespaceURI = node.lookupNamespaceURI(null)) == null) {
                    lookupNamespaceURI = "";
                }
                s = lookupPrefix;
                if (this.namespace.getUri().equals(lookupNamespaceURI)) {
                    s = "";
                }
            }
            int n = 0;
            while (s == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("e4x_");
                sb.append(n);
                final String string = sb.toString();
                if (node.lookupNamespaceURI(string) == null) {
                    s = string;
                    Node parentNode;
                    for (parentNode = node; parentNode.getParentNode() != null && parentNode.getParentNode() instanceof Element; parentNode = parentNode.getParentNode()) {}
                    final Element element = (Element)parentNode;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("xmlns:");
                    sb2.append(s);
                    element.setAttributeNS("http://www.w3.org/2000/xmlns/", sb2.toString(), this.namespace.getUri());
                }
                ++n;
            }
            this.namespace.setPrefix(s);
        }
        
        String qualify(final Node node) {
            if (this.namespace.getPrefix() == null) {
                if (node != null) {
                    this.lookupPrefix(node);
                }
                else if (this.namespace.getUri().equals("")) {
                    this.namespace.setPrefix("");
                }
                else {
                    this.namespace.setPrefix("");
                }
            }
            return qualify(this.namespace.getPrefix(), this.localName);
        }
        
        void setAttribute(final Element element, final String s) {
            if (this.namespace.getPrefix() == null) {
                this.lookupPrefix(element);
            }
            element.setAttributeNS(this.namespace.getUri(), qualify(this.namespace.getPrefix(), this.localName), s);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("XmlNode.QName [");
            sb.append(this.localName);
            sb.append(",");
            sb.append(this.namespace);
            sb.append("]");
            return sb.toString();
        }
    }
    
    static class XmlNodeUserDataHandler implements UserDataHandler, Serializable
    {
        private static final long serialVersionUID = 4666895518900769588L;
        
        @Override
        public void handle(final short n, final String s, final Object o, final Node node, final Node node2) {
        }
    }
}
