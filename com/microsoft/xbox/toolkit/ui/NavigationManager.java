package com.microsoft.xbox.toolkit.ui;

import com.microsoft.xbox.toolkit.anim.*;
import com.microsoft.xbox.toolkit.*;
import java.util.*;
import com.microsoft.xboxtcui.*;
import android.view.*;

public class NavigationManager implements View$OnKeyListener
{
    private static final String TAG = "NavigationManager";
    private NavigationManagerAnimationState animationState;
    final Runnable callAfterAnimation;
    private boolean cannotNavigateTripwire;
    private XLEAnimationPackage currentAnimation;
    private boolean goingBack;
    private NavigationCallbacks navigationCallbacks;
    private OnNavigatedListener navigationListener;
    private final Stack<ActivityParameters> navigationParameters;
    private final Stack<ScreenLayout> navigationStack;
    private boolean transitionAnimate;
    private Runnable transitionLambda;
    
    private NavigationManager() {
        this.navigationParameters = new Stack<ActivityParameters>();
        this.navigationStack = new Stack<ScreenLayout>();
        this.currentAnimation = null;
        this.animationState = NavigationManagerAnimationState.NONE;
        this.transitionLambda = null;
        boolean b = false;
        this.goingBack = false;
        this.transitionAnimate = true;
        this.cannotNavigateTripwire = false;
        this.callAfterAnimation = new Runnable() {
            @Override
            public void run() {
                NavigationManager.this.OnAnimationEnd();
            }
        };
        if (Thread.currentThread() == ThreadManager.UIThread) {
            b = true;
        }
        XLEAssert.assertTrue("You must access navigation manager on UI thread.", b);
    }
    
    private void OnAnimationEnd() {
        final int n = NavigationManager$3.$SwitchMap$com$microsoft$xbox$toolkit$ui$NavigationManager$NavigationManagerAnimationState[this.animationState.ordinal()];
        if (n == 2) {
            final NavigationCallbacks navigationCallbacks = this.navigationCallbacks;
            if (navigationCallbacks != null) {
                navigationCallbacks.setAnimationBlocking(false);
            }
            this.animationState = NavigationManagerAnimationState.NONE;
            if (this.getCurrentActivity() != null) {
                this.getCurrentActivity().onAnimateInCompleted();
            }
            return;
        }
        if (n != 3) {
            return;
        }
        this.transitionLambda.run();
        XLEAnimationPackage animateIn = null;
        if (this.getCurrentActivity() != null) {
            animateIn = this.getCurrentActivity().getAnimateIn(this.goingBack);
        }
        final NavigationCallbacks navigationCallbacks2 = this.navigationCallbacks;
        if (navigationCallbacks2 != null) {
            navigationCallbacks2.onBeforeNavigatingIn();
        }
        this.startAnimation(animateIn, NavigationManagerAnimationState.ANIMATING_IN);
    }
    
    private void ReplaceOnAnimationEnd(final boolean goingBack, final Runnable transitionLambda, final boolean transitionAnimate) {
        XLEAssert.assertTrue(this.animationState == NavigationManagerAnimationState.ANIMATING_OUT || this.animationState == NavigationManagerAnimationState.ANIMATING_IN);
        this.animationState = NavigationManagerAnimationState.ANIMATING_OUT;
        this.transitionLambda = transitionLambda;
        this.transitionAnimate = transitionAnimate;
        this.goingBack = goingBack;
    }
    
    private int Size() {
        return this.navigationStack.size();
    }
    
    private void Transition(final boolean goingBack, final Runnable transitionLambda, final boolean transitionAnimate) {
        this.transitionLambda = transitionLambda;
        this.transitionAnimate = transitionAnimate;
        this.goingBack = goingBack;
        XLEAnimationPackage animateOut;
        if (this.getCurrentActivity() == null) {
            animateOut = null;
        }
        else {
            animateOut = this.getCurrentActivity().getAnimateOut(goingBack);
        }
        this.startAnimation(this.currentAnimation = animateOut, NavigationManagerAnimationState.ANIMATING_OUT);
    }
    
    public static NavigationManager getInstance() {
        return NavigationManagerHolder.instance;
    }
    
