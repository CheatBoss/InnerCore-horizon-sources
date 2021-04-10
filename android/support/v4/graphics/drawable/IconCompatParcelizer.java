package android.support.v4.graphics.drawable;

import androidx.annotation.*;
import androidx.versionedparcelable.*;
import androidx.core.graphics.drawable.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY })
public final class IconCompatParcelizer extends androidx.core.graphics.drawable.IconCompatParcelizer
{
    public static IconCompat read(final VersionedParcel versionedParcel) {
        return androidx.core.graphics.drawable.IconCompatParcelizer.read(versionedParcel);
    }
    
    public static void write(final IconCompat iconCompat, final VersionedParcel versionedParcel) {
        androidx.core.graphics.drawable.IconCompatParcelizer.write(iconCompat, versionedParcel);
    }
}
