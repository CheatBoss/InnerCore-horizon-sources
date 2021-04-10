package androidx.core.view;

import androidx.annotation.*;
import android.os.*;
import androidx.core.*;
import android.view.*;
import android.view.accessibility.*;

public final class ViewGroupCompat
{
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    
    private ViewGroupCompat() {
    }
    
    public static int getLayoutMode(@NonNull final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 18) {
            return viewGroup.getLayoutMode();
        }
        return 0;
    }
    
    public static int getNestedScrollAxes(@NonNull final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 21) {
            return viewGroup.getNestedScrollAxes();
        }
        if (viewGroup instanceof NestedScrollingParent) {
            return ((NestedScrollingParent)viewGroup).getNestedScrollAxes();
        }
        return 0;
    }
    
    public static boolean isTransitionGroup(@NonNull final ViewGroup viewGroup) {
        if (Build$VERSION.SDK_INT >= 21) {
            return viewGroup.isTransitionGroup();
        }
        final Boolean b = (Boolean)viewGroup.getTag(R$id.tag_transition_group);
        return (b != null && b) || viewGroup.getBackground() != null || ViewCompat.getTransitionName((View)viewGroup) != null;
    }
    
    @Deprecated
    public static boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewGroup.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    public static void setLayoutMode(@NonNull final ViewGroup viewGroup, final int layoutMode) {
        if (Build$VERSION.SDK_INT >= 18) {
            viewGroup.setLayoutMode(layoutMode);
        }
    }
    
    @Deprecated
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean motionEventSplittingEnabled) {
        viewGroup.setMotionEventSplittingEnabled(motionEventSplittingEnabled);
    }
    
    public static void setTransitionGroup(@NonNull final ViewGroup viewGroup, final boolean transitionGroup) {
        if (Build$VERSION.SDK_INT >= 21) {
            viewGroup.setTransitionGroup(transitionGroup);
            return;
        }
        viewGroup.setTag(R$id.tag_transition_group, (Object)transitionGroup);
    }
}
