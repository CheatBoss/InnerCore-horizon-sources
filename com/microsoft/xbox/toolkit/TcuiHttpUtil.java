package com.microsoft.xbox.toolkit;

import java.util.concurrent.atomic.*;
import android.util.*;
import com.microsoft.xbox.idp.util.*;
import java.io.*;
import java.util.*;

public class TcuiHttpUtil
{
    public static String getResponseBodySync(final HttpCall httpCall) throws XLEException {
        final AtomicReference<Pair> atomicReference = new AtomicReference<Pair>();
        atomicReference.set(new Pair((Object)false, (Object)null));
        httpCall.getResponseAsync((HttpCall.Callback)new HttpCall.Callback() {
            @Override
            public void processResponse(final int n, final InputStream inputStream, HttpHeaders value) throws Exception {
                value = (HttpHeaders)Boolean.valueOf(true);
                if (n >= 200) {
                    if (n <= 299) {
                        String string;
                        try {
                            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 4096);
                            final StringBuilder sb = new StringBuilder();
                            while (true) {
                                final String line = bufferedReader.readLine();
                                if (line == null) {
                                    break;
                                }
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append(line);
                                sb2.append("\n");
                                sb.append(sb2.toString());
                            }
                            string = sb.toString();
                        }
                        catch (IOException ex) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Failed to read ShortCircuitProfileMessage string - ");
                            sb3.append(ex.getMessage());
                            XLEAssert.assertTrue(sb3.toString(), false);
                            string = null;
                        }
                        synchronized (atomicReference) {
                            atomicReference.set(new Pair((Object)value, (Object)string));
                            atomicReference.notify();
                            return;
                        }
                    }
                }
                synchronized (atomicReference) {
                    atomicReference.set(new Pair((Object)value, (Object)null));
                    atomicReference.notify();
                }
            }
        });
        // monitorenter(atomicReference)
        try {
            try {
                while (!(boolean)atomicReference.get().first) {
                    atomicReference.wait();
                }
            }
            finally {
                // monitorexit(atomicReference)
                // monitorexit(atomicReference)
                return (String)atomicReference.get().second;
            }
        }
        catch (InterruptedException ex) {}
    }
    
    public static <T> T getResponseSync(final HttpCall httpCall, final Class<T> clazz) throws XLEException {
        final AtomicReference<Pair> atomicReference = new AtomicReference<Pair>();
        atomicReference.set(new Pair((Object)false, (Object)null));
        httpCall.getResponseAsync((HttpCall.Callback)new HttpCall.Callback() {
            @Override
            public void processResponse(final int n, final InputStream inputStream, final HttpHeaders httpHeaders) throws Exception {
                Object deserializeJson;
                if (n < 200 && n > 299) {
                    deserializeJson = null;
                }
                else {
                    deserializeJson = GsonUtil.deserializeJson(inputStream, (Class<Object>)clazz);
                }
                synchronized (atomicReference) {
                    atomicReference.set(new Pair((Object)true, deserializeJson));
                    atomicReference.notify();
                }
            }
        });
        // monitorenter(atomicReference)
        try {
            try {
                while (!(boolean)atomicReference.get().first) {
                    atomicReference.wait();
                }
            }
            finally {
                // monitorexit(atomicReference)
                // monitorexit(atomicReference)
                return (T)atomicReference.get().second;
            }
        }
        catch (InterruptedException ex) {}
    }
    
    public static boolean getResponseSyncSucceeded(final HttpCall httpCall, final List<Integer> list) {
        final AtomicReference<Boolean> atomicReference = new AtomicReference<Boolean>();
        httpCall.getResponseAsync((HttpCall.Callback)new HttpCall.Callback() {
            @Override
            public void processResponse(final int n, final InputStream inputStream, final HttpHeaders httpHeaders) throws Exception {
                synchronized (atomicReference) {
                    atomicReference.set(list.contains(n));
                    atomicReference.notify();
                }
            }
        });
        // monitorenter(atomicReference)
        try {
            try {
                while (atomicReference.get() == null) {
                    atomicReference.wait();
                }
            }
            finally {
                // monitorexit(atomicReference)
                // monitorexit(atomicReference)
                return atomicReference.get();
            }
        }
        catch (InterruptedException ex) {}
    }
    
    public static <T> void throwIfNullOrFalse(final T t) throws XLEException {
        if (t != null) {
            return;
        }
        if (Boolean.getBoolean(t.toString())) {
            return;
        }
        throw new XLEException(2L);
    }
}
