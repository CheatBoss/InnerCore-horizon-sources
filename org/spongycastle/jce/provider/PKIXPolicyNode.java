package org.spongycastle.jce.provider;

import java.security.cert.*;
import java.util.*;

public class PKIXPolicyNode implements PolicyNode
{
    protected List children;
    protected boolean critical;
    protected int depth;
    protected Set expectedPolicies;
    protected PolicyNode parent;
    protected Set policyQualifiers;
    protected String validPolicy;
    
    public PKIXPolicyNode(final List children, final int depth, final Set expectedPolicies, final PolicyNode parent, final Set policyQualifiers, final String validPolicy, final boolean critical) {
        this.children = children;
        this.depth = depth;
        this.expectedPolicies = expectedPolicies;
        this.parent = parent;
        this.policyQualifiers = policyQualifiers;
        this.validPolicy = validPolicy;
        this.critical = critical;
    }
    
    public void addChild(final PKIXPolicyNode pkixPolicyNode) {
        this.children.add(pkixPolicyNode);
        pkixPolicyNode.setParent(this);
    }
    
    public Object clone() {
        return this.copy();
    }
    
    public PKIXPolicyNode copy() {
        final HashSet<String> set = new HashSet<String>();
        final Iterator<String> iterator = this.expectedPolicies.iterator();
        while (iterator.hasNext()) {
            set.add(new String(iterator.next()));
        }
        final HashSet<String> set2 = new HashSet<String>();
        final Iterator<String> iterator2 = this.policyQualifiers.iterator();
        while (iterator2.hasNext()) {
            set2.add(new String(iterator2.next()));
        }
        final PKIXPolicyNode parent = new PKIXPolicyNode(new ArrayList(), this.depth, set, null, set2, new String(this.validPolicy), this.critical);
        final Iterator<PKIXPolicyNode> iterator3 = this.children.iterator();
        while (iterator3.hasNext()) {
            final PKIXPolicyNode copy = iterator3.next().copy();
            copy.setParent(parent);
            parent.addChild(copy);
        }
        return parent;
    }
    
    @Override
    public Iterator getChildren() {
        return this.children.iterator();
    }
    
    @Override
    public int getDepth() {
        return this.depth;
    }
    
    @Override
    public Set getExpectedPolicies() {
        return this.expectedPolicies;
    }
    
    @Override
    public PolicyNode getParent() {
        return this.parent;
    }
    
    @Override
    public Set getPolicyQualifiers() {
        return this.policyQualifiers;
    }
    
    @Override
    public String getValidPolicy() {
        return this.validPolicy;
    }
    
    public boolean hasChildren() {
        return this.children.isEmpty() ^ true;
    }
    
    @Override
    public boolean isCritical() {
        return this.critical;
    }
    
    public void removeChild(final PKIXPolicyNode pkixPolicyNode) {
        this.children.remove(pkixPolicyNode);
    }
    
    public void setCritical(final boolean critical) {
        this.critical = critical;
    }
    
    public void setExpectedPolicies(final Set expectedPolicies) {
        this.expectedPolicies = expectedPolicies;
    }
    
    public void setParent(final PKIXPolicyNode parent) {
        this.parent = parent;
    }
    
    @Override
    public String toString() {
        return this.toString("");
    }
    
    public String toString(final String s) {
        final StringBuffer sb = new StringBuffer();
        sb.append(s);
        sb.append(this.validPolicy);
        sb.append(" {\n");
        for (int i = 0; i < this.children.size(); ++i) {
            final PKIXPolicyNode pkixPolicyNode = this.children.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("    ");
            sb.append(pkixPolicyNode.toString(sb2.toString()));
        }
        sb.append(s);
        sb.append("}\n");
        return sb.toString();
    }
}
