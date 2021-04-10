package org.mozilla.javascript.regexp;

class REProgState
{
    final REBackTrackData backTrack;
    final int continuationOp;
    final int continuationPc;
    final int index;
    final int max;
    final int min;
    final REProgState previous;
    
    REProgState(final REProgState previous, final int min, final int max, final int index, final REBackTrackData backTrack, final int continuationOp, final int continuationPc) {
        this.previous = previous;
        this.min = min;
        this.max = max;
        this.index = index;
        this.continuationOp = continuationOp;
        this.continuationPc = continuationPc;
        this.backTrack = backTrack;
    }
}
