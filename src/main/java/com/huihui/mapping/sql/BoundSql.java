package com.huihui.mapping.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/1 0001.
 */
public class BoundSql {
    private String sql ;
    private List<String> parameterList = new ArrayList<>();

    public BoundSql() {
    }

    public BoundSql(String sql) {
        this.sql = sql;
    }

    public void addParameter(String propertyName){
        parameterList.add(propertyName);
    }

    public List<String> getParameterList() {
        return parameterList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
