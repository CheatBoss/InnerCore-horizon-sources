package com.bumptech.glide.manager;

import java.util.*;
import com.bumptech.glide.*;

public interface RequestManagerTreeNode
{
    Set<RequestManager> getDescendants();
}
