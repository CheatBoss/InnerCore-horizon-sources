package org.reflections.serializers;

import java.util.*;
import org.reflections.*;
import org.dom4j.*;
import java.lang.reflect.*;
import org.reflections.util.*;
import org.dom4j.io.*;
import java.io.*;

public class XmlSerializer implements Serializer
{
    private Document createDocument(final Reflections reflections) {
        final Store store = reflections.getStore();
        final Document document = DocumentFactory.getInstance().createDocument();
        final Element addElement = document.addElement("Reflections");
        for (final String s : store.keySet()) {
            final Element addElement2 = addElement.addElement(s);
            for (final String text : store.get(s).keySet()) {
                final Element addElement3 = addElement2.addElement("entry");
                addElement3.addElement("key").setText(text);
                final Element addElement4 = addElement3.addElement("values");
                final Iterator iterator3 = store.get(s).get((Object)text).iterator();
                while (iterator3.hasNext()) {
                    addElement4.addElement("value").setText((String)iterator3.next());
                }
            }
        }
        return document;
    }
    
    @Override
    public Reflections read(final InputStream inputStream) {
        Reflections reflections;
        try {
            final Constructor<Reflections> declaredConstructor = Reflections.class.getDeclaredConstructor((Class<?>[])new Class[0]);
            declaredConstructor.setAccessible(true);
            reflections = declaredConstructor.newInstance(new Object[0]);
        }
        catch (Exception ex2) {
            reflections = new Reflections(new ConfigurationBuilder());
        }
        try {
            for (final Element element : new SAXReader().read(inputStream).getRootElement().elements()) {
                for (final Element element2 : element.elements()) {
                    final Element element3 = element2.element("key");
                    final Iterator iterator3 = element2.element("values").elements().iterator();
                    while (iterator3.hasNext()) {
                        reflections.getStore().getOrCreate(element.getName()).put((Object)element3.getText(), (Object)iterator3.next().getText());
                    }
                }
            }
            return reflections;
        }
        catch (Throwable t) {
            throw new RuntimeException("Could not read. Make sure relevant dependencies exist on classpath.", t);
        }
        catch (DocumentException ex) {
            throw new ReflectionsException("could not read.", (Throwable)ex);
        }
    }
    
    @Override
    public File save(final Reflections reflections, final String s) {
        final File prepareFile = Utils.prepareFile(s);
        try {
            final Document document = this.createDocument(reflections);
            final XMLWriter xmlWriter = new XMLWriter((OutputStream)new FileOutputStream(prepareFile), OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
            return prepareFile;
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not save to file ");
            sb.append(s);
            sb.append(". Make sure relevant dependencies exist on classpath.");
            throw new RuntimeException(sb.toString(), t);
        }
        catch (IOException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("could not save to file ");
            sb2.append(s);
            throw new ReflectionsException(sb2.toString(), ex);
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        final Document document = this.createDocument(reflections);
        try {
            final StringWriter stringWriter = new StringWriter();
            final XMLWriter xmlWriter = new XMLWriter((Writer)stringWriter, OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
            return stringWriter.toString();
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}
