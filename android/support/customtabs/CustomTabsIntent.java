package android.support.customtabs;

import android.net.*;
import java.util.*;
import android.app.*;
import android.os.*;
import android.graphics.*;
import android.content.*;
import android.support.v4.app.*;
import android.support.annotation.*;

public final class CustomTabsIntent
{
    public static final String EXTRA_ACTION_BUTTON_BUNDLE = "android.support.customtabs.extra.ACTION_BUTTON_BUNDLE";
    public static final String EXTRA_CLOSE_BUTTON_ICON = "android.support.customtabs.extra.CLOSE_BUTTON_ICON";
    public static final String EXTRA_DEFAULT_SHARE_MENU_ITEM = "android.support.customtabs.extra.SHARE_MENU_ITEM";
    public static final String EXTRA_ENABLE_URLBAR_HIDING = "android.support.customtabs.extra.ENABLE_URLBAR_HIDING";
    public static final String EXTRA_EXIT_ANIMATION_BUNDLE = "android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE";
    public static final String EXTRA_MENU_ITEMS = "android.support.customtabs.extra.MENU_ITEMS";
    public static final String EXTRA_SECONDARY_TOOLBAR_COLOR = "android.support.customtabs.extra.SECONDARY_TOOLBAR_COLOR";
    public static final String EXTRA_SESSION = "android.support.customtabs.extra.SESSION";
    public static final String EXTRA_TINT_ACTION_BUTTON = "android.support.customtabs.extra.TINT_ACTION_BUTTON";
    public static final String EXTRA_TITLE_VISIBILITY_STATE = "android.support.customtabs.extra.TITLE_VISIBILITY";
    public static final String EXTRA_TOOLBAR_COLOR = "android.support.customtabs.extra.TOOLBAR_COLOR";
    public static final String EXTRA_TOOLBAR_ITEMS = "android.support.customtabs.extra.TOOLBAR_ITEMS";
    public static final String KEY_DESCRIPTION = "android.support.customtabs.customaction.DESCRIPTION";
    public static final String KEY_ICON = "android.support.customtabs.customaction.ICON";
    public static final String KEY_ID = "android.support.customtabs.customaction.ID";
    public static final String KEY_MENU_ITEM_TITLE = "android.support.customtabs.customaction.MENU_ITEM_TITLE";
    public static final String KEY_PENDING_INTENT = "android.support.customtabs.customaction.PENDING_INTENT";
    private static final int MAX_TOOLBAR_ITEMS = 5;
    public static final int NO_TITLE = 0;
    public static final int SHOW_PAGE_TITLE = 1;
    public static final int TOOLBAR_ACTION_BUTTON_ID = 0;
    @NonNull
    public final Intent intent;
    @Nullable
    public final Bundle startAnimationBundle;
    
    private CustomTabsIntent(final Intent intent, final Bundle startAnimationBundle) {
        this.intent = intent;
        this.startAnimationBundle = startAnimationBundle;
    }
    
    public static int getMaxToolbarItems() {
        return 5;
    }
    
    public void launchUrl(final Activity activity, final Uri data) {
        this.intent.setData(data);
        ActivityCompat.startActivity(activity, this.intent, this.startAnimationBundle);
    }
    
    public static final class Builder
    {
        private ArrayList<Bundle> mActionButtons;
        private final Intent mIntent;
        private ArrayList<Bundle> mMenuItems;
        private Bundle mStartAnimationBundle;
        
        public Builder() {
            this(null);
        }
        
        public Builder(@Nullable final CustomTabsSession customTabsSession) {
            this.mIntent = new Intent("android.intent.action.VIEW");
            final IBinder binder = null;
            this.mMenuItems = null;
            this.mStartAnimationBundle = null;
            this.mActionButtons = null;
            if (customTabsSession != null) {
                this.mIntent.setPackage(customTabsSession.getComponentName().getPackageName());
            }
            final Bundle bundle = new Bundle();
            IBinder binder2;
            if (customTabsSession == null) {
                binder2 = binder;
            }
            else {
                binder2 = customTabsSession.getBinder();
            }
            BundleCompat.putBinder(bundle, "android.support.customtabs.extra.SESSION", binder2);
            this.mIntent.putExtras(bundle);
        }
        
