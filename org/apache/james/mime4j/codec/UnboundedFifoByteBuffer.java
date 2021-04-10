package org.apache.james.mime4j.codec;

import java.util.function.*;
import java.util.*;

class UnboundedFifoByteBuffer
{
    protected byte[] buffer;
    protected int head;
    protected int tail;
    
    public UnboundedFifoByteBuffer() {
        this(32);
    }
    
    public UnboundedFifoByteBuffer(final int n) {
        if (n > 0) {
            this.buffer = new byte[n + 1];
            this.head = 0;
            this.tail = 0;
            return;
        }
        throw new IllegalArgumentException("The size must be greater than 0");
    }
    
    private int decrement(int n) {
        if (--n < 0) {
            n = this.buffer.length - 1;
        }
        return n;
    }
    
    private int increment(int n) {
        if (++n >= this.buffer.length) {
            n = 0;
        }
        return n;
    }
    
    public boolean add(final byte b) {
        final int size = this.size();
        final byte[] buffer = this.buffer;
        if (size + 1 >= buffer.length) {
            final byte[] buffer2 = new byte[(buffer.length - 1) * 2 + 1];
            int i = this.head;
            int tail = 0;
            while (i != this.tail) {
                final byte[] buffer3 = this.buffer;
                buffer2[tail] = buffer3[i];
                buffer3[i] = 0;
                final int n = tail + 1;
                final int n2 = ++i;
                tail = n;
                if (n2 == buffer3.length) {
                    i = 0;
                    tail = n;
                }
            }
            this.buffer = buffer2;
            this.head = 0;
            this.tail = tail;
        }
        final byte[] buffer4 = this.buffer;
        final int tail2 = this.tail;
        buffer4[tail2] = b;
        if ((this.tail = tail2 + 1) >= buffer4.length) {
            this.tail = 0;
        }
        return true;
    }
    
    public byte get() {
        if (!this.isEmpty()) {
            return this.buffer[this.head];
        }
        throw new IllegalStateException("The buffer is already empty");
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int index = UnboundedFifoByteBuffer.this.head;
            private int lastReturnedIndex = -1;
            
            @Override
            public void forEachRemaining(final Consumer<?> p0) {
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
            public boolean hasNext() {
                return this.index != UnboundedFifoByteBuffer.this.tail;
            }
            
            @Override
            public Byte next() {
                if (this.hasNext()) {
                    final int index = this.index;
                    this.lastReturnedIndex = index;
                    this.index = UnboundedFifoByteBuffer.this.increment(index);
                    return new Byte(UnboundedFifoByteBuffer.this.buffer[this.lastReturnedIndex]);
                }
                throw new NoSuchElementException();
            }
            
            @Override
            public void remove() {
                final int lastReturnedIndex = this.lastReturnedIndex;
                if (lastReturnedIndex == -1) {
                    throw new IllegalStateException();
                }
                if (lastReturnedIndex == UnboundedFifoByteBuffer.this.head) {
                    UnboundedFifoByteBuffer.this.remove();
                    this.lastReturnedIndex = -1;
                    return;
                }
                int i = this.lastReturnedIndex + 1;
                while (i != UnboundedFifoByteBuffer.this.tail) {
                    if (i >= UnboundedFifoByteBuffer.this.buffer.length) {
                        UnboundedFifoByteBuffer.this.buffer[i - 1] = UnboundedFifoByteBuffer.this.buffer[0];
                        i = 0;
                    }
                    else {
                        UnboundedFifoByteBuffer.this.buffer[i - 1] = UnboundedFifoByteBuffer.this.buffer[i];
                        ++i;
                    }
                }
                this.lastReturnedIndex = -1;
                final UnboundedFifoByteBuffer this$0 = UnboundedFifoByteBuffer.this;
                this$0.tail = this$0.decrement(this$0.tail);
                UnboundedFifoByteBuffer.this.buffer[UnboundedFifoByteBuffer.this.tail] = 0;
                this.index = UnboundedFifoByteBuffer.this.decrement(this.index);
            }
        };
    }
    
    public byte remove() {
        if (!this.isEmpty()) {
            final byte[] buffer = this.buffer;
            final int head = this.head;
            final byte b = buffer[head];
            if ((this.head = head + 1) >= buffer.length) {
                this.head = 0;
            }
            return b;
        }
        throw new IllegalStateException("The buffer is already empty");
    }
    
    public int size() {
        final int tail = this.tail;
        final int head = this.head;
        if (tail < head) {
            return this.buffer.length - head + tail;
        }
        return tail - head;
    }
}
