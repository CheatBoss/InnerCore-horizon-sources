package com.zhekasmirnov.innercore.utils;

import android.os.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.nio.*;
import java.nio.channels.*;
import com.zhekasmirnov.mcpe161.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import android.content.res.*;
import android.util.*;
import org.json.*;
import android.graphics.*;
import java.io.*;
import com.cedarsoftware.util.io.*;

public class FileTools
{
    public static String DIR_MINECRAFT;
    public static String DIR_PACK;
    public static String DIR_ROOT;
    public static String DIR_WORK;
    public static final String LOGGER_TAG = "INNERCORE-FILE";
    private static Typeface mcTypeface;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/");
        FileTools.DIR_ROOT = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(FileTools.DIR_ROOT);
        sb2.append("games/horizon/");
        FileTools.DIR_MINECRAFT = sb2.toString();
    }
    
    public static void addFileText(final String s, final String s2) throws IOException {
        final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(s, true)));
        printWriter.write(s2);
        printWriter.close();
    }
    
    public static boolean assetExists(final String s) {
        final InputStream assetInputStream = getAssetInputStream(s);
        if (assetInputStream == null) {
            return false;
        }
        try {
            assetInputStream.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public static String assureAndGetCrashDir() {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("crash-dump/");
        final String string = sb.toString();
        assureDir(string);
        return string;
    }
    
    public static boolean assureDir(final String s) {
        return exists(s) || mkdirs(s);
    }
    
    public static boolean assureFileDir(final File file) {
        final String absolutePath = file.getAbsolutePath();
        return assureDir(absolutePath.substring(0, absolutePath.lastIndexOf("/")));
    }
    
    public static Bitmap bitmapFromBytes(final byte[] array) {
        if (array != null) {
            return BitmapFactory.decodeByteArray(array, 0, array.length);
        }
        return null;
    }
    
    public static void checkdirs() {
        final File file = new File(FileTools.DIR_WORK);
        if (!file.exists()) {
            if (file.mkdirs()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("created work directory: ");
                sb.append(FileTools.DIR_WORK);
                Logger.debug("INNERCORE-FILE", sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("failed to create work directory: ");
                sb2.append(FileTools.DIR_WORK);
                Logger.debug("INNERCORE-FILE", sb2.toString());
            }
            return;
        }
        Logger.debug("INNERCORE-FILE", "work directory check successful");
    }
    
    public static byte[] convertStreamToBytes(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static void copy(final File file, final File file2) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(file);
        final FileOutputStream fileOutputStream = new FileOutputStream(file2);
        final ReadableByteChannel channel = Channels.newChannel(fileInputStream);
        final WritableByteChannel channel2 = Channels.newChannel(fileOutputStream);
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16384);
        while (channel.read(allocateDirect) != -1) {
            allocateDirect.flip();
            channel2.write(allocateDirect);
            allocateDirect.compact();
        }
        allocateDirect.flip();
        while (allocateDirect.hasRemaining()) {
            channel2.write(allocateDirect);
        }
    }
    
    public static void delete(final String s) {
        deleteRecursive(new File(s));
    }
    
    private static void deleteRecursive(final File file) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                deleteRecursive(listFiles[i]);
            }
        }
        file.delete();
    }
    
    public static boolean exists(final String s) {
        return new File(s).exists();
    }
    
    public static Bitmap getAssetAsBitmap(String decodeStream) {
        final InputStream assetInputStream = getAssetInputStream(decodeStream);
        if (assetInputStream != null) {
            decodeStream = (String)BitmapFactory.decodeStream(assetInputStream);
            try {
                assetInputStream.close();
                return (Bitmap)decodeStream;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return (Bitmap)decodeStream;
            }
        }
        return null;
    }
    
    public static JSONObject getAssetAsJSON(final String s) throws JSONException {
        return new JSONObject(getAssetAsString(s));
    }
    
    public static JSONArray getAssetAsJSONArray(final String s) throws JSONException {
        return new JSONArray(getAssetAsString(s));
    }
    
    public static String getAssetAsString(final String s) {
        return new String(getAssetBytes(s));
    }
    
    public static byte[] getAssetBytes(final String s) {
        return AssetPatch.getAssetBytes(EnvironmentSetup.getCurrentActivity().getAssets(), s);
    }
    
    public static byte[] getAssetBytes(final String s, final String[] array, final boolean b) {
        final AssetManager assets = EnvironmentSetup.getCurrentActivity().getAssets();
        if (b) {
            final byte[] assetBytes = AssetPatch.getAssetBytes(assets, s);
            if (assetBytes != null) {
                return assetBytes;
            }
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s2 = array[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append(s);
            final byte[] assetBytes2 = AssetPatch.getAssetBytes(assets, sb.toString());
            if (assetBytes2 != null) {
                return assetBytes2;
            }
        }
        return null;
    }
    
    public static InputStream getAssetInputStream(final String s) {
        return AssetPatch.getAssetInputStream(EnvironmentSetup.getCurrentActivity().getAssets(), s);
    }
    
    public static Typeface getMcTypeface() {
        if (FileTools.mcTypeface == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(FileTools.DIR_PACK);
            sb.append("assets/mc-typeface.ttf");
            final File file = new File(sb.toString());
            if (file.exists()) {
                try {
                    FileTools.mcTypeface = Typeface.createFromFile(file);
                }
                catch (Exception ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("loading typeface from file failed, unpacking: ");
                    sb2.append(ex);
                    Log.e("INNERCORE-FILE", sb2.toString());
                }
            }
            else {
                Logger.debug("INNERCORE-FILE", "unable to load typeface: assets broken");
            }
        }
        return FileTools.mcTypeface;
    }
    
    public static String getPrettyPath(final File file, final File file2) {
        String absolutePath;
        if (file == null) {
            absolutePath = "";
        }
        else {
            absolutePath = file.getAbsolutePath();
        }
        String absolutePath2;
        if (file2 == null) {
            absolutePath2 = "";
        }
        else {
            absolutePath2 = file2.getAbsolutePath();
        }
        return absolutePath2.substring(absolutePath.length() + 1);
    }
    
    public static void inStreamToOutStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final ReadableByteChannel channel = Channels.newChannel(inputStream);
        final WritableByteChannel channel2 = Channels.newChannel(outputStream);
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16384);
        while (channel.read(allocateDirect) != -1) {
            allocateDirect.flip();
            channel2.write(allocateDirect);
            allocateDirect.compact();
        }
        allocateDirect.flip();
        while (allocateDirect.hasRemaining()) {
            channel2.write(allocateDirect);
        }
    }
    
    public static void initializeDirectories(final File file) {
        final StringBuilder sb = new StringBuilder();
        sb.append(file.getAbsolutePath());
        sb.append("/");
        FileTools.DIR_PACK = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(FileTools.DIR_PACK);
        sb2.append("innercore/");
        FileTools.DIR_WORK = sb2.toString();
        checkdirs();
    }
    
    public static String[] listAssets(final String s) {
        return AssetPatch.listAssets(EnvironmentSetup.getCurrentActivity().getAssets(), s);
    }
    
    public static String[] listDirectory(final String s) {
        return new File(s).list();
    }
    
    public static boolean mkdirs(final String s) {
        return new File(s).mkdirs();
    }
    
    public static Bitmap readFileAsBitmap(final String s) {
        return BitmapFactory.decodeFile(s);
    }
    
    public static String readFileText(String string) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(string)));
        string = "";
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(line);
            sb.append("\n");
            string = sb.toString();
        }
        bufferedReader.close();
        return string;
    }
    
    public static JSONObject readJSON(final String s) throws IOException, JSONException {
        return new JSONObject(readFileText(s));
    }
    
    public static JSONArray readJSONArray(final String s) throws IOException, JSONException {
        return new JSONArray(new JSONTokener(readFileText(s)));
    }
    
    public static File unpackAsset(final String s, final String s2) throws IOException {
        return unpackInputStream(AssetPatch.getAssetInputStream(EnvironmentSetup.getCurrentActivity().getAssets(), s), s2);
    }
    
    public static void unpackAssetDir(final String s, final String s2) {
    }
    
    public static File unpackInputStream(final InputStream inputStream, final String s) throws IOException {
        final File file = new File(s);
        file.createNewFile();
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        final byte[] array = new byte[1024];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            fileOutputStream.write(array, 0, read);
        }
        fileOutputStream.close();
        inputStream.close();
        return file;
    }
    
    public static File unpackResource(final int n, String unpackInputStream) throws IOException {
        final InputStream openRawResource = EnvironmentSetup.getCurrentActivity().getResources().openRawResource(n);
        unpackInputStream = (String)unpackInputStream(openRawResource, unpackInputStream);
        try {
            openRawResource.close();
            return (File)unpackInputStream;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return (File)unpackInputStream;
        }
    }
    
    public static void writeBitmap(final String s, final Bitmap bitmap) {
        try {
            bitmap.compress(Bitmap$CompressFormat.PNG, 100, (OutputStream)new FileOutputStream(s));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void writeFileText(final String s, final String s2) throws IOException {
        final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(s, false)));
        printWriter.write(s2);
        printWriter.close();
    }
    
    public static void writeJSON(final String s, final JSONArray jsonArray) throws IOException {
        writeFileText(s, JsonWriter.formatJson(jsonArray.toString()));
    }
    
    public static void writeJSON(final String s, final JSONObject jsonObject) throws IOException {
        writeFileText(s, JsonWriter.formatJson(jsonObject.toString()));
    }
}
