package com.zhekasmirnov.horizon.compiler.packages;

import java.util.*;
import com.zhekasmirnov.horizon.compiler.util.*;
import org.w3c.dom.*;
import android.support.annotation.*;

public class RepoParser
{
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_PACKAGE_NAME = "name";
    public static final String KEY_LOCAL_FILE_NAME = "file";
    public static final String KEY_SIZE = "size";
    public static final String KEY_FILESIZE = "filesize";
    public static final String KEY_VERSION = "version";
    public static final String KEY_DESC = "description";
    public static final String KEY_DEPENDS = "depends";
    public static final String KEY_ARCH = "arch";
    public static final String KEY_REPLACES = "replaces";
    public static final String KEY_STATUS = "status";
    private static final String TAG = "RepoParser";
    private static final boolean DEBUG = true;
    
    @NonNull
    public List<PackageInfo> parseRepoXml(String repo) {
        repo = RepoUtils.replaceMacro(repo);
        final ArrayList<PackageInfo> list = new ArrayList<PackageInfo>();
        if (repo != null) {
            final XMLParser parser = new XMLParser();
            final Document doc = parser.getDomElement(repo);
            if (doc == null) {
                return list;
            }
            final NodeList nl = doc.getElementsByTagName("package");
            for (int i = 0; i < nl.getLength(); ++i) {
                final Element e = (Element)nl.item(i);
                System.out.println("RepoParser pkg [ " + parser.getValue(e, "name") + " ][ " + parser.getValue(e, "size") + "]");
                int size;
                if (parser.getValue(e, "size").length() > 0) {
                    size = Integer.valueOf(parser.getValue(e, "size").replaceAll("@SIZE@", "0"));
                }
                else {
                    size = 0;
                }
                int filesize;
                if (parser.getValue(e, "filesize").length() > 0) {
                    filesize = Integer.valueOf(parser.getValue(e, "filesize").replaceAll("@SIZE@", "0"));
                }
                else {
                    filesize = size;
                }
                if (RepoUtils.isContainsPackage(list, parser.getValue(e, "name"), parser.getValue(e, "version"))) {
                    System.out.println("RepoParserskip exists pkg" + parser.getValue(e, "name"));
                }
                else {
                    final PackageInfo packageInfo = new PackageInfo(parser.getValue(e, "name"), parser.getValue(e, "file"), size, filesize, parser.getValue(e, "version"), parser.getValue(e, "description"), parser.getValue(e, "depends"), parser.getValue(e, "arch"), parser.getValue(e, "replaces"));
                    list.add(packageInfo);
                    System.out.println("RepoParser added pkg = " + packageInfo.getName());
                }
            }
        }
        return list;
    }
}
