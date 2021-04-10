package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.ui.util.*;
import android.view.*;
import android.widget.*;
import android.content.res.*;

public class XLECheckBox extends ViewGroup
{
    private final CheckBox checkBox;
    private final TextView subText;
    private final TextView text;
    
    public XLECheckBox(final Context context) {
        super(context);
        this.checkBox = new CheckBox(context);
        this.text = new TextView(context);
        this.subText = new TextView(context);
    }
    
    public XLECheckBox(final Context context, final AttributeSet set) {
        super(context, set);
        this.checkBox = new CheckBox(context, set);
        this.text = new TextView(context, set);
        this.subText = new TextView(context, set);
        this.initialize(context, set);
    }
    
    public XLECheckBox(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.checkBox = new CheckBox(context, set);
        this.text = new TextView(context, set);
        this.subText = new TextView(context, set);
        this.initialize(context, set);
    }
    
    private void initialize(final Context context, AttributeSet obtainStyledAttributes) {
        this.checkBox.setButtonDrawable(R$drawable.apptheme_btn_check_holo_light);
        this.addView((View)this.checkBox, new ViewGroup$LayoutParams(-2, -2));
        this.text.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                XLECheckBox.this.checkBox.toggle();
            }
        });
        this.addView((View)this.text, new ViewGroup$LayoutParams(-2, -2));
        this.addView((View)this.subText, new ViewGroup$LayoutParams(-2, -2));
        obtainStyledAttributes = (AttributeSet)context.obtainStyledAttributes(obtainStyledAttributes, R$styleable.XLECheckBox);
        try {
            if (!this.isInEditMode()) {
                LibCompat.setTextAppearance(this.text, ((TypedArray)obtainStyledAttributes).getResourceId(R$styleable.XLECheckBox_textStyle, -1));
                this.text.setTypeface(FontManager.Instance().getTypeface(context, ((TypedArray)obtainStyledAttributes).getString(R$styleable.XLECheckBox_textTypefaceSource)));
                LibCompat.setTextAppearance(this.subText, ((TypedArray)obtainStyledAttributes).getResourceId(R$styleable.XLECheckBox_subTextStyle, -1));
                this.subText.setTypeface(FontManager.Instance().getTypeface(context, ((TypedArray)obtainStyledAttributes).getString(R$styleable.XLECheckBox_subTextTypefaceSource)));
            }
            this.text.setText((CharSequence)((TypedArray)obtainStyledAttributes).getString(R$styleable.XLECheckBox_text));
            this.subText.setText((CharSequence)((TypedArray)obtainStyledAttributes).getString(R$styleable.XLECheckBox_subText));
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public CharSequence getSubText() {
        return this.subText.getText();
    }
    
    public CharSequence getText() {
        return this.text.getText();
    }
    
    public boolean isChecked() {
        return this.checkBox.isChecked();
    }
    
    protected void onLayout(final boolean b, int n, int paddingLeft, int n2, final int n3) {
        paddingLeft = this.getPaddingLeft();
        n = this.getPaddingTop() + Math.max(this.checkBox.getMeasuredHeight() / 2, this.text.getMeasuredHeight() / 2);
        n2 = n - this.checkBox.getMeasuredWidth() / 2;
        final CheckBox checkBox = this.checkBox;
        checkBox.layout(paddingLeft, n2, checkBox.getMeasuredWidth() + paddingLeft, this.checkBox.getMeasuredHeight() + n2);
        paddingLeft += this.checkBox.getMeasuredWidth();
        n -= this.text.getMeasuredHeight() / 2;
        final TextView text = this.text;
        text.layout(paddingLeft, n, text.getMeasuredWidth() + paddingLeft, this.text.getMeasuredHeight() + n);
        n += this.text.getMeasuredHeight();
        final TextView subText = this.subText;
        subText.layout(paddingLeft, n, subText.getMeasuredWidth() + paddingLeft, this.subText.getMeasuredHeight() + n);
    }
    
    protected void onMeasure(int n, int n2) {
        final int size = View$MeasureSpec.getSize(n);
        final int mode = View$MeasureSpec.getMode(n);
        final int n3 = Integer.MIN_VALUE;
        if (mode == 0) {
            n = 0;
        }
        else {
            n = Integer.MIN_VALUE;
        }
        final int size2 = View$MeasureSpec.getSize(n2);
        final int mode2 = View$MeasureSpec.getMode(n2);
        n2 = n3;
        if (mode2 == 0) {
            n2 = 0;
        }
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        final int max = Math.max(size - paddingLeft - this.getPaddingRight(), 0);
        final int n4 = size2 - paddingTop;
        this.checkBox.measure(View$MeasureSpec.makeMeasureSpec(max, n), View$MeasureSpec.makeMeasureSpec(Math.max(n4 - this.getPaddingBottom(), 0), n2));
        final int n5 = paddingLeft + this.checkBox.getMeasuredWidth();
        final int n6 = size - n5;
        this.text.measure(View$MeasureSpec.makeMeasureSpec(Math.max(n6 - this.getPaddingRight(), 0), n), View$MeasureSpec.makeMeasureSpec(Math.max(n4 - this.getPaddingBottom(), 0), n2));
        final int n7 = paddingTop + Math.max(this.checkBox.getMeasuredHeight(), this.text.getMeasuredHeight());
        this.subText.measure(View$MeasureSpec.makeMeasureSpec(Math.max(n6 - this.getPaddingRight(), 0), n), View$MeasureSpec.makeMeasureSpec(Math.max(size2 - n7 - this.getPaddingBottom(), 0), n2));
        n = Math.max(this.text.getMeasuredWidth(), this.subText.getMeasuredWidth());
        n2 = this.subText.getMeasuredHeight();
        n = n5 + n + this.getPaddingRight();
        n2 = n7 + n2 + this.getPaddingBottom();
        if (mode != 0) {
            n = Math.min(n, size);
        }
        if (mode2 != 0) {
            n2 = Math.min(n2, size2);
        }
        this.setMeasuredDimension(n, n2);
    }
    
    public void setChecked(final boolean checked) {
        this.checkBox.setChecked(checked);
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        this.checkBox.setEnabled(b);
        this.text.setEnabled(b);
        this.subText.setEnabled(b);
    }
    
    public void setOnCheckedChangeListener(final CompoundButton$OnCheckedChangeListener onCheckedChangeListener) {
        this.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }
    
    public void setSubText(final CharSequence text) {
        this.subText.setText(text);
    }
    
    public void setText(final CharSequence text) {
        this.text.setText(text);
    }
    
    public void toggle() {
        this.checkBox.toggle();
    }
}
