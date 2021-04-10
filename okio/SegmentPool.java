package okio;

import javax.annotation.*;

final class SegmentPool
{
    static long byteCount;
    @Nullable
    static Segment next;
    
    private SegmentPool() {
    }
    
    static void recycle(final Segment next) {
        if (next.next == null && next.prev == null) {
            if (next.shared) {
                return;
            }
            synchronized (SegmentPool.class) {
                if (SegmentPool.byteCount + 8192L > 65536L) {
                    return;
                }
                SegmentPool.byteCount += 8192L;
                next.next = SegmentPool.next;
                next.limit = 0;
                next.pos = 0;
                SegmentPool.next = next;
                return;
            }
        }
        throw new IllegalArgumentException();
    }
    
    static Segment take() {
        synchronized (SegmentPool.class) {
            if (SegmentPool.next != null) {
                final Segment next = SegmentPool.next;
                SegmentPool.next = next.next;
                next.next = null;
                SegmentPool.byteCount -= 8192L;
                return next;
            }
            // monitorexit(SegmentPool.class)
            return new Segment();
        }
    }
}
