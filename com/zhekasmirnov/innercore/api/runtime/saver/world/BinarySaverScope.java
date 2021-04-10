package com.zhekasmirnov.innercore.api.runtime.saver.world;

import org.json.*;
import java.util.*;
import java.io.*;

public abstract class BinarySaverScope implements SaverScope
{
    public abstract void read(final InputStream p0);
    
    @Override
    public void readJson(Object o) throws Exception {
        final String optString = ((JSONObject)o).optString("data");
        if (optString == null) {
            throw new IOException("missing binary data for BinarySaverScope");
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(optString));
        o = null;
        while (true) {
            try {
                try {
                    this.read(byteArrayInputStream);
                    if (byteArrayInputStream != null) {
                        byteArrayInputStream.close();
                    }
                    return;
                }
                finally {
                    if (byteArrayInputStream != null) {
                        if (o != null) {
                            final ByteArrayInputStream byteArrayInputStream2 = byteArrayInputStream;
                            byteArrayInputStream2.close();
                        }
                        else {
                            byteArrayInputStream.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final ByteArrayInputStream byteArrayInputStream2 = byteArrayInputStream;
                byteArrayInputStream2.close();
                continue;
            }
            catch (Throwable t2) {}
            break;
        }
    }
    
    public abstract void save(final OutputStream p0);
    
    @Override
    public Object saveAsJson() throws Exception {
        final JSONObject jsonObject = new JSONObject();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Object o = null;
        while (true) {
            try {
                try {
                    this.save(byteArrayOutputStream);
                    o = o;
                    jsonObject.put("data", (Object)Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    return jsonObject;
                }
                finally {
                    if (byteArrayOutputStream != null) {
                        if (o != null) {
                            final ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                            byteArrayOutputStream2.close();
                        }
                        else {
                            byteArrayOutputStream.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                byteArrayOutputStream2.close();
                continue;
            }
            catch (Throwable t2) {}
            break;
        }
    }
}
