package com.mojang.android;

import android.widget.*;

public class TextViewReader implements StringValue
{
    private TextView _view;
    
    public TextViewReader(final TextView view) {
        this._view = view;
    }
    
    @Override
    public String getStringValue() {
        return this._view.getText().toString();
    }
}
