package com.android.dx.dex.cf;

import java.io.*;

public class CfOptions
{
    public String dontOptimizeListFile;
    public boolean localInfo;
    public boolean optimize;
    public String optimizeListFile;
    public int positionInfo;
    public boolean statistics;
    public boolean strictNameCheck;
    public PrintStream warn;
    
    public CfOptions() {
        this.positionInfo = 2;
        this.localInfo = false;
        this.strictNameCheck = true;
        this.optimize = false;
        this.optimizeListFile = null;
        this.dontOptimizeListFile = null;
        this.warn = System.err;
    }
}
