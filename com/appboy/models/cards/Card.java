package com.appboy.models.cards;

import com.appboy.models.*;
import org.json.*;
import com.appboy.support.*;
import com.appboy.enums.*;
import java.util.*;
import bo.app.*;

public class Card extends Observable implements IPutIntoJson<JSONObject>
{
    private static final String a;
    private final JSONObject b;
    private final Map<String, String> c;
    private final String d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean h;
    private boolean i;
    private final long j;
    private final long k;
    private final long l;
    private boolean m;
    private final EnumSet<CardCategory> n;
    private boolean o;
    private final br p;
    private final dn q;
    private final c r;
    
    static {
        a = AppboyLogger.getAppboyLogTag(Card.class);
    }
    
    public Card(final JSONObject jsonObject, final CardKey.Provider provider) {
        this(jsonObject, provider, null, null, null);
    }
    
    public Card(final JSONObject b, final CardKey.Provider provider, final br p5, final dn q, final c r) {
        this.e = false;
        this.f = false;
        this.g = false;
        this.h = false;
        this.i = false;
        this.m = false;
        this.b = b;
        this.p = p5;
        this.q = q;
        this.r = r;
        this.c = ec.a(b.optJSONObject(provider.getKey(CardKey.EXTRAS)), new HashMap<String, String>());
        this.d = b.getString(provider.getKey(CardKey.ID));
        this.e = b.optBoolean(provider.getKey(CardKey.VIEWED));
        this.g = b.optBoolean(provider.getKey(CardKey.DISMISSED), false);
        this.i = b.optBoolean(provider.getKey(CardKey.PINNED), false);
        this.j = b.getLong(provider.getKey(CardKey.CREATED));
        this.l = b.optLong(provider.getKey(CardKey.EXPIRES_AT), -1L);
        this.m = b.optBoolean(provider.getKey(CardKey.OPEN_URI_IN_WEBVIEW), false);
        this.h = b.optBoolean(provider.getKey(CardKey.REMOVED), false);
        final JSONArray optJSONArray = b.optJSONArray(provider.getKey(CardKey.CATEGORIES));
        if (optJSONArray != null && optJSONArray.length() != 0) {
            this.n = EnumSet.noneOf(CardCategory.class);
            for (int i = 0; i < optJSONArray.length(); ++i) {
                final CardCategory value = CardCategory.get(optJSONArray.getString(i));
                if (value != null) {
                    this.n.add(value);
                }
            }
        }
        else {
            this.n = EnumSet.of(CardCategory.NO_CATEGORY);
        }
        this.k = b.optLong(provider.getKey(CardKey.UPDATED), this.j);
        this.o = b.optBoolean(provider.getKey(CardKey.DISMISSIBLE), false);
        this.f = b.optBoolean(provider.getKey(CardKey.READ), this.e);
    }
    
    boolean a() {
        if (StringUtils.isNullOrBlank(this.d)) {
            AppboyLogger.e(Card.a, "Card ID cannot be null");
            return false;
        }
        return true;
    }
    
    @Override
    public JSONObject forJsonPut() {
        return this.b;
    }
    
    public CardType getCardType() {
        return CardType.DEFAULT;
    }
    
    public EnumSet<CardCategory> getCategories() {
        return this.n;
    }
    
    public long getCreated() {
        return this.j;
    }
    
    public long getExpiresAt() {
        return this.l;
    }
    
    public Map<String, String> getExtras() {
        return this.c;
    }
    
    public String getId() {
        return this.d;
    }
    
    public boolean getIsDismissible() {
        return this.o;
    }
    
    public boolean getIsPinned() {
        return this.i;
    }
    
    public boolean getOpenUriInWebView() {
        return this.m;
    }
    
    public long getUpdated() {
        return this.k;
    }
    
    public String getUrl() {
        return null;
    }
    
    public boolean getViewed() {
        return this.e;
    }
    
    public boolean isControl() {
        return this.getCardType() == CardType.CONTROL;
    }
    
    public boolean isDismissed() {
        return this.g;
    }
    
    public boolean isEqualToCard(final Card card) {
        return this.d.equals(card.getId()) && this.k == card.getUpdated() && this.p == card.p;
    }
    
    public boolean isExpired() {
        return this.getExpiresAt() != -1L && this.getExpiresAt() <= du.a();
    }
    
    public boolean isInCategorySet(final EnumSet<CardCategory> set) {
        final Iterator<CardCategory> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (this.n.contains(iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isRead() {
        return this.f;
    }
    
    public boolean isRemoved() {
        return this.h;
    }
    
    public boolean logClick() {
        try {
            if (this.p != null && this.r != null && this.a()) {
                this.p.a(this.r.e(this.d));
                return true;
            }
        }
        catch (Exception ex) {
            AppboyLogger.w(Card.a, "Failed to log card clicked.", ex);
        }
        return false;
    }
    
    public boolean logImpression() {
        try {
            if (this.p != null && this.r != null && this.q != null && this.a()) {
                br br;
                ca ca;
                if (!this.isControl()) {
                    final String a = Card.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Logging impression event for card with id: ");
                    sb.append(this.d);
                    AppboyLogger.v(a, sb.toString());
                    br = this.p;
                    ca = this.r.a(this.d);
                }
                else {
                    final String a2 = Card.a;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Logging control impression event for card with id: ");
                    sb2.append(this.d);
                    AppboyLogger.v(a2, sb2.toString());
                    br = this.p;
                    ca = this.r.d(this.d);
                }
                br.a(ca);
                this.q.b(this.d);
                return true;
            }
        }
        catch (Exception ex) {
            final String a3 = Card.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to log card impression for card id: ");
            sb3.append(this.d);
            AppboyLogger.w(a3, sb3.toString(), ex);
        }
        return false;
    }
    
    public void setIsDismissed(final boolean g) {
        if (this.g && g) {
            AppboyLogger.w(Card.a, "Cannot dismiss a card more than once. Doing nothing.");
            return;
        }
        this.g = g;
        final dn q = this.q;
        if (q != null) {
            q.c(this.d);
        }
        if (g) {
            try {
                if (this.p != null && this.r != null && this.a()) {
                    this.p.a(this.r.c(this.d));
                }
            }
            catch (Exception ex) {
                AppboyLogger.w(Card.a, "Failed to log card dismissed.", ex);
            }
        }
    }
    
    public void setIsPinned(final boolean i) {
        this.i = i;
    }
    
    public void setIsRead(final boolean f) {
        this.f = f;
        this.setChanged();
        this.notifyObservers();
        if (f) {
            final dn q = this.q;
            if (q != null) {
                try {
                    q.a(this.d);
                }
                catch (Exception ex) {
                    AppboyLogger.d(Card.a, "Failed to mark card as read.", ex);
                }
            }
        }
    }
    
    public void setViewed(final boolean e) {
        this.e = e;
        final dn q = this.q;
        if (q != null) {
            q.b(this.d);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("mId='");
        sb.append(this.d);
        sb.append('\'');
        sb.append(", mViewed='");
        sb.append(this.e);
        sb.append('\'');
        sb.append(", mCreated='");
        sb.append(this.j);
        sb.append('\'');
        sb.append(", mUpdated='");
        sb.append(this.k);
        sb.append('\'');
        return sb.toString();
    }
}
