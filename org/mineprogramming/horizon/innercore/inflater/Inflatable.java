package org.mineprogramming.horizon.innercore.inflater;

import android.content.*;
import org.json.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.drawable.*;
import android.*;
import org.mineprogramming.horizon.innercore.util.*;

public class Inflatable
{
    protected Context context;
    protected JSONObject object;
    
    protected Inflatable(final Context context, final JSONObject object) {
        this.context = context;
        this.object = object;
    }
    
    protected boolean api(final int n) {
        return Build$VERSION.SDK_INT >= n;
    }
    
    protected boolean getBool(final String s) throws JsonInflaterException {
        if (s.equals("true")) {
            return true;
        }
        if (s.equals("false")) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid bool: ");
        sb.append(s);
        throw new JsonInflaterException(sb.toString());
    }
    
    protected int getColor(final String s) {
        return Color.parseColor(s);
    }
    
    protected Drawable getDrawable(String s) throws JsonInflaterException {
        if (s.startsWith("@drawable/")) {
            s = s.substring(10);
            return DrawableInflater.getDrawable(this.context, s);
        }
        if (s.startsWith("@android:drawable/")) {
            s = s.substring(18);
            try {
                return this.context.getDrawable((int)R$drawable.class.getDeclaredField(s).get(null));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    protected float getFloat(final String s) {
        return Float.parseFloat(s);
    }
    
    protected int getInt(final String s) {
        return Integer.parseInt(s);
    }
    
    protected int getSize(final String s) throws JsonInflaterException {
        if (s.matches("^\\d+\\.?\\d*dp$")) {
            return DencityConverter.dp(Float.parseFloat(s.substring(0, s.length() - 2)));
        }
        if (s.matches("^\\d+\\.?\\d*sp$")) {
            return DencityConverter.sp(Float.parseFloat(s.substring(0, s.length() - 2)));
        }
        if (s.matches("^\\d+\\.?\\d*px$")) {
            return (int)Float.parseFloat(s.substring(0, s.length() - 2));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid dimension: ");
        sb.append(s);
        throw new JsonInflaterException(sb.toString());
    }
}
