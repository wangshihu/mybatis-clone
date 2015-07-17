package com.huihui.mapping.sql.node;

import com.huihui.session.DynamincContext;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public interface SqlNode {
    void parsing(DynamincContext context);
}
