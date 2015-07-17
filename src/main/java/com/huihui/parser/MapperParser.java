package com.huihui.parser;

import com.huihui.exceptions.BindingException;
import com.huihui.exceptions.XMLFormatException;
import com.huihui.io.Resourse;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.ResultMap;
import com.huihui.mapping.ResultMapping;
import com.huihui.mapping.XmlBuilderAssist;
import com.huihui.mapping.sql.SqlBuildAssist;
import com.huihui.mapping.sql.SqlCommandType;
import com.huihui.mapping.sql.node.SqlNode;
import com.huihui.mapping.sql.node.SqlSource;
import com.huihui.mapping.sql.node.StaticTextSqlNode;
import com.huihui.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

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
    private SqlBuildAssist sqlBuildAssist;



    public MapperParser(Resourse resource, Configuration configuration) {
        this.resource = resource;
        this.configuration = configuration;
        initAssist();//初始化所有的帮助类。
    }

    public MapperParser(Resourse resourse) {
        this.resource = resourse;
    }

    public void parsing(){
        try {
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
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
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void initAssist(){
        sqlBuildAssist = new SqlBuildAssist(this);
    }

    private void parsingMapperChild(Element node,List<Element> idusCache) {
        String type = node.getName();

        if("insert".equals(type)||"update".equals(type)||"delete".equals(type)||"select".equals(type)){
            idusCache.add(node);
        }else if("resultMap".equals(type)){
            String id = node.attributeValue("id");
            parsingResultMap(node,id);
        }
    }

    /**
     * 解析ResultMap
     * @param node
     */
    private ResultMap parsingResultMap(Element node,String id) {
        String type = XmlBuilderAssist.getMatchType(node);
        ResultMap resultMap = new ResultMap.Builder(configuration,id,type).build();
        Iterator<Element> it = node.elementIterator();
        while (it.hasNext()){
            Element child = it.next();
            parsingResultMapChild(resultMap,child);
        }
        configuration.registryResultMap(id,resultMap);
        return resultMap;
    }
    /**
     * 解析ResultMap的子节点
     */
    private void parsingResultMapChild(ResultMap resultMap, Element node) {
        String nodeName = node.getName();
        String property = node.attributeValue("property");
        String column = node.attributeValue("column");
        ResultMap nestedResultMap = null;
        if("association".equals(nodeName)){
            resultMap.hasNestedMap();
            String id = resultMap.getId()+"_association["+property+"]";

            nestedResultMap = parsingResultMap(node,id);
        }
        ResultMapping resultMapping = new ResultMapping.Builder(property,column,nestedResultMap).builder();
        resultMap.registryResultMapping(property, resultMapping);
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

        SqlSource sqlSource = parsingSqlSource(node);
        MappedStatement statement = null;
        if("insert".equals(type)){
            statement = new MappedStatement(SqlCommandType.INSERT,parameterType,sqlSource,id);
        }else if("update".equals(type)){
            statement = new MappedStatement(SqlCommandType.UPDATE,parameterType,sqlSource,id);
        }else if("delete".equals(type)){
            statement = new MappedStatement(SqlCommandType.DELETE,parameterType,sqlSource,id);
        }else if("select".equals(type)){
            statement = new MappedStatement(SqlCommandType.SELECT,parameterType,sqlSource,id);
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

        configuration.registryMappedStatement(id, statement);
    }

    /**
     * 构建SqlSource,并不解析OGNL表达式
     * @param node
     * @return
     */
    private SqlSource parsingSqlSource(Element node) {
        SqlSource sqlSource = new SqlSource();
        List<SqlNode> contents = parsingDynSQL(sqlSource,node);
        sqlSource.setContents(contents);

        return sqlSource;
    }


    /**
     * 解析动态节点
     * @param element
     * @return
     */
    public List<SqlNode> parsingDynSQL(SqlSource sqlSource,Element element){
        List<SqlNode> contents = new ArrayList<>();
        Iterator<Node> it = element.nodeIterator();
        while(it.hasNext()){
            Node node = it.next();
            if(node.getNodeType()==Node.TEXT_NODE){
                String text = node.getText();
                if(!text.trim().equals(""))
                    contents.add(new StaticTextSqlNode(sqlSource,text));
            }else if(node.getNodeType()==Node.ELEMENT_NODE){
                Element nodeElement = (Element) node;
                String name = node.getName();
                SqlBuildAssist.SqlNodeHandler handler = sqlBuildAssist.getSqlNodeHandler(name);//获得sqlNode handler
                SqlNode sqlNode = handler.createSqlNode(nodeElement,sqlSource);//解析创建sqlNode
                contents.add(sqlNode);
            }
        }
        return contents;
    }


}
