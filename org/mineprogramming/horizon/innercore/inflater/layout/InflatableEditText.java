package org.mineprogramming.horizon.innercore.inflater.layout;

import android.widget.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.graphics.drawable.*;
import android.view.*;

class InflatableEditText extends InflatableViewBase<EditText>
{
    private EditText editText;
    
    public InflatableEditText(final Context context, final ViewGroup viewGroup, final InflatedView inflatedView, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, viewGroup, inflatedView, jsonObject);
        (this.editText = new EditText(context)).setLines(1);
        final String optString = jsonObject.optString("inputType");
        if (optString.isEmpty()) {
            throw new JsonInflaterException("You have to specify inputType for the EditText");
        }
        final String[] split = optString.split("\\|");
        final int length = split.length;
        int inputType = 0;
        int n = 0;
        while (true) {
            int n2 = -1;
            if (n >= length) {
                this.editText.setInputType(inputType);
                final String optString2 = jsonObject.optString("imeOptions");
                if (!optString2.isEmpty()) {
                    final String[] split2 = optString2.split("\\|");
                    final int length2 = split2.length;
                    int imeOptions = 0;
                    for (int i = 0; i < length2; ++i) {
                        final String trim = split2[i].trim();
                        int n3 = 0;
                        Label_0242: {
                            if (trim.hashCode() == 1090704168) {
                                if (trim.equals("flagNoExtractUi")) {
                                    n3 = 0;
                                    break Label_0242;
                                }
                            }
                            n3 = -1;
                        }
                        if (n3 != 0) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unsupported EditText imeOptions: ");
                            sb.append(trim);
                            throw new JsonInflaterException(sb.toString());
                        }
                        imeOptions |= 0x10000000;
                    }
                    this.editText.setImeOptions(imeOptions);
                }
                final String optString3 = jsonObject.optString("hint");
                if (!optString3.isEmpty()) {
                    this.editText.setHint((CharSequence)this.getText(optString3));
                }
                final String optString4 = jsonObject.optString("textColorHint");
                if (!optString4.isEmpty()) {
                    this.editText.setHintTextColor(this.getColor(optString4));
                }
                final String optString5 = jsonObject.optString("textColor");
                if (!optString5.isEmpty()) {
                    this.editText.setTextColor(this.getColor(optString5));
                }
                final String optString6 = jsonObject.optString("drawableLeft");
                if (!optString6.isEmpty()) {
                    final Drawable[] compoundDrawables = this.editText.getCompoundDrawables();
                    this.editText.setCompoundDrawablesWithIntrinsicBounds(this.getDrawable(optString6), compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
                }
                final String optString7 = jsonObject.optString("drawableTop");
                if (!optString7.isEmpty()) {
                    final Drawable[] compoundDrawables2 = this.editText.getCompoundDrawables();
                    this.editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables2[0], this.getDrawable(optString7), compoundDrawables2[2], compoundDrawables2[3]);
                }
                final String optString8 = jsonObject.optString("drawableRight");
                if (!optString8.isEmpty()) {
                    final Drawable[] compoundDrawables3 = this.editText.getCompoundDrawables();
                    this.editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables3[0], compoundDrawables3[1], this.getDrawable(optString8), compoundDrawables3[3]);
                }
                final String optString9 = jsonObject.optString("drawableBottom");
                if (!optString9.isEmpty()) {
                    final Drawable[] compoundDrawables4 = this.editText.getCompoundDrawables();
                    this.editText.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables4[0], compoundDrawables4[1], compoundDrawables4[2], this.getDrawable(optString9));
                }
                final String optString10 = jsonObject.optString("drawableStart");
                if (!optString10.isEmpty()) {
                    final Drawable[] compoundDrawablesRelative = this.editText.getCompoundDrawablesRelative();
                    this.editText.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getDrawable(optString10), compoundDrawablesRelative[1], compoundDrawablesRelative[2], compoundDrawablesRelative[3]);
                }
                final String optString11 = jsonObject.optString("drawableEnd");
                if (!optString11.isEmpty()) {
                    final Drawable[] compoundDrawablesRelative2 = this.editText.getCompoundDrawablesRelative();
                    this.editText.setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative2[0], compoundDrawablesRelative2[1], this.getDrawable(optString11), compoundDrawablesRelative2[3]);
                }
                final String optString12 = jsonObject.optString("drawablePadding");
                if (!optString12.isEmpty()) {
                    this.editText.setCompoundDrawablePadding(this.getSize(optString12));
                }
                this.applyProperties(this.editText);
                return;
            }
            final String trim2 = split[n].trim();
            if (trim2.hashCode() == 3556653) {
                if (trim2.equals("text")) {
                    n2 = 0;
                }
            }
            if (n2 != 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unsupported EditText type: ");
                sb2.append(trim2);
                throw new JsonInflaterException(sb2.toString());
            }
            inputType |= 0x1;
            ++n;
        }
    }
    
    @Override
    public EditText getView() {
        return this.editText;
    }
}
