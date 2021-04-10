package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.toolkit.ui.*;
import android.view.*;
import com.microsoft.xbox.toolkit.anim.*;
import java.util.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xbox.toolkit.*;

public abstract class ViewModelBase implements XLEObserver<UpdateData>
{
    protected static int LAUNCH_TIME_OUT = 5000;
    public static final String TAG_PAGE_LOADING_TIME = "performance_measure_page_loadingtime";
    protected int LifetimeInMinutes;
    protected AdapterBase adapter;
    protected boolean isActive;
    protected boolean isForeground;
    protected boolean isLaunching;
    protected Runnable launchTimeoutHandler;
    protected int listIndex;
    private NavigationData nextScreenData;
    protected int offset;
    private boolean onlyProcessExceptionsAndShowToastsWhenActive;
    private ViewModelBase parent;
    private final ScreenLayout screen;
    private boolean shouldHideScreen;
    private boolean showNoNetworkPopup;
    private HashMap<UpdateType, XLEException> updateExceptions;
    private EnumSet<UpdateType> updateTypesToCheck;
    private boolean updating;
    
    public ViewModelBase() {
        this(null, true, false);
    }
    
    public ViewModelBase(final ScreenLayout screenLayout) {
        this(screenLayout, true, false);
    }
    
    public ViewModelBase(final ScreenLayout screen, final boolean showNoNetworkPopup, final boolean onlyProcessExceptionsAndShowToastsWhenActive) {
        this.LifetimeInMinutes = 60;
        this.updateExceptions = new HashMap<UpdateType, XLEException>();
        this.showNoNetworkPopup = true;
        this.onlyProcessExceptionsAndShowToastsWhenActive = false;
        this.nextScreenData = null;
        this.updating = false;
        this.isLaunching = false;
        this.screen = screen;
        this.showNoNetworkPopup = showNoNetworkPopup;
        this.onlyProcessExceptionsAndShowToastsWhenActive = onlyProcessExceptionsAndShowToastsWhenActive;
    }
    
    public ViewModelBase(final boolean b, final boolean b2) {
        this(null, b, b2);
    }
    
    private boolean shouldProcessErrors() {
        return !this.onlyProcessExceptionsAndShowToastsWhenActive || this.isActive;
    }
    
    protected void NavigateTo(final Class<? extends ScreenLayout> clazz) {
        this.NavigateTo(clazz, null);
    }
    
    protected void NavigateTo(final Class<? extends ScreenLayout> clazz, final ActivityParameters activityParameters) {
        this.NavigateTo(clazz, true, activityParameters);
    }
    
    protected void NavigateTo(final Class<? extends ScreenLayout> clazz, final boolean b) {
        this.NavigateTo(clazz, b, null);
    }
    
    protected void NavigateTo(final Class<? extends ScreenLayout> clazz, final boolean b, final ActivityParameters activityParameters) {
        this.cancelLaunchTimeout();
        XLEAssert.assertFalse("We shouldn't navigate to a new screen if the current screen is blocking", this.isBlockingBusy());
        if (this.updating) {
            NavigationType navigationType;
            if (b) {
                navigationType = NavigationType.Push;
            }
            else {
                navigationType = NavigationType.PopReplace;
            }
            this.nextScreenData = new NavigationData(clazz, navigationType);
            return;
        }
        XLEAssert.assertFalse("We shouldn't navigate to a new screen if the current screen is blocking", this.isBlockingBusy());
        NavigationManager.getInstance().NavigateTo(clazz, b, activityParameters);
    }
    
    public void TEST_induceGoBack() {
    }
    
