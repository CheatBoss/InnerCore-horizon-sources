package android.support.v4.widget;

import android.content.*;
import android.view.*;
import android.widget.*;

class SearchViewCompatIcs
{
    public static View newSearchView(final Context context) {
        return (View)new MySearchView(context);
    }
    
    public static void setImeOptions(final View view, final int imeOptions) {
        ((SearchView)view).setImeOptions(imeOptions);
    }
    
    public static void setInputType(final View view, final int inputType) {
        ((SearchView)view).setInputType(inputType);
    }
    
    public static class MySearchView extends SearchView
    {
        public MySearchView(final Context context) {
            super(context);
        }
        
        public void onActionViewCollapsed() {
            this.setQuery((CharSequence)"", false);
            super.onActionViewCollapsed();
        }
    }
}
