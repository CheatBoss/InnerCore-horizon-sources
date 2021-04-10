package com.zhekasmirnov.horizon.launcher.ads;

import android.util.*;
import java.util.*;

public class AdDistributionModel
{
    public final AdsManager manager;
    private final Node root;
    
    public AdDistributionModel(final AdsManager manager) {
        this.manager = manager;
        this.root = new Node((Node)null, "root", 1.0);
    }
    
    private AdDistributionModel(final AdsManager manager, final Node root) {
        this.manager = manager;
        this.root = root;
    }
    
    public List<Node> searchAll(final String name) {
        final List<Node> result = new ArrayList<Node>();
        this.root.searchAll(name, result);
        return result;
    }
    
    public Node search(final String name) {
        return this.root.search(name);
    }
    
    public AdDomain getRandomAdDomain(final String... distributionNodes) {
        if (distributionNodes == null || distributionNodes.length <= 0) {
            return this.root.getDomainFromTreeRoot();
        }
        final Node filtered = this.root.buildFilteredTree(distributionNodes);
        if (filtered != null) {
            return filtered.getDomainFromTreeRoot();
        }
        return null;
    }
    
    public String getRootName() {
        return this.root.name;
    }
    
    public Node addDistributionNode(final String parentName, final double weight, final String name) {
        final List<Node> possibleParents = this.searchAll(parentName);
        if (possibleParents.size() == 0) {
            throw new IllegalArgumentException("parent node " + parentName + " not found");
        }
        if (possibleParents.size() > 1) {
            throw new IllegalArgumentException("parent node " + parentName + " is defined more than once");
        }
        final Node parent = possibleParents.get(0);
        if (parent.domain != null) {
            throw new IllegalArgumentException("node " + parentName + " is already containing ad domain, so it cannot be used as parent");
        }
        final Node newNode = new Node(parent, name, weight);
        parent.addChild(newNode);
        return newNode;
    }
    
    public Node addDistributionNodeFromConfig(final String parentName, final double weight, final String name, final String configName, final DomainFactory fallback) {
        final Node node = this.addDistributionNode(parentName, weight, name);
        AdDomain domain = this.manager.configuration.domainByDistributionNodeName.get(configName);
        if (domain == null) {
            domain = fallback.newDomain();
            if (domain == null) {
                throw new NullPointerException();
            }
        }
        node.setDomain(domain);
        return node;
    }
    
    public double getWeight(final String name, final double defaultWeight) {
        final Double weight = this.manager.configuration.weightByDistributionNodeName.get(name);
        return (weight != null) ? weight : defaultWeight;
    }
    
    public void removeDistributionNodes(final String name) {
        this.root.removeNodes(name);
    }
    
    public AdDistributionModel getNodeDistributionModel(final String name) {
        final List<Node> nodes = this.searchAll(name);
        if (nodes.size() == 0) {
            throw new IllegalArgumentException("node " + name + " not found");
        }
        if (nodes.size() > 1) {
            throw new IllegalArgumentException("node " + name + " is defined more than once");
        }
        return new AdDistributionModel(this.manager, nodes.get(0));
    }
    
    public class Node
    {
        private final String name;
        private final double weight;
        private AdDomain domain;
        private Node parent;
        private List<Node> children;
        private double childWeight;
        
        private Node(final Node parent, final String name, final double weight) {
            this.domain = null;
            this.children = new ArrayList<Node>();
            this.childWeight = 0.0;
            this.parent = parent;
            this.name = name;
            this.weight = weight;
        }
        
        public double getWeight() {
            return this.weight;
        }
        
        public void addChild(final Node child) {
            if (this.domain != null) {
                throw new IllegalArgumentException("node " + this.name + " cannot be used as parent");
            }
            this.childWeight += child.weight;
            this.children.add(child);
            Collections.sort(this.children, new Comparator<Node>() {
                @Override
                public int compare(final Node o1, final Node o2) {
                    return Double.compare(o2.weight, o1.weight);
                }
            });
        }
        
        private Node getRandomChild() {
            if (this.childWeight <= 0.0) {
                return null;
            }
            final double rand = Math.random() * this.childWeight;
            double weight = 0.0;
            for (final Node child : this.children) {
                weight += child.weight;
                if (weight >= rand) {
                    return child;
                }
            }
            Log.e("AdDistributionModel", "random child search failed");
            return null;
        }
        
        private AdDomain getDomainFromTreeRoot() {
            for (Node node = this; node != null; node = node.getRandomChild()) {
                if (node.domain != null) {
                    return node.domain;
                }
            }
            return null;
        }
        
        private Node search(final String name) {
            if (this.name.equals(name)) {
                return this;
            }
            for (final Node node : this.children) {
                final Node found = node.search(name);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }
        
        private void searchAll(final String name, final List<Node> result) {
            if (this.name.equals(name)) {
                result.add(this);
            }
            for (final Node node : this.children) {
                node.searchAll(name, result);
            }
        }
        
        private void removeNodes(final String name) {
            final List<Node> newChildren = new ArrayList<Node>();
            this.childWeight = 0.0;
            for (final Node child : this.children) {
                if (!name.equals(child.name)) {
                    this.childWeight += child.weight;
                    newChildren.add(child);
                    child.removeNodes(name);
                }
                else {
                    child.parent = null;
                }
            }
            this.children = newChildren;
        }
        
        private Node buildFilteredTree(final String... distributionNodes) {
            for (final String name : distributionNodes) {
                if (name.equals(this.name)) {
                    return this;
                }
            }
            final List<Node> filteredChildren = new ArrayList<Node>();
            for (final Node child : this.children) {
                final Node filtered = child.buildFilteredTree(distributionNodes);
                if (filtered != null) {
                    filteredChildren.add(filtered);
                }
            }
            if (filteredChildren.size() > 0) {
                final Node newNode = new Node(null, this.name, this.weight);
                for (final Node filtered : filteredChildren) {
                    newNode.addChild(filtered);
                }
                return newNode;
            }
            return null;
        }
        
        public void setDomain(final AdDomain domain) {
            this.domain = domain;
        }
        
        private void displayTree(final String tag, final String prefix, final double sumWeight) {
            System.out.println(tag + ": " + prefix + this.name + " " + Math.round(this.weight / sumWeight * 100.0) + "%");
            if (this.domain != null) {
                System.out.println(tag + ": " + prefix + ">> domain node: " + this.domain.providerId);
            }
            for (final Node child : this.children) {
                child.displayTree(tag, prefix + "  ", this.childWeight);
            }
        }
        
        public void displayTree(final String tag) {
            this.displayTree(tag, "", 1.0);
        }
    }
    
    public interface DomainFactory
    {
        AdDomain newDomain();
    }
}
