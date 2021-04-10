package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.xml.*;
import java.util.*;
import org.mozilla.javascript.*;

class XMLList extends XMLObjectImpl implements Function
{
    static final long serialVersionUID = -4543618751670781135L;
    private XmlNode.InternalList _annos;
    private XMLObjectImpl targetObject;
    private XmlNode.QName targetProperty;
    
    XMLList(final XMLLibImpl xmlLibImpl, final Scriptable scriptable, final XMLObject xmlObject) {
        super(xmlLibImpl, scriptable, xmlObject);
        this.targetObject = null;
        this.targetProperty = null;
        this._annos = new XmlNode.InternalList();
    }
    
    private Object applyOrCall(final boolean b, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        String s;
        if (b) {
            s = "apply";
        }
        else {
            s = "call";
        }
        if (scriptable2 instanceof XMLList && ((XMLList)scriptable2).targetProperty != null) {
            return ScriptRuntime.applyOrCall(b, context, scriptable, scriptable2, array);
        }
        throw ScriptRuntime.typeError1("msg.isnt.function", s);
    }
    
    private XMLList getPropertyList(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        XmlNode.QName qname = null;
        if (!xmlName.isDescendants()) {
            qname = qname;
            if (!xmlName.isAttributeName()) {
                qname = xmlName.toQname();
            }
        }
        xmlList.setTargets(this, qname);
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).getPropertyList(xmlName));
        }
        return xmlList;
    }
    
    private XML getXML(final XmlNode.InternalList list, final int n) {
        if (n >= 0 && n < this.length()) {
            return this.xmlFromNode(list.item(n));
        }
        return null;
    }
    
    private XML getXmlFromAnnotation(final int n) {
        return this.getXML(this._annos, n);
    }
    
    private void insert(final int n, final XML xml) {
        if (n < this.length()) {
            final XmlNode.InternalList annos = new XmlNode.InternalList();
            annos.add(this._annos, 0, n);
            annos.add(xml);
            annos.add(this._annos, n, this.length());
            this._annos = annos;
        }
    }
    
    private void internalRemoveFromList(final int n) {
        this._annos.remove(n);
    }
    
    private void replaceNode(final XML xml, final XML xml2) {
        xml.replaceWith(xml2);
    }
    
    private void setAttribute(final XMLName xmlName, final Object o) {
        for (int i = 0; i < this.length(); ++i) {
            this.getXmlFromAnnotation(i).setAttribute(xmlName, o);
        }
    }
    
    @Override
    void addMatches(final XMLList list, final XMLName xmlName) {
        for (int i = 0; i < this.length(); ++i) {
            this.getXmlFromAnnotation(i).addMatches(list, xmlName);
        }
    }
    
    void addToList(final Object o) {
        this._annos.addToList(o);
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (this.targetProperty == null) {
            throw ScriptRuntime.notFunctionError(this);
        }
        final String localName = this.targetProperty.getLocalName();
        final boolean equals = localName.equals("apply");
        if (equals || localName.equals("call")) {
            return this.applyOrCall(equals, context, scriptable, scriptable2, array);
        }
        if (!(scriptable2 instanceof XMLObject)) {
            throw ScriptRuntime.typeError1("msg.incompat.call", localName);
        }
        final Callable callable = null;
        Scriptable scriptable3 = scriptable2;
        Scriptable extraMethodSource = scriptable2;
        Object property = callable;
        while (extraMethodSource instanceof XMLObject) {
            final XMLObject xmlObject = (XMLObject)extraMethodSource;
            final Object functionProperty = xmlObject.getFunctionProperty(context, localName);
            if (functionProperty != Scriptable.NOT_FOUND) {
                property = functionProperty;
                break;
            }
            extraMethodSource = xmlObject.getExtraMethodSource(context);
            property = functionProperty;
            if (extraMethodSource == null) {
                continue;
            }
            final XMLObject xmlObject2 = (XMLObject)extraMethodSource;
            property = functionProperty;
            scriptable3 = xmlObject2;
            if (extraMethodSource instanceof XMLObject) {
                continue;
            }
            property = ScriptableObject.getProperty(extraMethodSource, localName);
            scriptable3 = xmlObject2;
        }
        if (!(property instanceof Callable)) {
            throw ScriptRuntime.notFunctionError(scriptable3, property, localName);
        }
        return ((Callable)property).call(context, scriptable, scriptable3, array);
    }
    
    @Override
    XMLList child(final int n) {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).child(n));
        }
        return xmlList;
    }
    
    @Override
    XMLList child(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).child(xmlName));
        }
        return xmlList;
    }
    
    @Override
    XMLList children() {
        final ArrayList<XML> list = new ArrayList<XML>();
        final int n = 0;
        for (int i = 0; i < this.length(); ++i) {
            final XML xmlFromAnnotation = this.getXmlFromAnnotation(i);
            if (xmlFromAnnotation != null) {
                final XMLList children = xmlFromAnnotation.children();
                for (int length = children.length(), j = 0; j < length; ++j) {
                    list.add(children.item(j));
                }
            }
        }
        final XMLList xmlList = this.newXMLList();
        for (int size = list.size(), k = n; k < size; ++k) {
            xmlList.addToList(list.get(k));
        }
        return xmlList;
    }
    
    @Override
    XMLList comments() {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).comments());
        }
        return xmlList;
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        throw ScriptRuntime.typeError1("msg.not.ctor", "XMLList");
    }
    
    @Override
    boolean contains(final Object o) {
        for (int i = 0; i < this.length(); ++i) {
            if (this.getXmlFromAnnotation(i).equivalentXml(o)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    XMLObjectImpl copy() {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).copy());
        }
        return xmlList;
    }
    
    @Override
    public void delete(final int n) {
        if (n >= 0 && n < this.length()) {
            this.getXmlFromAnnotation(n).remove();
            this.internalRemoveFromList(n);
        }
    }
    
    @Override
    void deleteXMLProperty(final XMLName xmlName) {
        for (int i = 0; i < this.length(); ++i) {
            final XML xmlFromAnnotation = this.getXmlFromAnnotation(i);
            if (xmlFromAnnotation.isElement()) {
                xmlFromAnnotation.deleteXMLProperty(xmlName);
            }
        }
    }
    
    @Override
    XMLList elements(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).elements(xmlName));
        }
        return xmlList;
    }
    
    @Override
    boolean equivalentXml(final Object o) {
        final boolean b = false;
        if (o instanceof Undefined && this.length() == 0) {
            return true;
        }
        final int length = this.length();
        int n = 0;
        if (length == 1) {
            return this.getXmlFromAnnotation(0).equivalentXml(o);
        }
        boolean b2 = b;
        if (o instanceof XMLList) {
            final XMLList list = (XMLList)o;
            b2 = b;
            if (list.length() == this.length()) {
                final boolean b3 = true;
                while (true) {
                    b2 = b3;
                    if (n >= this.length()) {
                        break;
                    }
                    if (!this.getXmlFromAnnotation(n).equivalentXml(list.getXmlFromAnnotation(n))) {
                        return false;
                    }
                    ++n;
                }
            }
        }
        return b2;
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (n >= 0 && n < this.length()) {
            return this.getXmlFromAnnotation(n);
        }
        return Scriptable.NOT_FOUND;
    }
    
    @Override
    public String getClassName() {
        return "XMLList";
    }
    
    @Override
    public Scriptable getExtraMethodSource(final Context context) {
        if (this.length() == 1) {
            return this.getXmlFromAnnotation(0);
        }
        return null;
    }
    
    @Override
    public Object[] getIds() {
        final boolean prototype = this.isPrototype();
        int i = 0;
        if (prototype) {
            return new Object[0];
        }
        Object[] array;
        for (array = new Object[this.length()]; i < array.length; ++i) {
            array[i] = i;
        }
        return array;
    }
    
    public Object[] getIdsForDebug() {
        return this.getIds();
    }
    
    XmlNode.InternalList getNodeList() {
        return this._annos;
    }
    
    @Override
    XML getXML() {
        if (this.length() == 1) {
            return this.getXmlFromAnnotation(0);
        }
        return null;
    }
    
    @Override
    Object getXMLProperty(final XMLName xmlName) {
        return this.getPropertyList(xmlName);
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return n >= 0 && n < this.length();
    }
    
    @Override
    boolean hasComplexContent() {
        final int length = this.length();
        if (length == 0) {
            return false;
        }
        int n = 0;
        if (length == 1) {
            return this.getXmlFromAnnotation(0).hasComplexContent();
        }
        final boolean b = false;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= length) {
                break;
            }
            if (this.getXmlFromAnnotation(n).isElement()) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
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
        if (this.length() == 0) {
            return true;
        }
        if (this.length() == 1) {
            return this.getXmlFromAnnotation(0).hasSimpleContent();
        }
        for (int i = 0; i < this.length(); ++i) {
            if (this.getXmlFromAnnotation(i).isElement()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    boolean hasXMLProperty(final XMLName xmlName) {
        return this.getPropertyList(xmlName).length() > 0;
    }
    
    XML item(final int n) {
        if (this._annos != null) {
            return this.getXmlFromAnnotation(n);
        }
        return this.createEmptyXML();
    }
    
    @Override
    protected Object jsConstructor(final Context context, final boolean b, final Object[] array) {
        if (array.length == 0) {
            return this.newXMLList();
        }
        final Object o = array[0];
        if (!b && o instanceof XMLList) {
            return o;
        }
        return this.newXMLListFrom(o);
    }
    
    @Override
    int length() {
        int length = 0;
        if (this._annos != null) {
            length = this._annos.length();
        }
        return length;
    }
    
    @Override
    void normalize() {
        for (int i = 0; i < this.length(); ++i) {
            this.getXmlFromAnnotation(i).normalize();
        }
    }
    
    @Override
    Object parent() {
        if (this.length() == 0) {
            return Undefined.instance;
        }
        XML xml = null;
        for (int i = 0; i < this.length(); ++i) {
            final Object parent = this.getXmlFromAnnotation(i).parent();
            if (!(parent instanceof XML)) {
                return Undefined.instance;
            }
            final XML xml2 = (XML)parent;
            if (i == 0) {
                xml = xml2;
            }
            else if (!xml.is(xml2)) {
                return Undefined.instance;
            }
        }
        return xml;
    }
    
    @Override
    XMLList processingInstructions(final XMLName xmlName) {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).processingInstructions(xmlName));
        }
        return xmlList;
    }
    
    @Override
    boolean propertyIsEnumerable(final Object o) {
        final boolean b = o instanceof Integer;
        final boolean b2 = false;
        long testUint32String;
        if (b) {
            testUint32String = (int)o;
        }
        else if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            testUint32String = (long)doubleValue;
            if (testUint32String != doubleValue) {
                return false;
            }
            if (testUint32String == 0L && 1.0 / doubleValue < 0.0) {
                return false;
            }
        }
        else {
            testUint32String = ScriptRuntime.testUint32String(ScriptRuntime.toString(o));
        }
        boolean b3 = b2;
        if (0L <= testUint32String) {
            b3 = b2;
            if (testUint32String < this.length()) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        final Object instance = Undefined.instance;
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
        XMLObject xmlObject = null;
        Label_0117: {
            XMLObject xmlFromJs;
            if (children instanceof XMLObject) {
                xmlFromJs = (XMLObject)children;
            }
            else {
                if (this.targetProperty != null) {
                    if ((xmlObject = this.item(n)) == null) {
                        final XML item = this.item(0);
                        if (item == null) {
                            xmlObject = this.newTextElementXML(null, this.targetProperty, null);
                        }
                        else {
                            xmlObject = item.copy();
                        }
                    }
                    ((XML)xmlObject).setChildren(children);
                    break Label_0117;
                }
                xmlFromJs = this.newXMLFromJs(children.toString());
            }
            xmlObject = xmlFromJs;
        }
        Object o2;
        if (n < this.length()) {
            o2 = this.item(n).parent();
        }
        else if (this.length() == 0) {
            if (this.targetObject != null) {
                o2 = this.targetObject.getXML();
            }
            else {
                o2 = this.parent();
            }
        }
        else {
            o2 = this.parent();
        }
        final boolean b = o2 instanceof XML;
        final int n2 = 1;
        int i = 1;
        if (b) {
            final XML xml = (XML)o2;
            if (n < this.length()) {
                final XML xmlFromAnnotation = this.getXmlFromAnnotation(n);
                if (xmlObject instanceof XML) {
                    this.replaceNode(xmlFromAnnotation, (XML)xmlObject);
                    this.replace(n, xmlFromAnnotation);
                }
                else if (xmlObject instanceof XMLList) {
                    final XMLList list = (XMLList)xmlObject;
                    if (list.length() > 0) {
                        int childIndex = xmlFromAnnotation.childIndex();
                        this.replaceNode(xmlFromAnnotation, list.item(0));
                        this.replace(n, list.item(0));
                        while (i < list.length()) {
                            xml.insertChildAfter(xml.getXmlChild(childIndex), list.item(i));
                            ++childIndex;
                            this.insert(n + i, list.item(i));
                            ++i;
                        }
                    }
                }
            }
            else {
                xml.appendChild(xmlObject);
                this.addToList(xml.getLastXmlChild());
            }
            return;
        }
        if (n < this.length()) {
            final XML xml2 = this.getXML(this._annos, n);
            if (xmlObject instanceof XML) {
                this.replaceNode(xml2, (XML)xmlObject);
                this.replace(n, xml2);
            }
            else if (xmlObject instanceof XMLList) {
                final XMLList list2 = (XMLList)xmlObject;
                if (list2.length() > 0) {
                    this.replaceNode(xml2, list2.item(0));
                    this.replace(n, list2.item(0));
                    for (int j = n2; j < list2.length(); ++j) {
                        this.insert(n + j, list2.item(j));
                    }
                }
            }
            return;
        }
        this.addToList(xmlObject);
    }
    
    @Override
    void putXMLProperty(XMLName formProperty, final Object o) {
        Object o2;
        if (o == null) {
            o2 = "null";
        }
        else {
            o2 = o;
            if (o instanceof Undefined) {
                o2 = "undefined";
            }
        }
        if (this.length() > 1) {
            throw ScriptRuntime.typeError("Assignment to lists with more than one item is not supported");
        }
        if (this.length() == 0) {
            if (this.targetObject != null && this.targetProperty != null && this.targetProperty.getLocalName() != null && this.targetProperty.getLocalName().length() > 0) {
                this.addToList(this.newTextElementXML(null, this.targetProperty, null));
                if (formProperty.isAttributeName()) {
                    this.setAttribute(formProperty, o2);
                }
                else {
                    this.item(0).putXMLProperty(formProperty, o2);
                    this.replace(0, this.item(0));
                }
                formProperty = XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName());
                this.targetObject.putXMLProperty(formProperty, this);
                this.replace(0, this.targetObject.getXML().getLastXmlChild());
                return;
            }
            throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
        }
        else {
            if (formProperty.isAttributeName()) {
                this.setAttribute(formProperty, o2);
                return;
            }
            this.item(0).putXMLProperty(formProperty, o2);
            this.replace(0, this.item(0));
        }
    }
    
    void remove() {
        for (int i = this.length() - 1; i >= 0; --i) {
            final XML xmlFromAnnotation = this.getXmlFromAnnotation(i);
            if (xmlFromAnnotation != null) {
                xmlFromAnnotation.remove();
                this.internalRemoveFromList(i);
            }
        }
    }
    
    void replace(final int n, final XML xml) {
        if (n < this.length()) {
            final XmlNode.InternalList annos = new XmlNode.InternalList();
            annos.add(this._annos, 0, n);
            annos.add(xml);
            annos.add(this._annos, n + 1, this.length());
            this._annos = annos;
        }
    }
    
    void setTargets(final XMLObjectImpl targetObject, final XmlNode.QName targetProperty) {
        this.targetObject = targetObject;
        this.targetProperty = targetProperty;
    }
    
    @Override
    XMLList text() {
        final XMLList xmlList = this.newXMLList();
        for (int i = 0; i < this.length(); ++i) {
            xmlList.addToList(this.getXmlFromAnnotation(i).text());
        }
        return xmlList;
    }
    
    @Override
    String toSource(final int n) {
        return this.toXMLString();
    }
    
    @Override
    public String toString() {
        if (this.hasSimpleContent()) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.length(); ++i) {
                final XML xmlFromAnnotation = this.getXmlFromAnnotation(i);
                if (!xmlFromAnnotation.isComment()) {
                    if (!xmlFromAnnotation.isProcessingInstruction()) {
                        sb.append(xmlFromAnnotation.toString());
                    }
                }
            }
            return sb.toString();
        }
        return this.toXMLString();
    }
    
    @Override
    String toXMLString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length(); ++i) {
            if (this.getProcessor().isPrettyPrinting() && i != 0) {
                sb.append('\n');
            }
            sb.append(this.getXmlFromAnnotation(i).toXMLString());
        }
        return sb.toString();
    }
    
    @Override
    Object valueOf() {
        return this;
    }
}
