package androidx.core.app;

import android.view.*;
import android.content.*;
import androidx.core.util.*;
import android.graphics.*;
import android.app.*;
import android.os.*;
import androidx.annotation.*;

public class ActivityOptionsCompat
{
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";
    
    protected ActivityOptionsCompat() {
    }
    
    @NonNull
    public static ActivityOptionsCompat makeBasic() {
        if (Build$VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeBasic());
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeClipRevealAnimation(@NonNull final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeClipRevealAnimation(view, n, n2, n3, n4));
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeCustomAnimation(@NonNull final Context context, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation(context, n, n2));
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeScaleUpAnimation(@NonNull final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeScaleUpAnimation(view, n, n2, n3, n4));
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeSceneTransitionAnimation(@NonNull final Activity activity, @NonNull final View view, @NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation(activity, view, s));
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeSceneTransitionAnimation(@NonNull final Activity activity, final Pair<View, String>... array) {
        if (Build$VERSION.SDK_INT >= 21) {
            android.util.Pair[] array2 = null;
            if (array != null) {
                final android.util.Pair[] array3 = new android.util.Pair[array.length];
                int n = 0;
                while (true) {
                    array2 = array3;
                    if (n >= array.length) {
                        break;
                    }
                    array3[n] = android.util.Pair.create((Object)array[n].first, (Object)array[n].second);
                    ++n;
                }
            }
            return new ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation(activity, array2));
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        if (Build$VERSION.SDK_INT >= 21) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeTaskLaunchBehind());
        }
        return new ActivityOptionsCompat();
    }
    
    @NonNull
    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(@NonNull final View view, @NonNull final Bitmap bitmap, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeThumbnailScaleUpAnimation(view, bitmap, n, n2));
        }
        return new ActivityOptionsCompat();
    }
    
    @Nullable
    public Rect getLaunchBounds() {
        return null;
    }
    
    public void requestUsageTimeReport(@NonNull final PendingIntent pendingIntent) {
    }
    
    @NonNull
    public ActivityOptionsCompat setLaunchBounds(@Nullable final Rect rect) {
        return this;
    }
    
    @Nullable
    public Bundle toBundle() {
        return null;
    }
    
    public void update(@NonNull final ActivityOptionsCompat activityOptionsCompat) {
    }
    
    @RequiresApi(16)
    private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat
    {
        private final ActivityOptions mActivityOptions;
        
        ActivityOptionsCompatImpl(final ActivityOptions mActivityOptions) {
            this.mActivityOptions = mActivityOptions;
        }
        
        @Override
        public Rect getLaunchBounds() {
            if (Build$VERSION.SDK_INT < 24) {
                return null;
            }
            return this.mActivityOptions.getLaunchBounds();
        }
        
        @Override
        public void requestUsageTimeReport(final PendingIntent pendingIntent) {
            if (Build$VERSION.SDK_INT >= 23) {
                this.mActivityOptions.requestUsageTimeReport(pendingIntent);
            }
        }
        
        @Override
        public ActivityOptionsCompat setLaunchBounds(@Nullable final Rect launchBounds) {
            if (Build$VERSION.SDK_INT < 24) {
                return this;
            }
            return new ActivityOptionsCompatImpl(this.mActivityOptions.setLaunchBounds(launchBounds));
        }
        
        @Override
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }
        
        @Override
        public void update(final ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsCompatImpl) {
                this.mActivityOptions.update(((ActivityOptionsCompatImpl)activityOptionsCompat).mActivityOptions);
            }
        }
    }
}
