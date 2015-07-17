package com.huihui.mapping.sql.node;

import com.huihui.reflection.ExpressionEvaluator;
import com.huihui.session.DynamincContext;

import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/16 0016.
 */
public class ForEachSqlNode extends AbstractSqlNode {
    private List<SqlNode> contents;
    private String collection;
    private String open;
    private String close;
    private String separator;
    private String item;
    private ExpressionEvaluator evaluator;

    public ForEachSqlNode(SqlSource sqlSource,List<SqlNode> contents, String collections, String item, String open,String close, String separator) {
        super(sqlSource);
        sqlSource.setCache(false);//有ForEach取消缓存
        this.contents = contents;
        this.collection = collections;
        this.close = close;
        this.open = open;
        this.item = item;
        this.separator = separator;
        this.evaluator = new ExpressionEvaluator();
    }


    @Override
    public void parsing(DynamincContext context) {
        Iterable iterable = evaluator.evaluateIterable(collection, context.getBindings());
        FilterDynmicContext filterContext = new FilterDynmicContext(context);

        boolean first = true;
        int i=0;
        for (Object obj : iterable) {
            if(first){
                filterContext.applyOpen();
                first = false;
            }else{
                filterContext = new FilterDynmicContext(context);
                filterContext.applySeparator();
            }
            for (SqlNode sqlNode : contents){
                sqlNode.parsing(filterContext);
            }
            dealForEachSql(filterContext,i);
            filterContext.binding("_foreach_"+i+"_"+item,obj);//添加item元素。
            i++;
            filterContext.appendAll();
        }
        filterContext.applyClose();

    }

    /**
     * 解析OGNL表达式，添加_froeach_i_item参数
     * @param filterContext
     * @param i
     */
    private void dealForEachSql(FilterDynmicContext filterContext, int i) {
        StringBuilder sqlBuild = new StringBuilder();
        String sql = filterContext.sqlBuffer.toString();
        int end = sql.indexOf("#{");
        int begin = 0;
        while(end!=-1){
            sqlBuild.append(sql.substring(begin, end+2));
            int wordEnd = sql.indexOf("}", end);
            String expression = sql.substring(end + 2, wordEnd);
            sqlBuild.append("_foreach_"+i+"_"+ expression);//替换原表达式。
            begin=wordEnd;
            end = sql.indexOf("#{",begin);
        }
        sqlBuild.append(sql.substring(begin));
        filterContext.sqlBuffer = sqlBuild;
    }


    class FilterDynmicContext extends DynamincContext {

        private DynamincContext delegate;
        private StringBuilder sqlBuffer;

        public FilterDynmicContext(DynamincContext delegate) {
            super(null);
            this.delegate = delegate;
            this.sqlBuffer = new StringBuilder();
        }

        @Override
        public void appendSql(String content) {
            sqlBuffer.append(" ");
            sqlBuffer.append(content);

        }

        @Override
        public Map<String, Object> getBindings() {
            return delegate.getBindings();
        }

        @Override
        public void binding(String name, Object value) {
            delegate.binding(name, value);
        }

        public void applyOpen() {
            if (open != null) {
                sqlBuffer.append(open);
            }
        }

        public void applySeparator() {
            if (separator != null) {
                sqlBuffer.append(separator);
            }

        }

        public void applyClose() {
            if (close != null) {
                sqlBuffer.append(close);
            }
        }

        public void appendAll() {
            delegate.appendSql(sqlBuffer.toString());
        }
    }
}
