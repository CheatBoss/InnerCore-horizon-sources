package com.microsoft.xbox.xle.app.activity;

import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xboxtcui.*;
import android.util.*;
import com.microsoft.xbox.xle.ui.*;
import android.view.accessibility.*;
import com.microsoft.xbox.xle.anim.*;
import android.view.*;
import com.microsoft.xbox.toolkit.anim.*;
import android.content.*;
import com.microsoft.xbox.toolkit.*;
import java.lang.ref.*;
import android.content.res.*;
import android.os.*;

public abstract class ActivityBase extends ScreenLayout
{
    private boolean showRightPane;
    private boolean showUtilityBar;
    protected ViewModelBase viewModel;
    
    public ActivityBase() {
        this(0);
    }
    
    public ActivityBase(final int n) {
        super(XboxTcuiSdk.getApplicationContext(), n);
        this.showUtilityBar = true;
        this.showRightPane = true;
    }
    
    public ActivityBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.showUtilityBar = true;
        this.showRightPane = true;
    }
    
    private XLERootView getXLERootView() {
        if (this.getChildAt(0) instanceof XLERootView) {
            return (XLERootView)this.getChildAt(0);
        }
        return null;
    }
    
    @Override
    public void adjustBottomMargin(final int bottomMargin) {
        if (this.getXLERootView() != null) {
            this.getXLERootView().setBottomMargin(bottomMargin);
        }
    }
    
    protected int computeBottomMargin() {
        return 0;
    }
    
    protected boolean delayAppbarAnimation() {
        return false;
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 8 && this.getXLERootView() != null && this.getXLERootView().getContentDescription() != null) {
            accessibilityEvent.getText().clear();
            accessibilityEvent.getText().add(this.getXLERootView().getContentDescription());
            return true;
        }
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    @Override
    public void forceRefresh() {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.forceRefresh();
        }
    }
    
    @Override
    public void forceUpdateViewImmediately() {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.forceUpdateViewImmediately();
        }
    }
    
    protected abstract String getActivityName();
    
    @Override
    public XLEAnimationPackage getAnimateIn(final boolean b) {
        final View child = this.getChildAt(0);
        if (child != null) {
            final MAASAnimation animation = MAAS.getInstance().getAnimation("Screen");
            if (animation != null) {
                final XLEAnimation compile = ((XLEMAASAnimationPackageNavigationManager)animation).compile(MAAS.MAASAnimationType.ANIMATE_IN, b, child);
                if (compile != null) {
                    final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
                    xleAnimationPackage.add(compile);
                    return xleAnimationPackage;
                }
            }
        }
        return null;
    }
    
    @Override
    public XLEAnimationPackage getAnimateOut(final boolean b) {
        final View child = this.getChildAt(0);
        if (child != null) {
            final MAASAnimation animation = MAAS.getInstance().getAnimation("Screen");
            if (animation != null) {
                final XLEAnimation compile = ((XLEMAASAnimationPackageNavigationManager)animation).compile(MAAS.MAASAnimationType.ANIMATE_OUT, b, child);
                if (compile != null) {
                    final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
                    xleAnimationPackage.add(compile);
                    return xleAnimationPackage;
                }
            }
        }
        return null;
    }
    
    @Override
    public String getName() {
        return this.getActivityName();
    }
    
    @Override
    public String getRelativeId() {
        return null;
    }
    
    @Override
    public boolean getShouldShowAppbar() {
        return false;
    }
    
    @Override
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onActivityResult(n, n2, intent);
        }
    }
    
    @Override
    public void onAnimateInCompleted() {
        if (this.viewModel != null) {
            BackgroundThreadWaitor.getInstance().postRunnableAfterReady(new Runnable() {
                final /* synthetic */ WeakReference val$viewModelWeakPtr = new WeakReference((T)ActivityBase.this.viewModel);
                
                @Override
                public void run() {
                    final ViewModelBase viewModelBase = (ViewModelBase)this.val$viewModelWeakPtr.get();
                    if (viewModelBase != null) {
                        viewModelBase.forceUpdateViewImmediately();
                    }
                }
            });
        }
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onAnimateInCompleted();
        }
    }
    
    @Override
    public void onAnimateInStarted() {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.forceUpdateViewImmediately();
        }
    }
    
    @Override
    public void onApplicationPause() {
        super.onApplicationPause();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onApplicationPause();
        }
    }
    
    @Override
    public void onApplicationResume() {
        super.onApplicationResume();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onApplicationResume();
        }
    }
    
    @Override
    public boolean onBackButtonPressed() {
        final ViewModelBase viewModel = this.viewModel;
        return viewModel != null && viewModel.onBackButtonPressed();
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onConfigurationChanged(configuration);
        }
    }
    
    public abstract void onCreateContentView();
    
    @Override
    public void onDestroy() {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onDestroy();
        }
        this.viewModel = null;
        super.onDestroy();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.clearDisappearingChildren();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onPause();
        }
    }
    
    @Override
    public void onRehydrate() {
        super.onRehydrate();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onRehydrate();
        }
    }
    
    @Override
    public void onRehydrateOverride() {
        this.onCreateContentView();
    }
    
    @Override
    public void onRestoreInstanceState(final Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onRestoreInstanceState(bundle);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onResume();
        }
    }
    
    @Override
    public void onSetActive() {
        super.onSetActive();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onSetActive();
        }
    }
    
    @Override
    public void onSetInactive() {
        super.onSetInactive();
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onSetInactive();
        }
    }
    
    @Override
    public void onStart() {
        if (!this.getIsStarted()) {
            super.onStart();
            final ViewModelBase viewModel = this.viewModel;
            if (viewModel != null) {
                viewModel.onStart();
            }
            final ViewModelBase viewModel2 = this.viewModel;
            if (viewModel2 != null) {
                viewModel2.load();
            }
        }
        if (!this.delayAppbarAnimation()) {
            this.adjustBottomMargin(this.computeBottomMargin());
        }
    }
    
    @Override
    public void onStop() {
        if (this.getIsStarted()) {
            super.onStop();
            final ViewModelBase viewModel = this.viewModel;
            if (viewModel != null) {
                viewModel.onSetInactive();
            }
            final ViewModelBase viewModel2 = this.viewModel;
            if (viewModel2 != null) {
                viewModel2.onStop();
            }
        }
    }
    
    @Override
    public void onTombstone() {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.onTombstone();
        }
        super.onTombstone();
    }
    
    @Override
    public void removeBottomMargin() {
        if (this.getXLERootView() != null) {
            this.getXLERootView().setBottomMargin(0);
        }
    }
    
    @Override
    public void resetBottomMargin() {
        if (this.getXLERootView() != null) {
            this.adjustBottomMargin(this.computeBottomMargin());
        }
    }
    
    public void setHeaderName(final String s) {
    }
    
    @Override
    public void setScreenState(final int screenState) {
        final ViewModelBase viewModel = this.viewModel;
        if (viewModel != null) {
            viewModel.setScreenState(screenState);
        }
    }
}
