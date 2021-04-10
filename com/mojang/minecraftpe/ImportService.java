package com.mojang.minecraftpe;

import android.app.*;
import android.preference.*;
import android.os.*;
import android.content.pm.*;
import android.content.*;

public class ImportService extends Service
{
    static final int MSG_CORRELATION_CHECK = 672;
    static final int MSG_CORRELATION_RESPONSE = 837;
    final Messenger mMessenger;
    
    public ImportService() {
        this.mMessenger = new Messenger((Handler)new IncomingHandler());
    }
    
    public IBinder onBind(final Intent intent) {
        return this.mMessenger.getBinder();
    }
    
    class IncomingHandler extends Handler
    {
        public void handleMessage(final Message message) {
            if (message.what != 672) {
                super.handleMessage(message);
                return;
            }
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ImportService.this.getApplicationContext());
            final String string = defaultSharedPreferences.getString("deviceId", "?");
            final String string2 = defaultSharedPreferences.getString("LastDeviceSessionId", "");
            if (string == "?") {
                return;
            }
            try {
                final long firstInstallTime = ImportService.this.getPackageManager().getPackageInfo(ImportService.this.getApplicationContext().getPackageName(), 0).firstInstallTime;
                final Bundle data = new Bundle();
                data.putLong("time", firstInstallTime);
                data.putString("deviceId", string);
                data.putString("sessionId", string2);
                final Message obtain = Message.obtain((Handler)null, 837);
                obtain.setData(data);
                try {
                    message.replyTo.send(obtain);
                }
                catch (RemoteException ex) {}
            }
            catch (PackageManager$NameNotFoundException ex2) {}
        }
    }
}
