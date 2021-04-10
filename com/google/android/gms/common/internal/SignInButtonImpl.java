package com.google.android.gms.common.internal;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.support.v4.graphics.drawable.*;
import android.graphics.*;
import android.content.res.*;
import com.google.android.gms.base.*;
import android.text.method.*;
import com.google.android.gms.common.util.*;
import android.graphics.drawable.*;

public final class SignInButtonImpl extends Button
{
    public SignInButtonImpl(final Context context) {
        this(context, null);
    }
    
    public SignInButtonImpl(final Context context, final AttributeSet set) {
        super(context, set, 16842824);
    }
    
    private static int zza(final int n, final int n2, final int n3, final int n4) {
        if (n == 0) {
            return n2;
        }
        if (n == 1) {
            return n3;
        }
        if (n == 2) {
            return n4;
        }
        final StringBuilder sb = new StringBuilder(33);
        sb.append("Unknown color scheme: ");
        sb.append(n);
        throw new IllegalStateException(sb.toString());
    }
    
    public final void configure(final Resources resources, int n, final int n2) {
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setTextSize(14.0f);
        final int n3 = (int)(resources.getDisplayMetrics().density * 48.0f + 0.5f);
        this.setMinHeight(n3);
        this.setMinWidth(n3);
        int zza = zza(n2, R$drawable.common_google_signin_btn_icon_dark, R$drawable.common_google_signin_btn_icon_light, R$drawable.common_google_signin_btn_icon_light);
        final int zza2 = zza(n2, R$drawable.common_google_signin_btn_text_dark, R$drawable.common_google_signin_btn_text_light, R$drawable.common_google_signin_btn_text_light);
        if (n != 0 && n != 1) {
            if (n != 2) {
                final StringBuilder sb = new StringBuilder(32);
                sb.append("Unknown button size: ");
                sb.append(n);
                throw new IllegalStateException(sb.toString());
            }
        }
        else {
            zza = zza2;
        }
        final Drawable wrap = DrawableCompat.wrap(resources.getDrawable(zza));
        DrawableCompat.setTintList(wrap, resources.getColorStateList(R$color.common_google_signin_btn_tint));
        DrawableCompat.setTintMode(wrap, PorterDuff$Mode.SRC_ATOP);
        this.setBackgroundDrawable(wrap);
        this.setTextColor((ColorStateList)Preconditions.checkNotNull(resources.getColorStateList(zza(n2, R$color.common_google_signin_btn_text_dark, R$color.common_google_signin_btn_text_light, R$color.common_google_signin_btn_text_light))));
        Label_0268: {
            if (n != 0) {
                if (n != 1) {
                    if (n == 2) {
                        this.setText((CharSequence)null);
                        break Label_0268;
                    }
                    final StringBuilder sb2 = new StringBuilder(32);
                    sb2.append("Unknown button size: ");
                    sb2.append(n);
                    throw new IllegalStateException(sb2.toString());
                }
                else {
                    n = R$string.common_signin_button_text_long;
                }
            }
            else {
                n = R$string.common_signin_button_text;
            }
            this.setText((CharSequence)resources.getString(n));
        }
        this.setTransformationMethod((TransformationMethod)null);
        if (DeviceProperties.isWearable(this.getContext())) {
            this.setGravity(19);
        }
    }
}