    private void startAnimation(final XLEAnimationPackage currentAnimation, final NavigationManagerAnimationState animationState) {
        this.animationState = animationState;
        this.currentAnimation = currentAnimation;
        final NavigationCallbacks navigationCallbacks = this.navigationCallbacks;
        if (navigationCallbacks != null) {
            navigationCallbacks.setAnimationBlocking(true);
        }
        if (this.transitionAnimate && currentAnimation != null) {
            currentAnimation.setOnAnimationEndRunnable(this.callAfterAnimation);
            currentAnimation.startAnimation();
            return;
        }
        this.callAfterAnimation.run();
    }
    
    public int CountPopsToScreen(final Class<? extends ScreenLayout> clazz) {
        int i;
        for (int n = i = this.navigationStack.size() - 1; i >= 0; --i) {
            if (((ScreenLayout)this.navigationStack.get(i)).getClass().equals(clazz)) {
                return n - i;
            }
        }
        return -1;
    }
    
    public void GotoScreenWithPop(final ActivityParameters activityParameters, Class<? extends ScreenLayout> clazz, final Class<? extends ScreenLayout>... array) throws XLEException {
        int i;
        final int n = i = this.navigationStack.size() - 1;
    Label_0090:
        while (true) {
        Label_0122_Outer:
            while (i >= 0) {
                final Class<? extends ScreenLayout> class1 = this.navigationStack.get(i).getClass();
                for (int length = array.length, j = 0; j < length; ++j) {
                    final Class<? extends ScreenLayout> clazz2 = array[j];
                    if (clazz2 == class1) {
                        final Class<? extends ScreenLayout> clazz3 = clazz2;
                        break Label_0090;
                    }
                }
                --i;
                continue Label_0122_Outer;
                Class<? extends ScreenLayout> clazz3 = null;
                int size;
                if (clazz3 != null) {
                    if (clazz3 == clazz) {
                        if (i == n) {
                            this.RestartCurrentScreen(activityParameters, false);
                            return;
                        }
                        size = n - i;
                        clazz = null;
                    }
                    else {
                        size = n - i;
                    }
                }
                else {
                    size = this.Size();
                }
                while (true) {
                    this.PopScreensAndReplace(size, clazz, true, true, false, activityParameters);
                    return;
                    continue;
                }
            }
            final Class<? extends ScreenLayout> clazz3 = null;
            continue Label_0090;
        }
    }
    
    public void GotoScreenWithPop(final Class<? extends ScreenLayout> clazz) throws XLEException {
        final int countPopsToScreen = this.CountPopsToScreen(clazz);
        if (countPopsToScreen > 0) {
            this.PopScreensAndReplace(countPopsToScreen, null, true, false, false);
            return;
        }
        if (countPopsToScreen < 0) {
            this.PopScreensAndReplace(this.Size(), clazz, true, false, false);
            return;
        }
        this.RestartCurrentScreen(true);
    }
    
    public void GotoScreenWithPush(final Class<? extends ScreenLayout> clazz) throws XLEException {
        final int countPopsToScreen = this.CountPopsToScreen(clazz);
        if (countPopsToScreen > 0) {
            this.PopScreensAndReplace(countPopsToScreen, null, true, false, false);
            return;
        }
        if (countPopsToScreen < 0) {
            this.PopScreensAndReplace(0, clazz, true, false, false);
            return;
        }
        this.RestartCurrentScreen(true);
    }
    
    public void GotoScreenWithPush(final Class<? extends ScreenLayout> clazz, final ActivityParameters activityParameters) throws XLEException {
        final int countPopsToScreen = this.CountPopsToScreen(clazz);
        if (countPopsToScreen > 0) {
            this.PopScreensAndReplace(countPopsToScreen, null, true, false, false, activityParameters);
            return;
        }
        if (countPopsToScreen < 0) {
            this.PopScreensAndReplace(0, clazz, true, false, false, activityParameters);
            return;
        }
        this.RestartCurrentScreen(true);
    }
    
