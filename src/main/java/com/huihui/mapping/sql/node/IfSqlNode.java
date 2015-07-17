package com.huihui.mapping.sql.node;

import com.huihui.reflection.ExpressionEvaluator;
import com.huihui.session.DynamincContext;

import java.util.List;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class IfSqlNode extends AbstractSqlNode {
    private String test;
    private List<SqlNode> contents;
    private ExpressionEvaluator evaluator;
    public IfSqlNode(SqlSource sqlSource,String test, List<SqlNode> contents) {
        super(sqlSource);
        this.test = test;
        this.contents = contents;
        this.evaluator = new ExpressionEvaluator();
    }

    @Override
    public void parsing(DynamincContext context) {
        if(evaluator.evaluateBoolean(test,context.getBindings())){
            for(SqlNode sqlNode:contents){
                sqlNode.parsing(context);
            }
        }
    }



}
