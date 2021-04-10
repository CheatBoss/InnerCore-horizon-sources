package org.mozilla.javascript.regexp;

import java.io.*;

final class RECharSet implements Serializable
{
    static final long serialVersionUID = 7931787979395898394L;
    transient volatile byte[] bits;
    transient volatile boolean converted;
    final int length;
    final boolean sense;
    final int startIndex;
    final int strlength;
    
    RECharSet(final int length, final int startIndex, final int strlength, final boolean sense) {
        this.length = length;
        this.startIndex = startIndex;
        this.strlength = strlength;
        this.sense = sense;
    }
}
