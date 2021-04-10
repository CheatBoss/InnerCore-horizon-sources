package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.stats.*;
import android.content.*;
import android.os.*;

public final class zzef implements ServiceConnection, BaseConnectionCallbacks, BaseOnConnectionFailedListener
{
    final /* synthetic */ zzdr zzasg;
    private volatile boolean zzasm;
    private volatile zzao zzasn;
    
    protected zzef(final zzdr zzasg) {
        this.zzasg = zzasg;
    }
    
    public final void onConnected(final Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        // monitorenter(this)
        try {
            try {
                final zzag zzag = this.zzasn.getService();
                if (!zzn.zzia()) {
                    this.zzasn = null;
                }
                this.zzasg.zzgn().zzc(new zzei(this, zzag));
            }
            finally {
            }
            // monitorexit(this)
            // monitorexit(this)
        }
        catch (DeadObjectException ex) {}
        catch (IllegalStateException ex2) {}
    }
    
    public final void onConnectionFailed(final ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        final zzap zzkf = this.zzasg.zzadj.zzkf();
        if (zzkf != null) {
            zzkf.zzjg().zzg("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzasm = false;
            this.zzasn = null;
            // monitorexit(this)
            this.zzasg.zzgn().zzc(new zzek(this));
        }
    }
    
    public final void onConnectionSuspended(final int n) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zzasg.zzgo().zzjk().zzbx("Service connection suspended");
        this.zzasg.zzgn().zzc(new zzej(this));
    }
    
    public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceConnected");
        // monitorenter(this)
        Label_0038: {
            if (binder != null) {
                break Label_0038;
            }
            zzag zzag;
            zzag zzag2;
            zzag zzag3;
            zzag zzag4;
            zzag zzag5;
            String interfaceDescriptor;
            IInterface queryLocalInterface;
            Label_0188_Outer:Label_0125_Outer:
            while (true) {
                while (true) {
                    Label_0250: {
                        try {
                            this.zzasm = false;
                            this.zzasg.zzgo().zzjd().zzbx("Service connected with null binder");
                            // monitorexit(this)
                            return;
                        }
                        finally {
                            // monitorexit(this)
                            // monitorexit(this)
                            // iftrue(Label_0224:, zzag != null)
                            while (true) {
                                while (true) {
                                    return;
                                    Block_5: {
                                        break Block_5;
                                        Label_0224: {
                                            this.zzasg.zzgn().zzc(new zzeg(this, zzag));
                                        }
                                        continue Label_0188_Outer;
                                    }
                                    this.zzasm = false;
                                    try {
                                        ConnectionTracker.getInstance().unbindService(this.zzasg.getContext(), (ServiceConnection)this.zzasg.zzarz);
                                    }
                                    catch (IllegalArgumentException ex) {}
                                    continue Label_0188_Outer;
                                }
                                zzag2 = null;
                                zzag3 = null;
                                zzag4 = null;
                                zzag5 = zzag2;
                                try {
                                    interfaceDescriptor = binder.getInterfaceDescriptor();
                                    zzag5 = zzag2;
                                    if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                                        if (binder == null) {
                                            zzag = zzag4;
                                            zzag5 = zzag;
                                            this.zzasg.zzgo().zzjl().zzbx("Bound to IMeasurementService interface");
                                        }
                                        else {
                                            zzag5 = zzag2;
                                            queryLocalInterface = binder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                                            zzag5 = zzag2;
                                            if (queryLocalInterface instanceof zzag) {
                                                zzag5 = zzag2;
                                                zzag = (zzag)queryLocalInterface;
                                                break Label_0250;
                                            }
                                            zzag5 = zzag2;
                                            zzag = new zzai(binder);
                                            break Label_0250;
                                        }
                                    }
                                    else {
                                        zzag5 = zzag2;
                                        this.zzasg.zzgo().zzjd().zzg("Got binder with a wrong descriptor", interfaceDescriptor);
                                        zzag = zzag3;
                                    }
                                }
                                catch (RemoteException ex2) {
                                    this.zzasg.zzgo().zzjd().zzbx("Service connect failed to get IMeasurementService");
                                    zzag = zzag5;
                                }
                                continue Label_0125_Outer;
                            }
                        }
                    }
                    continue;
                }
            }
        }
    }
    
    public final void onServiceDisconnected(final ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zzasg.zzgo().zzjk().zzbx("Service disconnected");
        this.zzasg.zzgn().zzc(new zzeh(this, componentName));
    }
    
    public final void zzc(final Intent intent) {
        this.zzasg.zzaf();
        final Context context = this.zzasg.getContext();
        final ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzasm) {
                this.zzasg.zzgo().zzjl().zzbx("Connection attempt already in progress");
                return;
            }
            this.zzasg.zzgo().zzjl().zzbx("Using local app measurement service");
            this.zzasm = true;
            instance.bindService(context, intent, (ServiceConnection)this.zzasg.zzarz, 129);
        }
    }
    
    public final void zzlg() {
        if (this.zzasn != null && (this.zzasn.isConnected() || this.zzasn.isConnecting())) {
            this.zzasn.disconnect();
        }
        this.zzasn = null;
    }
    
    public final void zzlh() {
        this.zzasg.zzaf();
        final Context context = this.zzasg.getContext();
        synchronized (this) {
            if (this.zzasm) {
                this.zzasg.zzgo().zzjl().zzbx("Connection attempt already in progress");
                return;
            }
            if (this.zzasn != null && (!zzn.zzia() || this.zzasn.isConnecting() || this.zzasn.isConnected())) {
                this.zzasg.zzgo().zzjl().zzbx("Already awaiting connection attempt");
                return;
            }
            this.zzasn = new zzao(context, Looper.getMainLooper(), this, this);
            this.zzasg.zzgo().zzjl().zzbx("Connecting to remote service");
            this.zzasm = true;
            this.zzasn.checkAvailabilityAndConnect();
        }
    }
}
