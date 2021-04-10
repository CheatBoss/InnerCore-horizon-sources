package org.mozilla.javascript.regexp;

class REBackTrackData
{
    final int continuationOp;
    final int continuationPc;
    final int cp;
    final int op;
    final long[] parens;
    final int pc;
    final REBackTrackData previous;
    final REProgState stateStackTop;
    
    REBackTrackData(final REGlobalData reGlobalData, final int op, final int pc, final int cp, final int continuationOp, final int continuationPc) {
        this.previous = reGlobalData.backTrackStackTop;
        this.op = op;
        this.pc = pc;
        this.cp = cp;
        this.continuationOp = continuationOp;
        this.continuationPc = continuationPc;
        this.parens = reGlobalData.parens;
        this.stateStackTop = reGlobalData.stateStackTop;
    }
}
