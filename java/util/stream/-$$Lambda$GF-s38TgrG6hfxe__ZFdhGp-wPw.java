package java.util.stream;

import java.util.function.*;

public final class -$$Lambda$GF-s38TgrG6hfxe__ZFdhGp-wPw implements Sink
{
    @Override
    public void accept(final double n) {
        Sink-CC.$default$accept(this, n);
    }
    
    @Override
    public void accept(final int n) {
        Sink-CC.$default$accept(this, n);
    }
    
    @Override
    public void accept(final long n) {
        Sink-CC.$default$accept(this, n);
    }
    
    @Override
    public final void accept(final Object o) {
        this.f$0.accept(o);
    }
    
    @Override
    public Consumer<Object> andThen(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void begin(final long n) {
        Sink-CC.$default$begin(this, n);
    }
    
    @Override
    public boolean cancellationRequested() {
        return Sink-CC.$default$cancellationRequested(this);
    }
    
    @Override
    public void end() {
        Sink-CC.$default$end(this);
    }
}
