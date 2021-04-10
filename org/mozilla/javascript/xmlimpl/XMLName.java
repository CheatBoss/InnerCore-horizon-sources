package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.*;

class XMLName extends Ref
{
    static final long serialVersionUID = 3832176310755686977L;
    private boolean isAttributeName;
    private boolean isDescendants;
    private XmlNode.QName qname;
    private XMLObjectImpl xmlObject;
    
    private XMLName() {
    }
    
    static boolean accept(final Object o) {
        try {
            final String string = ScriptRuntime.toString(o);
            final int length = string.length();
            if (length != 0 && isNCNameStartChar(string.charAt(0))) {
                for (int i = 1; i != length; ++i) {
                    if (!isNCNameChar(string.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        catch (EcmaError ecmaError) {
            if ("TypeError".equals(ecmaError.getName())) {
                return false;
            }
            throw ecmaError;
        }
    }
    
    private void addAttributes(final XMLList list, final XML xml) {
        this.addMatchingAttributes(list, xml);
    }
    
    private void addDescendantAttributes(final XMLList list, final XML xml) {
        if (xml.isElement()) {
            this.addMatchingAttributes(list, xml);
            final XML[] children = xml.getChildren();
            for (int i = 0; i < children.length; ++i) {
                this.addDescendantAttributes(list, children[i]);
            }
        }
    }
    
    private void addDescendantChildren(final XMLList list, final XML xml) {
        if (xml.isElement()) {
            final XML[] children = xml.getChildren();
            for (int i = 0; i < children.length; ++i) {
                if (this.matches(children[i])) {
                    list.addToList(children[i]);
                }
                this.addDescendantChildren(list, children[i]);
            }
        }
    }
    
    static XMLName create(final String s, final String s2) {
        if (s2 == null) {
            throw new IllegalArgumentException();
        }
        final int length = s2.length();
        if (length != 0) {
            final char char1 = s2.charAt(0);
            if (char1 == '*') {
                if (length == 1) {
                    return formStar();
                }
            }
            else if (char1 == '@') {
                final XMLName formProperty = formProperty("", s2.substring(1));
                formProperty.setAttributeName();
                return formProperty;
            }
        }
        return formProperty(s, s2);
    }
    
    @Deprecated
    static XMLName create(final XmlNode.QName qName) {
        return create(qName, false, false);
    }
    
    static XMLName create(final XmlNode.QName qname, final boolean isAttributeName, final boolean isDescendants) {
        final XMLName xmlName = new XMLName();
        xmlName.qname = qname;
        xmlName.isAttributeName = isAttributeName;
        xmlName.isDescendants = isDescendants;
        return xmlName;
    }
    
    static XMLName formProperty(final String s, final String s2) {
        return formProperty(XmlNode.Namespace.create(s), s2);
    }
    
    @Deprecated
    static XMLName formProperty(final XmlNode.Namespace namespace, final String s) {
        String s2 = s;
        if (s != null) {
            s2 = s;
            if (s.equals("*")) {
                s2 = null;
            }
        }
        final XMLName xmlName = new XMLName();
        xmlName.qname = XmlNode.QName.create(namespace, s2);
        return xmlName;
    }
    
    static XMLName formStar() {
        final XMLName xmlName = new XMLName();
        xmlName.qname = XmlNode.QName.create(null, null);
        return xmlName;
    }
    
    private static boolean isNCNameChar(final int n) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        if ((n & 0xFFFFFF80) == 0x0) {
            if (n >= 97) {
                if (n <= 122) {
                    b3 = true;
                }
                return b3;
            }
            if (n >= 65) {
                if (n <= 90) {
                    return true;
                }
                boolean b4 = b;
                if (n == 95) {
                    b4 = true;
                }
                return b4;
            }
            else {
                if (n >= 48) {
                    boolean b5 = b2;
                    if (n <= 57) {
                        b5 = true;
                    }
                    return b5;
                }
                return n == 45 || n == 46;
            }
        }
        else {
            if ((n & 0xFFFFE000) == 0x0) {
                return isNCNameStartChar(n) || n == 183 || (768 <= n && n <= 879);
            }
            return isNCNameStartChar(n) || (8255 <= n && n <= 8256);
        }
    }
    
    private static boolean isNCNameStartChar(final int n) {
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        boolean b4 = false;
        if ((n & 0xFFFFFF80) == 0x0) {
            if (n >= 97) {
                if (n <= 122) {
                    b4 = true;
                }
                return b4;
            }
            if (n >= 65) {
                if (n <= 90) {
                    return true;
                }
                boolean b5 = b;
                if (n == 95) {
                    b5 = true;
                }
                return b5;
            }
        }
        else if ((n & 0xFFFFE000) == 0x0) {
            if ((192 > n || n > 214) && (216 > n || n > 246) && (248 > n || n > 767) && (880 > n || n > 893)) {
                final boolean b6 = b2;
                if (895 > n) {
                    return b6;
                }
            }
            return true;
        }
        if ((8204 > n || n > 8205) && (8304 > n || n > 8591) && (11264 > n || n > 12271) && (12289 > n || n > 55295) && (63744 > n || n > 64975) && (65008 > n || n > 65533)) {
            boolean b7 = b3;
            if (65536 > n) {
                return b7;
            }
            b7 = b3;
            if (n > 983039) {
                return b7;
            }
        }
        return true;
    }
    
    void addDescendants(final XMLList list, final XML xml) {
        if (this.isAttributeName()) {
            this.matchDescendantAttributes(list, xml);
            return;
        }
        this.matchDescendantChildren(list, xml);
    }
    
    void addMatches(final XMLList list, final XML xml) {
        if (this.isDescendants()) {
            this.addDescendants(list, xml);
            return;
        }
        if (this.isAttributeName()) {
            this.addAttributes(list, xml);
            return;
        }
        final XML[] children = xml.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                if (this.matches(children[i])) {
                    list.addToList(children[i]);
                }
            }
        }
        list.setTargets(xml, this.toQname());
    }
    
    void addMatchingAttributes(final XMLList list, final XML xml) {
        if (xml.isElement()) {
            final XML[] attributes = xml.getAttributes();
            for (int i = 0; i < attributes.length; ++i) {
                if (this.matches(attributes[i])) {
                    list.addToList(attributes[i]);
                }
            }
        }
    }
    
    @Override
    public boolean delete(final Context context) {
        if (this.xmlObject == null) {
            return true;
        }
        this.xmlObject.deleteXMLProperty(this);
        return this.xmlObject.hasXMLProperty(this) ^ true;
    }
    
    @Override
    public Object get(final Context context) {
        if (this.xmlObject == null) {
            throw ScriptRuntime.undefReadError(Undefined.instance, this.toString());
        }
        return this.xmlObject.getXMLProperty(this);
    }
    
    XMLList getMyValueOn(final XML xml) {
        final XMLList xmlList = xml.newXMLList();
        this.addMatches(xmlList, xml);
        return xmlList;
    }
    
    @Override
    public boolean has(final Context context) {
        return this.xmlObject != null && this.xmlObject.hasXMLProperty(this);
    }
    
    void initXMLObject(final XMLObjectImpl xmlObject) {
        if (xmlObject == null) {
            throw new IllegalArgumentException();
        }
        if (this.xmlObject != null) {
            throw new IllegalStateException();
        }
        this.xmlObject = xmlObject;
    }
    
    boolean isAttributeName() {
        return this.isAttributeName;
    }
    
    boolean isDescendants() {
        return this.isDescendants;
    }
    
    String localName() {
        if (this.qname.getLocalName() == null) {
            return "*";
        }
        return this.qname.getLocalName();
    }
    
    XMLList matchDescendantAttributes(final XMLList list, final XML xml) {
        list.setTargets(xml, null);
        this.addDescendantAttributes(list, xml);
        return list;
    }
    
    XMLList matchDescendantChildren(final XMLList list, final XML xml) {
        list.setTargets(xml, null);
        this.addDescendantChildren(list, xml);
        return list;
    }
    
    final boolean matches(final XML xml) {
        final XmlNode.QName nodeQname = xml.getNodeQname();
        Object uri = null;
        if (nodeQname.getNamespace() != null) {
            uri = nodeQname.getNamespace().getUri();
        }
        if (!this.isAttributeName) {
            if (this.uri() == null || (xml.isElement() && this.uri().equals(uri))) {
                if (this.localName().equals("*")) {
                    return true;
                }
                if (xml.isElement() && this.localName().equals(nodeQname.getLocalName())) {
                    return true;
                }
            }
            return false;
        }
        if (xml.isAttribute()) {
            if (this.uri() == null || this.uri().equals(uri)) {
                if (this.localName().equals("*")) {
                    return true;
                }
                if (this.localName().equals(nodeQname.getLocalName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    final boolean matchesElement(final XmlNode.QName qName) {
        return (this.uri() == null || this.uri().equals(qName.getNamespace().getUri())) && (this.localName().equals("*") || this.localName().equals(qName.getLocalName()));
    }
    
    final boolean matchesLocalName(final String s) {
        return this.localName().equals("*") || this.localName().equals(s);
    }
    
    @Override
    public Object set(final Context context, final Object o) {
        if (this.xmlObject == null) {
            throw ScriptRuntime.undefWriteError(Undefined.instance, this.toString(), o);
        }
        if (this.isDescendants) {
            throw Kit.codeBug();
        }
        this.xmlObject.putXMLProperty(this, o);
        return o;
    }
    
    void setAttributeName() {
        this.isAttributeName = true;
    }
    
    @Deprecated
    void setIsDescendants() {
        this.isDescendants = true;
    }
    
    void setMyValueOn(final XML xml, final Object o) {
        Object children;
        if (o == null) {
            children = "null";
        }
        else {
            children = o;
            if (o instanceof Undefined) {
                children = "undefined";
            }
        }
        if (this.isAttributeName()) {
            xml.setAttribute(this, children);
            return;
        }
        if (this.uri() == null && this.localName().equals("*")) {
            xml.setChildren(children);
            return;
        }
        XMLObjectImpl xmlFromString2;
        if (children instanceof XMLObjectImpl) {
            XMLObjectImpl xmlFromString;
            final XMLObjectImpl xmlObjectImpl = xmlFromString = (XMLObjectImpl)children;
            if (xmlObjectImpl instanceof XML) {
                xmlFromString = xmlObjectImpl;
                if (((XML)xmlObjectImpl).isAttribute()) {
                    xmlFromString = xml.makeXmlFromString(this, xmlObjectImpl.toString());
                }
            }
            xmlFromString2 = xmlFromString;
            if (xmlFromString instanceof XMLList) {
                int n = 0;
                while (true) {
                    xmlFromString2 = xmlFromString;
                    if (n >= xmlFromString.length()) {
                        break;
                    }
                    final XML item = ((XMLList)xmlFromString).item(n);
                    if (item.isAttribute()) {
                        ((XMLList)xmlFromString).replace(n, xml.makeXmlFromString(this, item.toString()));
                    }
                    ++n;
                }
            }
        }
        else {
            xmlFromString2 = xml.makeXmlFromString(this, ScriptRuntime.toString(children));
        }
        final XMLList propertyList = xml.getPropertyList(this);
        if (propertyList.length() == 0) {
            xml.appendChild(xmlFromString2);
            return;
        }
        for (int i = 1; i < propertyList.length(); ++i) {
            xml.removeChild(propertyList.item(i).childIndex());
        }
        xml.replace(propertyList.item(0).childIndex(), xmlFromString2);
    }
    
    final XmlNode.QName toQname() {
        return this.qname;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.isDescendants) {
            sb.append("..");
        }
        if (this.isAttributeName) {
            sb.append('@');
        }
        if (this.uri() == null) {
            sb.append('*');
            if (this.localName().equals("*")) {
                return sb.toString();
            }
        }
        else {
            sb.append('\"');
            sb.append(this.uri());
            sb.append('\"');
        }
        sb.append(':');
        sb.append(this.localName());
        return sb.toString();
    }
    
    String uri() {
        if (this.qname.getNamespace() == null) {
            return null;
        }
        return this.qname.getNamespace().getUri();
    }
}
