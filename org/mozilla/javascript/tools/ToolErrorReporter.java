package org.mozilla.javascript.tools;

import java.io.*;
import java.text.*;
import java.util.*;
import org.mozilla.javascript.*;

public class ToolErrorReporter implements ErrorReporter
{
    private static final String messagePrefix = "js: ";
    private PrintStream err;
    private boolean hasReportedErrorFlag;
    private boolean reportWarnings;
    
    public ToolErrorReporter(final boolean b) {
        this(b, System.err);
    }
    
    public ToolErrorReporter(final boolean reportWarnings, final PrintStream err) {
        this.reportWarnings = reportWarnings;
        this.err = err;
    }
    
    private String buildIndicator(final int n) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - 1; ++i) {
            sb.append(".");
        }
        sb.append("^");
        return sb.toString();
    }
    
    private static String getExceptionMessage(final RhinoException ex) {
        if (ex instanceof JavaScriptException) {
            return getMessage("msg.uncaughtJSException", ex.details());
        }
        if (ex instanceof EcmaError) {
            return getMessage("msg.uncaughtEcmaError", ex.details());
        }
        if (ex instanceof EvaluatorException) {
            return ex.details();
        }
        return ex.toString();
    }
    
    public static String getMessage(final String s) {
        return getMessage(s, (Object[])null);
    }
    
    public static String getMessage(final String s, final Object o, final Object o2) {
        return getMessage(s, new Object[] { o, o2 });
    }
    
    public static String getMessage(final String s, final String s2) {
        return getMessage(s, new Object[] { s2 });
    }
    
    public static String getMessage(final String s, final Object[] array) {
        final Context currentContext = Context.getCurrentContext();
        Locale locale;
        if (currentContext == null) {
            locale = Locale.getDefault();
        }
        else {
            locale = currentContext.getLocale();
        }
        final ResourceBundle bundle = ResourceBundle.getBundle("org.mozilla.javascript.tools.resources.Messages", locale);
        try {
            final String string = bundle.getString(s);
            if (array == null) {
                return string;
            }
            return new MessageFormat(string).format(array);
        }
        catch (MissingResourceException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no message resource found for message property ");
            sb.append(s);
            throw new RuntimeException(sb.toString());
        }
    }
    
    private void reportErrorMessage(String s, String message, final int n, final String s2, final int n2, final boolean b) {
        if (n > 0) {
            final String value = String.valueOf(n);
            if (message != null) {
                s = getMessage("msg.format3", new Object[] { message, value, s });
            }
            else {
                s = getMessage("msg.format2", new Object[] { value, s });
            }
        }
        else {
            s = getMessage("msg.format1", new Object[] { s });
        }
        message = s;
        if (b) {
            message = getMessage("msg.warning", s);
        }
        final PrintStream err = this.err;
        final StringBuilder sb = new StringBuilder();
        sb.append("js: ");
        sb.append(message);
        err.println(sb.toString());
        if (s2 != null) {
            final PrintStream err2 = this.err;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("js: ");
            sb2.append(s2);
            err2.println(sb2.toString());
            final PrintStream err3 = this.err;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("js: ");
            sb3.append(this.buildIndicator(n2));
            err3.println(sb3.toString());
        }
    }
    
    public static void reportException(final ErrorReporter errorReporter, final RhinoException ex) {
        if (errorReporter instanceof ToolErrorReporter) {
            ((ToolErrorReporter)errorReporter).reportException(ex);
            return;
        }
        errorReporter.error(getExceptionMessage(ex), ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber());
    }
    
    @Override
    public void error(final String s, final String s2, final int n, final String s3, final int n2) {
        this.hasReportedErrorFlag = true;
        this.reportErrorMessage(s, s2, n, s3, n2, false);
    }
    
    public boolean hasReportedError() {
        return this.hasReportedErrorFlag;
    }
    
    public boolean isReportingWarnings() {
        return this.reportWarnings;
    }
    
    public void reportException(final RhinoException ex) {
        if (ex instanceof WrappedException) {
            ((WrappedException)ex).printStackTrace(this.err);
            return;
        }
        final String systemProperty = SecurityUtilities.getSystemProperty("line.separator");
        final StringBuilder sb = new StringBuilder();
        sb.append(getExceptionMessage(ex));
        sb.append(systemProperty);
        sb.append(ex.getScriptStackTrace());
        this.reportErrorMessage(sb.toString(), ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber(), false);
    }
    
    @Override
    public EvaluatorException runtimeError(final String s, final String s2, final int n, final String s3, final int n2) {
        return new EvaluatorException(s, s2, n, s3, n2);
    }
    
    public void setIsReportingWarnings(final boolean reportWarnings) {
        this.reportWarnings = reportWarnings;
    }
    
    @Override
    public void warning(final String s, final String s2, final int n, final String s3, final int n2) {
        if (!this.reportWarnings) {
            return;
        }
        this.reportErrorMessage(s, s2, n, s3, n2, true);
    }
}
