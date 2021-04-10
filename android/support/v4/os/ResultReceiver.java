package android.support.v4.os;

import android.os.*;

public class ResultReceiver implements Parcelable
{
    public static final Parcelable$Creator<ResultReceiver> CREATOR;
    final Handler mHandler;
    final boolean mLocal;
    IResultReceiver mReceiver;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ResultReceiver>() {
            public ResultReceiver createFromParcel(final Parcel parcel) {
                return new ResultReceiver(parcel);
            }
            
            public ResultReceiver[] newArray(final int n) {
                return new ResultReceiver[n];
            }
        };
    }
    
    public ResultReceiver(final Handler mHandler) {
        this.mLocal = true;
        this.mHandler = mHandler;
    }
    
    ResultReceiver(final Parcel parcel) {
        this.mLocal = false;
        this.mHandler = null;
        this.mReceiver = IResultReceiver.Stub.asInterface(parcel.readStrongBinder());
    }
    
    public int describeContents() {
        return 0;
    }
    
    protected void onReceiveResult(final int n, final Bundle bundle) {
    }
    
    public void send(final int n, final Bundle bundle) {
        if (!this.mLocal) {
            if (this.mReceiver != null) {
                try {
                    this.mReceiver.send(n, bundle);
                }
                catch (RemoteException ex) {}
            }
            return;
        }
        if (this.mHandler != null) {
            this.mHandler.post((Runnable)new MyRunnable(n, bundle));
            return;
        }
        this.onReceiveResult(n, bundle);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        synchronized (this) {
            if (this.mReceiver == null) {
                this.mReceiver = new MyResultReceiver();
            }
            parcel.writeStrongBinder(this.mReceiver.asBinder());
        }
    }
    
    class MyResultReceiver extends Stub
    {
        public void send(final int n, final Bundle bundle) {
            if (ResultReceiver.this.mHandler != null) {
                ResultReceiver.this.mHandler.post((Runnable)new MyRunnable(n, bundle));
                return;
            }
            ResultReceiver.this.onReceiveResult(n, bundle);
        }
    }
    
    class MyRunnable implements Runnable
    {
        final int mResultCode;
        final Bundle mResultData;
        
        MyRunnable(final int mResultCode, final Bundle mResultData) {
            this.mResultCode = mResultCode;
            this.mResultData = mResultData;
        }
        
        @Override
        public void run() {
            ResultReceiver.this.onReceiveResult(this.mResultCode, this.mResultData);
        }
    }
}
