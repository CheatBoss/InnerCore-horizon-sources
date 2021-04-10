package android.support.v4.app;

import android.content.res.*;
import java.io.*;
import android.support.annotation.*;
import java.util.*;
import android.view.*;
import android.content.*;
import android.util.*;
import android.os.*;
import android.support.v4.util.*;

public class FragmentController
{
    private final FragmentHostCallback<?> mHost;
    
    private FragmentController(final FragmentHostCallback<?> mHost) {
        this.mHost = mHost;
    }
    
    public static final FragmentController createController(final FragmentHostCallback<?> fragmentHostCallback) {
        return new FragmentController(fragmentHostCallback);
    }
    
    public void attachHost(final Fragment fragment) {
        this.mHost.mFragmentManager.attachController(this.mHost, this.mHost, fragment);
    }
    
    public void dispatchActivityCreated() {
        this.mHost.mFragmentManager.dispatchActivityCreated();
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        this.mHost.mFragmentManager.dispatchConfigurationChanged(configuration);
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchContextItemSelected(menuItem);
    }
    
    public void dispatchCreate() {
        this.mHost.mFragmentManager.dispatchCreate();
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater);
    }
    
    public void dispatchDestroy() {
        this.mHost.mFragmentManager.dispatchDestroy();
    }
    
    public void dispatchDestroyView() {
        this.mHost.mFragmentManager.dispatchDestroyView();
    }
    
    public void dispatchLowMemory() {
        this.mHost.mFragmentManager.dispatchLowMemory();
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchOptionsItemSelected(menuItem);
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        this.mHost.mFragmentManager.dispatchOptionsMenuClosed(menu);
    }
    
    public void dispatchPause() {
        this.mHost.mFragmentManager.dispatchPause();
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(menu);
    }
    
    public void dispatchReallyStop() {
        this.mHost.mFragmentManager.dispatchReallyStop();
    }
    
    public void dispatchResume() {
        this.mHost.mFragmentManager.dispatchResume();
    }
    
    public void dispatchStart() {
        this.mHost.mFragmentManager.dispatchStart();
    }
    
    public void dispatchStop() {
        this.mHost.mFragmentManager.dispatchStop();
    }
    
    public void doLoaderDestroy() {
        this.mHost.doLoaderDestroy();
    }
    
    public void doLoaderRetain() {
        this.mHost.doLoaderRetain();
    }
    
    public void doLoaderStart() {
        this.mHost.doLoaderStart();
    }
    
    public void doLoaderStop(final boolean b) {
        this.mHost.doLoaderStop(b);
    }
    
    public void dumpLoaders(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.mHost.dumpLoaders(s, fileDescriptor, printWriter, array);
    }
    
    public boolean execPendingActions() {
        return this.mHost.mFragmentManager.execPendingActions();
    }
    
    @Nullable
    Fragment findFragmentByWho(final String s) {
        return this.mHost.mFragmentManager.findFragmentByWho(s);
    }
    
    public List<Fragment> getActiveFragments(final List<Fragment> list) {
        if (this.mHost.mFragmentManager.mActive == null) {
            return null;
        }
        List<Fragment> list2;
        if ((list2 = list) == null) {
            list2 = new ArrayList<Fragment>(this.getActiveFragmentsCount());
        }
        list2.addAll(this.mHost.mFragmentManager.mActive);
        return list2;
    }
    
    public int getActiveFragmentsCount() {
        final ArrayList<Fragment> mActive = this.mHost.mFragmentManager.mActive;
        if (mActive == null) {
            return 0;
        }
        return mActive.size();
    }
    
    public FragmentManager getSupportFragmentManager() {
        return this.mHost.getFragmentManagerImpl();
    }
    
    public LoaderManager getSupportLoaderManager() {
        return this.mHost.getLoaderManagerImpl();
    }
    
    public void noteStateNotSaved() {
        this.mHost.mFragmentManager.noteStateNotSaved();
    }
    
    public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        return this.mHost.mFragmentManager.onCreateView(view, s, context, set);
    }
    
    public void reportLoaderStart() {
        this.mHost.reportLoaderStart();
    }
    
    public void restoreAllState(final Parcelable parcelable, final List<Fragment> list) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, list);
    }
    
    public void restoreLoaderNonConfig(final SimpleArrayMap<String, LoaderManager> simpleArrayMap) {
        this.mHost.restoreLoaderNonConfig(simpleArrayMap);
    }
    
    public SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        return this.mHost.retainLoaderNonConfig();
    }
    
    public List<Fragment> retainNonConfig() {
        return this.mHost.mFragmentManager.retainNonConfig();
    }
    
    public Parcelable saveAllState() {
        return this.mHost.mFragmentManager.saveAllState();
    }
}
