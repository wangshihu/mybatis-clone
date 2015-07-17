package com.huihui.mapping.sql.node;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hadoop on 2015/7/16 0016.
 */
public class WhereSqlNode extends TrimSqlNode {
    private static List<String> prefixList = Arrays.asList("AND ","OR ","AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");
    public WhereSqlNode(SqlSource sqlSource,List<SqlNode> contents) {
        super(sqlSource,contents, "WHERE", prefixList, "", null);
    }
}
