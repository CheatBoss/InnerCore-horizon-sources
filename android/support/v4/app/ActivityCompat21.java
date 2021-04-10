package android.support.v4.app;

import android.app.*;
import android.media.session.*;
import android.view.*;
import android.graphics.*;
import android.os.*;
import android.content.*;
import java.util.*;

class ActivityCompat21
{
    private static SharedElementCallback createCallback(final SharedElementCallback21 sharedElementCallback21) {
        if (sharedElementCallback21 != null) {
            return new SharedElementCallbackImpl(sharedElementCallback21);
        }
        return null;
    }
    
    public static void finishAfterTransition(final Activity activity) {
        activity.finishAfterTransition();
    }
    
    public static void postponeEnterTransition(final Activity activity) {
        activity.postponeEnterTransition();
    }
    
    public static void setEnterSharedElementCallback(final Activity activity, final SharedElementCallback21 sharedElementCallback21) {
        activity.setEnterSharedElementCallback(createCallback(sharedElementCallback21));
    }
    
    public static void setExitSharedElementCallback(final Activity activity, final SharedElementCallback21 sharedElementCallback21) {
        activity.setExitSharedElementCallback(createCallback(sharedElementCallback21));
    }
    
    public static void setMediaController(final Activity activity, final Object o) {
        activity.setMediaController((MediaController)o);
    }
    
    public static void startPostponedEnterTransition(final Activity activity) {
        activity.startPostponedEnterTransition();
    }
    
    public abstract static class SharedElementCallback21
    {
        public abstract Parcelable onCaptureSharedElementSnapshot(final View p0, final Matrix p1, final RectF p2);
        
        public abstract View onCreateSnapshotView(final Context p0, final Parcelable p1);
        
        public abstract void onMapSharedElements(final List<String> p0, final Map<String, View> p1);
        
        public abstract void onRejectSharedElements(final List<View> p0);
        
        public abstract void onSharedElementEnd(final List<String> p0, final List<View> p1, final List<View> p2);
        
        public abstract void onSharedElementStart(final List<String> p0, final List<View> p1, final List<View> p2);
    }
    
    private static class SharedElementCallbackImpl extends SharedElementCallback
    {
        private SharedElementCallback21 mCallback;
        
        public SharedElementCallbackImpl(final SharedElementCallback21 mCallback) {
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
    }
}
