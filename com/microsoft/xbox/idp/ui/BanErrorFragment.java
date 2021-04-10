package com.microsoft.xbox.idp.ui;

import com.microsoft.xbox.idp.compat.*;
import android.os.*;
import android.view.*;
import com.microsoft.xboxtcui.*;
import android.util.*;

public class BanErrorFragment extends BaseFragment
{
    public static final String ARG_GAMER_TAG = "ARG_GAMER_TAG";
    private static final String TAG;
    
    static {
        TAG = BanErrorFragment.class.getSimpleName();
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R$layout.xbid_fragment_error_ban, viewGroup, false);
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final Bundle arguments = this.getArguments();
        if (arguments == null) {
            Log.e(BanErrorFragment.TAG, "No arguments provided");
            return;
        }
        if (arguments.containsKey("ARG_GAMER_TAG")) {
            arguments.getString("ARG_GAMER_TAG");
            return;
        }
        Log.e(BanErrorFragment.TAG, "No ARG_GAMER_TAG provided");
    }
}
