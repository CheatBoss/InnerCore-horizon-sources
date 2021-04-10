package java.util.stream;

import java.util.*;

interface TerminalOp<E_IN, R>
{
    default StreamShape inputShape() {
        return StreamShape.REFERENCE;
    }
    
    default int getOpFlags() {
        return 0;
    }
    
    default <P_IN> R evaluateParallel(final PipelineHelper<E_IN> pipelineHelper, final Spliterator<P_IN> spliterator) {
        if (Tripwire.ENABLED) {
            Tripwire.trip(this.getClass(), "{0} triggering TerminalOp.evaluateParallel serial default");
        }
        return this.evaluateSequential(pipelineHelper, (Spliterator<Object>)spliterator);
    }
    
     <P_IN> R evaluateSequential(final PipelineHelper<E_IN> p0, final Spliterator<P_IN> p1);
}
