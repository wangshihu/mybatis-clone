package com.huihui.mapping.sql.node;

import com.huihui.session.DynamincContext;

/**
 * Created by hadoop on 2015/7/16 0016.
 */
public abstract class AbstractSqlNode implements SqlNode{
    SqlSource sqlSource;

    public AbstractSqlNode(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    @Override
    public abstract void parsing(DynamincContext context);
}
