package com.huihui.parser;

import com.huihui.exceptions.ExceptionFactory;
import com.huihui.exceptions.XMLFormatException;
import com.huihui.io.ResolveUtil;
import com.huihui.io.Resources;
import com.huihui.io.Resourse;
import com.huihui.session.Configuration;
import com.huihui.session.ErrorContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class ConfigurationParser {
    private InputStream resourece;
    private Configuration configuration;

    public ConfigurationParser(InputStream resourece, Configuration configuration){
        ErrorContext.instance().resource("SQL Mapper Configuration");
        this.configuration = configuration;
        this.resourece = resourece;
    }
    public void parsing()  {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(resourece);
            Element root = document.getRootElement();
            ParsingConfigurationChild(root);
        }catch (DocumentException e){
            ExceptionFactory.wrapException("error Parsing root element",e);
        }
    }
    public void ParsingConfigurationChild(Element root) {
        List<Element> typeAliasesElements = new ArrayList<>();
        List<Element> environmentsList = new ArrayList<>();
        List<Element> mappersList = new ArrayList<>();
        Iterator<Element> it = root.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            String name = child.getName();
            if("typeAliases".equals(name)){
                typeAliasesElements.add(child);
            }else if("environments".equals(name)){
               environmentsList.add(child);
            }else if("mappers".equals(name)){
                mappersList.add(child);
            }
        }
        parsingTypeAliases(typeAliasesElements);
        parsingEnvironments(environmentsList);
        parsingMappers(mappersList);
    }

    /**
     * 解析别名
     */
    private void parsingTypeAliases(List<Element> nodes) {
        for(Element node:nodes){
            Iterator<Element> it = node.elementIterator();
            while(it.hasNext()){
                Element child = it.next();
                pasingTypeAlias(child);
            }
        }

    }

    private void pasingTypeAlias(Element child) {
        String name = child.getName();
        if("package".equals(name)){//解析package
            String packageName = child.attributeValue("name");
            List<Class> classes = ResolveUtil.resolvePackage(packageName);
            for(Class type:classes){
                String alias = type.getSimpleName();
                configuration.registryAlias(alias,type);
            }
        }else if("typeAlias".equals(name)){
            String alias = child.attributeValue("alias");
            String type = child.attributeValue("type");
            if(name==null||type==null)
                throw new XMLFormatException(ErrorContext.instance()+" alias or type connot be null");
            configuration.registryAlias(alias,type);
        }

    }

    private void parsingEnvironments(List<Element> nodes) {
        for(Element node:nodes){
            Iterator<Element> it = node.elementIterator();
            while(it.hasNext()){
                Element child = it.next();
                parsingEnvironment(child);
            }
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

    private void parsingMappers(List<Element> nodes) {
        for(Element node:nodes){
            Iterator<Element> it = node.elementIterator();
            while(it.hasNext()){
                Element child = it.next();
                parsingMapper(child);
            }
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
