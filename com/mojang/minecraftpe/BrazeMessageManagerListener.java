package com.mojang.minecraftpe;

import com.appboy.ui.inappmessage.listeners.*;
import com.appboy.models.*;
import java.util.*;
import com.appboy.ui.inappmessage.*;

public class BrazeMessageManagerListener implements IInAppMessageManagerListener
{
    private MessageButton _mostRecentButton0;
    private MessageButton _mostRecentButton1;
    private InAppMessageModal _mostRecentInAppDialog;
    private IInAppMessage _mostRecentInAppToast;
    
    public BrazeMessageManagerListener() {
        this._mostRecentInAppToast = null;
        this._mostRecentInAppDialog = null;
        this._mostRecentButton0 = null;
        this._mostRecentButton1 = null;
    }
    
    @Override
    public InAppMessageOperation beforeInAppMessageDisplayed(final IInAppMessage mostRecentInAppToast) {
        final boolean equals = mostRecentInAppToast.getClass().getSimpleName().equals("InAppMessageSlideup");
        String string = "";
        if (equals) {
            String string2;
            if (mostRecentInAppToast.getUri() != null) {
                string2 = mostRecentInAppToast.getUri().toString();
            }
            else {
                string2 = "";
            }
            String s = string;
            if (mostRecentInAppToast.getExtras() != null) {
                s = string;
                if (!mostRecentInAppToast.getExtras().isEmpty()) {
                    final Map<String, String> extras = mostRecentInAppToast.getExtras();
                    s = string;
                    if (extras.get("ToastSubtitle") != null) {
                        s = extras.get("ToastSubtitle");
                    }
                }
            }
            this._mostRecentInAppToast = mostRecentInAppToast;
            this.nativeBrazeToastMessageReceived(mostRecentInAppToast.getMessage(), s, string2);
        }
        else {
            if (!mostRecentInAppToast.getClass().getSimpleName().equals("InAppMessageModal")) {
                return InAppMessageOperation.DISCARD;
            }
            final InAppMessageModal mostRecentInAppDialog = (InAppMessageModal)mostRecentInAppToast;
            this._mostRecentInAppDialog = mostRecentInAppDialog;
            final List<MessageButton> messageButtons = mostRecentInAppDialog.getMessageButtons();
            this._mostRecentButton0 = messageButtons.get(0);
            this._mostRecentButton1 = messageButtons.get(1);
            String string3;
            if (this._mostRecentButton0.getUri() != null) {
                string3 = this._mostRecentButton0.getUri().toString();
            }
            else {
                string3 = "";
            }
            if (this._mostRecentButton1.getUri() != null) {
                string = this._mostRecentButton1.getUri().toString();
            }
            this.nativeBrazeModalDialogMessageReceived(this._mostRecentInAppDialog.getHeader(), this._mostRecentInAppDialog.getMessage(), this._mostRecentInAppDialog.getRemoteImageUrl(), this._mostRecentButton0.getText(), string3, this._mostRecentButton1.getText(), string);
        }
        mostRecentInAppToast.logImpression();
        return InAppMessageOperation.DISCARD;
    }
    
    public void logClickOnMostRecentDialog(final int n) {
        final InAppMessageModal mostRecentInAppDialog = this._mostRecentInAppDialog;
        if (mostRecentInAppDialog != null) {
            if (n == 0) {
                final MessageButton mostRecentButton0 = this._mostRecentButton0;
                if (mostRecentButton0 != null) {
                    mostRecentInAppDialog.logButtonClick(mostRecentButton0);
                    return;
                }
            }
            if (n == 1) {
                final MessageButton mostRecentButton2 = this._mostRecentButton1;
                if (mostRecentButton2 != null) {
                    this._mostRecentInAppDialog.logButtonClick(mostRecentButton2);
                }
            }
        }
    }
    
    public void logClickOnMostRecentToast() {
        final IInAppMessage mostRecentInAppToast = this._mostRecentInAppToast;
        if (mostRecentInAppToast != null) {
            mostRecentInAppToast.logClick();
        }
    }
    
    native void nativeBrazeModalDialogMessageReceived(final String p0, final String p1, final String p2, final String p3, final String p4, final String p5, final String p6);
    
    native void nativeBrazeToastMessageReceived(final String p0, final String p1, final String p2);
    
    @Override
    public boolean onInAppMessageButtonClicked(final MessageButton messageButton, final InAppMessageCloser inAppMessageCloser) {
        return true;
    }
    
    @Override
    public boolean onInAppMessageClicked(final IInAppMessage inAppMessage, final InAppMessageCloser inAppMessageCloser) {
        return true;
    }
    
    @Override
    public void onInAppMessageDismissed(final IInAppMessage inAppMessage) {
    }
    
    @Override
    public boolean onInAppMessageReceived(final IInAppMessage inAppMessage) {
        return false;
    }
}
