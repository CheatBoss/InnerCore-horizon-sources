package org.mozilla.javascript;

import java.util.*;
import java.util.regex.*;
import java.io.*;

public abstract class RhinoException extends RuntimeException
{
    private static final Pattern JAVA_STACK_PATTERN;
    static final long serialVersionUID = 1883500631321581169L;
    private static StackStyle stackStyle;
    private int columnNumber;
    int[] interpreterLineData;
    Object interpreterStackInfo;
    private int lineNumber;
    private String lineSource;
    private String sourceName;
    
    static {
        JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");
        RhinoException.stackStyle = StackStyle.RHINO;
        final String property = System.getProperty("rhino.stack.style");
        if (property != null) {
            if ("Rhino".equalsIgnoreCase(property)) {
                RhinoException.stackStyle = StackStyle.RHINO;
                return;
            }
            if ("Mozilla".equalsIgnoreCase(property)) {
                RhinoException.stackStyle = StackStyle.MOZILLA;
                return;
            }
            if ("V8".equalsIgnoreCase(property)) {
                RhinoException.stackStyle = StackStyle.V8;
            }
        }
    }
    
    RhinoException() {
        final Evaluator interpreter = Context.createInterpreter();
        if (interpreter != null) {
            interpreter.captureStackInfo(this);
        }
    }
    
    RhinoException(final String s) {
        super(s);
        final Evaluator interpreter = Context.createInterpreter();
        if (interpreter != null) {
            interpreter.captureStackInfo(this);
        }
    }
    
    static String formatStackTrace(final ScriptStackElement[] array, final String s) {
        final StringBuilder sb = new StringBuilder();
        final String systemProperty = SecurityUtilities.getSystemProperty("line.separator");
        if (RhinoException.stackStyle == StackStyle.V8 && !"null".equals(s)) {
            sb.append(s);
            sb.append(systemProperty);
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final ScriptStackElement scriptStackElement = array[i];
            switch (RhinoException.stackStyle) {
                case RHINO: {
                    scriptStackElement.renderJavaStyle(sb);
                    break;
                }
                case V8: {
                    scriptStackElement.renderV8Style(sb);
                    break;
                }
                case MOZILLA: {
                    scriptStackElement.renderMozillaStyle(sb);
                    break;
                }
            }
            sb.append(systemProperty);
        }
        return sb.toString();
    }
    
    private String generateStackTrace() {
        final CharArrayWriter charArrayWriter = new CharArrayWriter();
        super.printStackTrace(new PrintWriter(charArrayWriter));
        final String string = charArrayWriter.toString();
        final Evaluator interpreter = Context.createInterpreter();
        if (interpreter != null) {
            return interpreter.getPatchedStack(this, string);
        }
        return null;
    }
    
    public static StackStyle getStackStyle() {
        return RhinoException.stackStyle;
    }
    
    public static void setStackStyle(final StackStyle stackStyle) {
        RhinoException.stackStyle = stackStyle;
    }
    
    public static void useMozillaStackStyle(final boolean b) {
        StackStyle stackStyle;
        if (b) {
            stackStyle = StackStyle.MOZILLA;
        }
        else {
            stackStyle = StackStyle.RHINO;
        }
        RhinoException.stackStyle = stackStyle;
    }
    
    public static boolean usesMozillaStackStyle() {
        return RhinoException.stackStyle == StackStyle.MOZILLA;
    }
    
    public final int columnNumber() {
        return this.columnNumber;
    }
    
    public String details() {
        return super.getMessage();
    }
    
    @Override
    public final String getMessage() {
        final String details = this.details();
        if (this.sourceName == null) {
            return details;
        }
        if (this.lineNumber <= 0) {
            return details;
        }
        final StringBuilder sb = new StringBuilder(details);
        sb.append(" (");
        if (this.sourceName != null) {
            sb.append(this.sourceName);
        }
        if (this.lineNumber > 0) {
            sb.append('#');
            sb.append(this.lineNumber);
        }
        sb.append(')');
        return sb.toString();
    }
    
    public ScriptStackElement[] getScriptStack() {
        return this.getScriptStack(-1, null);
    }
    
