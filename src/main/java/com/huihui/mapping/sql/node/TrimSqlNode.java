package com.huihui.mapping.sql.node;

import com.huihui.session.DynamincContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/16 0016.
 */
public class TrimSqlNode extends AbstractSqlNode {
    private List<SqlNode> contents;
    private String prefix = "set";
    private String suffix = "";
    private List<String> prefixesToOverride ;
    private List<String> suffixesToOverride = Arrays.asList(",");

    public TrimSqlNode(SqlSource sqlSource,List<SqlNode> contents, String prefix, List<String> prefixesToOverride, String suffix, List<String> suffixesToOverride) {
        super(sqlSource);
        this.contents = contents;
        this.prefix = prefix;
        this.prefixesToOverride = prefixesToOverride;
        this.suffix = suffix;
        this.suffixesToOverride = suffixesToOverride;
    }

    @Override
    public void parsing(DynamincContext context) {
        FilterDynmicContext filterContext = new FilterDynmicContext(context);
        for(SqlNode sqlNode:contents){
            sqlNode.parsing(filterContext);
        }
        filterContext.addAll();
    }

    class FilterDynmicContext extends DynamincContext{

        private DynamincContext delegate;
        private StringBuilder sqlBuffer;
        private boolean prefixApplied;
        private boolean suffixApplied;

        public FilterDynmicContext(DynamincContext delegate) {
            super(null);
            this.delegate = delegate;
            this.prefixApplied = false;
            this.suffixApplied = false;
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



        public void addAll() {
            sqlBuffer = new StringBuilder(sqlBuffer.toString().trim());
            String trimUpcase = sqlBuffer.toString().trim().toUpperCase();
            dealPrefix(trimUpcase);
            dealSufix(trimUpcase);
            delegate.appendSql(sqlBuffer.toString());
        }

        /**
         * 消除间隙前缀，添加where/set
         * @param trimUpcase
         */
        private void dealPrefix(String trimUpcase) {
            if(!prefixApplied){
                if(prefixesToOverride!=null){
                    for(String remove:prefixesToOverride){
                        if(trimUpcase.startsWith(remove)){
                            sqlBuffer.delete(0,remove.length());
                            break;
                        }
                    }
                }
            }
            sqlBuffer.insert(0," ");
            sqlBuffer.insert(0,prefix);
        }

        /**
         * 消除间隙后缀
         * @param trimUpcase
         */
        private void dealSufix(String trimUpcase) {
            if(!suffixApplied){
                if(suffixesToOverride!=null){
                    for(String remove:suffixesToOverride){
                        if(trimUpcase.endsWith(remove)){
                            sqlBuffer.delete(sqlBuffer.length()-remove.length(),sqlBuffer.length());
                            break;
                        }
                    }
                }
            }
            sqlBuffer.append(" ");
            sqlBuffer.append(suffix);
        }
    }
}
