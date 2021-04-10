package bo.app;

import java.util.concurrent.*;

public class ei
{
    private static final int a;
    private static final int b;
    private static final int c;
    
    static {
        b = Math.max(2, Math.min((a = Runtime.getRuntime().availableProcessors()) - 1, 4));
        c = ei.a * 2 + 1;
    }
    
    public static int a() {
        return ei.b;
    }
    
    public static int b() {
        return ei.c;
    }
    
    public static long c() {
        return 1L;
    }
    
    public static BlockingQueue<Runnable> d() {
        return new LinkedBlockingQueue<Runnable>(128);
    }
}
