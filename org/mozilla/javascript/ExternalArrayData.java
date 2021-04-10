package org.mozilla.javascript;

public interface ExternalArrayData
{
    Object getArrayElement(final int p0);
    
    int getArrayLength();
    
    void setArrayElement(final int p0, final Object p1);
}
