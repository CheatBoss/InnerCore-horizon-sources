package com.zhekasmirnov.innercore.api.runtime.other;

import android.os.*;
import com.zhekasmirnov.innercore.utils.*;
import android.widget.*;
import android.content.*;

public class PrintStacking
{
    private static final int LONG_DELAY = 1500;
    private static final int MAX_LENGTH = 4096;
    private static final int SHORT_DELAY = 100;
    private static Handler handler;
    private static boolean isShowPosted;
    private static long last;
    private static final Object lock;
    private static final Runnable showAndClearRunnable;
    private static String text;
    
    static {
        lock = new Object();
        PrintStacking.isShowPosted = false;
        PrintStacking.last = -1L;
        PrintStacking.text = "";
        showAndClearRunnable = new Runnable() {
            @Override
            public void run() {
                showAndClear();
            }
        };
    }
    
    private static void post(final Runnable runnable, final int n) {
        if (PrintStacking.handler != null) {
            PrintStacking.handler.postDelayed(runnable, (long)n);
        }
    }
    
    private static void postShowAndClear(final int n) {
        PrintStacking.isShowPosted = true;
        post(PrintStacking.showAndClearRunnable, n);
    }
    
    public static void prepare() {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PrintStacking.handler = new Handler();
            }
        });
    }
    
    public static void print(final String s) {
        synchronized (PrintStacking.lock) {
            if (PrintStacking.text.length() > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(PrintStacking.text);
                sb.append("\n");
                PrintStacking.text = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(PrintStacking.text);
            sb2.append(s);
            PrintStacking.text = sb2.toString();
            // monitorexit(PrintStacking.lock)
            if (!PrintStacking.isShowPosted) {
                final long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - PrintStacking.last > 1500L) {
                    postShowAndClear(100);
                    return;
                }
                postShowAndClear((int)(1500L - (currentTimeMillis - PrintStacking.last)));
            }
        }
    }
    
    private static void showAndClear() {
        if (PrintStacking.text.length() > 0) {
            synchronized (PrintStacking.lock) {
                final String text = PrintStacking.text;
                PrintStacking.text = "";
                UIUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s;
                        if (text.length() > 4096) {
                            s = text.substring(0, 4096);
                        }
                        else {
                            s = text;
                        }
                        Toast.makeText((Context)UIUtils.getContext(), (CharSequence)s, 1).show();
                    }
                });
            }
        }
        PrintStacking.isShowPosted = false;
        PrintStacking.last = System.currentTimeMillis();
    }
}
