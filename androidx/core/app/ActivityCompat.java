package androidx.core.app;

import androidx.core.content.*;
import android.net.*;
import androidx.core.view.*;
import android.content.pm.*;
import android.view.*;
import androidx.annotation.*;
import android.graphics.*;
import android.os.*;
import android.content.*;
import java.util.*;
import android.app.*;

public class ActivityCompat extends ContextCompat
{
    private static PermissionCompatDelegate sDelegate;
    
    protected ActivityCompat() {
    }
    
    public static void finishAffinity(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
            return;
        }
        activity.finish();
    }
    
    public static void finishAfterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
            return;
        }
        activity.finish();
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return ActivityCompat.sDelegate;
    }
    
    @Nullable
    public static Uri getReferrer(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 22) {
            return activity.getReferrer();
        }
        final Intent intent = activity.getIntent();
        final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        final String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (stringExtra != null) {
            return Uri.parse(stringExtra);
        }
        return null;
    }
    
    @Deprecated
    public static boolean invalidateOptionsMenu(final Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }
    
    public static void postponeEnterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
        }
    }
    
    public static void recreate(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 28) {
            activity.recreate();
            return;
        }
        if (!ActivityRecreator.recreate(activity)) {
            activity.recreate();
        }
    }
    
    @Nullable
    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(final Activity activity, final DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }
    
    public static void requestPermissions(@NonNull final Activity activity, @NonNull final String[] array, @IntRange(from = 0L) final int n) {
        if (ActivityCompat.sDelegate != null && ActivityCompat.sDelegate.requestPermissions(activity, array, n)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(array, n);
            return;
        }
        if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final int[] array = new int[array.length];
                    final PackageManager packageManager = activity.getPackageManager();
                    final String packageName = activity.getPackageName();
                    for (int length = array.length, i = 0; i < length; ++i) {
                        array[i] = packageManager.checkPermission(array[i], packageName);
                    }
                    ((OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(n, array, array);
                }
            });
        }
    }
    
    @NonNull
    public static <T extends View> T requireViewById(@NonNull final Activity activity, @IdRes final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T)activity.requireViewById(n);
        }
        final View viewById = activity.findViewById(n);
        if (viewById == null) {
            throw new IllegalArgumentException("ID does not reference a View inside this Activity");
        }
        return (T)viewById;
    }
    
    public static void setEnterSharedElementCallback(@NonNull final Activity activity, @Nullable final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl enterSharedElementCallback;
            if (sharedElementCallback != null) {
                enterSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                enterSharedElementCallback = null;
            }
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)enterSharedElementCallback);
        }
    }
    
    public static void setExitSharedElementCallback(@NonNull final Activity activity, @Nullable final SharedElementCallback sharedElementCallback) {
        if (Build$VERSION.SDK_INT >= 21) {
            SharedElementCallback21Impl exitSharedElementCallback;
            if (sharedElementCallback != null) {
                exitSharedElementCallback = new SharedElementCallback21Impl(sharedElementCallback);
            }
            else {
                exitSharedElementCallback = null;
            }
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)exitSharedElementCallback);
        }
    }
    
    public static void setPermissionCompatDelegate(@Nullable final PermissionCompatDelegate sDelegate) {
        ActivityCompat.sDelegate = sDelegate;
    }
    
    public static boolean shouldShowRequestPermissionRationale(@NonNull final Activity activity, @NonNull final String s) {
        return Build$VERSION.SDK_INT >= 23 && activity.shouldShowRequestPermissionRationale(s);
    }
    
    public static void startActivityForResult(@NonNull final Activity activity, @NonNull final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
            return;
        }
        activity.startActivityForResult(intent, n);
    }
    
    public static void startIntentSenderForResult(@NonNull final Activity activity, @NonNull final IntentSender intentSender, final int n, @Nullable final Intent intent, final int n2, final int n3, final int n4, @Nullable final Bundle bundle) throws IntentSender$SendIntentException {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
    }
    
    public static void startPostponedEnterTransition(@NonNull final Activity activity) {
        if (Build$VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
        }
    }
    
    public interface OnRequestPermissionsResultCallback
    {
        void onRequestPermissionsResult(final int p0, @NonNull final String[] p1, @NonNull final int[] p2);
    }
    
    public interface PermissionCompatDelegate
    {
        boolean onActivityResult(@NonNull final Activity p0, @IntRange(from = 0L) final int p1, final int p2, @Nullable final Intent p3);
        
        boolean requestPermissions(@NonNull final Activity p0, @NonNull final String[] p1, @IntRange(from = 0L) final int p2);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public interface RequestPermissionsRequestCodeValidator
    {
        void validateRequestPermissionsRequestCode(final int p0);
    }
    
    @RequiresApi(21)
    private static class SharedElementCallback21Impl extends android.app.SharedElementCallback
    {
        private final SharedElementCallback mCallback;
        
        SharedElementCallback21Impl(final SharedElementCallback mCallback) {
            this.mCallback = mCallback;
        }
        
        public Parcelable onCaptureSharedElementSnapshot(final View view, final Matrix matrix, final RectF rectF) {
            return this.mCallback.onCaptureSharedElementSnapshot(view, matrix, rectF);
        }
        
        public View onCreateSnapshotView(final Context context, final Parcelable parcelable) {
            return this.mCallback.onCreateSnapshotView(context, parcelable);
        }
        
        public void onMapSharedElements(final List<String> list, final Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }
        
        public void onRejectSharedElements(final List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }
        
        public void onSharedElementEnd(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }
        
        public void onSharedElementStart(final List<String> list, final List<View> list2, final List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }
        
        @RequiresApi(23)
        public void onSharedElementsArrived(final List<String> list, final List<View> list2, final SharedElementCallback$OnSharedElementsReadyListener sharedElementCallback$OnSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, (SharedElementCallback.OnSharedElementsReadyListener)new SharedElementCallback.OnSharedElementsReadyListener() {
                @Override
                public void onSharedElementsReady() {
                    sharedElementCallback$OnSharedElementsReadyListener.onSharedElementsReady();
                }
            });
        }
    }
}
