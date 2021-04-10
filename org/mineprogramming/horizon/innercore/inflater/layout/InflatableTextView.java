package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.view.*;

class InflatableTextView extends InflatableViewBase<TextView>
{
    private TextView textView;
    
    public InflatableTextView(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        this.textView = new TextView(context);
        final String optString = jsonObject.optString("textColor");
        if (!optString.isEmpty()) {
            this.textView.setTextColor(this.getColor(optString));
        }
        final String optString2 = jsonObject.optString("text");
        if (!optString2.isEmpty()) {
            this.textView.setText((CharSequence)this.getText(optString2));
        }
        final String optString3 = jsonObject.optString("textSize");
        if (!optString3.isEmpty()) {
            this.textView.setTextSize(0, (float)this.getSize(optString3));
        }
        final String optString4 = jsonObject.optString("gravity");
        if (!optString4.isEmpty()) {
            this.textView.setGravity(this.getGravity(optString4));
        }
        final String optString5 = jsonObject.optString("drawableLeft");
        if (!optString5.isEmpty()) {
            final Drawable[] compoundDrawables = this.textView.getCompoundDrawables();
            this.textView.setCompoundDrawablesWithIntrinsicBounds(this.getDrawable(optString5), compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        }
        final String optString6 = jsonObject.optString("drawableTop");
        if (!optString6.isEmpty()) {
            final Drawable[] compoundDrawables2 = this.textView.getCompoundDrawables();
            this.textView.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables2[0], this.getDrawable(optString6), compoundDrawables2[2], compoundDrawables2[3]);
        }
        final String optString7 = jsonObject.optString("drawableRight");
        if (!optString7.isEmpty()) {
            final Drawable[] compoundDrawables3 = this.textView.getCompoundDrawables();
            this.textView.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables3[0], compoundDrawables3[1], this.getDrawable(optString7), compoundDrawables3[3]);
        }
        final String optString8 = jsonObject.optString("drawableBottom");
        if (!optString8.isEmpty()) {
            final Drawable[] compoundDrawables4 = this.textView.getCompoundDrawables();
            this.textView.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables4[0], compoundDrawables4[1], compoundDrawables4[2], this.getDrawable(optString8));
        }
        final String optString9 = jsonObject.optString("drawableStart");
        if (!optString9.isEmpty()) {
            final Drawable[] compoundDrawablesRelative = this.textView.getCompoundDrawablesRelative();
            this.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getDrawable(optString9), compoundDrawablesRelative[1], compoundDrawablesRelative[2], compoundDrawablesRelative[3]);
        }
        final String optString10 = jsonObject.optString("drawableEnd");
        if (!optString10.isEmpty()) {
            final Drawable[] compoundDrawablesRelative2 = this.textView.getCompoundDrawablesRelative();
            this.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative2[0], compoundDrawablesRelative2[1], this.getDrawable(optString10), compoundDrawablesRelative2[3]);
        }
        final String optString11 = jsonObject.optString("drawablePadding");
        if (!optString11.isEmpty()) {
            this.textView.setCompoundDrawablePadding(this.getSize(optString11));
        }
        final String optString12 = jsonObject.optString("textStyle");
        if (!optString12.isEmpty()) {
            final String[] split = optString12.split("\\|");
            final int length = split.length;
            int n = 0;
            int n2;
            for (int i = 0; i < length; ++i, n = n2) {
                final String trim = split[i].trim();
                if (trim.equals("normal")) {
                    n2 = (n | 0x0);
                }
                else if (trim.equals("bold")) {
                    n2 = (n | 0x1);
                }
                else {
                    n2 = n;
                    if (trim.equals("italic")) {
                        n2 = (n | 0x2);
                    }
                }
            }
            this.textView.setTypeface(this.textView.getTypeface(), n);
        }
        final String optString13 = jsonObject.optString("textAllCaps");
        if (!optString13.isEmpty()) {
            this.textView.setAllCaps(this.getBool(optString13));
        }
        this.applyProperties(this.textView);
    }
    
    @Override
    public TextView getView() {
        return this.textView;
    }
}
