package com.huihui.xmlParsing;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by hadoop on 2015/5/5 0005.
 */
public class Dom4jTest {
    @Test
    public void test() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream("com/huihui/xmlParsing/Dom4jTest.xml"));
        Element node = document.getRootElement();
        ParsingConfiguration(node);
    }

    public void ParsingConfiguration(Element node){
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            ParsingConfigurationChild(child);
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

    private void parsingMappers(Element node) {
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            parsingMapper(child);
        }
    }

    private void parsingMapper(Element child) {
        System.out.println("resource------"+child.attribute("resource").getValue());
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
                System.out.println("transactionManager type---"+child.attribute("type").getValue());
            }else if(child.getName().equals("dataSource")){
                parsingDataSource(child);
            }
        }
    }

    private void parsingDataSource(Element node) {
        System.out.println("dataSource type = "+node.attribute("type").getValue());
        Iterator<Element> it = node.elementIterator();
        while(it.hasNext()){
            Element child = it.next();
            System.out.println("property---"+child.attribute("name").getValue()+"----"+child.attribute("value").getValue());
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
        System.out.println("TypeAlias---"+ child.attribute("alias").getValue()+"-----type:"+child.attribute("type").getValue());
    }


}
