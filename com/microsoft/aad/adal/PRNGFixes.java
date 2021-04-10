package com.microsoft.aad.adal;

import java.lang.reflect.*;
import android.os.*;
import java.security.*;
import java.io.*;

final class PRNGFixes
{
    private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL;
    private static final int ONE_KB = 1024;
    private static final String TAG = "PRNGFixes";
    private static final int VERSION_CODE_JELLY_BEAN = 16;
    private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;
    
    static {
        BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
    }
    
    private PRNGFixes() {
    }
    
    public static void apply() {
        applyOpenSSLFix();
        installLinuxPRNGSecureRandom();
    }
    
    private static void applyOpenSSLFix() throws SecurityException {
        if (Build$VERSION.SDK_INT >= 16) {
            if (Build$VERSION.SDK_INT <= 18) {
                try {
                    Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", byte[].class).invoke(null, generateSeed());
                    final int intValue = (int)Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_load_file", String.class, Long.TYPE).invoke(null, "/dev/urandom", 1024);
                    if (intValue == 1024) {
                        return;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected number of bytes read from Linux PRNG: ");
                    sb.append(intValue);
                    throw new IOException(sb.toString());
                }
                catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ex3) {
                    final InvocationTargetException ex2;
                    final InvocationTargetException ex = ex2;
                    Logger.e("PRNGFixes:applyOpenSSLFix", "Failed to seed OpenSSL PRNG. ", "", ADALError.DEVICE_PRNG_FIX_ERROR, ex);
                    throw new SecurityException("Failed to seed OpenSSL PRNG", ex);
                }
            }
        }
        Logger.v("PRNGFixes:applyOpenSSLFix", "No need to apply the OpenSSL fix.");
    }
    
