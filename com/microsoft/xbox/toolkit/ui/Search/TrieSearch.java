package com.microsoft.xbox.toolkit.ui.Search;

import com.microsoft.xbox.toolkit.*;
import java.util.*;

public class TrieSearch
{
    private static String ComponentName;
    private static int DefaultTrieDepth;
    public TrieNode RootTrieNode;
    public int TrieDepth;
    public Hashtable<String, List<Object>> WordsDictionary;
    
    static {
        TrieSearch.ComponentName = TrieSearch.class.getName();
        TrieSearch.DefaultTrieDepth = 4;
    }
    
    public TrieSearch() {
        this.WordsDictionary = new Hashtable<String, List<Object>>();
        this.RootTrieNode = new TrieNode();
        this.TrieDepth = TrieSearch.DefaultTrieDepth;
    }
    
    public TrieSearch(final int trieDepth) {
        this.WordsDictionary = new Hashtable<String, List<Object>>();
        this.RootTrieNode = new TrieNode();
        this.TrieDepth = trieDepth;
    }
    
    public static int findWordIndex(final String s, final String s2) {
        int n2;
        if (!JavaUtil.isNullOrEmpty(s) && !JavaUtil.isNullOrEmpty(s2)) {
            int n = s.toLowerCase().indexOf(s2.toLowerCase());
            while (true) {
                n2 = n;
                if (n == -1 || (n2 = n) == 0) {
                    break;
                }
                if (isNullOrWhitespace(s.substring(n - 1, n))) {
                    return n;
                }
                n = s.toLowerCase().indexOf(s2.toLowerCase(), n + 1);
            }
        }
        else {
            n2 = -1;
        }
        return n2;
    }
    
    public static List<String> getRemainingWordMatches(final TrieNode trieNode, final int n, final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        if (trieNode != null && !JavaUtil.isNullOrEmpty(s)) {
            if (trieNode.IsWord && s.length() <= n) {
                list.add(s);
            }
            if (trieNode.MoreNodes != null) {
                final Enumeration<Character> keys = trieNode.MoreNodes.keys();
                while (keys.hasMoreElements()) {
                    final char charValue = keys.nextElement();
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(charValue);
                    list.addAll((Collection<?>)getRemainingWordMatches(trieNode.MoreNodes.get(charValue), n, sb.toString()));
                }
            }
            if (trieNode.Words != null) {
                for (final String s2 : trieNode.Words) {
                    if (s2.toLowerCase().startsWith(s.toLowerCase())) {
                        list.add(s2);
                    }
                }
            }
        }
        return list;
    }
    
    public static TrieNode getTrieNodes(final Hashtable<String, List<Object>> hashtable, final int n) {
        if (hashtable == null) {
            return null;
        }
        final TrieNode trieNode = new TrieNode();
        final Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final String s = keys.nextElement();
            int n2 = 0;
            TrieNode trieNode2 = trieNode;
            while (n2 < s.length() && n2 <= n) {
                final char char1 = s.charAt(n2);
                if (trieNode2.MoreNodes == null) {
                    trieNode2.MoreNodes = new Hashtable<Character, TrieNode>(26);
                }
                if (!trieNode2.MoreNodes.containsKey(char1)) {
                    trieNode2.MoreNodes.put(char1, new TrieNode());
                }
                trieNode2 = trieNode2.MoreNodes.get(char1);
                ++n2;
            }
            if (n2 > n) {
                if (trieNode2.Words == null) {
                    trieNode2.Words = new ArrayList<String>();
                }
                trieNode2.Words.add(s);
            }
            if (n2 == s.length()) {
                trieNode2.IsWord = true;
            }
        }
        return trieNode;
    }
    
    public static List<String> getWordMatches(final TrieNode trieNode, final int n, final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        if (JavaUtil.isNullOrEmpty(s)) {
            return list;
        }
        final String upperCase = s.toUpperCase();
        final String s2 = "";
        final boolean b = false;
        int n2 = 0;
        TrieNode trieNode2 = trieNode;
        String s3 = s2;
        while (true) {
            while (n2 < upperCase.length() && n2 <= n) {
                final char char1 = upperCase.charAt(n2);
                final StringBuilder sb = new StringBuilder();
                sb.append(s3);
                sb.append(char1);
                String string;
                s3 = (string = sb.toString());
                boolean b2 = b;
                if (trieNode2.MoreNodes != null) {
                    if (trieNode2.MoreNodes.containsKey(char1)) {
                        trieNode2 = trieNode2.MoreNodes.get(char1);
                        ++n2;
                        continue;
                    }
                    string = s3;
                    b2 = b;
                }
                if (n2 > n) {
                    if (trieNode2.Words != null) {
                        for (final String s4 : trieNode2.Words) {
                            if (s4.toLowerCase().startsWith(s.toLowerCase())) {
                                list.add(s4);
                            }
                        }
                    }
                }
                else if (b2) {
                    list.addAll((Collection<?>)getRemainingWordMatches(trieNode2, n, string));
                }
                return list;
            }
            boolean b2 = true;
            String string = s3;
            continue;
        }
    }
    
    public static Hashtable<String, List<Object>> getWordsDictionary(final List<TrieInput> list) {
        final Hashtable<String, ArrayList<Object>> hashtable = new Hashtable<String, ArrayList<Object>>();
        if (list == null) {
            return (Hashtable<String, List<Object>>)hashtable;
        }
        for (final TrieInput trieInput : list) {
            final boolean nullOrEmpty = JavaUtil.isNullOrEmpty(trieInput.Text);
            int i = 0;
            String[] split;
            if (nullOrEmpty) {
                split = new String[0];
            }
            else {
                split = trieInput.Text.split(" ");
            }
            while (i < split.length) {
                final int wordIndex = findWordIndex(trieInput.Text, split[i]);
                if (wordIndex != -1) {
                    final String upperCase = trieInput.Text.substring(wordIndex).toUpperCase();
                    if (hashtable.containsKey(upperCase)) {
                        if (!hashtable.get(upperCase).contains(trieInput.Context)) {
                            hashtable.get(upperCase).add(trieInput.Context);
                        }
                    }
                    else {
                        final ArrayList<Object> list2 = new ArrayList<Object>();
                        list2.add(trieInput.Context);
                        hashtable.put(upperCase, list2);
                    }
                }
                ++i;
            }
        }
        return (Hashtable<String, List<Object>>)hashtable;
    }
    
    private static boolean isNullOrWhitespace(final String s) {
        return JavaUtil.isNullOrEmpty(s) || s.trim().isEmpty();
    }
    
    public void initialize(final List<TrieInput> list) {
        final Hashtable<String, List<Object>> wordsDictionary = getWordsDictionary(list);
        this.WordsDictionary = wordsDictionary;
        this.RootTrieNode = getTrieNodes(wordsDictionary, this.TrieDepth);
    }
    
    public List<String> search(final String s) {
        return getWordMatches(this.RootTrieNode, this.TrieDepth, s);
    }
}
