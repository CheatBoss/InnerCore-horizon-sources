package okio;

import javax.annotation.*;

final class Segment
{
    final byte[] data;
    int limit;
    Segment next;
    boolean owner;
    int pos;
    Segment prev;
    boolean shared;
    
    Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }
    
    Segment(final byte[] data, final int pos, final int limit, final boolean shared, final boolean owner) {
        this.data = data;
        this.pos = pos;
        this.limit = limit;
        this.shared = shared;
        this.owner = owner;
    }
    
    public void compact() {
        final Segment prev = this.prev;
        if (prev == this) {
            throw new IllegalStateException();
        }
        if (!prev.owner) {
            return;
        }
        final int n = this.limit - this.pos;
        final int limit = prev.limit;
        int pos;
        if (prev.shared) {
            pos = 0;
        }
        else {
            pos = prev.pos;
        }
        if (n > 8192 - limit + pos) {
            return;
        }
        this.writeTo(this.prev, n);
        this.pop();
        SegmentPool.recycle(this);
    }
    
    @Nullable
    public Segment pop() {
        Segment next = this.next;
        if (next == this) {
            next = null;
        }
        final Segment prev = this.prev;
        prev.next = this.next;
        this.next.prev = prev;
        this.next = null;
        this.prev = null;
        return next;
    }
    
    public Segment push(final Segment segment) {
        segment.prev = this;
        segment.next = this.next;
        this.next.prev = segment;
        return this.next = segment;
    }
    
    Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true, false);
    }
    
    public Segment split(final int n) {
        if (n > 0 && n <= this.limit - this.pos) {
            Segment segment;
            if (n >= 1024) {
                segment = this.sharedCopy();
            }
            else {
                segment = SegmentPool.take();
                System.arraycopy(this.data, this.pos, segment.data, 0, n);
            }
            segment.limit = segment.pos + n;
            this.pos += n;
            this.prev.push(segment);
            return segment;
        }
        throw new IllegalArgumentException();
    }
    
    Segment unsharedCopy() {
        return new Segment(this.data.clone(), this.pos, this.limit, false, true);
    }
    
    public void writeTo(final Segment segment, final int n) {
        if (segment.owner) {
            final int limit = segment.limit;
            final int n2 = limit + n;
            if (n2 > 8192) {
                if (segment.shared) {
                    throw new IllegalArgumentException();
                }
                final int pos = segment.pos;
                if (n2 - pos > 8192) {
                    throw new IllegalArgumentException();
                }
                final byte[] data = segment.data;
                System.arraycopy(data, pos, data, 0, limit - pos);
                segment.limit -= segment.pos;
                segment.pos = 0;
            }
            System.arraycopy(this.data, this.pos, segment.data, segment.limit, n);
            segment.limit += n;
            this.pos += n;
            return;
        }
        throw new IllegalArgumentException();
    }
}
