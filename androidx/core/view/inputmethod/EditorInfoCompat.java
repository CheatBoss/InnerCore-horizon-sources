package androidx.core.view.inputmethod;

import android.view.inputmethod.*;
import androidx.annotation.*;
import android.os.*;

public final class EditorInfoCompat
{
    private static final String CONTENT_MIME_TYPES_INTEROP_KEY = "android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String CONTENT_MIME_TYPES_KEY = "androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES";
    private static final String[] EMPTY_STRING_ARRAY;
    public static final int IME_FLAG_FORCE_ASCII = Integer.MIN_VALUE;
    public static final int IME_FLAG_NO_PERSONALIZED_LEARNING = 16777216;
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
    
    @Deprecated
    public EditorInfoCompat() {
    }
    
    @NonNull
    public static String[] getContentMimeTypes(final EditorInfo editorInfo) {
        if (Build$VERSION.SDK_INT >= 25) {
            final String[] contentMimeTypes = editorInfo.contentMimeTypes;
            if (contentMimeTypes != null) {
                return contentMimeTypes;
            }
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
        else {
            if (editorInfo.extras == null) {
                return EditorInfoCompat.EMPTY_STRING_ARRAY;
            }
            String[] array;
            if ((array = editorInfo.extras.getStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES")) == null) {
                array = editorInfo.extras.getStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
            }
            if (array != null) {
                return array;
            }
            return EditorInfoCompat.EMPTY_STRING_ARRAY;
        }
    }
    
    static int getProtocol(final EditorInfo editorInfo) {
        if (Build$VERSION.SDK_INT >= 25) {
            return 1;
        }
        if (editorInfo.extras == null) {
            return 0;
        }
        final boolean containsKey = editorInfo.extras.containsKey("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        final boolean containsKey2 = editorInfo.extras.containsKey("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES");
        if (containsKey && containsKey2) {
            return 4;
        }
        if (containsKey) {
            return 3;
        }
        if (containsKey2) {
            return 2;
        }
        return 0;
    }
    
    public static void setContentMimeTypes(@NonNull final EditorInfo editorInfo, @Nullable final String[] contentMimeTypes) {
        if (Build$VERSION.SDK_INT >= 25) {
            editorInfo.contentMimeTypes = contentMimeTypes;
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        editorInfo.extras.putStringArray("androidx.core.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
        editorInfo.extras.putStringArray("android.support.v13.view.inputmethod.EditorInfoCompat.CONTENT_MIME_TYPES", contentMimeTypes);
    }
}
