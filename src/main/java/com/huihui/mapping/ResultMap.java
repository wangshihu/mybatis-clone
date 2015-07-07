package com.huihui.mapping;

import java.util.List;

/**
 * Created by hadoop on 2015/7/6 0006.
 */
public class ResultMap {
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;

    public ResultMap( String id,Class<?> type) {
        this.type = type;
        this.id = id;
    }

    public void registryResultMapping(String property,String column){
        ResultMapping mapping = new ResultMapping(property,column);
        resultMappings.add(mapping);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
