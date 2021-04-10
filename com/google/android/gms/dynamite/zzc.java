package com.google.android.gms.dynamite;

import android.content.*;

final class zzc implements VersionPolicy
{
    @Override
    public final SelectionResult selectModule(final Context context, final String s, final IVersions versions) throws LoadingException {
        final SelectionResult selectionResult = new DynamiteModule.SelectionResult();
        selectionResult.localVersion = versions.getLocalVersion(context, s);
        if (selectionResult.localVersion != 0) {
            selectionResult.selection = -1;
            return selectionResult;
        }
        selectionResult.remoteVersion = versions.getRemoteVersion(context, s, true);
        if (selectionResult.remoteVersion != 0) {
            selectionResult.selection = 1;
        }
        return selectionResult;
    }
}
