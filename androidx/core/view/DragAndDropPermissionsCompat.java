package androidx.core.view;

import android.app.*;
import android.os.*;
import android.view.*;
import androidx.annotation.*;

public final class DragAndDropPermissionsCompat
{
    private Object mDragAndDropPermissions;
    
    private DragAndDropPermissionsCompat(final Object mDragAndDropPermissions) {
        this.mDragAndDropPermissions = mDragAndDropPermissions;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static DragAndDropPermissionsCompat request(final Activity activity, final DragEvent dragEvent) {
        if (Build$VERSION.SDK_INT >= 24) {
            final DragAndDropPermissions requestDragAndDropPermissions = activity.requestDragAndDropPermissions(dragEvent);
            if (requestDragAndDropPermissions != null) {
                return new DragAndDropPermissionsCompat(requestDragAndDropPermissions);
            }
        }
        return null;
    }
    
    public void release() {
        if (Build$VERSION.SDK_INT >= 24) {
            ((DragAndDropPermissions)this.mDragAndDropPermissions).release();
        }
    }
}
