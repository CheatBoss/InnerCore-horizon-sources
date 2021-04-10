package com.microsoft.xbox.xle.ui;

import com.microsoft.xbox.toolkit.ui.*;
import android.content.*;
import android.util.*;
import android.view.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.app.*;
import android.content.res.*;
import android.view.accessibility.*;
import android.widget.*;

public class IconFontSubTextButton extends LinearLayout
{
    private FrameLayout iconFrameLayout;
    private CustomTypefaceTextView iconTextView;
    private CustomTypefaceTextView subtitleTextView;
    private CustomTypefaceTextView titleTextView;
    
    public IconFontSubTextButton(final Context context) {
        this(context, null);
    }
    
    public IconFontSubTextButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public IconFontSubTextButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        LayoutInflater.from(context).inflate(R$layout.icon_font_subtext_button, (ViewGroup)this, true);
        this.iconTextView = (CustomTypefaceTextView)this.findViewById(R$id.icon_font_subtext_icon);
        this.iconFrameLayout = (FrameLayout)this.findViewById(R$id.icon_font_subtext_btn_icon_bg);
        this.titleTextView = (CustomTypefaceTextView)this.findViewById(R$id.icon_font_subtext_btn_title);
        this.subtitleTextView = (CustomTypefaceTextView)this.findViewById(R$id.icon_font_subtext_btn_subtitle);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("IconFontSubTextButton"));
        final String string = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("IconFontSubTextButton_icon_uri"));
        final String string2 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("IconFontSubTextButton_text_title"));
        final String string3 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("IconFontSubTextButton_text_subtitle"));
        this.iconFrameLayout.setBackgroundColor(obtainStyledAttributes.getColor(XLERValueHelper.getStyleableRValue("IconFontSubTextButton_icon_bg"), 0));
        obtainStyledAttributes.recycle();
        XLEUtil.updateTextAndVisibilityIfNotNull(this.iconTextView, string, 0);
        XLEUtil.updateTextAndVisibilityIfNotNull(this.titleTextView, string2, 0);
        XLEUtil.updateTextAndVisibilityIfNotNull(this.subtitleTextView, string3, 0);
        this.setFocusable(true);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
}
