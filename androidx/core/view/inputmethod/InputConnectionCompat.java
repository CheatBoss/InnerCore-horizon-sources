package androidx.core.view.inputmethod;

import androidx.annotation.*;
import android.content.*;
import android.view.inputmethod.*;
import android.text.*;
import android.os.*;
import android.net.*;

public final class InputConnectionCompat
{
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    
    @Deprecated
    public InputConnectionCompat() {
    }
    
    public static boolean commitContent(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final InputContentInfoCompat inputContentInfoCompat, final int n, @Nullable final Bundle bundle) {
        final ClipDescription description = inputContentInfoCompat.getDescription();
        final boolean b = false;
        final String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        final int length = contentMimeTypes.length;
        int n2 = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n2 >= length) {
                break;
            }
            if (description.hasMimeType(contentMimeTypes[n2])) {
                b2 = true;
                break;
            }
            ++n2;
        }
        if (!b2) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
        }
        boolean b3 = false;
        switch (EditorInfoCompat.getProtocol(editorInfo)) {
            default: {
                return false;
            }
            case 3:
            case 4: {
                b3 = false;
                break;
            }
            case 2: {
                b3 = true;
                break;
            }
        }
        final Bundle bundle2 = new Bundle();
        String s;
        if (b3) {
            s = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
        }
        else {
            s = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
        }
        bundle2.putParcelable(s, (Parcelable)inputContentInfoCompat.getContentUri());
        String s2;
        if (b3) {
            s2 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
        }
        else {
            s2 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
        }
        bundle2.putParcelable(s2, (Parcelable)inputContentInfoCompat.getDescription());
        String s3;
        if (b3) {
            s3 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
        }
        else {
            s3 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
        }
        bundle2.putParcelable(s3, (Parcelable)inputContentInfoCompat.getLinkUri());
        String s4;
        if (b3) {
            s4 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
        }
        else {
            s4 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
        }
        bundle2.putInt(s4, n);
        String s5;
        if (b3) {
            s5 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
        }
        else {
            s5 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
        }
        bundle2.putParcelable(s5, (Parcelable)bundle);
        String s6;
        if (b3) {
            s6 = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
        }
        else {
            s6 = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
        }
        return inputConnection.performPrivateCommand(s6, bundle2);
    }
    
    @NonNull
    public static InputConnection createWrapper(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        }
        if (Build$VERSION.SDK_INT >= 25) {
            return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
                public boolean commitContent(final InputContentInfo inputContentInfo, final int n, final Bundle bundle) {
                    return onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), n, bundle) || super.commitContent(inputContentInfo, n, bundle);
                }
            };
        }
        if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        }
        return (InputConnection)new InputConnectionWrapper(inputConnection, false) {
            public boolean performPrivateCommand(final String s, final Bundle bundle) {
                return InputConnectionCompat.handlePerformPrivateCommand(s, bundle, onCommitContentListener) || super.performPrivateCommand(s, bundle);
            }
        };
    }
    
    static boolean handlePerformPrivateCommand(@Nullable String s, @NonNull Bundle o, @NonNull final OnCommitContentListener onCommitContentListener) {
        final int n = 0;
        final int n2 = 0;
        if (o == null) {
            return false;
        }
        while (true) {
            int n3 = 0;
            Label_0023: {
                if (TextUtils.equals((CharSequence)"androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)s)) {
                    n3 = 0;
                    break Label_0023;
                }
                if (TextUtils.equals((CharSequence)"android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)s)) {
                    n3 = 1;
                    break Label_0023;
                }
                return false;
                final String s2;
                Object o2;
                String s3;
                Uri uri;
                String s4;
                ClipDescription clipDescription;
                String s5;
                Uri uri2;
                String s6;
                int int1;
                String s7;
                final boolean b;
                boolean onCommitContent;
                final Object o3;
                Label_0103_Outer:Label_0128_Outer:Label_0153_Outer:Label_0175_Outer:
                while (true) {
                    while (true) {
                    Label_0321:
                        while (true) {
                        Label_0314:
                            while (true) {
                            Label_0307:
                                while (true) {
                                Label_0300:
                                    while (true) {
                                        Label_0293: {
                                            while (true) {
                                                try {
                                                    o2 = ((Bundle)o).getParcelable(s2);
                                                    if (n3 == 0) {
                                                        break Label_0293;
                                                    }
                                                    s3 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
                                                    s = (String)o2;
                                                    uri = (Uri)((Bundle)o).getParcelable(s3);
                                                    if (n3 == 0) {
                                                        break Label_0300;
                                                    }
                                                    s4 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
                                                    s = (String)o2;
                                                    clipDescription = (ClipDescription)((Bundle)o).getParcelable(s4);
                                                    if (n3 == 0) {
                                                        break Label_0307;
                                                    }
                                                    s5 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
                                                    s = (String)o2;
                                                    uri2 = (Uri)((Bundle)o).getParcelable(s5);
                                                    if (n3 == 0) {
                                                        break Label_0314;
                                                    }
                                                    s6 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
                                                    s = (String)o2;
                                                    int1 = ((Bundle)o).getInt(s6);
                                                    if (n3 != 0) {
                                                        s7 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
                                                        s = (String)o2;
                                                        o = ((Bundle)o).getParcelable(s7);
                                                        onCommitContent = b;
                                                        if (uri != null) {
                                                            onCommitContent = b;
                                                            if (clipDescription != null) {
                                                                s = (String)o2;
                                                                onCommitContent = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), int1, (Bundle)o);
                                                            }
                                                        }
                                                        if (o2 != null) {
                                                            n3 = n2;
                                                            if (onCommitContent) {
                                                                n3 = 1;
                                                            }
                                                            ((ResultReceiver)o2).send(n3, (Bundle)null);
                                                        }
                                                        return onCommitContent;
                                                    }
                                                    break Label_0321;
                                                    // iftrue(Label_0278:, s == null)
                                                Block_16_Outer:
                                                    while (true) {
                                                        break Label_0278;
                                                    Label_0272:
                                                        while (true) {
                                                            n3 = 1;
                                                            break Label_0272;
                                                            n3 = n;
                                                            continue;
                                                        }
                                                        ((ResultReceiver)s).send(n3, (Bundle)null);
                                                        continue Block_16_Outer;
                                                    }
                                                }
                                                // iftrue(Label_0272:, !b)
                                                finally {}
                                                o = o3;
                                                continue;
                                            }
                                        }
                                        s3 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
                                        continue Label_0103_Outer;
                                    }
                                    s4 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
                                    continue Label_0128_Outer;
                                }
                                s5 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
                                continue Label_0153_Outer;
                            }
                            s6 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
                            continue Label_0175_Outer;
                        }
                        s7 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
                        continue;
                    }
                }
            }
            s = null;
            final boolean b = false;
            if (n3 != 0) {
                final String s2 = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
                continue;
            }
            final String s2 = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
            continue;
        }
    }
    
    public interface OnCommitContentListener
    {
        boolean onCommitContent(final InputContentInfoCompat p0, final int p1, final Bundle p2);
    }
}
