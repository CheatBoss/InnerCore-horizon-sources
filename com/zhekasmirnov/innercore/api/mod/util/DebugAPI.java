package com.zhekasmirnov.innercore.api.mod.util;

import org.mozilla.javascript.*;
import android.app.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.utils.*;
import android.content.*;
import android.widget.*;
import android.view.*;
import org.mozilla.javascript.annotations.*;

public class DebugAPI extends ScriptableObject
{
    private static AlertDialog$Builder constructDialog() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)UIUtils.getContext());
        alertDialog$Builder.setTitle((CharSequence)"debug");
        return alertDialog$Builder;
    }
    
    private static AlertDialog$Builder constructDialog(final Bitmap imageBitmap) {
        final ImageView view = new ImageView((Context)UIUtils.getContext());
        view.setImageBitmap(imageBitmap);
        return constructDialog().setView((View)view);
    }
    
    private static AlertDialog$Builder constructDialog(final String message) {
        return constructDialog().setMessage((CharSequence)message);
    }
    
    @JSStaticFunction
    public static void dialog(final String s) {
        dialog(s, "");
    }
    
    @JSStaticFunction
    public static void dialog(final String s, final String s2) {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                constructDialog(s).setTitle((CharSequence)s2).show();
            }
        });
    }
    
    @JSStaticFunction
    public static void img(final Bitmap bitmap) {
        img(bitmap, "");
    }
    
    @JSStaticFunction
    public static void img(Bitmap bitmap, final String s) {
        if (bitmap == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" bitmap is null");
            dialog(sb.toString());
            return;
        }
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final float n = UIUtils.screenWidth * 0.4f;
        final float n2 = UIUtils.screenHeight * 0.6f;
        final float n3 = bitmap.getWidth() / (float)bitmap.getHeight();
        if (n / n3 > n2) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)(n2 * n3), (int)n2, false);
        }
        else {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)n, (int)(n / n3), false);
        }
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog$Builder access$100 = constructDialog(bitmap);
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(" bitmap ");
                sb.append(width);
                sb.append("x");
                sb.append(height);
                access$100.setTitle((CharSequence)sb.toString()).show();
            }
        });
    }
    
    public String getClassName() {
        return "DebugAPI";
    }
}
