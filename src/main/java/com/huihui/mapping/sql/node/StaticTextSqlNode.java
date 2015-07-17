package com.huihui.mapping.sql.node;

import com.huihui.session.DynamincContext;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class StaticTextSqlNode extends AbstractSqlNode{
    String content;

    public StaticTextSqlNode(SqlSource sqlSource,String content) {
        super(sqlSource);
        this.content = content;
    }

    @Override
    public void parsing(DynamincContext context) {
        context.appendSql(content);

    }

}
