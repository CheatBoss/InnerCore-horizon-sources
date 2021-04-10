package android.support.v4.app;

import android.app.*;
import android.content.*;
import android.util.*;
import android.os.*;
import android.view.*;

abstract class BaseFragmentActivityDonut extends Activity
{
    abstract View dispatchFragmentsOnCreateView(final View p0, final String p1, final Context p2, final AttributeSet p3);
    
    protected void onCreate(final Bundle bundle) {
        if (Build$VERSION.SDK_INT < 11 && this.getLayoutInflater().getFactory() == null) {
            this.getLayoutInflater().setFactory((LayoutInflater$Factory)this);
        }
        super.onCreate(bundle);
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        View view;
        if ((view = this.dispatchFragmentsOnCreateView(null, s, context, set)) == null) {
            view = super.onCreateView(s, context, set);
        }
        return view;
    }
}
