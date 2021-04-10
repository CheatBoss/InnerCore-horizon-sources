package com.appboy.ui.inappmessage.views;

import com.appboy.support.*;
import com.appboy.ui.inappmessage.*;
import java.util.*;
import com.appboy.models.*;
import android.view.*;
import com.appboy.ui.support.*;
import android.graphics.drawable.*;
import android.content.*;
import com.appboy.ui.*;
import android.graphics.*;
import android.widget.*;
import com.appboy.enums.inappmessage.*;

public class InAppMessageViewUtils
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(InAppMessageViewUtils.class);
    }
    
    public static void closeInAppMessageOnKeycodeBack() {
        AppboyLogger.d(InAppMessageViewUtils.TAG, "Back button intercepted by in-app message view, closing in-app message.");
        AppboyInAppMessageManager.getInstance().hideCurrentlyDisplayingInAppMessage(true);
    }
    
    public static boolean isValidIcon(final String s) {
        return s != null;
    }
    
    public static boolean isValidInAppMessageColor(final int n) {
        return n != 0;
    }
    
    protected static void resetButtonSizesIfNecessary(final List<View> list, final List<MessageButton> list2) {
        if (list2 != null && list2.size() == 1) {
            list.get(0).setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, -2, 1.0f));
        }
    }
    
    protected static void resetMessageMarginsIfNecessary(final TextView textView, final TextView textView2) {
        if (textView2 == null && textView != null) {
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(textView.getLayoutParams().width, textView.getLayoutParams().height);
            layoutParams.setMargins(0, 0, 0, 0);
            textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
    }
    
    public static void setButtons(final List<View> list, final View view, final int n, final List<MessageButton> list2) {
        if (list2 != null && list2.size() != 0) {
            for (int i = 0; i < list.size(); ++i) {
                if (list2.size() <= i) {
                    list.get(i).setVisibility(8);
                }
                else if (list.get(i) instanceof Button) {
                    final Button button = (Button)list.get(i);
                    final MessageButton messageButton = list2.get(i);
                    button.setText((CharSequence)messageButton.getText());
                    setTextViewColor((TextView)button, messageButton.getTextColor());
                    setDrawableColor(button.getBackground(), messageButton.getBackgroundColor(), n);
                }
            }
            return;
        }
        ViewUtils.removeViewFromParent(view);
    }
    
    public static void setDrawableColor(final Drawable drawable, final int n, final int n2) {
        if (drawable instanceof GradientDrawable) {
            setDrawableColor((GradientDrawable)drawable, n, n2);
            return;
        }
        if (isValidInAppMessageColor(n)) {
            drawable.setColorFilter(n, PorterDuff$Mode.MULTIPLY);
            return;
        }
        drawable.setColorFilter(n2, PorterDuff$Mode.MULTIPLY);
    }
    
    public static void setDrawableColor(final GradientDrawable gradientDrawable, final int color, final int color2) {
        if (isValidInAppMessageColor(color)) {
            gradientDrawable.setColor(color);
            return;
        }
        gradientDrawable.setColor(color2);
    }
    
    public static void setFrameColor(final View view, final Integer n) {
        if (n != null) {
            view.setBackgroundColor((int)n);
        }
    }
    
    public static void setIcon(final Context context, final String text, final int n, final int n2, final TextView textView) {
        if (isValidIcon(text)) {
            try {
                textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf"));
                textView.setText((CharSequence)text);
                setTextViewColor(textView, n);
                if (textView.getBackground() != null) {
                    setDrawableColor(textView.getBackground(), n2, context.getResources().getColor(R$color.com_appboy_inappmessage_icon_background));
                    return;
                }
                setViewBackgroundColor((View)textView, n2);
            }
            catch (Exception ex) {
                AppboyLogger.e(InAppMessageViewUtils.TAG, "Caught exception setting icon typeface. Not rendering icon.", ex);
            }
        }
    }
    
    public static void setImage(final Bitmap imageBitmap, final ImageView imageView) {
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }
    
    public static void setTextAlignment(final TextView textView, final TextAlign textAlign) {
        int gravity;
        if (textAlign.equals(TextAlign.START)) {
            gravity = 8388611;
        }
        else {
            if (!textAlign.equals(TextAlign.END)) {
                if (textAlign.equals(TextAlign.CENTER)) {
                    textView.setGravity(17);
                }
                return;
            }
            gravity = 8388613;
        }
        textView.setGravity(gravity);
    }
    
    public static void setTextViewColor(final TextView textView, final int textColor) {
        if (isValidInAppMessageColor(textColor)) {
            textView.setTextColor(textColor);
        }
    }
    
    public static void setViewBackgroundColor(final View view, final int backgroundColor) {
        if (isValidInAppMessageColor(backgroundColor)) {
            view.setBackgroundColor(backgroundColor);
        }
    }
    
    public static void setViewBackgroundColorFilter(final View view, final int n, final int n2) {
        if (isValidInAppMessageColor(n)) {
            view.getBackground().setColorFilter(n, PorterDuff$Mode.MULTIPLY);
            return;
        }
        view.getBackground().setColorFilter(n2, PorterDuff$Mode.MULTIPLY);
    }
}
