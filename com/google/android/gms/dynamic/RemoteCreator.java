package com.google.android.gms.dynamic;

import android.os.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.*;

public abstract class RemoteCreator<T>
{
    private final String zzabo;
    private T zzabp;
    
    protected RemoteCreator(final String zzabo) {
        this.zzabo = zzabo;
    }
    
    protected abstract T getRemoteCreator(final IBinder p0);
    
    protected final T getRemoteCreatorInstance(Context remoteContext) throws RemoteCreatorException {
        if (this.zzabp == null) {
            Preconditions.checkNotNull(remoteContext);
            remoteContext = GooglePlayServicesUtilLight.getRemoteContext(remoteContext);
            if (remoteContext != null) {
                final ClassLoader classLoader = remoteContext.getClassLoader();
                try {
                    this.zzabp = this.getRemoteCreator((IBinder)classLoader.loadClass(this.zzabo).newInstance());
                    return this.zzabp;
                }
                catch (IllegalAccessException ex) {
                    throw new RemoteCreatorException("Could not access creator.", ex);
                }
                catch (InstantiationException ex2) {
                    throw new RemoteCreatorException("Could not instantiate creator.", ex2);
                }
                catch (ClassNotFoundException ex3) {
                    throw new RemoteCreatorException("Could not load creator class.", ex3);
                }
            }
            throw new RemoteCreatorException("Could not get remote context.");
        }
        return this.zzabp;
    }
    
    public static class RemoteCreatorException extends Exception
    {
        public RemoteCreatorException(final String s) {
            super(s);
        }
        
        public RemoteCreatorException(final String s, final Throwable t) {
            super(s, t);
        }
    }
}
