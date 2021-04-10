package org.mineprogramming.horizon.innercore.view.mod;

import android.content.*;
import android.view.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;

public class ModCategoriesAdapter extends BaseAdapter
{
    private static final String[] categories;
    private final Context context;
    
    static {
        categories = new String[] { "popular", "new", "redaction", "updated" };
    }
    
    public ModCategoriesAdapter(final Context context) {
        this.context = context;
    }
    
    public int getCount() {
        return 4;
    }
    
    public Object getItem(final int n) {
        return ModCategoriesAdapter.categories[n];
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        final String s = (String)this.getItem(n);
        final Context context = this.context;
        final StringBuilder sb = new StringBuilder();
        sb.append("category_");
        sb.append(s);
        final String string = JsonInflater.getString(context, sb.toString());
        TextView textView = null;
        Label_0098: {
            if (view == null) {
                try {
                    textView = (TextView)JsonInflater.inflateLayout(this.context, null, ResourceReader.readLayout(this.context, "category_item")).getView();
                    break Label_0098;
                }
                catch (JSONException | JsonInflaterException ex) {
                    final Object o;
                    throw new RuntimeException((Throwable)o);
                }
            }
            textView = (TextView)view;
        }
        textView.setText((CharSequence)string);
        return (View)textView;
    }
}
