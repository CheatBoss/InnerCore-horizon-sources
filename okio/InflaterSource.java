package okio;

import java.io.*;
import java.util.zip.*;

public final class InflaterSource implements Source
{
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;
    
    InflaterSource(final BufferedSource source, final Inflater inflater) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (inflater != null) {
            this.source = source;
            this.inflater = inflater;
            return;
        }
        throw new IllegalArgumentException("inflater == null");
    }
    
    private void releaseInflatedBytes() throws IOException {
        final int bufferBytesHeldByInflater = this.bufferBytesHeldByInflater;
        if (bufferBytesHeldByInflater == 0) {
            return;
        }
        final int n = bufferBytesHeldByInflater - this.inflater.getRemaining();
        this.bufferBytesHeldByInflater -= n;
        this.source.skip(n);
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }
    
    @Override
    public long read(final Buffer buffer, long size) throws IOException {
        if (size >= 0L) {
            if (!this.closed) {
                if (size == 0L) {
                    return 0L;
                }
            Label_0134_Outer:
                while (true) {
                    final boolean refill = this.refill();
                    while (true) {
                        Label_0232: {
                            try {
                                final Segment writableSegment = buffer.writableSegment(1);
                                final int inflate = this.inflater.inflate(writableSegment.data, writableSegment.limit, (int)Math.min(size, 8192 - writableSegment.limit));
                                if (inflate > 0) {
                                    writableSegment.limit += inflate;
                                    size = buffer.size;
                                    final long n = inflate;
                                    buffer.size = size + n;
                                    return n;
                                }
                                if (!this.inflater.finished() && !this.inflater.needsDictionary()) {
                                    break Label_0232;
                                }
                                this.releaseInflatedBytes();
                                if (writableSegment.pos == writableSegment.limit) {
                                    buffer.head = writableSegment.pop();
                                    SegmentPool.recycle(writableSegment);
                                }
                                return -1L;
                                throw new EOFException("source exhausted prematurely");
                            }
                            catch (DataFormatException ex) {
                                throw new IOException(ex);
                            }
                            break;
                        }
                        if (!refill) {
                            continue Label_0134_Outer;
                        }
                        continue;
                    }
                }
            }
            throw new IllegalStateException("closed");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("byteCount < 0: ");
        sb.append(size);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        this.releaseInflatedBytes();
        if (this.inflater.getRemaining() != 0) {
            throw new IllegalStateException("?");
        }
        if (this.source.exhausted()) {
            return true;
        }
        final Segment head = this.source.buffer().head;
        this.bufferBytesHeldByInflater = head.limit - head.pos;
        this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
        return false;
    }
    
    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}