    protected void adapterUpdateView() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.updateView();
        }
    }
    
    public void cancelLaunch() {
        this.isLaunching = false;
    }
    
    protected void cancelLaunchTimeout() {
        this.isLaunching = false;
        if (this.launchTimeoutHandler != null) {
            ThreadManager.Handler.removeCallbacks(this.launchTimeoutHandler);
        }
    }
    
    protected boolean checkErrorCode(final UpdateType updateType, final long n) {
        return this.updateExceptions.containsKey(updateType) && this.updateExceptions.get(updateType).getErrorCode() == n && !this.updateExceptions.get(updateType).getIsHandled();
    }
    
    public View findViewById(final int n) {
        final ScreenLayout screen = this.screen;
        if (screen != null) {
            return screen.xleFindViewId(n);
        }
        return null;
    }
    
    public void forceRefresh() {
        this.load(true);
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.updateView();
        }
    }
    
    public void forceUpdateViewImmediately() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.forceUpdateViewImmediately();
        }
    }
    
    public AdapterBase getAdapter() {
        return this.adapter;
    }
    
    public int getAndResetListOffset() {
        final int offset = this.offset;
        this.offset = 0;
        return offset;
    }
    
    public int getAndResetListPosition() {
        final int listIndex = this.listIndex;
        this.listIndex = 0;
        return listIndex;
    }
    
    public XLEAnimationPackage getAnimateIn(final boolean b) {
        final ArrayList<XLEAnimation> animateIn = this.adapter.getAnimateIn(b);
        if (animateIn != null && animateIn.size() > 0) {
            final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
            final Iterator<XLEAnimation> iterator = animateIn.iterator();
            while (iterator.hasNext()) {
                xleAnimationPackage.add(iterator.next());
            }
            return xleAnimationPackage;
        }
        return null;
    }
    
    public XLEAnimationPackage getAnimateOut(final boolean b) {
        final ArrayList<XLEAnimation> animateOut = this.adapter.getAnimateOut(b);
        if (animateOut != null && animateOut.size() > 0) {
            final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
            final Iterator<XLEAnimation> iterator = animateOut.iterator();
            while (iterator.hasNext()) {
                xleAnimationPackage.add(iterator.next());
            }
            return xleAnimationPackage;
        }
        return null;
    }
    
    public String getBlockingStatusText() {
        return null;
    }
    
    public boolean getIsActive() {
        return this.isActive;
    }
    
    protected ViewModelBase getParent() {
        return this.parent;
    }
    
    public ScreenLayout getScreen() {
        return this.screen;
    }
    
    public boolean getShouldHideScreen() {
        return this.shouldHideScreen;
    }
    
    public boolean getShowNoNetworkPopup() {
        return this.showNoNetworkPopup;
    }
    
    public boolean isBlockingBusy() {
        return false;
    }
    
    public abstract boolean isBusy();
    
    public void leaveViewModel(final Runnable runnable) {
        runnable.run();
    }
    
    public void load() {
        this.load(XLEGlobalData.getInstance().CheckDrainShouldRefresh(this.getClass()));
    }
    
    public abstract void load(final boolean p0);
    
    protected void logOut(final boolean b) {
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }
    
    public void onAnimateInCompleted() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onAnimateInCompleted();
        }
    }
    
    public void onApplicationPause() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onApplicationPause();
        }
    }
    
    public void onApplicationResume() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onApplicationResume();
        }
    }
    
    public boolean onBackButtonPressed() {
        return false;
    }
    
    protected void onChildViewModelChanged(final ViewModelBase viewModelBase) {
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    public void onDestroy() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onDestroy();
        }
        this.adapter = null;
    }
    
    public void onPause() {
        this.cancelLaunchTimeout();
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onPause();
        }
    }
    
    public abstract void onRehydrate();
    
    public void onRestoreInstanceState(final Bundle bundle) {
    }
    
    public void onResume() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onResume();
            this.adapter.updateView();
        }
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
    }
    
    public void onSetActive() {
        this.isActive = true;
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onSetActive();
        }
    }
    
    public void onSetInactive() {
        DialogManager.getInstance().dismissToast();
        this.isActive = false;
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onSetInactive();
        }
    }
    
    public void onStart() {
        this.isForeground = true;
        this.onStartOverride();
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onStart();
        }
    }
    
    protected abstract void onStartOverride();
    
    public void onStop() {
        this.isForeground = false;
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onStop();
        }
        DialogManager.getInstance().dismissBlocking();
        if (this.shouldDismissTopNoFatalAlert()) {
            DialogManager.getInstance().dismissTopNonFatalAlert();
        }
        DialogManager.getInstance().dismissToast();
        this.onStopOverride();
    }
    
    protected abstract void onStopOverride();
    
    public void onTombstone() {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.onDestroy();
        }
        this.adapter = null;
    }
    
    protected void onUpdateFinished() {
        this.updateTypesToCheck = null;
        this.updateExceptions.clear();
    }
    
    public void setAsPivotPane() {
        this.showNoNetworkPopup = true;
        this.onlyProcessExceptionsAndShowToastsWhenActive = true;
    }
    
    public void setListPosition(final int listIndex, final int offset) {
        this.listIndex = listIndex;
        this.offset = offset;
    }
    
    protected void setParent(final ViewModelBase parent) {
        this.parent = parent;
    }
    
    public void setScreenState(final int screenState) {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.setScreenState(screenState);
        }
    }
    
    public void setShouldHideScreen(final boolean shouldHideScreen) {
        this.shouldHideScreen = shouldHideScreen;
    }
    
    protected void setUpdateTypesToCheck(final EnumSet<UpdateType> updateTypesToCheck) {
        this.updateTypesToCheck = updateTypesToCheck;
        this.updateExceptions.clear();
    }
    
    protected boolean shouldDismissTopNoFatalAlert() {
        return true;
    }
    
    public boolean shouldRefreshAsPivotHeader() {
        return false;
    }
    
    protected void showError(final int n) {
        DialogManager.getInstance().showToast(n);
    }
    
    protected void showMustActDialog(final String s, final String s2, final String s3, final Runnable runnable, final boolean b) {
    }
    
    protected void showOkCancelDialog(final String s, final String s2, final Runnable runnable, final String s3, final Runnable runnable2) {
        this.showOkCancelDialog(null, s, s2, runnable, s3, runnable2);
    }
    
    protected void showOkCancelDialog(final String s, final String s2, final String s3, final Runnable runnable, final String s4, final Runnable runnable2) {
        if (!this.shouldProcessErrors()) {
            return;
        }
        XLEUtil.showOkCancelDialog(s, s2, s3, runnable, s4, runnable2);
    }
    
    @Override
    public final void update(final AsyncResult<UpdateData> asyncResult) {
        this.updating = true;
        XLEAssert.assertTrue(this.nextScreenData == null);
        this.nextScreenData = null;
        if (asyncResult.getException() != null) {
            final long errorCode = asyncResult.getException().getErrorCode();
            if (!asyncResult.getException().getIsHandled() && errorCode == 1005L) {
                asyncResult.getException().setIsHandled(true);
            }
        }
        if (this.nextScreenData == null && (this.adapter != null || this.updateWithoutAdapter())) {
            this.updateOverride(asyncResult);
        }
        this.updating = false;
        if (this.nextScreenData != null) {
            try {
                final int n = ViewModelBase$1.$SwitchMap$com$microsoft$xbox$xle$viewmodel$ViewModelBase$NavigationType[this.nextScreenData.getNavigationType().ordinal()];
                if (n != 1) {
                    if (n != 2) {
                        if (n == 3) {
                            NavigationManager.getInstance().GotoScreenWithPop(this.nextScreenData.getScreenClass());
                        }
                    }
                    else {
                        NavigationManager.getInstance().NavigateTo(this.nextScreenData.getScreenClass(), false);
                    }
                }
                else {
                    NavigationManager.getInstance().NavigateTo(this.nextScreenData.getScreenClass(), true);
                }
            }
            catch (XLEException ex) {}
        }
        else if (this.shouldProcessErrors()) {
            if (asyncResult.getException() != null && !asyncResult.getException().getIsHandled()) {
                final EnumSet<UpdateType> updateTypesToCheck = this.updateTypesToCheck;
                if (updateTypesToCheck != null && updateTypesToCheck.contains(asyncResult.getResult().getUpdateType())) {
                    this.updateExceptions.put(asyncResult.getResult().getUpdateType(), asyncResult.getException());
                }
            }
            if (asyncResult.getResult().getIsFinal()) {
                final EnumSet<UpdateType> updateTypesToCheck2 = this.updateTypesToCheck;
                if (updateTypesToCheck2 != null) {
                    updateTypesToCheck2.remove(asyncResult.getResult().getUpdateType());
                }
                final EnumSet<UpdateType> updateTypesToCheck3 = this.updateTypesToCheck;
                if (updateTypesToCheck3 == null || updateTypesToCheck3.isEmpty()) {
                    this.onUpdateFinished();
                    this.updateTypesToCheck = null;
                }
            }
        }
        this.nextScreenData = null;
    }
    
    protected void updateAdapter() {
        this.updateAdapter(true);
    }
    
    protected void updateAdapter(final boolean b) {
        final AdapterBase adapter = this.adapter;
        if (adapter != null) {
            adapter.updateView();
        }
        final ViewModelBase parent = this.parent;
        if (parent != null && b) {
            parent.onChildViewModelChanged(this);
        }
    }
    
    protected void updateOverride(final AsyncResult<UpdateData> asyncResult) {
    }
    
    protected boolean updateTypesToCheckHadAnyErrors() {
        return this.updateExceptions.isEmpty() ^ true;
    }
    
    protected boolean updateTypesToCheckIsEmpty() {
        final EnumSet<UpdateType> updateTypesToCheck = this.updateTypesToCheck;
        return updateTypesToCheck == null || updateTypesToCheck.isEmpty();
    }
    
    protected boolean updateWithoutAdapter() {
        return false;
    }
    
    private class NavigationData
    {
        private NavigationType navigationType;
        private Class<? extends ScreenLayout> screenClass;
        
        protected NavigationData(final Class<? extends ScreenLayout> screenClass, final NavigationType navigationType) {
            this.screenClass = screenClass;
            this.navigationType = navigationType;
        }
        
        protected NavigationType getNavigationType() {
            return this.navigationType;
        }
        
        protected Class<? extends ScreenLayout> getScreenClass() {
            return this.screenClass;
        }
    }
    
    private enum NavigationType
    {
        PopAll, 
        PopReplace, 
        Push;
    }
}
