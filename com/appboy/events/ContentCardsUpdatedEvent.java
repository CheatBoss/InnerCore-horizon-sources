package com.appboy.events;

import com.appboy.models.cards.*;
import java.util.*;

public class ContentCardsUpdatedEvent
{
    private final List<Card> a;
    private final String b;
    private final long c;
    private final boolean d;
    
    public ContentCardsUpdatedEvent(final List<Card> a, final String b, final long c, final boolean d) {
        this.b = b;
        this.a = a;
        this.c = c;
        this.d = d;
    }
    
    public List<Card> getAllCards() {
        return this.a;
    }
    
    public int getCardCount() {
        return this.a.size();
    }
    
    public long getLastUpdatedInSecondsFromEpoch() {
        return this.c;
    }
    
    public int getUnviewedCardCount() {
        final Iterator<Card> iterator = this.a.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (!iterator.next().getViewed()) {
                ++n;
            }
        }
        return n;
    }
    
    public String getUserId() {
        return this.b;
    }
    
    public boolean isEmpty() {
        return this.a.isEmpty();
    }
    
    public boolean isFromOfflineStorage() {
        return this.d;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ContentCardsUpdatedEvent{mContentCards=");
        sb.append(this.a);
        sb.append(", mUserId='");
        sb.append(this.b);
        sb.append('\'');
        sb.append(", mTimestamp=");
        sb.append(this.c);
        sb.append('}');
        return sb.toString();
    }
}
