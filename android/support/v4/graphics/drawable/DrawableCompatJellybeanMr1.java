package android.support.v4.graphics.drawable;

import java.lang.reflect.*;
import android.graphics.drawable.*;
import android.util.*;

class DrawableCompatJellybeanMr1
{
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    
    public static int getLayoutDirection(final Drawable drawable) {
        if (!DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched) {
            try {
                (DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve getLayoutDirection() method", (Throwable)ex);
            }
            DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched = true;
        }
        if (DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod != null) {
            try {
                return (int)DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod.invoke(drawable, new Object[0]);
            }
            catch (Exception ex2) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to invoke getLayoutDirection() via reflection", (Throwable)ex2);
                DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod = null;
            }
        }
        return -1;
    }
    
    public static void setLayoutDirection(final Drawable drawable, final int n) {
        if (!DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched) {
            try {
                (DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", (Throwable)ex);
            }
            DrawableCompatJellybeanMr1.sSetLayoutDirectionMethodFetched = true;
        }
        if (DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod != null) {
            try {
                DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod.invoke(drawable, n);
            }
            catch (Exception ex2) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", (Throwable)ex2);
                DrawableCompatJellybeanMr1.sSetLayoutDirectionMethod = null;
            }
        }
    }
}
