package org.apache.james.mime4j.codec;

import java.util.function.*;
import java.util.*;

public class ByteQueue implements Iterable<Byte>
{
    private UnboundedFifoByteBuffer buf;
    private int initialCapacity;
    
    public ByteQueue() {
        this.initialCapacity = -1;
        this.buf = new UnboundedFifoByteBuffer();
    }
    
    public ByteQueue(final int initialCapacity) {
        this.initialCapacity = -1;
        this.buf = new UnboundedFifoByteBuffer(initialCapacity);
        this.initialCapacity = initialCapacity;
    }
    
    public void clear() {
        UnboundedFifoByteBuffer buf;
        if (this.initialCapacity != -1) {
            buf = new UnboundedFifoByteBuffer(this.initialCapacity);
        }
        else {
            buf = new UnboundedFifoByteBuffer();
        }
        this.buf = buf;
    }
    
    public int count() {
        return this.buf.size();
    }
    
    public byte dequeue() {
        return this.buf.remove();
    }
    
    public void enqueue(final byte b) {
        this.buf.add(b);
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
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
    public Iterator<Byte> iterator() {
        return this.buf.iterator();
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
    }
}
