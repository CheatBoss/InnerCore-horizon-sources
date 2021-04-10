package com.microsoft.xbox.toolkit;

import java.util.*;
import java.io.*;
import com.microsoft.xbox.toolkit.system.*;
import com.microsoft.xboxtcui.*;

public class XLEFileCacheManager
{
    public static XLEFileCache emptyFileCache;
    private static HashMap<String, XLEFileCache> sAllCaches;
    private static HashMap<XLEFileCache, File> sCacheRootDirMap;
    
    static {
        XLEFileCacheManager.sAllCaches = new HashMap<String, XLEFileCache>();
        XLEFileCacheManager.sCacheRootDirMap = new HashMap<XLEFileCache, File>();
        XLEFileCacheManager.emptyFileCache = new XLEFileCache();
    }
    
    public static XLEFileCache createCache(final String s, final int n) {
        synchronized (XLEFileCacheManager.class) {
            return createCache(s, n, true);
        }
    }
    
    public static XLEFileCache createCache(final String s, final int n, final boolean b) {
        // monitorenter(XLEFileCacheManager.class)
        Label_0175: {
            if (n <= 0) {
                break Label_0175;
            }
            Label_0165: {
                if (s == null) {
                    break Label_0165;
                }
                try {
                    if (s.length() > 0) {
                        final XLEFileCache xleFileCache = XLEFileCacheManager.sAllCaches.get(s);
                        XLEFileCache xleFileCache3;
                        if (xleFileCache == null) {
                            if (!b || !SystemUtil.isSDCardAvailable()) {
                                return XLEFileCacheManager.emptyFileCache;
                            }
                            final XLEFileCache xleFileCache2 = new XLEFileCache(s, n);
                            final File file = new File(XboxTcuiSdk.getActivity().getCacheDir(), s);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            xleFileCache2.size = file.list().length;
                            XLEFileCacheManager.sAllCaches.put(s, xleFileCache2);
                            XLEFileCacheManager.sCacheRootDirMap.put(xleFileCache2, file);
                            xleFileCache3 = xleFileCache2;
                        }
                        else {
                            if (xleFileCache.maxFileNumber != n) {
                                throw new IllegalArgumentException("The same subDirectory with different maxFileNumber already exist.");
                            }
                            xleFileCache3 = xleFileCache;
                        }
                        return xleFileCache3;
                    }
                    throw new IllegalArgumentException("subDirectory must be not null and at least one character length");
                    throw new IllegalArgumentException("maxFileNumber must be > 0");
                }
                finally {
                }
                // monitorexit(XLEFileCacheManager.class)
            }
        }
    }
    
    static File getCacheRootDir(final XLEFileCache xleFileCache) {
        return XLEFileCacheManager.sCacheRootDirMap.get(xleFileCache);
    }
    
    public static String getCacheStatus() {
        return XLEFileCacheManager.sAllCaches.values().toString();
    }
}
