package androidx.core.view.accessibility;

import android.text.style.*;
import androidx.annotation.*;
import android.view.*;
import android.os.*;

public final class AccessibilityClickableSpanCompat extends ClickableSpan
{
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static final String SPAN_ID = "ACCESSIBILITY_CLICKABLE_SPAN_ID";
    private final int mClickableSpanActionId;
    private final AccessibilityNodeInfoCompat mNodeInfoCompat;
    private final int mOriginalClickableSpanId;
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public AccessibilityClickableSpanCompat(final int mOriginalClickableSpanId, final AccessibilityNodeInfoCompat mNodeInfoCompat, final int mClickableSpanActionId) {
        this.mOriginalClickableSpanId = mOriginalClickableSpanId;
        this.mNodeInfoCompat = mNodeInfoCompat;
        this.mClickableSpanActionId = mClickableSpanActionId;
    }
    
    public void onClick(final View view) {
        final Bundle bundle = new Bundle();
        bundle.putInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", this.mOriginalClickableSpanId);
        this.mNodeInfoCompat.performAction(this.mClickableSpanActionId, bundle);
    }
}