        public Builder addDefaultShareMenuItem() {
            this.mIntent.putExtra("android.support.customtabs.extra.SHARE_MENU_ITEM", true);
            return this;
        }
        
        public Builder addMenuItem(@NonNull final String s, @NonNull final PendingIntent pendingIntent) {
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayList<Bundle>();
            }
            final Bundle bundle = new Bundle();
            bundle.putString("android.support.customtabs.customaction.MENU_ITEM_TITLE", s);
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", (Parcelable)pendingIntent);
            this.mMenuItems.add(bundle);
            return this;
        }
        
        public Builder addToolbarItem(final int n, @NonNull final Bitmap bitmap, @NonNull final String s, final PendingIntent pendingIntent) throws IllegalStateException {
            if (this.mActionButtons == null) {
                this.mActionButtons = new ArrayList<Bundle>();
            }
            if (this.mActionButtons.size() >= 5) {
                throw new IllegalStateException("Exceeded maximum toolbar item count of 5");
            }
            final Bundle bundle = new Bundle();
            bundle.putInt("android.support.customtabs.customaction.ID", n);
            bundle.putParcelable("android.support.customtabs.customaction.ICON", (Parcelable)bitmap);
            bundle.putString("android.support.customtabs.customaction.DESCRIPTION", s);
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", (Parcelable)pendingIntent);
            this.mActionButtons.add(bundle);
            return this;
        }
        
        public CustomTabsIntent build() {
            if (this.mMenuItems != null) {
                this.mIntent.putParcelableArrayListExtra("android.support.customtabs.extra.MENU_ITEMS", (ArrayList)this.mMenuItems);
            }
            if (this.mActionButtons != null) {
                this.mIntent.putParcelableArrayListExtra("android.support.customtabs.extra.TOOLBAR_ITEMS", (ArrayList)this.mActionButtons);
            }
            return new CustomTabsIntent(this.mIntent, this.mStartAnimationBundle, null);
        }
        
        public Builder enableUrlBarHiding() {
            this.mIntent.putExtra("android.support.customtabs.extra.ENABLE_URLBAR_HIDING", true);
            return this;
        }
        
        public Builder setActionButton(@NonNull final Bitmap bitmap, @NonNull final String s, @NonNull final PendingIntent pendingIntent) {
            return this.setActionButton(bitmap, s, pendingIntent, false);
        }
        
        public Builder setActionButton(@NonNull final Bitmap bitmap, @NonNull final String s, @NonNull final PendingIntent pendingIntent, final boolean b) {
            final Bundle bundle = new Bundle();
            bundle.putInt("android.support.customtabs.customaction.ID", 0);
            bundle.putParcelable("android.support.customtabs.customaction.ICON", (Parcelable)bitmap);
            bundle.putString("android.support.customtabs.customaction.DESCRIPTION", s);
            bundle.putParcelable("android.support.customtabs.customaction.PENDING_INTENT", (Parcelable)pendingIntent);
            this.mIntent.putExtra("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
            this.mIntent.putExtra("android.support.customtabs.extra.TINT_ACTION_BUTTON", b);
            return this;
        }
        
        public Builder setCloseButtonIcon(@NonNull final Bitmap bitmap) {
            this.mIntent.putExtra("android.support.customtabs.extra.CLOSE_BUTTON_ICON", (Parcelable)bitmap);
            return this;
        }
        
        public Builder setExitAnimations(@NonNull final Context context, @AnimRes final int n, @AnimRes final int n2) {
            this.mIntent.putExtra("android.support.customtabs.extra.EXIT_ANIMATION_BUNDLE", ActivityOptionsCompat.makeCustomAnimation(context, n, n2).toBundle());
            return this;
        }
        
        public Builder setSecondaryToolbarColor(@ColorInt final int n) {
            this.mIntent.putExtra("android.support.customtabs.extra.SECONDARY_TOOLBAR_COLOR", n);
            return this;
        }
        
        public Builder setShowTitle(final boolean b) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
        }
        
        public Builder setStartAnimations(@NonNull final Context context, @AnimRes final int n, @AnimRes final int n2) {
            this.mStartAnimationBundle = ActivityOptionsCompat.makeCustomAnimation(context, n, n2).toBundle();
            return this;
        }
        
        public Builder setToolbarColor(@ColorInt final int n) {
            this.mIntent.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", n);
            return this;
        }
    }
}
