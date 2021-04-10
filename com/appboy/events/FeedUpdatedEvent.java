package com.appboy.events;

import com.appboy.models.cards.*;
import com.appboy.support.*;
import com.appboy.enums.*;
import java.util.*;

public final class FeedUpdatedEvent
{
    private static final String a;
    private final List<Card> b;
    private final String c;
    private final boolean d;
    private final long e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(FeedUpdatedEvent.class);
    }
    
    public FeedUpdatedEvent(final List<Card> b, final String c, final boolean d, final long e) {
        this.c = c;
        this.d = d;
        if (b != null) {
            this.b = b;
            this.e = e;
            return;
        }
        throw null;
    }
    
    public int getCardCount() {
        return this.getCardCount(CardCategory.getAllCategories());
    }
    
    public int getCardCount(final CardCategory cardCategory) {
        return this.getCardCount(EnumSet.of(cardCategory));
    }
    
    public int getCardCount(final EnumSet<CardCategory> set) {
        if (set == null) {
            AppboyLogger.i(FeedUpdatedEvent.a, "The categories passed into getCardCount are null, FeedUpdatedEvent is going to return the count of all the cards in cache.");
            return this.b.size();
        }
        if (set.isEmpty()) {
            AppboyLogger.w(FeedUpdatedEvent.a, "The parameters passed into categories are not valid, Braze is returning 0 in getCardCount().Please pass in a non-empty EnumSet of CardCategory.");
            return 0;
        }
        return this.getFeedCards(set).size();
    }
    
    public List<Card> getFeedCards() {
        return this.getFeedCards(CardCategory.getAllCategories());
    }
    
    public List<Card> getFeedCards(final CardCategory cardCategory) {
        return this.getFeedCards(EnumSet.of(cardCategory));
    }
    
    public List<Card> getFeedCards(final EnumSet<CardCategory> set) {
        EnumSet<CardCategory> allCategories = set;
        Label_0026: {
            if (set != null) {
                break Label_0026;
            }
            EnumSet<CardCategory> set2 = set;
            while (true) {
                try {
                    AppboyLogger.i(FeedUpdatedEvent.a, "The categories passed to getFeedCards are null, FeedUpdatedEvent is going to return all the cards in cache.");
                    set2 = set;
                    allCategories = CardCategory.getAllCategories();
                    set2 = allCategories;
                    if (allCategories.isEmpty()) {
                        set2 = allCategories;
                        AppboyLogger.w(FeedUpdatedEvent.a, "The parameter passed into categories is not valid, Braze is returning an empty card list.Please pass in a non-empty EnumSet of CardCategory for getFeedCards().");
                        set2 = allCategories;
                        return new ArrayList<Card>();
                    }
                    set2 = allCategories;
                    final ArrayList<Card> list = new ArrayList<Card>();
                    set2 = allCategories;
                    final Iterator<Card> iterator = this.b.iterator();
                    while (true) {
                        set2 = allCategories;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        set2 = allCategories;
                        final Card card = iterator.next();
                        set2 = allCategories;
                        if (!card.isInCategorySet(allCategories)) {
                            continue;
                        }
                        set2 = allCategories;
                        if (card.isExpired()) {
                            continue;
                        }
                        set2 = allCategories;
                        list.add(card);
                    }
                    return list;
                    final String a = FeedUpdatedEvent.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to get cards with categories[");
                    sb.append(set2);
                    sb.append("]. Ignoring.");
                    final Exception ex;
                    AppboyLogger.w(a, sb.toString(), ex);
                    return null;
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    public int getUnreadCardCount() {
        return this.getUnreadCardCount(CardCategory.getAllCategories());
    }
    
    public int getUnreadCardCount(final CardCategory cardCategory) {
        return this.getUnreadCardCount(EnumSet.of(cardCategory));
    }
    
    public int getUnreadCardCount(final EnumSet<CardCategory> set) {
        if (set == null) {
            AppboyLogger.w(FeedUpdatedEvent.a, "The categories passed to getUnreadCardCount are null, FeedUpdatedEvent is going to return the count of all the unread cards in cache.");
            return this.getUnreadCardCount(CardCategory.getAllCategories());
        }
        final boolean empty = set.isEmpty();
        int n = 0;
        if (empty) {
            AppboyLogger.w(FeedUpdatedEvent.a, "The parameters passed into categories are Empty, Braze is returning 0 in getUnreadCardCount().Please pass in a non-empty EnumSet of CardCategory.");
            return 0;
        }
        for (final Card card : this.b) {
            if (card.isInCategorySet(set) && !card.getViewed() && !card.isExpired()) {
                ++n;
            }
        }
        return n;
    }
    
    public String getUserId() {
        return this.c;
    }
    
    public boolean isFromOfflineStorage() {
        return this.d;
    }
    
    public long lastUpdatedInSecondsFromEpoch() {
        return this.e;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FeedUpdatedEvent{");
        sb.append("mFeedCards=");
        sb.append(this.b);
        sb.append(", mUserId='");
        sb.append(this.c);
        sb.append('\'');
        sb.append(", mFromOfflineStorage=");
        sb.append(this.d);
        sb.append(", mTimestamp=");
        sb.append(this.e);
        sb.append('}');
        return sb.toString();
    }
}
