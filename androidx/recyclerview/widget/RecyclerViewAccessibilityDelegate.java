package androidx.recyclerview.widget;

import android.view.accessibility.*;
import android.os.*;
import java.util.*;
import androidx.core.view.accessibility.*;
import androidx.annotation.*;
import android.view.*;
import androidx.core.view.*;

public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat
{
    private final ItemDelegate mItemDelegate;
    final RecyclerView mRecyclerView;
    
    public RecyclerViewAccessibilityDelegate(@NonNull final RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        final AccessibilityDelegateCompat itemDelegate = this.getItemDelegate();
        if (itemDelegate != null && itemDelegate instanceof ItemDelegate) {
            this.mItemDelegate = (ItemDelegate)itemDelegate;
            return;
        }
        this.mItemDelegate = new ItemDelegate(this);
    }
    
    @NonNull
    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        if (view instanceof RecyclerView && !this.shouldIgnore()) {
            final RecyclerView recyclerView = (RecyclerView)view;
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onInitializeAccessibilityEvent(accessibilityEvent);
            }
        }
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        if (!this.shouldIgnore() && this.mRecyclerView.getLayoutManager() != null) {
            this.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
        }
    }
    
    @Override
    public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return super.performAccessibilityAction(view, n, bundle) || (!this.shouldIgnore() && this.mRecyclerView.getLayoutManager() != null && this.mRecyclerView.getLayoutManager().performAccessibilityAction(n, bundle));
    }
    
    boolean shouldIgnore() {
        return this.mRecyclerView.hasPendingAdapterUpdates();
    }
    
    public static class ItemDelegate extends AccessibilityDelegateCompat
    {
        private Map<View, AccessibilityDelegateCompat> mOriginalItemDelegates;
        final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;
        
        public ItemDelegate(@NonNull final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate) {
            this.mOriginalItemDelegates = new WeakHashMap<View, AccessibilityDelegateCompat>();
            this.mRecyclerViewDelegate = mRecyclerViewDelegate;
        }
        
        @Override
        public boolean dispatchPopulateAccessibilityEvent(@NonNull final View view, @NonNull final AccessibilityEvent accessibilityEvent) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
            }
            return super.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Nullable
        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(@NonNull final View view) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.getAccessibilityNodeProvider(view);
            }
            return super.getAccessibilityNodeProvider(view);
        }
        
        AccessibilityDelegateCompat getAndRemoveOriginalDelegateForItem(final View view) {
            return this.mOriginalItemDelegates.remove(view);
        }
        
        @Override
        public void onInitializeAccessibilityEvent(@NonNull final View view, @NonNull final AccessibilityEvent accessibilityEvent) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
                return;
            }
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
                this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
                final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
                if (accessibilityDelegateCompat != null) {
                    accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                }
                else {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                }
                return;
            }
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        }
        
        @Override
        public void onPopulateAccessibilityEvent(@NonNull final View view, @NonNull final AccessibilityEvent accessibilityEvent) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
                return;
            }
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
        }
        
        @Override
        public boolean onRequestSendAccessibilityEvent(@NonNull final ViewGroup viewGroup, @NonNull final View view, @NonNull final AccessibilityEvent accessibilityEvent) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(viewGroup);
            if (accessibilityDelegateCompat != null) {
                return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
                final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
                if (accessibilityDelegateCompat != null) {
                    if (accessibilityDelegateCompat.performAccessibilityAction(view, n, bundle)) {
                        return true;
                    }
                }
                else if (super.performAccessibilityAction(view, n, bundle)) {
                    return true;
                }
                return this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().performAccessibilityActionForItem(view, n, bundle);
            }
            return super.performAccessibilityAction(view, n, bundle);
        }
        
        void saveOriginalDelegate(final View view) {
            final AccessibilityDelegateCompat accessibilityDelegate = ViewCompat.getAccessibilityDelegate(view);
            if (accessibilityDelegate != null && accessibilityDelegate != this) {
                this.mOriginalItemDelegates.put(view, accessibilityDelegate);
            }
        }
        
        @Override
        public void sendAccessibilityEvent(@NonNull final View view, final int n) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEvent(view, n);
                return;
            }
            super.sendAccessibilityEvent(view, n);
        }
        
        @Override
        public void sendAccessibilityEventUnchecked(@NonNull final View view, @NonNull final AccessibilityEvent accessibilityEvent) {
            final AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(view);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
                return;
            }
            super.sendAccessibilityEventUnchecked(view, accessibilityEvent);
        }
    }
}
