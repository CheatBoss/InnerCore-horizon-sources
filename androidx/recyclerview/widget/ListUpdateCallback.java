package androidx.recyclerview.widget;

import androidx.annotation.*;

public interface ListUpdateCallback
{
    void onChanged(final int p0, final int p1, @Nullable final Object p2);
    
    void onInserted(final int p0, final int p1);
    
    void onMoved(final int p0, final int p1);
    
    void onRemoved(final int p0, final int p1);
}
