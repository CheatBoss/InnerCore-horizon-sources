package com.google.android.gms.dynamite;

import android.content.*;

final class zzg implements VersionPolicy
{
    @Override
    public final SelectionResult selectModule(final Context context, final String s, final IVersions versions) throws LoadingException {
        final SelectionResult selectionResult = new DynamiteModule.SelectionResult();
        selectionResult.localVersion = versions.getLocalVersion(context, s);
        int remoteVersion;
        if (selectionResult.localVersion != 0) {
            remoteVersion = versions.getRemoteVersion(context, s, false);
        }
        else {
            remoteVersion = versions.getRemoteVersion(context, s, true);
        }
        selectionResult.remoteVersion = remoteVersion;
        if (selectionResult.localVersion == 0 && selectionResult.remoteVersion == 0) {
            selectionResult.selection = 0;
            return selectionResult;
        }
        if (selectionResult.remoteVersion >= selectionResult.localVersion) {
            selectionResult.selection = 1;
            return selectionResult;
        }
        selectionResult.selection = -1;
        return selectionResult;
    }
}
