package com.microsoft.xbox.xle.ui;

import android.content.*;
import android.util.*;
import com.microsoft.xbox.toolkit.ui.*;
import android.view.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import android.content.res.*;
import android.view.accessibility.*;
import android.widget.*;

public class IconFontToggleButton extends LinearLayout implements Checkable
{
    private boolean checked;
    private String checkedIcon;
    private String checkedText;
    private TextView iconTextView;
    private TextView labelTextView;
    private String uncheckedIcon;
    private String uncheckedText;
    
    public IconFontToggleButton(final Context context) {
        super(context);
    }
    
    public IconFontToggleButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.initViews(context, set);
    }
    
    public IconFontToggleButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.initViews(context, set);
    }
    
    private void applyCustomTypeface(final Context context, final String s) {
        if (s != null && this.labelTextView != null) {
            this.labelTextView.setTypeface(FontManager.Instance().getTypeface(this.getContext(), s));
        }
    }
    
    private void initViews(final Context context, final AttributeSet set) {
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R$layout.iconfont_toggle_btn_view, (ViewGroup)this, true);
        this.iconTextView = (TextView)this.findViewById(R$id.iconfont_toggle_btn_icon);
        this.labelTextView = (TextView)this.findViewById(R$id.iconfont_toggle_btn_text);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("CustomTypeface"));
        final String string = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("CustomTypeface_typefaceSource"));
        obtainStyledAttributes.recycle();
        final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("IconFontToggleButton"));
        this.checkedText = obtainStyledAttributes2.getString(XLERValueHelper.getStyleableRValue("IconFontToggleButton_text_checked"));
        this.uncheckedText = obtainStyledAttributes2.getString(XLERValueHelper.getStyleableRValue("IconFontToggleButton_text_unchecked"));
        this.checkedIcon = obtainStyledAttributes2.getString(XLERValueHelper.getStyleableRValue("IconFontToggleButton_icon_checked"));
        this.uncheckedIcon = obtainStyledAttributes2.getString(XLERValueHelper.getStyleableRValue("IconFontToggleButton_icon_unchecked"));
        final float n = (float)obtainStyledAttributes2.getDimensionPixelSize(XLERValueHelper.getStyleableRValue("IconFontToggleButton_icon_size"), -1);
        if (n != -1.0f) {
            this.iconTextView.setTextSize(0, n);
        }
        obtainStyledAttributes2.recycle();
        this.applyCustomTypeface(context, string);
        this.setFocusable(true);
    }
    
    public boolean isChecked() {
        return this.checked;
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    public void setChecked(final boolean checked) {
        this.checked = checked;
        this.sendAccessibilityEvent(1);
        final TextView labelTextView = this.labelTextView;
        if (labelTextView != null) {
            String text;
            if (this.checked) {
                text = this.checkedText;
            }
            else {
                text = this.uncheckedText;
            }
            labelTextView.setText((CharSequence)text);
            this.labelTextView.setVisibility(0);
        }
        final TextView iconTextView = this.iconTextView;
        if (iconTextView != null) {
            String text2;
            if (this.checked) {
                text2 = this.checkedIcon;
            }
            else {
                text2 = this.uncheckedIcon;
            }
            iconTextView.setText((CharSequence)text2);
            this.iconTextView.setVisibility(0);
        }
        this.invalidate();
    }
    
    public void setCheckedText(final String checkedText) {
        this.checkedText = checkedText;
    }
    
    public void setUncheckedText(final String uncheckedText) {
        this.uncheckedText = uncheckedText;
    }
    
    public void toggle() {
        this.setChecked(this.checked ^ true);
    }
}
