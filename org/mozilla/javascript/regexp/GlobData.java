package org.mozilla.javascript.regexp;

import org.mozilla.javascript.*;

final class GlobData
{
    Scriptable arrayobj;
    StringBuilder charBuf;
    int dollar;
    boolean global;
    Function lambda;
    int leftIndex;
    int mode;
    String repstr;
    String str;
    
    GlobData() {
        this.dollar = -1;
    }
}
