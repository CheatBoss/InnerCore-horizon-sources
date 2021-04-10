package org.mozilla.javascript.regexp;

class REGlobalData
{
    REBackTrackData backTrackStackTop;
    int cp;
    boolean multiline;
    long[] parens;
    RECompiled regexp;
    int skipped;
    REProgState stateStackTop;
    
    int parensIndex(final int n) {
        return (int)this.parens[n];
    }
    
    int parensLength(final int n) {
        return (int)(this.parens[n] >>> 32);
    }
    
    void setParens(final int n, final int n2, final int n3) {
        if (this.backTrackStackTop != null && this.backTrackStackTop.parens == this.parens) {
            this.parens = this.parens.clone();
        }
        this.parens[n] = (((long)n2 & 0xFFFFFFFFL) | (long)n3 << 32);
    }
}
