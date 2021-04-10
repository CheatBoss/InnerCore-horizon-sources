package androidx.core.os;

import android.content.*;
import androidx.annotation.*;
import android.os.*;

public class UserManagerCompat
{
    private UserManagerCompat() {
    }
    
    public static boolean isUserUnlocked(@NonNull final Context context) {
        return Build$VERSION.SDK_INT < 24 || ((UserManager)context.getSystemService((Class)UserManager.class)).isUserUnlocked();
    }
}
