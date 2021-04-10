package com.microsoft.xbox.idp.ui;

import com.microsoft.xbox.idp.compat.*;
import android.os.*;
import android.view.*;
import com.microsoft.xboxtcui.*;

public class OfflineErrorFragment extends BaseFragment
{
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R$layout.xbid_fragment_error_offline, viewGroup, false);
    }
}
