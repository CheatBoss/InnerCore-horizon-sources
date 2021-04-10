package com.google.android.gms.dynamite;

import android.content.*;

final class zzd implements VersionPolicy
{
    @Override
    public final SelectionResult selectModule(final Context context, final String s, final IVersions versions) throws LoadingException {
        final SelectionResult selectionResult = new DynamiteModule.SelectionResult();
        selectionResult.localVersion = versions.getLocalVersion(context, s);
        selectionResult.remoteVersion = versions.getRemoteVersion(context, s, true);
        int selection;
        if (selectionResult.localVersion == 0 && selectionResult.remoteVersion == 0) {
            selection = 0;
        }
        else {
            if (selectionResult.localVersion < selectionResult.remoteVersion) {
                selectionResult.selection = 1;
                return selectionResult;
            }
            selection = -1;
        }
        selectionResult.selection = selection;
        return selectionResult;
    }
}
