package org.mineprogramming.horizon.innercore.view.page;

import java.util.*;
import android.view.*;
import android.content.*;

public class PagesManager
{
    private LinkedList<Page> pagesStack;
    private final ViewGroup root;
    
    public PagesManager(final ViewGroup root) {
        this.pagesStack = new LinkedList<Page>();
        this.root = root;
    }
    
    private void display(final Page page) {
        this.root.removeAllViews();
        page.display(this.root);
    }
    
    public Context getContext() {
        return this.root.getContext();
    }
    
    public Page getCurrent() {
        return this.pagesStack.peek();
    }
    
    public boolean navigateBack() {
        if (this.pagesStack.isEmpty()) {
            return false;
        }
        this.pagesStack.pop();
        final Page current = this.getCurrent();
        if (current != null) {
            this.display(current);
            return true;
        }
        return false;
    }
    
    public void push(final Page page) {
        final Page current = this.getCurrent();
        if (current != null) {
            current.pause();
        }
        this.pagesStack.push(page);
        this.root.removeAllViews();
        this.display(page);
    }
    
    public void reset(final Page page) {
        this.pagesStack.clear();
        this.push(page);
    }
}
