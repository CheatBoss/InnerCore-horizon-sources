package android.support.v4.content;

import android.content.*;
import android.support.annotation.*;

class EditorCompatGingerbread
{
    public static void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
        try {
            sharedPreferences$Editor.apply();
        }
        catch (AbstractMethodError abstractMethodError) {
            sharedPreferences$Editor.commit();
        }
    }
}
