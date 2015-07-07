package com.huihui.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class TypeHandlerRegistry {
    private final Map<JdbcType,Class<?>> JDBCTYPE_HANDLER_REGISTRY = new HashMap<>();

    private final Map<Class<?>,TypeHandler<?>> TYPE_HANDLER_REGISTRY = new HashMap<>();

    public TypeHandlerRegistry(){
        registry(JdbcType.INTEGER,new IntegerTypeHandler(),Integer.class);
        registry(JdbcType.VARCHAR,new StringTypeHandler(),String.class);
    }

    public void registry(JdbcType jdbcType,Class<?> type){
        JDBCTYPE_HANDLER_REGISTRY.put(jdbcType, type);
    }

    public void registry(Class<?>type,TypeHandler handler){
        TYPE_HANDLER_REGISTRY.put(type,handler);
    }

    public void registry(JdbcType jdbcType,TypeHandler handler,Class<?> type){
        JDBCTYPE_HANDLER_REGISTRY.put(jdbcType, type);
        TYPE_HANDLER_REGISTRY.put(type,handler);
    }

    public TypeHandler getTypeHandler(Class<?> type){
        return TYPE_HANDLER_REGISTRY.get(type);
    }

}
