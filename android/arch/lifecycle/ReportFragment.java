package android.arch.lifecycle;

import android.app.*;
import android.os.*;

public class ReportFragment extends Fragment
{
    private ActivityInitializationListener mProcessListener;
    
    private void dispatch(final Lifecycle$Event lifecycle$Event) {
        final Activity activity = this.getActivity();
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner)activity).getLifecycle().handleLifecycleEvent(lifecycle$Event);
            return;
        }
        if (activity instanceof LifecycleOwner) {
            final Lifecycle lifecycle = ((LifecycleOwner)activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry)lifecycle).handleLifecycleEvent(lifecycle$Event);
            }
        }
    }
    
    private void dispatchCreate(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onCreate();
        }
    }
    
    private void dispatchResume(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onResume();
        }
    }
    
    private void dispatchStart(final ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onStart();
        }
    }
    
    public static void injectIfNeededIn(final Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();
        if (fragmentManager.findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
            fragmentManager.beginTransaction().add((Fragment)new ReportFragment(), "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
            fragmentManager.executePendingTransactions();
        }
    }
    
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);
        this.dispatchCreate(this.mProcessListener);
        this.dispatch(Lifecycle$Event.ON_CREATE);
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.dispatch(Lifecycle$Event.ON_DESTROY);
        this.mProcessListener = null;
    }
    
    public void onPause() {
        super.onPause();
        this.dispatch(Lifecycle$Event.ON_PAUSE);
    }
    
    public void onResume() {
        super.onResume();
        this.dispatchResume(this.mProcessListener);
        this.dispatch(Lifecycle$Event.ON_RESUME);
    }
    
    public void onStart() {
        super.onStart();
        this.dispatchStart(this.mProcessListener);
        this.dispatch(Lifecycle$Event.ON_START);
    }
    
    public void onStop() {
        super.onStop();
        this.dispatch(Lifecycle$Event.ON_STOP);
    }
    
    interface ActivityInitializationListener
    {
        void onCreate();
        
        void onResume();
        
        void onStart();
    }
}
