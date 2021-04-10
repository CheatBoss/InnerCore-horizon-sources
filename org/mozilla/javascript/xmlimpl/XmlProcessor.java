package org.mozilla.javascript.xmlimpl;

import java.util.concurrent.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.*;
import org.mozilla.javascript.*;

class XmlProcessor implements Serializable
{
    private static final long serialVersionUID = 6903514433204808713L;
    private transient LinkedBlockingDeque<DocumentBuilder> documentBuilderPool;
    private transient DocumentBuilderFactory dom;
    private RhinoSAXErrorHandler errorHandler;
    private boolean ignoreComments;
    private boolean ignoreProcessingInstructions;
    private boolean ignoreWhitespace;
    private int prettyIndent;
    private boolean prettyPrint;
    private transient TransformerFactory xform;
    
    XmlProcessor() {
        this.errorHandler = new RhinoSAXErrorHandler();
        this.setDefault();
        (this.dom = DocumentBuilderFactory.newInstance()).setNamespaceAware(true);
        this.dom.setIgnoringComments(false);
        this.xform = TransformerFactory.newInstance();
        this.documentBuilderPool = new LinkedBlockingDeque<DocumentBuilder>(Runtime.getRuntime().availableProcessors() * 2);
    }
    
    private void addCommentsTo(final List<Node> list, final Node node) {
        if (node instanceof Comment) {
            list.add(node);
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
                this.addProcessingInstructionsTo(list, node.getChildNodes().item(i));
            }
        }
    }
    
    private void addProcessingInstructionsTo(final List<Node> list, final Node node) {
        if (node instanceof ProcessingInstruction) {
            list.add(node);
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
                this.addProcessingInstructionsTo(list, node.getChildNodes().item(i));
            }
        }
    }
    
    private void addTextNodesToRemoveAndTrim(final List<Node> list, final Node node) {
        if (node instanceof Text) {
            final Text text = (Text)node;
            if (!false) {
                text.setData(text.getData().trim());
            }
            else if (text.getData().trim().length() == 0) {
                text.setData("");
            }
            if (text.getData().length() == 0) {
                list.add(node);
            }
        }
        if (node.getChildNodes() != null) {
            for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
                this.addTextNodesToRemoveAndTrim(list, node.getChildNodes().item(i));
            }
        }
    }
    
    private void beautifyElement(final Element element, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        final int n2 = 0;
        for (int i = 0; i < n; ++i) {
            sb.append(' ');
        }
        final String string = sb.toString();
        for (int j = 0; j < this.prettyIndent; ++j) {
            sb.append(' ');
        }
        final String string2 = sb.toString();
        final ArrayList<Node> list = new ArrayList<Node>();
        boolean b = false;
        for (int k = 0; k < element.getChildNodes().getLength(); ++k) {
            if (k == 1) {
                b = true;
            }
            if (element.getChildNodes().item(k) instanceof Text) {
                list.add(element.getChildNodes().item(k));
            }
            else {
                b = true;
                list.add(element.getChildNodes().item(k));
            }
        }
        if (b) {
            for (int l = 0; l < list.size(); ++l) {
                element.insertBefore(element.getOwnerDocument().createTextNode(string2), list.get(l));
            }
        }
        final NodeList childNodes = element.getChildNodes();
        final ArrayList<Element> list2 = new ArrayList<Element>();
        for (int n3 = n2; n3 < childNodes.getLength(); ++n3) {
            if (childNodes.item(n3) instanceof Element) {
                list2.add((Element)childNodes.item(n3));
            }
        }
        final Iterator<Element> iterator = list2.iterator();
        while (iterator.hasNext()) {
            this.beautifyElement(iterator.next(), this.prettyIndent + n);
        }
        if (b) {
            element.appendChild(element.getOwnerDocument().createTextNode(string));
        }
    }
    
    private String elementToXmlString(Element element) {
        element = (Element)element.cloneNode(true);
        if (this.prettyPrint) {
            this.beautifyElement(element, 0);
        }
        return this.toString(element);
    }
    
    private String escapeElementValue(final String s) {
        return this.escapeTextValue(s);
    }
    
    private DocumentBuilder getDocumentBuilderFromPool() throws ParserConfigurationException {
        DocumentBuilder documentBuilder;
        if ((documentBuilder = this.documentBuilderPool.pollFirst()) == null) {
            documentBuilder = this.getDomFactory().newDocumentBuilder();
        }
        documentBuilder.setErrorHandler(this.errorHandler);
        return documentBuilder;
    }
    
    private DocumentBuilderFactory getDomFactory() {
        return this.dom;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        (this.dom = DocumentBuilderFactory.newInstance()).setNamespaceAware(true);
        this.dom.setIgnoringComments(false);
        this.xform = TransformerFactory.newInstance();
        this.documentBuilderPool = new LinkedBlockingDeque<DocumentBuilder>(Runtime.getRuntime().availableProcessors() * 2);
    }
    
    private void returnDocumentBuilderToPool(final DocumentBuilder documentBuilder) {
        try {
            documentBuilder.reset();
            this.documentBuilderPool.offerFirst(documentBuilder);
        }
        catch (UnsupportedOperationException ex) {}
    }
    
    private String toString(final Node node) {
        final DOMSource domSource = new DOMSource(node);
        final StringWriter stringWriter = new StringWriter();
        final StreamResult streamResult = new StreamResult(stringWriter);
        try {
            final Transformer transformer = this.xform.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("indent", "no");
            transformer.setOutputProperty("method", "xml");
            transformer.transform(domSource, streamResult);
            return this.toXmlNewlines(stringWriter.toString());
        }
        catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
        catch (TransformerConfigurationException ex2) {
            throw new RuntimeException(ex2);
        }
    }
    
    private String toXmlNewlines(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '\r') {
                if (s.charAt(i + 1) != '\n') {
                    sb.append('\n');
                }
            }
            else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
    
    final String ecmaToXmlString(final Node node) {
        final StringBuilder sb = new StringBuilder();
        if (this.prettyPrint) {
            for (int i = 0; i < 0; ++i) {
                sb.append(' ');
            }
        }
        if (node instanceof Text) {
            String s = ((Text)node).getData();
            if (this.prettyPrint) {
                s = s.trim();
            }
            sb.append(this.escapeElementValue(s));
            return sb.toString();
        }
        if (node instanceof Attr) {
            sb.append(this.escapeAttributeValue(((Attr)node).getValue()));
            return sb.toString();
        }
        if (node instanceof Comment) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("<!--");
            sb2.append(((Comment)node).getNodeValue());
            sb2.append("-->");
            sb.append(sb2.toString());
            return sb.toString();
        }
        if (node instanceof ProcessingInstruction) {
            final ProcessingInstruction processingInstruction = (ProcessingInstruction)node;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("<?");
            sb3.append(processingInstruction.getTarget());
            sb3.append(" ");
            sb3.append(processingInstruction.getData());
            sb3.append("?>");
            sb.append(sb3.toString());
            return sb.toString();
        }
        sb.append(this.elementToXmlString((Element)node));
        return sb.toString();
    }
    
    String escapeAttributeValue(final Object o) {
        final String string = ScriptRuntime.toString(o);
        if (string.length() == 0) {
            return "";
        }
        final Element element = this.newDocument().createElement("a");
        element.setAttribute("b", string);
        final String string2 = this.toString(element);
        return string2.substring(string2.indexOf(34) + 1, string2.lastIndexOf(34));
    }
    
    String escapeTextValue(final Object o) {
        if (o instanceof XMLObjectImpl) {
            return ((XMLObjectImpl)o).toXMLString();
        }
        final String string = ScriptRuntime.toString(o);
        if (string.length() == 0) {
            return string;
        }
        final Element element = this.newDocument().createElement("a");
        element.setTextContent(string);
        final String string2 = this.toString(element);
        final int n = string2.indexOf(62) + 1;
        final int lastIndex = string2.lastIndexOf(60);
        if (n < lastIndex) {
            return string2.substring(n, lastIndex);
        }
        return "";
    }
    
    final int getPrettyIndent() {
        return this.prettyIndent;
    }
    
    final boolean isIgnoreComments() {
        return this.ignoreComments;
    }
    
    final boolean isIgnoreProcessingInstructions() {
        return this.ignoreProcessingInstructions;
    }
    
    final boolean isIgnoreWhitespace() {
        return this.ignoreWhitespace;
    }
    
    final boolean isPrettyPrinting() {
        return this.prettyPrint;
    }
    
    Document newDocument() {
        DocumentBuilder documentBuilderFromPool = null;
        try {
            try {
                final DocumentBuilder documentBuilder = documentBuilderFromPool = this.getDocumentBuilderFromPool();
                final Document document = documentBuilder.newDocument();
                if (documentBuilder != null) {
                    this.returnDocumentBuilderToPool(documentBuilder);
                }
                return document;
            }
            finally {
                if (documentBuilderFromPool != null) {
                    this.returnDocumentBuilderToPool(documentBuilderFromPool);
                }
            }
        }
        catch (ParserConfigurationException ex) {}
    }
    
    final void setDefault() {
        this.setIgnoreComments(true);
        this.setIgnoreProcessingInstructions(true);
        this.setIgnoreWhitespace(true);
        this.setPrettyPrinting(true);
        this.setPrettyIndent(2);
    }
    
    final void setIgnoreComments(final boolean ignoreComments) {
        this.ignoreComments = ignoreComments;
    }
    
    final void setIgnoreProcessingInstructions(final boolean ignoreProcessingInstructions) {
        this.ignoreProcessingInstructions = ignoreProcessingInstructions;
    }
    
    final void setIgnoreWhitespace(final boolean ignoreWhitespace) {
        this.ignoreWhitespace = ignoreWhitespace;
    }
    
    final void setPrettyIndent(final int prettyIndent) {
        this.prettyIndent = prettyIndent;
    }
    
    final void setPrettyPrinting(final boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }
    
    final Node toXml(final String s, String string) throws SAXException {
        DocumentBuilder documentBuilderFromPool;
        final DocumentBuilder documentBuilder = documentBuilderFromPool = null;
        try {
            try {
                final StringBuilder sb = new StringBuilder();
                documentBuilderFromPool = documentBuilder;
                sb.append("<parent xmlns=\"");
                documentBuilderFromPool = documentBuilder;
                sb.append(s);
                documentBuilderFromPool = documentBuilder;
                sb.append("\">");
                documentBuilderFromPool = documentBuilder;
                sb.append(string);
                documentBuilderFromPool = documentBuilder;
                sb.append("</parent>");
                documentBuilderFromPool = documentBuilder;
                string = sb.toString();
                documentBuilderFromPool = documentBuilder;
                final DocumentBuilder documentBuilder2 = documentBuilderFromPool = this.getDocumentBuilderFromPool();
                final Document parse = documentBuilder2.parse(new InputSource(new StringReader(string)));
                documentBuilderFromPool = documentBuilder2;
                if (this.ignoreProcessingInstructions) {
                    documentBuilderFromPool = documentBuilder2;
                    final ArrayList<Node> list = new ArrayList<Node>();
                    documentBuilderFromPool = documentBuilder2;
                    this.addProcessingInstructionsTo(list, parse);
                    documentBuilderFromPool = documentBuilder2;
                    final Iterator<Object> iterator = list.iterator();
                    while (true) {
                        documentBuilderFromPool = documentBuilder2;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        documentBuilderFromPool = documentBuilder2;
                        final Node node = iterator.next();
                        documentBuilderFromPool = documentBuilder2;
                        node.getParentNode().removeChild(node);
                    }
                }
                documentBuilderFromPool = documentBuilder2;
                if (this.ignoreComments) {
                    documentBuilderFromPool = documentBuilder2;
                    final ArrayList<Node> list2 = new ArrayList<Node>();
                    documentBuilderFromPool = documentBuilder2;
                    this.addCommentsTo(list2, parse);
                    documentBuilderFromPool = documentBuilder2;
                    final Iterator<Object> iterator2 = list2.iterator();
                    while (true) {
                        documentBuilderFromPool = documentBuilder2;
                        if (!iterator2.hasNext()) {
                            break;
                        }
                        documentBuilderFromPool = documentBuilder2;
                        final Node node2 = iterator2.next();
                        documentBuilderFromPool = documentBuilder2;
                        node2.getParentNode().removeChild(node2);
                    }
                }
                documentBuilderFromPool = documentBuilder2;
                if (this.ignoreWhitespace) {
                    documentBuilderFromPool = documentBuilder2;
                    final ArrayList<Node> list3 = new ArrayList<Node>();
                    documentBuilderFromPool = documentBuilder2;
                    this.addTextNodesToRemoveAndTrim(list3, parse);
                    documentBuilderFromPool = documentBuilder2;
                    final Iterator<Object> iterator3 = list3.iterator();
                    while (true) {
                        documentBuilderFromPool = documentBuilder2;
                        if (!iterator3.hasNext()) {
                            break;
                        }
                        documentBuilderFromPool = documentBuilder2;
                        final Node node3 = iterator3.next();
                        documentBuilderFromPool = documentBuilder2;
                        node3.getParentNode().removeChild(node3);
                    }
                }
                documentBuilderFromPool = documentBuilder2;
                final NodeList childNodes = parse.getDocumentElement().getChildNodes();
                documentBuilderFromPool = documentBuilder2;
                if (childNodes.getLength() > 1) {
                    documentBuilderFromPool = documentBuilder2;
                    throw ScriptRuntime.constructError("SyntaxError", "XML objects may contain at most one node.");
                }
                documentBuilderFromPool = documentBuilder2;
                if (childNodes.getLength() == 0) {
                    documentBuilderFromPool = documentBuilder2;
                    final Text textNode = parse.createTextNode("");
                    if (documentBuilder2 != null) {
                        this.returnDocumentBuilderToPool(documentBuilder2);
                    }
                    return textNode;
                }
                documentBuilderFromPool = documentBuilder2;
                final Node item = childNodes.item(0);
                documentBuilderFromPool = documentBuilder2;
                parse.getDocumentElement().removeChild(item);
                if (documentBuilder2 != null) {
                    this.returnDocumentBuilderToPool(documentBuilder2);
                }
                return item;
            }
            finally {
                if (documentBuilderFromPool != null) {
                    this.returnDocumentBuilderToPool(documentBuilderFromPool);
                }
            }
        }
        catch (ParserConfigurationException ex) {}
        catch (IOException ex2) {}
    }
    
    private static class RhinoSAXErrorHandler implements ErrorHandler, Serializable
    {
        private static final long serialVersionUID = 6918417235413084055L;
        
        private void throwError(final SAXParseException ex) {
            throw ScriptRuntime.constructError("TypeError", ex.getMessage(), ex.getLineNumber() - 1);
        }
        
        @Override
        public void error(final SAXParseException ex) {
            this.throwError(ex);
        }
        
        @Override
        public void fatalError(final SAXParseException ex) {
            this.throwError(ex);
        }
        
        @Override
        public void warning(final SAXParseException ex) {
            Context.reportWarning(ex.getMessage());
        }
    }
}