    public ScriptStackElement[] getScriptStack(final int n, final String s) {
        final ArrayList<ScriptStackElement> list = new ArrayList<ScriptStackElement>();
        ScriptStackElement[][] scriptStackElements;
        final ScriptStackElement[][] array = scriptStackElements = null;
        if (this.interpreterStackInfo != null) {
            final Evaluator interpreter = Context.createInterpreter();
            scriptStackElements = array;
            if (interpreter instanceof Interpreter) {
                scriptStackElements = ((Interpreter)interpreter).getScriptStackElements(this);
            }
        }
        final StackTraceElement[] stackTrace = this.getStackTrace();
        int n2;
        if (s == null) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int length = stackTrace.length;
        int n3 = 0;
        int n4 = 0;
        int n6;
        int n7;
        int n8;
        for (int i = 0; i < length; ++i, n4 = n6, n3 = n7, n2 = n8) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            final String fileName = stackTraceElement.getFileName();
            if (stackTraceElement.getMethodName().startsWith("_c_") && stackTraceElement.getLineNumber() > -1 && fileName != null && !fileName.endsWith(".java")) {
                final String methodName = stackTraceElement.getMethodName();
                final Matcher matcher = RhinoException.JAVA_STACK_PATTERN.matcher(methodName);
                String group;
                if (!"_c_script_0".equals(methodName) && matcher.find()) {
                    group = matcher.group(1);
                }
                else {
                    group = null;
                }
                Label_0268: {
                    int n5 = 0;
                    Label_0212: {
                        if (n2 == 0 && s.equals(group)) {
                            n5 = 1;
                        }
                        else {
                            if (n2 != 0) {
                                if (n >= 0) {
                                    n5 = n2;
                                    if (n3 >= n) {
                                        break Label_0212;
                                    }
                                }
                                list.add(new ScriptStackElement(fileName, group, stackTraceElement.getLineNumber()));
                                ++n3;
                            }
                            break Label_0268;
                        }
                    }
                    n2 = n5;
                }
                n6 = n4;
                n7 = n3;
                n8 = n2;
            }
            else {
                n6 = n4;
                n7 = n3;
                n8 = n2;
                if ("org.mozilla.javascript.Interpreter".equals(stackTraceElement.getClassName())) {
                    n6 = n4;
                    n7 = n3;
                    n8 = n2;
                    if ("interpretLoop".equals(stackTraceElement.getMethodName())) {
                        n6 = n4;
                        n7 = n3;
                        n8 = n2;
                        if (scriptStackElements != null) {
                            n6 = n4;
                            n7 = n3;
                            n8 = n2;
                            if (scriptStackElements.length > n4) {
                                final int n9 = n4 + 1;
                                final ScriptStackElement[] array2 = scriptStackElements[n4];
                                int n10;
                                int n11;
                                for (int length2 = array2.length, j = 0; j < length2; ++j, n3 = n11, n2 = n10) {
                                    final ScriptStackElement scriptStackElement = array2[j];
                                    if (n2 == 0 && s.equals(scriptStackElement.functionName)) {
                                        n10 = 1;
                                        n11 = n3;
                                    }
                                    else {
                                        n11 = n3;
                                        if ((n10 = n2) != 0) {
                                            if (n >= 0) {
                                                n11 = n3;
                                                n10 = n2;
                                                if (n3 >= n) {
                                                    continue;
                                                }
                                            }
                                            list.add(scriptStackElement);
                                            n11 = n3 + 1;
                                            n10 = n2;
                                        }
                                    }
                                }
                                n6 = n9;
                                n8 = n2;
                                n7 = n3;
                            }
                        }
                    }
                }
            }
        }
        return list.toArray(new ScriptStackElement[list.size()]);
    }
    
    public String getScriptStackTrace() {
        return this.getScriptStackTrace(-1, null);
    }
    
    public String getScriptStackTrace(final int n, final String s) {
        return formatStackTrace(this.getScriptStack(n, s), this.details());
    }
    
    @Deprecated
    public String getScriptStackTrace(final FilenameFilter filenameFilter) {
        return this.getScriptStackTrace();
    }
    
    public final void initColumnNumber(final int columnNumber) {
        if (columnNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(columnNumber));
        }
        if (this.columnNumber > 0) {
            throw new IllegalStateException();
        }
        this.columnNumber = columnNumber;
    }
    
    public final void initLineNumber(final int lineNumber) {
        if (lineNumber <= 0) {
            throw new IllegalArgumentException(String.valueOf(lineNumber));
        }
        if (this.lineNumber > 0) {
            throw new IllegalStateException();
        }
        this.lineNumber = lineNumber;
    }
    
    public final void initLineSource(final String lineSource) {
        if (lineSource == null) {
            throw new IllegalArgumentException();
        }
        if (this.lineSource != null) {
            throw new IllegalStateException();
        }
        this.lineSource = lineSource;
    }
    
    public final void initSourceName(final String sourceName) {
        if (sourceName == null) {
            throw new IllegalArgumentException();
        }
        if (this.sourceName != null) {
            throw new IllegalStateException();
        }
        this.sourceName = sourceName;
    }
    
    public final int lineNumber() {
        return this.lineNumber;
    }
    
    public final String lineSource() {
        return this.lineSource;
    }
    
    @Override
    public void printStackTrace(final PrintStream printStream) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(printStream);
            return;
        }
        printStream.print(this.generateStackTrace());
    }
    
    @Override
    public void printStackTrace(final PrintWriter printWriter) {
        if (this.interpreterStackInfo == null) {
            super.printStackTrace(printWriter);
            return;
        }
        printWriter.print(this.generateStackTrace());
    }
    
    final void recordErrorOrigin(final String s, final int n, final String s2, final int n2) {
        int n3 = n;
        if (n == -1) {
            n3 = 0;
        }
        if (s != null) {
            this.initSourceName(s);
        }
        if (n3 != 0) {
            this.initLineNumber(n3);
        }
        if (s2 != null) {
            this.initLineSource(s2);
        }
        if (n2 != 0) {
            this.initColumnNumber(n2);
        }
    }
    
    public final String sourceName() {
        return this.sourceName;
    }
}
