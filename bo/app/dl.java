package bo.app;

import com.appboy.events.*;
import com.appboy.support.*;
import com.appboy.enums.*;
import com.appboy.models.cards.*;
import java.util.concurrent.*;
import android.content.*;
import org.json.*;
import java.util.*;

public class dl implements dn<FeedUpdatedEvent>
{
    private static final String a;
    private final SharedPreferences b;
    private final Set<String> c;
    private final Set<String> d;
    private final c e;
    private bl f;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dl.class);
    }
    
    public dl(final Context context, final String s) {
        this.e = new b();
        String s2;
        if (s == null) {
            s2 = "";
        }
        else {
            s2 = s;
        }
        final String cacheFileSuffix = StringUtils.getCacheFileSuffix(context, s2);
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.feedstorageprovider");
        sb.append(cacheFileSuffix);
        this.b = context.getSharedPreferences(sb.toString(), 0);
        this.c = this.a(dl.a.b);
        this.d = this.a(dl.a.a);
        this.e(s);
    }
    
    private FeedUpdatedEvent a(final JSONArray jsonArray, final String s, final boolean b, final long n) {
        List<Card> a;
        if (jsonArray != null && jsonArray.length() != 0) {
            a = bz.a(jsonArray, new CardKey.Provider(false), this.f, this, this.e);
        }
        else {
            a = new ArrayList<Card>();
        }
        for (final Card card : a) {
            if (this.c.contains(card.getId())) {
                card.setViewed(true);
                card.setIsRead(true);
            }
            if (this.d.contains(card.getId())) {
                card.setIsRead(true);
            }
        }
        return new FeedUpdatedEvent(a, s, b, n);
    }
    
    private Set<String> a(final a a) {
        final String a2 = a.a();
        final String b = a.b();
        if (this.b.contains(b)) {
            final Set<String> d = d(this.b.getString(b, (String)null));
            final SharedPreferences$Editor edit = this.b.edit();
            edit.remove(b);
            edit.apply();
            this.a(d, a);
            return d;
        }
        return new ConcurrentSkipListSet<String>(this.b.getStringSet(a2, (Set)new HashSet()));
    }
    
    static Set<String> a(final JSONArray jsonArray) {
        final HashSet<String> set = new HashSet<String>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(CardKey.ID.getFeedKey())) {
                    set.add(jsonObject.getString(CardKey.ID.getFeedKey()));
                }
            }
        }
        return set;
    }
    
    private void a(final JSONArray jsonArray, final long n) {
        final SharedPreferences$Editor edit = this.b.edit();
        if (jsonArray != null && jsonArray.length() != 0) {
            edit.putString("cards", jsonArray.toString());
        }
        else {
            edit.remove("cards");
        }
        edit.putLong("cards_timestamp", n);
        edit.apply();
    }
    
    static Set<String> d(final String s) {
        final HashSet<Object> set = new HashSet<Object>();
        if (s != null) {
            Collections.addAll(set, s.split(";"));
        }
        return (Set<String>)set;
    }
    
    private void e(final String s) {
        final SharedPreferences$Editor edit = this.b.edit();
        edit.putString("uid", s);
        edit.apply();
    }
    
    public FeedUpdatedEvent a() {
        return this.a(new JSONArray(this.b.getString("cards", "[]")), this.b.getString("uid", ""), true, this.b.getLong("cards_timestamp", -1L));
    }
    
    public FeedUpdatedEvent a(final JSONArray jsonArray, final String s) {
        String s2;
        if (s == null) {
            s2 = "";
        }
        else {
            s2 = s;
        }
        final String string = this.b.getString("uid", "");
        if (string.equals(s2)) {
            final String a = dl.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Updating offline feed for user with id: ");
            sb.append(s);
            AppboyLogger.i(a, sb.toString());
            final long a2 = du.a();
            this.a(jsonArray, a2);
            this.c.retainAll(a(jsonArray));
            this.a(this.c, dl.a.b);
            this.d.retainAll(a(jsonArray));
            this.a(this.d, dl.a.a);
            return this.a(jsonArray, s, false, a2);
        }
        final String a3 = dl.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("The received cards are for user ");
        sb2.append(s);
        sb2.append(" and the current user is ");
        sb2.append(string);
        sb2.append(" , the cards will be discarded and no changes will be made.");
        AppboyLogger.i(a3, sb2.toString());
        return null;
    }
    
    public void a(final bl f) {
        this.f = f;
    }
    
    @Override
    public void a(final String s) {
        if (this.d.contains(s)) {
            return;
        }
        this.d.add(s);
        this.a(this.d, dl.a.a);
    }
    
    void a(final Set<String> set, final a a) {
        final String a2 = a.a();
        final SharedPreferences$Editor edit = this.b.edit();
        if (set != null && !set.isEmpty()) {
            edit.putStringSet(a2, (Set)set);
        }
        else {
            edit.remove(a2);
        }
        edit.apply();
    }
    
    @Override
    public void b(final String s) {
        if (this.c.contains(s)) {
            return;
        }
        this.c.add(s);
        this.a(this.c, dl.a.b);
    }
    
    @Override
    public void c(final String s) {
    }
    
    enum a
    {
        a("read_cards_set", "read_cards_flat"), 
        b("viewed_cards_set", "viewed_cards_flat");
        
        private final String c;
        private final String d;
        
        private a(final String c, final String d) {
            this.c = c;
            this.d = d;
        }
        
        public String a() {
            return this.c;
        }
        
        public String b() {
            return this.d;
        }
    }
}
