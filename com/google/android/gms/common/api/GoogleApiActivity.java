package com.google.android.gms.common.api;

import android.app.*;
import com.google.android.gms.common.api.internal.*;
import android.os.*;
import android.util.*;
import android.content.*;
import com.google.android.gms.common.*;

public class GoogleApiActivity extends Activity implements DialogInterface$OnCancelListener
{
    private int zzct;
    
    public GoogleApiActivity() {
        this.zzct = 0;
    }
    
    public static PendingIntent zza(final Context context, final PendingIntent pendingIntent, final int n) {
        return PendingIntent.getActivity(context, 0, zza(context, pendingIntent, n, true), 134217728);
    }
    
    public static Intent zza(final Context context, final PendingIntent pendingIntent, final int n, final boolean b) {
        final Intent intent = new Intent(context, (Class)GoogleApiActivity.class);
        intent.putExtra("pending_intent", (Parcelable)pendingIntent);
        intent.putExtra("failing_client_id", n);
        intent.putExtra("notify_manager", b);
        return intent;
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 1) {
            final boolean booleanExtra = this.getIntent().getBooleanExtra("notify_manager", true);
            this.zzct = 0;
            this.setResult(n2, intent);
            if (booleanExtra) {
                final GoogleApiManager zzb = GoogleApiManager.zzb((Context)this);
                if (n2 != -1) {
                    if (n2 == 0) {
                        zzb.zza(new ConnectionResult(13, null), this.getIntent().getIntExtra("failing_client_id", -1));
                    }
                }
                else {
                    zzb.zzr();
                }
            }
        }
        else if (n == 2) {
            this.zzct = 0;
            this.setResult(n2, intent);
        }
        this.finish();
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        this.setResult(this.zzct = 0);
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzct = bundle.getInt("resolution");
        }
        if (this.zzct != 1) {
            final Bundle extras = this.getIntent().getExtras();
            Label_0119: {
                String s;
                if (extras == null) {
                    s = "Activity started without extras";
                }
                else {
                    final PendingIntent pendingIntent = (PendingIntent)extras.get("pending_intent");
                    final Integer n = (Integer)extras.get("error_code");
                    if (pendingIntent == null && n == null) {
                        s = "Activity started without resolution";
                    }
                    else {
                        if (pendingIntent != null) {
                            try {
                                this.startIntentSenderForResult(pendingIntent.getIntentSender(), 1, (Intent)null, 0, 0, 0);
                                this.zzct = 1;
                                return;
                            }
                            catch (IntentSender$SendIntentException ex) {
                                Log.e("GoogleApiActivity", "Failed to launch pendingIntent", (Throwable)ex);
                            }
                            break Label_0119;
                        }
                        GoogleApiAvailability.getInstance().showErrorDialogFragment(this, n, 2, (DialogInterface$OnCancelListener)this);
                        this.zzct = 1;
                        return;
                    }
                }
                Log.e("GoogleApiActivity", s);
            }
            this.finish();
        }
    }
    
    protected void onSaveInstanceState(final Bundle bundle) {
        bundle.putInt("resolution", this.zzct);
        super.onSaveInstanceState(bundle);
    }
}
