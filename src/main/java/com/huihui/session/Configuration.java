package com.huihui.session;


import com.huihui.binding.MapperProxyFactory;
import com.huihui.binding.MapperRegistry;
import com.huihui.exceptions.BindingException;
import com.huihui.io.Resources;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.ResultMap;
import com.huihui.type.TypeHandler;
import com.huihui.type.TypeHandlerRegistry;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class Configuration {
    private DataSource dataSource;
    //别名注册器
    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    //类型解析注册器
    private TypeHandlerRegistry handlerRegistry = new TypeHandlerRegistry();
    //映射statement注册器
    private Map<String,MappedStatement> mappedStatementRegistry = new HashMap<>();
    //加载过的资源
    private Map<String,String> loadMapperReources = new HashMap<>();
    //映射的类型（命名空间）
    private MapperRegistry mapperRegistry = new MapperRegistry(this);
    //映射的ResultMap
    private Map<String,ResultMap> resultMapRegistry = new HashMap<>();

    public Configuration(){

    }

    /**
     * 注册MapperedStatement
     * @param name
     * @param statement
     */
    public void registryMappedStatement(String name,MappedStatement statement){mappedStatementRegistry.put(name,statement);}

    public MappedStatement getMapperStatement(String name){
        return mappedStatementRegistry.get(name);
    }

    /**
     * 注册加载过的Resource;
     * @param type
     * @param resource
     */
    public void registryMapperSources(String type ,String resource){
        loadMapperReources.put(type,resource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void registryAlias(String name,String className){
        typeAliasRegistry.registryAlias(name, className);
    }

    public Class<?> getAliasClass(String name){
        return typeAliasRegistry.getAliasClass(name);
    }

    public void registryMapperFactory(String className, InputStream resource){
        Class<?> clazz = null;
        try {
            clazz = Resources.classForName(className);
        } catch (ClassNotFoundException e) {
            throw new BindingException("connot found interface for "+resource+" namespace");
        }
        MapperProxyFactory mapperProxyFactory = new MapperProxyFactory(clazz);
        mapperRegistry.registry(clazz,mapperProxyFactory);
    }


    public  <T> T getMapper(Class<T> type,SqlSession session) {
        return mapperRegistry.getMapper(type,session);
    }

    public void registryResultMap(String id,ResultMap resultMap){
        resultMapRegistry.put(id,resultMap);
    }

    public TypeHandler getTypeHandler(Class<? extends Object> type) {

        return handlerRegistry.getTypeHandler(type);
    }
}
