package org.mineprogramming.horizon.innercore.view.page;

import org.mineprogramming.horizon.innercore.*;
import android.view.*;

public abstract class Page extends Displayable
{
    private PageState pageState;
    protected PagesManager pagesManager;
    
    protected Page(final PagesManager pagesManager) {
        super(pagesManager.getContext());
        this.pageState = new PageState();
        this.pagesManager = pagesManager;
    }
    
    @Override
    public void display(final ViewGroup viewGroup) {
        this.display(viewGroup, this.pageState);
    }
    
    public void display(final ViewGroup viewGroup, final PageState pageState) {
    }
    
    public void navigateBack() {
        this.pagesManager.navigateBack();
    }
    
    public void onPause(final PageState pageState) {
    }
    
    void pause() {
        this.onPause(this.pageState);
    }
}
