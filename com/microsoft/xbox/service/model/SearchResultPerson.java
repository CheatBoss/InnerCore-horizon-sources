package com.microsoft.xbox.service.model;

import java.util.*;
import com.microsoft.xbox.toolkit.ui.Search.*;
import com.microsoft.xbox.toolkit.*;

public class SearchResultPerson
{
    public String GamertagAfter;
    public String GamertagBefore;
    public String GamertagMatch;
    public String RealNameAfter;
    public String RealNameBefore;
    public String RealNameMatch;
    public String SearchText;
    public String StatusAfter;
    public String StatusBefore;
    public String StatusMatch;
    
    public SearchResultPerson(final FollowersData inlineRuns, final String searchText) {
        if (!isNullOrWhitespace(searchText)) {
            this.SearchText = searchText;
            this.setInlineRuns(inlineRuns);
            return;
        }
        throw new IllegalArgumentException(searchText);
    }
    
    private static List<String> getRuns(String substring, final String s) {
        final ArrayList<String> list = new ArrayList<String>(3);
        final int wordIndex = TrieSearch.findWordIndex(substring, s);
        final int length = s.length();
        if (wordIndex != -1) {
            list.add(substring.substring(0, wordIndex));
            list.add(substring.substring(wordIndex, s.length() + wordIndex));
            substring = substring.substring(length + wordIndex, substring.length());
        }
        else {
            list.add(substring);
            list.add("");
            substring = "";
        }
        list.add(substring);
        return list;
    }
    
    private static boolean isNullOrWhitespace(final String s) {
        return JavaUtil.isNullOrEmpty(s) || s.trim().isEmpty();
    }
    
    private void setInlineRuns(final FollowersData followersData) {
        final List<String> runs = getRuns(followersData.getGamertag(), this.SearchText);
        if (runs.size() == 3) {
            this.GamertagBefore = runs.get(0);
            this.GamertagMatch = runs.get(1);
            this.GamertagAfter = runs.get(2);
        }
        final List<String> runs2 = getRuns(followersData.getGamerRealName(), this.SearchText);
        if (runs2.size() == 3) {
            this.RealNameBefore = runs2.get(0);
            this.RealNameMatch = runs2.get(1);
            this.RealNameAfter = runs2.get(2);
        }
        final List<String> runs3 = getRuns(followersData.presenceString, this.SearchText);
        if (runs3.size() == 3) {
            this.StatusBefore = runs3.get(0);
            this.StatusMatch = runs3.get(1);
            this.StatusAfter = runs3.get(2);
        }
    }
}
