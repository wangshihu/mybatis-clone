package com.huihui.session;

import com.huihui.dataSource.pooled.PooledDataSource;
import com.huihui.io.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class TypeAliasRegistry{
    private final Map<String,Class> ALIASMAP = new HashMap<String, Class>();

     {
        registryAlias("hashmap",HashMap.class);
        registryAlias("pooled",PooledDataSource.class);
        registryAlias("void",Void.class);
        registryAlias(null,Void.class);
    }

    public  void registryAlias(String name,Class<?> clazz){
        ALIASMAP.put(name, clazz);
    }

    public void registryAlias(String name,String className){
        try {
            Class<?> clazz = Resources.classForName(className);
            ALIASMAP.put(name, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Class<?> getAliasClass(String name){
        return ALIASMAP.get(name);
    }

}
