package com.google.android.gms.common.internal;

import android.content.res.*;
import android.content.*;
import com.google.android.gms.common.*;
import javax.annotation.*;

public class StringResourceValueReader
{
    private final Resources zzvb;
    private final String zzvc;
    
    public StringResourceValueReader(final Context context) {
        Preconditions.checkNotNull(context);
        final Resources resources = context.getResources();
        this.zzvb = resources;
        this.zzvc = resources.getResourcePackageName(R$string.common_google_play_services_unknown_issue);
    }
    
    @Nullable
    public String getString(final String s) {
        final int identifier = this.zzvb.getIdentifier(s, "string", this.zzvc);
        if (identifier == 0) {
            return null;
        }
        return this.zzvb.getString(identifier);
    }
}
