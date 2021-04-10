package com.microsoft.xbox.toolkit;

import org.simpleframework.xml.*;
import org.simpleframework.xml.convert.*;
import org.simpleframework.xml.core.*;
import org.simpleframework.xml.strategy.*;
import java.io.*;

public class XMLHelper
{
    private static final int XML_WAIT_TIMEOUT_MS = 1000;
    private static XMLHelper instance;
    private Serializer serializer;
    
    static {
        XMLHelper.instance = new XMLHelper();
    }
    
    private XMLHelper() {
        this.serializer = null;
        this.serializer = (Serializer)new Persister((Strategy)new AnnotationStrategy());
    }
    
    public static XMLHelper instance() {
        return XMLHelper.instance;
    }
    
    public <T> T load(final InputStream inputStream, final Class<T> clazz) throws XLEException {
        if (ThreadManager.UIThread != Thread.currentThread()) {
            BackgroundThreadWaitor.getInstance().waitForReady(1000);
        }
        new TimeMonitor();
        try {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
                return (T)this.serializer.read((Class)clazz, inputStream, false);
            }
            finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        }
        catch (Exception ex) {
            throw new XLEException(9L, ex.toString());
        }
    }
    
    public <T> String save(final T t) throws XLEException {
        new TimeMonitor();
        final StringWriter stringWriter = new StringWriter();
        try {
            this.serializer.write((Object)t, (Writer)stringWriter);
            return stringWriter.toString();
        }
        catch (Exception ex) {
            throw new XLEException(9L, ex.toString());
        }
    }
    
    public <T> void save(final T t, final OutputStream outputStream) throws XLEException {
        new TimeMonitor();
        try {
            this.serializer.write((Object)t, outputStream);
        }
        catch (Exception ex) {
            throw new XLEException(9L, ex.toString());
        }
    }
}
