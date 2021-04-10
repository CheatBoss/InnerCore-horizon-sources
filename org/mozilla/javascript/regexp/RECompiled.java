package org.mozilla.javascript.regexp;

import java.io.*;

class RECompiled implements Serializable
{
    static final long serialVersionUID = -6144956577595844213L;
    int anchorCh;
    int classCount;
    RECharSet[] classList;
    int flags;
    int parenCount;
    byte[] program;
    final char[] source;
    
    RECompiled(final String s) {
        this.anchorCh = -1;
        this.source = s.toCharArray();
    }
}
