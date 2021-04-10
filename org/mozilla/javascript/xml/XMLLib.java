package org.mozilla.javascript.xml;

import org.mozilla.javascript.*;

public abstract class XMLLib
{
    private static final Object XML_LIB_KEY;
    
    static {
        XML_LIB_KEY = new Object();
    }
    
    public static XMLLib extractFromScope(final Scriptable scriptable) {
        final XMLLib fromScopeOrNull = extractFromScopeOrNull(scriptable);
        if (fromScopeOrNull != null) {
            return fromScopeOrNull;
        }
        throw Context.reportRuntimeError(ScriptRuntime.getMessage0("msg.XML.not.available"));
    }
    
    public static XMLLib extractFromScopeOrNull(final Scriptable scriptable) {
        final ScriptableObject libraryScopeOrNull = ScriptRuntime.getLibraryScopeOrNull(scriptable);
        if (libraryScopeOrNull == null) {
            return null;
        }
        ScriptableObject.getProperty(libraryScopeOrNull, "XML");
        return (XMLLib)libraryScopeOrNull.getAssociatedValue(XMLLib.XML_LIB_KEY);
    }
    
    protected final XMLLib bindToScope(final Scriptable scriptable) {
        final ScriptableObject libraryScopeOrNull = ScriptRuntime.getLibraryScopeOrNull(scriptable);
        if (libraryScopeOrNull == null) {
            throw new IllegalStateException();
        }
        return (XMLLib)libraryScopeOrNull.associateValue(XMLLib.XML_LIB_KEY, this);
    }
    
    public abstract String escapeAttributeValue(final Object p0);
    
    public abstract String escapeTextValue(final Object p0);
    
    public int getPrettyIndent() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isIgnoreComments() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isIgnoreProcessingInstructions() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isIgnoreWhitespace() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isPrettyPrinting() {
        throw new UnsupportedOperationException();
    }
    
    public abstract boolean isXMLName(final Context p0, final Object p1);
    
    public abstract Ref nameRef(final Context p0, final Object p1, final Object p2, final Scriptable p3, final int p4);
    
    public abstract Ref nameRef(final Context p0, final Object p1, final Scriptable p2, final int p3);
    
    public void setIgnoreComments(final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public void setIgnoreProcessingInstructions(final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public void setIgnoreWhitespace(final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public void setPrettyIndent(final int n) {
        throw new UnsupportedOperationException();
    }
    
    public void setPrettyPrinting(final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public abstract Object toDefaultXmlNamespace(final Context p0, final Object p1);
    
    public abstract static class Factory
    {
        public static Factory create(final String s) {
            return new Factory() {
                @Override
                public String getImplementationClassName() {
                    return s;
                }
            };
        }
        
        public abstract String getImplementationClassName();
    }
}
