package androidx.core.content;

import android.content.*;
import androidx.annotation.*;

@Deprecated
public final class SharedPreferencesCompat
{
    private SharedPreferencesCompat() {
    }
    
    @Deprecated
    public static final class EditorCompat
    {
        private static EditorCompat sInstance;
        private final Helper mHelper;
        
        private EditorCompat() {
            this.mHelper = new Helper();
        }
        
        @Deprecated
        public static EditorCompat getInstance() {
            if (EditorCompat.sInstance == null) {
                EditorCompat.sInstance = new EditorCompat();
            }
            return EditorCompat.sInstance;
        }
        
        @Deprecated
        public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
            this.mHelper.apply(sharedPreferences$Editor);
        }
        
        private static class Helper
        {
            Helper() {
            }
            
            public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
                try {
                    sharedPreferences$Editor.apply();
                }
                catch (AbstractMethodError abstractMethodError) {
                    sharedPreferences$Editor.commit();
                }
            }
        }
    }
}
