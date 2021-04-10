package com.android.dx.command.dump;

class Args
{
    boolean basicBlocks;
    boolean debug;
    boolean dotDump;
    String method;
    boolean optimize;
    boolean rawBytes;
    boolean ropBlocks;
    boolean ssaBlocks;
    String ssaStep;
    boolean strictParse;
    int width;
    
    Args() {
        this.debug = false;
        this.rawBytes = false;
        this.basicBlocks = false;
        this.ropBlocks = false;
        this.ssaBlocks = false;
        this.ssaStep = null;
        this.optimize = false;
        this.strictParse = false;
        this.width = 0;
        this.dotDump = false;
    }
}
