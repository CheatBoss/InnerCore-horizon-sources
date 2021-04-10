package com.zhekasmirnov.innercore.ui;

import java.lang.ref.*;
import android.app.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.util.*;
import android.content.*;
import android.graphics.drawable.*;
import com.zhekasmirnov.innercore.utils.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class LoadingUI
{
    private static ImageView backgroundView;
    private static LoadingOverlayDrawable backgroundViewDrawable;
    private static WeakReference<Activity> context;
    private static Handler customUiThreadHandler;
    private static Looper customUiThreadLooper;
    private static Handler handler;
    private static boolean isOpened;
    private static boolean isShowed;
    private static WindowManager windowManager;
    private static WindowManager$LayoutParams windowParams;
    
    static {
        LoadingUI.isShowed = false;
        LoadingUI.isOpened = false;
    }
    
    public static void close() {
        if (LoadingUI.isOpened) {
            LoadingUI.isOpened = false;
        }
    }
    
    public static void hide() {
        if (LoadingUI.context == null) {
            return;
        }
        if (LoadingUI.windowManager == null) {
            return;
        }
        if (LoadingUI.isShowed) {
            Logger.debug("INNERCORE-LOADING-UI", "hiding...");
            LoadingUI.isShowed = false;
            try {
                LoadingUI.windowManager.removeViewImmediate((View)LoadingUI.backgroundView);
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    public static void initViews() {
        final Activity activity = LoadingUI.context.get();
        if (activity == null) {
            return;
        }
        if (LoadingUI.windowManager == null) {
            return;
        }
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int max = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        final int min = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        (LoadingUI.backgroundView = new ImageView((Context)activity)).setMinimumWidth(max);
        LoadingUI.backgroundView.setMinimumHeight(min);
        LoadingUI.backgroundViewDrawable = new LoadingOverlayDrawable((View)LoadingUI.backgroundView);
        LoadingUI.backgroundView.setImageDrawable((Drawable)LoadingUI.backgroundViewDrawable);
    }
    
    public static void initializeFor(final Activity activity) {
        LoadingUI.context = new WeakReference<Activity>(activity);
        LoadingUI.windowParams = new WindowManager$LayoutParams(-1, -1, 2002, 256, -3);
        LoadingUI.windowManager = (WindowManager)activity.getApplicationContext().getSystemService("window");
        LoadingUI.handler = new Handler();
        initViews();
    }
    
    public static boolean isShowed() {
        return LoadingUI.isShowed;
    }
    
    public static void open() {
        if (!LoadingUI.isOpened) {
            LoadingUI.isOpened = true;
            runCustomUiThread();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    postOnCustomUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoadingUI.show();
                            Logger.debug("LOADING-UI", "opened");
                        }
                    });
                    while (LoadingUI.isOpened) {
                        final boolean innerCoreActivityOpened = UIUtils.isInnerCoreActivityOpened();
                        if (innerCoreActivityOpened && !LoadingUI.isShowed) {
                            Logger.debug("LOADING-UI", "application open, showing...");
                            postOnCustomUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingUI.show();
                                }
                            });
                        }
                        if (!innerCoreActivityOpened && LoadingUI.isShowed) {
                            postOnCustomUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingUI.hide();
                                }
                            });
                        }
                        try {
                            Thread.sleep(75L);
                        }
                        catch (InterruptedException ex) {}
                    }
                    postOnCustomUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoadingUI.hide();
                            stopCustomUiThread();
                        }
                    });
                }
            }).start();
        }
    }
    
    private static void postOnCustomUiThread(final Runnable runnable) {
        LoadingUI.customUiThreadHandler.post(runnable);
    }
    
    private static void runCustomUiThread() {
        stopCustomUiThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(-18);
                Looper.prepare();
                LoadingUI.customUiThreadHandler = new Handler();
                LoadingUI.customUiThreadLooper = Looper.myLooper();
                Looper.loop();
            }
        }).start();
        while (LoadingUI.customUiThreadLooper == null) {
            Thread.yield();
        }
    }
    
    public static void setProgress(final float n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("updated loading ui progress: ");
        sb.append(n);
        Logger.debug("INNERCORE", sb.toString());
        ModLoadingOverlay.sendLoadingProgress(n);
    }
    
    public static void setText(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("updated loading ui text: ");
        sb.append(s);
        Logger.debug("INNERCORE", sb.toString());
        ModLoadingOverlay.sendLoadingText(s);
        ModLoadingOverlay.sendLoadingTip("");
    }
    
    public static void setTextAndProgressBar(final String s, final float n) {
        ModLoadingOverlay.sendLoadingText(s);
        ModLoadingOverlay.sendLoadingProgress(n);
    }
    
    public static void setTip(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("updated loading ui tip: ");
        sb.append(s);
        Logger.debug("INNERCORE", sb.toString());
        ModLoadingOverlay.sendLoadingTip(s);
    }
    
    public static void show() {
        if (LoadingUI.context == null) {
            return;
        }
        if (LoadingUI.windowManager == null) {
            return;
        }
        if (!LoadingUI.isShowed) {
            try {
                Logger.debug("INNERCORE-LOADING-UI", "showing...");
                LoadingUI.isShowed = true;
                LoadingUI.windowManager.addView((View)LoadingUI.backgroundView, (ViewGroup$LayoutParams)LoadingUI.windowParams);
            }
            catch (Exception ex) {
                LoadingUI.isShowed = false;
                UIUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText((Context)UIUtils.getContext(), (CharSequence)"failed to display loading overlay, try to allow system overlays in settings.", 1).show();
                    }
                });
            }
        }
    }
    
    private static void stopCustomUiThread() {
        if (LoadingUI.customUiThreadLooper != null) {
            LoadingUI.customUiThreadLooper.quit();
        }
        LoadingUI.customUiThreadHandler = null;
        LoadingUI.customUiThreadLooper = null;
    }
}
