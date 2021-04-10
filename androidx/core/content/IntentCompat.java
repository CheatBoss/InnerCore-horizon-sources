package androidx.core.content;

import androidx.annotation.*;
import android.content.*;
import android.os.*;

public final class IntentCompat
{
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
    
    private IntentCompat() {
    }
    
    @NonNull
    public static Intent makeMainSelectorActivity(@NonNull final String s, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 15) {
            return Intent.makeMainSelectorActivity(s, s2);
        }
        final Intent intent = new Intent(s);
        intent.addCategory(s2);
        return intent;
    }
}
