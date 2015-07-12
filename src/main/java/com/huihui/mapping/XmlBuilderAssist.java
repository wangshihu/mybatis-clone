package com.huihui.mapping;

import org.dom4j.Element;

/**
 * Created by hadoop on 2015/7/12 0012.
 */
public class XmlBuilderAssist {
    public static String getMatchType(Element node){
        String type = node.attributeValue("type");
        String javaType = node.attributeValue("javaType");
        if(type!=null)
            return type;
        else
            return javaType;

    }
}
