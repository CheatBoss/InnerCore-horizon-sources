package androidx.core.content.pm;

import androidx.core.graphics.drawable.*;
import android.content.*;
import androidx.core.app.*;
import androidx.annotation.*;
import android.os.*;
import android.graphics.drawable.*;
import android.content.pm.*;
import android.text.*;
import java.util.*;

public class ShortcutInfoCompat
{
    private static final String EXTRA_LONG_LIVED = "extraLongLived";
    private static final String EXTRA_PERSON_ = "extraPerson_";
    private static final String EXTRA_PERSON_COUNT = "extraPersonCount";
    ComponentName mActivity;
    Set<String> mCategories;
    Context mContext;
    CharSequence mDisabledMessage;
    IconCompat mIcon;
    String mId;
    Intent[] mIntents;
    boolean mIsAlwaysBadged;
    boolean mIsLongLived;
    CharSequence mLabel;
    CharSequence mLongLabel;
    Person[] mPersons;
    
    ShortcutInfoCompat() {
    }
    
    @RequiresApi(22)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    private PersistableBundle buildExtrasBundle() {
        final PersistableBundle persistableBundle = new PersistableBundle();
        if (this.mPersons != null && this.mPersons.length > 0) {
            persistableBundle.putInt("extraPersonCount", this.mPersons.length);
            for (int i = 0; i < this.mPersons.length; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("extraPerson_");
                sb.append(i + 1);
                persistableBundle.putPersistableBundle(sb.toString(), this.mPersons[i].toPersistableBundle());
            }
        }
        persistableBundle.putBoolean("extraLongLived", this.mIsLongLived);
        return persistableBundle;
    }
    
    @Nullable
    @RequiresApi(25)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    @VisibleForTesting
    static boolean getLongLivedFromExtra(@NonNull final PersistableBundle persistableBundle) {
        return persistableBundle != null && persistableBundle.containsKey("extraLongLived") && persistableBundle.getBoolean("extraLongLived");
    }
    
    @Nullable
    @RequiresApi(25)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    @VisibleForTesting
    static Person[] getPersonsFromExtra(@NonNull final PersistableBundle persistableBundle) {
        if (persistableBundle != null && persistableBundle.containsKey("extraPersonCount")) {
            final int int1 = persistableBundle.getInt("extraPersonCount");
            final Person[] array = new Person[int1];
            for (int i = 0; i < int1; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append("extraPerson_");
                sb.append(i + 1);
                array[i] = Person.fromPersistableBundle(persistableBundle.getPersistableBundle(sb.toString()));
            }
            return array;
        }
        return null;
    }
    
