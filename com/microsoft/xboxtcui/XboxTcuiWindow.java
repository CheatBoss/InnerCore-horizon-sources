package com.microsoft.xboxtcui;

import android.app.*;
import com.microsoft.xbox.toolkit.ui.*;
import java.util.*;
import android.content.*;
import android.util.*;
import android.widget.*;
import android.view.*;
import android.os.*;
import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.xle.app.*;
import com.microsoft.xbox.toolkit.*;

public class XboxTcuiWindow extends FrameLayout implements NavigationCallbacks, OnNavigatedListener
{
    private static final int NAVIGATION_BLOCK_TIMEOUT_MS = 5000;
    private static final String TAG;
    private Activity activity;
    private boolean animationBlocking;
    private final ActivityParameters launchParams;
    private final Class<? extends ScreenLayout> launchScreenClass;
    private final Stack<ScreenLayout> screens;
    private boolean wasRestarted;
    
    static {
        TAG = XboxTcuiWindow.class.getSimpleName();
    }
    
    public XboxTcuiWindow(final Activity activity, final Class<? extends ScreenLayout> launchScreenClass, final ActivityParameters launchParams) {
        super((Context)activity);
        this.screens = new Stack<ScreenLayout>();
        XLEAssert.assertNotNull(launchParams.getMeXuid());
        this.activity = activity;
        this.launchScreenClass = launchScreenClass;
        this.launchParams = launchParams;
        this.setBackgroundResource(R$color.backgroundColor);
    }
    
    private void setupNavigationManager() {
        NavigationManager.getInstance().setNavigationCallbacks((NavigationManager.NavigationCallbacks)this);
        NavigationManager.getInstance().setOnNavigatedListener((NavigationManager.OnNavigatedListener)this);
        try {
            NavigationManager.getInstance().PopAllScreens();
        }
        catch (XLEException ex) {
            final String tag = XboxTcuiWindow.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("setupNavigationManager: ");
            sb.append(Log.getStackTraceString((Throwable)ex));
            Log.e(tag, sb.toString());
        }
    }
    
    private void setupThreadManager() {
        ThreadManager.UIThread = Thread.currentThread();
        ThreadManager.Handler = new Handler();
        final Thread uiThread = ThreadManager.UIThread;
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)XLEUnhandledExceptionHandler.Instance);
    }
    
    public void addContentViewXLE(final ScreenLayout screenLayout) {
        if (!this.screens.isEmpty()) {
            if (screenLayout == this.screens.peek()) {
                screenLayout.setAllEventsEnabled(true);
                return;
            }
            if (screenLayout.isKeepPreviousScreen()) {
                this.screens.peek().setAllEventsEnabled(false);
            }
            else {
                this.removeView((View)this.screens.pop());
            }
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -1);
        relativeLayout$LayoutParams.addRule(10);
        relativeLayout$LayoutParams.addRule(12);
        this.addView((View)screenLayout, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        this.screens.push(screenLayout);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return NavigationManager.getInstance().onKey((View)this, keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyEvent(keyEvent);
    }
    
    public boolean dispatchUnhandledMove(View view, final int n) {
        if (view != this) {
            return false;
        }
        Label_0058: {
            if (n != 1) {
                if (n != 2) {
                    if (n == 33) {
                        break Label_0058;
                    }
                    if (n != 130) {
                        return true;
                    }
                }
                view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, this.getFocusedChild(), 130);
                if (view != null) {
                    view.requestFocus();
                    return true;
                }
                return true;
            }
        }
        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, this.getFocusedChild(), 33);
        if (view != null) {
            view.requestFocus();
        }
        return true;
    }
    
    public void onBeforeNavigatingIn() {
    }
    
    public void onCreate(final Bundle bundle) {
        this.wasRestarted = (bundle != null);
        this.setupThreadManager();
        ProjectSpecificDataProvider.getInstance().setProvider(XleProjectSpecificDataProvider.getInstance());
        final String xuidString = ProjectSpecificDataProvider.getInstance().getXuidString();
        if (!JavaUtil.isNullOrEmpty(xuidString) && !xuidString.equalsIgnoreCase(this.launchParams.getMeXuid())) {
            ProfileModel.getMeProfileModel();
            ProfileModel.reset();
        }
        ProjectSpecificDataProvider.getInstance().setXuidString(this.launchParams.getMeXuid());
        ProjectSpecificDataProvider.getInstance().setPrivileges(this.launchParams.getPrivileges());
        DialogManager.getInstance().setManager(SGProjectSpecificDialogManager.getInstance());
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        this.setupNavigationManager();
    }
    
    public void onPageNavigated(final ScreenLayout screenLayout, final ScreenLayout screenLayout2) {
    }
    
    public void onPageRestarted(final ScreenLayout screenLayout) {
    }
    
    public void onStart() {
        XboxTcuiSdk.sdkInitialize(this.activity);
        DialogManager.getInstance().setEnabled(true);
        try {
            try {
                if (!this.wasRestarted) {
                    NavigationManager.getInstance().PushScreen(this.launchScreenClass, this.launchParams);
                }
                final ScreenLayout currentActivity = NavigationManager.getInstance().getCurrentActivity();
                if (currentActivity != null) {
                    final Bundle bundle = new Bundle();
                    NavigationManager.getInstance().getCurrentActivity().onSaveInstanceState(bundle);
                    NavigationManager.getInstance().RestartCurrentScreen(false);
                    currentActivity.onRestoreInstanceState(bundle);
                }
            }
            finally {}
        }
        catch (XLEException ex) {
            final String tag = XboxTcuiWindow.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("onStart: ");
            sb.append(Log.getStackTraceString((Throwable)ex));
            Log.e(tag, sb.toString());
        }
        this.wasRestarted = false;
        return;
        this.wasRestarted = false;
    }
    
    public void onStop() {
        DialogManager.getInstance().setEnabled(false);
        try {
            NavigationManager.getInstance().PopAllScreens();
        }
        catch (XLEException ex) {
            final String tag = XboxTcuiWindow.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("onStop: ");
            sb.append(Log.getStackTraceString((Throwable)ex));
            Log.e(tag, sb.toString());
        }
    }
    
    public void removeContentViewXLE(final ScreenLayout screenLayout) {
        final int index = this.screens.indexOf(screenLayout);
        if (index >= 0) {
            while (this.screens.size() > index) {
                this.removeView((View)this.screens.pop());
            }
        }
    }
    
    public void setAnimationBlocking(final boolean animationBlocking) {
        if (this.animationBlocking != animationBlocking) {
            this.animationBlocking = animationBlocking;
            if (animationBlocking) {
                BackgroundThreadWaitor.getInstance().setBlocking(BackgroundThreadWaitor.WaitType.Navigation, 5000);
                return;
            }
            BackgroundThreadWaitor.getInstance().clearBlocking(BackgroundThreadWaitor.WaitType.Navigation);
        }
    }
}
