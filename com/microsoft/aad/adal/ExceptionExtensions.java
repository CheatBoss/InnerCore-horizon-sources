package com.microsoft.aad.adal;

import java.io.*;

final class ExceptionExtensions
{
    private ExceptionExtensions() {
    }
    
    static String getExceptionMessage(final Exception ex) {
        String message;
        if (ex != null) {
            if ((message = ex.getMessage()) == null) {
                final StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                return stringWriter.toString();
            }
        }
        else {
            message = null;
        }
        return message;
    }
}
