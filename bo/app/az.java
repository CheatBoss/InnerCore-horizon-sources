package bo.app;

import java.util.concurrent.*;

public final class az extends ThreadPoolExecutor
{
    private static final TimeUnit a;
    
    static {
        a = TimeUnit.MILLISECONDS;
    }
    
    public az(final ThreadFactory threadFactory) {
        super(1, 1, 0L, az.a, ei.d(), threadFactory);
        this.setRejectedExecutionHandler(new DiscardOldestPolicy());
    }
}
