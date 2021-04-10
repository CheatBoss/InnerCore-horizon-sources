package com.mojang.minecraftpe;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import java.util.*;
import android.text.*;
import android.view.inputmethod.*;

public class TextInputProxyEditTextbox extends EditText
{
    private MCPEKeyWatcher _mcpeKeyWatcher;
    public int allowedLength;
    private String mLastSentText;
    
    public TextInputProxyEditTextbox(final Context context) {
        super(context);
        this._mcpeKeyWatcher = null;
    }
    
    public TextInputProxyEditTextbox(final Context context, final AttributeSet set) {
        super(context, set);
        this._mcpeKeyWatcher = null;
        this.allowedLength = 160;
    }
    
    public TextInputProxyEditTextbox(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this._mcpeKeyWatcher = null;
        this.allowedLength = 160;
    }
    
    private InputFilter createSingleLineFilter() {
        return (InputFilter)new InputFilter() {
            public CharSequence filter(final CharSequence charSequence, int i, final int n, final Spanned spanned, final int n2, final int n3) {
                while (i < n) {
                    if (charSequence.charAt(i) == '\n') {
                        return spanned.subSequence(n2, n3);
                    }
                    ++i;
                }
                return null;
            }
        };
    }
    
    private InputFilter createUnicodeFilter() {
        return (InputFilter)new InputFilter() {
            public CharSequence filter(final CharSequence charSequence, final int n, final int n2, final Spanned spanned, int i, final int n3) {
                i = n;
                StringBuilder sb = null;
                while (i < n2) {
                    StringBuilder sb2 = sb;
                    if (charSequence.charAt(i) == '\u3000') {
                        if ((sb2 = sb) == null) {
                            sb2 = new StringBuilder(charSequence);
                        }
                        sb2.setCharAt(i, ' ');
                    }
                    ++i;
                    sb = sb2;
                }
                if (sb != null) {
                    return sb.subSequence(n, n2);
                }
                return null;
            }
        };
    }
    
    public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
        return (InputConnection)new MCPEInputConnection(super.onCreateInputConnection(editorInfo), true, this);
    }
    
    public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
        if (n == 4 && keyEvent.getAction() == 1) {
            final MCPEKeyWatcher mcpeKeyWatcher = this._mcpeKeyWatcher;
            return mcpeKeyWatcher != null && mcpeKeyWatcher.onBackKeyPressed();
        }
        return super.onKeyPreIme(n, keyEvent);
    }
    
    public void setOnMCPEKeyWatcher(final MCPEKeyWatcher mcpeKeyWatcher) {
        this._mcpeKeyWatcher = mcpeKeyWatcher;
    }
    
    void setTextFromGame(final String text) {
        this.mLastSentText = new String(text);
        this.setText((CharSequence)text);
    }
    
    boolean shouldSendText() {
        return this.mLastSentText == null || !this.getText().toString().equals(this.mLastSentText);
    }
    
    public void updateFilters(final int allowedLength, final boolean b) {
        this.allowedLength = allowedLength;
        final ArrayList<InputFilter$LengthFilter> list = new ArrayList<InputFilter$LengthFilter>();
        if (allowedLength != 0) {
            list.add(new InputFilter$LengthFilter(this.allowedLength));
        }
        if (b) {
            list.add((InputFilter$LengthFilter)this.createSingleLineFilter());
        }
        list.add((InputFilter$LengthFilter)this.createUnicodeFilter());
        this.setFilters((InputFilter[])list.toArray(new InputFilter[list.size()]));
    }
    
    void updateLastSentText() {
        this.mLastSentText = new String(this.getText().toString());
    }
    
    private class MCPEInputConnection extends InputConnectionWrapper
    {
        TextInputProxyEditTextbox textbox;
        
        public MCPEInputConnection(final InputConnection inputConnection, final boolean b, final TextInputProxyEditTextbox textbox) {
            super(inputConnection, b);
            this.textbox = textbox;
        }
        
        public boolean sendKeyEvent(final KeyEvent keyEvent) {
            if (this.textbox.getText().length() == 0 && keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 67) {
                if (TextInputProxyEditTextbox.this._mcpeKeyWatcher != null) {
                    TextInputProxyEditTextbox.this._mcpeKeyWatcher.onDeleteKeyPressed();
                }
                return false;
            }
            return super.sendKeyEvent(keyEvent);
        }
    }
    
    public interface MCPEKeyWatcher
    {
        boolean onBackKeyPressed();
        
        void onDeleteKeyPressed();
    }
}
