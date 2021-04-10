package com.android.dx.command;

import java.io.*;

public class DxConsole
{
    public static PrintStream err;
    public static final PrintStream noop;
    public static PrintStream out;
    
    static {
        DxConsole.out = System.out;
        DxConsole.err = System.err;
        noop = new PrintStream(new OutputStream() {
            @Override
            public void write(final int n) throws IOException {
            }
        });
    }
}
