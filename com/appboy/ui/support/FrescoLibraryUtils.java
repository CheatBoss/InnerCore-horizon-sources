package com.appboy.ui.support;

import android.content.*;
import android.net.*;
import com.appboy.support.*;
import com.appboy.configuration.*;
import com.facebook.drawee.view.*;
import com.facebook.imagepipeline.image.*;
import com.appboy.*;
import com.facebook.drawee.controller.*;
import android.graphics.drawable.*;
import com.facebook.drawee.backends.pipeline.*;
import com.facebook.imagepipeline.request.*;
import com.facebook.drawee.interfaces.*;

public class FrescoLibraryUtils
{
    private static final String FILE_SCHEME = "file";
    private static final String HTTPS_SCHEME = "https";
    private static final String HTTP_SCHEME = "http";
    private static final String TAG;
    private static final String[] USED_FRESCO_CLASSES;
    private static boolean sCanUseFresco;
    private static boolean sCanUseFrescoSet;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(FrescoLibraryUtils.class);
        FrescoLibraryUtils.sCanUseFresco = false;
        FrescoLibraryUtils.sCanUseFrescoSet = false;
        USED_FRESCO_CLASSES = new String[] { "com.facebook.drawee.backends.pipeline.Fresco", "com.facebook.drawee.interfaces.DraweeController", "com.facebook.drawee.view.SimpleDraweeView", "com.facebook.drawee.backends.pipeline.Fresco", "com.facebook.drawee.controller.BaseControllerListener", "com.facebook.drawee.controller.ControllerListener", "com.facebook.imagepipeline.image.ImageInfo" };
    }
    
    public static boolean canUseFresco(final Context context) {
        if (FrescoLibraryUtils.sCanUseFrescoSet) {
            return FrescoLibraryUtils.sCanUseFresco;
        }
        if (!getIsFrescoEnabled(context.getApplicationContext())) {
            FrescoLibraryUtils.sCanUseFresco = false;
            FrescoLibraryUtils.sCanUseFrescoSet = true;
            return false;
        }
        try {
            final ClassLoader classLoader = FrescoLibraryUtils.class.getClassLoader();
            FrescoLibraryUtils.sCanUseFresco = true;
            final String[] used_FRESCO_CLASSES = FrescoLibraryUtils.USED_FRESCO_CLASSES;
            for (int length = used_FRESCO_CLASSES.length, i = 0; i < length; ++i) {
                if (Class.forName(used_FRESCO_CLASSES[i], false, classLoader) == null) {
                    break;
                }
            }
        }
        catch (Exception ex) {}
        catch (NoClassDefFoundError noClassDefFoundError) {}
        finally {
            FrescoLibraryUtils.sCanUseFresco = false;
        }
        FrescoLibraryUtils.sCanUseFrescoSet = true;
        return FrescoLibraryUtils.sCanUseFresco;
    }
    
    static Uri getFrescoUri(final String s) {
        final Uri parse = Uri.parse(s);
        if (StringUtils.isNullOrBlank(parse.getScheme())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("file://");
            sb.append(s);
            return Uri.parse(sb.toString());
        }
        return parse;
    }
    
    private static boolean getIsFrescoEnabled(final Context context) {
        return new AppboyConfigurationProvider(context).getIsFrescoLibraryUseEnabled();
    }
    
    public static void setDraweeControllerHelper(final SimpleDraweeView simpleDraweeView, final String s, final float n, final boolean b) {
        setDraweeControllerHelper(simpleDraweeView, s, n, b, null);
    }
    
    public static void setDraweeControllerHelper(final SimpleDraweeView simpleDraweeView, final String s, final float n, final boolean b, final ControllerListener<ImageInfo> controllerListener) {
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(FrescoLibraryUtils.TAG, "The url set for the Drawee controller was null. Controller not set.");
            return;
        }
        if (simpleDraweeView == null) {
            AppboyLogger.w(FrescoLibraryUtils.TAG, "The SimpleDraweeView set for the Drawee controller was null. Controller not set.");
            return;
        }
        ImageRequest$RequestLevel lowestPermittedRequestLevel;
        if (Appboy.getOutboundNetworkRequestsOffline()) {
            lowestPermittedRequestLevel = ImageRequest$RequestLevel.DISK_CACHE;
        }
        else {
            lowestPermittedRequestLevel = ImageRequest$RequestLevel.FULL_FETCH;
        }
        final String tag = FrescoLibraryUtils.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Setting Fresco image request level to: ");
        sb.append(lowestPermittedRequestLevel);
        AppboyLogger.d(tag, sb.toString());
        Object controllerListener2 = controllerListener;
        if (controllerListener == null) {
            controllerListener2 = new BaseControllerListener<ImageInfo>() {
                public void onFinalImageSet(final String s, final ImageInfo imageInfo, final Animatable animatable) {
                    if (imageInfo == null) {
                        return;
                    }
                    float val$aspectRatio;
                    if (b) {
                        val$aspectRatio = n;
                    }
                    else {
                        val$aspectRatio = (float)(imageInfo.getWidth() / imageInfo.getHeight());
                    }
                    simpleDraweeView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            simpleDraweeView.setAspectRatio(val$aspectRatio);
                        }
                    });
                }
            };
        }
        try {
            final Uri frescoUri = getFrescoUri(s);
            simpleDraweeView.setController((DraweeController)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)((PipelineDraweeControllerBuilder)Fresco.newDraweeControllerBuilder().setUri(frescoUri).setAutoPlayAnimations(true)).setTapToRetryEnabled(true)).setControllerListener((ControllerListener)controllerListener2)).setImageRequest((Object)ImageRequestBuilder.newBuilderWithSource(frescoUri).setLowestPermittedRequestLevel(lowestPermittedRequestLevel).build())).build());
        }
        catch (Exception ex) {
            AppboyLogger.e(FrescoLibraryUtils.TAG, "Fresco controller builder could not be retrieved. Fresco most likely prematurely shutdown.", ex);
        }
        catch (NullPointerException ex2) {
            AppboyLogger.e(FrescoLibraryUtils.TAG, "Fresco controller builder could not be retrieved. Fresco most likely prematurely shutdown.", ex2);
        }
    }
}
