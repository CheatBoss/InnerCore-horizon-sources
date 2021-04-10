package com.microsoft.xbox.idp.interop;

import android.content.*;
import android.util.*;
import java.io.*;
import com.google.firebase.iid.*;
import com.google.android.gms.tasks.*;
import java.util.*;

public class Interop
{
    private static final String DNET_SCOPE = "open-user.auth.dnet.xboxlive.com";
    private static final String PACKAGE_NAME_TO_REMOVE = "com.microsoft.onlineid.sample";
    private static final String POLICY = "mbi_ssl";
    private static final String PROD_SCOPE = "open-user.auth.xboxlive.com";
    private static final String TAG;
    private static Context s_context;
    
    static {
        TAG = Interop.class.getSimpleName();
    }
    
    public static String GetLocalStoragePath(final Context context) {
        return context.getFilesDir().getPath();
    }
    
    public static void NotificationRegisterCallback(final String s) {
        final String tag = Interop.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("NotificationRegisterCallback, token:");
        sb.append(s);
        Log.i(tag, sb.toString());
        notificiation_registration_callback(s);
    }
    
    public static String ReadConfigFile(Context s_context) {
        Interop.s_context = s_context;
        final InputStream openRawResource = s_context.getResources().openRawResource(s_context.getResources().getIdentifier("xboxservices", "raw", s_context.getPackageName()));
        s_context = (Context)new ByteArrayOutputStream();
        final byte[] array = new byte[1024];
        try {
            while (true) {
                final int read = openRawResource.read(array);
                if (read == -1) {
                    break;
                }
                ((ByteArrayOutputStream)s_context).write(array, 0, read);
            }
            ((ByteArrayOutputStream)s_context).close();
            openRawResource.close();
        }
        catch (IOException ex) {}
        return ((ByteArrayOutputStream)s_context).toString();
    }
    
    public static void RegisterWithGNS(final Context context) {
        Log.i("XSAPI.Android", "trying to register..");
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((OnSuccessListener)new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(final InstanceIdResult instanceIdResult) {
                    final String access$000 = Interop.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Got Firebase id:");
                    sb.append(instanceIdResult.getId());
                    Log.d(access$000, sb.toString());
                    final String access$2 = Interop.TAG;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Got Firebase token:");
                    sb2.append(instanceIdResult.getToken());
                    Log.d(access$2, sb2.toString());
                    Interop.NotificationRegisterCallback(instanceIdResult.getToken());
                }
            }).addOnFailureListener((OnFailureListener)new OnFailureListener() {
                @Override
                public void onFailure(final Exception ex) {
                    final String access$000 = Interop.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Gettting Firebase token failed, message:");
                    sb.append(ex.getMessage());
                    Log.d(access$000, sb.toString());
                }
            });
        }
        catch (Exception ex) {
            final String tag = Interop.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Gettting Firebase instance failed, message:");
            sb.append(ex.getMessage());
            Log.e(tag, sb.toString());
        }
    }
    
    public static native boolean deinitializeInterop();
    
    public static Context getApplicationContext() {
        return Interop.s_context;
    }
    
    public static String getLocale() {
        final String string = Locale.getDefault().toString();
        final String tag = Interop.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("locale is: ");
        sb.append(string);
        Log.i(tag, sb.toString());
        return string;
    }
    
    public static String getSystemProxy() {
        final String property = System.getProperty("http.proxyHost");
        if (property != null) {
            final String property2 = System.getProperty("http.proxyPort");
            if (property2 != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("http://");
                sb.append(property);
                sb.append(":");
                sb.append(property2);
                final String string = sb.toString();
                Log.i(Interop.TAG, string);
                return string;
            }
        }
        return "";
    }
    
    public static native boolean initializeInterop(final Context p0);
    
    private static native void notificiation_registration_callback(final String p0);
    
    public enum AuthFlowScreenStatus
    {
        ERROR_USER_CANCEL(1), 
        NO_ERROR(0), 
        PROVIDER_ERROR(2);
        
        private final int id;
        
        private AuthFlowScreenStatus(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
    
    public interface ErrorCallback
    {
        void onError(final int p0, final int p1, final String p2);
    }
    
    public enum ErrorStatus
    {
        CLOSE(1), 
        TRY_AGAIN(0);
        
        private final int id;
        
        private ErrorStatus(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
    
    public enum ErrorType
    {
        BAN(0), 
        CATCHALL(3), 
        CREATION(1), 
        OFFLINE(2);
        
        private final int id;
        
        private ErrorType(final int id) {
            this.id = id;
        }
        
        public int getId() {
            return this.id;
        }
    }
    
    public interface EventInitializationCallback extends ErrorCallback
    {
        void onSuccess();
    }
}
