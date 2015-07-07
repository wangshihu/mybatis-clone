package com.huihui.binding;

import com.huihui.session.Configuration;
import com.huihui.session.SqlSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hadoop on 2015/7/4 0004.
 */
public class MapperRegistry {
    private Map<Class,MapperProxyFactory> kownsMap = new ConcurrentHashMap<>();
    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public void registry(Class<?> type,MapperProxyFactory mapperProxyFactory){
        kownsMap.put(type,mapperProxyFactory);
    }

    public <T> T getMapper(Class<T> type,SqlSession session) {
        MapperProxyFactory factory = kownsMap.get(type);
        return (T) factory.newInstance(session);

    }
}
