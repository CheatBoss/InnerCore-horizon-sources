package com.zhekasmirnov.innercore.ui;

import java.util.*;
import android.widget.*;
import android.app.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.content.*;
import com.google.android.gms.ads.formats.*;
import android.view.*;

public class MainMenuBanner
{
    private static final MainMenuBanner instance;
    private final double density;
    public final boolean isAvailable;
    private final HashMap<String, PopupWindow> windows;
    
    static {
        instance = new MainMenuBanner();
    }
    
    private MainMenuBanner() {
        this.density = AdsManager.getInstance().getDesiredAdDensity();
        this.windows = new HashMap<String, PopupWindow>();
        final double density = this.density;
        boolean isAvailable = false;
        if (density == 1.0) {
            this.isAvailable = false;
            return;
        }
        if (this.density < 0.8) {
            this.isAvailable = false;
            return;
        }
        if (Math.random() < (this.density - 0.8) * 10.0) {
            isAvailable = true;
        }
        this.isAvailable = isAvailable;
    }
    
    public static MainMenuBanner getInstance() {
        return MainMenuBanner.instance;
    }
    
    private static int getScreenHeight(final Activity activity) {
        final Display defaultDisplay = ((WindowManager)activity.getSystemService("window")).getDefaultDisplay();
        return Math.min(defaultDisplay.getWidth(), defaultDisplay.getHeight());
    }
    
    private static int getScreenWidth(final Activity activity) {
        final Display defaultDisplay = ((WindowManager)activity.getSystemService("window")).getDefaultDisplay();
        return Math.max(defaultDisplay.getWidth(), defaultDisplay.getHeight());
    }
    
    public void close(final String s) {
        AdsManager.getInstance().getContext().runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                synchronized (MainMenuBanner.this.windows) {
                    final PopupWindow popupWindow = MainMenuBanner.this.windows.get(s);
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                        MainMenuBanner.this.windows.remove(s);
                    }
                }
            }
        });
    }
    
    public boolean isShowed(final String s) {
        return this.windows.get(s) != null;
    }
    
    public boolean runFullScreenAdRandom() {
        if (this.density < 0.8) {
            return AdsManager.getInstance().runDesiredDensityRandom();
        }
        if (this.density < 0.9) {
            return true ^ this.isAvailable;
        }
        return Math.random() < (this.density - 0.9) * 10.0;
    }
    
    public void show(final String s, final Location location) {
        if (!this.isAvailable) {
            return;
        }
        if (this.isShowed(s)) {
            return;
        }
        AdsManager.getInstance().loadConcurrentAd(new String[] { "native" }, 200, false, (AdsManager$AdListener)new AdsManager$AdListener() {
            public void onAdLoaded(final AdContainer adContainer) {
                final Activity context = AdsManager.getInstance().getContext();
                boolean b = false;
                synchronized (MainMenuBanner.this.windows) {
                    PopupWindow popupWindow;
                    if ((popupWindow = MainMenuBanner.this.windows.get(s)) == null) {
                        b = true;
                        final UnifiedNativeAdView unifiedNativeAdView = new UnifiedNativeAdView((Context)context);
                        final MediaView mediaView = new MediaView((Context)context);
                        mediaView.setId(65538);
                        unifiedNativeAdView.addView((View)mediaView, (ViewGroup$LayoutParams)new WindowManager$LayoutParams(-1, -1));
                        popupWindow = new PopupWindow((View)unifiedNativeAdView);
                        MainMenuBanner.this.windows.put(s, popupWindow);
                    }
                    // monitorexit(MainMenuBanner.access$000(this.this$0))
                    if (b) {
                        final int[] array = new int[2];
                        context.getWindow().getDecorView().getLocationOnScreen(array);
                        final int access$100 = getScreenWidth(context);
                        final int access$101 = getScreenHeight(context);
                        switch (location) {
                            case LEFT_SIDE: {
                                popupWindow.setWidth((int)(access$100 * 0.2));
                                popupWindow.setHeight((int)(access$101 * 0.35));
                                popupWindow.showAtLocation(context.getWindow().getDecorView(), 51, 0, (int)(access$101 * 0.325));
                                break;
                            }
                            case BOTTOM: {
                                popupWindow.setWidth((access$100 - array[0]) / 3);
                                popupWindow.setHeight((int)(access$101 * 0.2));
                                popupWindow.showAtLocation(context.getWindow().getDecorView(), 83, (access$100 - array[0]) / 3 + array[0], 0);
                                break;
                            }
                        }
                    }
                    adContainer.inflate((ViewGroup)popupWindow.getContentView());
                }
            }
        }, new String[] { "horizon-dev", "pack-main-menu" });
    }
    
    public enum Location
    {
        BOTTOM, 
        LEFT_SIDE;
    }
}
