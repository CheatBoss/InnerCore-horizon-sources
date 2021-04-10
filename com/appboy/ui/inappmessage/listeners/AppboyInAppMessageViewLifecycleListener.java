package com.appboy.ui.inappmessage.listeners;

import com.appboy.enums.inappmessage.*;
import com.appboy.ui.inappmessage.*;
import android.net.*;
import com.appboy.ui.*;
import com.appboy.enums.*;
import android.content.*;
import com.appboy.ui.actions.*;
import com.appboy.support.*;
import android.app.*;
import android.view.*;
import com.appboy.models.*;

public class AppboyInAppMessageViewLifecycleListener implements IInAppMessageViewLifecycleListener
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageViewLifecycleListener.class);
    }
    
    private AppboyInAppMessageManager getInAppMessageManager() {
        return AppboyInAppMessageManager.getInstance();
    }
    
    private void performClickAction(final ClickAction clickAction, final IInAppMessage inAppMessage, final InAppMessageCloser inAppMessageCloser, final Uri uri, final boolean b) {
        if (this.getInAppMessageManager().getActivity() == null) {
            AppboyLogger.w(AppboyInAppMessageViewLifecycleListener.TAG, "Can't perform click action because the cached activity is null.");
            return;
        }
        final int n = AppboyInAppMessageViewLifecycleListener$2.$SwitchMap$com$appboy$enums$inappmessage$ClickAction[clickAction.ordinal()];
        if (n == 1) {
            inAppMessageCloser.close(false);
            AppboyNavigator.getAppboyNavigator().gotoNewsFeed((Context)this.getInAppMessageManager().getActivity(), new NewsfeedAction(BundleUtils.mapToBundle(inAppMessage.getExtras()), Channel.INAPP_MESSAGE));
            return;
        }
        if (n == 2) {
            inAppMessageCloser.close(false);
            AppboyNavigator.getAppboyNavigator().gotoUri((Context)this.getInAppMessageManager().getActivity(), ActionFactory.createUriActionFromUri(uri, BundleUtils.mapToBundle(inAppMessage.getExtras()), b, Channel.INAPP_MESSAGE));
            return;
        }
        if (n != 3) {
            inAppMessageCloser.close(false);
            return;
        }
        inAppMessageCloser.close(inAppMessage.getAnimateOut());
    }
    
    private void performInAppMessageButtonClicked(final MessageButton messageButton, final IInAppMessage inAppMessage, final InAppMessageCloser inAppMessageCloser) {
        this.performClickAction(messageButton.getClickAction(), inAppMessage, inAppMessageCloser, messageButton.getUri(), messageButton.getOpenUriInWebview());
    }
    
    private void performInAppMessageClicked(final IInAppMessage inAppMessage, final InAppMessageCloser inAppMessageCloser) {
        this.performClickAction(inAppMessage.getClickAction(), inAppMessage, inAppMessageCloser, inAppMessage.getUri(), inAppMessage.getOpenUriInWebView());
    }
    
    private void startClearHtmlInAppMessageAssetsThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Activity activity = AppboyInAppMessageManager.getInstance().getActivity();
                if (activity != null) {
                    AppboyFileUtils.deleteFileOrDirectory(WebContentUtils.getHtmlInAppMessageAssetCacheDirectory((Context)activity));
                }
            }
        }).start();
    }
    
    @Override
    public void afterClosed(final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.afterClosed called.");
        this.getInAppMessageManager().resetAfterInAppMessageClose();
        if (inAppMessage instanceof IInAppMessageHtml) {
            this.startClearHtmlInAppMessageAssetsThread();
        }
        inAppMessage.onAfterClosed();
    }
    
    @Override
    public void afterOpened(final View view, final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.afterOpened called.");
    }
    
    @Override
    public void beforeClosed(final View view, final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.beforeClosed called.");
    }
    
    @Override
    public void beforeOpened(final View view, final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.beforeOpened called.");
        inAppMessage.logImpression();
    }
    
    @Override
    public void onButtonClicked(final InAppMessageCloser inAppMessageCloser, final MessageButton messageButton, final IInAppMessageImmersive inAppMessageImmersive) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.onButtonClicked called.");
        inAppMessageImmersive.logButtonClick(messageButton);
        if (!this.getInAppMessageManager().getInAppMessageManagerListener().onInAppMessageButtonClicked(messageButton, inAppMessageCloser)) {
            this.performInAppMessageButtonClicked(messageButton, inAppMessageImmersive, inAppMessageCloser);
        }
    }
    
    @Override
    public void onClicked(final InAppMessageCloser inAppMessageCloser, final View view, final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.onClicked called.");
        inAppMessage.logClick();
        if (!this.getInAppMessageManager().getInAppMessageManagerListener().onInAppMessageClicked(inAppMessage, inAppMessageCloser)) {
            this.performInAppMessageClicked(inAppMessage, inAppMessageCloser);
        }
    }
    
    @Override
    public void onDismissed(final View view, final IInAppMessage inAppMessage) {
        AppboyLogger.d(AppboyInAppMessageViewLifecycleListener.TAG, "InAppMessageViewWrapper.IInAppMessageViewLifecycleListener.onDismissed called.");
        this.getInAppMessageManager().getInAppMessageManagerListener().onInAppMessageDismissed(inAppMessage);
    }
}
