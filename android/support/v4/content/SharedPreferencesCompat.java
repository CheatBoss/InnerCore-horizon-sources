package android.support.v4.content;

import android.os.*;
import android.content.*;
import android.support.annotation.*;

public final class SharedPreferencesCompat
{
    private SharedPreferencesCompat() {
    }
    
    public static final class EditorCompat
    {
        private static EditorCompat sInstance;
        private final Helper mHelper;
        
        private EditorCompat() {
            Helper mHelper;
            if (Build$VERSION.SDK_INT >= 9) {
                mHelper = new EditorHelperApi9Impl();
            }
            else {
                mHelper = new EditorHelperBaseImpl();
            }
            this.mHelper = mHelper;
        }
        
        public static EditorCompat getInstance() {
            if (EditorCompat.sInstance == null) {
                EditorCompat.sInstance = new EditorCompat();
            }
            return EditorCompat.sInstance;
        }
        
        public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
            this.mHelper.apply(sharedPreferences$Editor);
        }
        
        private static class EditorHelperApi9Impl implements Helper
        {
            @Override
            public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
                EditorCompatGingerbread.apply(sharedPreferences$Editor);
            }
        }
        
        private static class EditorHelperBaseImpl implements Helper
        {
            @Override
            public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
                sharedPreferences$Editor.commit();
            }
        }
    }
    
    private interface Helper
    {
        void apply(@NonNull final SharedPreferences$Editor p0);
    }
}
