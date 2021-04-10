package androidx.recyclerview.widget;

import androidx.core.util.*;
import java.util.*;

class AdapterHelper implements OpReorderer.Callback
{
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;
    
    AdapterHelper(final Callback callback) {
        this(callback, false);
    }
    
    AdapterHelper(final Callback mCallback, final boolean mDisableRecycler) {
        this.mUpdateOpPool = new Pools.SimplePool<UpdateOp>(30);
        this.mPendingUpdates = new ArrayList<UpdateOp>();
        this.mPostponedList = new ArrayList<UpdateOp>();
        this.mExistingUpdateTypes = 0;
        this.mCallback = mCallback;
        this.mDisableRecycler = mDisableRecycler;
        this.mOpReorderer = new OpReorderer((OpReorderer.Callback)this);
    }
    
    private void applyAdd(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyMove(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyRemove(final UpdateOp updateOp) {
        final int positionStart = updateOp.positionStart;
        int n = 0;
        int n2 = updateOp.positionStart + updateOp.itemCount;
        int n3 = -1;
        int n8;
        for (int i = updateOp.positionStart; i < n2; i = n8) {
            boolean b = false;
            boolean b2 = false;
            int n4;
            if (this.mCallback.findViewHolder(i) == null && !this.canFindInPreLayout(i)) {
                if (n3 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n, null));
                    b2 = true;
                }
                n4 = 0;
            }
            else {
                if (n3 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n, null));
                    b = true;
                }
                final boolean b3 = true;
                b2 = b;
                n4 = (b3 ? 1 : 0);
            }
            int n5;
            int n6;
            if (b2) {
                n5 = i - n;
                n2 -= n;
                n6 = 1;
            }
            else {
                final int n7 = n + 1;
                n5 = i;
                n6 = n7;
            }
            n8 = n5 + 1;
            n = n6;
            n3 = n4;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(2, positionStart, n, null);
        }
        if (n3 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
            return;
        }
        this.postponeAndUpdateViewHolders(obtainUpdateOp);
    }
    
    private void applyUpdate(final UpdateOp updateOp) {
        int positionStart = updateOp.positionStart;
        int n = 0;
        final int positionStart2 = updateOp.positionStart;
        final int itemCount = updateOp.itemCount;
        int n2 = -1;
        int n6;
        for (int i = updateOp.positionStart; i < positionStart2 + itemCount; ++i, n2 = n6) {
            int n5;
            if (this.mCallback.findViewHolder(i) == null && !this.canFindInPreLayout(i)) {
                int n3 = positionStart;
                int n4 = n;
                if (n2 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n, updateOp.payload));
                    n4 = 0;
                    n3 = i;
                }
                final boolean b = false;
                positionStart = n3;
                n5 = n4;
                n6 = (b ? 1 : 0);
            }
            else {
                int n7 = positionStart;
                n5 = n;
                if (n2 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n, updateOp.payload));
                    n5 = 0;
                    n7 = i;
                }
                n6 = 1;
                positionStart = n7;
            }
            n = n5 + 1;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n != updateOp.itemCount) {
            final Object payload = updateOp.payload;
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(4, positionStart, n, payload);
        }
        if (n2 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
            return;
        }
        this.postponeAndUpdateViewHolders(obtainUpdateOp);
    }
    
    private boolean canFindInPreLayout(final int n) {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (this.findPositionOffset(updateOp.itemCount, i + 1) == n) {
                    return true;
                }
            }
            else if (updateOp.cmd == 1) {
                for (int positionStart = updateOp.positionStart, itemCount = updateOp.itemCount, j = updateOp.positionStart; j < positionStart + itemCount; ++j) {
                    if (this.findPositionOffset(j, i + 1) == n) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void dispatchAndUpdateViewHolders(UpdateOp obtainUpdateOp) {
        if (obtainUpdateOp.cmd != 1 && obtainUpdateOp.cmd != 8) {
            int updatePositionWithPostponed = this.updatePositionWithPostponed(obtainUpdateOp.positionStart, obtainUpdateOp.cmd);
            int n = 1;
            int positionStart = obtainUpdateOp.positionStart;
            final int cmd = obtainUpdateOp.cmd;
            int n2;
            if (cmd != 2) {
                if (cmd != 4) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("op should be remove or update.");
                    sb.append(obtainUpdateOp);
                    throw new IllegalArgumentException(sb.toString());
                }
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            int n3;
            for (int i = 1; i < obtainUpdateOp.itemCount; ++i, n = n3) {
                final int updatePositionWithPostponed2 = this.updatePositionWithPostponed(obtainUpdateOp.positionStart + n2 * i, obtainUpdateOp.cmd);
                boolean b = false;
                final int cmd2 = obtainUpdateOp.cmd;
                final boolean b2 = false;
                final boolean b3 = false;
                if (cmd2 != 2) {
                    if (cmd2 == 4) {
                        b = b3;
                        if (updatePositionWithPostponed2 == updatePositionWithPostponed + 1) {
                            b = true;
                        }
                    }
                }
                else {
                    b = b2;
                    if (updatePositionWithPostponed2 == updatePositionWithPostponed) {
                        b = true;
                    }
                }
                if (b) {
                    n3 = n + 1;
                }
                else {
                    final UpdateOp obtainUpdateOp2 = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n, obtainUpdateOp.payload);
                    this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, positionStart);
                    this.recycleUpdateOp(obtainUpdateOp2);
                    int n4 = positionStart;
                    if (obtainUpdateOp.cmd == 4) {
                        n4 = positionStart + n;
                    }
                    updatePositionWithPostponed = updatePositionWithPostponed2;
                    final int n5 = 1;
                    positionStart = n4;
                    n3 = n5;
                }
            }
            final Object payload = obtainUpdateOp.payload;
            this.recycleUpdateOp(obtainUpdateOp);
            if (n > 0) {
                obtainUpdateOp = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n, payload);
                this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, positionStart);
                this.recycleUpdateOp(obtainUpdateOp);
            }
            return;
        }
        throw new IllegalArgumentException("should not dispatch add or move for pre layout");
    }
    
    private void postponeAndUpdateViewHolders(final UpdateOp updateOp) {
        this.mPostponedList.add(updateOp);
        final int cmd = updateOp.cmd;
        if (cmd == 4) {
            this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
            return;
        }
        if (cmd == 8) {
            this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
            return;
        }
        switch (cmd) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown update op type for ");
                sb.append(updateOp);
                throw new IllegalArgumentException(sb.toString());
            }
            case 2: {
                this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(updateOp.positionStart, updateOp.itemCount);
            }
            case 1: {
                this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
            }
        }
    }
    
    private int updatePositionWithPostponed(int i, final int n) {
        int j = this.mPostponedList.size() - 1;
        int n2 = i;
        while (j >= 0) {
            final UpdateOp updateOp = this.mPostponedList.get(j);
            if (updateOp.cmd == 8) {
                int n3;
                if (updateOp.positionStart < updateOp.itemCount) {
                    i = updateOp.positionStart;
                    n3 = updateOp.itemCount;
                }
                else {
                    i = updateOp.itemCount;
                    n3 = updateOp.positionStart;
                }
                if (n2 >= i && n2 <= n3) {
                    if (i == updateOp.positionStart) {
                        if (n == 1) {
                            ++updateOp.itemCount;
                        }
                        else if (n == 2) {
                            --updateOp.itemCount;
                        }
                        i = n2 + 1;
                    }
                    else {
                        if (n == 1) {
                            ++updateOp.positionStart;
                        }
                        else if (n == 2) {
                            --updateOp.positionStart;
                        }
                        i = n2 - 1;
                    }
                }
                else if ((i = n2) < updateOp.positionStart) {
                    if (n == 1) {
                        ++updateOp.positionStart;
                        ++updateOp.itemCount;
                        i = n2;
                    }
                    else {
                        i = n2;
                        if (n == 2) {
                            --updateOp.positionStart;
                            --updateOp.itemCount;
                            i = n2;
                        }
                    }
                }
            }
            else if (updateOp.positionStart <= n2) {
                if (updateOp.cmd == 1) {
                    i = n2 - updateOp.itemCount;
                }
                else {
                    i = n2;
                    if (updateOp.cmd == 2) {
                        i = n2 + updateOp.itemCount;
                    }
                }
            }
            else if (n == 1) {
                ++updateOp.positionStart;
                i = n2;
            }
            else {
                i = n2;
                if (n == 2) {
                    --updateOp.positionStart;
                    i = n2;
                }
            }
            --j;
            n2 = i;
        }
        UpdateOp updateOp2;
        for (i = this.mPostponedList.size() - 1; i >= 0; --i) {
            updateOp2 = this.mPostponedList.get(i);
            if (updateOp2.cmd == 8) {
                if (updateOp2.itemCount == updateOp2.positionStart || updateOp2.itemCount < 0) {
                    this.mPostponedList.remove(i);
                    this.recycleUpdateOp(updateOp2);
                }
            }
            else if (updateOp2.itemCount <= 0) {
                this.mPostponedList.remove(i);
                this.recycleUpdateOp(updateOp2);
            }
        }
        return n2;
    }
    
    AdapterHelper addUpdateOp(final UpdateOp... array) {
        Collections.addAll(this.mPendingUpdates, array);
        return this;
    }
    
    public int applyPendingUpdatesToPosition(int n) {
        final int size = this.mPendingUpdates.size();
        int i = 0;
        int n2 = n;
        while (i < size) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            n = updateOp.cmd;
            if (n != 8) {
                switch (n) {
                    default: {
                        n = n2;
                        break;
                    }
                    case 2: {
                        n = n2;
                        if (updateOp.positionStart > n2) {
                            break;
                        }
                        if (updateOp.positionStart + updateOp.itemCount > n2) {
                            return -1;
                        }
                        n = n2 - updateOp.itemCount;
                        break;
                    }
                    case 1: {
                        n = n2;
                        if (updateOp.positionStart <= n2) {
                            n = n2 + updateOp.itemCount;
                            break;
                        }
                        break;
                    }
                }
            }
            else if (updateOp.positionStart == n2) {
                n = updateOp.itemCount;
            }
            else {
                int n3;
                if (updateOp.positionStart < (n3 = n2)) {
                    n3 = n2 - 1;
                }
                if (updateOp.itemCount <= (n = n3)) {
                    n = n3 + 1;
                }
            }
            ++i;
            n2 = n;
        }
        return n2;
    }
    
    void consumePostponedUpdates() {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            this.mCallback.onDispatchSecondPass((UpdateOp)this.mPostponedList.get(i));
        }
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    void consumeUpdatesInOnePass() {
        this.consumePostponedUpdates();
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            final int cmd = updateOp.cmd;
            if (cmd != 4) {
                if (cmd != 8) {
                    switch (cmd) {
                        case 2: {
                            this.mCallback.onDispatchSecondPass(updateOp);
                            this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
                            break;
                        }
                        case 1: {
                            this.mCallback.onDispatchSecondPass(updateOp);
                            this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                            break;
                        }
                    }
                }
                else {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                }
            }
            else {
                this.mCallback.onDispatchSecondPass(updateOp);
                this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }
    
    void dispatchFirstPassAndUpdateViewHolders(final UpdateOp updateOp, final int n) {
        this.mCallback.onDispatchFirstPass(updateOp);
        final int cmd = updateOp.cmd;
        if (cmd == 2) {
            this.mCallback.offsetPositionsForRemovingInvisible(n, updateOp.itemCount);
            return;
        }
        if (cmd != 4) {
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
        }
        this.mCallback.markViewHoldersUpdated(n, updateOp.itemCount, updateOp.payload);
    }
    
    int findPositionOffset(final int n) {
        return this.findPositionOffset(n, 0);
    }
    
    int findPositionOffset(int itemCount, int n) {
        final int size = this.mPostponedList.size();
        int i = n;
        n = itemCount;
        while (i < size) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart == n) {
                    itemCount = updateOp.itemCount;
                }
                else {
                    int n2;
                    if (updateOp.positionStart < (n2 = n)) {
                        n2 = n - 1;
                    }
                    if (updateOp.itemCount <= (itemCount = n2)) {
                        itemCount = n2 + 1;
                    }
                }
            }
            else if (updateOp.positionStart <= (itemCount = n)) {
                if (updateOp.cmd == 2) {
                    if (n < updateOp.positionStart + updateOp.itemCount) {
                        return -1;
                    }
                    itemCount = n - updateOp.itemCount;
                }
                else {
                    itemCount = n;
                    if (updateOp.cmd == 1) {
                        itemCount = n + updateOp.itemCount;
                    }
                }
            }
            ++i;
            n = itemCount;
        }
        return n;
    }
    
    boolean hasAnyUpdateTypes(final int n) {
        return (this.mExistingUpdateTypes & n) != 0x0;
    }
    
    boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }
    
    boolean hasUpdates() {
        return !this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty();
    }
    
    @Override
    public UpdateOp obtainUpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
        final UpdateOp updateOp = this.mUpdateOpPool.acquire();
        if (updateOp == null) {
            return new UpdateOp(cmd, positionStart, itemCount, payload);
        }
        updateOp.cmd = cmd;
        updateOp.positionStart = positionStart;
        updateOp.itemCount = itemCount;
        updateOp.payload = payload;
        return updateOp;
    }
    
    boolean onItemRangeChanged(final int n, final int n2, final Object o) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(4, n, n2, o));
        this.mExistingUpdateTypes |= 0x4;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    boolean onItemRangeInserted(final int n, final int n2) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(1, n, n2, null));
        this.mExistingUpdateTypes |= 0x1;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    boolean onItemRangeMoved(final int n, final int n2, final int n3) {
        boolean b = false;
        if (n == n2) {
            return false;
        }
        if (n3 != 1) {
            throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(8, n, n2, null));
        this.mExistingUpdateTypes |= 0x8;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    boolean onItemRangeRemoved(final int n, final int n2) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(2, n, n2, null));
        this.mExistingUpdateTypes |= 0x2;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            final int cmd = updateOp.cmd;
            if (cmd != 4) {
                if (cmd != 8) {
                    switch (cmd) {
                        case 2: {
                            this.applyRemove(updateOp);
                            break;
                        }
                        case 1: {
                            this.applyAdd(updateOp);
                            break;
                        }
                    }
                }
                else {
                    this.applyMove(updateOp);
                }
            }
            else {
                this.applyUpdate(updateOp);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.mPendingUpdates.clear();
    }
    
    @Override
    public void recycleUpdateOp(final UpdateOp updateOp) {
        if (!this.mDisableRecycler) {
            updateOp.payload = null;
            this.mUpdateOpPool.release(updateOp);
        }
    }
    
    void recycleUpdateOpsAndClearList(final List<UpdateOp> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            this.recycleUpdateOp(list.get(i));
        }
        list.clear();
    }
    
    void reset() {
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    interface Callback
    {
        RecyclerView.ViewHolder findViewHolder(final int p0);
        
        void markViewHoldersUpdated(final int p0, final int p1, final Object p2);
        
        void offsetPositionsForAdd(final int p0, final int p1);
        
        void offsetPositionsForMove(final int p0, final int p1);
        
        void offsetPositionsForRemovingInvisible(final int p0, final int p1);
        
        void offsetPositionsForRemovingLaidOutOrNewView(final int p0, final int p1);
        
        void onDispatchFirstPass(final UpdateOp p0);
        
        void onDispatchSecondPass(final UpdateOp p0);
    }
    
    static class UpdateOp
    {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;
        
        UpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }
        
        String cmdToString() {
            final int cmd = this.cmd;
            if (cmd == 4) {
                return "up";
            }
            if (cmd == 8) {
                return "mv";
            }
            switch (cmd) {
                default: {
                    return "??";
                }
                case 2: {
                    return "rm";
                }
                case 1: {
                    return "add";
                }
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final UpdateOp updateOp = (UpdateOp)o;
            if (this.cmd != updateOp.cmd) {
                return false;
            }
            if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == updateOp.positionStart && this.positionStart == updateOp.itemCount) {
                return true;
            }
            if (this.itemCount != updateOp.itemCount) {
                return false;
            }
            if (this.positionStart != updateOp.positionStart) {
                return false;
            }
            if (this.payload != null) {
                if (!this.payload.equals(updateOp.payload)) {
                    return false;
                }
            }
            else if (updateOp.payload != null) {
                return false;
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append("[");
            sb.append(this.cmdToString());
            sb.append(",s:");
            sb.append(this.positionStart);
            sb.append("c:");
            sb.append(this.itemCount);
            sb.append(",p:");
            sb.append(this.payload);
            sb.append("]");
            return sb.toString();
        }
    }
}
