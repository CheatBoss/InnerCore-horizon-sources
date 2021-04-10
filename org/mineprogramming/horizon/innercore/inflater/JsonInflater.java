package org.mineprogramming.horizon.innercore.inflater;

import org.json.*;
import android.content.*;
import android.graphics.drawable.*;
import org.mineprogramming.horizon.innercore.inflater.drawable.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import android.graphics.*;

public class JsonInflater
{
    static IncludeListener includeListener;
    private static JSONObject strings;
    
    public static String formatString(final Context context, final String s, final Object... array) {
        return String.format(getString(context, s), array);
    }
    
    public static IncludeListener getIncludeListener() throws JsonInflaterException {
        if (JsonInflater.includeListener == null) {
            throw new JsonInflaterException("Cannot process include task due to IncludeListener is not set");
        }
        return JsonInflater.includeListener;
    }
    
    public static String getString(final Context context, final String s) {
        try {
            if (JsonInflater.strings == null) {
                JsonInflater.strings = getIncludeListener().getStrings(context);
            }
            return JsonInflater.strings.getString(s);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    public static Drawable inflateDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        return DrawableInflater.inflateDrawable(context, jsonObject);
    }
    
    public static InflatedView inflateLayout(final Context context, final ViewGroup viewGroup, final JSONObject jsonObject) throws JsonInflaterException {
        return LayoutInflater.inflateLayout(context, viewGroup, jsonObject);
    }
    
    public static void setIncludeListener(final IncludeListener includeListener) {
        JsonInflater.includeListener = includeListener;
    }
    
    public interface IncludeListener
    {
        Bitmap getDrawableBitmap(final Context p0, final String p1);
        
        JSONObject getDrawableJson(final Context p0, final String p1);
        
        JSONObject getStrings(final Context p0);
        
        JSONObject includeLayout(final Context p0, final String p1);
    }
}
