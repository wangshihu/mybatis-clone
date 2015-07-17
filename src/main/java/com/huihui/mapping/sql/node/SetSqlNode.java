package com.huihui.mapping.sql.node;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class SetSqlNode extends TrimSqlNode {
    public SetSqlNode(SqlSource sqlSource,List<SqlNode> contents) {
        super(sqlSource,contents, "SET", null, "", Arrays.asList(","));
    }
}
