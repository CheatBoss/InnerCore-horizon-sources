package bo.app;

import android.os.*;
import com.appboy.support.*;
import java.util.concurrent.*;
import com.appboy.*;
import android.content.*;
import android.app.*;

public class bn
{
    static final long a;
    private static final String b;
    private static final long c;
    private final Object d;
    private final do e;
    private final ad f;
    private final Context g;
    private final AlarmManager h;
    private final int i;
    private final String j;
    private final dr k;
    private volatile cd l;
    private final Handler m;
    private final Runnable n;
    private final boolean o;
    
    static {
        b = AppboyLogger.getAppboyLogTag(bn.class);
        c = TimeUnit.SECONDS.toMillis(10L);
        a = TimeUnit.SECONDS.toMillis(10L);
    }
    
    public bn(final Context g, final do e, final ad f, final AlarmManager h, final dr k, final int i, final boolean o) {
        this.d = new Object();
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.k = k;
        this.m = dz.a();
        this.n = new Runnable() {
            @Override
            public void run() {
                AppboyLogger.d(bn.b, "Requesting data flush on internal session close flush timer.");
                Appboy.getInstance(g).requestImmediateDataFlush();
            }
        };
        this.o = o;
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context p0, final Intent p1) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: getfield        bo/app/bn$2.a:Lbo/app/bn;
                //     4: invokestatic    bo/app/bn.a:(Lbo/app/bn;)Ljava/lang/Object;
                //     7: astore_1       
                //     8: aload_1        
                //     9: monitorenter   
                //    10: aload_0        
                //    11: getfield        bo/app/bn$2.a:Lbo/app/bn;
                //    14: invokestatic    bo/app/bn.b:(Lbo/app/bn;)V
                //    17: goto            54
                //    20: astore_2       
                //    21: goto            57
                //    24: astore_2       
                //    25: aload_0        
                //    26: getfield        bo/app/bn$2.a:Lbo/app/bn;
                //    29: invokestatic    bo/app/bn.c:(Lbo/app/bn;)Lbo/app/ad;
                //    32: aload_2        
                //    33: ldc             Ljava/lang/Throwable;.class
                //    35: invokeinterface bo/app/ad.a:(Ljava/lang/Object;Ljava/lang/Class;)V
                //    40: goto            54
                //    43: astore_2       
                //    44: invokestatic    bo/app/bn.h:()Ljava/lang/String;
                //    47: ldc             "Failed to log throwable."
                //    49: aload_2        
                //    50: invokestatic    com/appboy/support/AppboyLogger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                //    53: pop            
                //    54: aload_1        
                //    55: monitorexit    
                //    56: return         
                //    57: aload_1        
                //    58: monitorexit    
                //    59: aload_2        
                //    60: athrow         
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  10     17     24     54     Ljava/lang/Exception;
                //  10     17     20     61     Any
                //  25     40     43     54     Ljava/lang/Exception;
                //  25     40     20     61     Any
                //  44     54     20     61     Any
                //  54     56     20     61     Any
                //  57     59     20     61     Any
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index: 33, Size: 33
                //     at java.util.ArrayList.rangeCheck(Unknown Source)
                //     at java.util.ArrayList.get(Unknown Source)
                //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
                //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
                //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
                //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
                //     at java.lang.Thread.run(Unknown Source)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        };
        final StringBuilder sb = new StringBuilder();
        sb.append(g.getPackageName());
        sb.append(".intent.APPBOY_SESSION_SHOULD_SEAL");
        this.j = sb.toString();
        g.registerReceiver((BroadcastReceiver)broadcastReceiver, new IntentFilter(this.j));
    }
    
    private void a(final long n) {
        final String b = bn.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("Creating a session seal alarm with a delay of ");
        sb.append(n);
        sb.append(" ms");
        AppboyLogger.d(b, sb.toString());
        final Intent intent = new Intent(this.j);
        intent.putExtra("session_id", this.l.toString());
        this.h.set(1, du.c() + n, PendingIntent.getBroadcast(this.g, 0, intent, 1073741824));
    }
    
    static boolean a(final cd cd, final int n, final boolean b) {
        final long c = du.c();
        final long millis = TimeUnit.SECONDS.toMillis(n);
        final long millis2 = TimeUnit.SECONDS.toMillis(cd.c().longValue());
        final long millis3 = TimeUnit.SECONDS.toMillis((long)cd.b());
        if (b) {
            return millis3 + millis + bn.a <= c;
        }
        return millis2 + millis <= c;
    }
    
    static long b(final cd cd, final int n, final boolean b) {
        final long millis = TimeUnit.SECONDS.toMillis(n);
        if (b) {
            return Math.max(bn.a, TimeUnit.SECONDS.toMillis((long)cd.b()) + millis - du.c());
        }
        return millis;
    }
    
    private boolean i() {
        synchronized (this.d) {
            this.k();
            if (this.l == null || this.l.d()) {
                final cd l = this.l;
                this.l = this.j();
                if (l != null && l.d()) {
                    final String b = bn.b;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Clearing completely dispatched sealed session ");
                    sb.append(l.a());
                    AppboyLogger.d(b, sb.toString());
                    this.e.b(l);
                }
                return true;
            }
            if (this.l.c() != null) {
                this.l.a(null);
                return true;
            }
            return false;
        }
    }
    
    private cd j() {
        final cd cd = new cd(ce.a(), du.b());
        this.k.a(true);
        this.f.a(ao.a, ao.class);
        final String b = bn.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("New session created with ID: ");
        sb.append(cd.a());
        AppboyLogger.i(b, sb.toString());
        return cd;
    }
    
    private void k() {
        synchronized (this.d) {
            if (this.l == null) {
                this.l = this.e.a();
                if (this.l != null) {
                    final String b = bn.b;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Restored session from offline storage: ");
                    sb.append(this.l.a().toString());
                    AppboyLogger.d(b, sb.toString());
                }
            }
            if (this.l != null && this.l.c() != null && !this.l.d() && a(this.l, this.i, this.o)) {
                final String b2 = bn.b;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Session [");
                sb2.append(this.l.a());
                sb2.append("] being sealed because its end time is over the grace period.");
                AppboyLogger.i(b2, sb2.toString());
                this.e();
                this.e.b(this.l);
                this.l = null;
            }
        }
    }
    
    private void l() {
        final Intent intent = new Intent(this.j);
        intent.putExtra("session_id", this.l.toString());
        this.h.cancel(PendingIntent.getBroadcast(this.g, 0, intent, 1073741824));
    }
    
    public cd a() {
        synchronized (this.d) {
            if (this.i()) {
                this.e.a(this.l);
            }
            this.g();
            this.l();
            this.f.a(aq.a, aq.class);
            return this.l;
        }
    }
    
    public cd b() {
        synchronized (this.d) {
            this.i();
            this.l.a(du.b());
            this.e.a(this.l);
            this.f();
            this.a(b(this.l, this.i, this.o));
            this.f.a(ar.a, ar.class);
            return this.l;
        }
    }
    
    public ce c() {
        synchronized (this.d) {
            this.k();
            if (this.l == null) {
                return null;
            }
            return this.l.a();
        }
    }
    
    public boolean d() {
        while (true) {
            synchronized (this.d) {
                if (this.l != null && this.l.d()) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public void e() {
        synchronized (this.d) {
            if (this.l != null) {
                this.l.e();
                this.e.a(this.l);
                this.f.a(new ap(this.l), ap.class);
            }
        }
    }
    
    protected void f() {
        this.g();
        this.m.postDelayed(this.n, bn.c);
    }
    
    protected void g() {
        this.m.removeCallbacks(this.n);
    }
}