    public boolean IsScreenOnStack(final Class<? extends ScreenLayout> clazz) {
        final Iterator<ScreenLayout> iterator = this.navigationStack.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    public void NavigateTo(final Class<? extends ScreenLayout> clazz, final boolean b) {
        this.NavigateTo(clazz, b, null);
    }
    
    public void NavigateTo(final Class<? extends ScreenLayout> clazz, final boolean b, final ActivityParameters activityParameters) {
        Label_0011: {
            if (!b) {
                break Label_0011;
            }
            try {
                this.PushScreen(clazz, activityParameters);
                return;
                this.PopScreensAndReplace(1, clazz, activityParameters);
            }
            catch (XLEException ex) {}
        }
    }
    
    public boolean OnBackButtonPressed() {
        final boolean shouldBackCloseApp = this.ShouldBackCloseApp();
        if (this.getCurrentActivity() == null || this.getCurrentActivity().onBackButtonPressed()) {
            return shouldBackCloseApp;
        }
        Label_0037: {
            if (!shouldBackCloseApp) {
                break Label_0037;
            }
            try {
                this.PopScreensAndReplace(1, null, false, false, false);
                return shouldBackCloseApp;
                this.PopScreen();
                return shouldBackCloseApp;
            }
            catch (XLEException ex) {
                return shouldBackCloseApp;
            }
        }
    }
    
    public void PopAllScreens() throws XLEException {
        if (this.Size() > 0) {
            this.PopScreensAndReplace(this.Size(), null, false, false, false);
        }
    }
    
    public void PopScreen() throws XLEException {
        this.PopScreens(1);
    }
    
    public void PopScreens(final int n) throws XLEException {
        this.PopScreensAndReplace(n, null);
    }
    
    public void PopScreensAndReplace(final int n, final Class<? extends ScreenLayout> clazz) throws XLEException {
        this.PopScreensAndReplace(n, clazz, null);
    }
    
    public void PopScreensAndReplace(final int n, final Class<? extends ScreenLayout> clazz, final ActivityParameters activityParameters) throws XLEException {
        this.PopScreensAndReplace(n, clazz, true, true, false, activityParameters);
    }
    
    public void PopScreensAndReplace(final int n, final Class<? extends ScreenLayout> clazz, final boolean b, final boolean b2) throws XLEException {
        this.PopScreensAndReplace(n, clazz, b, true, b2);
    }
    
    public void PopScreensAndReplace(final int n, final Class<? extends ScreenLayout> clazz, final boolean b, final boolean b2, final boolean b3) throws XLEException {
        this.PopScreensAndReplace(n, clazz, b, b2, b3, null);
    }
    
    public void PopScreensAndReplace(final int n, Class<? extends ScreenLayout> o, boolean b, final boolean b2, final boolean b3, ActivityParameters activityParameters) throws XLEException {
        XLEAssert.assertTrue("You must access navigation manager on UI thread.", Thread.currentThread() == ThreadManager.UIThread);
        if (this.cannotNavigateTripwire) {
            throw new UnsupportedOperationException("NavigationManager: attempted to execute a recursive navigation in the OnStop/OnStart method.  This is forbidden.");
        }
        Label_0141: {
            if (o != null) {
                if (!b3) {
                    try {
                        final ScreenLayout screenLayout = ((Class<ScreenLayout>)o).getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                        if (b && screenLayout.isAnimateOnPush()) {
                            b = true;
                        }
                        else {
                            b = false;
                        }
                        o = screenLayout;
                        break Label_0141;
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("FIXME: Failed to create a screen of type ");
                        sb.append(((Class)o).getName());
                        throw new XLEException(19L, sb.toString(), ex);
                    }
                }
            }
            o = null;
        }
        boolean b4 = b;
        if (this.getCurrentActivity() != null) {
            b4 = (b && this.getCurrentActivity().isAnimateOnPop());
        }
        if (activityParameters == null) {
            activityParameters = new ActivityParameters();
        }
        final NavigationCallbacks navigationCallbacks = this.navigationCallbacks;
        XLEAssert.assertNotNull(navigationCallbacks);
        Runnable runnable;
        if (b3) {
            runnable = new RestartRunner(activityParameters);
        }
        else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    NavigationManager.this.cannotNavigateTripwire = true;
                    final ScreenLayout currentActivity = NavigationManager.this.getCurrentActivity();
                    activityParameters.putFromScreen(currentActivity);
                    activityParameters.putSourcePage(NavigationManager.this.getCurrentActivityName());
                    if (NavigationManager.this.getCurrentActivity() != null) {
                        NavigationManager.this.getCurrentActivity().onSetInactive();
                        NavigationManager.this.getCurrentActivity().onPause();
                        NavigationManager.this.getCurrentActivity().onStop();
                    }
                    for (int i = 0; i < n; ++i) {
                        NavigationManager.this.getCurrentActivity().onDestroy();
                        navigationCallbacks.removeContentViewXLE(NavigationManager.this.navigationStack.pop());
                        NavigationManager.this.navigationParameters.pop();
                    }
                    TextureManager.Instance().purgeResourceBitmapCache();
                    ScreenLayout currentActivity2 = null;
                    if (o != null) {
                        if (NavigationManager.this.getCurrentActivity() != null && !((ScreenLayout)o).isKeepPreviousScreen()) {
                            NavigationManager.this.getCurrentActivity().onTombstone();
                        }
                        navigationCallbacks.addContentViewXLE(NavigationManager.this.navigationStack.push(o));
                        NavigationManager.this.navigationParameters.push(activityParameters);
                        NavigationManager.this.getCurrentActivity().onCreate();
                    }
                    else if (NavigationManager.this.getCurrentActivity() != null) {
                        navigationCallbacks.addContentViewXLE(NavigationManager.this.getCurrentActivity());
                        if (NavigationManager.this.getCurrentActivity().getIsTombstoned()) {
                            NavigationManager.this.getCurrentActivity().onRehydrate();
                        }
                    }
                    if (NavigationManager.this.getCurrentActivity() != null) {
                        NavigationManager.this.getCurrentActivity().onStart();
                        NavigationManager.this.getCurrentActivity().onResume();
                        NavigationManager.this.getCurrentActivity().onSetActive();
                        NavigationManager.this.getCurrentActivity().onAnimateInStarted();
                        XboxTcuiSdk.getActivity().invalidateOptionsMenu();
                        currentActivity2 = NavigationManager.this.getCurrentActivity();
                    }
                    if (NavigationManager.this.navigationListener != null) {
                        NavigationManager.this.navigationListener.onPageNavigated(currentActivity, currentActivity2);
                    }
                    NavigationManager.this.cannotNavigateTripwire = false;
                }
            };
        }
        if (NavigationManager$3.$SwitchMap$com$microsoft$xbox$toolkit$ui$NavigationManager$NavigationManagerAnimationState[this.animationState.ordinal()] != 1) {
            this.ReplaceOnAnimationEnd(b2, runnable, b4);
            return;
        }
        this.Transition(b2, runnable, b4);
    }
    
    public void PopTillScreenThenPush(final Class<? extends ScreenLayout> clazz, final Class<? extends ScreenLayout> clazz2) throws XLEException {
        this.PopTillScreenThenPush(clazz, clazz2, null);
    }
    
    public void PopTillScreenThenPush(final Class<? extends ScreenLayout> clazz, final Class<? extends ScreenLayout> clazz2, final ActivityParameters activityParameters) throws XLEException {
        final int countPopsToScreen = this.CountPopsToScreen(clazz);
        if (countPopsToScreen > 0) {
            this.PopScreensAndReplace(countPopsToScreen, clazz2, true, true, false, activityParameters);
            return;
        }
        if (countPopsToScreen < 0) {
            this.PopScreensAndReplace(0, clazz2, true, false, false, activityParameters);
            return;
        }
        this.PopScreensAndReplace(0, clazz2, true, false, false, activityParameters);
    }
    
    public void PushScreen(final Class<? extends ScreenLayout> clazz) throws XLEException {
        this.PushScreen(clazz, null);
    }
    
    public void PushScreen(final Class<? extends ScreenLayout> clazz, final ActivityParameters activityParameters) throws XLEException {
        this.PopScreensAndReplace(0, clazz, true, false, false, activityParameters);
    }
    
    public void RestartCurrentScreen(final ActivityParameters activityParameters, final boolean b) throws XLEException {
        if (this.animationState == NavigationManagerAnimationState.ANIMATING_OUT) {
            this.OnAnimationEnd();
            return;
        }
        if (this.animationState == NavigationManagerAnimationState.ANIMATING_IN) {
            this.OnAnimationEnd();
        }
        this.PopScreensAndReplace(1, this.getCurrentActivity().getClass(), b, true, true, activityParameters);
    }
    
    public void RestartCurrentScreen(final boolean b) throws XLEException {
        this.RestartCurrentScreen(null, b);
    }
    
    public boolean ShouldBackCloseApp() {
        return this.Size() <= 1 && this.animationState == NavigationManagerAnimationState.NONE;
    }
    
    public boolean TEST_isAnimatingIn() {
        return false;
    }
    
    public boolean TEST_isAnimatingOut() {
        return false;
    }
    
    public ActivityParameters getActivityParameters() {
        return this.getActivityParameters(0);
    }
    
    public ActivityParameters getActivityParameters(final int n) {
        XLEAssert.assertTrue(n >= 0 && n < this.navigationParameters.size());
        final Stack<ActivityParameters> navigationParameters = this.navigationParameters;
        return (ActivityParameters)navigationParameters.get(navigationParameters.size() - n - 1);
    }
    
    public ScreenLayout getCurrentActivity() {
        if (this.navigationStack.empty()) {
            return null;
        }
        return this.navigationStack.peek();
    }
    
    public String getCurrentActivityName() {
        final ScreenLayout currentActivity = this.getCurrentActivity();
        if (currentActivity != null) {
            return currentActivity.getName();
        }
        return null;
    }
    
    public ScreenLayout getPreviousActivity() {
        if (!this.navigationStack.empty() && this.navigationStack.size() > 1) {
            final Stack<ScreenLayout> navigationStack = this.navigationStack;
            return (ScreenLayout)navigationStack.get(navigationStack.size() - 2);
        }
        return null;
    }
    
    public boolean isAnimating() {
        return this.animationState != NavigationManagerAnimationState.NONE;
    }
    
    public void onApplicationPause() {
        for (int i = 0; i < this.navigationStack.size(); ++i) {
            ((ScreenLayout)this.navigationStack.get(i)).onApplicationPause();
        }
    }
    
    public void onApplicationResume() {
        for (int i = 0; i < this.navigationStack.size(); ++i) {
            ((ScreenLayout)this.navigationStack.get(i)).onApplicationResume();
        }
    }
    
    public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
        if (n != 4 || keyEvent.getAction() != 1) {
            return false;
        }
        if (this.OnBackButtonPressed()) {
            this.removeNavigationCallbacks();
            this.removeNaviationListener();
            return false;
        }
        return true;
    }
    
    public void removeNaviationListener() {
        this.navigationListener = null;
    }
    
    public void removeNavigationCallbacks() {
        this.navigationCallbacks = null;
    }
    
    public void setAnimationBlocking(final boolean animationBlocking) {
        final NavigationCallbacks navigationCallbacks = this.navigationCallbacks;
        if (navigationCallbacks != null) {
            navigationCallbacks.setAnimationBlocking(animationBlocking);
        }
    }
    
    public void setNavigationCallbacks(final NavigationCallbacks navigationCallbacks) {
        this.navigationCallbacks = navigationCallbacks;
    }
    
    public void setOnNavigatedListener(final OnNavigatedListener navigationListener) {
        this.navigationListener = navigationListener;
    }
    
    public interface NavigationCallbacks
    {
        void addContentViewXLE(final ScreenLayout p0);
        
        void onBeforeNavigatingIn();
        
        void removeContentViewXLE(final ScreenLayout p0);
        
        void setAnimationBlocking(final boolean p0);
    }
    
    private enum NavigationManagerAnimationState
    {
        ANIMATING_IN, 
        ANIMATING_OUT, 
        COUNT, 
        NONE;
    }
    
    private static class NavigationManagerHolder
    {
        public static final NavigationManager instance;
        
        static {
            instance = new NavigationManager(null);
        }
    }
    
    public interface OnNavigatedListener
    {
        void onPageNavigated(final ScreenLayout p0, final ScreenLayout p1);
        
        void onPageRestarted(final ScreenLayout p0);
    }
    
    private class RestartRunner implements Runnable
    {
        private final ActivityParameters params;
        
        public RestartRunner(final ActivityParameters params) {
            this.params = params;
        }
        
        @Override
        public void run() {
            NavigationManager.this.cannotNavigateTripwire = true;
            final ScreenLayout currentActivity = NavigationManager.this.getCurrentActivity();
            XLEAssert.assertNotNull(currentActivity);
            NavigationManager.this.getCurrentActivity().onSetInactive();
            NavigationManager.this.getCurrentActivity().onPause();
            NavigationManager.this.getCurrentActivity().onStop();
            XLEAssert.assertTrue("navigationParameters cannot be empty!", true ^ NavigationManager.this.navigationParameters.isEmpty());
            NavigationManager.this.navigationParameters.pop();
            NavigationManager.this.navigationParameters.push(this.params);
            NavigationManager.this.getCurrentActivity().onStart();
            NavigationManager.this.getCurrentActivity().onResume();
            NavigationManager.this.getCurrentActivity().onSetActive();
            NavigationManager.this.getCurrentActivity().onAnimateInStarted();
            XboxTcuiSdk.getActivity().invalidateOptionsMenu();
            if (NavigationManager.this.navigationListener != null) {
                NavigationManager.this.navigationListener.onPageRestarted(currentActivity);
            }
            NavigationManager.this.cannotNavigateTripwire = false;
        }
    }
}