    private static byte[] generateSeed() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(System.currentTimeMillis());
            dataOutputStream.writeLong(System.nanoTime());
            dataOutputStream.writeInt(Process.myPid());
            dataOutputStream.writeInt(Process.myUid());
            dataOutputStream.write(PRNGFixes.BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
            dataOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            throw new SecurityException("Failed to generate seed", ex);
        }
    }
    
    private static byte[] getBuildFingerprintAndDeviceSerial() {
        final StringBuilder sb = new StringBuilder();
        final String fingerprint = Build.FINGERPRINT;
        if (fingerprint != null) {
            sb.append(fingerprint);
        }
        final String deviceSerialNumber = getDeviceSerialNumber();
        if (deviceSerialNumber != null) {
            sb.append(deviceSerialNumber);
        }
        try {
            return sb.toString().getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 encoding not supported", ex);
        }
    }
    
    private static String getDeviceSerialNumber() {
        try {
            return (String)Build.class.getField("SERIAL").get(null);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private static void installLinuxPRNGSecureRandom() throws SecurityException {
        if (Build$VERSION.SDK_INT > 18) {
            Logger.v("PRNGFixes:installLinuxPRNGSecureRandom", "No need to apply the fix.");
            return;
        }
        final Provider[] providers = Security.getProviders("SecureRandom.SHA1PRNG");
        if (providers == null || providers.length < 1 || !LinuxPRNGSecureRandomProvider.class.equals(providers[0].getClass())) {
            Logger.v("PRNGFixes:installLinuxPRNGSecureRandom", "Insert provider as LinuxPRNGSecureRandomProvider.");
            Security.insertProviderAt(new LinuxPRNGSecureRandomProvider(), 1);
        }
        final SecureRandom secureRandom = new SecureRandom();
        final StringBuilder sb = new StringBuilder();
        sb.append("Provider: ");
        sb.append(secureRandom.getProvider().getClass().getName());
        Logger.i("PRNGFixes:installLinuxPRNGSecureRandom", "LinuxPRNGSecureRandomProvider for SecureRandom. ", sb.toString());
        try {
            final SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Provider: ");
            sb2.append(instance.getProvider().getClass().getName());
            Logger.i("PRNGFixes:installLinuxPRNGSecureRandom", "LinuxPRNGSecureRandomProvider for SecureRandom with alg SHA1PRNG. ", sb2.toString());
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.v("PRNGFixes:installLinuxPRNGSecureRandom", "SHA1PRNG not available.");
            throw new SecurityException("SHA1PRNG not available", ex);
        }
    }
    
    public static class LinuxPRNGSecureRandom extends SecureRandomSpi
    {
        private static final Object SLOCK;
        private static final File URANDOM_FILE;
        private static DataInputStream sUrandomIn;
        private static OutputStream sUrandomOut;
        private static final long serialVersionUID = 1L;
        private boolean mSeeded;
        
        static {
            URANDOM_FILE = new File("/dev/urandom");
            SLOCK = new Object();
        }
        
        private DataInputStream getUrandomInputStream() {
            synchronized (LinuxPRNGSecureRandom.SLOCK) {
                if (LinuxPRNGSecureRandom.sUrandomIn == null) {
                    try {
                        LinuxPRNGSecureRandom.sUrandomIn = new DataInputStream(new FileInputStream(LinuxPRNGSecureRandom.URANDOM_FILE));
                    }
                    catch (IOException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to open ");
                        sb.append(LinuxPRNGSecureRandom.URANDOM_FILE);
                        sb.append(" for reading");
                        throw new SecurityException(sb.toString(), ex);
                    }
                }
                return LinuxPRNGSecureRandom.sUrandomIn;
            }
        }
        
        private OutputStream getUrandomOutputStream() throws IOException {
            synchronized (LinuxPRNGSecureRandom.SLOCK) {
                if (LinuxPRNGSecureRandom.sUrandomOut == null) {
                    LinuxPRNGSecureRandom.sUrandomOut = new FileOutputStream(LinuxPRNGSecureRandom.URANDOM_FILE);
                }
                return LinuxPRNGSecureRandom.sUrandomOut;
            }
        }
        
        @Override
        protected byte[] engineGenerateSeed(final int n) {
            final byte[] array = new byte[n];
            this.engineNextBytes(array);
            return array;
        }
        
        @Override
        protected void engineNextBytes(final byte[] array) {
            if (!this.mSeeded) {
                this.engineSetSeed(generateSeed());
            }
            Object slock = null;
            DataInputStream urandomInputStream = null;
            try {
                try {
                    urandomInputStream = this.getUrandomInputStream();
                    try {
                        slock = LinuxPRNGSecureRandom.SLOCK;
                        synchronized (slock) {
                            urandomInputStream.readFully(array);
                            // monitorexit(slock)
                            if (urandomInputStream != null) {
                                try {
                                    urandomInputStream.close();
                                }
                                catch (IOException ex) {
                                    slock = new StringBuilder();
                                    ((StringBuilder)slock).append("Failed to close the input stream to \"/dev/urandom\" . Exception: ");
                                    ((StringBuilder)slock).append(ex.toString());
                                    Logger.v("PRNGFixesengineNextBytes", ((StringBuilder)slock).toString());
                                }
                            }
                            return;
                        }
                    }
                    catch (IOException slock) {}
                    finally {
                        slock = urandomInputStream;
                    }
                }
                finally {}
            }
            catch (IOException urandomInputStream) {}
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to read from ");
            sb.append(LinuxPRNGSecureRandom.URANDOM_FILE);
            throw new SecurityException(sb.toString(), (Throwable)urandomInputStream);
            if (slock != null) {
                try {
                    ((DataInputStream)slock).close();
                }
                catch (IOException ex2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to close the input stream to \"/dev/urandom\" . Exception: ");
                    sb2.append(ex2.toString());
                    Logger.v("PRNGFixesengineNextBytes", sb2.toString());
                }
            }
        }
        
        @Override
        protected void engineSetSeed(final byte[] array) {
            final StringBuilder sb = null;
            Object o = null;
            StringBuilder sb3;
            try {
                try {
                    final OutputStream urandomOutputStream = this.getUrandomOutputStream();
                    try {
                        urandomOutputStream.write(array);
                        urandomOutputStream.flush();
                        this.mSeeded = true;
                        if (urandomOutputStream == null) {
                            return;
                        }
                        try {
                            urandomOutputStream.close();
                            return;
                        }
                        catch (IOException o) {
                            final StringBuilder sb2 = new StringBuilder();
                        }
                    }
                    catch (IOException ex2) {}
                    finally {
                        o = urandomOutputStream;
                    }
                }
                finally {}
            }
            catch (IOException ex3) {
                sb3 = sb;
            }
            final String simpleName = PRNGFixes.class.getSimpleName();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to mix seed into ");
            sb4.append(LinuxPRNGSecureRandom.URANDOM_FILE);
            Logger.w(simpleName, sb4.toString());
            this.mSeeded = true;
            if (sb3 == null) {
                return;
            }
            try {
                ((OutputStream)sb3).close();
                return;
            }
            catch (IOException o) {
                sb3 = new StringBuilder();
            }
            sb3.append("Failed to close the output stream to \"/dev/urandom\" . Exception: ");
            sb3.append(((Throwable)o).toString());
            Logger.v("PRNGFixesengineSetSeed", sb3.toString());
            return;
            this.mSeeded = true;
            if (o != null) {
                try {
                    ((OutputStream)o).close();
                }
                catch (IOException ex) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("Failed to close the output stream to \"/dev/urandom\" . Exception: ");
                    sb5.append(ex.toString());
                    Logger.v("PRNGFixesengineSetSeed", sb5.toString());
                }
            }
        }
    }
    
    private static class LinuxPRNGSecureRandomProvider extends Provider
    {
        private static final long serialVersionUID = 1L;
        
        public LinuxPRNGSecureRandomProvider() {
            super("LinuxPRNG", 1.0, "A Linux-specific random number provider that uses /dev/urandom");
            this.put("SecureRandom.SHA1PRNG", LinuxPRNGSecureRandom.class.getName());
            this.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
