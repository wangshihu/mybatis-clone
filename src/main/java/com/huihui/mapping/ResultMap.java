package com.huihui.mapping;

import com.huihui.exceptions.BindingException;
import com.huihui.session.Configuration;
import com.huihui.session.ErrorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/6 0006.
 */
public class ResultMap {
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings = new ArrayList<>();
    private boolean hasNestedMap;

    private ResultMap(){}

    public void registryResultMapping(String property,ResultMapping mapping){
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

    public List<ResultMapping> getResultMappings() {
        return resultMappings;
    }
    public boolean isHasNestedMap() {
        return hasNestedMap;
    }

    public void hasNestedMap() {
        this.hasNestedMap = true;
    }

    public static class Builder{
        private ResultMap resultMap = new ResultMap();
        public Builder(Configuration configuration,String id,String type){
            if(id==null)
                throw new BindingException(ErrorContext.instance()+" resultMap id connot be null");
            Class<?> clazz = configuration.getAliasClass(type);
            if(clazz==null)
                throw new BindingException(ErrorContext.instance()+" "+type+" cannot be found");
        }
        public ResultMap build(){
            return resultMap;
        }
    }
}
