package com.huihui.mapping;

/**
 * Created by hadoop on 2015/7/7 0007.
 */
public class ResultMapping {
    private String property;
    private String colum;

    public ResultMapping(String property, String colum) {
        this.property = property;
        this.colum = colum;
    }

    public String getProperty() {
        return property;
    }

    public String getColum() {
        return colum;
    }
}
