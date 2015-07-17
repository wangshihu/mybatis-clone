package com.huihui.mapping.sql;

import com.huihui.mapping.sql.node.*;
import com.huihui.parser.MapperParser;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class SqlBuildAssist {
    MapperParser mapperParser;
    Map<String,SqlNodeHandler> map  = new HashMap<>();

    {
        map.put("set",new SetSqlNodeHandler());
        map.put("if",new IfSqlNodeHandler());
        map.put("where",new WhereSqlNodeHandler());
        map.put("foreach",new ForEachSqlNodeHandler());

    }

    public SqlBuildAssist(MapperParser mapperParser) {
        this.mapperParser = mapperParser;
    }

    public SqlNodeHandler getSqlNodeHandler(String name) {
        return map.get(name);
    }

    public interface SqlNodeHandler{
        SqlNode createSqlNode(Element nodeElement,SqlSource sqlSource);
    }

    private class IfSqlNodeHandler implements  SqlNodeHandler{
        private IfSqlNodeHandler(){}
        @Override
        public SqlNode createSqlNode(Element element,SqlSource sqlSource) {
            String test = element.attributeValue("test");
            List<SqlNode> contents = mapperParser.parsingDynSQL(sqlSource,element);
            IfSqlNode ifSqlNode = new IfSqlNode(sqlSource,test,contents);
            return ifSqlNode;
        }
    }

    private class SetSqlNodeHandler implements  SqlNodeHandler{
        private SetSqlNodeHandler(){}
        @Override
        public SqlNode createSqlNode(Element element,SqlSource sqlSource) {
            List<SqlNode> contents = mapperParser.parsingDynSQL(sqlSource,element);
            SetSqlNode sqlNode = new SetSqlNode(sqlSource,contents);
            return sqlNode;
        }
    }

    private class WhereSqlNodeHandler implements  SqlNodeHandler{
        private WhereSqlNodeHandler(){}
        @Override
        public SqlNode createSqlNode(Element element,SqlSource sqlSource) {
            List<SqlNode> contents = mapperParser.parsingDynSQL(sqlSource,element);
            WhereSqlNode sqlNode = new WhereSqlNode(sqlSource,contents);
            return sqlNode;
        }
    }
    private class ForEachSqlNodeHandler implements SqlNodeHandler{
        private ForEachSqlNodeHandler(){}

        @Override
        public SqlNode createSqlNode(Element nodeElement,SqlSource sqlSource) {
            List<SqlNode> contents = mapperParser.parsingDynSQL(sqlSource,nodeElement);
            String collections = nodeElement.attributeValue("collection");
            String item = nodeElement.attributeValue("item");
            String separator = nodeElement.attributeValue("separator");
            String open = nodeElement.attributeValue("open");
            String close = nodeElement.attributeValue("close");
            ForEachSqlNode forEachSqlNode = new ForEachSqlNode(sqlSource,contents,collections,item,open,close,separator);
            return forEachSqlNode;
        }
    }

}
