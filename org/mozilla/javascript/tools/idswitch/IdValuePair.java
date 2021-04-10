package org.mozilla.javascript.tools.idswitch;

public class IdValuePair
{
    public final String id;
    public final int idLength;
    private int lineNumber;
    public final String value;
    
    public IdValuePair(final String id, final String value) {
        this.idLength = id.length();
        this.id = id;
        this.value = value;
    }
    
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
