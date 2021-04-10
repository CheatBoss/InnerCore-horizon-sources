package bo.app;

import android.app.*;
import java.util.*;
import com.appboy.support.*;
import java.util.concurrent.*;
import com.appboy.receivers.*;
import android.net.*;
import com.appboy.events.*;
import android.content.*;

public class q
{
    private static final String a;
    private static final int b;
    private final Context c;
    private final s d;
    private final AlarmManager e;
    private final p f;
    private final BroadcastReceiver g;
    private final PendingIntent h;
    private final Random i;
    private aa j;
    private long k;
    private boolean l;
    private int m;
    private volatile boolean n;
    
    static {
        a = AppboyLogger.getAppboyLogTag(q.class);
        b = (int)TimeUnit.MINUTES.toMillis(5L);
    }
    
    public q(final Context c, final ad ad, final s d, final AlarmManager e, final p f, final String s) {
        this.i = new Random();
        this.m = 0;
        this.n = false;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.j = aa.b;
        this.k = -1L;
        this.h = PendingIntent.getBroadcast(this.c, s.hashCode(), new Intent("com.appboy.action.receiver.DATA_SYNC").setClass(c, (Class)AppboyActionReceiver.class), 134217728);
        this.g = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                new Thread(new Runnable() {
                    final /* synthetic */ BroadcastReceiver$PendingResult c = q$1.this.goAsync();
                    
                    @Override
                    public void run() {
                        try {
                            q.this.d.a(intent, (ConnectivityManager)context.getSystemService("connectivity"));
                            q.this.c();
                        }
                        catch (Exception ex) {
                            AppboyLogger.e(q.a, "Failed to process connectivity event.", ex);
                            q.this.a(ad, ex);
                        }
                        this.c.finish();
                    }
                }).start();
            }
        };
        AppboyLogger.d(q.a, "Registered broadcast filters");
    }
    
    static int a(final Random random, final int n, final int n2) {
        return random.nextInt(Math.abs(n - n2)) + Math.min(n, n2);
    }
    
    private void a(final long n, final long n2) {
        this.e.setInexactRepeating(1, n, n2, this.h);
    }
    
    private void a(final ad ad, final Throwable t) {
        try {
            ad.a(t, Throwable.class);
        }
        catch (Exception ex) {
            AppboyLogger.e(q.a, "Failed to log throwable.", ex);
        }
    }
    
    private void h() {
        final PendingIntent h = this.h;
        if (h != null) {
            this.e.cancel(h);
        }
    }
    
    void a(final long n) {
        if (this.e == null) {
            AppboyLogger.d(q.a, "Alarm manager was null. Ignoring request to reset it.");
            return;
        }
        if (this.k <= 0L) {
            AppboyLogger.d(q.a, "Cancelling alarm because delay value was not positive.");
            this.h();
            return;
        }
        this.a(du.c() + n, this.k);
    }
    
    public void a(final ac ac) {
        ac.a(new IEventSubscriber<aq>() {
            public void a(final aq aq) {
                q.this.j = aa.a;
                q.this.c();
            }
        }, aq.class);
        ac.a(new IEventSubscriber<ar>() {
            public void a(final ar ar) {
                q.this.j = aa.b;
                q.this.c();
            }
        }, ar.class);
        ac.b(new IEventSubscriber<ag>() {
            public void a(final ag ag) {
                final String f = q.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Handling network request failure. Previous sleep delay: ");
                sb.append(q.this.m);
                AppboyLogger.d(f, sb.toString());
                q.this.m = Math.min(q.b, q.a(q.this.i, (int)q.this.k, q.this.m * 3));
                final String f2 = q.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("New flush sleep delay: ");
                sb2.append(q.this.m);
                sb2.append(" default interval: ");
                sb2.append(q.this.k);
                AppboyLogger.d(f2, sb2.toString());
                final q a = q.this;
                a.a(a.k + q.this.m);
            }
        }, ag.class);
        ac.a(new IEventSubscriber<ah>() {
            public void a(final ah ah) {
                if (q.this.m != 0) {
                    final String f = q.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Received successful request flush. Default flush interval reset to ");
                    sb.append(q.this.k);
                    AppboyLogger.d(f, sb.toString());
                    q.this.m = 0;
                    final q a = q.this;
                    a.a(a.k);
                }
            }
        }, ah.class);
    }
    
    public void a(final boolean l) {
        synchronized (this) {
            this.l = l;
            this.c();
            if (l) {
                this.b();
            }
            else {
                this.a();
            }
        }
    }
    
    public boolean a() {
        synchronized (this) {
            if (this.n) {
                AppboyLogger.d(q.a, "The data sync policy is already running. Ignoring request.");
                return false;
            }
            AppboyLogger.d(q.a, "Data sync started");
            this.d();
            this.a(3000L);
            return this.n = true;
        }
    }
    
    public boolean b() {
        synchronized (this) {
            if (!this.n) {
                AppboyLogger.d(q.a, "The data sync policy is not running. Ignoring request.");
                return false;
            }
            AppboyLogger.d(q.a, "Data sync stopped");
            this.h();
            this.e();
            this.n = false;
            return true;
        }
    }
    
    protected void c() {
        final long k = this.k;
        Label_0108: {
            if (this.j != aa.b) {
                if (!this.l) {
                    final int n = q$6.a[this.d.a().ordinal()];
                    if (n != 1) {
                        long i;
                        if (n != 2) {
                            if (n != 3 && n != 4) {
                                i = this.f.b();
                            }
                            else {
                                i = this.f.c();
                            }
                        }
                        else {
                            i = this.f.a();
                        }
                        this.k = i;
                        break Label_0108;
                    }
                }
            }
            this.k = -1L;
        }
        final long j = this.k;
        if (k != j) {
            this.a(j);
            final String a = q.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Dispatch state has changed from ");
            sb.append(k);
            sb.append(" to ");
            sb.append(this.k);
            sb.append(".");
            AppboyLogger.d(a, sb.toString());
        }
    }
    
    protected void d() {
        this.c.registerReceiver(this.g, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    
    protected void e() {
        this.c.unregisterReceiver(this.g);
    }
}
