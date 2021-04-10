package com.googleplay.licensing;

import android.content.*;
import android.util.*;

public class PreferenceObfuscator
{
    private static final String TAG = "PreferenceObfuscator";
    private SharedPreferences$Editor mEditor;
    private final Obfuscator mObfuscator;
    private final SharedPreferences mPreferences;
    
    public PreferenceObfuscator(final SharedPreferences mPreferences, final Obfuscator mObfuscator) {
        this.mPreferences = mPreferences;
        this.mObfuscator = mObfuscator;
        this.mEditor = null;
    }
    
    public void commit() {
        final SharedPreferences$Editor mEditor = this.mEditor;
        if (mEditor != null) {
            mEditor.commit();
            this.mEditor = null;
        }
    }
    
    public String getString(final String s, final String s2) {
        final String string = this.mPreferences.getString(s, (String)null);
        if (string != null) {
            try {
                return this.mObfuscator.unobfuscate(string, s);
            }
            catch (ValidationException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Validation error while reading preference: ");
                sb.append(s);
                Log.w("PreferenceObfuscator", sb.toString());
            }
        }
        return s2;
    }
    
    public void putString(final String s, String obfuscate) {
        if (this.mEditor == null) {
            this.mEditor = this.mPreferences.edit();
        }
        obfuscate = this.mObfuscator.obfuscate(obfuscate, s);
        this.mEditor.putString(s, obfuscate);
    }
}