    Intent addToIntent(final Intent intent) {
        intent.putExtra("android.intent.extra.shortcut.INTENT", (Parcelable)this.mIntents[this.mIntents.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        if (this.mIcon != null) {
            Drawable loadIcon = null;
            final Drawable drawable = null;
            if (this.mIsAlwaysBadged) {
                final PackageManager packageManager = this.mContext.getPackageManager();
                Drawable activityIcon = drawable;
                if (this.mActivity != null) {
                    try {
                        activityIcon = packageManager.getActivityIcon(this.mActivity);
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        activityIcon = drawable;
                    }
                }
                if ((loadIcon = activityIcon) == null) {
                    loadIcon = this.mContext.getApplicationInfo().loadIcon(packageManager);
                }
            }
            this.mIcon.addToShortcutIntent(intent, loadIcon, this.mContext);
        }
        return intent;
    }
    
    @Nullable
    public ComponentName getActivity() {
        return this.mActivity;
    }
    
    @Nullable
    public Set<String> getCategories() {
        return this.mCategories;
    }
    
    @Nullable
    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public IconCompat getIcon() {
        return this.mIcon;
    }
    
    @NonNull
    public String getId() {
        return this.mId;
    }
    
    @NonNull
    public Intent getIntent() {
        return this.mIntents[this.mIntents.length - 1];
    }
    
    @NonNull
    public Intent[] getIntents() {
        return Arrays.copyOf(this.mIntents, this.mIntents.length);
    }
    
    @Nullable
    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }
    
    @NonNull
    public CharSequence getShortLabel() {
        return this.mLabel;
    }
    
    @RequiresApi(25)
    public ShortcutInfo toShortcutInfo() {
        final ShortcutInfo$Builder setIntents = new ShortcutInfo$Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        if (this.mIcon != null) {
            setIntents.setIcon(this.mIcon.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            setIntents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            setIntents.setDisabledMessage(this.mDisabledMessage);
        }
        if (this.mActivity != null) {
            setIntents.setActivity(this.mActivity);
        }
        if (this.mCategories != null) {
            setIntents.setCategories((Set)this.mCategories);
        }
        setIntents.setExtras(this.buildExtrasBundle());
        return setIntents.build();
    }
    
    public static class Builder
    {
        private final ShortcutInfoCompat mInfo;
        
        @RequiresApi(25)
        @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
        public Builder(@NonNull final Context mContext, @NonNull final ShortcutInfo shortcutInfo) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = mContext;
            this.mInfo.mId = shortcutInfo.getId();
            final Intent[] intents = shortcutInfo.getIntents();
            this.mInfo.mIntents = Arrays.copyOf(intents, intents.length);
            this.mInfo.mActivity = shortcutInfo.getActivity();
            this.mInfo.mLabel = shortcutInfo.getShortLabel();
            this.mInfo.mLongLabel = shortcutInfo.getLongLabel();
            this.mInfo.mDisabledMessage = shortcutInfo.getDisabledMessage();
            this.mInfo.mCategories = (Set<String>)shortcutInfo.getCategories();
            this.mInfo.mPersons = ShortcutInfoCompat.getPersonsFromExtra(shortcutInfo.getExtras());
        }
        
        public Builder(@NonNull final Context mContext, @NonNull final String mId) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = mContext;
            this.mInfo.mId = mId;
        }
        
        @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
        public Builder(@NonNull final ShortcutInfoCompat shortcutInfoCompat) {
            this.mInfo = new ShortcutInfoCompat();
            this.mInfo.mContext = shortcutInfoCompat.mContext;
            this.mInfo.mId = shortcutInfoCompat.mId;
            this.mInfo.mIntents = Arrays.copyOf(shortcutInfoCompat.mIntents, shortcutInfoCompat.mIntents.length);
            this.mInfo.mActivity = shortcutInfoCompat.mActivity;
            this.mInfo.mLabel = shortcutInfoCompat.mLabel;
            this.mInfo.mLongLabel = shortcutInfoCompat.mLongLabel;
            this.mInfo.mDisabledMessage = shortcutInfoCompat.mDisabledMessage;
            this.mInfo.mIcon = shortcutInfoCompat.mIcon;
            this.mInfo.mIsAlwaysBadged = shortcutInfoCompat.mIsAlwaysBadged;
            this.mInfo.mIsLongLived = shortcutInfoCompat.mIsLongLived;
            if (shortcutInfoCompat.mPersons != null) {
                this.mInfo.mPersons = Arrays.copyOf(shortcutInfoCompat.mPersons, shortcutInfoCompat.mPersons.length);
            }
            if (shortcutInfoCompat.mCategories != null) {
                this.mInfo.mCategories = new HashSet<String>(shortcutInfoCompat.mCategories);
            }
        }
        
        @NonNull
        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut must have a non-empty label");
            }
            if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            }
            throw new IllegalArgumentException("Shortcut must have an intent");
        }
        
        @NonNull
        public Builder setActivity(@NonNull final ComponentName mActivity) {
            this.mInfo.mActivity = mActivity;
            return this;
        }
        
        @NonNull
        public Builder setAlwaysBadged() {
            this.mInfo.mIsAlwaysBadged = true;
            return this;
        }
        
        @NonNull
        public Builder setCategories(@NonNull final Set<String> mCategories) {
            this.mInfo.mCategories = mCategories;
            return this;
        }
        
        @NonNull
        public Builder setDisabledMessage(@NonNull final CharSequence mDisabledMessage) {
            this.mInfo.mDisabledMessage = mDisabledMessage;
            return this;
        }
        
        @NonNull
        public Builder setIcon(final IconCompat mIcon) {
            this.mInfo.mIcon = mIcon;
            return this;
        }
        
        @NonNull
        public Builder setIntent(@NonNull final Intent intent) {
            return this.setIntents(new Intent[] { intent });
        }
        
        @NonNull
        public Builder setIntents(@NonNull final Intent[] mIntents) {
            this.mInfo.mIntents = mIntents;
            return this;
        }
        
        @NonNull
        public Builder setLongLabel(@NonNull final CharSequence mLongLabel) {
            this.mInfo.mLongLabel = mLongLabel;
            return this;
        }
        
        @NonNull
        public Builder setLongLived() {
            this.mInfo.mIsLongLived = true;
            return this;
        }
        
        @NonNull
        public Builder setPerson(@NonNull final Person person) {
            return this.setPersons(new Person[] { person });
        }
        
        @NonNull
        public Builder setPersons(@NonNull final Person[] mPersons) {
            this.mInfo.mPersons = mPersons;
            return this;
        }
        
        @NonNull
        public Builder setShortLabel(@NonNull final CharSequence mLabel) {
            this.mInfo.mLabel = mLabel;
            return this;
        }
    }
}
