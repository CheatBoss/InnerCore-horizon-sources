package com.appboy.ui.inappmessage;

import android.app.*;
import android.content.*;
import java.util.concurrent.atomic.*;
import com.appboy.events.*;
import java.util.*;
import com.appboy.support.*;
import com.appboy.ui.inappmessage.listeners.*;
import com.appboy.ui.inappmessage.factories.*;
import com.appboy.models.*;
import com.appboy.enums.inappmessage.*;
import android.view.*;
import android.view.animation.*;
import com.appboy.*;
import android.os.*;
import com.appboy.ui.support.*;

public final class AppboyInAppMessageManager
{
    private static final String TAG;
    private static volatile AppboyInAppMessageManager sInstance;
    private Activity mActivity;
    private Context mApplicationContext;
    private IInAppMessage mCarryoverInAppMessage;
    private IInAppMessageManagerListener mCustomControlInAppMessageManagerListener;
    private IHtmlInAppMessageActionListener mCustomHtmlInAppMessageActionListener;
    private IInAppMessageAnimationFactory mCustomInAppMessageAnimationFactory;
    private IInAppMessageManagerListener mCustomInAppMessageManagerListener;
    private IInAppMessageViewFactory mCustomInAppMessageViewFactory;
    private IHtmlInAppMessageActionListener mDefaultHtmlInAppMessageActionListener;
    private IInAppMessageManagerListener mDefaultInAppMessageManagerListener;
    private AtomicBoolean mDisplayingInAppMessage;
    private IInAppMessageAnimationFactory mInAppMessageAnimationFactory;
    private IEventSubscriber<InAppMessageEvent> mInAppMessageEventSubscriber;
    private IInAppMessageViewFactory mInAppMessageFullViewFactory;
    private IInAppMessageViewFactory mInAppMessageHtmlFullViewFactory;
    private IInAppMessageViewFactory mInAppMessageModalViewFactory;
    private IInAppMessageViewFactory mInAppMessageSlideupViewFactory;
    private final Stack<IInAppMessage> mInAppMessageStack;
    private final IInAppMessageViewLifecycleListener mInAppMessageViewLifecycleListener;
    private IInAppMessageViewWrapper mInAppMessageViewWrapper;
    private final IInAppMessageWebViewClientListener mInAppMessageWebViewClientListener;
    private Integer mOriginalOrientation;
    private IInAppMessage mUnRegisteredInAppMessage;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageManager.class);
        AppboyInAppMessageManager.sInstance = null;
    }
    
    public AppboyInAppMessageManager() {
        this.mInAppMessageStack = new Stack<IInAppMessage>();
        this.mDisplayingInAppMessage = new AtomicBoolean(false);
        this.mInAppMessageWebViewClientListener = new AppboyInAppMessageWebViewClientListener();
        this.mInAppMessageViewLifecycleListener = new AppboyInAppMessageViewLifecycleListener();
        this.mDefaultInAppMessageManagerListener = new AppboyDefaultInAppMessageManagerListener();
        this.mDefaultHtmlInAppMessageActionListener = new AppboyDefaultHtmlInAppMessageActionListener();
        this.mInAppMessageSlideupViewFactory = new AppboySlideupViewFactory();
        this.mInAppMessageModalViewFactory = new AppboyModalViewFactory();
        this.mInAppMessageFullViewFactory = new AppboyFullViewFactory();
        this.mInAppMessageHtmlFullViewFactory = new AppboyHtmlFullViewFactory(this.mInAppMessageWebViewClientListener);
        this.mInAppMessageAnimationFactory = new AppboyInAppMessageAnimationFactory();
    }
    
    private IEventSubscriber<InAppMessageEvent> createInAppMessageEventSubscriber() {
        return new IEventSubscriber<InAppMessageEvent>() {
            @Override
            public void trigger(final InAppMessageEvent inAppMessageEvent) {
                if (AppboyInAppMessageManager.this.getInAppMessageManagerListener().onInAppMessageReceived(inAppMessageEvent.getInAppMessage())) {
                    return;
                }
                AppboyInAppMessageManager.this.addInAppMessage(inAppMessageEvent.getInAppMessage());
            }
        };
    }
    
    private boolean currentOrientationIsValid(final int n, final Orientation orientation) {
        String s;
        String s2;
        if (n == 2 && orientation == Orientation.LANDSCAPE) {
            s = AppboyInAppMessageManager.TAG;
            s2 = "Current and preferred orientation are landscape.";
        }
        else {
            if (n != 1 || orientation != Orientation.PORTRAIT) {
                final String tag = AppboyInAppMessageManager.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Current orientation ");
                sb.append(n);
                sb.append(" and preferred orientation ");
                sb.append(orientation);
                sb.append(" don't match");
                AppboyLogger.d(tag, sb.toString());
                return false;
            }
            s = AppboyInAppMessageManager.TAG;
            s2 = "Current and preferred orientation are portrait.";
        }
        AppboyLogger.d(s, s2);
        return true;
    }
    
    private IInAppMessageAnimationFactory getInAppMessageAnimationFactory() {
        final IInAppMessageAnimationFactory mCustomInAppMessageAnimationFactory = this.mCustomInAppMessageAnimationFactory;
        if (mCustomInAppMessageAnimationFactory != null) {
            return mCustomInAppMessageAnimationFactory;
        }
        return this.mInAppMessageAnimationFactory;
    }
    
    private IInAppMessageViewFactory getInAppMessageViewFactory(final IInAppMessage inAppMessage) {
        final IInAppMessageViewFactory mCustomInAppMessageViewFactory = this.mCustomInAppMessageViewFactory;
        if (mCustomInAppMessageViewFactory != null) {
            return mCustomInAppMessageViewFactory;
        }
        if (inAppMessage instanceof InAppMessageSlideup) {
            return this.mInAppMessageSlideupViewFactory;
        }
        if (inAppMessage instanceof InAppMessageModal) {
            return this.mInAppMessageModalViewFactory;
        }
        if (inAppMessage instanceof InAppMessageFull) {
            return this.mInAppMessageFullViewFactory;
        }
        if (inAppMessage instanceof InAppMessageHtmlFull) {
            return this.mInAppMessageHtmlFullViewFactory;
        }
        return null;
    }
    
    public static AppboyInAppMessageManager getInstance() {
        if (AppboyInAppMessageManager.sInstance == null) {
            synchronized (AppboyInAppMessageManager.class) {
                if (AppboyInAppMessageManager.sInstance == null) {
                    AppboyInAppMessageManager.sInstance = new AppboyInAppMessageManager();
                }
            }
        }
        return AppboyInAppMessageManager.sInstance;
    }
    
    public void addInAppMessage(final IInAppMessage inAppMessage) {
        this.mInAppMessageStack.push(inAppMessage);
        this.requestDisplayInAppMessage();
    }
    
    boolean displayInAppMessage(final IInAppMessage mCarryoverInAppMessage, final boolean b) {
        if (!this.mDisplayingInAppMessage.compareAndSet(false, true)) {
            AppboyLogger.d(AppboyInAppMessageManager.TAG, "A in-app message is currently being displayed. Adding in-app message back on the stack.");
            this.mInAppMessageStack.push(mCarryoverInAppMessage);
            return false;
        }
        try {
            if (this.mActivity == null) {
                this.mCarryoverInAppMessage = mCarryoverInAppMessage;
                throw new Exception("No activity is currently registered to receive in-app messages. Registering in-app message as carry-over in-app message. It will automatically be displayed when the next activity registers to receive in-app messages.");
            }
            Label_0150: {
                String s;
                String s2;
                if (!b) {
                    final long expirationTimestamp = mCarryoverInAppMessage.getExpirationTimestamp();
                    if (expirationTimestamp > 0L) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis <= expirationTimestamp) {
                            break Label_0150;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("In-app message is expired. Doing nothing. Expiration: $");
                        sb.append(expirationTimestamp);
                        sb.append(". Current time: ");
                        sb.append(currentTimeMillis);
                        throw new Exception(sb.toString());
                    }
                    else {
                        s = AppboyInAppMessageManager.TAG;
                        s2 = "Expiration timestamp not defined. Continuing.";
                    }
                }
                else {
                    s = AppboyInAppMessageManager.TAG;
                    s2 = "Not checking expiration status for carry-over in-app message.";
                }
                AppboyLogger.d(s, s2);
            }
            if (!this.verifyOrientationStatus(mCarryoverInAppMessage)) {
                throw new Exception("Current orientation did not match specified orientation for in-app message. Doing nothing.");
            }
            if (mCarryoverInAppMessage.isControl()) {
                AppboyLogger.d(AppboyInAppMessageManager.TAG, "Not displaying control in-app message. Logging impression and ending display execution.");
                mCarryoverInAppMessage.logImpression();
                this.resetAfterInAppMessageClose();
                return true;
            }
            final IInAppMessageViewFactory inAppMessageViewFactory = this.getInAppMessageViewFactory(mCarryoverInAppMessage);
            if (inAppMessageViewFactory == null) {
                mCarryoverInAppMessage.logDisplayFailure(InAppMessageFailureType.DISPLAY_VIEW_GENERATION);
                throw new Exception("ViewFactory from getInAppMessageViewFactory was null.");
            }
            final View inAppMessageView = inAppMessageViewFactory.createInAppMessageView(this.mActivity, mCarryoverInAppMessage);
            if (inAppMessageView == null) {
                mCarryoverInAppMessage.logDisplayFailure(InAppMessageFailureType.DISPLAY_VIEW_GENERATION);
                throw new Exception("The in-app message view returned from the IInAppMessageViewFactory was null. The in-app message will not be displayed and will not be put back on the stack.");
            }
            if (inAppMessageView.getParent() == null) {
                final Animation openingAnimation = this.getInAppMessageAnimationFactory().getOpeningAnimation(mCarryoverInAppMessage);
                final Animation closingAnimation = this.getInAppMessageAnimationFactory().getClosingAnimation(mCarryoverInAppMessage);
                Label_0413: {
                    InAppMessageViewWrapper mInAppMessageViewWrapper;
                    if (inAppMessageView instanceof IInAppMessageImmersiveView) {
                        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Creating view wrapper for immersive in-app message.");
                        final IInAppMessageImmersiveView inAppMessageImmersiveView = (IInAppMessageImmersiveView)inAppMessageView;
                        mInAppMessageViewWrapper = new InAppMessageViewWrapper(inAppMessageView, mCarryoverInAppMessage, this.mInAppMessageViewLifecycleListener, openingAnimation, closingAnimation, inAppMessageImmersiveView.getMessageClickableView(), inAppMessageImmersiveView.getMessageButtonViews(), inAppMessageImmersiveView.getMessageCloseButtonView());
                    }
                    else {
                        if (!(inAppMessageView instanceof IInAppMessageView)) {
                            AppboyLogger.d(AppboyInAppMessageManager.TAG, "Creating view wrapper for in-app message.");
                            this.mInAppMessageViewWrapper = new InAppMessageViewWrapper(inAppMessageView, mCarryoverInAppMessage, this.mInAppMessageViewLifecycleListener, openingAnimation, closingAnimation, inAppMessageView);
                            break Label_0413;
                        }
                        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Creating view wrapper for base in-app message.");
                        mInAppMessageViewWrapper = new InAppMessageViewWrapper(inAppMessageView, mCarryoverInAppMessage, this.mInAppMessageViewLifecycleListener, openingAnimation, closingAnimation, ((IInAppMessageImmersiveView)inAppMessageView).getMessageClickableView());
                    }
                    this.mInAppMessageViewWrapper = mInAppMessageViewWrapper;
                }
                this.mInAppMessageViewWrapper.open(this.mActivity);
                return true;
            }
            mCarryoverInAppMessage.logDisplayFailure(InAppMessageFailureType.DISPLAY_VIEW_GENERATION);
            throw new Exception("The in-app message view returned from the IInAppMessageViewFactory already has a parent. This is a sign that the view is being reused. The IInAppMessageViewFactory method createInAppMessageViewmust return a new view without a parent. The in-app message will not be displayed and will not be put back on the stack.");
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyInAppMessageManager.TAG, "Could not display in-app message", ex);
            this.resetAfterInAppMessageClose();
            return false;
        }
    }
    
    public void ensureSubscribedToInAppMessageEvents(final Context context) {
        if (this.mInAppMessageEventSubscriber == null) {
            AppboyLogger.d(AppboyInAppMessageManager.TAG, "Subscribing in-app message event subscriber");
            this.mInAppMessageEventSubscriber = this.createInAppMessageEventSubscriber();
            Appboy.getInstance(context).subscribeToNewInAppMessages(this.mInAppMessageEventSubscriber);
        }
    }
    
    public Activity getActivity() {
        return this.mActivity;
    }
    
    public Context getApplicationContext() {
        return this.mApplicationContext;
    }
    
    public IInAppMessageManagerListener getControlInAppMessageManagerListener() {
        final IInAppMessageManagerListener mCustomControlInAppMessageManagerListener = this.mCustomControlInAppMessageManagerListener;
        if (mCustomControlInAppMessageManagerListener != null) {
            return mCustomControlInAppMessageManagerListener;
        }
        return this.mDefaultInAppMessageManagerListener;
    }
    
    public IHtmlInAppMessageActionListener getHtmlInAppMessageActionListener() {
        final IHtmlInAppMessageActionListener mCustomHtmlInAppMessageActionListener = this.mCustomHtmlInAppMessageActionListener;
        if (mCustomHtmlInAppMessageActionListener != null) {
            return mCustomHtmlInAppMessageActionListener;
        }
        return this.mDefaultHtmlInAppMessageActionListener;
    }
    
    public IInAppMessageManagerListener getInAppMessageManagerListener() {
        final IInAppMessageManagerListener mCustomInAppMessageManagerListener = this.mCustomInAppMessageManagerListener;
        if (mCustomInAppMessageManagerListener != null) {
            return mCustomInAppMessageManagerListener;
        }
        return this.mDefaultInAppMessageManagerListener;
    }
    
    public void hideCurrentlyDisplayingInAppMessage(final boolean b) {
        final IInAppMessageViewWrapper mInAppMessageViewWrapper = this.mInAppMessageViewWrapper;
        if (mInAppMessageViewWrapper != null) {
            if (b) {
                this.mInAppMessageViewLifecycleListener.onDismissed(mInAppMessageViewWrapper.getInAppMessageView(), mInAppMessageViewWrapper.getInAppMessage());
            }
            mInAppMessageViewWrapper.close();
        }
    }
    
    public void registerInAppMessageManager(final Activity mActivity) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "registerInAppMessageManager called");
        this.mActivity = mActivity;
        if (mActivity != null && this.mApplicationContext == null) {
            this.mApplicationContext = mActivity.getApplicationContext();
        }
        if (this.mCarryoverInAppMessage != null) {
            AppboyLogger.d(AppboyInAppMessageManager.TAG, "Requesting display of carryover in-app message.");
            this.mCarryoverInAppMessage.setAnimateIn(false);
            this.displayInAppMessage(this.mCarryoverInAppMessage, true);
            this.mCarryoverInAppMessage = null;
        }
        else if (this.mUnRegisteredInAppMessage != null) {
            AppboyLogger.d(AppboyInAppMessageManager.TAG, "Adding previously unregistered in-app message.");
            this.addInAppMessage(this.mUnRegisteredInAppMessage);
            this.mUnRegisteredInAppMessage = null;
        }
        this.ensureSubscribedToInAppMessageEvents(this.mApplicationContext);
    }
    
    public boolean requestDisplayInAppMessage() {
        try {
            if (this.mActivity == null) {
                if (!this.mInAppMessageStack.empty()) {
                    AppboyLogger.w(AppboyInAppMessageManager.TAG, "No activity is currently registered to receive in-app messages. Saving in-app message as unregistered in-app message. It will automatically be displayed when the next activity registers to receive in-app messages.");
                    this.mUnRegisteredInAppMessage = this.mInAppMessageStack.pop();
                    return false;
                }
                AppboyLogger.d(AppboyInAppMessageManager.TAG, "No activity is currently registered to receive in-app messages and the in-app message stack is empty. Doing nothing.");
                return false;
            }
            else {
                if (this.mDisplayingInAppMessage.get()) {
                    AppboyLogger.d(AppboyInAppMessageManager.TAG, "A in-app message is currently being displayed. Ignoring request to display in-app message.");
                    return false;
                }
                if (this.mInAppMessageStack.isEmpty()) {
                    AppboyLogger.d(AppboyInAppMessageManager.TAG, "The in-app message stack is empty. No in-app message will be displayed.");
                    return false;
                }
                final IInAppMessage inAppMessage = this.mInAppMessageStack.pop();
                IInAppMessageManagerListener inAppMessageManagerListener;
                if (!inAppMessage.isControl()) {
                    inAppMessageManagerListener = this.getInAppMessageManagerListener();
                }
                else {
                    AppboyLogger.d(AppboyInAppMessageManager.TAG, "Using the control in-app message manager listener.");
                    inAppMessageManagerListener = this.getControlInAppMessageManagerListener();
                }
                final int n = AppboyInAppMessageManager$3.$SwitchMap$com$appboy$ui$inappmessage$InAppMessageOperation[inAppMessageManagerListener.beforeInAppMessageDisplayed(inAppMessage).ordinal()];
                if (n == 1) {
                    AppboyLogger.d(AppboyInAppMessageManager.TAG, "The IInAppMessageManagerListener method beforeInAppMessageDisplayed returned DISPLAY_NOW. The in-app message will be displayed.");
                    new Handler(this.mApplicationContext.getMainLooper()).post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            new AppboyAsyncInAppMessageDisplayer().execute((Object[])new IInAppMessage[] { inAppMessage });
                        }
                    });
                    return true;
                }
                if (n == 2) {
                    AppboyLogger.d(AppboyInAppMessageManager.TAG, "The IInAppMessageManagerListener method beforeInAppMessageDisplayed returned DISPLAY_LATER. The in-app message will be pushed back onto the stack.");
                    this.mInAppMessageStack.push(inAppMessage);
                    return false;
                }
                if (n != 3) {
                    AppboyLogger.e(AppboyInAppMessageManager.TAG, "The IInAppMessageManagerListener method beforeInAppMessageDisplayed returned null instead of a InAppMessageOperation. Ignoring the in-app message. Please check the IInAppMessageStackBehaviour implementation.");
                    return false;
                }
                AppboyLogger.d(AppboyInAppMessageManager.TAG, "The IInAppMessageManagerListener method beforeInAppMessageDisplayed returned DISCARD. The in-app message will not be displayed and will not be put back on the stack.");
                return false;
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyInAppMessageManager.TAG, "Error running requestDisplayInAppMessage", ex);
            return false;
        }
    }
    
    public void resetAfterInAppMessageClose() {
        AppboyLogger.v(AppboyInAppMessageManager.TAG, "Resetting after in-app message close.");
        this.mInAppMessageViewWrapper = null;
        this.mDisplayingInAppMessage.set(false);
        if (this.mActivity != null && this.mOriginalOrientation != null) {
            final String tag = AppboyInAppMessageManager.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Setting requested orientation to original orientation ");
            sb.append(this.mOriginalOrientation);
            AppboyLogger.d(tag, sb.toString());
            ViewUtils.setActivityRequestedOrientation(this.mActivity, this.mOriginalOrientation);
            this.mOriginalOrientation = null;
        }
    }
    
    public void setCustomControlInAppMessageManagerListener(final IInAppMessageManagerListener mCustomControlInAppMessageManagerListener) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Custom ControlInAppMessageManagerListener set. This listener will only be used for control in-app messages.");
        this.mCustomControlInAppMessageManagerListener = mCustomControlInAppMessageManagerListener;
    }
    
    public void setCustomHtmlInAppMessageActionListener(final IHtmlInAppMessageActionListener mCustomHtmlInAppMessageActionListener) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Custom htmlInAppMessageActionListener set");
        this.mCustomHtmlInAppMessageActionListener = mCustomHtmlInAppMessageActionListener;
    }
    
    public void setCustomInAppMessageAnimationFactory(final IInAppMessageAnimationFactory mCustomInAppMessageAnimationFactory) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Custom InAppMessageAnimationFactory set");
        this.mCustomInAppMessageAnimationFactory = mCustomInAppMessageAnimationFactory;
    }
    
    public void setCustomInAppMessageManagerListener(final IInAppMessageManagerListener mCustomInAppMessageManagerListener) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Custom InAppMessageManagerListener set");
        this.mCustomInAppMessageManagerListener = mCustomInAppMessageManagerListener;
    }
    
    public void setCustomInAppMessageViewFactory(final IInAppMessageViewFactory mCustomInAppMessageViewFactory) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Custom InAppMessageViewFactory set");
        this.mCustomInAppMessageViewFactory = mCustomInAppMessageViewFactory;
    }
    
    public void unregisterInAppMessageManager(final Activity activity) {
        AppboyLogger.d(AppboyInAppMessageManager.TAG, "unregisterInAppMessageManager called");
        final IInAppMessageViewWrapper mInAppMessageViewWrapper = this.mInAppMessageViewWrapper;
        if (mInAppMessageViewWrapper != null) {
            ViewUtils.removeViewFromParent(mInAppMessageViewWrapper.getInAppMessageView());
            if (this.mInAppMessageViewWrapper.getIsAnimatingClose()) {
                this.mInAppMessageViewLifecycleListener.afterClosed(this.mInAppMessageViewWrapper.getInAppMessage());
                this.mCarryoverInAppMessage = null;
            }
            else {
                this.mCarryoverInAppMessage = this.mInAppMessageViewWrapper.getInAppMessage();
            }
            this.mInAppMessageViewWrapper = null;
        }
        else {
            this.mCarryoverInAppMessage = null;
        }
        this.mActivity = null;
        this.mDisplayingInAppMessage.set(false);
    }
    
    boolean verifyOrientationStatus(final IInAppMessage inAppMessage) {
        String s;
        String s2;
        if (ViewUtils.isRunningOnTablet(this.mActivity)) {
            s = AppboyInAppMessageManager.TAG;
            s2 = "Running on tablet. In-app message can be displayed in any orientation.";
        }
        else {
            final Orientation orientation = inAppMessage.getOrientation();
            if (orientation == null) {
                s = AppboyInAppMessageManager.TAG;
                s2 = "No orientation specified. In-app message can be displayed in any orientation.";
            }
            else if (orientation == Orientation.ANY) {
                s = AppboyInAppMessageManager.TAG;
                s2 = "Any orientation specified. In-app message can be displayed in any orientation.";
            }
            else {
                if (this.currentOrientationIsValid(this.mActivity.getResources().getConfiguration().orientation, orientation)) {
                    if (this.mOriginalOrientation == null) {
                        AppboyLogger.d(AppboyInAppMessageManager.TAG, "Requesting orientation lock.");
                        this.mOriginalOrientation = this.mActivity.getRequestedOrientation();
                        ViewUtils.setActivityRequestedOrientation(this.mActivity, 14);
                    }
                    return true;
                }
                return false;
            }
        }
        AppboyLogger.d(s, s2);
        return true;
    }
}
