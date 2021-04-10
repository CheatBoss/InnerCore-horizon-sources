package bin.mt.apksignaturekillerplus;

import android.app.*;
import android.content.*;
import android.util.*;
import java.io.*;
import java.lang.reflect.*;
import android.content.pm.*;

public class HookApplication extends Application implements InvocationHandler
{
    private static final int GET_SIGNATURES = 64;
    private String appPkgName;
    private Object base;
    private byte[][] sign;
    
    public HookApplication() {
        this.appPkgName = "";
    }
    
    private void hook(final Context context) {
        try {
            final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(Base64.decode("AQAAA3AwggNsMIICVKADAgECAgROPuATMA0GCSqGSIb3DQEBBQUAMHgxCzAJBgNVBAYTAlNFMRkw\nFwYDVQQIDBBTdG9ja2hvbG1zIEzigJ5uMRIwEAYDVQQHEwlTdG9ja2hvbG0xEjAQBgNVBAoTCU1v\namFuZyBBQjESMBAGA1UECxMJTW9qYW5nIEFCMRIwEAYDVQQDEwlNb2phbmcgQUIwHhcNMTEwODA3\nMTg1NzIzWhcNMzgxMjIzMTg1NzIzWjB4MQswCQYDVQQGEwJTRTEZMBcGA1UECAwQU3RvY2tob2xt\ncyBM4oCebjESMBAGA1UEBxMJU3RvY2tob2xtMRIwEAYDVQQKEwlNb2phbmcgQUIxEjAQBgNVBAsT\nCU1vamFuZyBBQjESMBAGA1UEAxMJTW9qYW5nIEFCMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\nCgKCAQEAhD5zm6F0AVIE/N3rl8MQ1v/HAbQI59Oy/EME8RQp99hORYmnWPx2HufFQ4K4sZQYrqGq\n5pxFXvxGqpWnKLUvCtxQbTFbs3KHaoMuDYtLSjfy1XtAPB6O4nshh1OsNoTEy3rlU7TNsMFHK2Nu\nZZuZKqIWvEEIcif8A83n+i20yHcU4D7ZN1/610Uu92ccT0Ls3K4SH8oDb7LKMd6O/ahllbngdNx0\nbKv0wLQfj4vyKhzX7HACDKNuigEOeuoOFa2o33YbxqHCbnSp+kXxdgwlynR0uz/udFgm5BgCtufp\niRrPJShz7JtYIy0rk3CzWy7s4oGZZlxQVtPJx7zwDnZg9wIDAQABMA0GCSqGSIb3DQEBBQUAA4IB\nAQAbeYC/oCRcgawXwI3hV9Cr0EcIPDOX4t9+9f320t2U1LZEJuf+o3O7hwehRx4jk0FOUPTjPlld\nTSZ0Ysm2KW6FFSQw14O98ksP64m0xpvRuBF6hjCXBk+OdqUM547WkdRRqNiyh81vwXgmleUyNuPg\nUyRcylMLhBmWgO0AG2PD2RJg010ZIvJQ8h3mk9VEukO+hWOIbvR62b6wcPRcmsRxe+AGF6dzQajz\nMBtT+F1Nbep4sThFmvMipQWTuUx30ElO0ejRPmtumztSDPgXi9TEbbv6XQwe8hJJWg1+u7XEF/Jw\nSfn2NW4NLAV5a5jWr9N/qJl/2sanTavjo4fddiXG\n", 0)));
            final byte[][] sign = new byte[dataInputStream.read() & 0xFF][];
            for (int i = 0; i < sign.length; ++i) {
                dataInputStream.readFully(sign[i] = new byte[dataInputStream.readInt()]);
            }
            final Class<?> forName = Class.forName("android.app.ActivityThread");
            final Object invoke = forName.getDeclaredMethod("currentActivityThread", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            final Field declaredField = forName.getDeclaredField("sPackageManager");
            declaredField.setAccessible(true);
            final Object value = declaredField.get(invoke);
            final Class<?> forName2 = Class.forName("android.content.pm.IPackageManager");
            this.base = value;
            this.sign = sign;
            this.appPkgName = context.getPackageName();
            final Object proxyInstance = Proxy.newProxyInstance(forName2.getClassLoader(), new Class[] { forName2 }, this);
            declaredField.set(invoke, proxyInstance);
            final PackageManager packageManager = context.getPackageManager();
            final Field declaredField2 = packageManager.getClass().getDeclaredField("mPM");
            declaredField2.setAccessible(true);
            declaredField2.set(packageManager, proxyInstance);
            System.out.println("PmsHook success.");
        }
        catch (Exception ex) {
            System.err.println("PmsHook failed.");
            ex.printStackTrace();
        }
    }
    
    protected void attachBaseContext(final Context context) {
        this.hook(context);
        super.attachBaseContext(context);
    }
    
    public Object invoke(Object invoke, final Method method, final Object[] array) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            int n = 0;
            final String s = (String)array[0];
            if (((int)array[1] & 0x40) != 0x0 && this.appPkgName.equals(s)) {
                final PackageInfo packageInfo = (PackageInfo)method.invoke(this.base, array);
                packageInfo.signatures = new Signature[this.sign.length];
                while (true) {
                    invoke = packageInfo;
                    if (n >= packageInfo.signatures.length) {
                        return invoke;
                    }
                    packageInfo.signatures[n] = new Signature(this.sign[n]);
                    ++n;
                }
            }
        }
        invoke = method.invoke(this.base, array);
        return invoke;
    }
}
