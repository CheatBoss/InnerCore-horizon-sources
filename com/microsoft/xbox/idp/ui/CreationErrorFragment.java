package com.microsoft.xbox.idp.ui;

import com.microsoft.xbox.idp.compat.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.microsoft.xboxtcui.*;
import android.text.style.*;
import android.util.*;
import com.microsoft.xbox.idp.model.*;
import android.content.*;

public class CreationErrorFragment extends BaseFragment
{
    public static final String TAG;
    
    static {
        TAG = CreationErrorFragment.class.getSimpleName();
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R$layout.xbid_fragment_error_creation, viewGroup, false);
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        UiUtil.ensureClickableSpanOnUnderlineSpan((TextView)view.findViewById(R$id.xbid_error_message), R$string.xbid_creation_error_android, new ClickableSpan() {
            public void onClick(final View view) {
                Log.d(CreationErrorFragment.TAG, "onClick");
                try {
                    CreationErrorFragment.this.startActivity(new Intent("android.intent.action.VIEW", Const.URL_XBOX_COM));
                }
                catch (ActivityNotFoundException ex) {
                    Log.e(CreationErrorFragment.TAG, ex.getMessage());
                }
            }
        });
    }
}
