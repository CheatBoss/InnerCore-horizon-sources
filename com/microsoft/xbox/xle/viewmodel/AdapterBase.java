package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.xle.app.module.*;
import android.view.*;
import com.microsoft.xboxtcui.*;
import java.util.*;
import com.microsoft.xbox.toolkit.anim.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.app.*;

public abstract class AdapterBase
{
    public static String ALLOCATION_TAG = "ADAPTERBASE";
    private static HashMap<String, Integer> adapterCounter;
    protected boolean isActive;
    private boolean isStarted;
    private ArrayList<ScreenModuleLayout> screenModules;
    private final ViewModelBase viewModel;
    
    static {
        AdapterBase.adapterCounter = new HashMap<String, Integer>();
    }
    
    public AdapterBase() {
        this(null);
    }
    
    public AdapterBase(final ViewModelBase viewModel) {
        this.isActive = false;
        this.isStarted = false;
        this.screenModules = new ArrayList<ScreenModuleLayout>();
        this.viewModel = viewModel;
        XLEAllocationTracker.getInstance().debugIncrement(AdapterBase.ALLOCATION_TAG, this.getClass().getSimpleName());
        XLEAllocationTracker.getInstance().debugPrintOverallocated(AdapterBase.ALLOCATION_TAG);
    }
    
    public void finalize() {
        XLEAllocationTracker.getInstance().debugDecrement(AdapterBase.ALLOCATION_TAG, this.getClass().getSimpleName());
        XLEAllocationTracker.getInstance().debugPrintOverallocated(AdapterBase.ALLOCATION_TAG);
    }
    
    protected void findAndInitializeModuleById(final int n, final ViewModelBase viewModel) {
        final View viewById = this.findViewById(n);
        if (viewById != null && viewById instanceof ScreenModuleLayout) {
            final ScreenModuleLayout screenModuleLayout = (ScreenModuleLayout)this.findViewById(n);
            screenModuleLayout.setViewModel(viewModel);
            this.screenModules.add(screenModuleLayout);
        }
    }
    
    public View findViewById(final int n) {
        final ViewModelBase viewModel = this.viewModel;
        View viewById;
        if (viewModel != null) {
            viewById = viewModel.findViewById(n);
        }
        else {
            viewById = null;
        }
        if (viewById != null) {
            return viewById;
        }
        return XboxTcuiSdk.getActivity().findViewById(n);
    }
    
    public void forceUpdateViewImmediately() {
        XLEAssert.assertIsUIThread();
        this.updateViewOverride();
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().updateView();
        }
    }
    
    public ArrayList<XLEAnimation> getAnimateIn(final boolean b) {
        return null;
    }
    
    public ArrayList<XLEAnimation> getAnimateOut(final boolean b) {
        return null;
    }
    
    protected boolean getIsStarted() {
        return this.isStarted;
    }
    
    public void invalidateView() {
        if (!NavigationManager.getInstance().isAnimating()) {
            this.invalidateViewOverride();
            final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
            while (iterator.hasNext()) {
                iterator.next().invalidateView();
            }
        }
    }
    
    protected void invalidateViewOverride() {
    }
    
    public void onAnimateInCompleted() {
    }
    
    protected void onAppBarButtonsAdded() {
    }
    
    @Deprecated
    protected void onAppBarUpdated() {
    }
    
    public void onApplicationPause() {
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onApplicationPause();
        }
    }
    
    public void onApplicationResume() {
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onApplicationResume();
        }
    }
    
    public void onDestroy() {
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDestroy();
        }
        this.screenModules.clear();
    }
    
    public void onPause() {
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPause();
        }
    }
    
    public void onResume() {
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onResume();
        }
    }
    
    public void onSetActive() {
        this.isActive = true;
        if (XboxTcuiSdk.getActivity() != null && this.isStarted) {
            this.updateView();
        }
    }
    
    public void onSetInactive() {
        this.isActive = false;
    }
    
    public void onStart() {
        this.isStarted = true;
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStart();
        }
    }
    
    public void onStop() {
        this.isStarted = false;
        final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
        while (iterator.hasNext()) {
            iterator.next().onStop();
        }
    }
    
    protected void setBlocking(final boolean b, final String s) {
        DialogManager.getInstance().setBlocking(b, s);
    }
    
    protected void setCancelableBlocking(final boolean b, final String s, final Runnable runnable) {
        DialogManager.getInstance().setCancelableBlocking(b, s, runnable);
    }
    
    public void setScreenState(final int n) {
    }
    
    protected void showKeyboard(final View view, final int n) {
        XLEUtil.showKeyboard(view, n);
    }
    
    public void updateView() {
        if (!NavigationManager.getInstance().isAnimating()) {
            this.updateViewOverride();
            final Iterator<ScreenModuleLayout> iterator = this.screenModules.iterator();
            while (iterator.hasNext()) {
                iterator.next().updateView();
            }
        }
    }
    
    protected abstract void updateViewOverride();
}
