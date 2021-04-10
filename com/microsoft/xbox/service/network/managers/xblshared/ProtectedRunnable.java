package com.microsoft.xbox.service.network.managers.xblshared;

public class ProtectedRunnable implements Runnable
{
    private static final String TAG;
    private final Runnable runnable;
    
    static {
        TAG = ProtectedRunnable.class.getSimpleName();
    }
    
    public ProtectedRunnable(final Runnable runnable) {
        this.runnable = runnable;
    }
    
    @Override
    public void run() {
        for (int n = 0, n2 = 0; n == 0 && n2 < 10; ++n2) {
            try {
                this.runnable.run();
                n = 1;
            }
            catch (LinkageError linkageError) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {}
            }
        }
    }
}
