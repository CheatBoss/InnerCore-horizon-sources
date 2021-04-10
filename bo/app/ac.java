package bo.app;

import android.app.*;
import com.appboy.support.*;
import java.util.concurrent.*;
import java.util.*;
import com.appboy.events.*;

public class ac implements ad
{
    private static final String a;
    private final ConcurrentMap<Activity, ConcurrentMap<Class, CopyOnWriteArraySet<IEventSubscriber>>> b;
    private final ConcurrentMap<Class, CopyOnWriteArraySet<IEventSubscriber>> c;
    private final ConcurrentMap<Class, CopyOnWriteArraySet<IEventSubscriber>> d;
    private final Executor e;
    private final dq f;
    private final Object g;
    private final Object h;
    private final Object i;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ac.class);
    }
    
    public ac(final Executor e, final dq f) {
        this.b = new ConcurrentHashMap<Activity, ConcurrentMap<Class, CopyOnWriteArraySet<IEventSubscriber>>>();
        this.c = new ConcurrentHashMap<Class, CopyOnWriteArraySet<IEventSubscriber>>();
        this.d = new ConcurrentHashMap<Class, CopyOnWriteArraySet<IEventSubscriber>>();
        this.g = new Object();
        this.h = new Object();
        this.i = new Object();
        this.e = e;
        this.f = f;
    }
    
    private <T> CopyOnWriteArraySet<IEventSubscriber<T>> a(final Class<T> clazz, final CopyOnWriteArraySet<IEventSubscriber> set) {
        final CopyOnWriteArraySet<IEventSubscriber> set2 = set;
        final String a = ac.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Triggering ");
        sb.append(clazz.getName());
        sb.append(" on ");
        sb.append(set.size());
        sb.append(" subscribers.");
        AppboyLogger.d(a, sb.toString(), false);
        return (CopyOnWriteArraySet<IEventSubscriber<T>>)set2;
    }
    
    private <T> boolean a(final IEventSubscriber<T> eventSubscriber, final Class<T> clazz, final ConcurrentMap<Class, CopyOnWriteArraySet<IEventSubscriber>> concurrentMap) {
        if (eventSubscriber == null) {
            String name;
            if (clazz == null) {
                name = "null eventClass";
            }
            else {
                name = clazz.getName();
            }
            final String a = ac.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Error: Attempted to add a null subscriber for eventClass ");
            sb.append(name);
            sb.append(". This subscriber is being ignored. Please check your calling code to ensure that all potential subscriptions are valid.");
            AppboyLogger.e(a, sb.toString());
            return false;
        }
        CopyOnWriteArraySet<IEventSubscriber<T>> set;
        if ((set = concurrentMap.get(clazz)) == null) {
            set = new CopyOnWriteArraySet<IEventSubscriber<T>>();
            final CopyOnWriteArraySet<IEventSubscriber<T>> set2 = concurrentMap.putIfAbsent(clazz, set);
            if (set2 != null) {
                set = set2;
            }
        }
        return set.add(eventSubscriber);
    }
    
    private <T> boolean a(final CopyOnWriteArraySet<IEventSubscriber> set, final IEventSubscriber<T> eventSubscriber) {
        return set != null && eventSubscriber != null && set.remove(eventSubscriber);
    }
    
    public void a() {
        synchronized (this.h) {
            this.c.clear();
            // monitorexit(this.h)
            Object o = this.i;
            synchronized (this.h) {
                this.d.clear();
                // monitorexit(this.h)
                o = this.g;
                synchronized (this.h) {
                    this.b.clear();
                }
            }
        }
    }
    
    @Override
    public <T> void a(final T t, final Class<T> clazz) {
        if (this.f.a()) {
            final String a = ac.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("SDK is disabled. Not publishing event class: ");
            sb.append(clazz.getName());
            sb.append(" and message: ");
            sb.append(t.toString());
            AppboyLogger.d(a, sb.toString());
            return;
        }
        final String a2 = ac.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(clazz.getName());
        sb2.append(" fired: ");
        sb2.append(t.toString());
        final String string = sb2.toString();
        boolean b = false;
        AppboyLogger.d(a2, string, false);
        final Iterator<Map.Entry<Object, Object>> iterator = this.b.entrySet().iterator();
        final boolean b2 = true;
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final CopyOnWriteArraySet set = entry.getValue().get(clazz);
            if (set != null && !set.isEmpty()) {
                entry.getKey().runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Iterator<IEventSubscriber<Object>> iterator = ac.this.a((Class<Object>)clazz, set).iterator();
                        while (iterator.hasNext()) {
                            iterator.next().trigger(t);
                        }
                    }
                });
                b = true;
            }
        }
        final CopyOnWriteArraySet<IEventSubscriber> set2 = this.c.get(clazz);
        boolean b3 = b;
        if (set2 != null) {
            final Iterator<IEventSubscriber<T>> iterator2 = this.a(clazz, set2).iterator();
            while (iterator2.hasNext()) {
                this.e.execute(new Runnable() {
                    final /* synthetic */ IEventSubscriber a = iterator2.next();
                    
                    @Override
                    public void run() {
                        this.a.trigger(t);
                    }
                });
            }
            b3 = b;
            if (!set2.isEmpty()) {
                b3 = true;
            }
        }
        final CopyOnWriteArraySet<IEventSubscriber> set3 = this.d.get(clazz);
        if (set3 != null) {
            final Iterator<IEventSubscriber<T>> iterator3 = this.a(clazz, set3).iterator();
            while (iterator3.hasNext()) {
                iterator3.next().trigger(t);
            }
            if (!set3.isEmpty()) {
                b3 = b2;
            }
        }
        if (!b3 && clazz.equals(InAppMessageEvent.class)) {
            AppboyLogger.i(ac.a, "***********************************************************************************************");
            AppboyLogger.i(ac.a, "**                                       !! WARNING !!                                       **");
            AppboyLogger.i(ac.a, "**             InAppMessageEvent was published, but no subscribers were found.               **");
            AppboyLogger.i(ac.a, "**  This is likely an integration error. Please ensure that the AppboyInAppMessageManager is **");
            AppboyLogger.i(ac.a, "**               registered as early as possible. Additionally, be sure to call              **");
            AppboyLogger.i(ac.a, "**       AppboyInAppMessageManager.ensureSubscribedToInAppMessageEvents(Context) in your     **");
            AppboyLogger.i(ac.a, "**          Application onCreate() to avoid losing any in-app messages in the future.        **");
            AppboyLogger.i(ac.a, "***********************************************************************************************");
        }
    }
    
    public <T> boolean a(final IEventSubscriber<T> eventSubscriber, final Class<T> clazz) {
        synchronized (this.h) {
            return this.a(eventSubscriber, clazz, this.c);
        }
    }
    
    public <T> boolean b(final IEventSubscriber<T> eventSubscriber, final Class<T> clazz) {
        synchronized (this.i) {
            return this.a(eventSubscriber, clazz, this.d);
        }
    }
    
    public <T> boolean c(final IEventSubscriber<T> eventSubscriber, final Class<T> clazz) {
        synchronized (this.h) {
            return this.a(this.c.get(clazz), eventSubscriber);
        }
    }
}
