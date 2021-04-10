package com.appboy.ui.inappmessage;

import com.appboy.enums.inappmessage.*;
import com.appboy.ui.support.*;
import android.content.*;
import android.os.*;
import java.io.*;
import android.net.*;
import com.appboy.enums.*;
import com.appboy.*;
import com.facebook.drawee.backends.pipeline.*;
import com.facebook.imagepipeline.request.*;
import com.facebook.datasource.*;
import com.appboy.models.*;
import com.appboy.support.*;

public class AppboyAsyncInAppMessageDisplayer extends AsyncTask<IInAppMessage, Integer, IInAppMessage>
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyAsyncInAppMessageDisplayer.class);
    }
    
    protected IInAppMessage doInBackground(final IInAppMessage... array) {
        final IInAppMessage inAppMessage = array[0];
        try {
            if (inAppMessage.isControl()) {
                AppboyLogger.d(AppboyAsyncInAppMessageDisplayer.TAG, "Skipping in-app message preparation for control in-app message.");
                return inAppMessage;
            }
            AppboyLogger.d(AppboyAsyncInAppMessageDisplayer.TAG, "Starting asynchronous in-app message preparation.");
            final Context applicationContext = AppboyInAppMessageManager.getInstance().getApplicationContext();
            if (inAppMessage instanceof InAppMessageHtmlFull) {
                if (!this.prepareInAppMessageWithHtml(inAppMessage)) {
                    inAppMessage.logDisplayFailure(InAppMessageFailureType.ZIP_ASSET_DOWNLOAD);
                    return null;
                }
            }
            else {
                boolean b;
                if (FrescoLibraryUtils.canUseFresco(applicationContext)) {
                    b = this.prepareInAppMessageWithFresco(inAppMessage);
                }
                else {
                    b = this.prepareInAppMessageWithBitmapDownload(inAppMessage);
                }
                if (!b) {
                    inAppMessage.logDisplayFailure(InAppMessageFailureType.IMAGE_DOWNLOAD);
                    return null;
                }
            }
            return inAppMessage;
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyAsyncInAppMessageDisplayer.TAG, "Error running AsyncInAppMessageDisplayer", ex);
            return null;
        }
    }
    
    protected void onPostExecute(final IInAppMessage inAppMessage) {
        Label_0043: {
            if (inAppMessage == null) {
                break Label_0043;
            }
            while (true) {
                try {
                    AppboyLogger.d(AppboyAsyncInAppMessageDisplayer.TAG, "Finished asynchronous in-app message preparation. Attempting to display in-app message.");
                    new Handler(AppboyInAppMessageManager.getInstance().getApplicationContext().getMainLooper()).post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            AppboyLogger.d(AppboyAsyncInAppMessageDisplayer.TAG, "Displaying in-app message.");
                            AppboyInAppMessageManager.getInstance().displayInAppMessage(inAppMessage, false);
                        }
                    });
                    return;
                    AppboyLogger.e(AppboyAsyncInAppMessageDisplayer.TAG, "Cannot display the in-app message because the in-app message was null.");
                    return;
                    final Exception ex;
                    AppboyLogger.e(AppboyAsyncInAppMessageDisplayer.TAG, "Error running onPostExecute", ex);
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    boolean prepareInAppMessageWithBitmapDownload(final IInAppMessage inAppMessage) {
        if (inAppMessage.getBitmap() != null) {
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message already contains image bitmap. Not downloading image from URL.");
            inAppMessage.setImageDownloadSuccessful(true);
            return true;
        }
        final String localImageUrl = inAppMessage.getLocalImageUrl();
        if (!StringUtils.isNullOrBlank(localImageUrl) && new File(localImageUrl).exists()) {
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message has local image url.");
            inAppMessage.setBitmap(AppboyImageUtils.getBitmap(Uri.parse(localImageUrl)));
        }
        if (inAppMessage.getBitmap() == null) {
            final String remoteImageUrl = inAppMessage.getRemoteImageUrl();
            if (StringUtils.isNullOrBlank(remoteImageUrl)) {
                AppboyLogger.w(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message has no remote image url. Not downloading image.");
                return true;
            }
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message has remote image url. Downloading.");
            AppboyViewBounds appboyViewBounds = AppboyViewBounds.NO_BOUNDS;
            if (inAppMessage instanceof InAppMessageSlideup) {
                appboyViewBounds = AppboyViewBounds.IN_APP_MESSAGE_SLIDEUP;
            }
            else if (inAppMessage instanceof InAppMessageModal) {
                appboyViewBounds = AppboyViewBounds.IN_APP_MESSAGE_MODAL;
            }
            final Context applicationContext = AppboyInAppMessageManager.getInstance().getApplicationContext();
            inAppMessage.setBitmap(Appboy.getInstance(applicationContext).getAppboyImageLoader().getBitmapFromUrl(applicationContext, remoteImageUrl, appboyViewBounds));
        }
        if (inAppMessage.getBitmap() != null) {
            inAppMessage.setImageDownloadSuccessful(true);
            return true;
        }
        return false;
    }
    
    boolean prepareInAppMessageWithFresco(final IInAppMessage inAppMessage) {
        final String localImageUrl = inAppMessage.getLocalImageUrl();
        if (!StringUtils.isNullOrBlank(localImageUrl) && new File(localImageUrl).exists()) {
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message has local image url for Fresco display. Not downloading image.");
            inAppMessage.setImageDownloadSuccessful(true);
            return true;
        }
        inAppMessage.setLocalImageUrl(null);
        final String remoteImageUrl = inAppMessage.getRemoteImageUrl();
        if (StringUtils.isNullOrBlank(remoteImageUrl)) {
            AppboyLogger.w(AppboyAsyncInAppMessageDisplayer.TAG, "In-app message has no remote image url. Not downloading image.");
            return true;
        }
        final DataSource prefetchToDiskCache = Fresco.getImagePipeline().prefetchToDiskCache(ImageRequest.fromUri(remoteImageUrl), new Object());
        while (!prefetchToDiskCache.isFinished()) {}
        final boolean b = prefetchToDiskCache.hasFailed() ^ true;
        if (b) {
            inAppMessage.setImageDownloadSuccessful(true);
        }
        else {
            String s;
            StringBuilder sb;
            String s2;
            if (prefetchToDiskCache.getFailureCause() == null) {
                s = AppboyAsyncInAppMessageDisplayer.TAG;
                sb = new StringBuilder();
                s2 = "Fresco disk prefetch failed with null cause for remote image url:";
            }
            else {
                s = AppboyAsyncInAppMessageDisplayer.TAG;
                sb = new StringBuilder();
                sb.append("Fresco disk prefetch failed with cause: ");
                sb.append(prefetchToDiskCache.getFailureCause().getMessage());
                s2 = " with remote image url: ";
            }
            sb.append(s2);
            sb.append(remoteImageUrl);
            AppboyLogger.w(s, sb.toString());
        }
        prefetchToDiskCache.close();
        return b;
    }
    
    boolean prepareInAppMessageWithHtml(final IInAppMessage inAppMessage) {
        final InAppMessageHtmlBase inAppMessageHtmlBase = (InAppMessageHtmlBase)inAppMessage;
        final String localAssetsDirectoryUrl = inAppMessageHtmlBase.getLocalAssetsDirectoryUrl();
        if (!StringUtils.isNullOrBlank(localAssetsDirectoryUrl) && new File(localAssetsDirectoryUrl).exists()) {
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "Local assets for html in-app message are already populated. Not downloading assets.");
            return true;
        }
        if (StringUtils.isNullOrBlank(inAppMessageHtmlBase.getAssetsZipRemoteUrl())) {
            AppboyLogger.i(AppboyAsyncInAppMessageDisplayer.TAG, "Html in-app message has no remote asset zip. Continuing with in-app message preparation.");
            return true;
        }
        final String localHtmlUrlFromRemoteUrl = WebContentUtils.getLocalHtmlUrlFromRemoteUrl(WebContentUtils.getHtmlInAppMessageAssetCacheDirectory(AppboyInAppMessageManager.getInstance().getApplicationContext()), inAppMessageHtmlBase.getAssetsZipRemoteUrl());
        if (!StringUtils.isNullOrBlank(localHtmlUrlFromRemoteUrl)) {
            final String tag = AppboyAsyncInAppMessageDisplayer.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Local url for html in-app message assets is ");
            sb.append(localHtmlUrlFromRemoteUrl);
            AppboyLogger.d(tag, sb.toString());
            inAppMessageHtmlBase.setLocalAssetsDirectoryUrl(localHtmlUrlFromRemoteUrl);
            return true;
        }
        final String tag2 = AppboyAsyncInAppMessageDisplayer.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Download of html content to local directory failed for remote url: ");
        sb2.append(inAppMessageHtmlBase.getAssetsZipRemoteUrl());
        sb2.append(" . Returned local url is: ");
        sb2.append(localHtmlUrlFromRemoteUrl);
        AppboyLogger.w(tag2, sb2.toString());
        return false;
    }
}
