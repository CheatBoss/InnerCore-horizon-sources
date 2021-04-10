package com.zhekasmirnov.innercore.ui;

import android.app.*;
import android.widget.*;
import java.util.*;
import android.util.*;
import android.content.*;
import android.graphics.drawable.*;
import android.view.*;

public class ModLoadingOverlay
{
    private static final ArrayList<ModLoadingOverlay> overlayInstances;
    private final Activity context;
    private LoadingOverlayDrawable drawable;
    private boolean isOpened;
    private ImageView view;
    private final WindowManager$LayoutParams windowParams;
    
    static {
        overlayInstances = new ArrayList<ModLoadingOverlay>();
    }
    
    public ModLoadingOverlay(final Activity context) {
        this.isOpened = false;
        this.context = context;
        this.windowParams = new WindowManager$LayoutParams(-1, -1, 1000, 256, -3);
    }
    
    public static void sendLoadingProgress(final float progress) {
        while (true) {
            while (true) {
                Label_0058: {
                    synchronized (ModLoadingOverlay.overlayInstances) {
                        final Iterator<ModLoadingOverlay> iterator = ModLoadingOverlay.overlayInstances.iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final ModLoadingOverlay modLoadingOverlay = iterator.next();
                        if (modLoadingOverlay.drawable != null) {
                            modLoadingOverlay.drawable.setProgress(progress);
                            break Label_0058;
                        }
                        break Label_0058;
                    }
                }
                continue;
            }
        }
    }
    
    public static void sendLoadingText(final String text) {
        while (true) {
            while (true) {
                Label_0058: {
                    synchronized (ModLoadingOverlay.overlayInstances) {
                        final Iterator<ModLoadingOverlay> iterator = ModLoadingOverlay.overlayInstances.iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final ModLoadingOverlay modLoadingOverlay = iterator.next();
                        if (modLoadingOverlay.drawable != null) {
                            modLoadingOverlay.drawable.setText(text);
                            break Label_0058;
                        }
                        break Label_0058;
                    }
                }
                continue;
            }
        }
    }
    
    public static void sendLoadingTip(final String tip) {
        while (true) {
            while (true) {
                Label_0058: {
                    synchronized (ModLoadingOverlay.overlayInstances) {
                        final Iterator<ModLoadingOverlay> iterator = ModLoadingOverlay.overlayInstances.iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final ModLoadingOverlay modLoadingOverlay = iterator.next();
                        if (modLoadingOverlay.drawable != null) {
                            modLoadingOverlay.drawable.setTip(tip);
                            break Label_0058;
                        }
                        break Label_0058;
                    }
                }
                continue;
            }
        }
    }
    
    public boolean await(final int n) {
        this.context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ModLoadingOverlay.this.openNow();
            }
        });
        final long currentTimeMillis = System.currentTimeMillis();
        while (!this.isOpened && currentTimeMillis + n > System.currentTimeMillis()) {
            try {
                Thread.sleep(10L);
                continue;
            }
            catch (InterruptedException ex) {}
            break;
        }
        return false;
    }
    
    public void close() {
        this.context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ModLoadingOverlay.this.closeNow();
            }
        });
    }
    
    public void closeNow() {
        this.isOpened = false;
        final WindowManager windowManager = (WindowManager)this.context.getSystemService("window");
        if (this.view != null) {
            try {
                windowManager.removeView((View)this.view);
            }
            catch (Exception ex) {}
        }
        Object o = ModLoadingOverlay.overlayInstances;
        synchronized (o) {
            ModLoadingOverlay.overlayInstances.remove(this);
            // monitorexit(o)
            o = MainMenuBanner.getInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append("loading-");
            sb.append(this.hashCode());
            ((MainMenuBanner)o).close(sb.toString());
        }
    }
    
    public void openNow() {
        final WindowManager windowManager = (WindowManager)this.context.getSystemService("window");
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        final int max = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        final int min = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
        if (this.view != null) {
            try {
                windowManager.removeView((View)this.view);
            }
            catch (Exception ex) {}
        }
        (this.view = new ImageView((Context)this.context)).setMinimumWidth(max);
        this.view.setMinimumHeight(min);
        this.view.setSystemUiVisibility(5894);
        this.view.setImageDrawable((Drawable)(this.drawable = new LoadingOverlayDrawable((View)this.view)));
        windowManager.addView((View)this.view, (ViewGroup$LayoutParams)this.windowParams);
        this.isOpened = true;
        Object o = ModLoadingOverlay.overlayInstances;
        synchronized (o) {
            ModLoadingOverlay.overlayInstances.add(this);
            // monitorexit(o)
            o = MainMenuBanner.getInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append("loading-");
            sb.append(this.hashCode());
            ((MainMenuBanner)o).show(sb.toString(), MainMenuBanner.Location.LEFT_SIDE);
        }
    }
}
