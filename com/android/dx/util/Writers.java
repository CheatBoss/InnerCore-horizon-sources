package com.android.dx.util;

import java.io.*;

public final class Writers
{
    private Writers() {
    }
    
    public static PrintWriter printWriterFor(final Writer writer) {
        if (writer instanceof PrintWriter) {
            return (PrintWriter)writer;
        }
        return new PrintWriter(writer);
    }
}
