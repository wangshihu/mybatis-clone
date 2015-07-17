package com.huihui.mapping;

import com.huihui.mapping.sql.BoundSql;
import com.huihui.mapping.sql.SqlCommandType;
import com.huihui.mapping.sql.node.SqlSource;
import com.huihui.session.DynamincContext;

/**
 * Created by hadoop on 2015/7/1 0001.
 */
public class MappedStatement {
    SqlCommandType type;
    Class<?> parameterType;
    Class<?> resultType;
    SqlSource sqlSource;

    String id;

    ResultMap resultMap;

    boolean isUseResultMap;
    boolean isUseResultType;



    public MappedStatement() {
    }

    public MappedStatement(SqlCommandType type, Class<?> parameterType, SqlSource sqlSource, String id) {
        this.type = type;
        this.parameterType = parameterType;
        this.sqlSource = sqlSource;
        this.id = id;
    }

    public SqlCommandType getType() {
        return type;
    }

    public void setType(SqlCommandType type) {
        this.type = type;
    }


    public BoundSql getBoudSQL(DynamincContext context) {

      return  sqlSource.parsing(context);


    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public ResultMap getResultMap() {
        return resultMap;
    }

    public void setResultMap(ResultMap resultMap) {
        isUseResultMap=true;
        isUseResultType=false;
        this.resultMap = resultMap;
    }
    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        isUseResultMap=false;
        isUseResultType=true;
        this.resultType = resultType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public boolean isUseResultType() {
        return isUseResultType;
    }

    public void setIsUseResultType(boolean isUseResultType) {
        this.isUseResultType = isUseResultType;
    }

    public boolean isUseResultMap() {
        return isUseResultMap;
    }

    public void setIsUseResultMap(boolean isUseResultMap) {
        this.isUseResultMap = isUseResultMap;
    }
}
