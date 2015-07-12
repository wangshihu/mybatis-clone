package com.huihui.mapping;

import com.huihui.exceptions.BindingException;
import com.huihui.session.ErrorContext;

/**
 * Created by hadoop on 2015/7/7 0007.
 */
public class ResultMapping {
    private String property;
    private String colum;
    private String nestedMapId;

   private ResultMapping(){}



    public static class Builder{
        private ResultMapping resultMapping = new ResultMapping();

        public Builder(String property, String column, ResultMap nestedResultMap){
            resultMapping.setColum(column);
            resultMapping.setProperty(property);
            resultMapping.setNestedMapId(nestedResultMap.getId());
            if(property ==null||column==null)
                throw new BindingException(ErrorContext.instance()+" resultMap colum or property cannot be null ");
        }
        public ResultMapping builder(){
            return resultMapping;
        }

    }

    public String getProperty() {
        return property;
    }

    public String getColum() {
        return colum;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setColum(String colum) {
        this.colum = colum;
    }

    public String getNestedMapId() {
        return nestedMapId;
    }

    public void setNestedMapId(String nestedMapId) {
        this.nestedMapId = nestedMapId;
    }
}
