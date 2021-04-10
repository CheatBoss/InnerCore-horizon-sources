package com.zhekasmirnov.horizon.modloader;

import com.zhekasmirnov.horizon.modloader.library.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.*;

public class LaunchSequence
{
    private final ExecutionDirectory directory;
    private final List<LibraryDirectory> libraries;
    private final List<JavaLibrary> javaLibraries;
    private List<LibraryTreeNode> sequence;
    
    public LaunchSequence(final ExecutionDirectory directory, final List<LibraryDirectory> libraries, final List<JavaLibrary> javaLibraries) {
        this.sequence = new ArrayList<LibraryTreeNode>();
        this.directory = directory;
        this.libraries = libraries;
        this.javaLibraries = javaLibraries;
    }
    
    private void resolveDependencies(final List<LibraryTreeNode> nodes, final List<LibraryTreeNode> sequence, final LibraryTreeNode node, final EventLogger logger) {
        nodes.remove(node);
        for (final LibraryTreeNode dependency : node.getDependencies()) {
            if (nodes.contains(dependency)) {
                this.resolveDependencies(nodes, sequence, dependency, logger);
            }
            else {
                if (sequence.contains(dependency)) {
                    continue;
                }
                logger.fault("BUILD", "Failed to resolve dependency " + dependency.getName() + " of " + node.getName() + " due to cyclic dependency. Loading order for this two will be reversed");
            }
        }
        sequence.add(node);
    }
    
    public void buildSequence(final EventLogger logger) {
        final HashMap<LibraryDirectory, LibraryTreeNode> nodes = new HashMap<LibraryDirectory, LibraryTreeNode>();
        for (final LibraryDirectory library : this.libraries) {
            nodes.put(library, new LibraryTreeNode(library));
        }
        for (final LibraryTreeNode node : nodes.values()) {
            for (final String dependencyName : node.library.getDependencyNames()) {
                final LibraryDirectory dependency = this.directory.getLibByName(dependencyName);
                if (dependency != null) {
                    final LibraryTreeNode dependencyNode = nodes.get(dependency);
                    if (dependencyNode != null) {
                        if (dependencyNode != node) {
                            node.addDependency(dependencyNode);
                        }
                        else {
                            logger.fault("BUILD", "Self-dependency for " + dependency.getName() + " detected and will not be added");
                        }
                    }
                    else {
                        logger.fault("BUILD", "Assertion error occurred during dependency resolving: failed to find node for directory " + dependency.directory);
                    }
                }
            }
        }
        final List<LibraryTreeNode> nodeList = new ArrayList<LibraryTreeNode>(nodes.values());
        final List<LibraryTreeNode> sequence = new ArrayList<LibraryTreeNode>();
        while (nodeList.size() > 0) {
            this.resolveDependencies(nodeList, sequence, nodeList.get(0), logger);
        }
        this.sequence.clear();
        this.sequence.addAll(sequence);
    }
    
    public void loadAll(final EventLogger logger) {
        for (final LibraryTreeNode node : this.sequence) {
            final LibraryDirectory library = node.library;
            try {
                library.loadExecutableFile();
            }
            catch (Throwable err) {
                logger.fault("LOAD", "failed to load library " + library.getName(), err);
            }
        }
        for (final JavaLibrary javaLibrary : this.javaLibraries) {
            if (!javaLibrary.isInitialized()) {
                try {
                    javaLibrary.initialize();
                }
                catch (Throwable err2) {
                    logger.fault("LOAD", "failed to load java library " + javaLibrary.getDirectory(), err2);
                }
            }
        }
    }
    
    public List<LibraryDirectory> getAllLibraries() {
        return this.libraries;
    }
    
    private class LibraryTreeNode
    {
        public final LibraryDirectory library;
        private List<LibraryTreeNode> dependencies;
        
        public LibraryTreeNode(final LibraryDirectory library) {
            this.dependencies = new ArrayList<LibraryTreeNode>();
            this.library = library;
        }
        
        public void addDependency(final LibraryTreeNode node) {
            this.dependencies.add(node);
        }
        
        public List<LibraryTreeNode> getDependencies() {
            return this.dependencies;
        }
        
        public String getName() {
            return this.library.getName();
        }
    }
}
