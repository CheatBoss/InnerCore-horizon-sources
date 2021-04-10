package com.zhekasmirnov.horizon.compiler.util;

import java.io.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XMLParser
{
    private static final String TAG = "XMLParser";
    
    public String getXmlFromFile(final String filePath) {
        final File f = new File(filePath);
        if (!f.exists()) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        try {
            final FileInputStream fin = new FileInputStream(f);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        }
        catch (IOException e) {
            System.err.println("XMLParser getRepoXmlFromFile() IO error " + e);
            return null;
        }
        return sb.toString();
    }
    
    public Document getDomElement(final String xml) {
        Document doc = null;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        }
        catch (ParserConfigurationException e) {
            System.err.println("XMLParser Error: " + e.getMessage());
            return null;
        }
        catch (SAXException e2) {
            System.err.println("XMLParser Error: " + e2.getMessage());
            return null;
        }
        catch (IOException e3) {
            System.err.println("XMLParser Error: " + e3.getMessage());
            return null;
        }
        return doc;
    }
    
    public final String getElementValue(final Node elem) {
        if (elem != null && elem.hasChildNodes()) {
            for (Node child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeType() == 3) {
                    return child.getNodeValue();
                }
            }
        }
        return "";
    }
    
    public String getValue(final Element item, final String str) {
        final NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}
