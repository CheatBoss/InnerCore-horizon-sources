package com.microsoft.xbox.xle.ui;

import com.microsoft.xbox.toolkit.ui.*;
import android.content.*;
import android.util.*;
import android.view.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.xle.app.*;
import android.content.res.*;
import android.view.accessibility.*;
import android.widget.*;
import com.microsoft.xbox.toolkit.*;
import java.net.*;

public class ImageTitleSubtitleButton extends LinearLayout
{
    private XLEUniversalImageView iconImageView;
    private CustomTypefaceTextView subtitleTextView;
    private CustomTypefaceTextView titleTextView;
    
    public ImageTitleSubtitleButton(final Context context) {
        this(context, null);
    }
    
    public ImageTitleSubtitleButton(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ImageTitleSubtitleButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        LayoutInflater.from(context).inflate(R$layout.image_title_subtitle_button, (ViewGroup)this, true);
        this.iconImageView = (XLEUniversalImageView)this.findViewById(R$id.image_title_subtitle_button_image);
        this.titleTextView = (CustomTypefaceTextView)this.findViewById(R$id.image_title_subtitle_button_title);
        this.subtitleTextView = (CustomTypefaceTextView)this.findViewById(R$id.image_title_subtitle_button_subtitle);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("ImageTitleSubtitleButton"));
        final String string = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("ImageTitleSubtitleButton_image_uri"));
        final String string2 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("ImageTitleSubtitleButton_text_title"));
        final String string3 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("ImageTitleSubtitleButton_text_subtitle"));
        obtainStyledAttributes.recycle();
        this.setImageUri(string);
        XLEUtil.updateTextAndVisibilityIfNotNull(this.titleTextView, string2, 0);
        XLEUtil.updateTextAndVisibilityIfNotNull(this.subtitleTextView, string3, 0);
        this.setFocusable(true);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    public void setImageUri(final String s) {
        if (!JavaUtil.isNullOrEmpty(s)) {
            this.iconImageView.setImageURI2(URI.create(s));
        }
    }
}
