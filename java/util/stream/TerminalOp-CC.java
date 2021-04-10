package java.util.stream;

import java.util.*;

public final class TerminalOp-CC
{
    public static Object $default$evaluateParallel(final TerminalOp terminalOp, final PipelineHelper pipelineHelper, final Spliterator spliterator) {
        if (Tripwire.ENABLED) {
            Tripwire.trip(terminalOp.getClass(), "{0} triggering TerminalOp.evaluateParallel serial default");
        }
        return terminalOp.evaluateSequential(pipelineHelper, spliterator);
    }
    
    public static int $default$getOpFlags(final TerminalOp terminalOp) {
        return 0;
    }
    
    public static StreamShape $default$inputShape(final TerminalOp terminalOp) {
        return StreamShape.REFERENCE;
    }
}
