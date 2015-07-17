package com.huihui.mapping.sql.node;

import com.huihui.mapping.sql.BoundSql;
import com.huihui.session.DynamincContext;

import java.util.List;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class SqlSource {
    private List<SqlNode> contents;
    private BoundSql boundSql;
    private boolean isCache =true;

    public SqlSource() {
    }



    public BoundSql parsing(DynamincContext context ) {
        if(boundSql!=null&&isCache)
            return boundSql;
        for(SqlNode sqlNode:contents){
            sqlNode.parsing(context);
        }
        BoundSql boundSql = parsingSQL(context.getSqlBuffer().toString());
        this.boundSql= boundSql;
        return boundSql;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean isCache) {
        this.isCache = isCache;
    }

    public void setContents(List<SqlNode> contents) {
        this.contents = contents;
    }

    /**
     * 解析SQL文本，把#{}解析成？，然后把OGNL表达式存储；
     * @param sql SQL文本
     * @return
     */
    private BoundSql parsingSQL(String sql){
        BoundSql boundSQL = new BoundSql();
        StringBuilder sqlBuild = new StringBuilder();
        int end = sql.indexOf("#{");
        int begin = 0;
        while(end!=-1){
            sqlBuild.append(sql.substring(begin, end));
            int wordEnd = sql.indexOf("}",end);
            String propery = sql.substring(end+2, wordEnd);
            boundSQL.addParameter(propery);
            sqlBuild.append("?");
            begin=wordEnd+1;
            end = sql.indexOf("#{",begin);
        }
        sqlBuild.append(sql.substring(begin));
        boundSQL.setSql(sqlBuild.toString());
        return boundSQL;
    }
}
