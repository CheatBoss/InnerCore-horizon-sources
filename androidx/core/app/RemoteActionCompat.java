package androidx.core.app;

import androidx.versionedparcelable.*;
import androidx.core.graphics.drawable.*;
import androidx.core.util.*;
import android.app.*;
import android.os.*;
import androidx.annotation.*;

public final class RemoteActionCompat implements VersionedParcelable
{
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public PendingIntent mActionIntent;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public CharSequence mContentDescription;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public boolean mEnabled;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public IconCompat mIcon;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public boolean mShouldShowIcon;
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public CharSequence mTitle;
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP })
    public RemoteActionCompat() {
    }
    
    public RemoteActionCompat(@NonNull final RemoteActionCompat remoteActionCompat) {
        Preconditions.checkNotNull(remoteActionCompat);
        this.mIcon = remoteActionCompat.mIcon;
        this.mTitle = remoteActionCompat.mTitle;
        this.mContentDescription = remoteActionCompat.mContentDescription;
        this.mActionIntent = remoteActionCompat.mActionIntent;
        this.mEnabled = remoteActionCompat.mEnabled;
        this.mShouldShowIcon = remoteActionCompat.mShouldShowIcon;
    }
    
    public RemoteActionCompat(@NonNull final IconCompat iconCompat, @NonNull final CharSequence charSequence, @NonNull final CharSequence charSequence2, @NonNull final PendingIntent pendingIntent) {
        this.mIcon = Preconditions.checkNotNull(iconCompat);
        this.mTitle = Preconditions.checkNotNull(charSequence);
        this.mContentDescription = Preconditions.checkNotNull(charSequence2);
        this.mActionIntent = Preconditions.checkNotNull(pendingIntent);
        this.mEnabled = true;
        this.mShouldShowIcon = true;
    }
    
    @NonNull
    @RequiresApi(26)
    public static RemoteActionCompat createFromRemoteAction(@NonNull final RemoteAction remoteAction) {
        Preconditions.checkNotNull(remoteAction);
        final RemoteActionCompat remoteActionCompat = new RemoteActionCompat(IconCompat.createFromIcon(remoteAction.getIcon()), remoteAction.getTitle(), remoteAction.getContentDescription(), remoteAction.getActionIntent());
        remoteActionCompat.setEnabled(remoteAction.isEnabled());
        if (Build$VERSION.SDK_INT >= 28) {
            remoteActionCompat.setShouldShowIcon(remoteAction.shouldShowIcon());
        }
        return remoteActionCompat;
    }
    
    @NonNull
    public PendingIntent getActionIntent() {
        return this.mActionIntent;
    }
    
    @NonNull
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }
    
    @NonNull
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    @NonNull
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
    
    public void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
    }
    
    public void setShouldShowIcon(final boolean mShouldShowIcon) {
        this.mShouldShowIcon = mShouldShowIcon;
    }
    
    public boolean shouldShowIcon() {
        return this.mShouldShowIcon;
    }
    
    @NonNull
    @RequiresApi(26)
    public RemoteAction toRemoteAction() {
        final RemoteAction remoteAction = new RemoteAction(this.mIcon.toIcon(), this.mTitle, this.mContentDescription, this.mActionIntent);
        remoteAction.setEnabled(this.isEnabled());
        if (Build$VERSION.SDK_INT >= 28) {
            remoteAction.setShouldShowIcon(this.shouldShowIcon());
        }
        return remoteAction;
    }
}
