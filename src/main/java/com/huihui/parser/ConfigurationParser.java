package com.huihui.parser;

import com.huihui.exceptions.XMLFormatException;
import com.huihui.io.Resources;
import com.huihui.io.Resourse;
import com.huihui.session.Configuration;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class ConfigurationParser {
    private InputStream resourece;
    private Configuration configuration;

    public ConfigurationParser(InputStream resourece, Configuration configuration){
        this.configuration = configuration;
        this.resourece = resourece;
    }
    public void parsing()  {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(resourece);
            Element node = document.getRootElement();
            Iterator<Element> it = node.elementIterator();
            while(it.hasNext()){
                Element child = it.next();
                ParsingConfigurationChild(child);
            }
        }catch (DocumentException e){
            e.printStackTrace();
        }

    }
    public void ParsingConfigurationChild(Element child) {
        String name = child.getName();
        if("typeAliases".equals(name)){
            parsingTypeAliases(child);
        }else if("environments".equals(name)){
            parsingEnvironments(child);
        }else if("mappers".equals(name)){
            parsingMappers(child);
        }

    }
    private void parsingTypeAliases(Element node) {
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            pasingTypeAlias(child);
        }
    }

    private void pasingTypeAlias(Element child) {
        String name = child.attributeValue("alias");
        String className = child.attributeValue("type");
        if(name==null||className==null)
            throw new XMLFormatException(resourece+" alias or type connot be null");
        configuration.registryAlias(name,className);
    }

    private void parsingEnvironments(Element node) {
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            parsingEnvironment(child);
        }
    }

    private void parsingEnvironment(Element node) {
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            if(child.getName().equals("transactionManager")){
                System.out.println("transactionManager type---"+child.attribute("type").getValue());//TODO TranctionInit
            }else if(child.getName().equals("dataSource")){
                parsingDataSource(child);
            }
        }
    }

    private void parsingDataSource(Element node) {
        String dataSourceType = node.attribute("type").getValue();
        Class<?> dataSourceClass = configuration.getAliasClass(dataSourceType.toLowerCase());
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) dataSourceClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        configuration.setDataSource(dataSource);
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            pasingDataSourcePropety(child,dataSource);
        }
    }

    private void pasingDataSourcePropety(Element node, DataSource dataSource) {
        String name =  node.attribute("name").getValue();
        String value = node.attribute("value").getValue();
        try {
            PropertyUtils.setSimpleProperty(dataSource,name,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsingMappers(Element node) {
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            parsingMapper(child);
        }
    }

    private void parsingMapper(Element node) {
        if(node.attribute("resource")!=null){//定位Resource资源
            try {
                String resourceName = node.attributeValue("resource");
                Resourse resource = new Resourse(Resources.getResourceAsStream(resourceName),resourceName);
                configuration.registryMapperSources("resource",resource);//注册加载过的Resource
                new MapperParser(resource,configuration).parsing();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




}
