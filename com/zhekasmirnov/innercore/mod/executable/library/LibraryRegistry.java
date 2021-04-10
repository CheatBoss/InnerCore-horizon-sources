package com.zhekasmirnov.innercore.mod.executable.library;

import com.zhekasmirnov.innercore.mod.build.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.ui.*;
import java.util.*;

public class LibraryRegistry
{
    private static final List<Library> allLibraries;
    private static final List<Library> builtInLibraries;
    private static final Map<Mod, ArrayList<Library>> libraryMap;
    
    static {
        allLibraries = new ArrayList<Library>();
        builtInLibraries = new ArrayList<Library>();
        libraryMap = new HashMap<Mod, ArrayList<Library>>();
    }
    
    public static void addBuiltInLibrary(final Library library) {
        LibraryRegistry.allLibraries.add(library);
        LibraryRegistry.builtInLibraries.add(library);
    }
    
    public static void addLibrary(final Library library) {
        final Mod parentMod = library.getParentMod();
        if (LibraryRegistry.libraryMap.containsKey(parentMod)) {
            LibraryRegistry.libraryMap.get(parentMod).add(library);
        }
        else {
            final ArrayList<Library> list = new ArrayList<Library>();
            list.add(library);
            LibraryRegistry.libraryMap.put(parentMod, list);
        }
        LibraryRegistry.allLibraries.add(library);
    }
    
    public static void importLibrary(final Scriptable scriptable, final LibraryDependency libraryDependency, final String s) {
        final Library resolveDependencyAndLoadLib = resolveDependencyAndLoadLib(libraryDependency);
        if (resolveDependencyAndLoadLib != null) {
            importLibraryInternal(scriptable, resolveDependencyAndLoadLib, libraryDependency, s);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to import library ");
        sb.append(libraryDependency);
        sb.append(", it does not exist or failed to load");
        ICLog.i("ERROR", sb.toString());
    }
    
    private static void importLibraryInternal(final Scriptable scriptable, final Library library, final LibraryDependency libraryDependency, final String s) {
        if (s.equals("*")) {
            final Iterator<String> iterator = library.getExportNames().iterator();
            while (iterator.hasNext()) {
                importLibraryInternal(scriptable, library, libraryDependency, iterator.next());
            }
            return;
        }
        final LibraryExport exportForDependency = library.getExportForDependency(libraryDependency, s);
        if (exportForDependency != null) {
            scriptable.put(exportForDependency.name, scriptable, exportForDependency.value);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to import value ");
        sb.append(s);
        sb.append(" from ");
        sb.append(libraryDependency);
        sb.append(", library does not have value with this name to import");
        ICLog.i("ERROR", sb.toString());
    }
    
    public static void loadAllBuiltInLibraries() {
    }
    
    public static void prepareAllLibraries() {
        for (final Library library : LibraryRegistry.allLibraries) {
            if (!library.isInvalid() && !library.isPrepared()) {
                library.initialize();
                if (library.isInitialized()) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("library failed to initialize for some reason: ");
                sb.append(library.getLibName());
                ICLog.i("ERROR", sb.toString());
            }
        }
    }
    
    public static Library resolveDependency(final LibraryDependency libraryDependency) {
        final Library resolveLocalDependency = resolveLocalDependency(libraryDependency);
        Library resolveSharedDependency;
        if (resolveLocalDependency != null) {
            resolveSharedDependency = resolveLocalDependency;
            if (resolveLocalDependency.isShared()) {
                return resolveSharedDependency(libraryDependency);
            }
        }
        else {
            resolveSharedDependency = resolveSharedDependency(libraryDependency);
        }
        return resolveSharedDependency;
    }
    
    public static Library resolveDependencyAndLoadLib(final LibraryDependency libraryDependency) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Resolving dependency: ");
        sb.append(libraryDependency);
        LoadingUI.setTip(sb.toString());
        final Library resolveDependency = resolveDependency(libraryDependency);
        if (resolveDependency != null) {
            if (!resolveDependency.isLoaded()) {
                if (resolveDependency.isLoadingInProgress()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("DEPENDENCY RECURSION DETECTED! it may be caused by recursive library dependencies or recursive imports, recursion detected at: ");
                    sb2.append(libraryDependency);
                    ICLog.i("ERROR", sb2.toString());
                    return null;
                }
                resolveDependency.load();
            }
            if (resolveDependency.isInvalid()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("incorrectly loaded library found for dependency ");
                sb3.append(libraryDependency);
                sb3.append(" searching other matches");
                ICLog.i("ERROR", sb3.toString());
                return resolveDependencyAndLoadLib(libraryDependency);
            }
        }
        LoadingUI.setTip("");
        return resolveDependency;
    }
    
    private static Library resolveDependencyInList(final Collection<Library> collection, final LibraryDependency libraryDependency) {
        final Library library = null;
        final Iterator<Library> iterator = collection.iterator();
        Library library2 = library;
        while (iterator.hasNext()) {
            final Library library3 = iterator.next();
            Library library4 = library2;
            Label_0071: {
                if (!library3.isInvalid()) {
                    library4 = library2;
                    if (libraryDependency.isMatchesLib(library3)) {
                        if (library2 != null) {
                            library4 = library2;
                            if (library2.getVersionCode() >= library3.getVersionCode()) {
                                break Label_0071;
                            }
                        }
                        library4 = library3;
                    }
                }
            }
            library2 = library4;
        }
        return library2;
    }
    
    private static Library resolveLocalDependency(final LibraryDependency libraryDependency) {
        final Mod parentMod = libraryDependency.getParentMod();
        if (parentMod != null) {
            final ArrayList<Library> list = new ArrayList<Library>(LibraryRegistry.builtInLibraries);
            final ArrayList<Library> list2 = LibraryRegistry.libraryMap.get(parentMod);
            if (list2 != null) {
                list.addAll(list2);
            }
            return resolveDependencyInList(list, libraryDependency);
        }
        return null;
    }
    
    private static Library resolveSharedDependency(final LibraryDependency libraryDependency) {
        return resolveDependencyInList(LibraryRegistry.allLibraries, libraryDependency);
    }
}
