package com.bumptech.glide.manager;

import com.bumptech.glide.*;
import java.util.*;

final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode
{
    @Override
    public Set<RequestManager> getDescendants() {
        return Collections.emptySet();
    }
}
