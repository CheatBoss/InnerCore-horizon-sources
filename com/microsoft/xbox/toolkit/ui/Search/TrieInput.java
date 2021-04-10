package com.microsoft.xbox.toolkit.ui.Search;

public class TrieInput
{
    public Object Context;
    public String Text;
    
    public TrieInput(final String text, final Object context) {
        this.Text = text;
        this.Context = context;
    }
}
