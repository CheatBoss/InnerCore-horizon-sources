package androidx.core.app;

import android.app.*;
import androidx.annotation.*;
import android.view.*;
import android.os.*;

public class DialogCompat
{
    private DialogCompat() {
    }
    
    @NonNull
    public static View requireViewById(@NonNull final Dialog dialog, final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return dialog.requireViewById(n);
        }
        final View viewById = dialog.findViewById(n);
        if (viewById == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
        }
        return viewById;
    }
}
