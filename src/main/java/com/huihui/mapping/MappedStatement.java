package com.huihui.mapping;

/**
 * Created by hadoop on 2015/7/1 0001.
 */
public class MappedStatement {
    SqlCommandType type;
    Class<?> parameterType;
    Class<?> resultType;
    BoundSQLSource boudSQL;
    String id;

    public MappedStatement() {
    }

    public MappedStatement(SqlCommandType type, Class<?> parameterType, Class<?> resultType, BoundSQLSource boudSQL, String id) {
        this.type = type;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.boudSQL = boudSQL;
        this.id = id;
    }

    public SqlCommandType getType() {
        return type;
    }

    public void setType(SqlCommandType type) {
        this.type = type;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public BoundSQLSource getBoudSQL() {
        return boudSQL;
    }

    public void setBoudSQL(BoundSQLSource boudSQL) {
        this.boudSQL = boudSQL;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
