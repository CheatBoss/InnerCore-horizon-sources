package com.zhekasmirnov.horizon.util;

import android.graphics.*;
import org.json.*;
import java.nio.*;
import java.nio.channels.*;
import android.content.res.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import com.zhekasmirnov.horizon.activity.util.*;
import android.util.*;
import java.io.*;
import java.util.zip.*;
import java.util.*;

public class FileUtils
{
    public static String readFileText(final File file) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final StringBuilder text = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        return text.toString();
    }
    
    public static void writeFileText(final File path, final String text) throws IOException {
        final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, false)));
        writer.write(text);
        writer.close();
    }
    
    public static void addFileText(final File path, final String text) throws IOException {
        final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
        writer.write(text);
        writer.close();
    }
    
    public static String convertStreamToString(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
    
    public static Bitmap readFileAsBitmap(final String path) {
        return BitmapFactory.decodeFile(path);
    }
    
    public static void writeBitmap(final String path, final Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)out);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static JSONObject readJSON(final File path) throws IOException, JSONException {
        return new JSONObject(readFileText(path));
    }
    
    public static JSONArray readJSONArray(final File path) throws IOException, JSONException {
        return new JSONArray(new JSONTokener(readFileText(path)));
    }
    
    public static void writeJSON(final File path, final JSONObject json) throws IOException {
        final String result = json.toString();
        writeFileText(path, result);
    }
    
    public static void writeJSON(final File path, final JSONArray json) throws IOException {
        final String result = json.toString();
        writeFileText(path, result);
    }
    
    public static void copy(final File src, final File dst) throws IOException {
        final FileInputStream inputStream = new FileInputStream(src);
        final FileOutputStream outputStream = new FileOutputStream(dst);
        final ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
        final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16384);
        while (inputChannel.read(buffer) != -1) {
            buffer.flip();
            outputChannel.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            outputChannel.write(buffer);
        }
    }
    
    public static File unpackInputStream(final InputStream inputStream, final File outputFile, final boolean closeInput) throws IOException {
        final OutputStream outputStream = new FileOutputStream(outputFile);
        int read = 0;
        final byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, read);
        }
        outputStream.close();
        if (closeInput) {
            inputStream.close();
        }
        return outputFile;
    }
    
    public static File unpackInputStream(final InputStream inputStream, final File outputFile) throws IOException {
        return unpackInputStream(inputStream, outputFile, true);
    }
    
    public static void unpackAssetOrDirectory(final AssetManager assets, final File path, final String name) throws IOException {
        final String[] names = assets.list(name);
        if (names != null && names.length > 0) {
            path.mkdirs();
            for (final String child : names) {
                unpackAssetOrDirectory(assets, new File(path, child), new File(name, child).getPath());
            }
        }
        else {
            unpackInputStream(AssetPatch.getAssetInputStream(assets, name), path);
        }
    }
    
    public static String readStringFromAsset(final AssetManager assets, final String name) throws IOException {
        final StringBuilder buf = new StringBuilder();
        final InputStream is = AssetPatch.getAssetInputStream(assets, name);
        if (is == null) {
            throw new IOException("null");
        }
        final BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String str;
        while ((str = in.readLine()) != null) {
            buf.append(str).append("\n");
        }
        in.close();
        return buf.toString();
    }
    
    public static JSONObject readJSONFromAssets(final AssetManager assets, final String name) throws IOException, JSONException {
        return new JSONObject(readStringFromAsset(assets, name));
    }
    
    public static void clearFileTree(final File file, final boolean selfDelete) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (final File child : file.listFiles()) {
                    clearFileTree(child, true);
                }
            }
            if (selfDelete) {
                file.delete();
            }
        }
    }
    
    public static void copyFileTree(final File srcDir, final File dstDir, final DialogHelper.ProgressDialogHolder dialog, final String prefix) throws IOException {
        if (!dstDir.isDirectory() && !dstDir.mkdirs()) {
            throw new IOException("mkdirs failed: " + dstDir);
        }
        for (final String name : srcDir.list()) {
            if (dialog != null && dialog.isTerminated()) {
                return;
            }
            final File srcFile = new File(srcDir, name);
            final File dstFile = new File(dstDir, name);
            if (srcFile.isDirectory()) {
                copyFileTree(srcFile, dstFile, dialog, prefix + name + "/");
            }
            else {
                if (dialog != null) {
                    dialog.onDownloadMessage(name);
                }
                copy(srcFile, dstFile);
            }
        }
    }
    
    private static void debugFileTree(final File file, final String prefix) {
        if (file.exists()) {
            System.out.println(prefix + " |-" + file.getName());
            if (file.isDirectory()) {
                for (final File child : file.listFiles()) {
                    debugFileTree(child, prefix + "  ");
                }
            }
        }
        else {
            System.out.println(prefix + "| file does not exist: " + file);
        }
    }
    
    public static void debugFileTree(final File file) {
        debugFileTree(file, "");
    }
    
    public static String cleanupPath(String path) {
        for (path = path.replaceAll("\\\\", "/"); path.startsWith("/"); path = path.substring(1)) {}
        return path;
    }
    
    private static void deleteFileTree(final File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (final File child : file.listFiles()) {
                    deleteFileTree(child);
                }
            }
            file.delete();
        }
    }
    
    private static void getAllRelativePaths(final File file, final File directoryBase, final List<String> result, final boolean addDirectories) {
        if (addDirectories || file.isFile()) {
            result.add(cleanupPath(file.getAbsolutePath().substring(directoryBase.getAbsolutePath().length())));
        }
        if (file.isDirectory()) {
            for (final File child : file.listFiles()) {
                getAllRelativePaths(child, directoryBase, result, addDirectories);
            }
        }
    }
    
    public static List<String> getAllRelativePaths(final File directory, final boolean addDirectories) {
        final List<String> result = new ArrayList<String>();
        getAllRelativePaths(directory, directory, result, addDirectories);
        return result;
    }
    
    public static boolean zipDirectory(final File directory, final File zipFileName) throws IOException {
        final List<String> relativePaths = getAllRelativePaths(directory, false);
        if (relativePaths.size() == 0) {
            return false;
        }
        final int BUFFER = 4096;
        BufferedInputStream origin = null;
        final FileOutputStream dest = new FileOutputStream(zipFileName);
        final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        final byte[] data = new byte[4096];
        for (final String fileName : relativePaths) {
            final File _file = new File(directory, fileName);
            Log.v("Compress", "Adding: " + _file);
            final FileInputStream fi = new FileInputStream(_file);
            origin = new BufferedInputStream(fi, 4096);
            final ZipEntry entry = new ZipEntry(fileName);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, 4096)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
        return true;
    }
    
    public static boolean getFileFlag(final File directory, final String name) {
        return new File(directory, "." + name).exists();
    }
    
    public static void setFileFlag(final File directory, final String name, final boolean exists) {
        final File flag = new File(directory, "." + name);
        if (exists) {
            if (flag.exists()) {
                return;
            }
            try {
                flag.createNewFile();
                return;
            }
            catch (IOException e) {
                throw new RuntimeException("failed to set flag: " + flag, e);
            }
        }
        flag.delete();
    }
}
