package com.microsoft.xbox.toolkit.ui.Search;

import java.util.*;

public class TrieNode
{
    public boolean IsWord;
    public Hashtable<Character, TrieNode> MoreNodes;
    public List<String> Words;
    
    public TrieNode() {
        this.MoreNodes = new Hashtable<Character, TrieNode>(26);
        this.Words = new ArrayList<String>();
    }
    
    public void accept(final ITrieNodeVisitor trieNodeVisitor) {
        if (trieNodeVisitor != null) {
            trieNodeVisitor.visit(this);
        }
        final Hashtable<Character, TrieNode> moreNodes = this.MoreNodes;
        if (moreNodes != null) {
            final Enumeration<Character> keys = moreNodes.keys();
            while (keys.hasMoreElements()) {
                this.MoreNodes.get(keys.nextElement()).accept(trieNodeVisitor);
            }
        }
    }
}
