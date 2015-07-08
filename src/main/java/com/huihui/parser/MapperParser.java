package com.huihui.parser;

import com.huihui.exceptions.BindingException;
import com.huihui.exceptions.XMLFormatException;
import com.huihui.io.Resourse;
import com.huihui.mapping.BoundSQLSource;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.ResultMap;
import com.huihui.mapping.SqlCommandType;
import com.huihui.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class MapperParser {
    private Resourse resource;
    private String nameSpace;
    private Configuration configuration;
    public MapperParser() {
    }



    public MapperParser(Resourse resource, Configuration configuration) {
        this.resource = resource;
        this.configuration = configuration;
    }

    public MapperParser(Resourse resourse) {
        this.resource = resourse;
    }

    public void parsing(){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(resource.getStream());
            Element node = document.getRootElement();
            nameSpace = node.attributeValue("namespace");
            //注册MapperProxyFactroy
            configuration.registryMapperFactory(nameSpace,resource);
            if(nameSpace==null)
                throw new XMLFormatException(resource.toString()+" namespace cannot be null");
            List<Element> idusCache = new ArrayList<>();//缓存Insert,update等，保证ResultMap先加载;
            Iterator<Element> it = node.elementIterator();
            while(it.hasNext()){
                Element child = it.next();
                parsingMapperChild(child,idusCache);
            }
            for(Element child:idusCache){
                parsingIdus(child);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void parsingMapperChild(Element node,List<Element> idusCache) {
        String type = node.getName();

        if("insert".equals(type)||"update".equals(type)||"delete".equals(type)||"select".equals(type)){
            idusCache.add(node);
        }else if("resultMap".equals(type)){
            parsingResultMap(node);
        }
    }

    /**
     * 解析ResultMap
     * @param node
     */
    private void parsingResultMap(Element node) {
        String type = node.attributeValue("type");
        String id = node.attributeValue("id");
        if(id==null)
            throw new BindingException(resource+" resultMap id connot be null");
        Class<?> clazz = configuration.getAliasClass(type);
        if(clazz==null)
            throw new BindingException(resource+" "+type+" cannot be found");
        ResultMap resultMap = new ResultMap(id,clazz);
        configuration.registryResultMap(id,resultMap);
        Iterator<Element> it = node.elementIterator();
        while (it.hasNext()){
            Element child = it.next();
            parsingResultMapChild(resultMap,child);
        }
    }
    /**
     * 解析ResultMap的子节点
     */
    private void parsingResultMapChild(ResultMap resultMap, Element node) {
        String nodeName = node.getName();
        if("result".equals(nodeName)||"id".equals(nodeName)){
            String property = node.attributeValue("property");
            String column = node.attributeValue("column");
            if(property==null||column==null)
                throw new BindingException(resource+" "+resultMap.getId()+" resultMap colum or property cannot be null ");
            resultMap.registryResultMapping(property,column);
        }
    }


    /**
     * 解析Insert,update,delete,select
     * @param node
     */
    public void parsingIdus(Element node){
        String type = node.getName();
        String idStr = node.attributeValue("id");

        if(idStr==null)
            throw new XMLFormatException(resource.toString()+" id cannot be null");

        Class<?> parameterType = configuration.getAliasClass(node.attributeValue("parameterType"));



        String id = nameSpace+"."+idStr;
        BoundSQLSource boundSQL = parsingSQL(node.getText().trim());
        MappedStatement statement = null;
        if("insert".equals(type)){
            statement = new MappedStatement(SqlCommandType.INSERT,parameterType,boundSQL,id);
        }else if("update".equals(type)){
            statement = new MappedStatement(SqlCommandType.UPDATE,parameterType,boundSQL,id);
        }else if("delete".equals(type)){
            statement = new MappedStatement(SqlCommandType.DELETE,parameterType,boundSQL,id);
        }else if("select".equals(type)){
            statement = new MappedStatement(SqlCommandType.SELECT,parameterType,boundSQL,id);
        }
        Class<?> resultType = configuration.getAliasClass(node.attributeValue("resultType"));
        ResultMap resultMap = configuration.getResultMap(node.attributeValue("resultMap"));
        if((resultType!=null&&resultMap!=null)&&(resultMap==null||resultType==null))
            throw new BindingException(resource+" resultMap and resultType cannot be exist or null together");
        if(resultMap!=null){
            statement.setResultMap(resultMap);

        }else {
            statement.setResultType(resultType);
        }

        configuration.registryMappedStatement(id,statement);
    }

    /**
     * 解析SQL文本，把#{}解析成？，然后把OGNL表达式存储；
     * @param sql SQL文本
     * @return
     */
    private BoundSQLSource parsingSQL(String sql){
        BoundSQLSource boundSQL = new BoundSQLSource();
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
