package androidx.core.app;

import androidx.annotation.*;
import android.app.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface NotificationBuilderWithBuilderAccessor
{
    Notification$Builder getBuilder();
}
