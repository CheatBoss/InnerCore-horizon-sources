package org.mozilla.javascript.ast;

public class ParseProblem
{
    private int length;
    private String message;
    private int offset;
    private String sourceName;
    private Type type;
    
    public ParseProblem(final Type type, final String message, final String sourceName, final int fileOffset, final int length) {
        this.setType(type);
        this.setMessage(message);
        this.setSourceName(sourceName);
        this.setFileOffset(fileOffset);
        this.setLength(length);
    }
    
    public int getFileOffset() {
        return this.offset;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getSourceName() {
        return this.sourceName;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setFileOffset(final int offset) {
        this.offset = offset;
    }
    
    public void setLength(final int length) {
        this.length = length;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public void setSourceName(final String sourceName) {
        this.sourceName = sourceName;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(200);
        sb.append(this.sourceName);
        sb.append(":");
        sb.append("offset=");
        sb.append(this.offset);
        sb.append(",");
        sb.append("length=");
        sb.append(this.length);
        sb.append(",");
        String s;
        if (this.type == Type.Error) {
            s = "error: ";
        }
        else {
            s = "warning: ";
        }
        sb.append(s);
        sb.append(this.message);
        return sb.toString();
    }
    
    public enum Type
    {
        Error, 
        Warning;
    }
}
