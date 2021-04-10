package org.mozilla.javascript.regexp;

import org.mozilla.javascript.*;

class CompilerState
{
    int backReferenceLimit;
    int classCount;
    int cp;
    char[] cpbegin;
    int cpend;
    Context cx;
    int flags;
    int maxBackReference;
    int parenCount;
    int parenNesting;
    int progLength;
    RENode result;
    
    CompilerState(final Context cx, final char[] cpbegin, final int cpend, final int flags) {
        this.cx = cx;
        this.cpbegin = cpbegin;
        this.cp = 0;
        this.cpend = cpend;
        this.flags = flags;
        this.backReferenceLimit = Integer.MAX_VALUE;
        this.maxBackReference = 0;
        this.parenCount = 0;
        this.classCount = 0;
        this.progLength = 0;
    }
}
