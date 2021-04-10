package android.support.v4.view;

import android.widget.*;
import android.text.method.*;
import java.util.*;
import android.content.*;
import android.view.*;

class PagerTitleStripIcs
{
    public static void setSingleLineAllCaps(final TextView textView) {
        textView.setTransformationMethod((TransformationMethod)new SingleLineAllCapsTransform(textView.getContext()));
    }
    
    private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod
    {
        private static final String TAG = "SingleLineAllCapsTransform";
        private Locale mLocale;
        
        public SingleLineAllCapsTransform(final Context context) {
            this.mLocale = context.getResources().getConfiguration().locale;
        }
        
        public CharSequence getTransformation(CharSequence transformation, final View view) {
            transformation = super.getTransformation(transformation, view);
            if (transformation != null) {
                return transformation.toString().toUpperCase(this.mLocale);
            }
            return null;
        }
    }
}
