package org.mozilla.javascript;

import java.io.*;

public final class ScriptStackElement implements Serializable
{
    static final long serialVersionUID = -6416688260860477449L;
    public final String fileName;
    public final String functionName;
    public final int lineNumber;
    
    public ScriptStackElement(final String fileName, final String functionName, final int lineNumber) {
        this.fileName = fileName;
        this.functionName = functionName;
        this.lineNumber = lineNumber;
    }
    
    private void appendV8Location(final StringBuilder sb) {
        sb.append(this.fileName);
        if (this.lineNumber > -1) {
            sb.append(':');
            sb.append(this.lineNumber);
        }
    }
    
    public void renderJavaStyle(final StringBuilder sb) {
        sb.append("\tat ");
        sb.append(this.fileName);
        if (this.lineNumber > -1) {
            sb.append(':');
            sb.append(this.lineNumber);
        }
        if (this.functionName != null) {
            sb.append(" (");
            sb.append(this.functionName);
            sb.append(')');
        }
    }
    
    public void renderMozillaStyle(final StringBuilder sb) {
        if (this.functionName != null) {
            sb.append(this.functionName);
            sb.append("()");
        }
        sb.append('@');
        sb.append(this.fileName);
        if (this.lineNumber > -1) {
            sb.append(':');
            sb.append(this.lineNumber);
        }
    }
    
    public void renderV8Style(final StringBuilder sb) {
        sb.append("    at ");
        if (this.functionName != null && !"anonymous".equals(this.functionName) && !"undefined".equals(this.functionName)) {
            sb.append(this.functionName);
            sb.append(" (");
            this.appendV8Location(sb);
            sb.append(')');
            return;
        }
        this.appendV8Location(sb);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.renderMozillaStyle(sb);
        return sb.toString();
    }
}
