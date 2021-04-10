package android.support.v4.app;

import androidx.annotation.*;
import androidx.versionedparcelable.*;
import androidx.core.app.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY })
public final class RemoteActionCompatParcelizer extends androidx.core.app.RemoteActionCompatParcelizer
{
    public static RemoteActionCompat read(final VersionedParcel versionedParcel) {
        return androidx.core.app.RemoteActionCompatParcelizer.read(versionedParcel);
    }
    
    public static void write(final RemoteActionCompat remoteActionCompat, final VersionedParcel versionedParcel) {
        androidx.core.app.RemoteActionCompatParcelizer.write(remoteActionCompat, versionedParcel);
    }
}
