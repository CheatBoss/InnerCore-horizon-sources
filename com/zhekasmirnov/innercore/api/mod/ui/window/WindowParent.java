package com.zhekasmirnov.innercore.api.mod.ui.window;

import java.util.*;
import android.widget.*;
import com.zhekasmirnov.innercore.utils.*;
import android.app.*;
import com.zhekasmirnov.innercore.api.log.*;
import android.view.*;
import android.content.*;

public class WindowParent
{
    private static HashMap<Integer, PopupWindow> attachedPopups;
    
    static {
        WindowParent.attachedPopups = new HashMap<Integer, PopupWindow>();
    }
    
    public static void applyWindowInsets(final UIWindow uiWindow, final WindowInsets windowInsets) {
        final Activity context = UIUtils.getContext();
        final UIWindowLocation location = uiWindow.getLocation();
        if (uiWindow.isNotFocusable()) {
            final PopupWindow popupWindow = WindowParent.attachedPopups.get(uiWindow.hashCode());
            if (popupWindow != null) {
                location.updatePopupWindow(popupWindow);
            }
            return;
        }
        ((WindowManager)((Context)context).getSystemService("window")).updateViewLayout((View)uiWindow.layout, (ViewGroup$LayoutParams)location.getLayoutParams(1000, getWindowFlags(uiWindow), -3));
    }
    
    public static void closeWindow(final UIWindow uiWindow) {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uiWindow.isNotFocusable()) {
                    final PopupWindow popupWindow = WindowParent.attachedPopups.get(uiWindow.hashCode());
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                        WindowParent.attachedPopups.remove(uiWindow.hashCode());
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("no popup attached to non-focusable ");
                        sb.append(uiWindow);
                        ICLog.i("ERROR", sb.toString());
                    }
                    return;
                }
                ((WindowManager)UIUtils.getContext().getSystemService("window")).removeView((View)uiWindow.layout);
            }
        });
    }
    
    private static int getWindowFlags(final UIWindow uiWindow) {
        int n = 256;
        if (uiWindow.isTouchable()) {
            if (!uiWindow.isBlockingBackground()) {
                n = (0x100 | 0x20);
            }
        }
        else {
            n = (0x100 | 0x10);
        }
        int n2 = n;
        if (uiWindow.isBlockingBackground()) {
            n2 = (n | 0x2);
        }
        return n2;
    }
    
    public static void openWindow(final UIWindow uiWindow) {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final UIWindowLocation location = uiWindow.getLocation();
                final WindowManager windowManager = (WindowManager)((Context)UIUtils.getContext()).getSystemService("window");
                if (uiWindow.isNotFocusable()) {
                    final PopupWindow popupWindow = new PopupWindow((View)uiWindow.layout);
                    uiWindow.layout.setSystemUiVisibility(5894);
                    WindowParent.attachedPopups.put(uiWindow.hashCode(), popupWindow);
                    popupWindow.setTouchable(uiWindow.isTouchable());
                    location.showPopupWindow(popupWindow);
                    return;
                }
                final int access$100 = getWindowFlags(uiWindow);
                uiWindow.layout.setSystemUiVisibility(5894);
                uiWindow.layout.setFocusable(true);
                uiWindow.layout.setFocusableInTouchMode(true);
                uiWindow.layout.requestFocus();
                uiWindow.layout.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
                    public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                        if (n == 4) {
                            WindowProvider.instance.onBackPressed();
                        }
                        return false;
                    }
                });
                windowManager.addView((View)uiWindow.layout, (ViewGroup$LayoutParams)location.getLayoutParams(1000, access$100, -3));
            }
        });
    }
    
    public static void releaseWindowLayout(final View view) {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((WindowManager)UIUtils.getContext().getSystemService("window")).removeView(view);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }
}
